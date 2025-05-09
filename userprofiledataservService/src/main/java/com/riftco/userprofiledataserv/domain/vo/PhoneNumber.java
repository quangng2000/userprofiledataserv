package com.riftco.userprofiledataserv.domain.vo;

import com.riftco.userprofiledataserv.domain.common.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
public final class PhoneNumber implements ValueObject {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9\\s\\-()]{8,20}$");
    
    private final String value;

    private PhoneNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
        String sanitized = value.replaceAll("\\s", "");
        if (!PHONE_PATTERN.matcher(sanitized).matches()) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        this.value = sanitized;
    }

    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
