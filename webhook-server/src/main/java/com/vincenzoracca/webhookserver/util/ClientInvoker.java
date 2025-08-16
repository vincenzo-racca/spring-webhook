package com.vincenzoracca.webhookserver.util;

import com.vincenzoracca.webhookserver.model.ShipmentEvent;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ClientInvoker {

    private final RestClient restClient;

    public ClientInvoker(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void invoke(String url, long timestamp, String signature, ShipmentEvent event) {
        restClient.post()
                .uri(url)
                .header("X-Timestamp", String.valueOf(timestamp))
                .header("X-Signature", signature)
                .body(event)
                .retrieve().toBodilessEntity();

    }
}
