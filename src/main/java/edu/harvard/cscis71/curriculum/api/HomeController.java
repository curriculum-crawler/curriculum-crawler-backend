package edu.harvard.cscis71.curriculum.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    public static final String OK_RESPONSE = "OK";

    @RequestMapping("/")
    public @ResponseBody String root() {
        return OK_RESPONSE;
    }

}
