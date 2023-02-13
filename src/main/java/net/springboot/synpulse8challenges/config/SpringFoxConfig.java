package net.springboot.synpulse8challenges.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SpringFoxConfig extends WebMvcConfigurationSupport {
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {"classpath:/static/","classpath:/public/"};

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(addHeaders())
                .globalResponseMessage(RequestMethod.GET,
                        addResponseModel())
                .globalResponseMessage(RequestMethod.POST,
                        addResponseModel())
                .select()
                .apis(RequestHandlerSelectors.basePackage("net.springboot.synpulse8challenges.controllers"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/");
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/*")
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }

    private List<Parameter> addHeaders(){
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        parameterBuilder.name("jwtToken")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(true).build();
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(parameterBuilder.build());
        return parameters;
    }

    public List<ResponseMessage> addResponseModel(){
        List<ResponseMessage> responses = Arrays.asList(
                new ResponseMessageBuilder().code(HttpStatus.FOUND.value())
                        .message(HttpStatus.FOUND.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.NOT_FOUND.value())
                        .message(HttpStatus.NOT_FOUND.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value())
                        .message(HttpStatus.BAD_REQUEST.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.CREATED.value())
                        .message(HttpStatus.CREATED.getReasonPhrase()).build()
                );
        return responses;
    }
}