package com.riftco.userprofiledataserv.domain;

import com.riftco.userprofiledataserv.domain.common.AggregateRoot;
import com.riftco.userprofiledataserv.domain.event.DomainEvent;
import com.riftco.userprofiledataserv.domain.event.TenantUserCreatedEvent;
import com.riftco.userprofiledataserv.domain.event.TenantUserDeactivatedEvent;
import com.riftco.userprofiledataserv.domain.event.TenantUserRoleChangedEvent;
import com.riftco.userprofiledataserv.domain.vo.TenantId;
import com.riftco.userprofiledataserv.domain.vo.UserId;

import java.util.Objects;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * TenantUser represents the relationship between a User and a Tenant.
 * This is a key entity in multi-tenancy architecture that allows users to have
 * different roles across different tenants.
 */
public final class TenantUser extends AggregateRoot {

    public static final TenantUser NONE = new TenantUser();

    private TenantId tenantId;
    private UserId userId;
    private UserRole role;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    private TenantUser() {}

    /**
     * Creates a new tenant-user association. For PERSON tenant types, this will validate
     * that only one user can be associated with the tenant.
     * 
     * @param tenant The tenant to associate with the user
     * @param userId The user ID to associate with the tenant
     * @param role The role the user will have in this tenant
     * @param existingUserCount For PERSON tenant types, this should be 0, otherwise an exception is thrown
     * @return The new TenantUser instance
     */
    public TenantUser create(Tenant tenant, UserId userId, UserRole role, int existingUserCount) {
        Objects.requireNonNull(tenant, "Tenant cannot be null");
        Objects.requireNonNull(userId, "User ID cannot be null");
        Objects.requireNonNull(role, "Role cannot be null");
        
        // Enforce PERSON tenant type can only have one user
        if (tenant.getType() == TenantType.PERSON && existingUserCount > 0) {
            throw new IllegalStateException(
                "Cannot add multiple users to a PERSON tenant type. " +
                "Use ORGANIZATION tenant type for multi-user tenants.");
        }
        
        // For PERSON tenants, the role should always be TENANT_ADMIN
        if (tenant.getType() == TenantType.PERSON && role != UserRole.TENANT_ADMIN) {
            role = UserRole.TENANT_ADMIN;
        }

        return this.applyEvent(new TenantUserCreatedEvent(
                UUID.randomUUID(),
                tenant.getTenantId(),
                userId,
                role,
                Instant.now()));
    }

    public TenantUser changeRole(UserRole newRole) {
        if (newRole == null) {
            throw new IllegalArgumentException("New role cannot be null");
        }
        if (newRole == this.role) {
            return this;
        }
        return this.applyEvent(new TenantUserRoleChangedEvent(
                getUUID(),
                newRole,
                Instant.now()));
    }

    public TenantUser deactivate() {
        if (!this.active) {
            return this;
        }
        return this.applyEvent(new TenantUserDeactivatedEvent(getUUID(), Instant.now()));
    }

    public TenantUser activate() {
        if (this.active) {
            return this;
        }
        return this.applyEvent(new TenantUserRoleChangedEvent(getUUID(), this.role, Instant.now()));
    }

    public static TenantUser from(UUID uuid, List<DomainEvent> history) {
        return history
                .stream()
                .reduce(
                        NONE,
                        (tenantUser, event) -> tenantUser.applyEvent(event, false),
                        (t1, t2) -> {throw new UnsupportedOperationException();}
                );
    }

    private TenantUser applyEvent(DomainEvent event) {
        return this.applyEvent(event, true);
    }

    private TenantUser applyEvent(DomainEvent event, boolean isNew) {
        final TenantUser tenantUser = this.apply(event);
        if (isNew) {
            tenantUser.getEvents().add(event);
        }
        return tenantUser;
    }

    private TenantUser apply(DomainEvent event) {
        if (event instanceof TenantUserCreatedEvent) {
            return this.apply((TenantUserCreatedEvent) event);
        } else if (event instanceof TenantUserRoleChangedEvent) {
            return this.apply((TenantUserRoleChangedEvent) event);
        } else if (event instanceof TenantUserDeactivatedEvent) {
            return this.apply((TenantUserDeactivatedEvent) event);
        } else {
            throw new IllegalArgumentException("Cannot handle event " + event.getClass());
        }
    }

    private TenantUser apply(TenantUserCreatedEvent event) {
        this.setUUID(event.getUuid());
        this.tenantId = event.getTenantId();
        this.userId = event.getUserId();
        this.role = event.getRole();
        this.active = true;
        this.createdAt = event.getOccurredAt();
        this.updatedAt = event.getOccurredAt();
        return this;
    }

    private TenantUser apply(TenantUserRoleChangedEvent event) {
        this.role = event.getRole();
        this.updatedAt = event.getOccurredAt();
        return this;
    }

    private TenantUser apply(TenantUserDeactivatedEvent event) {
        this.active = false;
        this.updatedAt = event.getOccurredAt();
        return this;
    }

    public List<DomainEvent> getUncommittedEvents() {
        return this.getEvents();
    }

    public TenantUser markEventsAsCommitted() {
        this.getEvents().clear();
        return this;
    }

    // Getters
    public TenantId getTenantId() {
        return tenantId;
    }

    public UserId getUserId() {
        return userId;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
