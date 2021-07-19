package com.xxxx.wiki.controller;

import com.xxxx.wiki.req.EbookQueryReq;
import com.xxxx.wiki.req.EbookSaveReq;
import com.xxxx.wiki.resp.CommonResp;
import com.xxxx.wiki.resp.EbookQueryResp;
import com.xxxx.wiki.resp.PageResp;
import com.xxxx.wiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by stfu
 * 2021-07-08 16:32
 */
@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(@Valid EbookQueryReq req) {
        PageResp<EbookQueryResp> list = ebookService.list(req);
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
    resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid EbookSaveReq req) {
        ebookService.save(req);
        CommonResp resp = new CommonResp<>();
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        ebookService.delete(id);
        CommonResp resp = new CommonResp<>();
        return resp;
    }
}
