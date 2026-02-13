package com.Sacrometrix.Sacrometrix.dto.token;

import java.util.List;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String role,
        List<String> roles
        ) {}
