package com.zoom.exam_sys_backend.config;

import com.zoom.exam_sys_backend.interceptor.UserRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/1 13:35
 **/

@SpringBootConfiguration
public class MyWebConfigurer implements WebMvcConfigurer {

    @Autowired
    UserRequestInterceptor userRequestInterceptor;

    /**
    * @Author: ZooMEISTER
    * @Description: 请求跨域配置
    * @DateTime: 2024/1/23 22:25
    * @Params: [corsRegistry]
    * @Return void
    */
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){
        /**
         * 所有请求都允许跨域，使用这种配置就不需要
         * 在interceptor中配置header了
         */
        corsRegistry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*")
                .maxAge(5000);
    }


    /**
    * @Author: ZooMEISTER
    * @Description: 添加拦截器
    * @DateTime: 2024/1/23 22:25
    * @Params: [registry]
    * @Return void
    */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 用户请求拦截器，用于用户鉴权
        registry.addInterceptor(userRequestInterceptor);
    }
}
