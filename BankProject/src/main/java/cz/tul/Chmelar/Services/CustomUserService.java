package cz.tul.Chmelar.Services;

import cz.tul.Chmelar.Models.CustomUser;
import cz.tul.Chmelar.Models.User;
import cz.tul.Chmelar.Models.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = null;
        user = UserRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Uživatel nenalezen");
        }
        return new CustomUser(user);
    }
}
