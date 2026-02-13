package com.Sacrometrix.Sacrometrix.dto.token;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(@NotBlank String refreshToken) {}
