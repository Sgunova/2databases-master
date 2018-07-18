package org.communis.javawebintro.controller.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для публичного API
 *
 */
@RestController
@RequestMapping(value = "public")
public class PublicRestController {

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String getCaInfo() {
        return "API simple info";
    }

}
