package com.riftco.userprofiledataserv.domain.common;

import com.riftco.userprofiledataserv.domain.vo.AuditInfo;
import com.riftco.userprofiledataserv.domain.vo.UserId;
import lombok.Getter;

/**
 * Base class for all entities that require auditing.
 * Provides standard auditing capabilities for tracking creation, modification, etc.
 */
@Getter
public abstract class AuditableEntity extends Entity {
    
    protected AuditInfo auditInfo;
    
    protected AuditableEntity() {
        super();
    }
    
    protected void initializeAudit(UserId creator) {
        this.auditInfo = AuditInfo.createNew(creator);
    }
    
    protected void updateAudit(UserId modifier) {
        if (this.auditInfo == null) {
            this.auditInfo = AuditInfo.createNew(modifier);
        } else {
            this.auditInfo = this.auditInfo.withModifiedBy(modifier);
        }
    }
}
