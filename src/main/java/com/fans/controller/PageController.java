package com.fans.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName PageController
 * @Description: 页面跳转
 * @Author fan
 * @Date 2019-04-05 14:15
 * @Version 1.0
 **/
@RestController
public class PageController {

    @RequestMapping(value = "/error/{page}")
    public ModelAndView toErrorPage(@PathVariable String page) {
        return new ModelAndView("/error/" + page);
    }
}
