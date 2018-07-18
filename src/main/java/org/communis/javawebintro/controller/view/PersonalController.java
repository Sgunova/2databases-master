package org.communis.javawebintro.controller.view;

import org.communis.javawebintro.dto.UserWrapper;
import org.communis.javawebintro.enums.UserRole;
import org.communis.javawebintro.enums.UserStatus;
import org.communis.javawebintro.exception.ServerException;
import org.communis.javawebintro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PersonalController {

    private final UserService userService;

    @Autowired
    public PersonalController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/my", method = RequestMethod.GET)
    public ModelAndView getPersonalPage() throws ServerException {
        ModelAndView modelAndView = new ModelAndView("user/my");
        modelAndView.addObject("user", new UserWrapper(userService.getCurrentUser()));
        modelAndView.addObject("statuses", UserStatus.values());
        modelAndView.addObject("roles", UserRole.values());
        return modelAndView;
    }
}
