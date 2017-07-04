$(document).ready(function () {
    $("#button-order").button();
//搜索事件
    $("#button-order").click(function () {
        var orderCode = $("#orderCode-s").val().replace(/\s/g,"");
        if (!orderCode && typeof(orderCode)!="undefined"){
            alert("订单号不能为空！");
            return;
        }
        var lenovoId = $("#lenovoId-s").val().replace(/\s/g,"");
        if (!lenovoId && typeof(lenovoId)!="undefined"){
            alert("订单号不能为空！");
            return;
        }
        $.ajax({
            type:"get",
            url:"/order/queryOrderToPayQuery.jhtm",
            dataType:"json",
            cache:false,
            data:{
                orderCode:orderCode,
                lenovoId:lenovoId,
                shopId:$("#shopId-s option:selected").val(),
                terminal:$("#terminal-s option:selected").val()
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