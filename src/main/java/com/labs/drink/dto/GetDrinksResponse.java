package com.labs.drink.dto;

import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * GET drinks response. Contains list of available characters. Can be used to list particular user's characters as
 * well as all characters in the game.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetDrinksResponse {

    /**
     * Represents single drink in list.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class Drink {

        /**
         * Unique id identifying drink.
         */
        private Long id;

        /**
         * Name of the drink.
         */
        private String name;

    }

    /**
     * Name of the selected characters.
     */
    @Singular
    private List<Drink> drinks;

    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<Collection<com.labs.drink.entity.Drink>, GetDrinksResponse> entityToDtoMapper() {
        return drinks -> {
            GetDrinksResponseBuilder response = GetDrinksResponse.builder();
            drinks.stream()
                    .map(drink -> Drink.builder()
                            .id(drink.getId())
                            .name(drink.getName())
                            .build())
                    .forEach(response::drink);
            return response.build();
        };
    }

}
