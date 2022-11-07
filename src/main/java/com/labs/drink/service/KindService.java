package com.labs.drink.service;

import com.labs.drink.entity.Kind;
import com.labs.drink.repository.KindRepository;

import java.util.Optional;

/**
 * Service layer for all business actions regarding character's profession entity.
 */
public class KindService {

    /**
     * Repository for profession entity.
     */
    private KindRepository repository;

    /**
     * @param repository repository for profession entity
     */
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
     * @param profession new profession to be saved
     */
    public void create(Kind profession) {
        repository.create(profession);
    }

}

