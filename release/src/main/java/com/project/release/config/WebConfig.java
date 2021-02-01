package com.project.release.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${resources.location}")
    private String resourcesLocation;
    @Value("${resources.uri_path:}")
    private String resourcesUriPath;

//    private final String uploadImagePath;
//    public WebConfig(@Value("${custom.path.upload-images}") String uploadImagePath){
//        this.uploadImagePath = uploadImagePath;
//    }
//
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler(".html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//
//        //폴더 설정,,
//        List<String> imageFolders = Arrays.asList("test", "test2");
//        for (String imageFolder : imageFolders) {
//            registry.addResourceHandler("/static/img" + imageFolder + "/**")
//                    .addResourceLocations("file:///" + uploadImagePath + imageFolder + "/")
//                    .setCachePeriod(3600)
//                    .resourceChain(true)
//                    .addResolver(new PathResourceResolver());
//        }
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcesUriPath + "/**")
                .addResourceLocations("file://" + resourcesLocation);

    }

}
