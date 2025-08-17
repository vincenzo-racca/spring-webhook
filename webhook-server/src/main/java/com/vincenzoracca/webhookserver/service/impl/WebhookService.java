package com.vincenzoracca.webhookserver.service.impl;

import com.vincenzoracca.webhookserver.dao.WebhookDao;
import com.vincenzoracca.webhookserver.model.Webhook;
import com.vincenzoracca.webhookserver.model.WebhookRegistrationRequest;
import com.vincenzoracca.webhookserver.util.SecurityServerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WebhookService {

    private static final Logger log = LoggerFactory.getLogger(WebhookService.class);

    private final WebhookDao webhookDao;
    private final SecurityServerUtil securityServerUtil;

    public WebhookService(WebhookDao webhookDao, SecurityServerUtil securityServerUtil) {
        this.webhookDao = webhookDao;
        this.securityServerUtil = securityServerUtil;
    }


    public Webhook registerWebhook(WebhookRegistrationRequest request) {
        String secret = securityServerUtil.newSecret();
        String webhookId = UUID.randomUUID().toString();
        var registration = new Webhook(
                webhookId,
                request.callbackUrl(),
                secret, request.eventFilter());
        webhookDao.insert(registration);
        log.info("Webhook registration success: {}", registration.webhookId());
        return registration;
    }

}
