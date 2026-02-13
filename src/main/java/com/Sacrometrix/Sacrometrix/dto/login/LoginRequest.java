package com.Sacrometrix.Sacrometrix.dto.login;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank Integer matricula,
        @NotBlank String senha
) {}
