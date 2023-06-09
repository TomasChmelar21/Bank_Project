package cz.tul.Chmelar.services;

import cz.tul.Chmelar.models.CustomUser;
import cz.tul.Chmelar.models.User;
import cz.tul.Chmelar.models.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserServiceTest {

    @Mock
    private UserRepository userRepository;

    private CustomUserService customUserService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        customUserService = new CustomUserService();
    }

    @Test
    public void testLoadUserByUsername_UserFound_ReturnsCustomUser() {
        String email = "misty211@seznam.cz";
        User user = new User(email, "password");

        CustomUser customUser = (CustomUser) customUserService.loadUserByUsername(email);

        assertEquals(user.getEmail(), customUser.getUsername());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound_ThrowsException() {
        String email = "nonexistent@example.com";


        // Act & Assert
        UsernameNotFoundException exception =
                assertThrows(
                        UsernameNotFoundException.class,
                        () -> customUserService.loadUserByUsername(email)
                );

        assertEquals("Uživatel nenalezen", exception.getMessage());
    }

    @Test
    public void testGenerateTwoFactorCode_ReturnsSixDigitCode() {
        String code = CustomUserService.generateTwoFactorCode();

        assertEquals(6, code.length());
        assertTrue(code.matches("\\d+"));
    }
}