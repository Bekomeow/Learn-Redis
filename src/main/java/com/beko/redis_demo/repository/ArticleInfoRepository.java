package com.beko.redis_demo.repository;

import com.beko.redis_demo.entity.ArticleInfo;
import org.springframework.data.repository.CrudRepository;

public interface ArticleInfoRepository extends CrudRepository<ArticleInfo, Long> {
}
