package com.vallacartelera.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

// TODO:
// - License
// - Servers
// - Email
// - personal URL
// - security?? -> Secuity Scheme is added
// I have follow this tutorial:
// https://www.youtube.com/watch?v=2o_3hjUPAfQ

@OpenAPIDefinition(
		info = @Info(
				contact = @Contact(
						name = "Alberto", 
						email = "albertovelazquezrapado@yahoo.es", 
						url = "https://github.com/AlVelRap"), 
				description = "OpenApi documentation for Vallacartelera Backend", 
				title = "OpenApi Specification - Vallacartelera", 
				version = "1.0", 
				license = @License(
						name = "Apache 2.0", 
						url = "https://direcion-licencia.com"
						),
				termsOfService = "Terms of service"
				),
		servers = {
				@Server(
						description = "Development Env",
						url = "http://localhost:8080"
						),
				@Server(
						description = "Production Env",
						url = "http://direccion-produccion.com"
						) 
				} 
		)
//@SecurityScheme(
//		name = "bearerAuth",
//		description = "JWT auth description", 
//		scheme = "bearer", 
//		type = SecuritySchemeType.HTTP, 
//		bearerFormat = "JWT", 
//		in = SecuritySchemeIn.HEADER
//		)
public class OpenApiConfig {

}
