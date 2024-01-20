package app.rbac.rbac.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.*;
import io.swagger.v3.oas.annotations.info.*;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * @author MJ Makki
 * @version 1.0
 * @license SkyLimits, LLC (<a href="https://www.skylimits.tech">SkyLimits, LLC</a>)
 * @email m.makki@skylimits.tech
 * @since long time ago
 */

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Role Based Access Control Service API Documentation",
                        email = "info@rbac.app",
                        url = "https://rbac.app"
                ),
                description = "OpenApi documentation for Role Based Access Control Service API",
                title = "OpenApi specification - Role Based Access Control Service",
                version = "1.0",
                license = @License(
                        name = "Role Based Access Control Service",
                        url = "https://rbac.app"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8008"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "https://api.rbac.app"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
