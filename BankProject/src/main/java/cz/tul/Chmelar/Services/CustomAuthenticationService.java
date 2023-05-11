package cz.tul.Chmelar.Services;

import cz.tul.Chmelar.Models.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class CustomAuthenticationService {

    private final UserDetailsService userDetailsService;

    public CustomAuthenticationService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void check(UserDetails user) {
        if (user instanceof User) {
            User appUser = (User) user;
            String providedToken = obtainProvidedToken();

            if (!appUser.getToken().equals(providedToken)) {
                throw new BadCredentialsException("Invalid token");
            }
        }
    }

    private String obtainProvidedToken() {
        // Implement the logic to retrieve the provided token from the request
        // For example, using a request parameter or header
        // Replace the return statement with your implementation
        return null;
    }
}

