package cz.tul.Chmelar.Controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling error page
 */
@Controller
public class ErrorController  implements org.springframework.boot.web.servlet.error.ErrorController {

    /**
     * redirect to my custom error_page
     *
     * @return error_page
     */
    @RequestMapping(value = "/error_page")
    public String error() {
        return "error_page";
    }
}
