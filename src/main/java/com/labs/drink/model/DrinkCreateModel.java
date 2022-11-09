package com.labs.drink.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DrinkCreateModel {
    private String id;
    private String name;
    private String kindName;
    private String description;
    private String price;
    private String login;
}
