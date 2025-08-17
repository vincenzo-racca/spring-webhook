package com.vincenzoracca.webhookserver.dao.impl;

import com.vincenzoracca.webhookserver.dao.WebhookDao;
import com.vincenzoracca.webhookserver.model.Webhook;
import com.vincenzoracca.webhookserver.model.Webhook.EventFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WebhookInMemoryDao implements WebhookDao {

    private static final Logger log = LoggerFactory.getLogger(WebhookInMemoryDao.class);

    private static final Map<String, Webhook> WEBHOOKS_DB = new ConcurrentHashMap<>();

    @Override
    public void insert(Webhook webhook) {
        var oldValue = WEBHOOKS_DB.putIfAbsent(webhook.webhookId(), webhook);
        if (oldValue != null) {
            throw new IllegalArgumentException("Webhook already exists");
        }
        log.debug("Webhook inserted: {}", webhook);
    }

    public List<Webhook> findAllByEventFilters(EventFilter eventFilter) {
        return WEBHOOKS_DB.values().stream()
                .filter(client ->
                        client.eventFilter().equals(EventFilter.ALL) ||
                        eventFilter.equals(client.eventFilter()))
                .toList();
    }
}
