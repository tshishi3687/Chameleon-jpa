package Tshishi.Chameleon.Securities.Swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.Collections;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Your API Title")
                        .version("1.0.0"))
                .externalDocs(new ExternalDocumentation()
                        .description("Your external documentation")
                        .url("your-external-docs-url"))
                .servers(Collections.singletonList(
                        new Server().url("http://localhost:8081").description("Local Server")))
                .paths(new Paths())
                .components(new Components())
                .tags(Collections.singletonList(
                        new Tag().name("Your API Tag").description("Your API Tag Description")));
    }
}
