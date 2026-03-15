package com.vesrati.weza.auth.domain.model;

import com.vesrati.weza.auth.domain.exception.AuthException;
import org.springframework.cglib.core.Local;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class User {

    private final UUID id;
    private String name;
    private Email email;
    private Password password;
    private PhoneNumber phoneNumber;
    private Role role;
    private boolean active;
    private boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(UUID id, String name, Email email, PhoneNumber phoneNumber, Role role, Password password,
                boolean active,
                boolean emailVerified, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.password = password;
        this.active = active;
        this.emailVerified = emailVerified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(String name, String email, String phoneNumber, String role, String passwordHash){
        return new User(
                UUID.randomUUID(),
                validateName(name),
                new Email(email),
                new PhoneNumber(phoneNumber),
                Role.valueOf(role),
                Password.fromRaw(passwordHash),
                true,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static User reconstitute(UUID id, String name, Email email, PhoneNumber phoneNumber, Role role,
                                    Password passwordHash, boolean active, boolean emailVerified, LocalDateTime createdAt,
                                    LocalDateTime updatedAt){
        return new User(id, name, email, phoneNumber, role, passwordHash, active, emailVerified, createdAt, updatedAt);
    }

    public void changePassword(String currentPassword, String newPassword, PasswordEncoder encoder){
        if (!encoder.matches(currentPassword, password.hashed()))
            throw new AuthException("Current password is incorrect");
        this.password = Password.fromEncoded(encoder.encode(newPassword));
        this.updatedAt = LocalDateTime.now();
    }

    public void verifyEmail(){
        this.emailVerified = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate(){
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean matchesPassword(String rawPassword, PasswordEncoder encoder){
        return encoder.matches(rawPassword, password.hashed());
    }

    public void assertActivate(){
        if (!active)
            throw new AuthException("Deactivated Account. Please contact the support team.");
    }

    private static String validateName(String name) {
        if (name == null || name.isBlank())
            throw new AuthException("The name is mandatory");
        if (name.trim().length() < 2)
            throw new AuthException("The name must be at least 2 characters");
        if (name.trim().length() > 100)
            throw new AuthException("The name cannot exceed 100 characters");
        return name.trim();
    }



    public void changeName(String newName){
        if (newName == null || newName.isBlank()) throw new IllegalArgumentException("Name cannot be blank");
        this.name = validateName(newName);
        this.updatedAt = LocalDateTime.now();
    }

    public void changePhoneNumber(String newPhoneNumber){
        if (newPhoneNumber == null || newPhoneNumber.isBlank()) throw new IllegalArgumentException("Phone number cannot be blank");
        this.phoneNumber = new PhoneNumber(newPhoneNumber);
        this.updatedAt = LocalDateTime.now();
    }
    public void changeRole(Role newRole){
        this.role = newRole;
        this.updatedAt = LocalDateTime.now();
    }


    public UUID getId() { return id;}
    public String getName() {return name;}
    public Email getEmail() {return email;}
    public PhoneNumber getPhoneNumber() {return phoneNumber;}
    public Role getRole() {return role;}
    public Password getPasswordHash() { return password;}
    public boolean isActive() {return active;}
    public boolean isEmailVerified() {return emailVerified;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public LocalDateTime getUpdatedAt() {return updatedAt;}

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
