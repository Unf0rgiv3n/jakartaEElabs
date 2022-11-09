package com.labs.datastore;

import lombok.extern.java.Log;
import com.labs.drink.entity.Drink;
import com.labs.drink.entity.Kind;
import com.labs.serialization.CloningUtility;
import com.labs.user.entity.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * For the sake of simplification instead of using real database this example is using an data source object which
 * should be put in servlet context in a single instance. In order to avoid {@link
 * java.util.ConcurrentModificationException} all methods are synchronized. Normally synchronization would be carried on
 * by the database server.
 */
@Log
@ApplicationScoped
public class DataStore {

    /**
     * Set of all available professions.
     */
    private Set<Kind> kinds = new HashSet<>();

    /**
     * Set of all characters.
     */
    private Set<Drink> drinks = new HashSet<>();

    /**
     * Set of all users.
     */
    private Set<User> users = new HashSet<>();

    /**
     * Seeks for all kinds.
     *
     * @return list (can be empty) of all professions
     */
    public synchronized List<Kind> findAllKinds() {
        return kinds.stream()
                .map(CloningUtility::clone)
                .collect(Collectors.toList());
    }

    /**
     * Seeks for the kind in the memory storage.
     *
     * @param name name of the kind
     * @return container (can be empty) with kind if present
     */
    public synchronized Optional<Kind> findKind(String name) {
        return kinds.stream()
                .filter(kind -> kind.getName().equals(name))
                .findFirst()
                .map(CloningUtility::clone);
    }

    /**
     * Stores new kind.
     *
     * @param kind new kind to be stored
     * @throws IllegalArgumentException if kind with provided name already exists
     */
    public synchronized void createKind(Kind kind) throws IllegalArgumentException {
        findKind(kind.getName()).ifPresentOrElse(
                original -> {
                    throw new IllegalArgumentException(
                            String.format("The kind name \"%s\" is not unique", kind.getName()));
                },
                () -> kinds.add(CloningUtility.clone(kind)));
    }

    /**
     * Seeks for all drinks.
     *
     * @return list (can be empty) of all drinks
     */
    public synchronized List<Drink> findAllDrinks() {
        return drinks.stream()
                .map(CloningUtility::clone)
                .collect(Collectors.toList());
    }

    /**
     * Seeks for single drink.
     *
     * @param id drink's id
     * @return container (can be empty) with drink
     */
    public synchronized Optional<Drink> findDrink(Long id) {
        return drinks.stream()
                .filter(drink -> drink.getId().equals(id))
                .findFirst()
                .map(CloningUtility::clone);
    }

    /**
     * Stores new drink.
     *
     * @param drink new drink
     */
    public synchronized void createDrink(Drink drink) throws IllegalArgumentException {
        drink.setId(findAllDrinks().stream()
                .mapToLong(Drink::getId)
                .max().orElse(0) + 1);
        drinks.add(CloningUtility.clone(drink));
    }

    /**
     * Updates existing drink.
     *
     * @param drink drink to be updated
     * @throws IllegalArgumentException if drink with the same id does not exist
     */
    public synchronized void updateDrink(Drink drink) throws IllegalArgumentException {
        findDrink(drink.getId()).ifPresentOrElse(
                original -> {
                    drinks.remove(original);
                    drinks.add(CloningUtility.clone(drink));
                },
                () -> {
                    throw new IllegalArgumentException(
                            String.format("The drink with id \"%d\" does not exist", drink.getId()));
                });
    }

    /**
     * Deletes existing drink.
     *
     * @param id drink's id
     * @throws IllegalArgumentException if drink with provided id does not exist
     */
    public synchronized void deleteDrink(Long id) throws IllegalArgumentException {
        findDrink(id).ifPresentOrElse(
                original -> drinks.remove(original),
                () -> {
                    throw new IllegalArgumentException(
                            String.format("The drink with id \"%d\" does not exist", id));
                });
    }

    /**
     * Seeks for single user.
     *
     * @param login user's login
     * @return container (can be empty) with user
     */
    public synchronized Optional<User> findUser(String login) {
        return users.stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst()
                .map(CloningUtility::clone);
    }

    /**
     * Seeks for all users.
     *
     * @return collection of all users
     */
    public synchronized List<User> findAllUsers() {
        return users.stream()
                .map(CloningUtility::clone)
                .collect(Collectors.toList());
    }

    public synchronized void updateUser(User user) {
        findUser(user.getLogin()).ifPresentOrElse(
                og -> {
                    System.out.println(og.isHaveAvatar());
                    users.remove(og);
                    users.add(CloningUtility.clone(user));
                },
                () -> {
                    throw new IllegalArgumentException(
                            String.format("The user with id \"%d\" does not exist", user.getLogin()));
                });
    }

    /**
     * Stores new user.
     *
     * @param user new user to be stored
     * @throws IllegalArgumentException if user with provided login already exists
     */
    public synchronized void createUser(User user) throws IllegalArgumentException {
        findUser(user.getLogin()).ifPresentOrElse(
                original -> {
                    throw new IllegalArgumentException(
                            String.format("The user login \"%s\" is not unique", user.getLogin()));
                },
                () -> users.add(CloningUtility.clone(user)));
    }

    public synchronized void deleteUser(User user) {
        findUser(user.getLogin()).ifPresentOrElse(
                og -> {
                    users.remove(og);
                },
                () -> {
                    throw new IllegalArgumentException(
                            String.format("The user with id \"%d\" does not exist", user.getLogin()));
                });
    }

}
