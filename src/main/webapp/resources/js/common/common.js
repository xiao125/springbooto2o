/**
 * Created by Administrator on 2017/12/3.
 */
Date.prototype.Format = function(fmt) {
    var o = {
        "M+" : this.getMonth() + 1, // 月份
        "d+" : this.getDate(), // 日
        "h+" : this.getHours(), // 小时
        "m+" : this.getMinutes(), // 分
        "s+" : this.getSeconds(), // 秒
        "q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
        "S" : this.getMilliseconds()
        // 毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    for ( var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
                : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

//随机生成四位数验证码
function changeVerifyCode(img) {
    img.src ="../Kaptcha?"+ Math.floor(Math.random()*100);
}

//识别url中的是否传入shopId参数
// 例如：http://127.0.0.1:8080/o2oMaven/shopadmin/shopoperation?shopId=1
function getQueryString(name) {

    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]);
    }
    return '';
}

/**
 * 获取项目的ContextPath以便修正图片路由让其正常显示
 * @returns {string}
 */
function getContextPath() {
    return "/o2oMaven/";
}