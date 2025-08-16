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
    public boolean invoke(String url, ShipmentEvent event) {
        return Boolean.TRUE.equals(restClient.post()
                .uri(url)
                .body(event)
                .exchange((request, response) -> !response.getStatusCode().isError()));



    }
}
