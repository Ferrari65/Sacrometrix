package com.Sacrometrix.Sacrometrix.controller.login;

import com.Sacrometrix.Sacrometrix.dto.login.LoginRequest;
import com.Sacrometrix.Sacrometrix.dto.token.RefreshRequest;
import com.Sacrometrix.Sacrometrix.dto.token.TokenResponse;
import com.Sacrometrix.Sacrometrix.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest req) {
        String subject = String.valueOf(req.matricula());
        List<String> roles = List.of("ANALISTA");

        String access = jwtService.generateAccessToken(subject, roles);
        String refresh = jwtService.generateRefreshToken(subject);

        return ResponseEntity.ok(new TokenResponse(access, refresh, roles));
    }

    @GetMapping("/debug/header")
    public Map<String, Object> header(HttpServletRequest req) {
        return Map.of("authorization", req.getHeader("Authorization"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest req) {
        var claims = jwtService.validateAndGetClaims(req.refreshToken());

        String typ = (String) claims.getClaim("typ");
        if (!"refresh".equals(typ)) {
            return ResponseEntity.badRequest().build();
        }

        String subject = claims.getSubject();
        List<String> roles = List.of("ANALISTA");

        String newAccess = jwtService.generateAccessToken(subject, roles);
        String newRefresh = jwtService.generateRefreshToken(subject);

        return ResponseEntity.ok(new TokenResponse(newAccess, newRefresh, roles));
    }
}
