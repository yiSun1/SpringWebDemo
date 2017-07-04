(function (w, $) {
    $.extend({
        getFormData: function (form) {
            var data = {};
            var inputs = $(form + " :input");
            $.each(inputs, function (i, n) {
                data[n.name] = n.value;
            });
            return data;
        }
    });

    $.GLOBAL_CODE_MAP = {
        PAY: {
            L_X: {
                1: '线上支付',
                2: '线下支付'
            },
            STATUS: {
                0: '支付中',
                1: '已支付',
                2: '退款中',
                3: '已退款',
                4: '申请关闭'
            },
            PAYMENT: {
                0: '招商银行',
                1: '支付宝',
                2: '银联',
                4: '佳意堂',
                7: 'Think支付宝',
                8: 'Think招商银行',
                16: '财付通',
                17: '农行分期'
            },PAYTYPE: {
                0: '招商银行',
                1: '支付宝即时支付',
                2: '支付宝手机支付',
                3:'支付宝纯网关支付',
                4: '支付宝合并支付',
                5: '支付宝分期支付',
                6: '支付宝移动快捷支付',
                7: '银联支付',
                9: '微信支付',
                10: '农行分期支付',
                11: '花呗分期',
                100: '已废弃'
            },DWRCSTATE: {
                0: '待审核',
                1: '审核失败',
                2: '审核成功',
                3: '待入账',
                4: '已入账',
                5: '已退回'
            },OS:{
                0: 'eee',
                1:'B2C_WAP',
                2:'B2C_WEChat',
                3:'B2C_APP',
                4:'B2C_PC',
                5:'THINK_WAP',
                6:'THINK_WEChat',
                7:'THINK_APP',
                8:'THINK_PC',
                20:'EPP_WAP',
                22:'EPP_PC'
            },TRADESTATE:{
                0:'失败',
                1:'成功',
                2:'未支付'
            },PROMOTIONTYPE:{
                0: '无优惠'
            },ISVALID:{
                0: '无效',
                1: '有效'
            },VERIFYCODE:{
                0: '花呗分期持卡人自费',
                20035000: '农行分期商户付费',
                20036208: '农行分期持卡人自费',
                100: '花呗分期商户付费'
            }
        },
        PRODUCT: {
            TYPE: {
                0: '主机',
                1: '选件',
                2: '赠品',
                3: '服务'
            }
        },
        DELIVERY: {
            DATE: {
                1: '工作日',
                2: '双休日',
                3: '节假日',
                4: '任意日期'
            }
        },
        MERCHANT: {
            1: '商城',
            2: 'EPP',
            3: '神奇工程',
            4: 'roming',
            5: 'Think'
        },
        FA_TYPE: {
            0: '直营',
            1: '代理',
            2: 'MBG',
            3: 'Think直营',
            4: 'ThinkFA'
        }
    };

    /** 设置jqGrid插件的全局默认值 */
    $.extend($.jgrid.defaults, {
        mtype: 'get',
        datatype: 'json',
        rowNum: 20,
        rowList: [20, 50, 100],
        jsonReader: {
            root: "dateList",
            repeatitems: false
        },
        altRows: true,// 隔行变色
        altclass: 'jqgrid-altclass',// 隔行变色样式
        multiselect: true,
        viewrecords: true,
        height: "auto",
        autowidth: true,
        hidegrid: false,// 禁用控制表格显示、隐藏的按钮
        loadtext: "努力加载中.....",
        rownumbers: true,
        sortorder: "desc",
        sortable: false,
        resizable: false
    });
}(window, jQuery));