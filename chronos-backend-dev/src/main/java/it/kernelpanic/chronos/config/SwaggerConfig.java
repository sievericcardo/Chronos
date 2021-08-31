package it.kernelpanic.chronos.config;

import it.kernelpanic.chronos.controller.AuthenticationEntryPoint;
import it.kernelpanic.chronos.controller.EventController;
import it.kernelpanic.chronos.controller.NoteController;
import it.kernelpanic.chronos.controller.UserController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@PropertySource("classpath:swagger.properties")
@ComponentScan(basePackageClasses = AuthenticationEntryPoint.class)
@ComponentScan(basePackageClasses = EventController.class)
@ComponentScan(basePackageClasses = NoteController.class)
@ComponentScan(basePackageClasses = UserController.class)
@Configuration
public class SwaggerConfig {

    private static final String SWAGGER_API_VERSION = "1.0";
    private static final String LICENSE_TEXT = "GPLv3";
    private static final String title = "Chronos API";
    private static final String description = "Restful API for events";

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .license(LICENSE_TEXT)
                .version(SWAGGER_API_VERSION)
                .build();
    }

    @Bean
    public Docket eventsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .pathMapping("/")
                .select()
                .paths(PathSelectors.regex("/api.*"))
                .build();
    }
}
