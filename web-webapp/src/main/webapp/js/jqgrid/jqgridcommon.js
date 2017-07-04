/**
 * Created by rongss on 15-1-4.
 */


var time = "0";
var id= "";

/**
 * jqGrid表格
 * @param colNames 列名称
 * @param colModel 表格内容
 * @param url 服务器地址
 * @param tableID 添加表格table的id
 * @param captionName 表头名称
 * @param paperID 工具栏div的id
 * @param gridComplete 操作（如果没有操作选项，可以传null）
 * @param autowidth，shrinkToFit都为true，表示列整体宽度固定100%，都为false，表示整体宽度不固定
 */
function gridUI(colNames,colModel,url,tableID,captionName,paperID,gridComplete,autowidth,shrinkToFit){

    $("#"+tableID).jqGrid({   //myTab:装在数据的table id
        sortable: true,//支持鼠标拖动列排序
        datatype: "json", //将这里改为使用JSON数据
        url:url, //这是数据的请求地址
        colNames:colNames,
        colModel:colModel,
        gridComplete: gridComplete,
        autowidth: autowidth,
        shrinkToFit:shrinkToFit,
        height:"100%",
        pgfirst : "首页",
        pglast : "末页",
        pgnext : "下一页",
        pgprev : "上一页",
        sortname: 'id',
        recordtext: "{0} - {1}\u3000共 {2} 条数据",
        pager: "#"+paperID, //分页工具栏，pager:分页DIV的id
        rowNum:20, //每页显示记录数
        viewrecords: true, //是否显示行数
        rowList:[10,20,30,60], //可调整每页显示的记录数
        multiselect: true, //是否支持多选
        caption: captionName,
        altRows:true,//设置斑马
        altclass:'i-altclass'
    }).navGrid("#"+paperID, { add: false, edit: false, del: false,search:false,refresh:true }
    );
    resizing(grid_width);
}

function linkItem(cellvalue, options, rowObject,url,name){
    return "<a href="+url+"?id=" + rowObject.id+"&"+name+"="+cellvalue+"' style='cursor:pointer;text-decoration:underline;color:blue'>"+cellvalue+"</a>";

}

function selectAll(tableID,selectflag){
    var ids = $("#"+tableID).jqGrid("getDataIDs");
    //设置全选
    if(!selectflag){
        selectflag=true;
        $("#"+tableID).jqGrid("resetSelection");
        for(var i=0;i<ids.length;i++){
            $("#"+tableID).jqGrid("setSelection",ids[i]);
        }
    }else{
        selectflag=false;
        $("#"+tableID).trigger("reloadGrid");
    }
    return selectflag;
    //用ajax在后台全部选中数据
}

/**
 * 删除
 * @param id 数据的ID
 * @param strDel 删除服务器地址
 * @param tableID 必须项，否则无法刷新表格
 */
function dele(id,strDel,tableID){
    confirmMessage("确定要删除吗？\n（删除后数据不可恢复）",function(data){
        if(data){
            var urlDel = strDel+"?id="+id;
            var type= "GET";
            ajaxfn(type,urlDel,tableID);
        }
    });
}

/**
 * ajax提交删除
 * @param type
 * @param urlDel
 * @param tableID
 */
function ajaxfn(type,urlDel,tableID){

    $.ajax({
        type: type,
        url:urlDel,
        dataType: "text",
        error: function(data) {
            alertMessage("数据源连接失败！", '提示');
        },
        success: function(data) {
            jQuery("#"+tableID).trigger('reloadGrid');
            if(data !="success"){
                alertMessage(data, '提示');
            }
        }
    });
}

/**
 * 表单验证
 * @param formID
 * @param divID
 * @param tableID
 * @param errormsgID
 */
function valid(formID,divID,tableID,errormsgID){
    var formvalid = $("#"+formID).Validform({
        tiptype:function(msg,o,cssctl){
            if(!o.obj.is("form")){//验证表单元素时o.obj为该表单元素，全部验证通过提交表单时o.obj为该表单对象;
                var objtip=o.obj.parents("td").next().find(".Validform_checktip");
                cssctl(objtip,o.type);
                objtip.text(msg);

                var infoObj=o.obj.parents("td").next().find(".info");
                if(o.type==2){
                    infoObj.fadeOut(200);

                }else{
                    if(infoObj.is(":visible")){return;}
                    var left=o.obj.offset().left,
                        top=o.obj.offset().top;

                    infoObj.css({
                        left:left+100,
                        top:top-45
                    }).show().animate({
                            top:top+18
                        },200);
                }

            }
        },
        usePlugin:{
            passwordstrength:{
                minLen:6,
                maxLen:18,
                trigger:function(obj,error){
                    if(error){
                        obj.parents("td").next().find(".passwordStrength").hide();
                    }else{
                        obj.parents("td").next().find(".info").hide();
                        obj.parents("td").next().find(".passwordStrength").show();
                    }
                }
            }
        },
        ajaxPost:true,
        callback:function(data){
            if(data.status=="N"){
                var tip=$("#"+errormsgID);
                tip.text(data.info);
            }else{
                parent.window.closeUrlDlgAndRefresh(divID,tableID);
            }
        }

    });
    return formvalid;
}
