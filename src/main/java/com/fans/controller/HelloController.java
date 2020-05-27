package com.fans.controller;

import com.fans.annotation.Encrypt;
import com.fans.common.*;
import com.fans.pojo.User;
import com.fans.service.interfaces.IUserService;
import com.fans.service.interfaces.SysCacheService;
import com.fans.singleton.factory.LocalCacheProxyFactory;
import com.fans.singleton.proxy.LocalCacheProxy;
import com.fans.threadpool.basic.PoolRegister;
import com.fans.threadpool.eventBean.MessageBean;
import com.fans.threadpool.eventBean.PayBean;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;

import static com.fans.common.CommonConstants.LOGIN_MAP;

/**
 * className: HelloController
 *
 * @author k
 * @version 1.0
 * @description 控制层AOP
 * @date 2018-12-20 14:14
 **/
@Controller
@Api(value = "helloController", tags = "用户服务层")
@Slf4j
public class HelloController {
    @Resource(name = "iUserService")
    private IUserService userService;
    @Resource(name = "configProperties")
    private ConfigProperties configProperties;
    @Resource(name = "sysCacheService")
    private SysCacheService cacheService;
    @Resource(name = "poolRegister")
    private PoolRegister<MessageBean> messageBeanPool;
    @Resource(name = "poolRegister")
    private PoolRegister<PayBean> payBeanPool;

    private LocalCacheProxy instance = LocalCacheProxyFactory.getLocalCacheProxy();

    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login.do", method = RequestMethod.GET)
    @ResponseBody
    public JsonData<String> login() {
        instance.put(instance.TOKEN_PREFIX.concat("username"), "111111");
        PayBean payBean = PayBean.builder()
                .productId(1L)
                .orderNo(1L)
                .price(200)
                .productName("雪糕")
                .createTime(DateTime.now().toDate())
                .build();
        MessageBean messageBean = MessageBean.builder()
                .name("范凯")
                .age(18)
                .sex("男")
                .build();
        messageBeanPool.executeHandler(messageBean);
        payBeanPool.peek(payBean);
        payBeanPool.executeHandler(payBean);
        //thymeleaf 模板用 去除@ResponseBody 将返回值改为 String 返回网页名称
        //ModelMap modelMap = new ModelMap();
        //modelMap.addAttribute("host", configProperties.getHost());
        cacheService.saveCache(CacheKeyConstants.KAPOK, configProperties.getHost(), 0, "1");
        cacheService.saveCache(CacheKeyConstants.KAPOK, configProperties.getHost(), 0, "1");
        cacheService.saveCache(CacheKeyConstants.KAPOK, configProperties.getHost(), 0, "2");
        cacheService.saveCache(CacheKeyConstants.KAPOK, configProperties.getHost(), 0, "2");
        //1. session管理开始
        WebInitialize webInitialize = new WebInitialize();
        HttpSession session = webInitialize.getSession();
        ServletContext servletContext = session.getServletContext();
        //session用户管理容器
        Map<String, Object> loginMap = (Map<String, Object>) servletContext.getAttribute(LOGIN_MAP);
        if (loginMap == null || loginMap.size() == 0) {
            //初始化用户容器
            loginMap = Maps.newHashMap();
        }
        //2. 开始登陆逻辑 .....
        //3. 验证是否异地登陆
        for (String sessionKey : loginMap.keySet()) {
            if (session.getId().equals(loginMap.get(sessionKey))) {
                // 相同ip在同一地点重复登录即同一浏览器（一般直接走第四步 正常登陆逻辑）
                log.info("--> 相同ip在同一地点重复登录即同一浏览器");
            } else {
                // 相同ip异地已登录即不同浏览器，（挤掉已登录）
                loginAction(session, servletContext, loginMap, sessionKey);
                log.info("--> 挤掉已登录的相同用户");
            }
        }
        //4. 验证通过后正常的登陆
        loginAction(session, servletContext, loginMap, CommonConstants.CURRENT_USER);
        return JsonData.success("登录成功！！！");
    }

    @RequestMapping(value = "/get.do", method = RequestMethod.GET)
    @ResponseBody
    public JsonData<Object> get() {
        instance.refreshAll();
        Object localCache = instance.get(instance.TOKEN_PREFIX.concat("username"));
        System.out.println(instance.getSize());
        if (localCache == null) {
            return JsonData.fail("获取失败，过期");
        }
        return JsonData.success("获取成功", localCache);
    }

    /**
     * description: 登录成功后的会话和application管理
     *
     * @param session        会话
     * @param servletContext 服务上下文
     * @param loginMap       登录信息
     * @param sessionKey     会话key
     * @author k
     * @date 2019/03/29 16:32
     **/
    private void loginAction(HttpSession session, ServletContext servletContext, Map<String, Object> loginMap, String sessionKey) {
        String sid = session.getId();
        //存储会话
        session.setAttribute(sessionKey, sid);
        //设置会话过期时间s
        session.setMaxInactiveInterval(1800);
        //此处以一个用户 CURRENT_USER 作案例 实际开发 key应该为用户的唯一标识
        loginMap.put(sessionKey, sid);
        servletContext.setAttribute(LOGIN_MAP, loginMap);
        //此处可扩展逻辑 比如存储cookie
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
