package com.vincenzoracca.webhookclient.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class SecurityClientUtil {

    private static final String HMAC_ALGO = "HmacSHA256";
    private static final Duration TOLETANCE_MINUTES = Duration.ofMinutes(5);


    public boolean isInToleranceTime(long requestTimestamp) {
        long now = Instant.now().toEpochMilli();
//        var delayNow = Instant.ofEpochMilli(now).plus(10, ChronoUnit.MINUTES).toEpochMilli();
//        return delayNow - requestTimestamp <= TOLETANCE_MINUTES.toMillis();
        return now - requestTimestamp <= TOLETANCE_MINUTES.toMillis();
    }

    public boolean verifySignature(String secret, long requestTimestamp, String rawBody, String signature) {
        String expected = "sha256=" + hmacSha256(secret, requestTimestamp + "\n" + rawBody);
        return expected.equals(signature);
    }

    private String hmacSha256(String secret, String data) {
        try {
            var mac = Mac.getInstance(HMAC_ALGO);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGO));
            byte[] h = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(h.length * 2);
            for (byte b : h) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("HMAC error", e);
        }
    }
}
