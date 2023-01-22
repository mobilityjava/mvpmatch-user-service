package com.usermanagement.app.userservice.application.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI Configuration.
 */
@Configuration
public class OpenApiConfig {

  @Value("${usermanagement.app.doc.api.url}")
  private String apiUrl;

  /**
   * Creates the bean for the public API.
   *
   * @return bean for the public API
   */
  @Bean
  public OpenAPI publicApi() {

    return new OpenAPI()
        .info(new Info()
            .title("User Service API")
            .description("User Service API Documentation")
            .version("v0.1.0-SNAPSHOT"))
        .addServersItem(new Server().url(apiUrl))
        .externalDocs(new ExternalDocumentation()
            .description("User Service API Documentation"))
        .components(new Components().addSecuritySchemes("bearer-key",
            new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
                .bearerFormat("JWT")));

  }

}
