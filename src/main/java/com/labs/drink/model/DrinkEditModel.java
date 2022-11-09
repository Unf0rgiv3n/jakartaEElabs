package com.labs.drink.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class DrinkEditModel {
    private String id;
    private String name;
    private String kindName;
    private String description;
    private String price;
    private String login;
}