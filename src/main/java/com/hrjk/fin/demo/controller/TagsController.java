package com.hrjk.fin.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hrjk.fin.demo.dao.TagRepository;
import com.hrjk.fin.demo.model.Tag;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/tags")
public class TagsController {

	private final Logger logger = LoggerFactory.getLogger(TagsController.class);

	@Autowired
	private TagRepository tagDao;

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Tag> getPerson(@PathVariable Integer id) {
		
		return tagDao.findById(id).flux();
	}

	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public Flux<Tag> add(@RequestBody Tag tag) {
		return tagDao.save(tag).flux();
	}

	@DeleteMapping("/{id}")	
	@ResponseStatus(HttpStatus.CREATED)
	public Flux<Void> delete(@PathVariable Integer id, @RequestHeader("X-Logged-User") String userId) {
		return tagDao.deleteLogic(id, userId).flux();
	}
	
	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Tag> getPersonInPage(@RequestHeader("X-Page-Start") int start,@RequestHeader("X-Page-Size") int size ) {
		return tagDao.findInPage(start, size);
	}
	
	
}
