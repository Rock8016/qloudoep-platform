package com.hrjk.fin.demo.controller;

import com.hrjk.fin.demo.model.Tag;
import com.hrjk.fin.demo.service.DemoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/resilience")
public class ResilienceController {

	@Autowired
	@Qualifier("default")
	private DemoService service;

	@GetMapping(path = "/defaults", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Tag> getDefaults() {
		return service.getDefaultTags();
	}

	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Tag> add(@RequestBody Tag tag) {
		return service.saveOrUpdate(tag);
	}

	@GetMapping(path = "/servemsg", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<String> serverMsg() {
		return service.serve();
	}
		
}
