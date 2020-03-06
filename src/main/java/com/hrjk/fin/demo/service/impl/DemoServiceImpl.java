package com.hrjk.fin.demo.service.impl;

import java.time.Duration;

import com.hrjk.fin.demo.dao.TagRepository;
import com.hrjk.fin.demo.exception.BusinessException;
import com.hrjk.fin.demo.model.Tag;
import com.hrjk.fin.demo.service.DemoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service(value = "default")
public class DemoServiceImpl implements DemoService {

    @Autowired
    private TagRepository dao;

    @RateLimiter(name = "backendA", fallbackMethod = "fallback")
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback")
    @Retry(name = "backendA", fallbackMethod = "fallback")
    @Bulkhead(name = "backendA")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Flux<String> serve() {

        return Flux.just("Hello World from backend A").delayElements(Duration.ofSeconds(10));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public Flux<String> fallback(Throwable e) {
        return Flux.error(new BusinessException("service unavailable for the time"), false);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Flux<Tag> getDefaultTags() {
        Tag t1 = new Tag(1, "tttt1", 0, null, null, null, null, 0);
        Tag t2 = new Tag(2, "tttt2", 0, null, null, null, null, 0);
        Tag[] tags = new Tag[] { t1, t2 };

        return Flux.fromArray(tags);
    }

    @RateLimiter(name = "backendA", fallbackMethod = "fallbackC")
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallbackC")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Mono<Tag> saveOrUpdate(Tag tag) {
        // Mono<Tag> tagPersisted = dao.findByUniqueName(tag.getName());
        // return tagPersisted;
        return Mono.empty();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Mono<String> fallbackC(Tag tag, Throwable e) {
        if (e instanceof AccessDeniedException)
            return Mono.error(new BusinessException("access denied", e));
        else
            return Mono.error(new BusinessException("service unavailable for the time", e));
    }

}