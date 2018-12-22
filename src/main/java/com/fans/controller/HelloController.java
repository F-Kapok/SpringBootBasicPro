package com.fans.controller;

import com.fans.common.CacheKeyConstants;
import com.fans.common.ConfigProperties;
import com.fans.common.JsonData;
import com.fans.pojo.User;
import com.fans.service.interfaces.IUserService;
import com.fans.service.interfaces.SysCacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
@Api(value = "helloController")
public class HelloController {
    @Resource(name = "iUserService")
    private IUserService userService;
    @Resource(name = "configProperties")
    private ConfigProperties configProperties;
    @Resource(name = "sysCacheService")
    private SysCacheService cacheService;

    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login.do", method = RequestMethod.GET)
    public String index() {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("host", configProperties.getHost());
        cacheService.saveCache(CacheKeyConstants.KAPOK, configProperties.getHost(), 0, "1");
        cacheService.saveCache(CacheKeyConstants.KAPOK, configProperties.getHost(), 0, "1");
        cacheService.saveCache(CacheKeyConstants.KAPOK, configProperties.getHost(), 0, "2");
        cacheService.saveCache(CacheKeyConstants.KAPOK, configProperties.getHost(), 0, "2");
        return "index";
    }

    @ApiOperation(value = "展示用户列表")
    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    @ResponseBody
    public JsonData<List<User>> getList() {
        return JsonData.success("查询成功", userService.getList());
    }

    @RequestMapping(value = "/insert.do", method = RequestMethod.GET)
    @ResponseBody
    public JsonData<String> insertUser(User user) {
        userService.addUser(user);
        return JsonData.success("增加成功");
    }

    @RequestMapping(value = "/delete.do", method = RequestMethod.GET)
    @ResponseBody
    public JsonData<String> deleteUser(@ApiParam(value = "用户ID") Long id) {
        userService.deleteUser(id);
        return JsonData.success("删除成功");
    }
}
