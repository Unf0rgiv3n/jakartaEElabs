package com.labs.user.dto;

import lombok.*;

import java.time.LocalDate;

/**
 * PSOT user request. Contains only fields that can be set during user creation. User is defined in
 * {@link user.entity.User}.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CreateUserRequest {
    private String login;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String password;
    private String email;
}

