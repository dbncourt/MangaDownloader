package com.mangapanda.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/**
 * Created by Daniel Betancourt
 */
public class MangaPandaChaptersListExtractorService implements Callable<Void> {

    private final static String SITE_URL = "http://www.mangapanda.com";
    private final String mangaName;
    private BlockingQueue<Element> chapters;

    public MangaPandaChaptersListExtractorService(BlockingQueue<Element> chapters, String mangaName) {
        this.chapters = chapters;
        this.mangaName = mangaName;
    }

    @Override
    public Void call() throws Exception {
        Connection connection = Jsoup.connect(SITE_URL + "/" + mangaName);
        Document document = connection.get();
        Elements chaptersElements = document.select("#listing tbody tr td a");
        chapters.addAll(chaptersElements);
        System.out.println("Fetched: " + chaptersElements.size());
        return null;
    }
}