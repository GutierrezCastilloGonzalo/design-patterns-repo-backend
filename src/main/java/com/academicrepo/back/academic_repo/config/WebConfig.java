package com.academicrepo.back.academic_repo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.academicrepo.back.academic_repo.general.presentation.controllers.BaseV1Controller;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/v1",
                HandlerTypePredicate.forAssignableType(BaseV1Controller.class));
    }
}
