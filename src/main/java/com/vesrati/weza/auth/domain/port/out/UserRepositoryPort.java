package com.vesrati.weza.auth.domain.port.out;

import com.vesrati.weza.auth.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    User saveUser(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
