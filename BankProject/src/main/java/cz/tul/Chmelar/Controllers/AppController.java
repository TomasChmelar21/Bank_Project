package cz.tul.Chmelar.Controllers;

import cz.tul.Chmelar.Models.User;
import cz.tul.Chmelar.Models.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
    

    @PostMapping("/login_redirect")
    public String login_redirect(Model model, @RequestParam String email, @RequestParam String password) throws MessagingException{
        User user = UserRepository.findByEmail(email);
        model.addAttribute("email", email);
        model.addAttribute("password", password);
        if(user == null) {
            return "redirect:/login?error=true";
        }
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
            model.addAttribute("email", email);
            model.addAttribute("password", password);
            return "verify_token";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/verify_token")
    public String verify_token(Model model) {
        String email = (String) model.getAttribute("email");
        String password = (String) model.getAttribute("password");
        if(email == null) {
            return "redirect:/login";
        }
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        return "verify_token";
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
