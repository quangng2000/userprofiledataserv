package com.riftco.userprofiledataserv.adapter.persistence.users.eventstore;

import com.riftco.userprofiledataserv.adapter.persistence.users.UserRepository;
import com.riftco.userprofiledataserv.application.port.out.SendUserEventToBroker;
import com.riftco.userprofiledataserv.domain.User;
import com.riftco.userprofiledataserv.domain.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventSourcedUserRepository implements UserRepository {

    private final EventStore eventStore;
    private final EventSerializer eventSerializer;
    private final SendUserEventToBroker sendUserEventToBroker;

    @Override
    public User save(User aggregate) {
        final List<DomainEvent> pendingEvents = aggregate.getUncommittedEvents();
        this.eventStore.saveEvents(
                aggregate.getUUID(),
                pendingEvents
                        .stream()
                        .map(this.eventSerializer::serialize)
                        .collect(Collectors.toList())
        );
        pendingEvents.forEach(this.sendUserEventToBroker::send);
        return aggregate.markEventsAsCommitted();
    }

    @Override
    public User getByUUID(UUID uuid) {
        return User.from(uuid, this.getRelatedEvents(uuid));
    }

    @Override
    public User getByUUIDat(UUID uuid, Instant at) {
        return User.from(
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
