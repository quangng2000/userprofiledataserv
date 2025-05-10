package com.riftco.userprofiledataserv.adapter.api;

import com.riftco.userprofiledataserv.adapter.api.dto.request.CreateTenantRequest;
import com.riftco.userprofiledataserv.adapter.api.dto.request.ModifyTenantRequest;
import com.riftco.userprofiledataserv.adapter.api.dto.response.TenantResponse;
import com.riftco.userprofiledataserv.application.port.in.CreateTenantUseCase;
import com.riftco.userprofiledataserv.application.port.in.ModifyTenantUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

import static com.riftco.userprofiledataserv.application.port.in.CreateTenantUseCase.*;
import static com.riftco.userprofiledataserv.application.port.in.ModifyTenantUseCase.*;

@RestController
@RequestMapping(value = "v1/tenants")
@RequiredArgsConstructor
@Slf4j
public class TenantController {

    private final CreateTenantUseCase createTenantUseCase;
    private final ModifyTenantUseCase modifyTenantUseCase;
    
    /**
     * Create a new tenant
     * 
     * @param request Tenant creation request with required details
     * @return Response with created tenant details
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TenantResponse> createTenant(@Valid @RequestBody CreateTenantRequest request) {
        log.info("Creating new tenant with name: {}", request.getName());
        
        CreateTenantCommand command = new CreateTenantCommand(
                request.getName(),
                request.getDescription(),
                request.getTenantType(),
                request.getStatus(),
                request.getSubscriptionPlanType()
        );
        
        createTenantUseCase.createTenant(command);
        
        // In a real implementation, you would fetch the created tenant and return its details
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TenantResponse.builder()
                        .name(request.getName())
                        .type(request.getTenantType())
                        .build());
    }
    
    /**
     * Update a tenant's attributes
     * 
     * @param id Tenant ID to modify
     * @param request Contains attributes to modify
     * @return Updated tenant data
     */
    @PutMapping("/{id}")
    public ResponseEntity<TenantResponse> updateTenant(
            @PathVariable UUID id,
            @Valid @RequestBody ModifyTenantRequest request) {
        
        log.info("Updating tenant with ID: {}", id);
        
        // Check which fields are present and call appropriate use cases
        if (request.getName() != null) {
            modifyTenantUseCase.changeName(new ChangeTenantNameCommand(id, request.getName()));
        }
        
        if (request.getDescription() != null) {
            modifyTenantUseCase.changeDescription(new ChangeTenantDescriptionCommand(id, request.getDescription()));
        }
        
        if (request.getStatus() != null) {
            modifyTenantUseCase.changeStatus(new ChangeTenantStatusCommand(id, request.getStatus()));
        }
        
        // In a real implementation, you would fetch the updated tenant and return its details
        return ResponseEntity.ok(TenantResponse.builder().id(id).build());
    }
}
