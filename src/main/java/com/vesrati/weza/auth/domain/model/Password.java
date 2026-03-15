package com.vesrati.weza.auth.domain.model;

import com.vesrati.weza.auth.domain.exception.AuthException;

public final class Password {
    private final String hashed;
    private final boolean encoded;


    private Password(String hashed, boolean encoded) {
        this.hashed = hashed;
        this.encoded = encoded;
    }

    public static Password fromEncoded(String encoded) {
        if(encoded == null || encoded.isBlank())
            throw new AuthException("Invalid encoded password");
        return new Password(encoded.trim(), true);
    }

    public String hashed() {return hashed;}
    public boolean encoded() {return encoded;}

    private static void validate(String raw){
        if(raw == null || raw.isBlank())
            throw new AuthException("Password is mandatory");
        if (raw.length() < 8)
            throw new AuthException("The password must be at least 8 characters long");
        if (raw.length() > 100)
            throw new AuthException("Password too long");
        if (!raw.matches(".*[A-Za-z].*"))
            throw new AuthException("Password must contain at least one letter");
        if (!raw.matches(".*[0-9].*"))
            throw new AuthException("Password must contain at least one number");
    }

    @Override
    public String toString() { return "[PROTECTED]"; }
}
