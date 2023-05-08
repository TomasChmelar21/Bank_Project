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

/**
 * Controller which returning and proccess pages with user actions (deposit, payment, create/delete account)
 */
@Controller
public class UserController {

    /**
     * return page where user can deposit money
     *
     * @param model - holder for model attributes
     * @param authentication - authentication of user
     * @return page where user can deposit money
     */
    @GetMapping("/deposit")
    public String deposit(Model model, Authentication authentication){
        String email = authentication.getName();
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "deposit";
    }

    /**
     * return page where user can do payment
     *
     * @param model - holder for model attributes
     * @param authentication - authentication of user
     * @return page where user can do payment
     */
    @GetMapping("/payment")
    public String payment(Model model, Authentication authentication){
        String email = authentication.getName();
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "payment";
    }

    /**
     * return page where user can open new account
     *
     * @param model - holder for model attributes
     * @param authentication - authentication of user
     * @return page where user can open new account
     */
    @GetMapping("/open_account")
    public String open_account(Model model, Authentication authentication){
        String email = authentication.getName();
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "open_account";
    }

    /**
     * return page where user can delete account
     *
     * @param model - holder for model attributes
     * @param authentication - authentication of user
     * @return page where user can delete account
     */
    @GetMapping("/delete_account")
    public String delete_account(Model model, Authentication authentication){
        String email = authentication.getName();
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "delete_account";
    }



    /**
     * process payment from payment page
     *
     * @param currency - currency in which user want to do payment
     * @param amount - how much user want to send
     * @param model - holder for model attributes
     * @param authentication - authentication of user
     * @return account details page with processed payment
     * @throws IOException
     */
    @PostMapping("/process_payment")
    public String process_payment(@RequestParam("currency") String currency, @RequestParam(value = "amount", defaultValue = "0") double amount, Model model, Authentication authentication) {
        String email = authentication.getName();
        Boolean wasSuccess;
        String message = "";

        try{
            if(AppService.paymentFromAccount(email, currency, amount)){
                wasSuccess = true;
                message = "Transakce v měně " + currency + " proběhla úspěšně";
            }
            else{
                wasSuccess = false;
                message = "Transakce v měně " + currency + " neproběhla, protože nejde ze žádného účtu zaplatit";
            }
        }
        catch(IOException e){
            wasSuccess = false;
            message = "Vyskytla se chyba";
        }



        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("show", true);
        model.addAttribute("success", wasSuccess);
        model.addAttribute("message", message);

        return "account_details";
    }

    /**
     * process deposit from deposit page
     *
     * @param currency - currency in which user want to do deposit
     * @param amount - how much user want to get to account
     * @param model - holder for model attributes
     * @param authentication - authentication of user
     * @return account details page with processed deposit
     * @throws IOException
     */
    @PostMapping("/process_deposit")
    public String process_deposit(@RequestParam("currency") String currency, @RequestParam(value = "amount", defaultValue = "0") double amount, Model model, Authentication authentication) {
        String email = authentication.getName();
        Boolean wasSuccess;
        String message = "";


        try{
            if(AppService.userHasAccountOfCurrency(email, currency)) {
                if(AppService.depositToAccount(email, currency, amount)){
                    wasSuccess = true;
                    message = "Transakce na účet " + currency + " proběhla úspěšně";
                }
                else{
                    wasSuccess = false;
                    message = "Transakce na účet " + currency + " se nezdařila";
                }

            }
            else{
                wasSuccess = false;
                message = "Transakce na účet " + currency + " nemůže být provedena, protože účet neexistuje";
            }
        }
        catch (IOException e){
            wasSuccess = false;
            message = "Vyskytla se chyba";
        }


        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("show", true);
        model.addAttribute("success", wasSuccess);
        model.addAttribute("message", message);

        return "account_details";
    }
    
    /**
     * process new account from new account page
     *
     * @param currency - currency in which user want to do create account
     * @param model - holder for model attributes
     * @param authentication - authentication of user
     * @return account details page with processed new account
     * @throws IOException
     */
    @PostMapping("/process_new_account")
    public String process_new_account(@RequestParam("currency") String currency, Model model, Authentication authentication) throws IOException{
        String email = authentication.getName();
        Boolean wasSuccess;
        String message = "";

        try{
            if(AppService.userHasAccountOfCurrency(email,currency)){
                wasSuccess = false;
                message = "Účet s měnou " + currency + " už existuje";
            }
            else{
                if(AppService.createNewAccount(email, currency)){
                    wasSuccess = true;
                    message = "Účet s měnou " + currency + " je úspěšně založený";
                }
                else {
                    wasSuccess = false;
                    message = "Účet s měnou " + currency + " se nepodařilo vytvořit";
                }
            }

        }
        catch(IOException e){
            wasSuccess = false;
            message = "Vyskytla se chyba";
        }


        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("show", true);
        model.addAttribute("success", wasSuccess);
        model.addAttribute("message", message);

        return "account_details";
    }

    /**
     * process delete account from delete account page
     *
     * @param currency - currency in which user want to do delete account
     * @param model - holder for model attributes
     * @param authentication - authentication of user
     * @return account details page with processed delete account
     * @throws IOException
     */
    @PostMapping("/delete_old_account")
    public String delete_old_account(@RequestParam("currency") String currency, Model model, Authentication authentication) throws IOException{
        String email = authentication.getName();
        Boolean wasSuccess;
        String message = "";

        try{
            if(AppService.userHasAccountOfCurrency(email, currency)){
                if(AppService.deleteOldAccount(email, currency)){
                    wasSuccess = true;
                    message = "Účet s měnou " + currency + " byl úspěšně smazán";
                }
                else{
                    wasSuccess = false;
                    message = "Účet s měnou " + currency + " se nepodařilo smazat";
                }
            }
            else {
                wasSuccess = false;
                message = "Účet s měnou " + currency + " neexistuje";
            }
        }
        catch(IOException e) {
            wasSuccess = false;
            message = "Vyskytla se chyba";
        }


        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("show", true);
        model.addAttribute("success", wasSuccess);
        model.addAttribute("message", message);

        return "account_details";
    }
}
