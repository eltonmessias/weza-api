package com.vesrati.weza.auth.application.usecase;

import com.vesrati.weza.auth.application.dto.AuthResponse;
import com.vesrati.weza.auth.application.dto.LoginRequest;
import com.vesrati.weza.auth.application.port.in.LoginUseCase;
import com.vesrati.weza.auth.domain.event.AuthEvent;
import com.vesrati.weza.auth.domain.exception.AuthException;
import com.vesrati.weza.auth.domain.model.PasswordEncoder;
import com.vesrati.weza.auth.domain.model.User;
import com.vesrati.weza.auth.domain.port.out.TokenPort;
import com.vesrati.weza.auth.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenPort tokenPort;
    private final ApplicationEventPublisher eventPublisher;


    @Override
    @Transactional(readOnly = true)
    public AuthResponse execute(LoginRequest request) {
        final String INVALID = "Email or password is invalid";

        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new AuthException(INVALID));

        user.assertActivate();

        if(!user.matchesPassword(request.password(), passwordEncoder))
            throw new AuthException(INVALID);

        String accessToken = tokenPort.generateToken(user);
        String refreshToken = tokenPort.generateRefreshToken(user);

        eventPublisher.publishEvent(
                new AuthEvent.UserLoggedInEvent(user.getId(), user.getEmail().value())
        );

        log.info("Login: userId={}", user.getId());
        return AuthResponse.of(accessToken, refreshToken, 3600L, user);
    }
}
