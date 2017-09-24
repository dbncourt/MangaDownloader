package com.mangapanda.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Betancourt
 */
public class MangaPandaImageUrlExtractorService {

    private final static String SITE_URL = "http://www.mangapanda.com";

    public static List<String> extractImagesFromPages(Elements pages) throws Exception {
        List<String> imageUrls = new ArrayList<>(pages.size());
        for(Element page : pages){
            String imageUrl = extractImageURLs(page);
            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }

    private static String extractImageURLs(Element chapter) throws Exception {
        Connection connection = Jsoup.connect(SITE_URL + chapter.attr("value"));
        Document document = connection.get();
        Element imageElement = extractImageElement(document);
        String imageUrl = imageElement.attr("src");
        return imageUrl;
    }

    private static Element extractImageElement(Document document) {
        Element imageElement = null;
        Elements elements = document.select("#imgholder a img");
        if (elements.size() == 1) {
            imageElement = elements.get(0);
        }
        return imageElement;
    }
}
