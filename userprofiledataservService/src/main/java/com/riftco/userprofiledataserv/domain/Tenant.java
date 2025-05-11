package com.riftco.userprofiledataserv.domain;

import com.riftco.userprofiledataserv.domain.common.AggregateRoot;
import com.riftco.userprofiledataserv.domain.event.DomainEvent;
import com.riftco.userprofiledataserv.domain.event.TenantCreatedEvent;
import com.riftco.userprofiledataserv.domain.event.TenantNameChangedEvent;
import com.riftco.userprofiledataserv.domain.event.TenantDescriptionChangedEvent;
import com.riftco.userprofiledataserv.domain.event.TenantStatusChangedEvent;
import com.riftco.userprofiledataserv.domain.event.TenantSubscriptionChangedEvent;
import com.riftco.userprofiledataserv.domain.vo.SubscriptionPlan;
import com.riftco.userprofiledataserv.domain.vo.TenantId;
import com.riftco.userprofiledataserv.domain.vo.TenantName;
import com.riftco.userprofiledataserv.domain.vo.TenantStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class Tenant extends AggregateRoot {

    public static final Tenant NONE = new Tenant();

    private TenantId tenantId;
    private TenantName name;
    private TenantStatus status;
    private TenantType type;
    private String description;
    private SubscriptionPlan subscriptionPlan;
    private Instant subscriptionStartDate;
    private Instant subscriptionEndDate;
    private Instant createdAt;
    private Instant updatedAt;

    private Tenant() {}

    public Tenant create(TenantId tenantId, TenantName name, TenantType type, String description, SubscriptionPlan subscriptionPlan) {
        if (subscriptionPlan == null) {
            subscriptionPlan = SubscriptionPlan.freePlan(); // Default to free plan if not specified
        }
        TenantId uuid = TenantId.generate();
        return this.applyEvent(new TenantCreatedEvent(
                uuid.toUUID(),
                uuid,
                name,
                TenantStatus.ACTIVE,
                type,
                description,
                subscriptionPlan,
                Instant.now()));
    }

    /**
     * Updates the tenant's name if it differs from the current name.
     * 
     * @param name The new tenant name
     * @return Updated Tenant instance
     * @throws IllegalStateException if the new name is the same as the current name
     */
    public Tenant changeName(TenantName name) {
        if (name.equals(this.name)) {
            throw new IllegalStateException("New name must differ from current name");
        }
        return this.applyEvent(new TenantNameChangedEvent(getUUID(), name, Instant.now()));
    }
    
    /**
     * Updates the tenant's description if it differs from the current description.
     * 
     * @param description The new description
     * @return Updated Tenant instance
     * @throws IllegalStateException if the new description is the same as the current description
     */
    public Tenant changeDescription(String description) {
        if (description != null && description.equals(this.description)) {
            throw new IllegalStateException("New description must differ from current description");
        }
        return this.applyEvent(new TenantDescriptionChangedEvent(getUUID(), description, Instant.now()));
    }

    public Tenant deactivate() {
        if (this.status == TenantStatus.INACTIVE) {
            throw new IllegalStateException("Tenant is already inactive");
        }
        return this.changeStatus(TenantStatus.INACTIVE);
    }

    public Tenant activate() {
        if (this.status == TenantStatus.ACTIVE) {
            throw new IllegalStateException("Tenant is already active");
        }
        return this.changeStatus(TenantStatus.ACTIVE);
    }

    private Tenant changeStatus(TenantStatus status) {
        if (status.equals(this.status)) {
            throw new IllegalStateException("New status must differ from current status");
        }
        return this.applyEvent(new TenantStatusChangedEvent(getUUID(), status, Instant.now()));
    }

    public static Tenant from(UUID uuid, List<DomainEvent> history) {
        return history
                .stream()
                .reduce(
                        NONE,
                        (tenant, event) -> tenant.applyEvent(event, false),
                        (t1, t2) -> {throw new UnsupportedOperationException();}
                );
    }

    private Tenant applyEvent(DomainEvent event) {
        return this.applyEvent(event, true);
    }

    private Tenant applyEvent(DomainEvent event, boolean isNew) {
        final Tenant tenant = this.apply(event);
        if (isNew) {
            tenant.getEvents().add(event);
        }
        return tenant;
    }

    private Tenant apply(DomainEvent event) {
        if (event instanceof TenantCreatedEvent) {
            return this.apply((TenantCreatedEvent) event);
        } else if (event instanceof TenantNameChangedEvent) {
            return this.apply((TenantNameChangedEvent) event);
        } else if (event instanceof TenantDescriptionChangedEvent) {
            return this.apply((TenantDescriptionChangedEvent) event);
        } else if (event instanceof TenantStatusChangedEvent) {
            return this.apply((TenantStatusChangedEvent) event);
        } else if (event instanceof TenantSubscriptionChangedEvent) {
            return this.apply((TenantSubscriptionChangedEvent) event);
        } else {
            throw new IllegalArgumentException("Cannot handle event " + event.getClass());
        }
    }

    private Tenant apply(TenantCreatedEvent event) {
        this.setUUID(event.getUuid());
        this.tenantId = event.getTenantId();
        this.name = event.getName();
        this.status = event.getStatus();
        this.type = event.getType();
        this.description = event.getDescription();
        this.subscriptionPlan = event.getSubscriptionPlan();
        this.subscriptionStartDate = event.getOccurredAt();
        
        // Set initial subscription end date based on plan type (typically 1 month or 1 year from start)
        // For simplicity, we're setting it to 1 year from start
        this.subscriptionEndDate = event.getOccurredAt().plusSeconds(60 * 60 * 24 * 365); // 1 year
        
        this.createdAt = event.getOccurredAt();
        this.updatedAt = event.getOccurredAt();
        return this;
    }

    private Tenant apply(TenantNameChangedEvent event) {
        this.name = event.getName();
        this.updatedAt = event.getOccurredAt();
        return this;
    }
    
    private Tenant apply(TenantDescriptionChangedEvent event) {
        this.description = event.getDescription();
        this.updatedAt = event.getOccurredAt();
        return this;
    }
    
    private Tenant apply(TenantStatusChangedEvent event) {
        this.status = event.getStatus();
        this.updatedAt = event.getOccurredAt();
        return this;
    }

    public List<DomainEvent> getUncommittedEvents() {
        return this.getEvents();
    }

    public Tenant markEventsAsCommitted() {
        this.getEvents().clear();
        return this;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public TenantName getName() {
        return name;
    }

    public TenantStatus getStatus() {
        return status;
    }
    
    public TenantType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
    
    public SubscriptionPlan getSubscriptionPlan() {
        return subscriptionPlan;
    }
    
    public Instant getSubscriptionStartDate() {
        return subscriptionStartDate;
    }
    
    public Instant getSubscriptionEndDate() {
        return subscriptionEndDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
    
    public boolean isSubscriptionActive() {
        Instant now = Instant.now();
        return this.isActive() && 
               now.isAfter(subscriptionStartDate) && 
               now.isBefore(subscriptionEndDate);
    }
    
    /**
     * Factory method to create a PERSON tenant type (single user).
     * 
     * @param tenantId The tenant identifier
     * @param name The tenant name
     * @param description A description of the tenant
     * @param subscriptionPlan The subscription plan (or null for default free plan)
     * @return A new Tenant instance with type PERSON
     */
    public static Tenant createPersonTenant(TenantId tenantId, TenantName name, String description, SubscriptionPlan subscriptionPlan) {
        return NONE.create(tenantId, name, TenantType.PERSON, description, subscriptionPlan);
    }
    
    /**
     * Factory method to create an ORGANIZATION tenant type (multiple users).
     * 
     * @param tenantId The tenant identifier
     * @param name The tenant name
     * @param description A description of the tenant
     * @param subscriptionPlan The subscription plan (or null for default free plan)
     * @return A new Tenant instance with type ORGANIZATION
     */
    public static Tenant createOrganizationTenant(TenantId tenantId, TenantName name, String description, SubscriptionPlan subscriptionPlan) {
        return NONE.create(tenantId, name, TenantType.ORGANIZATION, description, subscriptionPlan);
    }
    
    /**
     * Changes the subscription plan for this tenant
     * 
     * @param newPlan the new subscription plan
     * @return updated Tenant instance
     */
    public Tenant changeSubscription(SubscriptionPlan newPlan) {
        if (newPlan == null) {
            throw new IllegalArgumentException("Subscription plan cannot be null");
        }
        
        Instant now = Instant.now();
        return this.applyEvent(new TenantSubscriptionChangedEvent(
                getUUID(),
                newPlan,
                now,
                // Calculate new end date based on current subscription time remaining
                // or add default period if expired
                now.isBefore(subscriptionEndDate) 
                    ? subscriptionEndDate
                    : now.plusSeconds(60 * 60 * 24 * 365), // Default 1 year if current sub expired
                now
        ));
    }
    
    private Tenant apply(TenantSubscriptionChangedEvent event) {
        this.subscriptionPlan = event.getSubscriptionPlan();
        this.subscriptionStartDate = event.getStartDate();
        this.subscriptionEndDate = event.getEndDate();
        this.updatedAt = event.getOccurredAt();
        return this;
    }

    public boolean isActive() {
        return TenantStatus.ACTIVE.equals(this.status);
    }
}
