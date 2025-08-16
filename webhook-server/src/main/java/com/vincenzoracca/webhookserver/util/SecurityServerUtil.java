package com.vincenzoracca.webhookserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;

@Component
public class SecurityServerUtil {

    private static final String HMAC_ALGO = "HmacSHA256";

    private static final int DEFAULT_NUM_BYTES = 32;
    private static final SecureRandom RNG = new SecureRandom();

    private final ObjectMapper mapper;

    public SecurityServerUtil(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public record SigHeaders(long timestamp, String signature) {}

    public String newSecret() {
//        byte[] buf = new byte[DEFAULT_NUM_BYTES];
//        RNG.nextBytes(buf);
//        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
        return "secret"; // secret mocked
    }

    public SigHeaders sign(String secret, Object value) {
        var bodyJson = toJson(value);
        long ts = Instant.now().toEpochMilli();
        String toSign = ts + "\n" + bodyJson;
        String sig = "sha256=" + hmacSha256(secret, toSign);
        return new SigHeaders(ts, sig);
    }

    private String hmacSha256(String secret, String data) {
        try {
            var mac = javax.crypto.Mac.getInstance(HMAC_ALGO);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGO));
            byte[] h = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(h.length * 2);
            for (byte b : h) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("HMAC error", e);
        }
    }

    private String toJson(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
