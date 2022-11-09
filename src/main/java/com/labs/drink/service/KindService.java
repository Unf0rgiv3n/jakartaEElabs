package com.labs.drink.service;

import com.labs.drink.entity.Kind;
import com.labs.drink.repository.KindRepository;
import lombok.NoArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Service layer for all business actions regarding character's profession entity.
 */
@ApplicationScoped
@NoArgsConstructor
public class KindService {

    /**
     * Repository for profession entity.
     */
    private KindRepository repository;

    /**
     * @param repository repository for profession entity
     */
    @Inject
    public KindService(KindRepository repository) {
        this.repository = repository;
    }

    /**
     * @param name name of the profession
     * @return container with profession entity
     */
    public Optional<Kind> find(String name) {
        return repository.find(name);
    }

    /**
     * Stores new profession in the data store.
     *
     */
    public void create(Kind kind) {
        repository.create(kind);
    }

    public void delete(Kind kind) { repository.delete(kind);}
}

