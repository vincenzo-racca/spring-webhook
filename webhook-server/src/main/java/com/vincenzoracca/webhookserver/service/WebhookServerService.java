package com.vincenzoracca.webhookserver.service;

import com.vincenzoracca.webhookserver.dao.ClientRegistrationDao;
import com.vincenzoracca.webhookserver.model.ClientRegistration;
import com.vincenzoracca.webhookserver.model.ClientRegistration.EventFilter;
import com.vincenzoracca.webhookserver.model.ClientRegistrationRequest;
import com.vincenzoracca.webhookserver.model.ShipmentEvent;
import com.vincenzoracca.webhookserver.util.ClientInvoker;
import com.vincenzoracca.webhookserver.util.SecurityServerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WebhookServerService {

    private static final Logger log = LoggerFactory.getLogger(WebhookServerService.class);

    private final ClientInvoker clientInvoker;
    private final ClientRegistrationDao clientRegistrationDao;
    private final SecurityServerUtil securityServerUtil;

    public WebhookServerService(ClientInvoker clientInvoker, ClientRegistrationDao clientRegistrationDao, SecurityServerUtil securityServerUtil) {
        this.clientInvoker = clientInvoker;
        this.clientRegistrationDao = clientRegistrationDao;
        this.securityServerUtil = securityServerUtil;
    }


    public ClientRegistration registerClient(ClientRegistrationRequest request) {
        String secret = securityServerUtil.newSecret();
        String webhookId = UUID.randomUUID().toString();
        var registration = new ClientRegistration(
                webhookId,
                request.callbackUrl(),
                secret, request.eventFilter());
        clientRegistrationDao.insert(registration);
        return registration;
    }

    public void sendEvent(ShipmentEvent event) {
        clientRegistrationDao.findAllByEventFilters(EventFilter.valueOf(event.status().name()))
                .forEach(client -> {
                    log.info("Sending {} to client {}", event, client.webhookId());
                    SecurityServerUtil.SigHeaders sign = securityServerUtil.sign(client.secret(), event);
                    invokeClient(client.callbackUrl(), sign.timestamp(), sign.signature(), event);
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
