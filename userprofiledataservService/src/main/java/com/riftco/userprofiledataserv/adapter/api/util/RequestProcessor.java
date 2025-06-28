package com.riftco.userprofiledataserv.adapter.api.util;

import com.riftco.userprofiledataserv.application.port.in.ModifyUserUseCase;
import com.riftco.userprofiledataserv.application.port.in.ModifyUserUseCase.ChangeNameCommand;
import com.riftco.userprofiledataserv.application.port.in.ModifyUserUseCase.ChangeEmailCommand;
import com.riftco.userprofiledataserv.application.port.in.ModifyUserUseCase.ChangePhoneNumberCommand;
import com.riftco.userprofiledataserv.application.port.in.ModifyUserUseCase.ChangeStateCommand;
import com.riftco.userprofiledataserv.application.port.in.ModifyUserProfileUseCase;
import com.riftco.userprofiledataserv.application.port.in.ModifyUserProfileUseCase.ChangeDisplayNameCommand;
import com.riftco.userprofiledataserv.application.port.in.ModifyUserProfileUseCase.ChangeAvatarCommand;
import com.riftco.userprofiledataserv.application.port.in.ModifyUserProfileUseCase.ChangeBiographyCommand;
import com.riftco.userprofiledataserv.application.port.in.ModifyUserProfileUseCase.ChangeJobInfoCommand;
import com.riftco.userprofiledataserv.application.port.in.ModifyUserProfileUseCase.ChangeLocationCommand;
import com.riftco.userprofiledataserv.application.port.in.ModifyUserProfileUseCase.ChangeSocialLinksCommand;
import com.riftco.userprofiledataserv.domain.UserState;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Utility class to process request objects using reflection for flexible field updates.
 * This approach reduces the need for explicit null checks for each field and makes
 * adding new fields more maintainable.
 */
@Slf4j
public class RequestProcessor {
    
    /**
     * Process a user update request using reflection to identify non-null fields.
     * 
     * @param request The request object containing fields to update
     * @param useCase The use case to handle the updates
     * @param userId The ID of the user to update
     */
    public static void processUserUpdateRequest(Object request, ModifyUserUseCase useCase, UUID userId) {
        log.debug("Processing user update request for user ID: {}", userId);
        
        try {
            // Find and process all getter methods that return non-null values
            Arrays.stream(request.getClass().getMethods())
                .filter(method -> method.getName().startsWith("get") && !method.getName().equals("getClass"))
                .forEach(getter -> {
                    try {
                        Object value = getter.invoke(request);
                        if (value != null) {
                            String fieldName = getter.getName().substring(3); // Remove "get"
                            processUserField(fieldName, value, useCase, userId);
                        }
                    } catch (Exception e) {
                        log.error("Error processing field with getter: {}", getter.getName(), e);
                        throw new RuntimeException("Failed to process update for field: " + getter.getName(), e);
                    }
                });
        } catch (Exception e) {
            log.error("Error processing user update request", e);
            throw new RuntimeException("Failed to process user update request", e);
        }
    }
    
    /**
     * Process a specific field from the user update request.
     * 
     * @param fieldName The name of the field (without "get" prefix)
     * @param value The value of the field
     * @param useCase The use case to handle the update
     * @param userId The ID of the user to update
     */
    private static void processUserField(String fieldName, Object value, ModifyUserUseCase useCase, UUID userId) {
        log.debug("Processing field: {} with value: {}", fieldName, value);
        
        switch (fieldName) {
            case "Name":
                useCase.changeName(new ChangeNameCommand(userId, (String)value));
                break;
            case "Email":
                useCase.changeEmail(new ChangeEmailCommand(userId, (String)value));
                break;
            case "ContactNumber":
                useCase.changePhoneNumber(new ChangePhoneNumberCommand(userId, (String)value));
                break;
            case "State":
                handleStateChange((UserState)value, useCase, userId);
                break;
            case "Password":
                // Handle password update if supported
                // useCase.changePassword(new ChangePasswordCommand(userId, (String)value));
                log.warn("Password change not implemented in processor yet");
                break;
            default:
                log.warn("Unhandled field in update request: {}", fieldName);
        }
    }
    
    /**
     * Handle the state change for a user.
     * 
     * @param state The new state
     * @param useCase The use case to handle the state change
     * @param userId The ID of the user to update
     */
    private static void handleStateChange(UserState state, ModifyUserUseCase useCase, UUID userId) {
        switch (state) {
            case ACTIVATED:
                useCase.changeState(ChangeStateCommand.activate(userId));
                break;
            case DEACTIVATED:
                useCase.changeState(ChangeStateCommand.deactivate(userId));
                break;
            default:
                throw new IllegalArgumentException("Unsupported state: " + state);
        }
    }
    
    /**
     * Process a user profile update request using reflection to identify non-null fields.
     * Handles special cases for grouped fields like job info and social media links.
     * 
     * @param request The request object containing fields to update
     * @param useCase The use case to handle the updates
     * @param profileId The ID of the profile to update
     */
    public static void processUserProfileUpdateRequest(Object request, ModifyUserProfileUseCase useCase, UUID profileId) {
        log.debug("Processing user profile update request for profile ID: {}", profileId);
        
        try {
            // Track fields that require special grouping
            Map<String, Object> jobInfoFields = new HashMap<>();
            Map<String, Object> socialLinkFields = new HashMap<>();
            
            // First pass: collect all non-null values
            Arrays.stream(request.getClass().getMethods())
                .filter(method -> method.getName().startsWith("get") && !method.getName().equals("getClass"))
                .forEach(getter -> {
                    try {
                        Object value = getter.invoke(request);
                        if (value != null) {
                            String fieldName = getter.getName().substring(3); // Remove "get"
                            
                            // Collect fields that need to be processed together
                            if (fieldName.equals("JobTitle") || fieldName.equals("Department")) {
                                jobInfoFields.put(fieldName, value);
                            } else if (fieldName.equals("LinkedInUrl") || fieldName.equals("TwitterUrl") || fieldName.equals("GitHubUrl")) {
                                socialLinkFields.put(fieldName, value);
                            } else {
                                // Process regular fields immediately
                                processUserProfileField(fieldName, value, useCase, profileId);
                            }
                        }
                    } catch (Exception e) {
                        log.error("Error processing field with getter: {}", getter.getName(), e);
                        throw new RuntimeException("Failed to process update for field: " + getter.getName(), e);
                    }
                });
            
            // Process grouped fields
            processJobInfoFields(jobInfoFields, useCase, profileId);
            processSocialLinkFields(socialLinkFields, useCase, profileId);
            
        } catch (Exception e) {
            log.error("Error processing user profile update request", e);
            throw new RuntimeException("Failed to process user profile update request", e);
        }
    }
    
    /**
     * Process a regular field from the user profile update request.
     * 
     * @param fieldName The name of the field (without "get" prefix)
     * @param value The value of the field
     * @param useCase The use case to handle the update
     * @param profileId The ID of the profile to update
     */
    private static void processUserProfileField(String fieldName, Object value, ModifyUserProfileUseCase useCase, UUID profileId) {
        log.debug("Processing profile field: {} with value: {}", fieldName, value);
        
        switch (fieldName) {
            case "DisplayName":
                useCase.changeDisplayName(new ChangeDisplayNameCommand(profileId, (String)value));
                break;
            case "AvatarUrl":
                useCase.changeAvatar(new ChangeAvatarCommand(profileId, (String)value));
                break;
            case "Biography":
                useCase.changeBiography(new ChangeBiographyCommand(profileId, (String)value));
                break;
            case "Location":
                useCase.changeLocation(new ChangeLocationCommand(profileId, (String)value));
                break;
            default:
                log.warn("Unhandled field in profile update request: {}", fieldName);
        }
    }
    
    /**
     * Process job info fields (job title and department) together.
     * 
     * @param jobInfoFields Map of job info field names to values
     * @param useCase The use case to handle the update
     * @param profileId The ID of the profile to update
     */
    private static void processJobInfoFields(Map<String, Object> jobInfoFields, ModifyUserProfileUseCase useCase, UUID profileId) {
        if (!jobInfoFields.isEmpty()) {
            String jobTitle = (String) jobInfoFields.getOrDefault("JobTitle", null);
            String department = (String) jobInfoFields.getOrDefault("Department", null);
            
            log.debug("Processing job info update - title: {}, department: {}", jobTitle, department);
            useCase.changeJobInfo(new ChangeJobInfoCommand(profileId, jobTitle, department));
        }
    }
    
    /**
     * Process social media link fields (LinkedIn, Twitter, GitHub) together.
     * 
     * @param socialLinkFields Map of social link field names to values
     * @param useCase The use case to handle the update
     * @param profileId The ID of the profile to update
     */
    private static void processSocialLinkFields(Map<String, Object> socialLinkFields, ModifyUserProfileUseCase useCase, UUID profileId) {
        if (!socialLinkFields.isEmpty()) {
            String linkedInUrl = (String) socialLinkFields.getOrDefault("LinkedInUrl", null);
            String twitterUrl = (String) socialLinkFields.getOrDefault("TwitterUrl", null);
            String gitHubUrl = (String) socialLinkFields.getOrDefault("GitHubUrl", null);
            
            log.debug("Processing social links update - LinkedIn: {}, Twitter: {}, GitHub: {}", 
                    linkedInUrl != null ? "present" : "absent",
                    twitterUrl != null ? "present" : "absent",
                    gitHubUrl != null ? "present" : "absent");
            useCase.changeSocialLinks(new ChangeSocialLinksCommand(profileId, linkedInUrl, twitterUrl, gitHubUrl));
        }
    }
}
