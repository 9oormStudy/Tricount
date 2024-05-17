package clonecoding.tricount.config;

import clonecoding.tricount.interceptor.LoginCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 로그인이 필요한 페이지에 대한 접근 제어를 위해 인터셉터 등록
        registry.addInterceptor(loginCheckInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/join", "/login"
                        );
    }
}
