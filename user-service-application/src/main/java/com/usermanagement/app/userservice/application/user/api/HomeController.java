package com.usermanagement.app.userservice.application.user.api;

import com.usermanagement.app.userservice.application.config.UriPathConsts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home controller.
 */
@Controller
public class HomeController {

  /**
   * Swagger UI path.
   */
  @Value("${springdoc.swagger-ui.path}")
  private String swaggerUiPath;

  /**
   * Redirect to swagger ui.
   *
   * @return redirect URI
   */
  @GetMapping("/")
  public String home() {

    String toRedirect =
        StringUtils.isNotBlank(this.swaggerUiPath) ? this.swaggerUiPath : UriPathConsts.SWAGGER_PATH;

    return "redirect:" + toRedirect;
  }

}
