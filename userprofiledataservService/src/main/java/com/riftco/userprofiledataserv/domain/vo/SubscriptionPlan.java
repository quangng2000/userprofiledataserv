package com.riftco.userprofiledataserv.domain.vo;

import com.riftco.userprofiledataserv.domain.common.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents a subscription plan for a tenant.
 * In SaaS multi-tenancy, different tenants may have different subscription plans
 * that determine features, limitations, and pricing.
 */
@Getter
@EqualsAndHashCode
public final class SubscriptionPlan implements ValueObject {
    public enum PlanType {
        FREE,
        BASIC,
        PROFESSIONAL,
        ENTERPRISE
    }

    private final PlanType planType;
    private final int maxUsers;
    private final boolean hasAdvancedFeatures;
    private final boolean hasPrioritySuppport;

    private SubscriptionPlan(PlanType planType, int maxUsers, boolean hasAdvancedFeatures, boolean hasPrioritySuppport) {
        this.planType = planType;
        this.maxUsers = maxUsers;
        this.hasAdvancedFeatures = hasAdvancedFeatures;
        this.hasPrioritySuppport = hasPrioritySuppport;
    }

    public static SubscriptionPlan freePlan() {
        return new SubscriptionPlan(PlanType.FREE, 3, false, false);
    }

    public static SubscriptionPlan basicPlan() {
        return new SubscriptionPlan(PlanType.BASIC, 10, false, false);
    }

    public static SubscriptionPlan professionalPlan() {
        return new SubscriptionPlan(PlanType.PROFESSIONAL, 50, true, false);
    }

    public static SubscriptionPlan enterprisePlan() {
        return new SubscriptionPlan(PlanType.ENTERPRISE, 1000, true, true);
    }

    public static SubscriptionPlan custom(PlanType planType, int maxUsers, boolean hasAdvancedFeatures, boolean hasPrioritySuppport) {
        return new SubscriptionPlan(planType, maxUsers, hasAdvancedFeatures, hasPrioritySuppport);
    }

    /**
     * Factory method for creating a SubscriptionPlan from a string representation.
     * This method is required for proper deserialization by the ValueObjectDeserializer.
     *
     * @param value The string representation of the plan type (case-insensitive)
     * @return The corresponding SubscriptionPlan
     * @throws IllegalArgumentException if the value cannot be converted to a valid plan
     */
    public static SubscriptionPlan of(String value) {
        try {
            PlanType planType = PlanType.valueOf(value.toUpperCase());
            switch (planType) {
                case FREE:
                    return freePlan();
                case BASIC:
                    return basicPlan();
                case PROFESSIONAL:
                    return professionalPlan();
                case ENTERPRISE:
                    return enterprisePlan();
                default:
                    throw new IllegalArgumentException("Unknown plan type: " + value);
            }
        } catch (IllegalArgumentException e) {
            // Fallback for older serialized data that might use different formats
            if (value.equalsIgnoreCase("FREE")) return freePlan();
            if (value.equalsIgnoreCase("BASIC")) return basicPlan();
            if (value.equalsIgnoreCase("PROFESSIONAL")) return professionalPlan();
            if (value.equalsIgnoreCase("ENTERPRISE")) return enterprisePlan();
            
            throw new IllegalArgumentException("Cannot convert value to SubscriptionPlan: " + value);
        }
    }

    @Override
    public String toString() {
        return planType.name();
    }
}
