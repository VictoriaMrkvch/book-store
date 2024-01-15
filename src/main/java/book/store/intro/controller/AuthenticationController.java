package book.store.intro.controller;

import book.store.intro.dto.user.request.UserLoginRequestDto;
import book.store.intro.dto.user.request.UserRegistrationRequestDto;
import book.store.intro.dto.user.response.UserLoginResponseDto;
import book.store.intro.dto.user.response.UserResponseDto;
import book.store.intro.exception.RegistrationException;
import book.store.intro.security.AuthenticationService;
import book.store.intro.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @PostMapping("/registration")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }
}
