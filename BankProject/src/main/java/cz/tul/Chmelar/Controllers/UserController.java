package cz.tul.Chmelar.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @GetMapping("/deposit")
    public String viewDeposit(){
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
    public String process_deposit(){
        return "account_details";
    }

    @PostMapping("/process_new_account")
    public String process_new_account(){
        return "account_details";
    }
}
