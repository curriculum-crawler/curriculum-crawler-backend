package edu.harvard.cscis71.curriculum;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Home redirection to OpenAPI api documentation
 */
@Controller
public class HomeController {

    public static final String REDIRECT_STRING = "swagger-ui.html";

    @RequestMapping("/")
    public String index() {
        return "redirect:" + REDIRECT_STRING;
    }


}
