package com.usermanagement.app.userservice.application;

import com.usermanagement.app.userservice.domain.config.TimeConfig;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.usermanagement.app")
@EnableMongoRepositories("com.usermanagement.app")
@EnableFeignClients("com.usermanagement.app")
@EnableMongoAuditing
public class Application {

  public static void main(String[] args) {

    SpringApplication.run(Application.class, args);
  }

  @PostConstruct
  void started() {
    TimeConfig.configureDefaultTimeZone();
  }

}
