
package com.tips.controller;

import com.tips.entity.Link;
import com.tips.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class LinkController {

    @Autowired
    private LinkService linkService;

    /**
     * Генерация коротких ссылок
     * @param url
     * @return Caron
     */
    @RequestMapping("/api")
    @ResponseBody
    public Object save(String url){

        if (url == null || "".equals(url)){
            return null;
        }
        if(url.startsWith("http://") || url.startsWith("https://")){
            Link link = new Link();
            link.setLongUrl(url);
            return  linkService.save(link);
        }else{
            return "URL должен начинаться с http или https";
        }
    }

    /**
     * 301 прыжок
     * @param url
     * @return
     */
    @RequestMapping("/{url}")
    public String restoreUrl(@PathVariable("url") String url){

        String restoreUrl = linkService.restoreUrl("http://cni.tips/"+url);

        if(restoreUrl != null && !"".equals(restoreUrl)){
            return "redirect:"+restoreUrl;
        }else{
            return "redirect:http://www.cnilink.com";
            // return "forward: /404.html"; // Если вы хотите получить доступ к локальному html, @RequestMapping ("/ {url}") должен добавить слой пути / aa / {url}
        }

    }

}