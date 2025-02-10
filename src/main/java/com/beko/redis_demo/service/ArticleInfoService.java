package com.beko.redis_demo.service;

import com.beko.redis_demo.entity.ArticleInfo;
import com.beko.redis_demo.entity.Comment;
import com.beko.redis_demo.repository.ArticleInfoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class ArticleInfoService {
    private final ArticleInfoRepository articleInfoRepository;
    private final RedisClient redisClient;
    private final ObjectMapper mapper;
    private final StatefulRedisConnection<String, String> connection;
    private final RedisCommands<String, String> commands;

    private static final int TTL = 20;

    @Autowired
    public ArticleInfoService(ArticleInfoRepository articleInfoRepository, RedisClient redisClient, ObjectMapper mapper) {
        this.articleInfoRepository = articleInfoRepository;
        this.redisClient = redisClient;
        this.mapper = mapper;
        this.connection = redisClient.connect();
        this.commands = connection.sync();
    }

        public ArticleInfo getArticle(Long id) {
        return articleInfoRepository.findById(id)
                .map(articleInfo -> new ArticleInfo(
                        articleInfo.getId(),
                        articleInfo.getTitle(),
                        articleInfo.getText(),
                        articleInfo.getRating(),
                        articleInfo.getComments().stream()
                                .mapToDouble(Comment::getScore)
                                .average().orElse(0),
                        articleInfo.getComments()
                )).orElse(null);
    }

    public ArticleInfo getCachedArticle(Long id) {
        String key = "article:%d".formatted(id);
        String raw = commands.get(key);

        if (raw != null) {
            try {
                return mapper.readValue(raw, ArticleInfo.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Ошибка десериализации JSON", e);
            }
        }

        var article = getArticle(id);
        if (article == null) {
            return null;
        }

        try {
            commands.setex(key, TTL, mapper.writeValueAsString(article));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации JSON", e);
        }

        return article;
    }


    public ArticleInfo getRandomArticle() {
        long count = articleInfoRepository.count();
        long articleId = new Random().nextLong(1, count);
        return getCachedArticle(articleId);
    }
}
