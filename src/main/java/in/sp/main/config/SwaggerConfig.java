package in.sp.main.config;

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

	final String securitySchemeName = "Bearer Authentication";

	@Bean
	public OpenAPI configSwagger() {
		return new OpenAPI()
				.info(new Info() // here we will provide informations about the API (title, description, version, license, contact etc)
				.title("Journal App API")
				.description("An API for the Journal App")
				)
				.servers(List.of(new Server().url("http://localhost:8080").description("local server"), //The servers object lets you specify one or more base URLs for your API.
						                                                                                //Each endpoint (like /users, /orders) is appended to this base URL
						                                                                                //to form the full request path.
						                           //In Swagger UI, these will show up in a dropdown so users can choose which environment to interact with.

						         new Server().url("http://localhost:8081").description("live server")))
				.tags(List.of(new Tag().name("Public API"),  //  used to group and describe API endpoints under a common label in the generated documentation.
						new Tag().name("Journal API"),
						new Tag().name("Authenticated User API"),
						new Tag().name("Admin API")))
				.addSecurityItem(new SecurityRequirement().addList(securitySchemeName))//This method adds a security requirement to your OpenAPI spec.
			                                                                           //It tells Swagger UI that certain endpoints require authentication 
				                                                                       //and enables the Authorize button.				
			                                                                           //This part basically authenticates endpoints to accept JWT token.					
	            .components(new Components() //This method defines reusable components like security schemes, schemas, parameters, etc.
	            		                     //In the context of security, it’s used to declare how authentication works

	                .addSecuritySchemes(securitySchemeName,
	                    new SecurityScheme()
	                        .name(securitySchemeName)
	                        .type(SecurityScheme.Type.HTTP)
	                        .scheme("bearer")
	                        .in(SecurityScheme.In.HEADER)
	                        .bearerFormat("JWT")));
	}
}
