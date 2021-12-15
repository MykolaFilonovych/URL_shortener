package com.tips.mapper;

import com.tips.entity.Link;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface LinkMapper {

    Link selectByPrimaryKey(Integer id);

    int insert(Link link);

    Link findByLongUrl(String longUrl);

    String findByShortUrl(String shortUrl);
}