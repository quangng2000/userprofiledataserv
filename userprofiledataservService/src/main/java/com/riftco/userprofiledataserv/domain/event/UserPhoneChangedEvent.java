package com.riftco.userprofiledataserv.domain.event;

import com.riftco.userprofiledataserv.domain.vo.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Event that captures when a user's phone number is changed.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPhoneChangedEvent implements DomainEvent {

    public static final String TYPE = "user.phone.changed";

    private UUID uuid;
    private PhoneNumber contactNumber;
    private Instant occurredAt;

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Instant getOccurredAt() {
        return this.occurredAt;
    }

    @Override
    public UUID uuid() {
        return this.uuid;
    }
}
