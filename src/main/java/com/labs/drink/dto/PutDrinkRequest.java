package com.labs.drink.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PutDrinkRequest {
    private Long id;
    private String name;
    private String description;
    private int price;
    private LocalDate consumed;
}
