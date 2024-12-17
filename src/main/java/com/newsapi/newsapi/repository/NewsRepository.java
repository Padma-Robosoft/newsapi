package com.newsapi.newsapi.repository;

import com.newsapi.newsapi.model.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<NewsArticle, Long> {
    List<NewsArticle> findTop10ByOrderByPublishedAtDesc();
}
