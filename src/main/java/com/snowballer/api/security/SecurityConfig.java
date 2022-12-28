package com.snowballer.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.snowballer.api.repository.UserRepository;
import com.snowballer.api.service.UrlService;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;
	private final UrlService urlService;

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		// @formatter:off
			http
					.authorizeRequests(a -> a
							.antMatchers("/", "/error", "/webjars/**","/submit","/basic/**","/create",
									"/templates/**", "/**/town", "/**/question", "/**/letter", "/auth/**").permitAll()
							.anyRequest().authenticated()
					)
					.exceptionHandling(e -> e
							.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
					)
					.logout(l -> l
							.logoutSuccessUrl("/basic/login").permitAll()
					)
				.cors().configurationSource(corsConfigurationSource())
				.and()
					.csrf().disable()
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					.and()
					.oauth2Login()
					.successHandler(new CustomLoginAuthHandler(jwtTokenProvider, urlService))
					.and()
					.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
							OAuth2LoginAuthenticationFilter.class);

			return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.addAllowedOriginPattern("*");
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		// configuration.addAllowedHeader("*");
		// configuration.addAllowedMethod("*");
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
