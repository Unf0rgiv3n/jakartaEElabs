package com.labs.drink.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PutKindRequest {
        private String name;
        private int concentration;
}
