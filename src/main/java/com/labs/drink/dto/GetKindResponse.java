package com.labs.drink.dto;

import com.labs.drink.entity.Kind;
import lombok.*;

import java.util.function.Function;

/**
 * GET profession response. Described details about selected profession. Can be used to present description while
 * character creation or on character's stat page. How profession is described is defined in
 * {@link pl.edu.pg.eti.kask.rpg.character.entity.Profession}.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetKindResponse {

    private String name;
    private int concentration;

    public static Function<Kind, GetKindResponse> entityToDtoMapper() {
        return kind -> GetKindResponse.builder()
                .name(kind.getName())
                .concentration(kind.getConcentration())
                .build();
    }

}
