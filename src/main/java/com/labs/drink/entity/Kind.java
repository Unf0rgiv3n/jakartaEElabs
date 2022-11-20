package com.labs.drink.entity;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class Kind implements Serializable {
    private String name;
    private int concentration;
    private List<Drink> drinks;

    public void addDrink(Drink drink) {
        drinks.add(drink);
    }

    public void deleteDrink(Drink drink) {
        drinks.remove(drink);
    }
}
