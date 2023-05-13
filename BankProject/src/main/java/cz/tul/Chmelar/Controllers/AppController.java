package cz.tul.Chmelar.Controllers;

import cz.tul.Chmelar.Models.User;
import cz.tul.Chmelar.Models.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static cz.tul.Chmelar.Services.AppService.validateTwoFactorCode;
import static cz.tul.Chmelar.Services.AppService.writeTokenToJson;
import static cz.tul.Chmelar.Services.CustomUserService.generateTwoFactorCode;

/**
 * Controller which returning pages based on url
 */
@Controller
public class AppController {

    @Autowired
    private JavaMailSender emailSender;

    /**
     * return index page
     *
     * @return index page
     */
    @GetMapping("")
    public String index() {
        return "index";
    }

    /**
     * return login_index page
     *
     * @return login_index page
     */
    @GetMapping("/login_index")
    public String login_index() {

        return "login_index";
    }

    /**
     * return account_details page
     *
     * @param model          - holder for model attributes
     * @param authentication - authentication of user
     * @return account_details page
     */
    @GetMapping("/account_details")
    public String useraccount(Model model, Authentication authentication) {
        String email = authentication.getName();
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "account_details";
    }

    /**
     * return login page
     *
     * @return login page
     */
    @GetMapping("/login")
    public String returnlogin() {
        return "login";
    }

    /**
     * return login_success page
     *
     * @return login_success page
     */
    @GetMapping("/login_success")
    public String login_success() {
        return "login_success";
    }

    /**
     * redirect to account_details page if user want again process same action
     *
     * @param model          - holder for model attributes
     * @param authentication - authentication of user
     * @return account_details page
     */
    @RequestMapping(value = {"/process_payment", "/process_deposit", "/process_new_account", "/delete_old_account"})
    public String refreshURL(Model model, Authentication authentication) {
        return "redirect:/account_details";

    }

    /**
     * redirect user from login page to token page
     *
     * @param model - holder for model attributes
     * @param email - users email
     * @param password - users password
     * @return if login information are wrong it return login page if its correct it return verify page
     * @throws MessagingException
     */
    @PostMapping("/login_redirect")
    public String login_redirect(Model model, HttpServletResponse response, @RequestParam String email, @RequestParam String password) throws MessagingException{
        User user = UserRepository.findByEmail(email);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        model.addAttribute("email", email);
        model.addAttribute("password", password);
        if(user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return "redirect:/login?error=true";
        }
        Cookie emailCookie = new Cookie("email", email);
        emailCookie.setMaxAge(604800);
        response.addCookie(emailCookie);

        Cookie passwordCookie = new Cookie("password", password);
        passwordCookie.setMaxAge(604800);
        response.addCookie(passwordCookie);

        model.addAttribute("user", user);
        try {
            String newToken = generateTwoFactorCode();
            writeTokenToJson(user.getEmail(), newToken);
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper message_Builder = new MimeMessageHelper(message);
            message_Builder.setFrom("tom.chmelar2002@gmail.com", "Moje Banka");
            message_Builder.setTo(user.getEmail());
            message_Builder.setSubject("Banka - ověřovací kód");
            message_Builder.setText("Váš ověřovací kód je: " + newToken, true);
            emailSender.send(message);
            return "verify_token";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * redirect user to login in form
     *
     * @param model - holder for model attributes
     * @param token - users proving token
     * @return form for automatic sign in
     */
    @PostMapping("/verify_login")
    public String verify_login(Model model, @RequestParam String token, HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        String email = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("email")) {
                    email = cookie.getValue();
                    break;
                }
            }
        }

        User user = UserRepository.findByEmail(email);
        if(user == null) {
            return "redirect:/login?error=true";
        }
        if(!user.getToken().equals(token)) {
            return "redirect:/login?error=true";
        }

        return "login_index";
    }

    /*@GetMapping("/verify_token")
    public String verify_token(User user, RedirectAttributes redirectAttributes) throws MessagingException {
        try {
            writeTokenToJson(user.getEmail(), generateTwoFactorCode());
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper message_Builder = new MimeMessageHelper(message);
            message_Builder.setFrom("tom.chmelar2002@gmail.com", "Moje Banka");
            message_Builder.setTo(user.getEmail());
            message_Builder.setSubject("Banka - ověřovací kód");
            message_Builder.setText("Váš ověřovací kód je: " + user.getToken(), true);
            emailSender.send(message);
            redirectAttributes.addAttribute("email", user.getEmail());
            return "redirect:/verify_token";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }*/

    /*@GetMapping("/verify_token")
    public String verify_token(Model model, Authentication authentication, RedirectAttributes redirectAttributes) throws MessagingException {
        String email = authentication.getName();
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
        try {
            writeTokenToJson(user.getEmail(), generateTwoFactorCode());
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper message_Builder = new MimeMessageHelper(message);
            message_Builder.setFrom("tom.chmelar2002@gmail.com", "Moje Banka");
            message_Builder.setTo(user.getEmail());
            message_Builder.setSubject("Banka - ověřovací kód");
            message_Builder.setText("Váš ověřovací kód je: " + user.getToken(), true);
            emailSender.send(message);
            redirectAttributes.addAttribute("email", user.getEmail());
            return "redirect:/verify_token";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    /* @GetMapping("/verify_token")
    public String verify_token(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "account_details";
    } */



}
