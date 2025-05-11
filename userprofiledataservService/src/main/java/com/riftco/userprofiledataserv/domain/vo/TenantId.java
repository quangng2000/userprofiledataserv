package com.riftco.userprofiledataserv.domain.vo;

import com.riftco.userprofiledataserv.domain.common.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public final class TenantId implements ValueObject {
    private final String value;

    private TenantId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Tenant ID cannot be null or empty");
        }
        this.value = value;
    }

    public static TenantId of(String value) {
        return new TenantId(value);
    }

    public static TenantId generate() {
        return new TenantId(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }

/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * Returns the tenant ID as a {@link UUID}.
     *
     * @return the tenant ID as a UUID
     */
/* <<<<<<<<<<  d852acbe-c422-444a-a9b4-f2f3e3fb1b12  >>>>>>>>>>> */
    public UUID toUUID() {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid UUID format for Tenant ID: " + value, e);
        }
    }
}
