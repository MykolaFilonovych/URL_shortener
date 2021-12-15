package com.tips.service.impl;

import com.tips.entity.Link;
import com.tips.mapper.LinkMapper;
import com.tips.service.LinkService;
import com.tips.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkServiceImpl  implements LinkService{

    //@Autowired
    private LinkMapper linkMapper;

    /**
     * Конвертировать URL
     * Запрос, существует ли база данных. Если она существует, вернуть короткую ссылку, соответствующую базе данных. Если она не существует, запросить новую запись и вернуть новую короткую ссылку
     * @param link
     * @return
     */
    @Override
    public String save(Link link) {
        String shortUrl = "http://127.0.0.1/";
        String longUrl = link.getLongUrl();
        System.out.println(longUrl);
        Link link1 = linkMapper.findByLongUrl(longUrl);
        if(link1 == null) {
            shortUrl += this.gererateShortUrl(longUrl);
            link.setShortUrl(shortUrl);
            linkMapper.insert(link);
        }else{
            shortUrl = link1.getShortUrl();
        }
        return shortUrl;
    }

    @Override
    public String restoreUrl(String url) {
        return linkMapper.findByShortUrl(url);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        String sLongUrl = "http://474515923.qzone.qq.com" ; // Длинная ссылка
        LinkServiceImpl link = new LinkServiceImpl();
        String result = link.gererateShortUrl(sLongUrl);
        // распечатать результат
        System.out.println("Короткая ссылка:"+ result);
    }

    /**
     * Конвертировать длинные ссылки в короткие ссылки
     * @param url
     * @return
     */
    public String gererateShortUrl(String url) {
        // Его можно настроить для генерации смешанного ключа перед передачей зашифрованного MD5
        String key = "caron" ;
        // Чтобы использовать символы, которые генерируют URL
        String[] chars = new String[] { "a" , "b" , "c" , "d" , "e" , "f" , "g" , "h" ,
                "i" , "j" , "k" , "l" , "m" , "n" , "o" , "p" , "q" , "r" , "s" , "t" ,
                "u" , "v" , "w" , "x" , "y" , "z" , "0" , "1" , "2" , "3" , "4" , "5" ,
                "6" , "7" , "8" , "9" , "A" , "B" , "C" , "D" , "E" , "F" , "G" , "H" ,
                "I" , "J" , "K" , "L" , "M" , "N" , "O" , "P" , "Q" , "R" , "S" , "T" ,
                "U" , "V" , "W" , "X" , "Y" , "Z"

        };
        // MD5 шифрует входящий URL
        String sMD5EncryptResult = MD5Util.MD5(key+url);
        String hex = sMD5EncryptResult;

//        String[] resUrl = new String[4];
//        for ( int i = 0; i < 4; i++) {

        // Выполняем битовые операции с зашифрованными символами в соответствии с 8-битным шестнадцатеричным и 0x3FFFFFFF
        String sTempSubString = hex.substring(2 * 8, 2 * 8 + 8);    // Исправлена ​​третья группа

        // Нужно использовать длинный тип для преобразования здесь, потому что Inteper.parseInt () может обрабатывать только 31 бит, первый бит является знаковым битом, если вы не используете long, он пересечет границу
        long lHexLong = 0x3FFFFFFF & Long.parseLong (sTempSubString, 16);
        String outChars = "" ;
        for ( int j = 0; j < 6; j++) {
            // Выполнить битовую операцию с полученным значением и 0x0000003D, чтобы получить индекс символов массива символов
            long index = 0x0000003D & lHexLong;
            // Добавить полученные символы
            outChars += chars[( int ) index];
            // сдвиг вправо на 5 бит каждый цикл
            lHexLong = lHexLong >> 5;
        }
        // Сохраняем строку в выходном массиве соответствующего индекса
//            resUrl[i] = outChars;
//        }
        return outChars;
    }
}