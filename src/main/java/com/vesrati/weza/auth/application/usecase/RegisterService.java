package com.vesrati.weza.auth.application.usecase;

import com.vesrati.weza.auth.application.dto.AuthResponse;
import com.vesrati.weza.auth.application.dto.LoginRequest;
import com.vesrati.weza.auth.application.dto.RegisterRequest;
import com.vesrati.weza.auth.application.port.in.RegisterUseCase;
import com.vesrati.weza.auth.domain.event.AuthEvent;
import com.vesrati.weza.auth.domain.exception.AuthException;
import com.vesrati.weza.auth.domain.model.Password;
import com.vesrati.weza.auth.domain.model.PhoneNumber;
import com.vesrati.weza.auth.domain.model.User;
import com.vesrati.weza.auth.domain.port.out.TokenPort;
import com.vesrati.weza.auth.domain.port.out.UserRepositoryPort;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterService implements RegisterUseCase {
    private final UserRepositoryPort userRepository;
    private final TokenPort tokenPort;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;


    @Override
    @Transactional
    public AuthResponse execute(RegisterRequest request) {
        log.info("Registering new user: {}", request.email());

        if (userRepository.existsByEmail(request.email()))
            throw new AuthException("User already exists");

        if (request.phone() != null && userRepository.existsByPhone(normalizedPhone(request.phone())))
            throw new AuthException("Phone number already exists");

        User user = User.create(
                request.name(), request.email(), request.phone(), request.role(), request.password()
        );

        String encoded = passwordEncoder.encode(user.getPasswordHash().hashed());
        user.setPassword(Password.fromEncoded(encoded));

        user = userRepository.saveUser(user);
        log.info("User registered: {}", user.getId());

        String accessToken = tokenPort.generateToken(user);
        String refreshToken = tokenPort.generateRefreshToken(user);

        eventPublisher.publishEvent(
                new AuthEvent.UserRegisteredEvent(
                        user.getId(), user.getName(), user.getEmail().value(), user.getRole().toString()
                )
        );

        return AuthResponse.of(accessToken, refreshToken, 3600L, user);
    }

    private String normalizedPhone(String phone) {
        return new PhoneNumber(phone).value();
    }
}
