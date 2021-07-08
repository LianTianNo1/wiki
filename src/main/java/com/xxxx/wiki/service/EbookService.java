package com.xxxx.wiki.service;

import com.xxxx.wiki.domain.Ebook;
import com.xxxx.wiki.domain.EbookExample;
import com.xxxx.wiki.mapper.EbookMapper;
import com.xxxx.wiki.req.EbookReq;
import com.xxxx.wiki.resp.EbookResp;
import com.xxxx.wiki.util.CopyUtil;
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

    public List<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + req.getName() + "%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

//        List<EbookResp> respList = new ArrayList<>();
//        //  iter （live template）
//        for (Ebook ebook : ebookList) {
//            EbookResp ebookResp = new EbookResp();
//            //  Spring提供的工具类
//            BeanUtils.copyProperties(ebook, ebookResp);
//            respList.add(ebookResp);
//        }

        List<EbookResp> respList = CopyUtil.copyList(ebookList, EbookResp.class);

        return respList;
    }
}
