package com.fans.controller;

import com.fans.annotation.Encrypt;
import com.fans.common.*;
import com.fans.pojo.User;
import com.fans.service.interfaces.IUserService;
import com.fans.service.interfaces.SysCacheService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName HelloController
 * @Description:
 * @Author fan
 * @Date 2018-12-19 14:06
 * @Version 1.0
 **/
@Controller
@Api(value = "helloController", tags = "用户服务层")
public class HelloController {
    @Resource(name = "iUserService")
    private IUserService userService;
    @Resource(name = "configProperties")
    private ConfigProperties configProperties;
    @Resource(name = "sysCacheService")
    private SysCacheService cacheService;

    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public String index() {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("host", configProperties.getHost());
        cacheService.saveCache(CacheKeyConstants.KAPOK, configProperties.getHost(), 0, "1");
        cacheService.saveCache(CacheKeyConstants.KAPOK, configProperties.getHost(), 0, "1");
        cacheService.saveCache(CacheKeyConstants.KAPOK, configProperties.getHost(), 0, "2");
        cacheService.saveCache(CacheKeyConstants.KAPOK, configProperties.getHost(), 0, "2");
        User user = User.builder()
                .username("kapok")
                .desc("管理用户")
                .status(0)
                .build();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(1800);
        session.setAttribute(CommonConstants.CURRENT_USER, user);
        return "index";
    }

    @ApiOperation(value = "展示用户列表")
    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    @ResponseBody
    public JsonData<PageInfo<User>> getList() {
        return JsonData.success("查询成功", userService.getList());
    }

    @ApiOperation(value = "新增用户")
    @RequestMapping(value = "/insert.do", method = RequestMethod.POST)
    @ResponseBody
    @Encrypt
    @ApiResponses({
            @ApiResponse(code = 401, message = "需要登录")
    })
    public JsonData<String> insertUser(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonData.fail(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
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
