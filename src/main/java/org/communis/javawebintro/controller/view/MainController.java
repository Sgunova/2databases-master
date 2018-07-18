package org.communis.javawebintro.controller.view;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @RequestMapping(value = {"/badbrowser"}, method = RequestMethod.GET)
    public String badBrowser() {
        return "errors/badBrowser";
    }

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String getPublicPage(Authentication authentication) {
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

}
