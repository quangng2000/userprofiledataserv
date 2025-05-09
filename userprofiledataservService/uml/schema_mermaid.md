# API Schema Diagram (Mermaid)

```mermaid
classDiagram
    class Tenant {
        +UUID id
        +string name
        +enum status
        +string domain
        +enum tier
        +object settings
        +datetime created_at
        +datetime updated_at
        +Links _links
    }

    class User {
        +UUID id
        +string email
        +string username
        +UUID tenant_id
        +enum status
        +boolean email_verified
        +string phone_number
        +boolean phone_verified
        +boolean mfa_enabled
        +datetime last_login_at
        +datetime created_at
        +datetime updated_at
        +Links _links
    }

    class Profile {
        +UUID user_id
        +string first_name
        +string last_name
        +string display_name
        +URI profile_picture
        +string bio
        +object location
        +object social_links
        +object preferences
        +object metadata
        +datetime created_at
        +datetime updated_at
        +Links _links
    }

    class Role {
        +UUID id
        +string name
        +string description
        +UUID tenant_id
        +boolean is_system
        +UUID[] permissions
        +datetime created_at
        +datetime updated_at
        +Links _links
    }

    class Permission {
        +UUID id
        +string name
        +string description
        +string resource
        +enum action
        +string scope
        +boolean is_system
        +datetime created_at
        +datetime updated_at
        +Links _links
    }

    class Link {
        +string href
        +string method
        +string type
        +string title
    }

    class Links {
        +Link self
        +Link [relation_name]
    }

    Tenant "1" --> "0..*" User: contains
    Tenant "1" --> "0..*" Role: defines
    User "1" --> "1" Profile: has
    User "0..*" --> "0..*" Role: assigned to
    Role "0..*" --> "0..*" Permission: grants
    
    Tenant ..> Links: uses
    User ..> Links: uses
    Profile ..> Links: uses
    Role ..> Links: uses
    Permission ..> Links: uses
    Links "1" *-- "1..*" Link: contains
```

## Schema Relationship Details

### Multi-Tenant Architecture
- The system follows a multi-tenant design where each tenant represents an organization
- Tenants act as containers for users, roles, and associated permissions
- Each user belongs to exactly one tenant, but a system user could potentially manage multiple tenants

### Identity and Profile Separation
- User objects contain authentication and identity information
- Profile objects store personal information and preferences
- This separation allows for flexible profile data without affecting core identity

### RBAC (Role-Based Access Control)
- Users are assigned to roles
- Roles contain collections of permissions
- Permissions define granular access rights to specific resources
- This allows for fine-grained access control and privilege management

### HATEOAS Implementation
- All resources include hypermedia links through the `_links` property
- Links provide navigational information for API clients
- This enables API discoverability and decouples clients from specific endpoints

### Data Integrity Features
- All entities use consistent UUID identifiers
- Creation and modification timestamps track resource history
- Status fields enable state management (active, inactive, etc.)
- System flags identify built-in entities that should not be modified
