$(function() {
	var shopId = getQueryString('shopId');
	var shopInfoUrl = '/o2oMaven/shopadmin/getshopmanagementinfo?shopId=' + shopId;
	$.getJSON(shopInfoUrl, function(data) {
		if (data.redirect) {
			window.location.href = data.url;
		} else {
			if (data.shopId != undefined && data.shopId != null) {
				shopId = data.shopId;
			}

			//商铺信息页面
			$('#shopInfo')
					.attr('href','/o2oMaven/shopadmin/shopoperation?shopId=' + shopId);//店铺注册编辑界面
		}
	});
});