package com.hrjk.fin.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
@EnableTransactionManagement
@EnableR2dbcRepositories("com.hrjk.fin.demo.dao")
@RefreshScope
public class WebConfig implements WebFluxConfigurer {

	private final Logger logger = LoggerFactory.getLogger(WebConfig.class);

	public WebConfig() {
	}

	@Override
	public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
		// ...

		// Jackson2ObjectMapperBuilder.json().build();
	}

}
