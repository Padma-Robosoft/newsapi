package com.newsapi.newsapi.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsapi.newsapi.model.NewsArticle;
import com.newsapi.newsapi.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class NewsFetcherServiceImpl implements NewsFetcherService {

    private final NewsRepository newsRepository;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${newsapi.key}")
    private String apiKey;

    public NewsFetcherServiceImpl(NewsRepository newsRepository, WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.newsRepository = newsRepository;
        this.webClientBuilder = webClientBuilder;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedRate = 3600000)
    public void fetchNews() {
        String url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=" + apiKey;

        // Using WebClient to fetch the data
        String response = webClientBuilder.baseUrl(url)
                .get()
                .retrieve()
                .bodyToMono(String.class)
                .block();  // Use block() to wait for the response

        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode articlesNode = rootNode.path("articles");

            for (JsonNode articleNode : articlesNode) {
                NewsArticle article = new NewsArticle();
                article.setTitle(articleNode.path("title").asText());
                article.setSource(articleNode.path("source").path("name").asText());
                article.setUrl(articleNode.path("url").asText());
                article.setContent(articleNode.path("content").asText());

                String publishedAtString = articleNode.path("publishedAt").asText();
                try {
                    OffsetDateTime offsetDateTime = OffsetDateTime.parse(publishedAtString, DateTimeFormatter.ISO_DATE_TIME);
                    Date publishedAtDate = Date.from(offsetDateTime.toInstant());
                    article.setPublishedAt(publishedAtDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                article.setImageUrl(articleNode.path("urlToImage").asText());
                newsRepository.save(article);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}