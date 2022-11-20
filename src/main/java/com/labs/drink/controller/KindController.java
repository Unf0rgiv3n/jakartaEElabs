package com.labs.drink.controller;

import com.labs.drink.dto.*;
import com.labs.servlet.MimeTypes;
import com.labs.drink.entity.Kind;
import com.labs.drink.service.KindService;

import javax.inject.Inject;
import javax.swing.text.html.Option;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/kinds")
public class KindController {
    private KindService kindService;

    public KindController(){}

    @Inject
    void setKindService(KindService kindService) {
        this.kindService = kindService;
    }

    @GET
    @Produces(MimeTypes.APPLICATION_JSON)
    public Response getKinds() {
        try {
            return Response.ok(GetKindsResponse.entityToDtoMapper().apply(kindService.findAll())).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_GATEWAY).build();
        }
    }

    @GET
    @Path("{name}")
    @Produces(MimeTypes.APPLICATION_JSON)
    public Response getKinds(@PathParam("name") String name) {
        Optional<Kind> kind = kindService.find(name);
        if (kind.isPresent()) {
            return Response.ok(GetKindResponse.entityToDtoMapper().apply(kind.get())).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("{name}")
    @Produces(MimeTypes.APPLICATION_JSON)
    public Response updateKind(@PathParam("name") String name, PutKindRequest request){
        Optional<Kind> kind = kindService.find(name);
        if(kind.isPresent()){
            kindService.update(Kind.builder()
                    .name(name)
                    .concentration(request.getConcentration())
                    .build());
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Produces(MimeTypes.APPLICATION_JSON)
    public Response createKind(PutKindRequest request) {
        try {
            kindService.create(
                    Kind.builder()
                            .name(request.getName())
                            .concentration(request.getConcentration())
                            .build());
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @DELETE
    @Path("{name}")
    @Produces(MimeTypes.APPLICATION_JSON)
    public Response deleteKind(@PathParam("name") String name) {
        Optional<Kind> kind = kindService.find(name);
        if(kind.isPresent()) {
            kindService.delete(kind.get());
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
