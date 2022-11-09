package com.labs.user.view;

import com.labs.user.entity.User;
import com.labs.user.service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.List;

@RequestScoped
@Named
public class UserList {
    private UserService userService;
    private Collection<User> users;

    @Inject
    public UserList(UserService userService) {
        this.userService = userService;
    }

    public Collection<User> getUsers() {
        users = userService.findAll();
        return users;
    }

    public String deleteAction(User user) {
        userService.delete(user);
        return "user_list_view?faces-redirect=true";
    }
}