package book.store.intro.service.user;

import book.store.intro.dto.user.request.UserRegistrationRequestDto;
import book.store.intro.dto.user.response.UserResponseDto;
import book.store.intro.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
