package com.riftco.userprofiledataserv.adapter.broker;

import org.springframework.messaging.MessageChannel;

public interface SourceBinding {
    String USERS_OUT = "usersv1";
    
    // Note: With Spring Cloud Stream 3.x+, the old annotation-based model is replaced
    // with functional approach using StreamBridge. This interface is kept for
    // backward compatibility, but actual implementation will use StreamBridge.
    MessageChannel usersOut();
}
