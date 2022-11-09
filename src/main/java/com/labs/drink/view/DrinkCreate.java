package com.labs.drink.view;

import com.labs.drink.entity.Drink;
import com.labs.drink.entity.Kind;
import com.labs.user.entity.User;
import com.labs.user.service.UserService;
import com.labs.drink.model.DrinkCreateModel;
import com.labs.drink.service.DrinkService;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Optional;

@SessionScoped
@Named
public class DrinkCreate implements Serializable {

    private final DrinkService drinkService;
    private final UserService userService;

    @Getter
    private User user;

    @Getter
    private DrinkCreateModel drinkCreateModel;

    @Setter
    @Getter
    private String login;

    @Inject
    public DrinkCreate(DrinkService drinkService, UserService userService) {
        this.drinkService = drinkService;
        this.userService = userService;
        this.drinkCreateModel = new DrinkCreateModel();
    }

    public void init() throws IOException {
        Optional<User> user = userService.find(login);
        if (user.isPresent()) {
            this.user = user.get();
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Not found!");
        }
    }

    public String saveAction() {
        try {
            Kind kind = new Kind();
            kind.setName(drinkCreateModel.getKindName());

            Drink visit = Drink.builder()
                    .id(Long.parseLong(drinkCreateModel.getId()))
                    .price(Integer.parseInt(drinkCreateModel.getPrice()))
                    .name(drinkCreateModel.getName())
                    .kind(kind)
                    .description(drinkCreateModel.getDescription())
                    .user(userService.find(login).get())
                    .build();

            drinkService.create(visit);

            return "../user/user_list_view.xhtml?faces-redirect=true";
        } catch (IllegalArgumentException | NoSuchElementException | NullPointerException e) {
            System.out.println(e);
            return "drink_create.xhtml?login=" + login + "&faces-redirect=true";
        }
    }
}