package com.xxxx.wiki.controller;

import com.xxxx.wiki.req.EbookQueryReq;
import com.xxxx.wiki.req.EbookSaveReq;
import com.xxxx.wiki.resp.CommonResp;
import com.xxxx.wiki.resp.EbookQueryResp;
import com.xxxx.wiki.resp.PageResp;
import com.xxxx.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by stfu
 * 2021-07-08 16:32
 */
@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(EbookQueryReq req) {
        PageResp<EbookQueryResp> list = ebookService.list(req);
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
    resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq req) {
        ebookService.save(req);
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        return resp;
    }
}
