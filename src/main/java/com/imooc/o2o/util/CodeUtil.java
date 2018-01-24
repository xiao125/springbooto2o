package com.imooc.o2o.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/12/3.
 */
public class CodeUtil {

    /**
     * 检查验证码是否和预期相符
     * @param request
     * @return
     */
    public static boolean checkVerifyCode(HttpServletRequest request){

        //获取验证码api生成的值
        String verifyCodeExpected = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);

        //获取前端传过来的验证码对比
        String verifyCodeActual = HttpServletRequestUtil.getString(request,"verifyCodeActual");
        if (verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)){
            return false;
        }
        return true;
    }
}
