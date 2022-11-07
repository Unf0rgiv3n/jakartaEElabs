package com.labs.user.entity;

import com.labs.drink.entity.Drink;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class User implements Serializable {

    private String login;
    private String name;
    private String surname;
    private LocalDate birthDate;
    @ToString.Exclude
    private String password;
    private String email;
    @ToString.Exclude//It's common to exclude lists from toString
    @EqualsAndHashCode.Exclude
    private List<Drink> drinks;
    private boolean isHaveAvatar;
    private byte[] avatar;

}
