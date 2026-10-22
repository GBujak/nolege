package com.gbujak.nolege.config;

import jakarta.annotation.Nullable;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class SpaWebConfig implements WebMvcConfigurer {

    private static final Resource INDEX_HTML = new ClassPathResource("static/index.html");

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/static/")
            .resourceChain(true)
            .addResolver(new PathResourceResolver() {
                @Override
                protected @Nullable Resource getResource(
                    String resourcePath,
                    Resource location
                ) throws IOException {
                    Resource requested = location.createRelative(resourcePath);
                    if (requested.exists() && requested.isReadable()
                        && !resourcePath.isEmpty() && !resourcePath.endsWith("/")) {
                        return requested;
                    }

                    if (isApiPath(resourcePath) || resourcePath.contains(".")) {
                        return null;
                    }

                    return INDEX_HTML;
                }
            });
    }

    private boolean isApiPath(String resourcePath) {
        return resourcePath.equals("api") || resourcePath.startsWith("api/");
    }
}
