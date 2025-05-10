package com.riftco.userprofiledataserv.adapter.api;

import com.riftco.userprofiledataserv.adapter.api.dto.request.CreateUserRequest;
import com.riftco.userprofiledataserv.adapter.api.dto.request.ModifyUserRequest;
import com.riftco.userprofiledataserv.adapter.api.dto.response.UserResponse;
import com.riftco.userprofiledataserv.application.port.in.CreateUserUseCase;
import com.riftco.userprofiledataserv.application.port.in.ModifyUserUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

import static com.riftco.userprofiledataserv.application.port.in.CreateUserUseCase.*;
import static com.riftco.userprofiledataserv.application.port.in.ModifyUserUseCase.*;

@RestController
@RequestMapping(value = "v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final ModifyUserUseCase modifyUserUseCase;
    
    /**
     * Create a new user
     * 
     * @param request User creation request with required details
     * @return Response with created user ID
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("Creating new user with email: {}", request.getEmail());
        
        CreateUserCommand command = new CreateUserCommand(
                request.getTenantId(),
                request.getName(),
                request.getEmail(),
                request.getContactNumber()
        );
        
        createUserUseCase.createUser(command);
        
        // In a real implementation, you would fetch the created user and return its details
        // This is simplified for this example
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.builder()
                        .email(request.getEmail())
                        .name(request.getName())
                        .build());
    }
    
    /**
     * Update a user's attributes
     * 
     * @param id User ID to modify
     * @param request Contains attributes to modify
     * @return Updated user data
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody ModifyUserRequest request) {
        
        log.info("Updating user with ID: {}", id);
        
        // Check which fields are present and call appropriate use cases
        if (request.getName() != null) {
            modifyUserUseCase.changeName(new ChangeNameCommand(id, request.getName()));
        }
        
        if (request.getEmail() != null) {
            modifyUserUseCase.changeEmail(new ChangeEmailCommand(id, request.getEmail()));
        }
        
        if (request.getContactNumber() != null) {
            modifyUserUseCase.changePhoneNumber(
                    new ChangePhoneNumberCommand(id, request.getContactNumber()));
        }
        
        if (request.getState() != null) {
            switch (request.getState()) {
                case ACTIVATED:
                    modifyUserUseCase.changeState(ChangeStateCommand.activate(id));
                    break;
                case DEACTIVATED:
                    modifyUserUseCase.changeState(ChangeStateCommand.deactivate(id));
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported state: " + request.getState());
            }
        }
        
        // In a real implementation, you would fetch the updated user and return its details
        // This is simplified for this example
        return ResponseEntity.ok(UserResponse.builder().id(id).build());
    }
    
    /**
     * Activate a user
     * 
     * @param id User ID to activate
     */
    @PutMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.OK)
    public void activateUser(@PathVariable UUID id) {
        log.info("Activating user with ID: {}", id);
        modifyUserUseCase.changeState(ChangeStateCommand.activate(id));
    }
    
    /**
     * Deactivate a user
     * 
     * @param id User ID to deactivate
     */
    @PutMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.OK)
    public void deactivateUser(@PathVariable UUID id) {
        log.info("Deactivating user with ID: {}", id);
        modifyUserUseCase.changeState(ChangeStateCommand.deactivate(id));
    }
}
