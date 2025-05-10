package com.riftco.userprofiledataserv.adapter.broker;

import org.springframework.messaging.MessageChannel;

public interface SourceBinding {
    // Topic for User events
    String USERS_OUT = "usersv1";
    
    // Topics for other entities
    String TENANTS_OUT = "tenantsv1";
    String USER_PROFILES_OUT = "userprofilesv1";
    String TENANT_USERS_OUT = "tenantusersv1";
    
    // Note: With Spring Cloud Stream 3.x+, the old annotation-based model is replaced
    // with functional approach using StreamBridge. This interface is kept for
    // backward compatibility, but actual implementation will use StreamBridge.
    
    // User events channel
    MessageChannel usersOut();
    
    // Tenant events channel
    MessageChannel tenantsOut();
    
    // UserProfile events channel
    MessageChannel userProfilesOut();
    
    // TenantUser events channel
    MessageChannel tenantUsersOut();
}
