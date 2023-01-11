package com.snowballer.api.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@ConfigurationProperties(prefix = "redirect")
public class RedirectUrlProperties {
	private final String uri;

	@ConstructorBinding
	public RedirectUrlProperties(String uri) {
		this.uri = uri;
	}
}
