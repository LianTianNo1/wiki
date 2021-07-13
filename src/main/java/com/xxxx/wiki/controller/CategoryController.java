package com.xxxx.wiki.controller;

import com.xxxx.wiki.req.CategoryQueryReq;
import com.xxxx.wiki.req.CategorySaveReq;
import com.xxxx.wiki.resp.CommonResp;
import com.xxxx.wiki.resp.CategoryQueryResp;
import com.xxxx.wiki.resp.PageResp;
import com.xxxx.wiki.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by stfu
 * 2021-07-08 16:32
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq req) {
        PageResp<CategoryQueryResp> list = categoryService.list(req);
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
    resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid CategorySaveReq req) {
        categoryService.save(req);
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        categoryService.delete(id);
        CommonResp resp = new CommonResp<>();
        return resp;
    }
}
