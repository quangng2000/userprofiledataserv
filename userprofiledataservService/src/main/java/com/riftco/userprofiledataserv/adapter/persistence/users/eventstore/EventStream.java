package com.riftco.userprofiledataserv.adapter.persistence.users.eventstore;

import lombok.Getter;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Document(collection = "users_event_streams")
class EventStream {

    @Id
    @Indexed(unique = true)
    private String id;

    @Getter
    private UUID aggregateUUID;

    @Version
    private long version;

    private List<EventDescriptor> events = new ArrayList<>();

    // Required by MongoDB - using @NoArgsConstructor would be better but preserving compatibility
    private EventStream() {
    }

    public EventStream(UUID aggregateUUID) {
        this.aggregateUUID = aggregateUUID;
    }

    void addEvents(List<EventDescriptor> events) {
        this.events.addAll(events);
    }

    List<EventDescriptor> getEvents() {
        return events.stream()
                .sorted(Comparator.comparing(EventDescriptor::getOccurredAt))
                .collect(Collectors.toList());
    }
}
