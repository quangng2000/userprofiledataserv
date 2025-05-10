package com.riftco.userprofiledataserv.domain.event;

import com.riftco.userprofiledataserv.domain.vo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileCreatedEvent implements DomainEvent {

    public static final String TYPE = "user.profile.created";

    private UUID uuid;
    private UserId userId;
    private TenantId tenantId;
    private DisplayName displayName;
    private AvatarUrl avatarUrl;
    private Biography biography;
    private JobTitle jobTitle;
    private Department department;
    private Location location;
    private LinkedInUrl linkedInUrl;
    private TwitterUrl twitterUrl;
    private GitHubUrl githubUrl;
    private Instant occurredAt;

    /**
     * Constructor for backward compatibility with previous version
     */
    public UserProfileCreatedEvent(UUID uuid, UserId userId, TenantId tenantId, 
                                  DisplayName displayName, AvatarUrl avatarUrl, 
                                  Instant occurredAt) {
        this.uuid = uuid;
        this.userId = userId;
        this.tenantId = tenantId;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
        this.biography = Biography.empty();
        this.jobTitle = JobTitle.empty();
        this.department = Department.empty();
        this.location = Location.empty();
        this.linkedInUrl = LinkedInUrl.empty();
        this.twitterUrl = TwitterUrl.empty();
        this.githubUrl = GitHubUrl.empty();
        this.occurredAt = occurredAt;
    }

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
