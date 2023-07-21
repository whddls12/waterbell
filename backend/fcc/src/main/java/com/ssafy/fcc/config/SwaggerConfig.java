package com.ssafy.fcc.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    //http://localhost:8080/swagger-ui.html

    public Docket getDocket(String groupName, Predicate<String> predicate) {
        return new Docket(DocumentationType.SWAGGER_2).groupName(groupName).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.ssafy.fcc.controller")).paths(predicate)
                .apis(RequestHandlerSelectors.any()).build();
    }

    @Bean
    public Docket allApi() {
        return getDocket("전체", Predicates.or(PathSelectors.regex("/*.*")));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("침수 방지 관제")
                .version("1.0.0")
                .description("침수 방지 관제 서비스의 swagger api 입니다.")
                .build();
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder().displayRequestDuration(true).validatorUrl("").build();
    }
}
