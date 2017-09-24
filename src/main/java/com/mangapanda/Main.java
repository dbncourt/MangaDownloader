package com.mangapanda;

import com.mangapanda.controller.MangaFetcher;

/**
 * Created by Daniel Betancourt
 */
public class Main {

    public static void main(String[] args){
        try {
            MangaFetcher mangaPandaFetcher = new MangaFetcher();
            mangaPandaFetcher.fetchManga(args[0]);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
