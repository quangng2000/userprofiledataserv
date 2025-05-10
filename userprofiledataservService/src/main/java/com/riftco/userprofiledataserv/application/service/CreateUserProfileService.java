package com.riftco.userprofiledataserv.application.service;

import com.riftco.userprofiledataserv.application.port.in.CreateUserProfileUseCase;
import com.riftco.userprofiledataserv.application.port.out.FindTenantPort;
import com.riftco.userprofiledataserv.application.port.out.FindUserPort;
import com.riftco.userprofiledataserv.application.port.out.SourceUserProfilePort;
import com.riftco.userprofiledataserv.domain.UserProfile;
import com.riftco.userprofiledataserv.domain.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.UUID;

import static com.riftco.userprofiledataserv.domain.UserProfile.NONE;

/**
 * Service implementation for creating user profiles.
 * Provides transactional handling and validation.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CreateUserProfileService implements CreateUserProfileUseCase {

    private final FindUserPort findUserPort;
    private final FindTenantPort findTenantPort;
    private final SourceUserProfilePort sourceUserProfilePort;

    /**
     * Creates a new user profile with the specified details.
     * 
     * @param command Contains the profile details
     * @throws IllegalArgumentException if the command contains invalid data
     */
    @Override
    @Transactional
    public void createUserProfile(CreateUserProfileCommand command) {
        log.debug("Creating new user profile for user: {} in tenant: {}", 
                command.getUserId(), command.getTenantId());
        
        // Validate input
        validateCreateUserProfileCommand(command);
        
        try {
            // Validate user and tenant exist
            findUserPort.find(command.getUserId());
            findTenantPort.find(command.getTenantId());
            
            // Convert value objects
            UserId userId = UserId.of(command.getUserId().toString());
            TenantId tenantId = TenantId.of(command.getTenantId().toString());
            DisplayName displayName = DisplayName.of(command.getDisplayName());
            
            // Optional value objects
            AvatarUrl avatarUrl = command.getAvatarUrl() != null ? 
                    AvatarUrl.of(command.getAvatarUrl()) : null;
            Biography biography = command.getBiography() != null ? 
                    Biography.of(command.getBiography()) : null;
            JobTitle jobTitle = command.getJobTitle() != null ?
                    JobTitle.of(command.getJobTitle()) : null;
            Department department = command.getDepartment() != null ?
                    Department.of(command.getDepartment()) : null;
            Location location = command.getLocation() != null ?
                    Location.of(command.getLocation()) : null;
            
            // Social links
            LinkedInUrl linkedInUrl = command.getLinkedInUrl() != null ?
                    LinkedInUrl.of(command.getLinkedInUrl()) : null;
            TwitterUrl twitterUrl = command.getTwitterUrl() != null ?
                    TwitterUrl.of(command.getTwitterUrl()) : null;
            GitHubUrl gitHubUrl = command.getGitHubUrl() != null ?
                    GitHubUrl.of(command.getGitHubUrl()) : null;
            
            // Create user profile domain object
            UserProfile userProfile = NONE.createBasicProfile(
                    userId,
                    tenantId,
                    displayName,
                    avatarUrl,
                    biography);
            
            // Add additional information if provided
            if (jobTitle != null || department != null) {
                userProfile.changeJobInfo(jobTitle, department);
            }
            
            if (location != null) {
                userProfile.changeLocation(location);
            }
            
            if (linkedInUrl != null || twitterUrl != null || gitHubUrl != null) {
                userProfile.updateSocialLinks(linkedInUrl, twitterUrl, gitHubUrl);
            }
            
            // Persist to event store
            this.sourceUserProfilePort.source(userProfile);
            
            log.info("User profile created successfully with id: {}", userProfile.getUUID());
        } catch (Exception e) {
            log.error("Failed to create user profile for user: {} in tenant: {}", 
                    command.getUserId(), command.getTenantId(), e);
            throw new RuntimeException("User profile creation failed", e);
        }
    }
    
    /**
     * Validates the create user profile command parameters.
     * 
     * @param command Command to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateCreateUserProfileCommand(CreateUserProfileCommand command) {
        Assert.notNull(command, "Command cannot be null");
        Assert.notNull(command.getUserId(), "User ID cannot be null");
        Assert.notNull(command.getTenantId(), "Tenant ID cannot be null");
        Assert.hasText(command.getDisplayName(), "Display name cannot be empty");
    }
}
