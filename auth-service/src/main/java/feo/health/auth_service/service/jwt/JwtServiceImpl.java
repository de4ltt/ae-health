package feo.health.auth_service.service.jwt;

import feo.health.auth_service.model.token.ParsedToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.access.secret}")
    private String accessSecret;

    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${jwt.access.ttl-minutes}")
    private long accessTtlMinutes;

    @Value("${jwt.refresh.ttl-days}")
    private long refreshTtlDays;

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_TTYPE = "type";
    private static final String TTYPE_ACCESS = "access";
    private static final String TTYPE_REFRESH = "refresh";

    @Override
    public String generateAccessToken(Long userId, String email) {
        Date exp = Date.from(Instant.now().plus(accessTtlMinutes, ChronoUnit.MINUTES));
        return Jwts.builder()
                .subject(email)
                .claim(CLAIM_USER_ID, userId)
                .claim(CLAIM_TTYPE, TTYPE_ACCESS)
                .expiration(exp)
                .signWith(hmac(accessSecret))
                .compact();
    }

    @Override
    public String generateRefreshToken(Long userId, String email, String jti) {
        Date exp = Date.from(Instant.now().plus(refreshTtlDays, ChronoUnit.DAYS));
        return Jwts.builder()
                .subject(email)
                .id(jti)
                .claim(CLAIM_USER_ID, userId)
                .claim(CLAIM_TTYPE, TTYPE_REFRESH)
                .expiration(exp)
                .signWith(hmac(refreshSecret))
                .compact();
    }

    @Override
    public ParsedToken parseAccess(String token) {
        Claims c = Jwts.parser().verifyWith(hmac(accessSecret)).build().parseSignedClaims(token).getPayload();
        if (!TTYPE_ACCESS.equals(c.get(CLAIM_TTYPE))) throw new JwtException("Not access token");
        return toParsed(c);
    }

    @Override
    public ParsedToken parseRefresh(String token) {
        Claims c = Jwts.parser().verifyWith(hmac(refreshSecret)).build().parseSignedClaims(token).getPayload();
        if (!TTYPE_REFRESH.equals(c.get(CLAIM_TTYPE))) throw new JwtException("Not refresh token");
        return toParsed(c);
    }

    private static SecretKey hmac(String b64) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(b64));
    }

    private static ParsedToken toParsed(Claims c) {
        return new ParsedToken(
                c.get(CLAIM_USER_ID, Long.class),
                c.getSubject(),
                c.getId(),
                c.getExpiration().toInstant()
        );
    }
}
