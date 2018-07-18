package org.communis.javawebintro.controller.rest;

import org.communis.javawebintro.dto.UserPasswordWrapper;
import org.communis.javawebintro.dto.UserWrapper;
import org.communis.javawebintro.exception.InvalidDataException;
import org.communis.javawebintro.exception.ServerException;
import org.communis.javawebintro.exception.error.ErrorCodeConstants;
import org.communis.javawebintro.exception.error.ErrorInformationBuilder;
import org.communis.javawebintro.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PersonalRestController {

    private final PersonalService personalService;

    @Autowired
    public PersonalRestController(PersonalService personalService) {
        this.personalService = personalService;
    }

    @RequestMapping(value = "/my", method = RequestMethod.PATCH)
    public void editPersonal(@Valid UserWrapper userWrapper, BindingResult bindingResult)
            throws InvalidDataException, ServerException {
        if (bindingResult.hasErrors()) {
            throw new InvalidDataException(ErrorInformationBuilder.build(ErrorCodeConstants.DATA_VALIDATE_ERROR));
        }
        personalService.edit(userWrapper);
    }

    @RequestMapping(value = "/my/password", method = RequestMethod.PATCH)
    public void changePassword(@Valid UserPasswordWrapper passwordWrapper, BindingResult bindingResult)
            throws ServerException, InvalidDataException {
        if (bindingResult.hasErrors()) {
            throw new InvalidDataException(ErrorInformationBuilder.build(ErrorCodeConstants.DATA_VALIDATE_ERROR));
        }
        personalService.changePassword(passwordWrapper);
    }
}
