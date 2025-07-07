package com.errday.splearn.domain;

import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record Email(String address) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-z0-9_+&*-]+(?:\\.[a-zA-z0-9_+&*-]+)*@(?:[a-zA-z0-9]+\\.)+[a-zA-z+]{2,7}$");

    public Email {
        if (!EMAIL_PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException("Invalid email: " + address);
        }
    }
}
