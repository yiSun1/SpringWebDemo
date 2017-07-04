$().ready(function(){
	$('.clicktable').delegate('.clickrow','click',function(){
		var id = $(this).find('td:eq(0)').attr('id');
        var out_trade_no = $(this).attr('id');
		this.index = this.index || 0;
		if(this.index%2==0){
			var jsonObj = getJson(out_trade_no);
			addJson(eval(jsonObj.responseText),$(this));
		}else{
			delJson($(this));
		}
		this.index++;
	});
});

function addJson(json,$node){
	var html = '';
    var out_trade_no = "";
	html = get_html(json);
	$node.after(html);
}

function get_html(json){
	var result = '';
	var length = json.length;
//    alert("length>>>>>>>>>>"+length)
	for(var i = 0,j = length;i < j;i++){
		result += joinTable(json[i]);
	}
	return result;
}

function delJson($node){
	var index = $('.clicktable tr.clickrow').index($node);
	var length = $('.clicktable tr.clickrow').length;
	if(index == length-1){//最后一个
		$node.nextAll('.dy').remove();
	}else{
		$node.nextUntil('.clickrow').remove();
	}
}

function getJson(out_trade_no){
    var jsonObj = $.ajax( {
        url:'/getOtherPayOrder.jhtm',
        data:{
            out_trade_no : out_trade_no
        },
        type:'post',
        cache:false,
        async: false,
        dataType:'json',
        success:function(data) {
            if(data.length>0){
                return data;
            }else{
//                    alert("只有一条数据")
            }
        },
        error : function() {
            //            alert("异常！");
        }
    });
    return jsonObj;
}

/*function joinTable(payOrder){
//    alert("payOrder.id>>>>>>>>>>>>>"+payOrder.id)
    var joinTd = "";
    joinTd +=   "<tr class='dy'><td width='7%' id='tdId'>";
    joinTd +=   "<a href='$!homeModule.getTarget("+"'/payOrderFindById.jhtm')>"+"</a>"
   *//* joinTd +=   "";
    joinTd +=   "?payOrderId=";
    joinTd +=   payOrder.id;
    joinTd +=   "'><span>详细</span></a>";*//*
    *//*joinTd +=   "<a href='$!homeModule.getTarget(\'payOrderFindById.jhtm\')?payOrderId="+payOrder.id+"'><span>详细</span></a>";*//*
    joinTd +=   "</td>";
    joinTd +=   "<td width='15%'><span>";
    if(payOrder.os == 1){
        joinTd +=   "B2C_WAP";
    }else if(payOrder.os == 2){
        joinTd +=   "B2C_WEChat";
    }else if(payOrder.os == 3){
        joinTd +=   "       B2C_APP";
    }else if(payOrder.os == 4){
        joinTd +=   "  B2C_PC";
    }else if(payOrder.os == 5){
        joinTd +=   "       THINK_WAP";
    }else if(payOrder.os == 6){
        joinTd +=   "    THINK_WEChat";
    }else if(payOrder.os == 7){
        joinTd +=   "   THINK_APP";
    }else if(payOrder.os == 20){
        joinTd +=   " THINK_PC";
    }else if(payOrder.os == 22){
        joinTd +=   "     EPP_WAP";
    }else if(payOrder.os == 0){
        joinTd +=   "      EPP_PC";
    }
    joinTd += "</span></td>";
    joinTd +=   "<td style='text-align:left' width='25%'><span>";
    if(payOrder.pay_type == 0){
        joinTd +=   "招商银行";
    }else if(payOrder.pay_type == 1){
        joinTd +=   "支付宝即时";
    }else if(payOrder.pay_type == 2){
        joinTd +=   "       支付宝WAP";
    }else if(payOrder.pay_type == 3){
        joinTd +=   "  支付宝纯网关";
    }else if(payOrder.pay_type == 4){
        joinTd +=   "       支付宝合并";
    }else if(payOrder.pay_type == 5){
        joinTd +=   "    支付宝分期";
    }else if(payOrder.pay_type == 6){
        joinTd +=   "   支付宝WAP网银前置";
    }else if(payOrder.pay_type == 7){
        joinTd +=   " 银联";
    }else if(payOrder.pay_type == 9){
        joinTd +=   "    微信";
    }
    joinTd += "</span></td>";
    joinTd += "<td style='text-align:left' width='25%'><span>";
    joinTd += payOrder.out_trade_no;
    joinTd += "</span></td>";
    joinTd += "<td style='text-align:left' width='8%'><span>";
    if(payOrder.trade_state == 1){
        joinTd +=   " 成功";
    }else {
        joinTd +=   "    失败";
    }
    joinTd += "</span></td>";
    joinTd += "<td style='text-align:left' width='15%'><span>";
    joinTd += payOrder.total_fee;
    joinTd += "</span></td>";
    joinTd += "<td style='text-align:left' width='15%'><span>";
    var newTime = new Date(payOrder.create_time);
    if(payOrder.create_time != null){
        joinTd += newTime.Format("yyyy-MM-dd hh:mm:ss");;
    }
    joinTd += "</span></td></tr>";
    return joinTd;
}*/

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
