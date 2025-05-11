package com.riftco.userprofiledataserv.domain.vo;

import com.riftco.userprofiledataserv.domain.common.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public final class UserId implements ValueObject {
    private final String value;

    private UserId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        this.value = value;
    }

    public static UserId of(String value) {
        return new UserId(value);
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }

/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * Returns the user ID as a {@link UUID}.
     *
     * @return the user ID as a UUID
     */
/* <<<<<<<<<<  0a0e4315-f7cd-4154-a6ac-661a6bc98fa4  >>>>>>>>>>> */
    public UUID toUUID() {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid UUID format: " + value, e);
        }

    }
}
