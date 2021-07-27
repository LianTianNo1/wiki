package com.xxxx.wiki.mapper;

import com.xxxx.wiki.resp.StatisticResp;

import java.util.List;

/**
 * Created by stfu
 * 2021-07-27 11:22
 */
public interface EbookSnapshotMapperCust {

    void genSnapshot();

    List<StatisticResp> getStatistic();

    List<StatisticResp> get30Statistic();
}
