package com.snowballer.api.common.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({RedirectUrlProperties.class})
public class AppConfiguration {
}
