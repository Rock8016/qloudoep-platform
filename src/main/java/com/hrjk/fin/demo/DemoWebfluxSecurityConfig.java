package com.hrjk.fin.demo;

import static org.springframework.security.config.Customizer.withDefaults;

import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
// @EnableReactiveMethodSecurity
public class DemoWebfluxSecurityConfig {

	private final Logger logger = LoggerFactory.getLogger(DemoWebfluxSecurityConfig.class);

	// @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
	// String jwkSetUri;

	// @RefreshScope
	// @Bean
	// public SecurityWebFilterChain springSecurityDisableCsrf(ServerHttpSecurity
	// http) {
	// http.csrf(csrf -> csrf.disable());
	// return http.build();
	// }

	// @Value("${spring.security.oauth2.resourceserver.jwt.public-key-location}")
	// RSAPublicKey pubkey;

	// @RefreshScope
	// @Bean
	// public ReactiveJwtDecoder jwtDecoder() {
	// return NimbusReactiveJwtDecoder.withPublicKey(this.pubkey).build();
	// }

	// @RefreshScope
	@Bean
	public SecurityWebFilterChain springSecurityConfigJwt(ServerHttpSecurity http) {
		// NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
		// logger.debug("jwkSetUri -----> {}", jwkSetUri);
		http.csrf(csrf -> csrf.disable())
				.authorizeExchange(exchanges -> 
					exchanges
						.pathMatchers("/actuator/**").permitAll()
						// .pathMatchers(HttpMethod.GET, "/api/**").hasAuthority("SCOPE_message:read")
						// .pathMatchers(HttpMethod.HEAD, "/api/**").hasAuthority("SCOPE_message:read")
						// .pathMatchers(HttpMethod.POST, "/api/**").hasAuthority("SCOPE_message:write")
						// .pathMatchers(HttpMethod.PUT, "/api/**").hasAuthority("SCOPE_message:write")
						// .pathMatchers(HttpMethod.DELETE,
						// "/api/**").hasAuthority("SCOPE_message:write")
						// .pathMatchers("/api/**").hasAnyRole("ROLE_user", "ROLE_user-premium")
						.anyExchange().authenticated()
				)
				// .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::opaqueToken)
				.oauth2ResourceServer(oauth2ResourceServer -> 
					oauth2ResourceServer
						.jwt( jwt-> 
							jwt
								.jwtAuthenticationConverter(grantedAuthoritiesExtractor() )
						)
						// .bearerTokenConverter(null)
				)
			;
		return http.build();
	}

	Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new GrantedAuthoritiesExtractor());
		return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
	}

	static class GrantedAuthoritiesExtractor implements Converter<Jwt, Collection<GrantedAuthority>> {
		public Collection<GrantedAuthority> convert(Jwt jwt) {
			Map<String, Object> realm_access  = (Map<String, Object>) jwt.getClaims().get("realm_access");
			if(realm_access!=null){
				Collection<String> authorities = (Collection<String>)realm_access.get("roles");
				if(authorities!=null)
					return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
			}
			return null;
		}
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