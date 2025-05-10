package com.riftco.userprofiledataserv.adapter.api;

import com.riftco.userprofiledataserv.adapter.api.dto.request.CreateUserProfileRequest;
import com.riftco.userprofiledataserv.adapter.api.dto.request.ModifyUserProfileRequest;
import com.riftco.userprofiledataserv.adapter.api.dto.response.UserProfileResponse;
import com.riftco.userprofiledataserv.application.port.in.CreateUserProfileUseCase;
import com.riftco.userprofiledataserv.application.port.in.ModifyUserProfileUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

import static com.riftco.userprofiledataserv.application.port.in.CreateUserProfileUseCase.*;
import static com.riftco.userprofiledataserv.application.port.in.ModifyUserProfileUseCase.*;

@RestController
@RequestMapping(value = "v1/profiles")
@RequiredArgsConstructor
@Slf4j
public class UserProfileController {

    private final CreateUserProfileUseCase createUserProfileUseCase;
    private final ModifyUserProfileUseCase modifyUserProfileUseCase;
    
    /**
     * Create a new user profile
     * 
     * @param request Profile creation request with required details
     * @return Response with created profile details
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserProfileResponse> createUserProfile(@Valid @RequestBody CreateUserProfileRequest request) {
        log.info("Creating new profile for user: {} in tenant: {}", 
                request.getUserId(), request.getTenantId());
        
        CreateUserProfileCommand command = new CreateUserProfileCommand(
                request.getUserId(),
                request.getTenantId(),
                request.getDisplayName(),
                request.getAvatarUrl(),
                request.getBiography(),
                request.getJobTitle(),
                request.getDepartment(),
                request.getLocation(),
                request.getLinkedInUrl(),
                request.getTwitterUrl(),
                request.getGitHubUrl()
        );
        
        createUserProfileUseCase.createUserProfile(command);
        
        // In a real implementation, you would fetch the created profile and return its details
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserProfileResponse.builder()
                        .userId(request.getUserId())
                        .tenantId(request.getTenantId())
                        .displayName(request.getDisplayName())
                        .build());
    }
    
    /**
     * Update a user profile's attributes
     * 
     * @param id Profile ID to modify
     * @param request Contains attributes to modify
     * @return Updated profile data
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResponse> updateUserProfile(
            @PathVariable UUID id,
            @Valid @RequestBody ModifyUserProfileRequest request) {
        
        log.info("Updating profile with ID: {}", id);
        
        // Check which fields are present and call appropriate use cases
        if (request.getDisplayName() != null) {
            modifyUserProfileUseCase.changeDisplayName(
                    new ChangeDisplayNameCommand(id, request.getDisplayName()));
        }
        
        if (request.getAvatarUrl() != null) {
            modifyUserProfileUseCase.changeAvatar(
                    new ChangeAvatarCommand(id, request.getAvatarUrl()));
        }
        
        if (request.getBiography() != null) {
            modifyUserProfileUseCase.changeBiography(
                    new ChangeBiographyCommand(id, request.getBiography()));
        }
        
        // Handle job title and department together if either is provided
        if (request.getJobTitle() != null || request.getDepartment() != null) {
            modifyUserProfileUseCase.changeJobInfo(
                    new ChangeJobInfoCommand(id, request.getJobTitle(), request.getDepartment()));
        }
        
        if (request.getLocation() != null) {
            modifyUserProfileUseCase.changeLocation(
                    new ChangeLocationCommand(id, request.getLocation()));
        }
        
        // Handle all social media URLs together if any is provided
        if (request.getLinkedInUrl() != null || request.getTwitterUrl() != null || request.getGitHubUrl() != null) {
            modifyUserProfileUseCase.changeSocialLinks(
                    new ChangeSocialLinksCommand(id, request.getLinkedInUrl(), request.getTwitterUrl(), request.getGitHubUrl()));
        }
        
        // In a real implementation, you would fetch the updated profile and return its details
        return ResponseEntity.ok(UserProfileResponse.builder().id(id).build());
    }
    
    /**
     * Get user profile by ID
     * 
     * @param id Profile ID
     * @return Profile data
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable UUID id) {
        log.info("Fetching profile with ID: {}", id);
        
        // In a real implementation, you would fetch the profile from your repository
        // This is simplified for this example
        return ResponseEntity.ok(UserProfileResponse.builder().id(id).build());
    }
    
    /**
     * Get user profiles by tenant ID
     * 
     * @param tenantId Tenant ID
     * @return List of profiles in the tenant
     */
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<UserProfileResponse[]> getProfilesByTenant(@PathVariable UUID tenantId) {
        log.info("Fetching profiles for tenant: {}", tenantId);
        
        // In a real implementation, you would fetch profiles from your repository
        // This is simplified for this example
        return ResponseEntity.ok(new UserProfileResponse[]{});
    }
    
    /**
     * Get user profile by user ID and tenant ID
     * 
     * @param userId User ID
     * @param tenantId Tenant ID
     * @return Profile data
     */
    @GetMapping("/user/{userId}/tenant/{tenantId}")
    public ResponseEntity<UserProfileResponse> getProfileByUserAndTenant(
            @PathVariable UUID userId,
            @PathVariable UUID tenantId) {
        
        log.info("Fetching profile for user: {} in tenant: {}", userId, tenantId);
        
        // In a real implementation, you would fetch the profile from your repository
        // This is simplified for this example
        return ResponseEntity.ok(UserProfileResponse.builder()
                .userId(userId)
                .tenantId(tenantId)
                .build());
    }
}
