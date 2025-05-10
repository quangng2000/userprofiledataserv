package com.riftco.userprofiledataserv.application.port.in;

import lombok.Value;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public interface ModifyUserProfileUseCase {

    void changeDisplayName(ChangeDisplayNameCommand command);
    
    void changeAvatar(ChangeAvatarCommand command);
    
    void changeBiography(ChangeBiographyCommand command);
    
    void changeJobInfo(ChangeJobInfoCommand command);
    
    void changeLocation(ChangeLocationCommand command);
    
    void changeSocialLinks(ChangeSocialLinksCommand command);

    @Value
    class ChangeDisplayNameCommand {
        @NotNull
        private UUID profileId;
        
        @NotEmpty
        private String displayName;
    }
    
    @Value
    class ChangeAvatarCommand {
        @NotNull
        private UUID profileId;
        
        private String avatarUrl;
    }
    
    @Value
    class ChangeBiographyCommand {
        @NotNull
        private UUID profileId;
        
        private String biography;
    }
    
    @Value
    class ChangeJobInfoCommand {
        @NotNull
        private UUID profileId;
        
        private String jobTitle;
        
        private String department;
    }
    
    @Value
    class ChangeLocationCommand {
        @NotNull
        private UUID profileId;
        
        private String location;
    }
    
    @Value
    class ChangeSocialLinksCommand {
        @NotNull
        private UUID profileId;
        
        private String linkedInUrl;
        
        private String twitterUrl;
        
        private String gitHubUrl;
    }
}
