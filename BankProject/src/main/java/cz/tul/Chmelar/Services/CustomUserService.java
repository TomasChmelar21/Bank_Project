package cz.tul.Chmelar.Services;

import cz.tul.Chmelar.Models.CustomUser;
import cz.tul.Chmelar.Models.User;
import cz.tul.Chmelar.Models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserService implements UserDetailsService {
    //@Autowired
    //private UserRepository repo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = UserRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Uživatel nenalezen");
        }
        return new CustomUser(user);
    }


}
