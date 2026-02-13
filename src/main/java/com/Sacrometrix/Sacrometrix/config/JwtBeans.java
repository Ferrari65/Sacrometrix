package com.Sacrometrix.Sacrometrix.config;

import com.Sacrometrix.Sacrometrix.security.JwtProperties;
import com.Sacrometrix.Sacrometrix.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtBeans {

    @Bean
    JwtService jwtService(JwtProperties props){
        return new JwtService(props);
    }
}
