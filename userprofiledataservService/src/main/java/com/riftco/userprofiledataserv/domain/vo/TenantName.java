package com.riftco.userprofiledataserv.domain.vo;

import com.riftco.userprofiledataserv.domain.common.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class TenantName implements ValueObject {
    private final String value;

    private TenantName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Tenant name cannot be null or empty");
        }
        if (value.length() > 100) {
            throw new IllegalArgumentException("Tenant name cannot exceed 100 characters");
        }
        this.value = value;
    }

    public static TenantName of(String value) {
        return new TenantName(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
