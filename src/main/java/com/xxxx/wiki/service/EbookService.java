package com.xxxx.wiki.service;

import com.xxxx.wiki.domain.Ebook;
import com.xxxx.wiki.mapper.EbookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by stfu
 * 2021-07-08 16:30
 */
@Service
public class EbookService {

    @Autowired(required = false)
    private EbookMapper ebookMapper;

    public List<Ebook> list() {
        return ebookMapper.selectByExample(null);
    }
}
