package com.vincenzoracca.webhookserver.dao;

import com.vincenzoracca.webhookserver.model.Webhook;

import java.util.List;

public interface WebhookDao {

    void insert(Webhook registration);
    List<Webhook> findAllByEventFilters(Webhook.EventFilter eventFilter);
}
