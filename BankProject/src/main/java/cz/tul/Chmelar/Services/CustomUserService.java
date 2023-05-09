package cz.tul.Chmelar.Services;

import cz.tul.Chmelar.Models.CustomUser;
import cz.tul.Chmelar.Models.User;
import cz.tul.Chmelar.Models.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static cz.tul.Chmelar.Services.ExchangeRateService.transferExchangeRateCount;

@Service
public class CustomUserService implements UserDetailsService {
    @Autowired
    private UserRepository repo;

    /**
     * find user by email and returning UserDetails
     *
     * @param email - email of user we want to find
     * @return CustomUser
     * @throws UsernameNotFoundException - Username(email) not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Uživatel nenalezen");
        }
        return new CustomUser(user);
    }

    /**
     * generate 6 place long token for user
     *
     * @return token
     */
    public static String generateTwoFactorCode() {
        String token = String.format("%06d", new Random().nextInt(999999));
        return token;
    }

}
