package com.mangapanda.controller;

import com.mangapanda.service.MangaPandaChaptersListExtractorService;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by Daniel Betancourt
 */
public class MangaFetcher {

    public void fetchManga(String mangaName) throws Exception{
        List<Element> chapters = MangaPandaChaptersListExtractorService.extractChapterElements(mangaName);

    }
}