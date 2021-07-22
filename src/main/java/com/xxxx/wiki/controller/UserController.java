package com.xxxx.wiki.controller;

import com.xxxx.wiki.req.UserQueryReq;
import com.xxxx.wiki.req.UserResetPasswordReq;
import com.xxxx.wiki.req.UserSaveReq;
import com.xxxx.wiki.resp.CommonResp;
import com.xxxx.wiki.resp.PageResp;
import com.xxxx.wiki.resp.UserQueryResp;
import com.xxxx.wiki.service.UserService;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by stfu
 * 2021-07-08 16:32
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

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
}
