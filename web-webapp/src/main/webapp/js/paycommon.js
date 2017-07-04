/**
 * Created by cofco on 14-12-29.
 */

var grid_width = $(window).width() - 4;
//var grid_width_left = grid_width - 165;
//var grid_height = $(window).height() - $("#o-serch").height() - $("#o-breadcrumb").height() - 120;
var grid_height = $(window).height() - 124 - $("#o-serch").height();


function resizing(width) {
    $("#myTab").jqGrid('setGridParam','autowidth',false);
    $("#myTab").jqGrid('setGridWidth',width);
    $("#myTab").jqGrid('setGridHeight', grid_height);
}

/**
 * 提示信息对话框
 * @param message
 */
function alertMessage(message) {
    var divhtml = "<div id='div_alert' title='提示' style='display:none;'>" +message + "</div>";
    $("body").append(divhtml);

    $("#div_alert").dialog({
        autoOpen:true,
        minWidth:150,
        minHeight:150,
        modal:true,
        resizable:true,
        bgiframe:true,
        buttons: {
            "关闭": function() {
                $( this ).dialog( "close" );
            }
        },
        close:function(evt,ui) {
            $("#div_alert").dialog("destroy");
            $("#div_alert").html("").remove();
        }
    });

}

//确认信息对话框的回调函数
var confirmCallback;

/**
 * 确认信息对话框
 * @param message
 * @param callback
 */
function confirmMessage(message, callback) {

    if(callback == null) {
        alertMessage(message);
    } else {
        confirmCallback = callback;


        var divhtml = "<div id='div_confirm' title='提示' style='display:none;'>" + message + "</div>";

        $("body").append(divhtml);
        var div=$("#div_confirm");

        div.dialog({
            autoOpen:true,
            minWidth:150,
            minHeight:150,
            modal:true,
            resizable:true,
            bgiframe:true,
            buttons: {
                "确定": function() {
                    confirmCallback(true);
                    $( this ).dialog( "close" );
                },
                "取消": function() {

                    confirmCallback(false);
                    $( this ).dialog( "close" );
                }
            },
            close:function(evt,ui) {
                div.dialog("destroy");
                $("#div_confirm").html("").remove();
            }
        });
    }

}

/**
 * 打开url对话框
 * @param url
 * @param params
 */
function openUrlDlg(url, params, undestroy) {
    if(undestroy) {
        openSnQueryDlg(url, params);
    } else {
        openDlgDestroy(url, params);
    }
}

function openDlgDestroy(url, params) {
    var iframe = "<iframe src=\'" + url + "\' width='100%' height='" + (params.height-55) + "px' scrolling='auto' frameborder=0 marginheight=0 marginwidth=0></iframe>";
    var divhtml = "<div id='div_" + params.dlgid + "' title='" + params.title + "' style='display:none;padding:5px;'>" + iframe + "</div>";
    $("body").append(divhtml);
    var div=$("#div_" + params.dlgid);
    div.dialog({
        autoOpen:true,
        width:params.width,
        height:params.height,
        modal:true,
        resizable:true,
        bgiframe:true,
        close:function(evt,ui) {
            div.dialog("destroy");
            div.remove();
        }
    });
}
//高级查询对话框，与上面对话框的区别是，在关闭对话框时并不销毁对话框，只是隐藏
function openSnQueryDlg(url, params) {
    var div=$("#div_" + params.dlgid);

    if(div.attr("id") == undefined) {
        var iframe = "<iframe src=\'" + url + "\' width='100%' height='" + (params.height-55) + "px' scrolling='auto' frameborder=0 marginheight=0 marginwidth=0></iframe>";
        var divhtml = "<div id='div_" + params.dlgid + "' title='" + params.title + "' style='display:none;padding:5px;'>" + iframe + "</div>";
        $("body").append(divhtml);
        div = $("#div_" + params.dlgid);
    }
    div.dialog({
        autoOpen:true,
        width:params.width,
        height:params.height,
        modal:true,
        resizable:true,
        bgiframe:true
    });
}

function closeUrlDlg(dlgid) {
    $("#div_" + dlgid).dialog("close");
}

function closeUrlDlgAndRefresh(divID,tableID) {
    $("#div_"+divID).dialog("close");
    jQuery("#"+tableID).trigger('reloadGrid');
}

function ajaxformsubmit(tourl,formId,divID,tableID){
    $.ajax({
        type: "POST",
        url:tourl,
        data:$('#'+formId).serialize(),
        error: function(data) {
            $("#errormsg").html("数据库操作异常！");
        },
        success: function(data) {
            if("success"!=data){
                $("#errormsg").html(data);
            }else{
                parent.window.closeUrlDlgAndRefresh(divID,tableID);
            }
        }
    });
}

function ajaxformsubmit1(tourl,formId,divID,divID1,description){
    $.ajax({
        type: "POST",
        url:tourl,
        data:$('#'+formId).serialize(),
        error: function(data) {
            $("#errormsg").html("数据库操作异常！");
        },
        success: function(data) {
            var datas = data.split("|");
            if("success"!=datas[0]){
                $("#errormsg").html(datas[0]);
            }else{
                var id = datas[1];
                var lidata = "<li id='"+id+"'>"+description+"</li>";
            	parent.window.$("#"+divID1).append(lidata);
                parent.window.resetALL();
                parent.window.closeUrlDlg(divID);
            }
        }
    });
}
//===分页相关===============

var pager_calback; //分页请求成功后的处理函数
var pager_url; //分页请求地址

/**
 * 创建分页
 * @param calback
 */
function setCalback(calback) {
    pager_calback = calback;
}

function setPagerUrl(url) {
    pager_url = url;
}

/**
 * 跳转到传入的页面
 * @param page
 */
function goPage(page) {
    var index = pager_url.indexOf("?");
    var urlP;
    if(index != -1) { //url已经包含参数的情况
        urlP = pager_url + "&page=" + page;
    } else { //url不包含参数
        urlP = pager_url + "?page=" + page;
    }

    $.ajax({
        url:urlP,
        type:"post",
        dataType:"json",
        success:pager_calback
    });
}

/**
 * 跳转到输入框输入的页码页面内
 */
function goInputPage() {
    goPage($("#i-currentPage").val());
}

/**
 * 跳转到首页
 */
function goFirstPage() {
    goPage(1);

}

/**
 * 跳转到上一页
 */
function goPrePage() {
    var currentPate = $("#i-currentPage").val();
    var page = 1;
    if(currentPate > 1) {
        page = parseInt(currentPate) - 1;
    }

    goPage(page);
}

/**
 * 跳转到下一页
 */
function goNextPage() {
    var lastPage = $("#i-totalPage").html();
    var currentPate = $("#i-currentPage").val();
    var page = $("#i-currentPage").val();
    if(currentPate < lastPage) {
        page = parseInt($("#i-currentPage").val()) + 1;
    }

    goPage(page);
}

/**
 * 跳转到末页
 */
function goLastPage() {
    var page = $("#i-totalPage").html();
    goPage(page);
}

/**
 * 表格排序
 * @param tableid
 * @param callback
 */
function tableSort(tableid) {
    $("#" + tableid + " tr th").each(function(){

        $(this).click(function() {
            //排除复选框的情况，复选框无需排序
            var clickObj = $(this).find("input[type=checkbox]").val();
            if(clickObj != undefined) {
                return;
            }

            //第一次点击添加排序图片显示的span
            var spanObj = $(this).find("span").html();
            if(spanObj == undefined) {
                var thHtml = $(this).html() + "<span class='sort_desc'>　</span>";
                $(this).html(thHtml);
            }

            //获取当前点击的span的样式名称
            var cls = $(this).find("span").attr("class");

            //清除所有排序相关的样式
            $(this).parent().find("span").removeClass("sort_desc").removeClass("sort_asc");

            //排序请求的连接
            var newUrl;
            var con;

            var index = pager_url.indexOf("orderstr");
            if(index != -1) {
                pager_url =pager_url.substr(0, index-1);
            }

            if(pager_url.indexOf("?") != -1) {
                con = "&";
//                pager_url = pager_url.replace("&sortStr=desc", "").replace(/orderstr/w*/&, "&");
            } else {
                con = "?";
            }

            //排序字段
            var id = $(this).attr("id");
            newUrl = pager_url + con + "orderstr=" + id;

            //判断是升序还是降序排序，同时替换排序所显示的图片样式
            if(cls != "sort_asc"){
                $(this).find("span").attr("class", "sort_asc");
            } else {
                newUrl = newUrl + "&sortStr=desc";
                $(this).find("span").attr("class", "sort_desc");
            }

            //执行排序请求
            $.ajax({
                url:newUrl,
                type:"post",
                dataType:"json",
                success:pager_calback
            });

            setPagerUrl(newUrl);

        });
    });
}