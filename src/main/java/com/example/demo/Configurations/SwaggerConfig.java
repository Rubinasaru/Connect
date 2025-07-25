package com.example.demo.Configurations;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class SwaggerConfig {
	 @Bean
	    public OpenAPI swaggerCustomConfiguration() {
	        return new OpenAPI()
	                .info(new Info().title("Connect")
	                        .description("Social media Networking System"))
	                .servers(
	                        List.of(
	                                new Server().url("http://localhost:1012").description("development")
	                               
	                        )
	                )
	                .tags(
	                        List.of( 
	                                new Tag().name("User Controller").description("User related information"),
	                                new Tag().name("Auth Controller").description("Authentication related information"),
	                                new Tag().name ("Admin Controller").description("Admin Dashboard"),
	                                new Tag().name("Otp Controller").description("OTP related information"),
	                                new Tag().name("Post Controller").description("Posts related information"),
									new Tag().name("Mentor Recommendation Controller"). description("Recommendation related information"),
									new Tag().name("Connections Controller").description("User Connections related information")
	                        )
	                )
	                .addSecurityItem(
	                        new SecurityRequirement().addList("bearerAuth"))
	                .components(
	                        new Components().addSecuritySchemes(
	                                "bearerAuth", new SecurityScheme()
	                                        .type(SecurityScheme.Type.HTTP)
	                                        .scheme("Bearer")
	                                        .bearerFormat("JWT")
	                                        .in(SecurityScheme.In.HEADER)
	                                        .name("Authorization")
	                        )
	                );
	    }
}
