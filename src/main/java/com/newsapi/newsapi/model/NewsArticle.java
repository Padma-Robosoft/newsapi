package com.newsapi.newsapi.model;

import jakarta.persistence.*;
import lombok.Data;


import java.util.Date;

@Entity
@Data
public class NewsArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String source;
    @Column(length = 2048)
    private String url;
    private String content;
    private Date publishedAt;
    private String imageUrl;
}
