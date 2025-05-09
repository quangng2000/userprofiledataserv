package com.riftco.userprofiledataserv.application.port.out;

import com.riftco.userprofiledataserv.domain.User;

public interface SourceUserPort {
    void source(User user);
}
