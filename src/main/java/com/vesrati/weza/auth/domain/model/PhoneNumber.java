package com.vesrati.weza.auth.domain.model;

import com.vesrati.weza.auth.domain.exception.AuthException;

public record PhoneNumber(String value) {
    private static final String MZ_PATTERN = "^258(8[2-7]\\\\d{7})$";

    public PhoneNumber{
        if (value == null || value.isBlank())
            throw new AuthException("Número de telefone é obrigatório");

        value = normalize(value);

        if (!value.matches(MZ_PATTERN))
            throw new AuthException(
                    "Número inválido. Formato esperado: 841234567 ou 258841234567");
    }

    public String operator() {
        String prefix = value.substring(3, 5);
        return switch (prefix) {
            case "84", "85" -> "VODACOM";
            case "86", "87" -> "MOVITEL";
            case "82", "83" -> "TMCEL";
            default         -> "DESCONHECIDO";
        };
    }

    public boolean isMpesaCompatible()  { return operator().equals("VODACOM"); }
    public boolean isEMolaCompatible()  { return operator().equals("MOVITEL"); }

    private static String normalize(String raw) {
        String digits = raw.replaceAll("[^0-9]", "");
        if (digits.length() == 9 && digits.startsWith("8"))
            return "258" + digits;
        return digits;
    }

    @Override public String toString() { return value; }
}
