package com.vincenzoracca.webhookserver.service;

import com.vincenzoracca.webhookserver.dao.ClientRegistrationDao;
import com.vincenzoracca.webhookserver.model.ClientRegistration;
import com.vincenzoracca.webhookserver.model.ClientRegistration.EventFilter;
import com.vincenzoracca.webhookserver.model.ClientRegistrationRequest;
import com.vincenzoracca.webhookserver.model.ShipmentEvent;
import com.vincenzoracca.webhookserver.util.ClientInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WebhookServerService {

    private static final Logger log = LoggerFactory.getLogger(WebhookServerService.class);

    private final ClientInvoker clientInvoker;
    private final ClientRegistrationDao clientRegistrationDao;

    public WebhookServerService(ClientInvoker clientInvoker, ClientRegistrationDao clientRegistrationDao) {
        this.clientInvoker = clientInvoker;
        this.clientRegistrationDao = clientRegistrationDao;
    }


    public ClientRegistration registerClient(String clientId, ClientRegistrationRequest request) {
        ClientRegistration registration = new ClientRegistration(clientId, request.callbackUrl(), request.eventFilter());
        clientRegistrationDao.insert(registration);
        return registration;
    }

    public void sendEvent(ShipmentEvent event) {
        clientRegistrationDao.findAllByEventFilters(EventFilter.valueOf(event.status().name()))
                .forEach(client -> {
                    log.info("Sending {} to client {}", event, client.clientId());
                    clientInvoker.invoke(client.callbackUrl(),  event);
                    // you can handle the return value
                });
    }

}
