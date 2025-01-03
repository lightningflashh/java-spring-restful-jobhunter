package vn.hoidanit.jobhunter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourcesWebConfiguration implements WebMvcConfigurer {

    @Value("${cheesethank.upload-file.base-uri}")
    private String baseUri;

    // Each time a request comes to /storage/**, it will point to the file storage
    // directory
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/storage/**")
                .addResourceLocations(baseUri);
    }

}
