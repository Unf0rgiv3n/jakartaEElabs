package com.labs.drink.dto;

import lombok.*;

import java.util.List;

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
    private List<String> kinds;
}
