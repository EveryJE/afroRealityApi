package com.example.afrorealityapi.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Serves files from the local "uploads/" directory at /uploads/** URLs.
 * Used as a dev-mode fallback when Cloudflare R2 credentials are not configured.
 */
@Configuration
public class LocalUploadConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
