package com.Sacrometrix.Sacrometrix.config;

import com.Sacrometrix.Sacrometrix.security.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class PropsConfig {
}
