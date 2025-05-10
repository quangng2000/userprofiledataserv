package com.riftco.userprofiledataserv.domain;

import com.riftco.userprofiledataserv.domain.common.AggregateRoot;
import com.riftco.userprofiledataserv.domain.event.DomainEvent;
import com.riftco.userprofiledataserv.domain.event.UserCreatedEvent;
import com.riftco.userprofiledataserv.domain.event.UserDeactivatedEvent;
import com.riftco.userprofiledataserv.domain.event.UserEmailChangedEvent;
import com.riftco.userprofiledataserv.domain.event.UserNameChangedEvent;
import com.riftco.userprofiledataserv.domain.event.UserPhoneChangedEvent;
import com.riftco.userprofiledataserv.domain.vo.DisplayName;
import com.riftco.userprofiledataserv.domain.vo.Email;
import com.riftco.userprofiledataserv.domain.vo.PhoneNumber;
import com.riftco.userprofiledataserv.domain.vo.TenantId;
import com.riftco.userprofiledataserv.domain.vo.UserId;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class User extends AggregateRoot {

    public static final User NONE = new User();

    private UserId userId;
    private TenantId tenantId;
    private UserState state;
    private String name;
    private Email email;
    private PhoneNumber contactNumber;
    private Instant createdAt;
    private Instant updatedAt;
    
    // Reference to the default profile of this user in their primary tenant
    // Actual profile data is stored in the UserProfile aggregate

    private User() {}

    /**
     * Creates a new user with the specified details.
     *
     * @param tenantId The tenant ID for the user
     * @param name The user's display name
     * @param email The user's email address
     * @param contactNumber The user's phone number
     * @return A new User instance
     * @throws IllegalArgumentException If any parameters are invalid
     */
    public User create(TenantId tenantId, String name, Email email, PhoneNumber contactNumber) {
        // Validate all parameters
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (contactNumber == null) {
            throw new IllegalArgumentException("Contact number cannot be null");
        }
        
        return this.applyEvent(new UserCreatedEvent(
                UUID.randomUUID(),
                UserId.generate(),
                tenantId,
                UserState.ACTIVATED,
                name,
                email,
                contactNumber,
                Instant.now()));
    }


    public static User from(UUID uuid, List<DomainEvent> history) {
        return history
                .stream()
                .reduce(
                        NONE,
                        (tx, event) -> tx.applyEvent(event, false),
                        (t1, t2) -> {throw new UnsupportedOperationException();}
                );
    }

    private User applyEvent(DomainEvent event) {
        return this.applyEvent(event, true);
    }

    private User applyEvent(DomainEvent event, boolean isNew) {
        final User user = this.apply(event);
        if (isNew) {
            user.getEvents().add(event);
        }
        return user;
    }

    private User apply(DomainEvent event) {
        if (event instanceof UserCreatedEvent) {
            return this.apply((UserCreatedEvent) event);
        } else if (event instanceof UserEmailChangedEvent) {
            return this.apply((UserEmailChangedEvent) event);
        } else if (event instanceof UserPhoneChangedEvent) {
            return this.apply((UserPhoneChangedEvent) event);
        } else if (event instanceof UserNameChangedEvent) {
            return this.apply((UserNameChangedEvent) event);
        } else if (event instanceof UserDeactivatedEvent) {
            return this.apply((UserDeactivatedEvent) event);
        } else {
            throw new IllegalArgumentException("Cannot handle event " + event.getClass());
        }
    }

    private User apply(UserCreatedEvent event) {
        this.setUUID(event.getUuid());
        this.userId = event.getUserId();
        this.tenantId = event.getTenantId();
        this.state = event.getState();
        this.name = event.getName();
        this.email = event.getEmail();
        this.contactNumber = event.getContactNumber();
        this.createdAt = event.getOccurredAt();
        this.updatedAt = event.getOccurredAt();
        return this;
    }

    private User apply(UserEmailChangedEvent event) {
        this.email = event.getEmail();
        this.updatedAt = event.getOccurredAt();
        return this;
    }
    
    private User apply(UserPhoneChangedEvent event) {
        this.contactNumber = event.getContactNumber();
        this.updatedAt = event.getOccurredAt();
        return this;
    }
    
    private User apply(UserNameChangedEvent event) {
        this.name = event.getName();
        this.updatedAt = event.getOccurredAt();
        return this;
    }
    
    private User apply(UserDeactivatedEvent event) {
        this.state = UserState.DEACTIVATED;
        this.updatedAt = event.getOccurredAt();
        return this;
    }
    
    public User deactivate() {
        if (this.state == UserState.DEACTIVATED) {
            throw new IllegalStateException("User is already deactivated");
        }
        return this.applyEvent(new UserDeactivatedEvent(getUUID(), Instant.now()));
    }
    
    /**
     * Updates the user's email address if it differs from the current one.
     * 
     * @param email The new email address
     * @return Updated User instance
     * @throws IllegalStateException if the new email is the same as the current one
     */
    public User changeEmail(Email email) {
        if (email.equals(this.email)) {
            throw new IllegalStateException("New email must differ from current email");
        }
        return this.applyEvent(new UserEmailChangedEvent(getUUID(), email, Instant.now()));
    }
    
    /**
     * Updates the user's phone number if it differs from the current one.
     * 
     * @param contactNumber The new phone number
     * @return Updated User instance
     * @throws IllegalStateException if the new phone number is the same as the current one
     */
    public User changePhoneNumber(PhoneNumber contactNumber) {
        if (contactNumber.equals(this.contactNumber)) {
            throw new IllegalStateException("New phone number must differ from current phone number");
        }
        return this.applyEvent(new UserPhoneChangedEvent(getUUID(), contactNumber, Instant.now()));
    }
    
    /**
     * Updates the user's name if it differs from the current one.
     * 
     * @param name The new name
     * @return Updated User instance
     * @throws IllegalStateException if the new name is the same as the current one
     */
    public User changeName(String name) {
        if (name.equals(this.name)) {
            throw new IllegalStateException("New name must differ from current name");
        }
        return this.applyEvent(new UserNameChangedEvent(getUUID(), name, Instant.now()));
    }
    
    public boolean isActive() {
        return UserState.ACTIVATED.equals(this.state);
    }
    
    // Getters
    public UserId getUserId() {
        return userId;
    }
    
    public TenantId getTenantId() {
        return tenantId;
    }
    
    public UserState getState() {
        return state;
    }
    
    public String getName() {
        return name;
    }
    
    public Email getEmail() {
        return email;
    }
    
    public PhoneNumber getContactNumber() {
        return contactNumber;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Creates a new profile for this user in the specified tenant.
     * Note: This is a factory method that should be used by application services
     * to ensure consistency between User and UserProfile entities.
     * 
     * @param displayName Display name for the profile
     * @return a new UserProfile instance (uncommitted)
     */
    public UserProfile createProfile(DisplayName displayName) {
        return UserProfile.NONE.create(this.userId, this.tenantId, displayName);
    }
    
    /**
     * Convenience method to create a profile using a string name.
     * 
     * @param displayName String display name to convert to DisplayName value object
     * @return a new UserProfile instance (uncommitted) 
     */
    public UserProfile createProfile(String displayName) {
        return createProfile(DisplayName.of(displayName));
    }

    public List<DomainEvent> getUncommittedEvents() {
        return this.getEvents();
    }

    public User markEventsAsCommitted() {
        this.getEvents().clear();
        return this;
    }

}
