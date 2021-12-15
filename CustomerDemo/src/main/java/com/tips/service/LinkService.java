package com.tips.service;

import com.tips.entity.Link;

public interface LinkService {

    String save(Link link);

    String restoreUrl(String url);

}