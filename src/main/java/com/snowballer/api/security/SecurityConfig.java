package com.snowballer.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		// @formatter:off
			http
					.authorizeRequests(a -> a
							.antMatchers("/", "/error", "/webjars/**","/submit","/basic/**","/create",
									"/templates/**", "/**/town").permitAll()
							.anyRequest().authenticated()
					)
					.exceptionHandling(e -> e
							.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
					)
					.logout(l -> l
							.logoutSuccessUrl("/basic/login").permitAll()
					)
					.csrf().disable()
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					.and()
					.oauth2Login()
					.successHandler(new CustomLoginAuthHandler(jwtTokenProvider))
					.and()
					.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
							OAuth2LoginAuthenticationFilter.class);

			return http.build();
	}

}
