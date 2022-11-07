package com.labs.user.dto;

import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * GET users response. Contains user names of users in the system. User name ios the same as login.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetUsersResponse {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class User {
        private String login;
    }

    /**
     * List of all user names.
     */
    @Singular
    private List<User> users;

    public static Function<Collection<com.labs.user.entity.User>, GetUsersResponse> entityToDtoMapper() {
        return users -> {
            GetUsersResponseBuilder response = GetUsersResponse.builder();
            users.stream()
                    .map(character -> User.builder()
                            .login(character.getLogin())
                            .build())
                    .forEach(response::user);
            return response.build();
        };
    }
}

