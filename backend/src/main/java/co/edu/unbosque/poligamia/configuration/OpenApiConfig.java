package co.edu.unbosque.poligamia.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration

public class OpenApiConfig {

	@Bean
    public OpenAPI customOpenAPI() {

        Info info = new Info()
            .title("API de Usuarios")
            .version("1.0")
            .description("Documentación de la API para gestión de usuarios con JWT");

        SecurityScheme securityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .name("Authorization");

        return new OpenAPI()
            .info(info)
            .components(
                new Components()
                    .addSecuritySchemes("bearerAuth", securityScheme)
                    .addResponses(
                        "UnauthorizedError",
                        new ApiResponse()
                            .description("No autenticado - Token JWT inválido o expirado")
                            .content(
                                new Content().addMediaType(
                                    "application/json",
                                    new MediaType().addExamples(
                                        "error",
                                        new Example()
                                            .value("{\"error\": \"No autorizado\", \"mensaje\": \"Token inválido o expirado\"}")
                                    )
                                )
                            )
                    )
                    .addResponses(
                        "ForbiddenError",
                        new ApiResponse()
                            .description("Acceso prohibido - No tienes permisos suficientes")
                            .content(
                                new Content().addMediaType(
                                    "application/json",
                                    new MediaType().addExamples(
                                        "error",
                                        new Example()
                                            .value("{\"error\": \"Acceso prohibido\", \"mensaje\": \"No tienes permisos para esta operación\"}")
                                    )
                                )
                            )
                    )
            )
            // 🔒 Aquí se activa la seguridad en Swagger
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}