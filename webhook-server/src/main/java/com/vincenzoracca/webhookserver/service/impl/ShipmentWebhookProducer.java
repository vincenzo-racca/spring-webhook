package com.vincenzoracca.webhookserver.service.impl;

import com.vincenzoracca.webhookserver.dao.WebhookDao;
import com.vincenzoracca.webhookserver.model.Webhook;
import com.vincenzoracca.webhookserver.model.ShipmentEvent;
import com.vincenzoracca.webhookserver.service.ShipmentProducer;
import com.vincenzoracca.webhookserver.util.ClientInvoker;
import com.vincenzoracca.webhookserver.util.SecurityServerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ShipmentWebhookProducer implements ShipmentProducer {

    private static final Logger log = LoggerFactory.getLogger(ShipmentWebhookProducer.class);

    private final WebhookDao webhookDao;
    private final ClientInvoker clientInvoker;
    private final SecurityServerUtil securityServerUtil;

    public ShipmentWebhookProducer(WebhookDao webhookDao, ClientInvoker clientInvoker, SecurityServerUtil securityServerUtil) {
        this.webhookDao = webhookDao;
        this.clientInvoker = clientInvoker;
        this.securityServerUtil = securityServerUtil;
    }


    @Override
    public void sendEvent(ShipmentEvent event) {
        webhookDao.findAllByEventFilters(Webhook.EventFilter.valueOf(event.status().name()))
                .forEach(webhook -> {
                    SecurityServerUtil.SigHeaders sign = securityServerUtil.sign(webhook.secret(), event);
                    invokeClient(webhook.callbackUrl(), sign.timestamp(), sign.signature(), event);
                    log.info("Sent {} to webhook {}", event, webhook.webhookId());
                });
    }

    private void invokeClient(String callbackUrl, long timestamp, String signature, ShipmentEvent event) {
        try {
            clientInvoker.invoke(callbackUrl, timestamp,signature, event);
        }
        catch (Exception e) {
            log.error("Error while invoking client {}", event, e);
            // handle with custom logic
        }
    }
}
