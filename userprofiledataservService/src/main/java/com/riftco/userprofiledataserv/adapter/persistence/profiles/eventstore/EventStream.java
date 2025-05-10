package com.riftco.userprofiledataserv.adapter.persistence.profiles.eventstore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "user_profile_events")
@NoArgsConstructor
class EventStream {

    @Id
    private String id;

    @Getter
    private List<EventDescriptor> events = new ArrayList<>();

    @Getter
    private UUID aggregateUUID;

    EventStream(UUID aggregateUUID) {
        this.id = aggregateUUID.toString();
        this.aggregateUUID = aggregateUUID;
    }

    void addEvents(List<EventDescriptor> newEvents) {
        this.events.addAll(newEvents);
    }
}
