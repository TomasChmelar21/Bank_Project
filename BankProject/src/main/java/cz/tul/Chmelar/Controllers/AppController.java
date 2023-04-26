package cz.tul.Chmelar.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping("")
    public String index(){
        return "index";
    }

    @GetMapping("/login_index")
    public String login_index(){
        return "login_index";
    }

    @GetMapping("/account_details")
    public String useraccount(){
        return "account_details";
    }

    @GetMapping("/login")
    public String returnlogin(){
        return "login";
    }

    @GetMapping("/login_success")
    public String login_success(){
        return "login_success";
    }
}
