package com.riftco.userprofiledataserv.application.port.out;

import com.riftco.userprofiledataserv.domain.UserProfile;

public interface SourceUserProfilePort {
    void source(UserProfile userProfile);
}
