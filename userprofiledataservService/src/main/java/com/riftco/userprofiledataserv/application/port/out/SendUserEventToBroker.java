package com.riftco.userprofiledataserv.application.port.out;

import com.riftco.userprofiledataserv.domain.event.DomainEvent;


public interface SendUserEventToBroker {
    void send(DomainEvent event);

}
