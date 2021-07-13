package com.xxxx.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.wiki.domain.Ebook;
import com.xxxx.wiki.domain.EbookExample;
import com.xxxx.wiki.mapper.EbookMapper;
import com.xxxx.wiki.req.EbookReq;
import com.xxxx.wiki.resp.EbookResp;
import com.xxxx.wiki.resp.PageResp;
import com.xxxx.wiki.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * Created by stfu
 * 2021-07-08 16:30
 */
@Service
public class EbookService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Autowired(required = false)
    private EbookMapper ebookMapper;

    public PageResp<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        // PageHelper分页插件
        // 这句话这对第一个遇到的select起作用
        PageHelper.startPage(req.getPage(),req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        // 获取总行数
        LOG.info("总行数，{}", pageInfo.getTotal());
        // 获取总页数
        LOG.info("总页数，{}", pageInfo.getPages());

//        List<EbookResp> respList = new ArrayList<>();
//        //  iter （live template）
//        for (Ebook ebook : ebookList) {
//            EbookResp ebookResp = new EbookResp();
//            //  Spring提供的工具类
//            BeanUtils.copyProperties(ebook, ebookResp);
//            respList.add(ebookResp);
//        }
//
//        上面这段代码可以用下面这段代码代替

        // 列表复制
        List<EbookResp> respList = CopyUtil.copyList(ebookList, EbookResp.class);

        PageResp<EbookResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }
}
