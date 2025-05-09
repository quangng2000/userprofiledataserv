package com.riftco.userprofiledataserv.domain.common;

import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode
public abstract class Entity {
    private UUID uuid;

    protected Entity() {}

    protected void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return this.uuid;
    }
}
