package com.riftco.userprofiledataserv.domain.event;

import com.riftco.userprofiledataserv.domain.vo.AvatarUrl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Event that captures when a user's profile avatar URL is changed.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileAvatarChangedEvent implements DomainEvent {

    public static final String TYPE = "user.profile.avatar.changed";

    private UUID uuid;
    private AvatarUrl avatarUrl;
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
