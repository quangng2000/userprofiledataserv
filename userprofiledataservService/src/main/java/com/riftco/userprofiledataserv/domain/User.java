package com.riftco.userprofiledataserv.domain;
import com.riftco.userprofiledataserv.domain.event.DomainEvent;
import com.riftco.userprofiledataserv.domain.event.UserCreatedEvent;
import com.riftco.userprofiledataserv.domain.event.UserNameChangedEvent;

import com.riftco.userprofiledataserv.domain.common.AggregateRoot;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class User extends AggregateRoot {

    public static final User NONE = new User();

    private UserState state;
    private String name;
    private String email;
    private String contactNumber;

    private User() {}

    public User create(UserState state, String name,
                       String email, String contactNumber) {
        return this.applyEvent(new UserCreatedEvent(
                UUID.randomUUID(),
                state,
                name,
                email,
                contactNumber,
                Instant.now()));
    }

    public User changeName(String name) {
        if (name.equals(this.name)) {
            throw new IllegalStateException("New name must differ old name!");
        }
        return this.applyEvent(new UserNameChangedEvent(getUUID(), name, Instant.now()));
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
        } else if (event instanceof UserNameChangedEvent) {
            return this.apply((UserNameChangedEvent) event);
        } else {
            throw new IllegalArgumentException("Cannot handle event " + event.getClass());
        }
    }

    private User apply(UserCreatedEvent event) {
        this.setUUID(event.getUuid());
        this.state = event.getState();
        this.name = event.getName();
        this.email = event.getEmail();
        this.contactNumber = event.getContactNumber();
        return this;
    }

    private User apply(UserNameChangedEvent event) {
        this.name = event.getName();
        return this;
    }

    public List<DomainEvent> getUncommittedEvents() {
        return this.getEvents();
    }

    public User markEventsAsCommitted() {
        this.getEvents().clear();
        return this;
    }

}
