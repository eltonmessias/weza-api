package com.vesrati.weza.auth.domain.model;

import com.vesrati.weza.auth.domain.exception.AuthException;

public record Email(String value) {


    public Email {
        if (value == null || value.isBlank())
            throw new AuthException("Email is mandatory");

        value = value.toLowerCase().trim();

        if(!value.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-z]{2,}$")){
            throw new AuthException("Email is invalid: " + value);
        }


    }
    @Override
    public String toString() { return value; }

}
