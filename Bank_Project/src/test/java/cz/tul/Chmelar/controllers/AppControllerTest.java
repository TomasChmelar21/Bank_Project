package cz.tul.Chmelar.controllers;

import cz.tul.Chmelar.models.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AppControllerTest {
    @Mock
    private UserRepository userRepository;


    private AppController appController = new AppController();


    private Model model;
    private Authentication authentication;

    @BeforeEach
    void setUp(){
        model = new Model() {
            private Map<String, Object> attributes = new HashMap<>();

            @Override
            public Model addAttribute(String attributeName, Object attributeValue) {
                attributes.put(attributeName, attributeValue);
                return this;
            }

            @Override
            public Model addAttribute(Object attributeValue) {
                return null;
            }

            @Override
            public Model addAllAttributes(Collection<?> attributeValues) {
                return null;
            }

            @Override
            public Model addAllAttributes(Map<String, ?> attributes) {
                return null;
            }

            @Override
            public Model mergeAttributes(Map<String, ?> attributes) {
                return null;
            }

            @Override
            public boolean containsAttribute(String attributeName) {
                return attributes.containsKey(attributeName);
            }

            @Override
            public Object getAttribute(String attributeName) {
                return attributes.get(attributeName);
            }

            @Override
            public Map<String, Object> asMap() {
                return attributes;
            }
        };

        authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return "tom.chmelar@seznam.cz";
            }
        };
    }


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
        String result = appController.useraccount(model,authentication);
        assertEquals("account_details", result);
    }

    @Test
    void returnlogin() {

        String result = appController.returnlogin();

        assertEquals("login", result);
    }

    @Test
    void login_success() {
        String result = appController.login_success();

        assertEquals("login_success", result);
    }

    @Test
    void refreshURL() {
        String result = appController.refreshURL(model, authentication);
        assertEquals("redirect:/account_details", result);
    }

    @Test
    void login_redirect() {

    }

    @Test
    void verify_login() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("email", "tom.chmelar@seznam.cz");

        Mockito.when(request.getCookies()).thenReturn(cookies);
        String result = appController.verify_login(model, "123456", request);
        assertEquals("login_index", result);
    }

    @Test
    void verify_token() {
        AppController controller = new AppController();


        String result = controller.verify_token();

        assertEquals("verify_token", result);
    }
}