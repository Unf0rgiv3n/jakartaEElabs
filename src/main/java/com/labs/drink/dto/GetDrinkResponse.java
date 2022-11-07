package com.labs.drink.dto;

import com.labs.drink.entity.Drink;
import lombok.*;

import java.time.LocalDate;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetDrinkResponse {
    private Long id;
    private String name;
    private String description;
    private int price;
    private String kind;
    private LocalDate consumed;

    public static Function<Drink, GetDrinkResponse> entityToDtoMapper() {
        return drink -> GetDrinkResponse.builder()
                .id(drink.getId())
                .name(drink.getName())
                .description(drink.getDescription())
                .price(drink.getPrice())
                .kind(drink.getKind().getName())
                .consumed(drink.getConsumed())
                .build();
    }
}
