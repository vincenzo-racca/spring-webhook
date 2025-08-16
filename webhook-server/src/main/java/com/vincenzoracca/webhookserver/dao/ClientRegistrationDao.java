package com.vincenzoracca.webhookserver.dao;

import com.vincenzoracca.webhookserver.model.ClientRegistration;

import java.util.List;

public interface ClientRegistrationDao {

    void insert(ClientRegistration registration);
    List<ClientRegistration> findAllByEventFilters(ClientRegistration.EventFilter eventFilter);
}
