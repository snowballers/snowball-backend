package com.snowballer.api.service;

import org.springframework.stereotype.Service;

@Service
public class UrlService {

    static final char[] BASE62 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    public String encoding(Long id) {
        StringBuilder url = new StringBuilder();
        Long value = id;

        do {
            url.append(BASE62[(int) (value % BASE62.length)]);
            value /= BASE62.length;
        } while(value > 0);

        return url.toString();
    }

    public Long decoding(String url) {
        Long id = 0L;
        int power = 1;

        for (int i = 0; i < url.length(); i++) {
            int digit = new String(BASE62).indexOf(url.charAt(i));
            id += digit * power;
            power *= BASE62.length;
        }

        return id;
    }
}
