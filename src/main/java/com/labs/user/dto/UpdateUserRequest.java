package com.labs.user.dto;

import lombok.*;

import java.time.LocalDate;

/**
 * PUT user request. Contains only fields which can be changed byt the user while updating its profile. User is defined
 * in {@link user.entity.User}.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdateUserRequest {

    /**
     * User's name.
     */
    private String name;

    /**
     * User's surname.
     */
    private String surname;

    /**
     * User's birth day.
     */
    private LocalDate birthDate;

    /**
     * User's email.
     */
    private String email;

}


