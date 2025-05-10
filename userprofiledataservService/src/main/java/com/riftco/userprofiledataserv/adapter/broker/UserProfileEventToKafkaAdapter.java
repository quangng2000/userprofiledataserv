package com.riftco.userprofiledataserv.adapter.broker;

import com.riftco.userprofiledataserv.application.port.out.SendUserProfileEventToBroker;
import com.riftco.userprofiledataserv.domain.event.DomainEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class UserProfileEventToKafkaAdapter extends BaseEventToKafkaAdapter implements SendUserProfileEventToBroker {
    
    public UserProfileEventToKafkaAdapter(StreamBridge streamBridge) {
        super(streamBridge, SourceBinding.USER_PROFILES_OUT);
    }

    @Override
    public void send(DomainEvent event) {
        super.send(event);
    }
}
