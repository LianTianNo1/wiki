package com.xxxx.wiki.controller;

import com.xxxx.wiki.req.DocQueryReq;
import com.xxxx.wiki.req.DocSaveReq;
import com.xxxx.wiki.resp.CommonResp;
import com.xxxx.wiki.resp.DocQueryResp;
import com.xxxx.wiki.resp.PageResp;
import com.xxxx.wiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * Created by stfu
 * 2021-07-08 16:32
 */
@RestController
@RequestMapping("/doc")
public class DocController {

    @Resource
    private DocService docService;

    @GetMapping("/all/{ebookId}")
    public CommonResp all(@PathVariable Long ebookId) {
        List<DocQueryResp> list = docService.all(ebookId);
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq req) {
        PageResp<DocQueryResp> list = docService.list(req);
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
    resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid DocSaveReq req) {
        docService.save(req);
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        return resp;
    }

    @DeleteMapping("/delete/{idsStr}")
    public CommonResp delete(@PathVariable String idsStr) {
        List<String> list = Arrays.asList(idsStr.split(","));
        docService.delete(list);
        CommonResp resp = new CommonResp<>();
        return resp;
    }

    @GetMapping("/find-content/{id}")
    public CommonResp findContent(@PathVariable Long id) {
        String content = docService.findContent(id);
        CommonResp<String> resp = new CommonResp<>();
        resp.setContent(content);
        return resp;
    }

    @GetMapping("/vote/{id}")
    public CommonResp vote(@PathVariable Long id) {
        docService.vote(id);
        CommonResp commonResp = new CommonResp();
        return commonResp;
    }
}
