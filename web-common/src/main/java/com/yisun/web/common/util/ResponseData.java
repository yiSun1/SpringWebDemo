package com.yisun.web.common.util;

/**
 * Created with IntelliJ IDEA.
 * User: zhangqy10
 * Date: 15-7-17
 * Time: 下午2:08
 * To change this template use File | Settings | File Templates.
 */
public class ResponseData {
    private String code;
//    private String cid;
    private String msg;
    private Object data;

    private boolean success;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
