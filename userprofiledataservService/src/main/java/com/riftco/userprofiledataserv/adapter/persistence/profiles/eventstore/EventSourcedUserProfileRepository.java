package com.riftco.userprofiledataserv.adapter.persistence.profiles.eventstore;

import com.riftco.userprofiledataserv.adapter.persistence.profiles.UserProfileRepository;
import com.riftco.userprofiledataserv.application.port.out.SendUserProfileEventToBroker;
import com.riftco.userprofiledataserv.domain.UserProfile;
import com.riftco.userprofiledataserv.domain.event.DomainEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class EventSourcedUserProfileRepository implements UserProfileRepository {

    private final EventStore eventStore;
    private final EventSerializer eventSerializer;
    private final SendUserProfileEventToBroker sendUserProfileEventToBroker;
    
    public EventSourcedUserProfileRepository(
            @Qualifier("profileEventStore") EventStore eventStore,
            @Qualifier("profileEventSerializer") EventSerializer eventSerializer,
            SendUserProfileEventToBroker sendUserProfileEventToBroker) {
        this.eventStore = eventStore;
        this.eventSerializer = eventSerializer;
        this.sendUserProfileEventToBroker = sendUserProfileEventToBroker;
    }

    @Override
    public UserProfile save(UserProfile aggregate) {
        final List<DomainEvent> pendingEvents = aggregate.getUncommittedEvents();
        this.eventStore.saveEvents(
                aggregate.getUUID(),
                pendingEvents
                        .stream()
                        .map(this.eventSerializer::serialize)
                        .collect(Collectors.toList())
        );
        pendingEvents.forEach(this.sendUserProfileEventToBroker::send);
        return aggregate.markEventsAsCommitted();
    }

    @Override
    public UserProfile getByUUID(UUID uuid) {
        return UserProfile.from(uuid, this.getRelatedEvents(uuid));
    }

    @Override
    public UserProfile getByUUIDat(UUID uuid, Instant at) {
        return UserProfile.from(
                uuid,
                this.getRelatedEvents(uuid)
                        .stream()
                        .filter(evt -> !evt.getOccurredAt().isAfter(at))
                        .collect(Collectors.toList())
        );
    }

    private List<DomainEvent> getRelatedEvents(UUID uuid) {
        return this.eventStore.getEventsForAggregate(uuid)
                .stream()
                .map(this.eventSerializer::deserialize)
                .collect(Collectors.toList());
    }
}
