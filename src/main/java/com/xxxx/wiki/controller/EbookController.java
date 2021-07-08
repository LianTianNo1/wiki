package com.xxxx.wiki.controller;

import com.xxxx.wiki.domain.Ebook;
import com.xxxx.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public List<Ebook> list() {
        return ebookService.list();
    }
}
