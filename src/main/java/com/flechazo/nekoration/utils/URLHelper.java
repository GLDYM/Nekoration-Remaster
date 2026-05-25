package com.flechazo.nekoration.utils;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class URLHelper {
    public static boolean isURL(String str) throws MalformedURLException, URISyntaxException {
        new URL(str).toURI();
        return true;
    }
}
