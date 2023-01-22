package com.usermanagement.app.userservice.application.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "usermanagement.app.firebase.credentials")
public class FirebaseCredentialsConfig {

  private String type;
  private String projectId;
  private String privateKeyId;
  private String privateKey;
  private String clientEmail;
  private String clientId;
  private String authUri;
  private String tokenUri;
  private String authProviderX509CertUrl;
  private String clientX509CertUrl;

  @SneakyThrows
  public FirebaseCredentials getCredentials() {
    return FirebaseCredentials.builder()
        .type(type)
        .projectId(projectId)
        .privateKeyId(privateKeyId)
        .privateKey(privateKey.replace("\\n", "\n"))
        .clientEmail(clientEmail)
        .clientId(clientId)
        .authUri(authUri)
        .tokenUri(tokenUri)
        .authProviderX509CertUrl(authProviderX509CertUrl)
        .clientX509CertUrl(clientX509CertUrl)
        .build();
  }

  @Data
  @Builder
  public static class FirebaseCredentials {

    private String type;
    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("private_key_id")
    private String privateKeyId;

    @JsonProperty("private_key")
    private String privateKey;

    @JsonProperty("client_email")
    private String clientEmail;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("auth_uri")
    private String authUri;

    @JsonProperty("token_uri")
    private String tokenUri;

    @JsonProperty("auth_provider_x509_cert_url")
    private String authProviderX509CertUrl;

    @JsonProperty("client_x509_cert_url")
    private String clientX509CertUrl;
  }

}
