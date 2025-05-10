package com.riftco.userprofiledataserv.domain.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Instant;
import java.util.UUID;

// Import all domain event types for JsonSubTypes annotation
import com.riftco.userprofiledataserv.domain.event.UserCreatedEvent;
import com.riftco.userprofiledataserv.domain.event.UserDeactivatedEvent;
import com.riftco.userprofiledataserv.domain.event.UserEmailChangedEvent;
import com.riftco.userprofiledataserv.domain.event.UserNameChangedEvent;
import com.riftco.userprofiledataserv.domain.event.UserPhoneChangedEvent;
import com.riftco.userprofiledataserv.domain.event.UserProfileCreatedEvent;
import com.riftco.userprofiledataserv.domain.event.UserProfileDisplayNameChangedEvent;
import com.riftco.userprofiledataserv.domain.event.UserProfileAvatarChangedEvent;
import com.riftco.userprofiledataserv.domain.event.UserProfileBiographyChangedEvent;
import com.riftco.userprofiledataserv.domain.event.UserProfileJobInfoChangedEvent;
import com.riftco.userprofiledataserv.domain.event.UserProfileLocationChangedEvent;
import com.riftco.userprofiledataserv.domain.event.TenantCreatedEvent;
import com.riftco.userprofiledataserv.domain.event.TenantNameChangedEvent;
import com.riftco.userprofiledataserv.domain.event.TenantDescriptionChangedEvent;
import com.riftco.userprofiledataserv.domain.event.TenantStatusChangedEvent;
import com.riftco.userprofiledataserv.domain.event.TenantSubscriptionChangedEvent;
import com.riftco.userprofiledataserv.domain.event.TenantUserCreatedEvent;
import com.riftco.userprofiledataserv.domain.event.TenantUserRoleChangedEvent;
import com.riftco.userprofiledataserv.domain.event.TenantUserDeactivatedEvent;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(name = UserCreatedEvent.TYPE, value = UserCreatedEvent.class),
        @JsonSubTypes.Type(name = UserDeactivatedEvent.TYPE, value = UserDeactivatedEvent.class),
        @JsonSubTypes.Type(name = UserEmailChangedEvent.TYPE, value = UserEmailChangedEvent.class),
        @JsonSubTypes.Type(name = UserPhoneChangedEvent.TYPE, value = UserPhoneChangedEvent.class),
        @JsonSubTypes.Type(name = UserNameChangedEvent.TYPE, value = UserNameChangedEvent.class),
        @JsonSubTypes.Type(name = UserProfileCreatedEvent.TYPE, value = UserProfileCreatedEvent.class),
        @JsonSubTypes.Type(name = UserProfileDisplayNameChangedEvent.TYPE, value = UserProfileDisplayNameChangedEvent.class),
        @JsonSubTypes.Type(name = UserProfileAvatarChangedEvent.TYPE, value = UserProfileAvatarChangedEvent.class),
        @JsonSubTypes.Type(name = UserProfileBiographyChangedEvent.TYPE, value = UserProfileBiographyChangedEvent.class),
        @JsonSubTypes.Type(name = UserProfileJobInfoChangedEvent.TYPE, value = UserProfileJobInfoChangedEvent.class),
        @JsonSubTypes.Type(name = UserProfileLocationChangedEvent.TYPE, value = UserProfileLocationChangedEvent.class),
        @JsonSubTypes.Type(name = TenantCreatedEvent.TYPE, value = TenantCreatedEvent.class),
        @JsonSubTypes.Type(name = TenantNameChangedEvent.TYPE, value = TenantNameChangedEvent.class),
        @JsonSubTypes.Type(name = TenantDescriptionChangedEvent.TYPE, value = TenantDescriptionChangedEvent.class),
        @JsonSubTypes.Type(name = TenantStatusChangedEvent.TYPE, value = TenantStatusChangedEvent.class),
        @JsonSubTypes.Type(name = TenantSubscriptionChangedEvent.TYPE, value = TenantSubscriptionChangedEvent.class),
        @JsonSubTypes.Type(name = TenantUserCreatedEvent.TYPE, value = TenantUserCreatedEvent.class),
        @JsonSubTypes.Type(name = TenantUserRoleChangedEvent.TYPE, value = TenantUserRoleChangedEvent.class),
        @JsonSubTypes.Type(name = TenantUserDeactivatedEvent.TYPE, value = TenantUserDeactivatedEvent.class)
})
public interface DomainEvent {
    String type();
    Instant getOccurredAt();
    UUID uuid();
}
