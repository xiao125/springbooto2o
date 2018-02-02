$(function () {

    var productName ='';
    getProductSellDailyList();

    getList();
    function getList() {
        //获取用户购买信息的URL
        var listUrl ='/o2oMaven/shopadmin/listuserproductmapsbyshop?pageIndex=1&pageSize=9999&productName='
            + productName;



    }

})