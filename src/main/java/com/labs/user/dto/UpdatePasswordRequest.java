package com.labs.user.dto;

import lombok.*;

/**
 * PUT password request. Usually there are seperate forms for updating user profile (name, etc.) and password.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdatePasswordRequest {

    /**
     * New value for user's passowrd.
     */
    private String password;

}
