package com.imooc.o2o.web.superadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/superadmin", method = { RequestMethod.GET, RequestMethod.POST })
public class SuperAdminController {

	@RequestMapping(value = "/areamanagement", method = RequestMethod.GET)
	private String areamanagementment() {
		// 区域管理页
		return "superadmin/areamanagement";
	}

	@RequestMapping(value = "/headlinemanagement", method = RequestMethod.GET)
	private String headLinemanagementment() {
		// 头条管理页
		return "superadmin/headlinemanagement";
	}

	@RequestMapping(value = "/shopcategorymanagement", method = RequestMethod.GET)
	private String shopCategorymanagement() {
		// 店铺类别管理页
		return "superadmin/shopcategorymanagement";
	}

	@RequestMapping(value = "/shopmanagement", method = RequestMethod.GET)
	private String shopmanagement() {
		// 店铺管理页
		return "superadmin/shopmanagement";
	}

	@RequestMapping(value = "/personinfomanagement", method = RequestMethod.GET)
	private String personInfomanagement() {
		// 用户信息管理页
		return "superadmin/personinfomanagement";
	}

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	private String main() {
		// 超级管理员主页
		return "superadmin/main";
	}

	@RequestMapping(value = "/top", method = RequestMethod.GET)
	private String top() {
		// 超级管理员frame top部分
		return "superadmin/top";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	private String login() {
		// 超级管理员登录页
		return "superadmin/login";
	}

}
