package cz.tul.Chmelar.Controllers;

import cz.tul.Chmelar.Models.User;
import cz.tul.Chmelar.Models.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    /**
     * return index page
     *
     * @return index page
     */
    @GetMapping("")
    public String index(){
        return "index";
    }

    /**
     * return login_index page
     *
     * @return login_index page
     */
    @GetMapping("/login_index")
    public String login_index(){

        return "login_index";
    }

    /**
     * return account_details page
     *
     * @param model - holder for model attributes
     * @param authentication - authentication of user
     * @return account_details page
     */
    @GetMapping("/account_details")
    public String useraccount(Model model, Authentication authentication){
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
    public String returnlogin(){
        return "login";
    }

    /**
     * return login_success page
     *
     * @return login_success page
     */
    @GetMapping("/login_success")
    public String login_success(){
        return "login_success";
    }
}
