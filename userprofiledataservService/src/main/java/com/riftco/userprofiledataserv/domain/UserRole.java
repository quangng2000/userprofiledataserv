package com.riftco.userprofiledataserv.domain;

/**
 * Represents the role of a user within a tenant.
 * Used for authorization and permission management in multi-tenant context.
 */
public enum UserRole {
    TENANT_ADMIN,     // Can manage all aspects of a tenant including users
    TENANT_MANAGER,   // Can manage most aspects of a tenant except critical settings
    TENANT_USER,      // Regular user with standard permissions
    SYSTEM_ADMIN      // Special role for system administrators (cross-tenant)
}
