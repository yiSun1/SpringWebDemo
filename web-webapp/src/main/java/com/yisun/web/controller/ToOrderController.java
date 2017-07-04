package com.yisun.web.controller;

import com.yisun.web.common.MD5;
import com.yisun.web.common.util.JacksonUtil;
import com.yisun.web.common.util.ResponseData;
import com.yisun.web.domain.RemoteResult;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhxs  on 2016/8/16.
 */
@Controller
@Scope("prototype")
public class ToOrderController {
    private static Logger logger = Logger.getLogger(ToOrderController.class);
    public static final String signKey = "pay.lenovo.toPay.order";
    public static final String signKeyDb = "pay.lenovo.toPay.order.db";

//    @Autowired
//    private ChannelPayCenterService payMainService;
//    @Autowired
//    private ChannelPayCheckService paySubService;

    @RequestMapping("order/queryOP")
     public String queryOP(HttpServletRequest request, HttpServletResponse response) {
//        String signkeyPage=request.getParameter("signkey");
//        if(StringUtils.isBlank(signkeyPage)){
//            return "403";
//        }
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMM");
        String day=dateFormat.format(new Date());
        Map<String,Object> keyMap=new HashMap<String, Object>();
        keyMap.put("singnKey",signKey);
        keyMap.put("day",day);
        String getKey=getSign(keyMap,signKey);
//        if(!getKey.equals(signkeyPage)){
//            return "403";
//        }

        Map<String,String> mainFlag=new HashMap<String, String>();
        mainFlag.put("1","主单");
        mainFlag.put("0","子单");
        Map<String,String> orderStatus=new HashMap<String, String>();
        orderStatus.put("0","正常");
        orderStatus.put("1","取消");
        Map<String,String> payStatus=new HashMap<String, String>();
        payStatus.put("0","未支付");
        payStatus.put("1","已支付");

        Map<String,String> payType=new HashMap<String, String>();
        payType.put("0","招行");
        payType.put("24","招行分期");
        payType.put("1","支付宝");
        payType.put("16","花呗分期");
        payType.put("9","微信");
        request.setAttribute("payType", payType);
        request.setAttribute("mainFlag", mainFlag);
        request.setAttribute("orderStatus", orderStatus);
        request.setAttribute("payStatus", payStatus);
        return "pay/queryOP";
    }

    @RequestMapping("order/queryOrderToPay")
    public String queryOrderToPay(HttpServletRequest request, HttpServletResponse response) {
        return "pay/queryOrderToPay";
    }

    @RequestMapping("order/queryOrderToPayQuery")
    public String queryOrderToPayQuery(HttpServletRequest request, HttpServletResponse response) {
        ResponseData responseData=new ResponseData();
        String orderCode=request.getParameter("orderCode");
        String lenovoId=request.getParameter("lenovoId");
        String shopId=request.getParameter("shopId");
        String terminal=request.getParameter("terminal");
        if(StringUtils.isBlank(orderCode) || !orderCode.matches("^[0-9]*$")){
            responseData.setSuccess(false);
            responseData.setMsg("订单错误！");
            return JacksonUtil.toJson(responseData);
        }
        if(StringUtils.isBlank(lenovoId) || !lenovoId.matches("^[0-9]*$")){
            responseData.setSuccess(false);
            responseData.setMsg("lenovoId错误！");
            return JacksonUtil.toJson(responseData);
        }
        if(StringUtils.isBlank(shopId) || !shopId.matches("^[0-9]*$")){
            responseData.setSuccess(false);
            responseData.setMsg("shopId错误！");
            return JacksonUtil.toJson(responseData);
        }
        if(StringUtils.isBlank(terminal) || !terminal.matches("^[0-9]*$")){
            responseData.setSuccess(false);
            responseData.setMsg("terminal错误！");
            return JacksonUtil.toJson(responseData);
        }

//        List<String> orderCodeList=new ArrayList<String>();
//        orderCodeList.add(orderCode);
//        com.yisun.web.domain.RemoteResult<List<OrderVo>> rs = null;
//        rs= payMainService.queryOrderMainDetail(orderCodeList,shopId,terminal,lenovoId);
        return "";
    }

    @RequestMapping("order/queryOPData")
    @ResponseBody
    public String queryOPData(HttpServletRequest request, HttpServletResponse response,Map formData) {
        ResponseData responseData=new ResponseData();
        String signkeyPage=request.getParameter("signkey");
        if(StringUtils.isBlank(signkeyPage)){
            return "403";
        }
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMM");
        String day=dateFormat.format(new Date());
        Map<String,Object> keyMap=new HashMap<String, Object>();
        keyMap.put("signKeyDb",signKeyDb);
        keyMap.put("day",day);
        String getKey=getSign(keyMap,signKey);
        if(!getKey.equals(signkeyPage)){
            responseData.setMsg("key err！");
            return JacksonUtil.toJson(responseData);
        }
        String orderCode=request.getParameter("orderCode");
        String mainFlag=request.getParameter("mainFlag");
        String transactionId=request.getParameter("transactionId");
        String payType=request.getParameter("payType");
        String payTime=request.getParameter("payTime");
        Map map=new HashMap();
        if(StringUtils.isBlank(orderCode) || !orderCode.matches("^[0-9]*$")){
            responseData.setSuccess(false);
            responseData.setMsg("订单错误！");
            return JacksonUtil.toJson(responseData);
        }else {
            map.put("orderCode",orderCode.trim());
        }

        if(StringUtils.isNotBlank(mainFlag)&&mainFlag.matches("^[01]?")){
            map.put("mainFlag", mainFlag);
        }else {
            responseData.setSuccess(false);
            responseData.setMsg("订单标识错误！");
            return JacksonUtil.toJson(responseData);
        }

        if(StringUtils.isNotBlank(transactionId) && transactionId.matches("^[0-9]*$")){
            map.put("transactionId",transactionId.trim());
        }else {
            responseData.setSuccess(false);
            responseData.setMsg("流水错误！");
            return JacksonUtil.toJson(responseData);
        }

        if(StringUtils.isNotBlank(payType)&& payType.matches("^[0-9]*$")){
            map.put("payType",payType.trim());
        }else {
            responseData.setSuccess(false);
            responseData.setMsg("支付方式错误！");
            return JacksonUtil.toJson(responseData);
        }

        if(StringUtils.isNotBlank(payTime)&& payTime.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")){
            map.put("payTime",payTime.trim());
        }else {
            responseData.setSuccess(false);
            responseData.setMsg("支付时间错误！");
            return JacksonUtil.toJson(responseData);
        }
        try {
//            RemoteResult<Integer> result=paySubService.updateChannelOrder(map);
            RemoteResult<Integer> result=new RemoteResult<Integer>(false);
            if(result.isSuccess()){
                logger.info("修改成功 参数"+map);
                responseData.setMsg(result.getResultMsg());
            }else {
                logger.info("修改失败 参数"+map+"  错误原因："+result.getResultMsg());
                responseData.setMsg(result.getResultMsg());
            }
        }catch (Exception e){
            logger.info("修改失败 参数"+map+"  错误原因："+e.getMessage());
            responseData.setMsg(e.getMessage());
        }

        return JacksonUtil.toJson(responseData);
    }


    /**
     * 获取验签
     * 方法： 通过参数map字典排序，用通过MD5进行加密
     * @param map 参数map
     * @param signKey  验签key
     * @return
     */
    public String getSign(Map<String, Object> map,String signKey) {
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != "") {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + signKey;
        result = MD5.MD5Encode(result).toUpperCase();
        return result;
    }
}
