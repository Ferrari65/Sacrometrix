package com.Sacrometrix.Sacrometrix.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(BEARER_PREFIX.length()).trim();
        if (token.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        try {
            var claims = jwtService.validateAndGetClaims(token);

            String typ = (String) claims.getClaim("typ");
            if (!"access".equals(typ)) {
                chain.doFilter(request, response);
                return;
            }

            String subject = claims.getSubject();
            if (subject == null || subject.isBlank()) {
                chain.doFilter(request, response);
                return;
            }

            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.getClaim("roles");
            if (roles == null) roles = Collections.emptyList();

            var authorities = roles.stream()
                    .filter(r -> r != null && !r.isBlank())
                    .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            var auth = new UsernamePasswordAuthenticationToken(subject, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }
}
