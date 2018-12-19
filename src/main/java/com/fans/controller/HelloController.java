package com.fans.controller;

import com.fans.common.JsonData;
import com.fans.pojo.User;
import com.fans.service.interfaces.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName HelloController
 * @Description: TODO
 * @Author fan
 * @Date 2018-12-19 14:06
 * @Version 1.0
 **/
@Controller
public class HelloController {
    @Resource
    private IUserService userService;

    @RequestMapping("/")
    public String index(ModelMap modelMap) {

        modelMap.addAttribute("host", "http://blog.didispace.com");

        return "index";
    }

    @RequestMapping("/list.do")
    @ResponseBody
    public JsonData<List<User>> getList() {
        return JsonData.success("查询成功", userService.getList());
    }
}
