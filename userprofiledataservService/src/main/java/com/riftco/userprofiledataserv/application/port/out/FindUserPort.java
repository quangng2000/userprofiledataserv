package com.riftco.userprofiledataserv.application.port.out;

import com.riftco.userprofiledataserv.domain.User;

import java.util.UUID;

public interface FindUserPort {
    User find(UUID uuid);
}
