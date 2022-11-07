package com.labs.drink.dto;

import com.labs.drink.entity.Drink;
import lombok.*;

import java.util.function.BiFunction;

/**
 * PUT character request. Contains all fields that can be updated by the user. .How character is described is defined in
 * {@link pl.edu.pg.eti.kask.rpg.character.entity.Character} and {@link pl.edu.pg.eti.kask.rpg.creature.entity.Creature}
 * classes.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdateDrinkRequest {
    private String name;
    private String description;
    private int price;

    /**
     * @return updater for convenient updating entity object using dto object
     */
    public static BiFunction<Drink, UpdateDrinkRequest, Drink> dtoToEntityUpdater() {
        return (drink, request) -> {
            drink.setName(request.getName());
            drink.setDescription(request.getDescription());
            drink.setPrice(request.getPrice());
            return drink;
        };
    }
}
