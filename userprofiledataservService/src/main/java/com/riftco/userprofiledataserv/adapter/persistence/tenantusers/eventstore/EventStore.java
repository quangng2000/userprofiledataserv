package com.riftco.userprofiledataserv.adapter.persistence.tenantusers.eventstore;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;

public interface EventStore extends MongoRepository<EventStream, String> {
    Optional<EventStream> findByAggregateUUID(UUID uuid);

    default void saveEvents(UUID aggregateId, List<EventDescriptor> events) {
        final EventStream eventStream = this.findByAggregateUUID(aggregateId)
                .orElseGet(() -> new EventStream(aggregateId));
        eventStream.addEvents(events);
        save(eventStream);
    }

    default List<EventDescriptor> getEventsForAggregate(UUID aggregateId) {
        return this.findByAggregateUUID(aggregateId)
                .map(EventStream::getEvents)
                .orElse(emptyList());
    }
}
