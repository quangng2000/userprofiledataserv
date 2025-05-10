package com.riftco.userprofiledataserv.domain.common;

// Implementations MUST provide equals and hashCode methods for proper value object semantics
public interface ValueObject {
    // Value objects are identified by their attributes, not by identity
    @Override
    boolean equals(Object o);
    
    @Override
    int hashCode();
    
    @Override
    String toString();
}
