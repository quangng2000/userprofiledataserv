package com.riftco.userprofiledataserv.adapter.api.dto.response;

import com.riftco.userprofiledataserv.domain.TenantType;
import com.riftco.userprofiledataserv.domain.vo.TenantStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Response DTO for tenant data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantResponse {
    private UUID id;
    private String name;
    private String description;
    private TenantType type;
    private TenantStatus status;
    private String subscriptionPlanType;
    private Instant subscriptionStartDate;
    private Instant subscriptionEndDate;
    private Instant createdAt;
    private Instant updatedAt;
}
