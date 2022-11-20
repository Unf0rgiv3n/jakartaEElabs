package com.labs.drink.service;

import com.labs.drink.entity.Drink;
import com.labs.drink.entity.Kind;
import com.labs.drink.repository.DrinkRepository;
import com.labs.drink.repository.KindRepository;
import lombok.NoArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
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
    private DrinkRepository drinkRepository;

    /**
     * @param repository repository for profession entity
     */
    @Inject
    public KindService(KindRepository repository, DrinkRepository drinkRepository)
    {
        this.repository = repository;
        this.drinkRepository = drinkRepository;
    }

    /**
     * @param name name of the profession
     * @return container with profession entity
     */
    public Optional<Kind> find(String name) {
        return repository.find(name);
    }

    public List<Kind> findAll() {
        return repository.findAll();
    }

    /**
     * Stores new profession in the data store.
     *
     */
    public void create(Kind kind) {
        repository.create(kind);
    }

    public void update(Kind kind) {repository.update(kind);}

    public void delete(Kind kind) {
        List<Drink> drinks = drinkRepository.findAllByKind(kind);
        drinks.forEach(drinkRepository::delete);
        repository.delete(kind);
    }
}

