package cz.tul.Chmelar.controllers;

import cz.tul.Chmelar.models.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AppControllerTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    private AppController appController;



    @Test
    public void testIndex() {
        AppController controller = new AppController();


        String result = controller.index();

        assertEquals("index", result);

    }

    @Test
    public void testLoginIndex() {
        AppController controller = new AppController();


        String result = controller.login_index();

        assertEquals("login_index", result);
    }


    @Test
    public void testUserAccount() {
        /*String email = "test@example.com";
        Account[] accounts = new Account[1];
        History[] history = new History[1];
        User user = new User(email, "$2a$10$q.ta9BP8.4Cn7lGBsA82h.f8L8IDowxO.Org9hBKxTiAf8umgtuny", "Jmeno", "Tester", "123456", "4454564 564 6544654", accounts, history);

        Mockito.when(authentication.getName()).thenReturn(email);
        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        String result = appController.useraccount(model, authentication);

        assertEquals("account_details", result);*/
    }

    @Test
    void returnlogin() {
    }

    @Test
    void login_success() {
    }

    @Test
    void refreshURL() {
    }

    @Test
    void login_redirect() {
    }

    @Test
    void verify_login() {
    }

    @Test
    void verify_token() {
        AppController controller = new AppController();


        String result = controller.verify_token();

        assertEquals("verify_token", result);
    }
}