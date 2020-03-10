package com.hrjk.fin.demo.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;

import reactor.core.publisher.Mono;

public class HrjkSecurityConfig{

   public  Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new GrantedAuthoritiesExtractor());
		return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
	}

	public static class GrantedAuthoritiesExtractor implements Converter<Jwt, Collection<GrantedAuthority>> {
		public Collection<GrantedAuthority> convert(Jwt jwt) {
			Collection<String> authorities = null;
			Map<String, Object> realm_access = (Map<String, Object>) jwt.getClaims().get("realm_access");
			if (realm_access != null) {
				authorities = (Collection<String>) realm_access.get("roles");
			}
			String scope_str = (String) jwt.getClaims().get("scope");
			if (scope_str != null) {
				authorities = authorities==null? new LinkedList<String>(): authorities;
				authorities.addAll(Arrays.asList(scope_str.split(" ")));
			}
			if (authorities != null)
				return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
			return null;
		}
	}

}