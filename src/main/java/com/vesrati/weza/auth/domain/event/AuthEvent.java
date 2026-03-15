package com.vesrati.weza.auth.domain.event;

import com.vesrati.weza.auth.domain.model.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public sealed interface AuthEvent permits
    AuthEvent.UserRegisteredEvent, AuthEvent.UserLoggedInEvent, AuthEvent.EmailVerifiedEvent,
        AuthEvent.PasswordChangedEvent, AuthEvent.AccountDeactivatedEvent {

    LocalDateTime occurredAt();

    record UserRegisteredEvent(UUID userId, String email, String name, Role role, LocalDateTime occurredAt) implements AuthEvent{
        public UserRegisteredEvent(UUID userId, String email, String name, Role role) {
            this(userId, email, name, role, LocalDateTime.now());
        }
    }

    record UserLoggedInEvent(UUID userId, String email, LocalDateTime occurredAt) implements AuthEvent{
        public UserLoggedInEvent(UUID userId, String email) {
            this(userId, email, LocalDateTime.now());
        }
    }

    record EmailVerifiedEvent(UUID userId, String email, LocalDateTime occurredAt) implements AuthEvent{
        public EmailVerifiedEvent(UUID userId, String email) {
            this(userId, email, LocalDateTime.now());
        }
    }

    record PasswordChangedEvent(UUID userId, LocalDateTime occurredAt) implements AuthEvent{
        public PasswordChangedEvent(UUID userId) {
            this(userId, LocalDateTime.now());
        }
    }

    record AccountDeactivatedEvent(UUID userId, LocalDateTime occurredAt) implements AuthEvent{
        public AccountDeactivatedEvent(UUID userId){
            this(userId, LocalDateTime.now());
        }
    }
}
