package com.xxxx.wiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.xxxx.wiki.req.UserLoginReq;
import com.xxxx.wiki.req.UserQueryReq;
import com.xxxx.wiki.req.UserResetPasswordReq;
import com.xxxx.wiki.req.UserSaveReq;
import com.xxxx.wiki.resp.CommonResp;
import com.xxxx.wiki.resp.PageResp;
import com.xxxx.wiki.resp.UserLoginResp;
import com.xxxx.wiki.resp.UserQueryResp;
import com.xxxx.wiki.service.UserService;
import com.xxxx.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

/**
 * Created by stfu
 * 2021-07-08 16:32
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq req) {
        PageResp<UserQueryResp> list = userService.list(req);
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
    resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid UserSaveReq req) {
        //  对明文密码进行加密
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        userService.save(req);
        CommonResp resp = new CommonResp<>();
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        userService.delete(id);
        CommonResp resp = new CommonResp<>();
        return resp;
    }

    @PostMapping("/reset-password")
    public CommonResp resetPassword(@RequestBody @Valid UserResetPasswordReq req) {
        //  对明文密码进行加密
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        userService.resetPassword(req);
        CommonResp resp = new CommonResp<>();
        return resp;
    }

    @PostMapping("/login")
    public CommonResp login(@RequestBody @Valid UserLoginReq req) {
        //  对明文密码进行加密
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        UserLoginResp userLoginResp = userService.login(req);

        //  通过雪花算法生成token
        Long token = snowFlake.nextId();
        LOG.info("生成单点登录token: {}，并放入redis中", token);
        //  将token返回给前端
        userLoginResp.setToken(token.toString());
        //  将token放入redis
        //  存入redis的类需要实现序列化 或者 JSONObject.toJSONString()
        redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResp), 3600 * 24, TimeUnit.SECONDS);

        CommonResp<UserLoginResp> resp = new CommonResp<>();
        resp.setContent(userLoginResp);
        return resp;
    }

    @GetMapping("/logout/{token}")
    public CommonResp logout(@PathVariable String token) {
        redisTemplate.delete(token);
        LOG.info("从redis中删除token: {}", token);
        CommonResp resp = new CommonResp<>();
        return resp;
    }
}
