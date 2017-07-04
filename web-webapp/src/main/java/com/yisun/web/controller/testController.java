package com.yisun.web.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangxs7 on 2017/7/4.
 */
@Controller
@Scope("prototype")
public class testController {

    @RequestMapping("hello")
    @ResponseBody
    public String hello(HttpServletRequest request, HttpServletResponse response){
        return "hello";
    }

}
