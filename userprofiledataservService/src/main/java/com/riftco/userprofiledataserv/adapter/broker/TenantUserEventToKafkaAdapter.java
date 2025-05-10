package com.riftco.userprofiledataserv.adapter.broker;

import com.riftco.userprofiledataserv.application.port.out.SendTenantUserEventToBroker;
import com.riftco.userprofiledataserv.domain.event.DomainEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class TenantUserEventToKafkaAdapter extends BaseEventToKafkaAdapter implements SendTenantUserEventToBroker {
    
    public TenantUserEventToKafkaAdapter(StreamBridge streamBridge) {
        super(streamBridge, SourceBinding.TENANT_USERS_OUT);
    }

    @Override
    public void send(DomainEvent event) {
        super.send(event);
    }
}
