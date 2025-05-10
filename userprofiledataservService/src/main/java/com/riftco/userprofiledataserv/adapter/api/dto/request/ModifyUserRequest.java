package com.riftco.userprofiledataserv.adapter.api.dto.request;

import com.riftco.userprofiledataserv.domain.UserRole;
import com.riftco.userprofiledataserv.domain.UserState;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Consolidated DTO for modifying user attributes.
 * The controller will check which fields are non-null to determine what to modify.
 */
@Getter
@Setter
public class ModifyUserRequest {
    // All fields are optional - only populated fields will be updated
    private String name;
    
    @Email(message = "Invalid email format")
    private String email;
    
    private String contactNumber;
    
    private UserState state;
    
    private String password;  // For password changes
}
