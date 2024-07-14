package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;

    private UserController userController;

    private HttpSession session;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
        session = new MockHttpSession();
    }

    @Test
    public void whenRegisterUserThenSaveAndGetToVacanciesPage() {
        var user = new User(1, "roman@gmail.com", "Roman", "123456");
        when(userService.save(user)).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.register(model, user);

        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    public void whenGetToRegisterPageThenGetThere() {
        var model = new ConcurrentModel();
        var view = userController.getRegistrationPage(model);

        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenGetToLoginPageThenGetThere() {
        var model = new ConcurrentModel();
        var view = userController.getLoginPage(model);

        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenLoginUserThenLoginAndGetToVacanciesPage() {
        var user = new User(1, "roman@gmail.com", "Roman", "123456");
        var view = userController.loginUser(user);
        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    public void whenLogoutUserThenLogoutAndGetToLoginPage() {
        var user = new User(1, "roman@gmail.com", "Roman", "123456");
        userController.loginUser(user);
        var view = userController.logout(session);
        assertThat(view).isEqualTo("redirect:/users/login");
    }

}