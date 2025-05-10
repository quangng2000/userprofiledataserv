package com.riftco.userprofiledataserv.application.service;

import com.riftco.userprofiledataserv.application.port.in.CreateUserUseCase;
import com.riftco.userprofiledataserv.application.port.out.SourceUserPort;
import com.riftco.userprofiledataserv.domain.User;
import com.riftco.userprofiledataserv.domain.vo.Email;
import com.riftco.userprofiledataserv.domain.vo.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static com.riftco.userprofiledataserv.domain.User.NONE;

/**
 * Service implementation for creating new users in the system.
 * Provides transactional handling and input validation.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CreateUserService implements CreateUserUseCase {

    private final SourceUserPort sourceUserPort;

    /**
     * Creates a new user with the specified details.
     * 
     * @param command Contains the user details for creation
     * @throws IllegalArgumentException if the command contains invalid data
     */
    @Override
    @Transactional
    public void createUser(CreateUserCommand command) {
        log.debug("Creating new user with email: {}", command.getEmail());
        
        // Validate input
        validateCreateUserCommand(command);
        
        try {
            // Create user domain object
            User user = NONE.create(
                    command.getTenantId(), // Use the TenantId from the command
                    command.getName(),
                    Email.of(command.getEmail()), // Convert String to Email using factory method
                    PhoneNumber.of(command.getContactNumber())); // Convert String to PhoneNumber using factory method
            
            // Persist to event store
            this.sourceUserPort.source(user);
            
            log.info("User created successfully with id: {}", user.getUUID());
        } catch (Exception e) {
            log.error("Failed to create user with email: {}", command.getEmail(), e);
            throw new RuntimeException("User creation failed", e);
        }
    }
    
    /**
     * Validates the create user command parameters.
     * 
     * @param command Command to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateCreateUserCommand(CreateUserCommand command) {
        Assert.notNull(command, "Command cannot be null");
        Assert.hasText(command.getName(), "User name cannot be empty");
        Assert.hasText(command.getEmail(), "Email cannot be empty");
        Assert.hasText(command.getContactNumber(), "Contact number cannot be empty");
        
        // Additional validations could be added here, like email format validation
    }
}
