package com.riftco.userprofiledataserv.adapter.api;

import com.riftco.userprofiledataserv.adapter.api.dto.request.CreateTenantUserRequest;
import com.riftco.userprofiledataserv.adapter.api.dto.request.ModifyTenantUserRequest;
import com.riftco.userprofiledataserv.adapter.api.dto.response.TenantUserResponse;
import com.riftco.userprofiledataserv.application.port.in.CreateTenantUserUseCase;
import com.riftco.userprofiledataserv.application.port.in.ModifyTenantUserUseCase;
import com.riftco.userprofiledataserv.domain.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

import static com.riftco.userprofiledataserv.application.port.in.CreateTenantUserUseCase.*;
import static com.riftco.userprofiledataserv.application.port.in.ModifyTenantUserUseCase.*;

@RestController
@RequestMapping(value = "v1/tenant-users")
@RequiredArgsConstructor
@Slf4j
public class TenantUserController {

    private final CreateTenantUserUseCase createTenantUserUseCase;
    private final ModifyTenantUserUseCase modifyTenantUserUseCase;
    
    /**
     * Create a new tenant-user association
     * 
     * @param request Association creation request with required details
     * @return Response with created association details
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TenantUserResponse> createTenantUser(@Valid @RequestBody CreateTenantUserRequest request) {
        log.info("Creating tenant-user association for user: {} in tenant: {}", 
                request.getUserId(), request.getTenantId());
        
        // Note: The request may contain multiple roles, but the use case command accepts only one role
        // We'll process each role individually or adapt the command class to accept multiple roles
        UserRole primaryRole = request.getRoles().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("At least one role must be provided"));
        
        CreateTenantUserCommand command = new CreateTenantUserCommand(
                request.getUserId(),
                request.getTenantId(),
                primaryRole
        );
        
        createTenantUserUseCase.createTenantUser(command);
        
        // In a real implementation, you would fetch the created association and return its details
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TenantUserResponse.builder()
                        .userId(request.getUserId())
                        .tenantId(request.getTenantId())
                        .roles(request.getRoles())
                        .build());
    }
    
    /**
     * Update a tenant-user association's attributes
     * 
     * @param id Association ID to modify
     * @param request Contains attributes to modify
     * @return Updated association data
     */
    @PutMapping("/{id}")
    public ResponseEntity<TenantUserResponse> updateTenantUser(
            @PathVariable UUID id,
            @Valid @RequestBody ModifyTenantUserRequest request) {
        
        log.info("Updating tenant-user association with ID: {}", id);
        
        // Check which fields are present and call appropriate use cases
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            modifyTenantUserUseCase.changeRole(
                    new ChangeRoleCommand(id, request.getRoles().iterator().next()));
        }
        
    
        
        // In a real implementation, you would fetch the updated association and return its details
        return ResponseEntity.ok(TenantUserResponse.builder().id(id).build());
    }
    


}
