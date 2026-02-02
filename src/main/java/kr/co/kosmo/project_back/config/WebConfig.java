package kr.co.kosmo.project_back.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 1. [타임아웃 무제한] 장고 AI 분석 대기용 RestTemplate
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        
        // 연결 시도는 10초, 읽기(응답 대기)는 0(무제한)으로 설정
        factory.setConnectTimeout(10000); 
        factory.setReadTimeout(0); 
        
        return new RestTemplate(factory);
    }

    // 2. [Deserializer] 프론트에서 넘어오는 모든 String의 앞뒤 공백 제거
    @Bean
    public SimpleModule stringTrimModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                return (value != null) ? value.trim() : null;
            }
        });
        return module;
    }

    // 3. [Serializer] 이미지 경로 정제 (중복 제거 + 공백 인코딩)
    @Bean
    public SimpleModule imagePathModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new JsonSerializer<String>() {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                String fieldName = gen.getOutputContext().getCurrentName();
                
                if (value != null && fieldName != null && fieldName.toLowerCase().contains("image")) {
                    
                    // ① 이미 외부 URL(http)이거나 인코딩이 완료된 경우 건너뜀
                    if (value.startsWith("http") || value.contains("%20")) {
                        gen.writeString(value);
                        return;
                    }

                    // ② 경로 무한 증식 방지: /bogam/api, /uploads 등 찌꺼기 싹 제거
                    String cleanPath = value.replace("/bogam/api", "")
                                            .replace("bogam/api", "")
                                            .replace("/uploads", "")
                                            .replace("uploads", "")
                                            .trim();
                    
                    if (cleanPath.startsWith("/")) cleanPath = cleanPath.substring(1);

                    // ③ /uploads/ 딱 한 번만 붙이고 공백 처리
                    // (주의: 여기서 /bogam/api는 붙이지 않습니다. 이건 서버/프론트 설정에 맡깁니다.)
                    String finalUrl = "/uploads/" + cleanPath;
                    finalUrl = finalUrl.replace(" ", "%20");
                    
                    gen.writeString(finalUrl.replaceAll("/+", "/"));
                } else {
                    gen.writeString(value);
                }
            }
        });
        return module;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("classpath:/static/product/", "classpath:/static/");
        
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://web.filmal.dev", "http://localhost:5173", "http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/{path:[^\\.]*}").setViewName("forward:/index.html");
    }
}