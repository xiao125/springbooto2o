package com.imooc.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Administrator on 2017/12/16.
 */
@Controller
@RequestMapping("/frontend")
public class FrontendController {

    /**
     * 首页路由
     * @return
     */
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    private String index(){

        return "frontend/index";
    }

    /**
     * 商品列表页路由
     * @return
     */
    @RequestMapping(value = "/shoplist",method = RequestMethod.GET)
    private String showShopList(){

        return "frontend/shoplist";
    }


    /**
     * 店铺详情页路由
     *
     * @return
     */
    @RequestMapping(value = "/shopdetail",method = RequestMethod.GET)
    private String showShopDetail(){

        return "frontend/shopdetail";
    }


    /**
     * 商品详情页路由
     *
     * @return
     */
    @RequestMapping(value = "/productdetail",method = RequestMethod.GET)
    private String showProductDetail(){
        return "frontend/productdetail";

    }

    /**
     * 店铺的奖品列表页路由
     *
     * @return
     */
    @RequestMapping(value = "/awardlist", method = RequestMethod.GET)
    private String showAwardList() {
        return "frontend/awardlist";
    }



}
