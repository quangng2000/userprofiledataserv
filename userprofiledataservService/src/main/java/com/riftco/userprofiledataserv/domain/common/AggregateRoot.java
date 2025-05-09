package com.riftco.userprofiledataserv.domain.common;
import com.riftco.userprofiledataserv.domain.event.DomainEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot extends Entity {
    private int version;
    private List<DomainEvent> events;

    protected AggregateRoot() {
        events = new ArrayList<>();
    }

    protected List<DomainEvent> getEvents() {
        return this.events;
    }

}
