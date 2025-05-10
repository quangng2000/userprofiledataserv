package com.riftco.userprofiledataserv.adapter.api.dto.request;

import com.riftco.userprofiledataserv.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

/**
 * DTO for creating a new user
 */
@Getter
@Setter
public class CreateUserRequest {
    @NotNull(message = "Tenant ID is required")
    private UUID tenantId;
    
    @NotEmpty(message = "Name is required")
    private String name;
    
    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotEmpty(message = "Contact number is required")
    private String contactNumber;
    
}
