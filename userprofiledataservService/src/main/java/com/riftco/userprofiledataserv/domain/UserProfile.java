package com.riftco.userprofiledataserv.domain;

import com.riftco.userprofiledataserv.domain.common.AggregateRoot;
import com.riftco.userprofiledataserv.domain.event.DomainEvent;
import com.riftco.userprofiledataserv.domain.event.UserProfileCreatedEvent;
import com.riftco.userprofiledataserv.domain.event.UserProfileDisplayNameChangedEvent;
import com.riftco.userprofiledataserv.domain.event.UserProfileAvatarChangedEvent;
import com.riftco.userprofiledataserv.domain.event.UserProfileBiographyChangedEvent;
import com.riftco.userprofiledataserv.domain.event.UserProfileJobInfoChangedEvent;
import com.riftco.userprofiledataserv.domain.event.UserProfileLocationChangedEvent;
import com.riftco.userprofiledataserv.domain.vo.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents extended profile information for a user.
 * In a multi-tenant environment, each user can have different profile data per tenant.
 */
public final class UserProfile extends AggregateRoot {

    public static final UserProfile NONE = new UserProfile();

    private UserId userId;
    private TenantId tenantId;
    
    // Core profile data
    private DisplayName displayName;
    private AvatarUrl avatarUrl;
    private Biography biography;
    private JobTitle jobTitle;
    private Department department;
    private Location location;
    
    // Social media links
    private LinkedInUrl linkedInUrl;
    private TwitterUrl twitterUrl;
    private GitHubUrl githubUrl;
    
    // User preferences and settings (extensible)
    private Map<String, String> preferences;
    
    // Extended attributes (dynamic/custom fields per tenant)
    private Map<String, String> attributes;
    
    private Instant createdAt;
    private Instant updatedAt;

    private UserProfile() {
        this.preferences = new HashMap<>();
        this.attributes = new HashMap<>();
    }

    public UserProfile create(UserId userId, TenantId tenantId, DisplayName displayName) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }
        
        return this.applyEvent(new UserProfileCreatedEvent(
                UUID.randomUUID(),
                userId,
                tenantId,
                displayName,
                AvatarUrl.empty(),
                Instant.now()
        ));
    }
    
    /**
     * Updates the user profile's display name if it differs from the current one.
     * 
     * @param displayName The new display name
     * @return Updated UserProfile instance
     * @throws IllegalStateException if the new display name is the same as the current one
     */
    public UserProfile changeDisplayName(DisplayName displayName) {
        if (displayName.equals(this.displayName)) {
            throw new IllegalStateException("New display name must differ from current display name");
        }
        return this.applyEvent(new UserProfileDisplayNameChangedEvent(getUUID(), displayName, Instant.now()));
    }
    
    /**
     * Updates the user profile's avatar URL if it differs from the current one.
     * 
     * @param avatarUrl The new avatar URL
     * @return Updated UserProfile instance
     * @throws IllegalStateException if the new avatar URL is the same as the current one
     */
    public UserProfile changeAvatar(AvatarUrl avatarUrl) {
        if (avatarUrl.equals(this.avatarUrl)) {
            throw new IllegalStateException("New avatar URL must differ from current avatar URL");
        }
        return this.applyEvent(new UserProfileAvatarChangedEvent(getUUID(), avatarUrl, Instant.now()));
    }
    
    /**
     * Updates the user profile's biography if it differs from the current one.
     * 
     * @param biography The new biography
     * @return Updated UserProfile instance
     * @throws IllegalStateException if the new biography is the same as the current one
     */
    public UserProfile changeBiography(Biography biography) {
        if (biography.equals(this.biography)) {
            throw new IllegalStateException("New biography must differ from current biography");
        }
        return this.applyEvent(new UserProfileBiographyChangedEvent(getUUID(), biography, Instant.now()));
    }
    
    /**
     * Updates the user profile's job information if it differs from the current one.
     * 
     * @param jobTitle The new job title
     * @param department The new department
     * @return Updated UserProfile instance
     * @throws IllegalStateException if both the new job title and department are the same as the current ones
     */
    public UserProfile changeJobInfo(JobTitle jobTitle, Department department) {
        if (jobTitle.equals(this.jobTitle) && department.equals(this.department)) {
            throw new IllegalStateException("At least one job info field must differ from current values");
        }
        return this.applyEvent(new UserProfileJobInfoChangedEvent(getUUID(), jobTitle, department, Instant.now()));
    }
    
    /**
     * Updates the user profile's location if it differs from the current one.
     * 
     * @param location The new location
     * @return Updated UserProfile instance
     * @throws IllegalStateException if the new location is the same as the current one
     */
    public UserProfile changeLocation(Location location) {
        if (location.equals(this.location)) {
            throw new IllegalStateException("New location must differ from current location");
        }
        return this.applyEvent(new UserProfileLocationChangedEvent(getUUID(), location, Instant.now()));
    }
    
    public UserProfile updateSocialLinks(LinkedInUrl linkedInUrl, TwitterUrl twitterUrl, GitHubUrl githubUrl) {
        this.linkedInUrl = linkedInUrl;
        this.twitterUrl = twitterUrl;
        this.githubUrl = githubUrl;
        this.updatedAt = Instant.now();
        return this;
    }
    
    public UserProfile setPreference(String key, String value) {
        this.preferences.put(key, value);
        this.updatedAt = Instant.now();
        return this;
    }
    
    public UserProfile setAttribute(String key, String value) {
        this.attributes.put(key, value);
        this.updatedAt = Instant.now();
        return this;
    }

    public static UserProfile from(UUID uuid, List<DomainEvent> history) {
        return history
                .stream()
                .reduce(
                        NONE,
                        (profile, event) -> profile.applyEvent(event, false),
                        (p1, p2) -> {throw new UnsupportedOperationException();}
                );
    }

    private UserProfile applyEvent(DomainEvent event) {
        return this.applyEvent(event, true);
    }

    private UserProfile applyEvent(DomainEvent event, boolean isNew) {
        final UserProfile profile = this.apply(event);
        if (isNew) {
            profile.getEvents().add(event);
        }
        return profile;
    }

    private UserProfile apply(DomainEvent event) {
        if (event instanceof UserProfileCreatedEvent) {
            return this.apply((UserProfileCreatedEvent) event);
        } else if (event instanceof UserProfileDisplayNameChangedEvent) {
            return this.apply((UserProfileDisplayNameChangedEvent) event);
        } else if (event instanceof UserProfileAvatarChangedEvent) {
            return this.apply((UserProfileAvatarChangedEvent) event);
        } else if (event instanceof UserProfileBiographyChangedEvent) {
            return this.apply((UserProfileBiographyChangedEvent) event);
        } else if (event instanceof UserProfileJobInfoChangedEvent) {
            return this.apply((UserProfileJobInfoChangedEvent) event);
        } else if (event instanceof UserProfileLocationChangedEvent) {
            return this.apply((UserProfileLocationChangedEvent) event);
        } else {
            throw new IllegalArgumentException("Cannot handle event " + event.getClass());
        }
    }

    private UserProfile apply(UserProfileCreatedEvent event) {
        this.setUUID(event.getUuid());
        this.userId = event.getUserId();
        this.tenantId = event.getTenantId();
        this.displayName = event.getDisplayName();
        this.avatarUrl = event.getAvatarUrl();
        this.biography = Biography.empty();
        this.jobTitle = JobTitle.empty();
        this.department = Department.empty();
        this.location = Location.empty();
        this.linkedInUrl = LinkedInUrl.empty();
        this.twitterUrl = TwitterUrl.empty();
        this.githubUrl = GitHubUrl.empty();
        this.createdAt = event.getOccurredAt();
        this.updatedAt = event.getOccurredAt();
        return this;
    }

    private UserProfile apply(UserProfileDisplayNameChangedEvent event) {
        this.displayName = event.getDisplayName();
        this.updatedAt = event.getOccurredAt();
        return this;
    }
    
    private UserProfile apply(UserProfileAvatarChangedEvent event) {
        this.avatarUrl = event.getAvatarUrl();
        this.updatedAt = event.getOccurredAt();
        return this;
    }
    
    private UserProfile apply(UserProfileBiographyChangedEvent event) {
        this.biography = event.getBiography();
        this.updatedAt = event.getOccurredAt();
        return this;
    }
    
    private UserProfile apply(UserProfileJobInfoChangedEvent event) {
        this.jobTitle = event.getJobTitle();
        this.department = event.getDepartment();
        this.updatedAt = event.getOccurredAt();
        return this;
    }
    
    private UserProfile apply(UserProfileLocationChangedEvent event) {
        this.location = event.getLocation();
        this.updatedAt = event.getOccurredAt();
        return this;
    }

    public List<DomainEvent> getUncommittedEvents() {
        return this.getEvents();
    }

    public UserProfile markEventsAsCommitted() {
        this.getEvents().clear();
        return this;
    }

    // Getters
    public UserId getUserId() {
        return userId;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public DisplayName getDisplayName() {
        return displayName;
    }

    public AvatarUrl getAvatarUrl() {
        return avatarUrl;
    }

    public Biography getBiography() {
        return biography;
    }

    public JobTitle getJobTitle() {
        return jobTitle;
    }

    public Department getDepartment() {
        return department;
    }

    public Location getLocation() {
        return location;
    }

    public LinkedInUrl getLinkedInUrl() {
        return linkedInUrl;
    }

    public TwitterUrl getTwitterUrl() {
        return twitterUrl;
    }

    public GitHubUrl getGithubUrl() {
        return githubUrl;
    }

    public Map<String, String> getPreferences() {
        return new HashMap<>(preferences);
    }

    public Map<String, String> getAttributes() {
        return new HashMap<>(attributes);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
    
    public String getPreference(String key) {
        return preferences.get(key);
    }
    
    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public UserProfile createBasicProfile(UserId userId, TenantId tenantId, DisplayName displayName, AvatarUrl avatarUrl, Biography biography) {
        this.userId = userId;
        this.tenantId = tenantId;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
        this.biography = biography;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        return this;
    }
}
