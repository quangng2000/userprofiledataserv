package com.riftco.userprofiledataserv.domain.event;

import com.riftco.userprofiledataserv.domain.vo.Department;
import com.riftco.userprofiledataserv.domain.vo.JobTitle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Event that captures when a user's profile job information is changed.
 * This includes job title and department.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileJobInfoChangedEvent implements DomainEvent {

    public static final String TYPE = "user.profile.jobinfo.changed";

    private UUID uuid;
    private JobTitle jobTitle;
    private Department department;
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
