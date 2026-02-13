package com.Sacrometrix.Sacrometrix.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties (String issuer, long accessTtlMinutes, long refreshTtlDays, long  clockSkewSeconds,
   String secret
){}
