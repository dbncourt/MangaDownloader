package com.mangapanda.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Daniel Betancourt
 */
public class MangaPandaChapterProcessorService implements Callable<Void> {

    private final static String SITE_URL = "http://www.mangapanda.com";

    private BlockingQueue<Element> chaptersRef;
    private final ExecutorService producerRef;
    private String mangaName;

    private BlockingQueue<String> chapterImagesUrl;


    public MangaPandaChapterProcessorService(String mangaName, BlockingQueue<Element> chapters, ExecutorService producer) {
        this.chaptersRef = chapters;
        this.producerRef = producer;
        this.mangaName = mangaName;
        chapterImagesUrl = new LinkedBlockingQueue<>();
    }

    @Override
    public Void call() throws Exception {
        try {
            while (!producerRef.isTerminated() || chaptersRef.peek() != null ) {
                Element chapter = chaptersRef.poll(100L, TimeUnit.MILLISECONDS);
                if(chapter != null) {
                    String chapterName = extractChapterName(chapter);
                    Elements pages = extractPagesElements(chapter);
                    processChapter(pages, chapterName);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    private String extractChapterName(Element chapter)  throws Exception{
        Connection connection = Jsoup.connect(SITE_URL + chapter.attr("href"));
        Document document = connection.get();
        return extractChapterName(document);
    }

    private String extractChapterName(Document chapterDocument){
        Elements elements = chapterDocument.select("#topchapter div h1");
        return elements.get(0).html();
    }

    private static Elements extractPagesElements(Element chapter) throws Exception{
        Connection connection = Jsoup.connect(SITE_URL + chapter.attr("href"));
        Document document = connection.get();
        Elements pagesElements = document.select("#selectpage option");
        return pagesElements;
    }

    private void processChapter(Elements pages, String chapterName) throws Exception{
        List<String> imagesUrls = MangaPandaImageUrlExtractorService.extractImagesFromPages(pages);
        ImageUrlsProcesserService imageUrlsProcesserService = new ImageUrlsProcesserService();
        imageUrlsProcesserService.processImagesUrls(imagesUrls, mangaName, chapterName);
    }

}