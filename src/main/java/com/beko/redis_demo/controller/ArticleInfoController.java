package com.beko.redis_demo.controller;

import com.beko.redis_demo.entity.ArticleInfo;
import com.beko.redis_demo.service.ArticleInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/article")
public class ArticleInfoController {
    private final ArticleInfoService articleInfoService;

    @GetMapping("/trending")
    public ResponseEntity<ArticleInfo> getRandomArticle() {
        return new ResponseEntity<>(articleInfoService.getRandomArticle(), HttpStatusCode.valueOf(201));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleInfo> getArticle(@PathVariable Long id) {
        return new ResponseEntity<>(articleInfoService.getCachedArticle(id), HttpStatusCode.valueOf(201));
    }
}
