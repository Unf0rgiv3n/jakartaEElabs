package com.labs.user.dto;

import lombok.*;
import com.labs.user.entity.User;

import java.time.LocalDate;
import java.util.function.Function;

/**
 * GET user response. Contains only field's which can be displayed on frontend. User is defined in
 * {@link user.entity.User}.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetUserResponse {

    private String login;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String email;

    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<User, GetUserResponse> entityToDtoMapper() {
        return user -> GetUserResponse.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .birthDate(user.getBirthDate())
                .email(user.getEmail())
                .login(user.getLogin())
                .build();
    }
}

