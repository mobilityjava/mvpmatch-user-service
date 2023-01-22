package com.usermanagement.app.userservice.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "usermanagement.app.firebase", value = "enabled")
@RequiredArgsConstructor
public class FirebaseConfig {

  private final ObjectMapper objectMapper;

  @Bean
  public FirebaseApp firebaseApp(FirebaseCredentialsConfig firebaseCredentials) {

    if (!FirebaseApp.getApps().isEmpty()) {
      return FirebaseApp.getInstance();
    }
    var inputStream = createFirebaseCredentials(firebaseCredentials);

    try {

      FirebaseOptions options = new FirebaseOptions.Builder()
          .setCredentials(GoogleCredentials.fromStream(inputStream))
          .build();

      return FirebaseApp.initializeApp(options);
    } catch (IOException e) {
      log.error("Could not initialize Firebase. Cause was {}", e.getMessage(), e);
      return FirebaseApp.initializeApp();
    }
  }

  @Bean
  public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
    return FirebaseAuth.getInstance();
  }

  @SneakyThrows
  private InputStream createFirebaseCredentials(FirebaseCredentialsConfig credentialsConfig) {
    var credentials = credentialsConfig.getCredentials();
    var configAsJson = objectMapper.writeValueAsString(credentials);
    log.debug("Resolved config '{}'",configAsJson);
    return new ByteArrayInputStream(        configAsJson.getBytes(StandardCharsets.UTF_8));
  }

}
