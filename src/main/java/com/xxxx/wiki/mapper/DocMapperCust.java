package com.xxxx.wiki.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * Created by stfu
 * 2021-07-23 10:28
 */
public interface DocMapperCust {

    void increaseViewCount(@Param("id") Long id);

    void increaseVoteCount(@Param("id") Long id);
}
