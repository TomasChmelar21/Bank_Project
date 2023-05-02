package cz.tul.Chmelar.Controllers;

import cz.tul.Chmelar.Models.User;
import cz.tul.Chmelar.Models.UserRepository;
import cz.tul.Chmelar.Services.AppService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class UserController {

    @GetMapping("/deposit")
    public String deposit(Model model, Authentication authentication){
        String email = authentication.getName();
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "deposit";
    }

    @GetMapping("/payment")
    public String viewPayment(){
        return "payment";
    }

    @GetMapping("/open_account")
    public String viewOpen_account(){
        return "open_account";
    }

    @PostMapping("/process_payment")
    public String process_payment(){
        return "account_details";
    }

    @PostMapping("/process_deposit")
    public String process_deposit(@RequestParam("currency") String currency, @RequestParam(value = "amount", defaultValue = "0") double amount, Model model, Authentication authentication) throws IOException {
        String email = authentication.getName();

        AppService.depositToAccount(email, currency, amount);

        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);

        return "account_details";
    }

    @PostMapping("/process_new_account")
    public String process_new_account(){
        return "account_details";
    }
}
