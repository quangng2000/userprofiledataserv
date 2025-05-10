package com.riftco.userprofiledataserv.domain;

/**
 * Represents the type of tenant in the system.
 * Each tenant type has specific validation rules, fields, and behaviors.
 */
public enum TenantType {
    PERSON,       // Individual tenant with single user (1:1 user-to-tenant relationship)
    ORGANIZATION  // Multi-user tenant (companies, schools, non-profits, etc. with 1:many relationship)
}
