package com.Sacrometrix.Sacrometrix.security;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class JwtService {
    private final JwtProperties props;
    private final byte[] secretBytes;

    public JwtService(JwtProperties props) {
        this.props = props;
        this.secretBytes = props.secret().getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalStateException("JWT_SECRET fraco. Use 32+ bytes (ideal 64+ chars).");
        }
    }

    public String generateAccessToken(String subject, List<String> roles) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(props.accessTtlMinutes() * 60);

        return sign(subject, roles, now, exp, "access");
    }

    public String generateRefreshToken(String subject) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(props.refreshTtlDays() * 24 * 3600);

        return sign(subject, List.of(), now, exp, "refresh");
    }

    private String sign(String subject, List<String> roles, Instant now, Instant exp, String type) {
        try {
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .issuer(props.issuer())
                    .subject(subject)
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(exp))
                    .claim("typ", type)
                    .claim("roles", roles)
                    .jwtID(java.util.UUID.randomUUID().toString())
                    .build();

            SignedJWT jwt = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claims
            );
            jwt.sign(new MACSigner(secretBytes));
            return jwt.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao assinar JWT", e);
        }
    }

    public JWTClaimsSet validateAndGetClaims(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);

            if (!jwt.verify(new MACVerifier(secretBytes))) {
                throw new RuntimeException("Assinatura inválida");
            }

            JWTClaimsSet claims = jwt.getJWTClaimsSet();

            if (!props.issuer().equals(claims.getIssuer())) {
                throw new RuntimeException("Issuer inválido");
            }

            Instant now = Instant.now();
            Instant exp = claims.getExpirationTime().toInstant();
            long skew = props.clockSkewSeconds();
            if (now.isAfter(exp.plusSeconds(skew))) {
                throw new RuntimeException("Token expirado");
            }

            return claims;
        } catch (Exception e) {
            throw new RuntimeException("JWT inválido", e);
        }
    }
}
