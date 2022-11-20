package com.labs.drink.controller;

import com.labs.servlet.MimeTypes;
import com.labs.drink.dto.PutKindRequest;
import com.labs.drink.dto.GetDrinkResponse;
import com.labs.drink.entity.Kind;
import com.labs.drink.service.KindService;
import com.labs.drink.dto.PutDrinkRequest;
import com.labs.drink.dto.GetDrinksResponse;
import com.labs.drink.entity.Drink;
import com.labs.drink.service.DrinkService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Path("/kinds/{nameKind}/drinks")
public class DrinkController {
    private DrinkService drinkService;
    private KindService kindService;

    public DrinkController(){};

    @Inject
    void setKindService(KindService kindService) {
        this.kindService = kindService;
    }

    @Inject
    void setDrinkService(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @GET
    @Produces(MimeTypes.APPLICATION_JSON)
    public Response getDrinks(@PathParam("nameKind") String nameKind) {
        Optional <List<Drink>> drinks = drinkService.findByKind(nameKind);
        if(drinks.isPresent()) {
            return Response.ok(GetDrinksResponse.entityToDtoMapper().apply(drinks.get())).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{id}")
    @Produces(MimeTypes.APPLICATION_JSON)
    public Response getDrinks(@PathParam("nameKind") String nameKind, @PathParam("id") Long id) {
        Optional <List<Drink>> drinks = drinkService.findByKind(nameKind);
        if (drinks.isPresent()) {
            Optional<Drink> drink = drinks.get().stream()
                    .filter(drink1 -> drink1.getId().equals(id))
                    .findAny();
            return Response.ok(GetDrinkResponse.entityToDtoMapper().apply(drink.get())).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Produces(MimeTypes.APPLICATION_JSON)
    public Response createDrink(@PathParam("nameKind") String nameKind, PutDrinkRequest putDrinkRequest){
        try {
            Optional<Kind> kind = kindService.find(nameKind);
            if (kind.isPresent()) {
                drinkService.create2(Drink.builder()
                        .id(putDrinkRequest.getId())
                        .name(putDrinkRequest.getName())
                        .description(putDrinkRequest.getDescription())
                        .price(putDrinkRequest.getPrice())
                        .consumed(putDrinkRequest.getConsumed())
                        .kind(kind.get())
                        .build());
                return Response.status(Response.Status.CREATED).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }


    @PUT
    @Path("{id}")
    @Produces(MimeTypes.APPLICATION_JSON)
    public Response updateDrink(@PathParam("nameKind") String nameKind, @PathParam("id") Long id, PutDrinkRequest putDrinkRequest){
        Optional<Kind> kind = kindService.find(nameKind);
        if(kind.isPresent()){
            drinkService.update(Drink.builder()
                    .id(id)
                    .name(putDrinkRequest.getName())
                    .description(putDrinkRequest.getDescription())
                    .price(putDrinkRequest.getPrice())
                    .consumed(putDrinkRequest.getConsumed())
                    .kind(kind.get())
                    .build());
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MimeTypes.APPLICATION_JSON)
    public Response deleteDrink(@PathParam("nameKind") String nameKind, @PathParam("id") Long id){
        Optional<List<Drink>> drinks = drinkService.findByKind(nameKind);
        if(drinks.isPresent()){
            Optional<Drink> drink = drinks.get().stream()
                    .filter(drink1 -> drink1.getId().equals(id))
                    .findAny();
            if(drink.isPresent()){
                drinkService.delete(drink.get().getId());
                return Response.status(Response.Status.OK).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
