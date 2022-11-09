package com.labs.drink.view;

import com.labs.drink.entity.Drink;
import com.labs.drink.service.DrinkService;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@RequestScoped
@Named
public class DrinkView implements Serializable {

    private final DrinkService drinkService;

    @Getter
    private Drink drink;

    @Setter
    @Getter
    private Long id;

    @Inject
    public DrinkView(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    public void init() throws IOException {
        Optional<Drink> drink = drinkService.find(id);
        if (drink.isPresent()) {
            this.drink = drink.get();
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Present not found!");
        }
    }
}
