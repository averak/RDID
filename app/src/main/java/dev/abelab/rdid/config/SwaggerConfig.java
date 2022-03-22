package dev.abelab.rdid.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ModelAttribute;

import dev.abelab.rdid.property.ProjectProperty;
import lombok.RequiredArgsConstructor;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swaggerの設定
 */
@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class SwaggerConfig {

    private final ProjectProperty projectProperty;

    @Bean
    public Docket dock() {
        return new Docket(DocumentationType.SWAGGER_2) //
            .useDefaultResponseMessages(false) //
            .ignoredParameterTypes(ModelAttribute.class) //
            .protocols(Collections.singleton(this.projectProperty.getSwagger().getProtocol())) //
            .host(this.projectProperty.getSwagger().getHostname()) //
            .select() //
            .apis(RequestHandlerSelectors.basePackage("dev.abelab.rdid.api.controller")) //
            .build() //
            .apiInfo(apiInfo()) //
            .tags( //
                new Tag("Auth", "認証"), //
                new Tag("User", "ユーザ"), //
                new Tag("User Group", "ユーザグループ"), //
                new Tag("Health Check", "ヘルスチェック") //
            );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder() //
            .title("RDID Internal API") //
            .version(this.projectProperty.getVersion()) //
            .build();
    }

}