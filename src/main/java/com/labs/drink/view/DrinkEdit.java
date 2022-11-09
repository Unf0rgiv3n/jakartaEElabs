package com.labs.drink.view;

import com.labs.drink.entity.Drink;
import com.labs.drink.entity.Kind;
import com.labs.drink.model.DrinkEditModel;
import com.labs.drink.service.DrinkService;
import com.labs.user.entity.User;
import com.labs.user.service.UserService;
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
public class DrinkEdit implements Serializable {

    private final DrinkService drinkService;

    private final UserService userService;

    @Getter
    private Drink drink;
    private User user;

    @Getter
    private DrinkEditModel drinkEditModel;

    @Setter
    @Getter
    private long id;

    @Inject
    public DrinkEdit(DrinkService drinkService, UserService userService) {
        this.drinkService = drinkService;
        this.userService = userService;
    }

    public void init() throws IOException {
        Optional<Drink> drink = drinkService.find(id);
        if (drink.isPresent()) {
            this.drink = drink.get();
            this.user = userService.find(this.drink.getUser().getLogin()).get();
            this.drinkEditModel = DrinkEditModel.builder()
                    .id(this.drink.getId().toString())
                    .name(this.drink.getName())
                    .price(Double.toString(this.drink.getPrice()))
                    .kindName(this.drink.getKind().getName())
                    .login(user.getLogin())
                    .description(this.drink.getDescription())
                    .build();
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Not found!");
        }
    }

    public String updateAction() {
        try {
            Kind kind = new Kind();
            kind.setName(drinkEditModel.getKindName());

            Drink drink = Drink.builder()
                    .id(Long.parseLong(drinkEditModel.getId()))
                    .price(Integer.parseInt(drinkEditModel.getPrice()))
                    .description(drinkEditModel.getDescription())
                    .kind(kind)
                    .name(drinkEditModel.getName())
                    .user(userService.find(drinkEditModel.getLogin()).get())
                    .build();

            drinkService.delete(this.drink.getId());
            this.user.deleteDrink(this.drink);
            userService.update(this.user);
            drinkService.create(drink);

            for (Drink d : this.user.getDrinks()) {
                System.out.println(d.getId());
                System.out.println("done");
            }

            return "../user/user_list_view.xhtml?faces-redirect=true";
        } catch (IllegalArgumentException | NoSuchElementException | NullPointerException e) {
            System.out.println(e);
            return "drink_create.xhtml?login=" + this.user.getLogin() + "&faces-redirect=true";
        }
    }
}