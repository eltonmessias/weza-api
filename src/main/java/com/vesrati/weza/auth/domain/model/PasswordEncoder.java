package com.vesrati.weza.auth.domain.model;

public interface PasswordEncoder {
    String encode(String raw);
    boolean matches(String raw, String encoded);
}
