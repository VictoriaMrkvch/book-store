package book.store.intro.mapper.user;

import book.store.intro.config.MapperConfig;
import book.store.intro.dto.user.request.UserRegistrationRequestDto;
import book.store.intro.dto.user.response.UserResponseDto;
import book.store.intro.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
