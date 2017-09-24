package com.mangapanda.service;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;

import java.io.File;
import java.util.List;

/**
 * Created by Daniel Betancourt
 */
public class ImageUrlsProcesserService{

    private final static String BASE_PATH = "";

    private static void downloadImageToFile(String mangaName, String folderName, String imageUrl, int imageName){
        try {
            byte[] bytes = Jsoup.connect(imageUrl).ignoreContentType(true).execute().bodyAsBytes();
            File imageFile = new File(BASE_PATH + mangaName + File.separator + folderName + File.separator + imageName + ".jpg");
            FileUtils.writeByteArrayToFile(imageFile, bytes);
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }

    public void processImagesUrls(List<String> imagesUrls, String mangaName, String chapterName){
        int chapterIndex = 1;
        for(String imageUrl : imagesUrls){
            downloadImageToFile(mangaName, chapterName, imageUrl, chapterIndex++);
        }
    }
}
