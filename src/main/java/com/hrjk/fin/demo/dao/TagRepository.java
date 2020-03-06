package com.hrjk.fin.demo.dao;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hrjk.fin.demo.model.Tag;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RefreshScope
public interface TagRepository extends ReactiveCrudRepository<Tag, Integer> {
	
    @Query("SELECT * FROM tag LIMIT :start , :pageSize")
    Flux<Tag> findInPage(int start, int pageSize);

    @Query("SELECT * FROM tag WHERE name = :name LIMIT 1 ")
    Mono<Tag> findByUniqueName(String name);
	
    @Transactional
    @Query("UPDATE tag set valid = false, modified_by = :userId, modified_at = NOW() WHERE id = :id")
    Mono<Void> deleteLogic(Integer id, String userId);
   
}
