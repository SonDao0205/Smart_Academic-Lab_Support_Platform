package com.dgnl.smartacademyandlabsupportplatform.config;

import com.dgnl.smartacademyandlabsupportplatform.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                // Quét tất cả các URL để kiểm tra login/phân quyền và chặn login/register
                .addPathPatterns("/**")
                // Chỉ loại trừ các tài nguyên tĩnh và trang lỗi
                .excludePathPatterns("/css/**", "/js/**", "/img/**", "/vendor/**", "/error", "/api/**");
    }
}