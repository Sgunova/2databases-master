package org.communis.javawebintro.controller.view;

import org.communis.javawebintro.first.entity.FirstDataSourceWiredEntity;
import org.communis.javawebintro.first.service.FirstDatasourceWiredEntityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for wired entities {@link FirstDataSourceWiredEntity}
 */
@Controller
public class FirstDatasourceWiredController {

    public static final String FIRST_WIRED = "/first-wired";
    public static final String FIRST_WIRED_ID = FIRST_WIRED + "/{id}";
    public static final String FIRST_WIRED_NEW = FIRST_WIRED + "/new";
    public static final String FIRST_WIRED_UPDATE = FIRST_WIRED + "/update";
    public static final String FIRST_WIRED_DELETE_ID = FIRST_WIRED + "/delete/{id}";
    public static final String FIRST_WIRED_FIND = FIRST_WIRED + "/find";
    /**
     * {@link FirstDatasourceWiredEntityService}
     */
    private final FirstDatasourceWiredEntityService service;

    public FirstDatasourceWiredController(
            FirstDatasourceWiredEntityService service) {
        this.service = service;
    }

    /**
     * Returns view containing all of the saved entities
     */
    @GetMapping(
            FIRST_WIRED
    )
    public String dataList(Model model) {
        model.addAttribute("entities", service.findAll());
        model.addAttribute("entitytype", "first-wired");
        return "entities";
    }

    /**
     * Returns view of entity with specified id, avaliable to update
     */
    @GetMapping(
            FIRST_WIRED_ID
    )
    public String data(Model model, @PathVariable("id") Long id) {
        model.addAttribute("entity", service.findById(id));
        model.addAttribute("entitytype", "first-wired");
        model.addAttribute("wired", service.findAllAvaliableBindings());
        model.addAttribute("action", "/first-wired/update");
        return "wired-entity";
    }

    /**
     * Save new entity
     */
    @PostMapping(
            FIRST_WIRED_NEW
    )
    public String save(@ModelAttribute("entity") FirstDataSourceWiredEntity entity, Model model) {
        service.save(entity);
        return dataList(model);
    }

    /**
     * Save new entity
     */
    @PostMapping(
            FIRST_WIRED_UPDATE
    )
    public String update(@ModelAttribute("entity") FirstDataSourceWiredEntity entity, Model model) {
        service.update(entity);
        return dataList(model);
    }

    /**
     * Deletes an entity with specified id
     */
    @GetMapping(
            FIRST_WIRED_DELETE_ID
    )
    public String delete(@PathVariable("id") Long id, Model model) {
        service.deleteById(id);
        return dataList(model);
    }

    /**
     * Returns list of filtered entities by name
     */
    @GetMapping(
            FIRST_WIRED_NEW
    )
    public String form(Model model) {
        model.addAttribute("entity", new FirstDataSourceWiredEntity());
        model.addAttribute("entitytype", "first-wired");
        model.addAttribute("wired", service.findAllAvaliableBindings());
        model.addAttribute("action", "/first-wired/new");
        return "wired-entity";
    }

    @GetMapping(
            FIRST_WIRED_FIND
    )
    public String find(@RequestParam("query") String query, Model model) {
        model.addAttribute("entities", service.filterByName(query));
        model.addAttribute("entitytype", "first-wired");
        return "entities";
    }
}
