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
    public String payment(Model model, Authentication authentication){
        String email = authentication.getName();
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "payment";
    }

    @GetMapping("/open_account")
    public String open_account(Model model, Authentication authentication){
        String email = authentication.getName();
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "open_account";
    }

    @GetMapping("/delete_account")
    public String delete_account(Model model, Authentication authentication){
        String email = authentication.getName();
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "delete_account";
    }

    @PostMapping("/process_payment")
    public String process_payment(@RequestParam("currency") String currency, @RequestParam(value = "amount", defaultValue = "0") double amount, Model model, Authentication authentication) throws IOException {
        String email = authentication.getName();

        AppService.payment_From_Account(email, currency, amount);

        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);

        return "account_details";
    }

    @PostMapping("/process_deposit")
    public String process_deposit(@RequestParam("currency") String currency, @RequestParam(value = "amount", defaultValue = "0") double amount, Model model, Authentication authentication) throws IOException {
        String email = authentication.getName();

        AppService.deposit_To_Account(email, currency, amount);

        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);

        return "account_details";
    }

    @PostMapping("/process_new_account")
    public String process_new_account(@RequestParam("currency") String currency, Model model, Authentication authentication) throws IOException{
        String email = authentication.getName();

        AppService.create_New_Account(email, currency);

        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);

        return "account_details";
    }

    @PostMapping("/delete_old_account")
    public String delete_old_account(@RequestParam("currency") String currency, Model model, Authentication authentication) throws IOException{
        String email = authentication.getName();

        AppService.delete_Old_Account(email, currency);

        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);

        return "account_details";
    }
}
