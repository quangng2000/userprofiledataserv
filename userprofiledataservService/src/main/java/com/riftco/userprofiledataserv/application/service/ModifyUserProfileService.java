package com.riftco.userprofiledataserv.application.service;

import com.riftco.userprofiledataserv.application.port.in.ModifyUserProfileUseCase;
import com.riftco.userprofiledataserv.application.port.out.FindUserProfilePort;
import com.riftco.userprofiledataserv.application.port.out.SourceUserProfilePort;
import com.riftco.userprofiledataserv.domain.UserProfile;
import com.riftco.userprofiledataserv.domain.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for modifying user profiles.
 * Provides transactional handling and operations for updating profile information.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ModifyUserProfileService implements ModifyUserProfileUseCase {

    private final FindUserProfilePort findUserProfilePort;
    private final SourceUserProfilePort sourceUserProfilePort;

    /**
     * Changes a profile's display name
     *
     * @param command The command containing profile ID and new display name
     */
    @Override
    @Transactional
    public void changeDisplayName(ChangeDisplayNameCommand command) {
        log.debug("Changing display name for profile: {}", command.getProfileId());
        
        UserProfile profile = this.findUserProfilePort.find(command.getProfileId());
        profile.changeDisplayName(DisplayName.of(command.getDisplayName()));
        
        this.sourceUserProfilePort.source(profile);
        
        log.info("Display name changed successfully for profile: {}", command.getProfileId());
    }

    /**
     * Changes a profile's avatar
     *
     * @param command The command containing profile ID and new avatar URL
     */
    @Override
    @Transactional
    public void changeAvatar(ChangeAvatarCommand command) {
        log.debug("Changing avatar for profile: {}", command.getProfileId());
        
        UserProfile profile = this.findUserProfilePort.find(command.getProfileId());
        profile.changeAvatar(command.getAvatarUrl() != null ? 
                AvatarUrl.of(command.getAvatarUrl()) : null);
        
        this.sourceUserProfilePort.source(profile);
        
        log.info("Avatar changed successfully for profile: {}", command.getProfileId());
    }

    /**
     * Changes a profile's biography
     *
     * @param command The command containing profile ID and new biography
     */
    @Override
    @Transactional
    public void changeBiography(ChangeBiographyCommand command) {
        log.debug("Changing biography for profile: {}", command.getProfileId());
        
        UserProfile profile = this.findUserProfilePort.find(command.getProfileId());
        profile.changeBiography(command.getBiography() != null ?
                Biography.of(command.getBiography()) : null);
        
        this.sourceUserProfilePort.source(profile);
        
        log.info("Biography changed successfully for profile: {}", command.getProfileId());
    }

    /**
     * Changes a profile's job information
     *
     * @param command The command containing profile ID and new job information
     */
    @Override
    @Transactional
    public void changeJobInfo(ChangeJobInfoCommand command) {
        log.debug("Changing job info for profile: {}", command.getProfileId());
        
        UserProfile profile = this.findUserProfilePort.find(command.getProfileId());
        
        JobTitle jobTitle = command.getJobTitle() != null ?
                JobTitle.of(command.getJobTitle()) : null;
        Department department = command.getDepartment() != null ?
                Department.of(command.getDepartment()) : null;
        
        profile.changeJobInfo(jobTitle, department);
        
        this.sourceUserProfilePort.source(profile);
        
        log.info("Job info changed successfully for profile: {}", command.getProfileId());
    }

    /**
     * Changes a profile's location
     *
     * @param command The command containing profile ID and new location
     */
    @Override
    @Transactional
    public void changeLocation(ChangeLocationCommand command) {
        log.debug("Changing location for profile: {}", command.getProfileId());
        
        UserProfile profile = this.findUserProfilePort.find(command.getProfileId());
        
        Location location = command.getLocation() != null ?
                Location.of(command.getLocation()) : null;
        
        profile.changeLocation(location);
        
        this.sourceUserProfilePort.source(profile);
        
        log.info("Location changed successfully for profile: {}", command.getProfileId());
    }

    /**
     * Changes a profile's social media links
     *
     * @param command The command containing profile ID and new social media links
     */
    @Override
    @Transactional
    public void changeSocialLinks(ChangeSocialLinksCommand command) {
        log.debug("Changing social links for profile: {}", command.getProfileId());
        
        UserProfile profile = this.findUserProfilePort.find(command.getProfileId());
        
        LinkedInUrl linkedInUrl = command.getLinkedInUrl() != null ?
                LinkedInUrl.of(command.getLinkedInUrl()) : null;
        TwitterUrl twitterUrl = command.getTwitterUrl() != null ?
                TwitterUrl.of(command.getTwitterUrl()) : null;
        GitHubUrl gitHubUrl = command.getGitHubUrl() != null ?
                GitHubUrl.of(command.getGitHubUrl()) : null;
        
        profile.updateSocialLinks(linkedInUrl, twitterUrl, gitHubUrl);
        
        this.sourceUserProfilePort.source(profile);
        
        log.info("Social links changed successfully for profile: {}", command.getProfileId());
    }
}
