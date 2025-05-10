package com.riftco.userprofiledataserv.adapter.broker;

import com.riftco.userprofiledataserv.application.port.out.SendTenantEventToBroker;
import com.riftco.userprofiledataserv.domain.event.DomainEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class TenantEventToKafkaAdapter extends BaseEventToKafkaAdapter implements SendTenantEventToBroker {
    
    public TenantEventToKafkaAdapter(StreamBridge streamBridge) {
        super(streamBridge, SourceBinding.TENANTS_OUT);
    }

    @Override
    public void send(DomainEvent event) {
        super.send(event);
    }
}
