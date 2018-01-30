$(function () {
    //从URL里获取awardId参数的值
    var awardId = getQueryString('awardId');
    //通过awardId获取奖品信息的URL
    var infoUrl = '/o2oMaven/shopadmin/getawardbyid?awardId='+ awardId;
    //更新奖品信息的URL
    var awardPostUrl ='o2oMaven/shopadmin/modifyaward';
    // 由于奖品添加和编辑使用的是同一个页面，
    // 该标识符用来标明本次是添加还是编辑操作
    var isEdit = false;
    if(awardId){
        //若有awardId则为编辑操作
        getInfo(awardId);
        isEdit = true;
    }else {


    }



    //获取需要编辑的奖品信息，并赋值给表单
    function getInfo(id) {

        $.getJSON(infoUrl,function (data) {
            if(data.success){
                //
                var award = data.award;

            }
        })
    }


});