package com.hrjk.fin.demo.controller;

import java.security.Principal;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/authuser")
public class AuthUserInfo {

    private final Logger logger = LoggerFactory.getLogger(AuthUserInfo.class);

    @RequestMapping(value = "/name", method = RequestMethod.GET)
    @ResponseBody
    public Mono<String> currentUserName(Principal principal) {
        return Mono.just(principal.getName());
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public Mono<String> currentUserInfo(Principal principal) {

        Mono<String> username = ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
                .map(Authentication::getName);

        logger.debug("authenticated user Authorization - > {}", principal.toString());

        return username;
    }

}