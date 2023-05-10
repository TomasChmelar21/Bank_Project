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



}
