package com.labs.drink.repository;

import com.labs.datastore.DataStore;
import com.labs.drink.entity.Drink;
import com.labs.repository.Repository;
import com.labs.serialization.CloningUtility;
import com.labs.user.entity.User;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository for character entity. Repositories should be used in business layer (e.g.: in services).
 */
@Dependent
public class DrinkRepository implements Repository<Drink, Long> {

    /**
     * Underlying data store. In future should be replaced with database connection.
     */
    private DataStore store;

    /**
     * @param store data store
     */
    @Inject
    public DrinkRepository(DataStore store) {
        this.store = store;
    }

    @Override
    public Optional<Drink> find(Long id) {
        return store.findDrink(id);
    }

    @Override
    public List<Drink> findAll() {
        return store.findAllDrinks();
    }

    @Override
    public void create(Drink entity) {
        store.createDrink(entity);
    }

    @Override
    public void delete(Drink entity) {
        store.deleteDrink(entity.getId());
    }

    @Override
    public void update(Drink entity) {
        store.updateDrink(entity);
    }

    /**
     * Seeks for single user's character.
     *
     * @param id   character's id
     * @param user characters's owner
     * @return container (can be empty) with character
     */
    public Optional<Drink> findByIdAndUser(Long id, User user) {
        return store.findAllDrinks().stream()
                .filter(drink -> drink.getUser().equals(user))
                .filter(drink -> drink.getId().equals(id))
                .findFirst()
                .map(CloningUtility::clone);
    }

    /**
     * Seeks for all user's characters.
     *
     * @param user characters' owner
     * @return list (can be empty) of user's characters
     */
    public List<Drink> findAllByUser(User user) {
        return store.findAllDrinks().stream()
                .filter(drink -> drink.getUser().equals(user))
                .map(CloningUtility::clone)
                .collect(Collectors.toList());
    }

}
