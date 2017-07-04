$(document).ready(function () {
    $("#button-order").button();
//搜索事件

    $("#button-order").click(function () {

        var orderCode = $("#orderCode-s").val().replace(/\s/g,"");
        if (!orderCode && typeof(orderCode)!="undefined"){
            alert("订单号不能为空！");
            return;
        }
        var siginKey = $("#siginKey-s").val().replace(/\s/g,"");
        if (!siginKey && typeof(siginKey)!="undefined"){
            alert("key不能为空！");
            return;
        }
        $.ajax({
            type:"get",
            url:"/order/queryOPData.jhtm",
            dataType:"json",
            cache:false,
            data:{
                orderCode:orderCode,
                mainFlag:$("#mainFlag-s option:selected").val(),
                payType:$("#payType-s option:selected").val(),
                payTime:$("#payTime-s").val(),
                transactionId:$("#transactionId-s").val().replace(/\s/g,""),
                signkey:siginKey
            },
            success:function(data){
                window.alertMessage(data.msg);
            },
            error:function(){
                window.alertMessage("请求失败，请稍后再试");
            }
        });
    });
});

function CurentTime() {
    var now = new Date();
    now.setDate(now.getDate()-1);
    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();            //日

    var hh = now.getHours();            //时
    var mm = now.getMinutes();          //分
    var ss = now.getSeconds();          //分
    var clock = year + "-";
    if (month < 10)
        clock += "0";
        clock += month + "-";
    if (day < 10)
        clock += "0";
        clock += day + " ";
    if(hh < 10)
     clock += "0";
     clock += hh + ":";
     if (mm < 10) clock += '0';
     clock += mm;
    clock +=":"+ss;
    return(clock);
}
//select 通过Text，获得Value
function getSelectValueByText(selectCtl,optionText){
    var valueOfSelect="";
    selectCtl.each(function(){
        if($(this).text() == optionText){
            valueOfSelect=$(this).val()
            return false;
        }
    });
    return valueOfSelect;
}
