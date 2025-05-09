# Multi-Tenant User Identity and Profile API Schema

## UML Class Diagram

```plantuml
@startuml "Multi-Tenant User Identity and Profile API Schema"

' Style and theme settings
!theme plain
skinparam classAttributeIconSize 0
skinparam classFontStyle bold
skinparam nodesep 100
skinparam ranksep 80

' Class definitions
class Tenant {
    +id: UUID
    +name: string
    +status: enum [active, inactive, suspended, pending]
    +domain: string
    +tier: enum [free, basic, professional, enterprise]
    +settings: object
    +created_at: datetime
    +updated_at: datetime
    +_links: Links
}

class User {
    +id: UUID
    +email: string
    +username: string
    +tenant_id: UUID
    +status: enum [active, inactive, pending, suspended, locked]
    +email_verified: boolean
    +phone_number: string
    +phone_verified: boolean
    +mfa_enabled: boolean
    +last_login_at: datetime
    +created_at: datetime
    +updated_at: datetime
    +_links: Links
}

class Profile {
    +user_id: UUID
    +first_name: string
    +last_name: string
    +display_name: string
    +profile_picture: URI
    +bio: string
    +location: object
    +social_links: object
    +preferences: object
    +metadata: object
    +created_at: datetime
    +updated_at: datetime
    +_links: Links
}

class Role {
    +id: UUID
    +name: string
    +description: string
    +tenant_id: UUID
    +is_system: boolean
    +permissions: array[UUID]
    +created_at: datetime
    +updated_at: datetime
    +_links: Links
}

class Permission {
    +id: UUID
    +name: string
    +description: string
    +resource: string
    +action: enum [create, read, update, delete, manage, execute]
    +scope: string
    +is_system: boolean
    +created_at: datetime
    +updated_at: datetime
    +_links: Links
}

class Link {
    +href: string
    +method: string
    +type: string
    +title: string
}

class Links {
    +self: Link
    +[relation_name]: Link
}

class Error {
    +code: string
    +message: string
    +details: array
    +target: string
    +documentation_url: string
}

' Relationships
Tenant "1" -- "0..*" User : contains >
Tenant "1" -- "0..*" Role : defines >
User "1" -- "1" Profile : has >
User "0..*" -- "0..*" Role : assigned to >
Role "0..*" -- "0..*" Permission : grants >

' HATEOAS relationships (dashed to indicate they're not database relationships)
Tenant ..> Links : uses >
User ..> Links : uses >
Profile ..> Links : uses >
Role ..> Links : uses >
Permission ..> Links : uses >
Links "1" *-- "1..*" Link : contains >

@enduml
```

## Key Entity Relationships

### Core Entities
1. **Tenant**: Represents an organization or customer that uses the system
2. **User**: Represents a user within a tenant with authentication properties
3. **Profile**: Contains extended user information and preferences
4. **Role**: Defines a set of permissions for role-based access control
5. **Permission**: Defines granular access rights to system resources

### Relationship Highlights
- **Multi-Tenancy**: Each tenant contains multiple users and roles
- **User-Profile**: Each user has exactly one profile with extended information
- **RBAC**: Users have roles which grant specific permissions
- **HATEOAS**: All entities include hypermedia links for API navigation

## Nested Resource Hierarchy
```
Tenant
├── Users
│   ├── Profile
│   └── Roles
└── Roles
    └── Permissions
```

## Schema Features
- **UUID Identifiers**: All entities use UUID format for IDs
- **Timestamp Tracking**: Created/updated timestamps on all entities
- **Stateful Resources**: Status fields track lifecycle states
- **Soft Delete**: Entities support soft delete operations
- **Extensible**: `metadata` fields allow for custom attributes
- **HATEOAS Controls**: Every resource includes `_links` for discoverability

This diagram illustrates the clean separation of concerns and logical relationships between entities in your API schema, following industry best practices for multi-tenant user management systems.
