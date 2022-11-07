package com.labs.drink.dto;

import com.labs.drink.entity.Drink;
import com.labs.drink.entity.Kind;
import lombok.*;
import com.labs.user.entity.User;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * POST character request. Contains only fields that can be set up byt the user while creating a new character.How
 * character is described is defined in {@link } and
 * {@link} classes.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CreateDrinkRequest {
    private String name;
    private String description;
    private int price;
    private String kind;
    private User user;
    private LocalDate consumed;
    /**
     * @param kindFunction function for converting profession name to profession entity object
     * @param userSupplier       supplier for providing new character owner
     * @return mapper for convenient converting dto object to entity object
     */
    public static Function<CreateDrinkRequest, Drink> dtoToEntityMapper(
            Function<String, Kind> kindFunction,
            Supplier<User> userSupplier) {
        return request -> Drink.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .consumed(request.getConsumed())
                .kind(kindFunction.apply(request.getKind()))
                .user(userSupplier.get())
                .build();
    }

}
