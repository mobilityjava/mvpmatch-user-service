package com.usermanagement.app.userservice.application.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@ConditionalOnProperty(prefix = "usermanagement.app.cache", value = "enabled")
public class CachingConfiguration {

}
