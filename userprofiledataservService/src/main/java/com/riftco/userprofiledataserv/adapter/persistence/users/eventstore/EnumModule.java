package com.riftco.userprofiledataserv.adapter.persistence.users.eventstore;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.riftco.userprofiledataserv.domain.TenantType;
import com.riftco.userprofiledataserv.domain.UserRole;
import com.riftco.userprofiledataserv.domain.UserState;
import com.riftco.userprofiledataserv.domain.vo.SubscriptionPlan;
import com.riftco.userprofiledataserv.domain.vo.TenantStatus;

/**
 * Jackson module for handling enum serialization/deserialization
 * This ensures proper handling of all enum types used in our domain model
 */
public class EnumModule extends SimpleModule {
    
    public EnumModule() {
        super("EnumModule");
        
        // Register domain enums
        addSerializer(UserState.class, new EnumSerializer<>());
        addDeserializer(UserState.class, new EnumDeserializer<>(UserState.class));
        
        addSerializer(UserRole.class, new EnumSerializer<>());
        addDeserializer(UserRole.class, new EnumDeserializer<>(UserRole.class));
        
        addSerializer(TenantType.class, new EnumSerializer<>());
        addDeserializer(TenantType.class, new EnumDeserializer<>(TenantType.class));
        
        addSerializer(TenantStatus.class, new EnumSerializer<>());
        addDeserializer(TenantStatus.class, new EnumDeserializer<>(TenantStatus.class));
        
        addSerializer(SubscriptionPlan.PlanType.class, new EnumSerializer<>());
        addDeserializer(SubscriptionPlan.PlanType.class, new EnumDeserializer<>(SubscriptionPlan.PlanType.class));
    }
}
