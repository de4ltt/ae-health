package feo.health.auth_service.controller;

import com.fasterxml.jackson.datatype.jsr310.deser.JavaTimeDeserializerModifier;
import feo.health.auth_service.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth/jwks")
@RequiredArgsConstructor
public class JwksController {

    @Value("${jwt.public-key}")
    private String publicKey;

    private final JwtService jwtService;

    @GetMapping
    public Map<String, Object> getJwks() {
        List<Map<String, Object>> keys = new ArrayList<>();
        keys.add(toJwk("access-key", publicKey));
        keys.add(toJwk("refresh-key", publicKey));
        return Map.of("keys", keys);
    }

    private Map<String, Object> toJwk(String kid, String b64PublicKey) {
        PublicKey publicKey = jwtService.rsaPublicKey(b64PublicKey);
        RSAPublicKey rsa = (RSAPublicKey) publicKey;
        return Map.of(
                "kty", "RSA",
                "kid", kid,
                "use", "sig",
                "alg", "RS256",
                "n", Base64.getUrlEncoder().withoutPadding().encodeToString(rsa.getModulus().toByteArray()),
                "e", Base64.getUrlEncoder().withoutPadding().encodeToString(rsa.getPublicExponent().toByteArray())
        );
    }
}
