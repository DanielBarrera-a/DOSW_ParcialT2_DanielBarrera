package edu.dosw.parcial.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String ESQUEMA = "bearerAuth";

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("University Food Hub API")
                        .version("v1")
                        .description("API REST de pedidos de comida para la cafetería universitaria. "
                                + "Inicie sesión, copie el token y péguelo en el botón Authorize."))
                .addSecurityItem(new SecurityRequirement().addList(ESQUEMA))
                .components(new Components()
                        .addSecuritySchemes(ESQUEMA, new SecurityScheme()
                                .name(ESQUEMA)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
