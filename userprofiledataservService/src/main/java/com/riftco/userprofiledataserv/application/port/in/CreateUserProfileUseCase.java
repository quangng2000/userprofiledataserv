package com.riftco.userprofiledataserv.application.port.in;

import com.riftco.userprofiledataserv.domain.vo.AvatarUrl;
import com.riftco.userprofiledataserv.domain.vo.Biography;
import com.riftco.userprofiledataserv.domain.vo.Department;
import com.riftco.userprofiledataserv.domain.vo.DisplayName;
import com.riftco.userprofiledataserv.domain.vo.GitHubUrl;
import com.riftco.userprofiledataserv.domain.vo.JobTitle;
import com.riftco.userprofiledataserv.domain.vo.LinkedInUrl;
import com.riftco.userprofiledataserv.domain.vo.Location;
import com.riftco.userprofiledataserv.domain.vo.TwitterUrl;
import lombok.Value;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public interface CreateUserProfileUseCase {

    void createUserProfile(CreateUserProfileCommand command);

    @Value
    class CreateUserProfileCommand {
        @NotNull
        private UUID userId;
        
        @NotNull
        private UUID tenantId;
        
        @NotEmpty
        private String displayName;
        
        private String avatarUrl;
        
        private String biography;
        
        private String jobTitle;
        
        private String department;
        
        private String location;
        
        private String linkedInUrl;
        
        private String twitterUrl;
        
        private String gitHubUrl;
    }
}
