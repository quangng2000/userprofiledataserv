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
                userId.toUUID(),
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
     * @param uuid
     * @param displayName The new display name
     * @return Updated UserProfile instance
     * @throws IllegalStateException if the new display name is the same as the current one
     */
    public UserProfile changeDisplayName(UUID uuid, DisplayName displayName) {
        if (displayName.equals(this.displayName)) {
            throw new IllegalStateException("New display name must differ from current display name");
        }
        return this.applyEvent(new UserProfileDisplayNameChangedEvent(uuid, displayName, Instant.now()));
    }
    
    /**
     * Updates the user profile's avatar URL if it differs from the current one.
     *
     * @param uuid
     * @param avatarUrl The new avatar URL
     * @return Updated UserProfile instance
     * @throws IllegalStateException if the new avatar URL is the same as the current one
     */
    public UserProfile changeAvatar(UUID uuid, AvatarUrl avatarUrl) {
        if (avatarUrl.equals(this.avatarUrl)) {
            throw new IllegalStateException("New avatar URL must differ from current avatar URL");
        }
        return this.applyEvent(new UserProfileAvatarChangedEvent(uuid, avatarUrl, Instant.now()));
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
     * @param uuid
     * @param jobTitle   The new job title
     * @param department The new department
     * @return Updated UserProfile instance
     * @throws IllegalStateException if both the new job title and department are the same as the current ones
     */
    public UserProfile changeJobInfo(UUID uuid, JobTitle jobTitle, Department department) {
        if (jobTitle.equals(this.jobTitle) && department.equals(this.department)) {
            throw new IllegalStateException("At least one job info field must differ from current values");
        }
        return this.applyEvent(new UserProfileJobInfoChangedEvent(uuid, jobTitle, department, Instant.now()));
    }
    
    /**
     * Updates the user profile's location if it differs from the current one.
     *
     * @param uuid
     * @param location The new location
     * @return Updated UserProfile instance
     * @throws IllegalStateException if the new location is the same as the current one
     */
    public UserProfile changeLocation(UUID uuid, Location location) {
        if (location.equals(this.location)) {
            throw new IllegalStateException("New location must differ from current location");
        }
        return this.applyEvent(new UserProfileLocationChangedEvent(uuid, location, Instant.now()));
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

    /**
     * Creates a new user profile with all possible attributes in a single event.
     * This method is preferred for initial profile creation to avoid multiple change events.
     * 
     * @param userId The user ID
     * @param tenantId The tenant ID
     * @param displayName The display name
     * @param avatarUrl The avatar URL (can be null)
     * @param biography The biography (can be null)
     * @param jobTitle The job title (can be null)
     * @param department The department (can be null)
     * @param location The location (can be null)
     * @param linkedInUrl The LinkedIn URL (can be null)
     * @param twitterUrl The Twitter URL (can be null)
     * @param githubUrl The GitHub URL (can be null)
     * @return A new user profile instance with all provided attributes
     */
    public UserProfile createCompleteProfile(
            UserId userId,
            TenantId tenantId,
            DisplayName displayName,
            AvatarUrl avatarUrl,
            Biography biography,
            JobTitle jobTitle,
            Department department,
            Location location,
            LinkedInUrl linkedInUrl,
            TwitterUrl twitterUrl,
            GitHubUrl githubUrl) {
        
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }
        if (displayName == null) {
            throw new IllegalArgumentException("Display name cannot be null");
        }
        
        // Use provided values or empty values if null
        AvatarUrl finalAvatarUrl = avatarUrl != null ? avatarUrl : AvatarUrl.empty();
        Biography finalBiography = biography != null ? biography : Biography.empty();
        JobTitle finalJobTitle = jobTitle != null ? jobTitle : JobTitle.empty();
        Department finalDepartment = department != null ? department : Department.empty();
        Location finalLocation = location != null ? location : Location.empty();
        LinkedInUrl finalLinkedInUrl = linkedInUrl != null ? linkedInUrl : LinkedInUrl.empty();
        TwitterUrl finalTwitterUrl = twitterUrl != null ? twitterUrl : TwitterUrl.empty();
        GitHubUrl finalGithubUrl = githubUrl != null ? githubUrl : GitHubUrl.empty();
        
        return this.applyEvent(new UserProfileCreatedEvent(
                UUID.randomUUID(),
                userId,
                tenantId,
                displayName,
                finalAvatarUrl,
                finalBiography,
                finalJobTitle,
                finalDepartment,
                finalLocation,
                finalLinkedInUrl,
                finalTwitterUrl,
                finalGithubUrl,
                Instant.now()
        ));
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
        this.biography = event.getBiography() != null ? event.getBiography() : Biography.empty();
        this.jobTitle = event.getJobTitle() != null ? event.getJobTitle() : JobTitle.empty();
        this.department = event.getDepartment() != null ? event.getDepartment() : Department.empty();
        this.location = event.getLocation() != null ? event.getLocation() : Location.empty();
        this.linkedInUrl = event.getLinkedInUrl() != null ? event.getLinkedInUrl() : LinkedInUrl.empty();
        this.twitterUrl = event.getTwitterUrl() != null ? event.getTwitterUrl() : TwitterUrl.empty();
        this.githubUrl = event.getGithubUrl() != null ? event.getGithubUrl() : GitHubUrl.empty();
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
