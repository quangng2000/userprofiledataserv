package com.riftco.userprofiledataserv.adapter.persistence.tenantusers.eventstore;

import com.riftco.userprofiledataserv.adapter.persistence.tenantusers.TenantUserRepository;
import com.riftco.userprofiledataserv.application.port.out.SendTenantUserEventToBroker;
import com.riftco.userprofiledataserv.domain.TenantUser;
import com.riftco.userprofiledataserv.domain.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventSourcedTenantUserRepository implements TenantUserRepository {

    private final EventStore eventStore;
    private final EventSerializer eventSerializer;
    private final SendTenantUserEventToBroker sendTenantUserEventToBroker;

    @Override
    public TenantUser save(TenantUser aggregate) {
        final List<DomainEvent> pendingEvents = aggregate.getUncommittedEvents();
        this.eventStore.saveEvents(
                aggregate.getUUID(),
                pendingEvents
                        .stream()
                        .map(this.eventSerializer::serialize)
                        .collect(Collectors.toList())
        );
        pendingEvents.forEach(this.sendTenantUserEventToBroker::send);
        return aggregate.markEventsAsCommitted();
    }

    @Override
    public TenantUser getByUUID(UUID uuid) {
        return TenantUser.from(uuid, this.getRelatedEvents(uuid));
    }

    @Override
    public TenantUser getByUUIDat(UUID uuid, Instant at) {
        return TenantUser.from(
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
