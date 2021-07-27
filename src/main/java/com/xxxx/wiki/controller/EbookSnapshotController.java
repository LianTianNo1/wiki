package com.xxxx.wiki.controller;

import com.xxxx.wiki.resp.CommonResp;
import com.xxxx.wiki.resp.StatisticResp;
import com.xxxx.wiki.service.EbookSnapshotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by stfu
 * 2021-07-27 11:40
 */
@RestController
@RequestMapping("/ebook-snapshot")
public class EbookSnapshotController {

    @Resource
    private EbookSnapshotService ebookSnapshotService;

    @GetMapping("/get-statistic")
    public CommonResp getStatistic() {
        List<StatisticResp> statisticResp = ebookSnapshotService.getStatistic();
        CommonResp<List<StatisticResp>> commonResp = new CommonResp<List<StatisticResp>>();
        commonResp.setContent(statisticResp);
        return commonResp;
    }

}
