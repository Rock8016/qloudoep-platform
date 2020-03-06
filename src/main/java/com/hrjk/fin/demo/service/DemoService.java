package com.hrjk.fin.demo.service;

import com.hrjk.fin.demo.model.Tag;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DemoService {

    public Flux<String> serve();
    
    public Flux<Tag> getDefaultTags();

    public Mono<Tag> saveOrUpdate(Tag tag);
    
}