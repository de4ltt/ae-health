package feo.health.auth_service.service.jwt;

import feo.health.auth_service.model.token.ParsedToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.private-key}") private String privateKey;
    @Value("${jwt.public-key}")  private String publicKey;
    @Value("${jwt.issuer}")      private String issuer;
    @Value("${jwt.access.ttl-minutes}") private long accessTtlMinutes;
    @Value("${jwt.refresh.ttl-days}")   private long refreshTtlDays;

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_TTYPE   = "type";
    private static final String TTYPE_ACCESS  = "access";
    private static final String TTYPE_REFRESH = "refresh";

    @Override
    public String generateAccessToken(Long userId, String email) {
        Date exp = Date.from(Instant.now().plus(accessTtlMinutes, ChronoUnit.MINUTES));
        return Jwts.builder()
                .header().keyId("access-key").and()
                .subject(email)
                .issuer(issuer)
                .claim(CLAIM_USER_ID, userId)
                .claim(CLAIM_TTYPE, TTYPE_ACCESS)
                .expiration(exp)
                .signWith(rsaPrivateKey(privateKey), Jwts.SIG.RS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(Long userId, String email, String jti) {
        Date exp = Date.from(Instant.now().plus(refreshTtlDays, ChronoUnit.DAYS));
        return Jwts.builder()
                .header().keyId("refresh-key").and()
                .subject(email)
                .issuer(issuer)
                .id(jti)
                .claim(CLAIM_USER_ID, userId)
                .claim(CLAIM_TTYPE, TTYPE_REFRESH)
                .expiration(exp)
                .signWith(rsaPrivateKey(privateKey), Jwts.SIG.RS256)
                .compact();
    }

    @Override
    public ParsedToken parseAccess(String token) {
        Claims c = Jwts.parser()
                .verifyWith(rsaPublicKey(publicKey))
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        if (!TTYPE_ACCESS.equals(c.get(CLAIM_TTYPE))) throw new JwtException("Not access token");
        return toParsed(c);
    }

    @Override
    public ParsedToken parseRefresh(String token) {
        Claims c = Jwts.parser()
                .verifyWith(rsaPublicKey(publicKey))
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        if (!TTYPE_REFRESH.equals(c.get(CLAIM_TTYPE))) throw new JwtException("Not refresh token");
        return toParsed(c);
    }

    private static PrivateKey rsaPrivateKey(String b64) {
        byte[] decoded = io.jsonwebtoken.io.Decoders.BASE64.decode(cleanPemI(b64));
        try {
            return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        } catch (Exception e) {
            throw new RuntimeException("Invalid RSA private key", e);
        }
    }

    private static String cleanPemI(String key) {
        return key.replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "")
                .replace("\n", "");
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
