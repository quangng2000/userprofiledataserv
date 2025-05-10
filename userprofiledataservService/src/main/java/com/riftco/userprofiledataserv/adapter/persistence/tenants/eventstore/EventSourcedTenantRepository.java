package com.riftco.userprofiledataserv.adapter.persistence.tenants.eventstore;

import com.riftco.userprofiledataserv.adapter.persistence.tenants.TenantRepository;
import com.riftco.userprofiledataserv.application.port.out.SendTenantEventToBroker;
import com.riftco.userprofiledataserv.domain.Tenant;
import com.riftco.userprofiledataserv.domain.event.DomainEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class EventSourcedTenantRepository implements TenantRepository {

    private final EventStore eventStore;
    private final EventSerializer eventSerializer;
    private final SendTenantEventToBroker sendTenantEventToBroker;
    
    public EventSourcedTenantRepository(
            @Qualifier("tenantEventStore") EventStore eventStore,
            @Qualifier("tenantEventSerializer") EventSerializer eventSerializer,
            SendTenantEventToBroker sendTenantEventToBroker) {
        this.eventStore = eventStore;
        this.eventSerializer = eventSerializer;
        this.sendTenantEventToBroker = sendTenantEventToBroker;
    }

    @Override
    public Tenant save(Tenant aggregate) {
        final List<DomainEvent> pendingEvents = aggregate.getUncommittedEvents();
        this.eventStore.saveEvents(
                aggregate.getUUID(),
                pendingEvents
                        .stream()
                        .map(this.eventSerializer::serialize)
                        .collect(Collectors.toList())
        );
        pendingEvents.forEach(this.sendTenantEventToBroker::send);
        return aggregate.markEventsAsCommitted();
    }

    @Override
    public Tenant getByUUID(UUID uuid) {
        return Tenant.from(uuid, this.getRelatedEvents(uuid));
    }

    @Override
    public Tenant getByUUIDat(UUID uuid, Instant at) {
        return Tenant.from(
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
