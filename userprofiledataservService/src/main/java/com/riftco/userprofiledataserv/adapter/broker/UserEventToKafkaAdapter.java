package com.riftco.userprofiledataserv.adapter.broker;

import com.riftco.userprofiledataserv.application.port.out.SendUserEventToBroker;
import com.riftco.userprofiledataserv.domain.event.DomainEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class UserEventToKafkaAdapter extends BaseEventToKafkaAdapter implements SendUserEventToBroker {
    
    public UserEventToKafkaAdapter(StreamBridge streamBridge) {
        super(streamBridge, SourceBinding.USERS_OUT);
    }

    @Override
    public void send(DomainEvent event) {
        super.send(event);
    }
}
