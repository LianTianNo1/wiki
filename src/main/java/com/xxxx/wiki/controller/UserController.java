package com.xxxx.wiki.controller;

import com.xxxx.wiki.req.UserQueryReq;
import com.xxxx.wiki.req.UserSaveReq;
import com.xxxx.wiki.resp.CommonResp;
import com.xxxx.wiki.resp.UserQueryResp;
import com.xxxx.wiki.resp.PageResp;
import com.xxxx.wiki.service.UserService;
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
}
