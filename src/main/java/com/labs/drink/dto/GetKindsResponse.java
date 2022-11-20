package com.labs.drink.dto;

import com.labs.drink.entity.Kind;
import lombok.*;

import javax.json.bind.annotation.JsonbPropertyOrder;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * GET professions response. Returns list of all available professions names.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetKindsResponse {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    @JsonbPropertyOrder({"name", "concetration"})
    public static class Kind {
        private String name;
        private int concetration;
    }

    @Singular
    private List<Kind> kinds;

    public static Function<Collection<com.labs.drink.entity.Kind>, GetKindsResponse> entityToDtoMapper() {
        return kinds -> {
            GetKindsResponseBuilder response = GetKindsResponse.builder();
            kinds.stream()
                    .map(kind -> Kind.builder()
                            .name(kind.getName())
                            .concetration(kind.getConcentration())
                            .build())
                    .forEach(response::kind);
            return response.build();
        };
    }
}
