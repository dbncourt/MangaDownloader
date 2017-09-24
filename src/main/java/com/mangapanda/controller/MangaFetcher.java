package com.mangapanda.controller;

import com.mangapanda.service.MangaPandaChapterProcessorService;
import com.mangapanda.service.MangaPandaChaptersListExtractorService;
import org.jsoup.nodes.Element;

import java.util.concurrent.*;

/**
 * Created by Daniel Betancourt
 */
public class MangaFetcher {

    public void fetchManga(String mangaName) throws Exception{
        BlockingQueue<Element> chapters = new LinkedBlockingQueue<>();

        ExecutorService chaptersElementExtractorService = getChaptersElementExtractorService(mangaName, chapters);
        chaptersElementExtractorService.shutdown();

        ExecutorService chapterProcessorService = getMangaPandaChapterProcessorService(mangaName, chapters, chaptersElementExtractorService);
        chapterProcessorService.shutdown();
    }

    private static ExecutorService getChaptersElementExtractorService(String mangaName, BlockingQueue<Element> chapters){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        MangaPandaChaptersListExtractorService chaptersListExtractorService = new MangaPandaChaptersListExtractorService(chapters, mangaName);
        executorService.submit(chaptersListExtractorService);
        return executorService;
    }

    private static ExecutorService getMangaPandaChapterProcessorService(String mangaName, BlockingQueue<Element> chapters, ExecutorService producer){
        int processors = 6;
        ExecutorService executorService = Executors.newFixedThreadPool(processors);
        for(int i = 0; i < processors; ++i){
            Callable<Void> imageUrlsProcesserService = new MangaPandaChapterProcessorService(mangaName, chapters, producer);
            executorService.submit(imageUrlsProcesserService);
        }
        return  executorService;
    }

    private static void waitForExecutorToFinish(ExecutorService executorService) {
        while (!executorService.isTerminated()) ;
    }
}