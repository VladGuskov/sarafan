package com.codegod.sarafan.config;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author XE on 06.09.2019
 * @project sarafan
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerCustomizer(){
        return container -> {
            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/"));
        };
    }
}
