package edu.harvard.cscis71.curriculum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Home redirection to OpenAPI api documentation
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    public static final String REDIRECT_STRING = "swagger-ui.html";

    @RequestMapping("/")
    public String index() {
        logger.info("Received request for index, redirecting...");
        return "redirect:" + REDIRECT_STRING;
    }


}
