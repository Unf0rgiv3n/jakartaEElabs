package com.labs.configuration;

import lombok.SneakyThrows;

import com.labs.drink.entity.Drink;
import com.labs.drink.entity.Kind;
import com.labs.drink.service.DrinkService;
import com.labs.drink.service.KindService;
import com.labs.digest.Sha256Utility;
import com.labs.user.entity.User;
import com.labs.user.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.InputStream;
import java.time.LocalDate;

/**
 * Listener started automatically on servlet context initialized. Fetches instance of the datasource from the servlet
 * context and fills it with default content. Normally this class would fetch database datasource and init data only
 * in cases of empty database. When using persistence storage application instance should be initialized only during
 * first run in order to init database with starting data. Good place to create first default admin user.
 */
//@WebListener//using annotation does not allow to configure order

@ApplicationScoped
public class InitializedData {

    //    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        DrinkService drinkService = (DrinkService) sce.getServletContext().getAttribute("drinkService");
//        UserService userService = (UserService) sce.getServletContext().getAttribute("userService");
//        KindService kindService = (KindService) sce.getServletContext().getAttribute("kindService");
//        init(userService, drinkService, kindService);
//    }
    private UserService userService;
    private DrinkService drinkService;
    private KindService kindService;

    @Inject
    public InitializedData(UserService userService) {
        this.userService = userService;
    }

    public void contextInitialized(@Observes @Initialized(ApplicationScoped.class) Object init) {
        init();
    }

    private synchronized void init() {
        User admin = User.builder()
                .login("admin")
                .name("Jakub")
                .surname("Kwiatkowski")
                .birthDate(LocalDate.of(2000, 8, 17))
                .email("test@admin.com")
                .password(Sha256Utility.hash("adminadmin"))
                .isHaveAvatar(true)
                .avatar(getResourceAsByteArray("avatars/wyborowa.png"))
                .build();

        User jake = User.builder()
                .login("jake")
                .name("Jake")
                .surname("Jakowski")
                .birthDate(LocalDate.of(2001, 1, 16))
                .email("jake@example.com")
                .password(Sha256Utility.hash("useruser"))
                .isHaveAvatar(true)
                .avatar(getResourceAsByteArray("avatars/jager.png"))
                .build();

        User kevin = User.builder()
                .login("kevin")
                .name("Kevin")
                .surname("Pear")
                .birthDate(LocalDate.of(2001, 1, 16))
                .email("kevin@example.com")
                .password(Sha256Utility.hash("useruser"))
                .isHaveAvatar(false)
                .build();

        User alice = User.builder()
                .login("alice")
                .name("Alice")
                .surname("Grape")
                .birthDate(LocalDate.of(2002, 3, 19))
                .email("kevin@example.com")
                .password(Sha256Utility.hash("useruser"))
                .isHaveAvatar(true)
                .avatar(getResourceAsByteArray("avatars/jack.png"))
                .build();

        userService.create(admin);
        userService.create(kevin);
        userService.create(alice);
        userService.create(jake);

        userService.saveAvatar(admin);
        userService.saveAvatar(kevin);
        userService.saveAvatar(alice);
        userService.saveAvatar(jake);
    }

    /**
     * @param name name of the desired resource
     * @return array of bytes read from the resource
     */
    @SneakyThrows
    private byte[] getResourceAsByteArray(String name) {
        try (InputStream is = this.getClass().getResourceAsStream(name)) {
            return is.readAllBytes();
        }
    }

}
