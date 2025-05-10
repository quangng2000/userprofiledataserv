package com.riftco.userprofiledataserv.adapter.persistence.tenantusers.eventstore;

import com.riftco.userprofiledataserv.adapter.persistence.tenantusers.TenantUserRepository;
import com.riftco.userprofiledataserv.application.port.out.SendTenantUserEventToBroker;
import com.riftco.userprofiledataserv.domain.TenantUser;
import com.riftco.userprofiledataserv.domain.event.DomainEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class EventSourcedTenantUserRepository implements TenantUserRepository {

    private final EventStore eventStore;
    private final EventSerializer eventSerializer;
    private final SendTenantUserEventToBroker sendTenantUserEventToBroker;
    
    public EventSourcedTenantUserRepository(
            @Qualifier("tenantUserEventStore") EventStore eventStore,
            @Qualifier("tenantUserEventSerializer") EventSerializer eventSerializer,
            SendTenantUserEventToBroker sendTenantUserEventToBroker) {
        this.eventStore = eventStore;
        this.eventSerializer = eventSerializer;
        this.sendTenantUserEventToBroker = sendTenantUserEventToBroker;
    }

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
    
    @Override
    public int countByTenantId(UUID tenantId) {
        // In an event-sourced system, we need to fetch and reconstruct all tenant users
        // then count those that match the given tenant ID
        return findByTenantId(tenantId).size();
    }
    
    @Override
    public List<TenantUser> findByTenantId(UUID tenantId) {
        // Since this is an event-sourced repository, we need to:
        // 1. Find all available tenant-user events
        // 2. Reconstruct the tenant-user aggregates
        // 3. Filter those that match the tenant ID
        // Note: In a real implementation, you would have an optimized query in the event store
        
        // This is a simplified implementation that assumes access to all tenant-user IDs
        // A production implementation would use a projection/read model to efficiently query by tenant
        List<TenantUser> result = new ArrayList<>();
        
        // For now, log that this method was called
        // In a real implementation, you would query a read model or create a projection
        System.out.println("Finding tenant users for tenant: " + tenantId);
        
        return result;
    }
}
