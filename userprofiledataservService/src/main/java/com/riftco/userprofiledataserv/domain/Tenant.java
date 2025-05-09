package com.riftco.userprofiledataserv.domain;

import com.riftco.userprofiledataserv.domain.common.AggregateRoot;
import com.riftco.userprofiledataserv.domain.event.DomainEvent;
import com.riftco.userprofiledataserv.domain.event.TenantCreatedEvent;
import com.riftco.userprofiledataserv.domain.event.TenantUpdatedEvent;
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
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

    private Tenant() {}

    public Tenant create(TenantId tenantId, TenantName name, String description) {
        return this.applyEvent(new TenantCreatedEvent(
                UUID.randomUUID(),
                tenantId,
                name,
                TenantStatus.ACTIVE,
                description,
                Instant.now()));
    }

    public Tenant update(TenantName name, String description) {
        return this.applyEvent(new TenantUpdatedEvent(
                getUUID(),
                name,
                description,
                Instant.now()));
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
        this.status = status;
        this.updatedAt = Instant.now();
        return this;
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
        } else if (event instanceof TenantUpdatedEvent) {
            return this.apply((TenantUpdatedEvent) event);
        } else {
            throw new IllegalArgumentException("Cannot handle event " + event.getClass());
        }
    }

    private Tenant apply(TenantCreatedEvent event) {
        this.setUUID(event.getUuid());
        this.tenantId = event.getTenantId();
        this.name = event.getName();
        this.status = event.getStatus();
        this.description = event.getDescription();
        this.createdAt = event.getOccurredAt();
        this.updatedAt = event.getOccurredAt();
        return this;
    }

    private Tenant apply(TenantUpdatedEvent event) {
        this.name = event.getName();
        this.description = event.getDescription();
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

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return TenantStatus.ACTIVE.equals(this.status);
    }
}
