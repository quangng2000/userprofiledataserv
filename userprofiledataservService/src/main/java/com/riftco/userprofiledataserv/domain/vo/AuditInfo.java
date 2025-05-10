package com.riftco.userprofiledataserv.domain.vo;

import com.riftco.userprofiledataserv.domain.common.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

/**
 * Value object that contains audit information for domain entities.
 * Used to track who created or last modified an entity and when.
 * This is an important part of multi-tenant SaaS applications for compliance and security.
 */
@Getter
@EqualsAndHashCode
public final class AuditInfo implements ValueObject {
    private final UserId createdBy;
    private final Instant createdAt;
    private final UserId lastModifiedBy;
    private final Instant lastModifiedAt;

    private AuditInfo(UserId createdBy, Instant createdAt, UserId lastModifiedBy, Instant lastModifiedAt) {
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedAt = lastModifiedAt;
    }

    public static AuditInfo createNew(UserId userId) {
        Instant now = Instant.now();
        return new AuditInfo(userId, now, userId, now);
    }

    public AuditInfo withModifiedBy(UserId userId) {
        return new AuditInfo(this.createdBy, this.createdAt, userId, Instant.now());
    }

    @Override
    public String toString() {
        return "Created by: " + createdBy + " at " + createdAt + ", Last modified by: " + lastModifiedBy + " at " + lastModifiedAt;
    }
}
