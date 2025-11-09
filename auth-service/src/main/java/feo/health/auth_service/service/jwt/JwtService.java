package feo.health.auth_service.service.jwt;

import feo.health.auth_service.model.token.ParsedToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public interface JwtService {
    String generateAccessToken(Long userId, String email);
    String generateRefreshToken(Long userId, String email, String jti);
    ParsedToken parseAccess(String token);
    ParsedToken parseRefresh(String token);
    default String cleanPem(String key) {
        return key
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s+", "");
    }
    default PublicKey rsaPublicKey(String key) {
        try {
            String clean = cleanPem(key);
            byte[] decoded = Base64.getDecoder().decode(clean);
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        } catch (Exception e) {
            throw new RuntimeException("Invalid RSA public key", e);
        }
    }
}