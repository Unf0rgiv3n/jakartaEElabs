package com.labs.user.view;

import com.labs.user.entity.User;
import com.labs.user.service.UserService;
import com.labs.drink.entity.Drink;
import com.labs.drink.service.DrinkService;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

@SessionScoped
@Named
public class UserView implements Serializable {

    private final UserService userService;
    private final DrinkService drinkService;

    @Getter
    private User user;
    private Collection<Drink> drinks;

    @Setter
    @Getter
    private String login;

    @Inject
    public UserView(UserService userService, DrinkService drinkService) {
        this.userService = userService;
        this.drinkService = drinkService;
    }


    public void init() throws IOException {
        Optional<User> user = userService.find(login);
        if (user.isPresent()) {
            this.user = user.get();
            this.login = this.user.getLogin();
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Not found!");
        }
    }

    public Collection<Drink> getDrinks() {
        return drinkService.findAll(this.user);
    }

    public String deleteAction(Drink drink){
        drinkService.delete(drink.getId());
        this.user.deleteDrink(drink);
        userService.update(this.user);

        return "user_view.xhtml?login=" + user.getLogin() + "&faces-redirect=true";
    }
}
