package kr.co.kosmo.project_back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer  {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080", "http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
        .addResourceLocations("file:///C:/ai/Workspace/bogam/storage/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 1. 루트 경로 접속 시 index.html 연결
        registry.addViewController("/").setViewName("forward:/index.html");
    
        // 2. 중요: 점(.)이 포함되지 않은 경로(리액트 라우터 경로)만 index.html로 포워딩
        // 이렇게 해야 /assets/index.js 같은 실제 파일 요청이 index.html로 가로채지지 않습니다.
        registry.addViewController("/{path:[^\\.]*}")
                .setViewName("forward:/index.html");
        registry.addViewController("/**/{path:[^\\.]*}")
                .setViewName("forward:/index.html");
    }
}
