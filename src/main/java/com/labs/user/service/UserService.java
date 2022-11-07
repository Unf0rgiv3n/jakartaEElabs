package com.labs.user.service;

import lombok.NoArgsConstructor;
import com.labs.user.entity.User;
import com.labs.user.repository.UserRepository;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
public class UserService {

    @Resource(name = "avatars.dir")
    private String avatarsDir;

    /**
     * Repository for user entity.
     */
    private UserRepository repository;

    /**
     * @param repository repository for character entity
     */
    @Inject
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * @param login existing username
     * @return container (can be empty) with user
     */
    public Optional<User> find(String login) {
        return repository.find(login);
    }


    public Collection<User> findAll() {
        return repository.findAll();
    }
    /**
     * Seeks for single user using login and password. Can be use in authentication module.
     *
     * @param login    user's login
     * @param password user's password (hash)
     * @return container (can be empty) with user
     */
    public Optional<User> find(String login, String password) {
        return repository.findByLoginAndPassword(login, password);
    }

    /**
     * Saves new user.
     *
     * @param user new user to be saved
     */
    public void create(User user) {
        repository.create(user);
    }

    public void deleteAvatar(User user) {
        Path path = Path.of(avatarsDir, user.getLogin()+".png");
        try{
            Files.deleteIfExists(path);
        } catch (Exception ex){

        }
        user.setHaveAvatar(false);
        System.out.println(user.isHaveAvatar());
        user.setAvatar(new byte[0]);
        repository.update(user);
    }

    public void createAvatar(User user, Part avatar) {
        if (avatar != null && !avatar.getSubmittedFileName().isEmpty()) {
            Path path = Path.of(avatarsDir, user.getLogin()+".png");
            try {
                if (!Files.exists(path)) {
                    Files.createFile(path);
                    Files.write(path, avatar.getInputStream().readAllBytes(), StandardOpenOption.WRITE);
                    user.setHaveAvatar(true);
                    user.setAvatar(avatar.getInputStream().readAllBytes());
                    repository.update(user);
                }
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public void updateAvatar(User user, Part avatar) {
        System.out.println(avatarsDir);
        if (user.isHaveAvatar()) {
            deleteAvatar(user);
        }
        createAvatar(user, avatar);
    }

    public void saveAvatar(User user){
        Path path = Path.of(avatarsDir, user.getLogin()+".png");

        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                Files.write(path, user.getAvatar(), StandardOpenOption.WRITE);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
