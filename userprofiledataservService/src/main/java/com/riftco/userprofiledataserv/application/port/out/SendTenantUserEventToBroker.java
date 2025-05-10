package com.riftco.userprofiledataserv.application.port.out;

import com.riftco.userprofiledataserv.domain.event.DomainEvent;

public interface SendTenantUserEventToBroker {
    void send(DomainEvent event);
}
