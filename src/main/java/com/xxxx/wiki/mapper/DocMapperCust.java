package com.xxxx.wiki.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * Created by stfu
 * 2021-07-23 10:28
 */
public interface DocMapperCust {

    public void increaseViewCount(@Param("id") Long id);

}
