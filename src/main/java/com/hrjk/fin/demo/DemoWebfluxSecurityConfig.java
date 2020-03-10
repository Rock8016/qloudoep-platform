package com.hrjk.fin.demo;

import com.hrjk.fin.demo.security.HrjkSecurityConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class DemoWebfluxSecurityConfig extends HrjkSecurityConfig {

	private final Logger logger = LoggerFactory.getLogger(DemoWebfluxSecurityConfig.class);

	// @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
	// String jwkSetUri;

	@RefreshScope
	@Bean
	public SecurityWebFilterChain springSecurityConfigJwt(ServerHttpSecurity http) {		
		http.csrf(csrf -> csrf.disable())
				.authorizeExchange(exchanges ->
					exchanges
						.pathMatchers("/actuator/**").permitAll()
						// .pathMatchers(HttpMethod.DELETE,
						// "/api/**").hasAuthority("SCOPE_message:write")
						.pathMatchers("/api/**").hasAnyAuthority("SCOPE_DEMO_SERVICE")
						//FIXME 此处不要使用hasRole() 
						.pathMatchers("/api/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
						.anyExchange().authenticated())
				.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
						.jwt(jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor()))
				);
		return http.build();
	}

	// @RefreshScope
	// @Bean
	// ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
	// ReactiveClientRegistrationRepository clientRegistrationRepository,
	// ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {

	// ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
	// ReactiveOAuth2AuthorizedClientProviderBuilder
	// .builder().authorizationCode().refreshToken().clientCredentials().password().build();
	// DefaultReactiveOAuth2AuthorizedClientManager authorizedClientManager = new
	// DefaultReactiveOAuth2AuthorizedClientManager(
	// clientRegistrationRepository, authorizedClientRepository);
	// authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

	// return authorizedClientManager;
	// }

}