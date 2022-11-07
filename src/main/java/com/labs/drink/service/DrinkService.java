package com.labs.drink.service;

import com.labs.drink.entity.Drink;
import com.labs.drink.repository.DrinkRepository;
import com.labs.user.entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for all business actions regarding character entity.
 */
public class DrinkService {

    /**
     * Repository for character entity.
     */
    private DrinkRepository repository;

    /**
     * @param repository repository for character entity
     */
    public DrinkService(DrinkRepository repository) {
        this.repository = repository;
    }

    /**
     * Finds single character.
     *
     * @param id character's id
     * @return container with character
     */
    public Optional<Drink> find(Long id) {
        return repository.find(id);
    }

    /**
     * @param id   character's id
     * @param user existing user
     * @return selected character for user
     */
    public Optional<Drink> find(User user, Long id) {
        return repository.findByIdAndUser(id, user);
    }

    /**
     * @return all available characters
     */
    public List<Drink> findAll() {
        return repository.findAll();
    }

    /**
     * @param user existing user, character's owner
     * @return all available characters of the selected user
     */
    public List<Drink> findAll(User user) {
        return repository.findAllByUser(user);
    }

    /**
     * Creates new character.
     *
     * @param drink new character
     */
    public void create(Drink drink) {
        repository.create(drink);
    }

    /**
     * Updates existing character.
     *
     * @param drink character to be updated
     */
    public void update(Drink drink) {
        repository.update(drink);
    }

    /**
     * Deletes existing character.
     *
     * @param drink existing character's id to be deleted
     */
    public void delete(Long drink) {
        repository.delete(repository.find(drink).orElseThrow());
    }

    /**
     * Updates portrait of the character.
     *
     * @param id character's id
     * @param is input stream containing new portrait
     */
    public void updatePortrait(Long id, InputStream is) {
        repository.find(id).ifPresent(drink -> {
            try {
                drink.setImage(is.readAllBytes());
                repository.update(drink);
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }

}
