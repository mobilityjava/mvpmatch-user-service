package com.usermanagement.app.userservice.application.config;

import com.mongodb.client.MongoClient;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.mongo.MongoLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "10m", defaultLockAtLeastFor = "10m")
@ConditionalOnProperty(prefix = "usermanagement.app.scheduler", value = "enabled")
public class SchedulerConfig {

  @Value("${usermanagement.app.mongo.database}")
  private String databaseName;

  @Bean
  public LockProvider lockProvider(MongoClient mongo) {
    return new MongoLockProvider(mongo.getDatabase(databaseName));
  }

}
