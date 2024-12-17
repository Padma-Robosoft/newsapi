package com.newsapi.newsapi.controller;

import com.newsapi.newsapi.model.NewsArticle;
import com.newsapi.newsapi.repository.NewsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NewsController {

    private final NewsRepository newsRepository;

    public NewsController(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @GetMapping("/news")
    public List<NewsArticle> getNews() {
        return newsRepository.findTop10ByOrderByPublishedAtDesc();
    }
}
