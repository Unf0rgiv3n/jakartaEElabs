package com.labs.drink.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import com.labs.user.entity.User;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode()
public class Drink implements Serializable {
    private Long id;
    private String name;
    private String description;
    private int price;
    private Kind kind;
    private User user;
    private LocalDate consumed;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] image;

}

