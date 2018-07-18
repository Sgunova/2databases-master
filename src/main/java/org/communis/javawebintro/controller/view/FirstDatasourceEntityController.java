package org.communis.javawebintro.controller.view;

import org.communis.javawebintro.first.entity.FirstDataSourceEntity;
import org.communis.javawebintro.first.service.FirstDatasourceEntityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller to work with {@link FirstDataSourceEntity}
 */
@Controller
public class FirstDatasourceEntityController {

    /**
     * {@link FirstDatasourceEntityService}
     */
    private final FirstDatasourceEntityService service;

    public FirstDatasourceEntityController(FirstDatasourceEntityService service) {
        this.service = service;
    }

    /**
     * Returns all saved {@link FirstDataSourceEntity}
     */
    @GetMapping(
            "/first"
    )
    public String dataList(Model model) {
        model.addAttribute("entities", service.findAll());
        model.addAttribute("entitytype", "first");
        return "entities";
    }

    /**
     * Returns view of {@link FirstDataSourceEntity} with specified id
     */
    @GetMapping(
            "/first/{id}"
    )
    public String data(Model model, @PathVariable("id") Long id) {
        model.addAttribute("entity", service.findById(id));
        model.addAttribute("entitytype", "first");
        model.addAttribute("wired", service.findAllAvaliableBindings());
        model.addAttribute("method", "POST");
        model.addAttribute("action", "/first/update");
        return "entity";
    }

    /**
     * Saves new {@link FirstDataSourceEntity}
     */
    @PostMapping(
            value = "/first/new")
    public String save(@ModelAttribute("entity") FirstDataSourceEntity entity, Model model) {
        service.save(entity);
        return "redirect:/first";
    }

    /**
     * Updates existing {@link FirstDataSourceEntity}
     */
    @PostMapping(
            value = "/first/update"
    )
    public String update(@ModelAttribute("entity") FirstDataSourceEntity entity, Model model) {
        service.update(entity);
        return "redirect:/first";
    }

    /**
     * Deletes {@link FirstDataSourceEntity} with specified id
     */
    @GetMapping(
            "/first/delete/{id}"
    )
    public String delete(@PathVariable("id") Long id, Model model) {
        service.deleteById(id);
        return dataList(model);
    }

    /**
     * Returns form to save new {@link FirstDataSourceEntity}
     */
    @GetMapping(
            "/first/new"
    )
    public String form(Model model) {
        model.addAttribute("entity", new FirstDataSourceEntity());
        model.addAttribute("wired", service.findAllAvaliableBindings());
        model.addAttribute("entitytype", "first");
        model.addAttribute("action", "/first/new");
        return "entity";
    }

    /**
     * Returns list of {@link FirstDataSourceEntity} filtered by name
     * @param query
     * @param model
     * @return
     */
    @GetMapping(
            "/first/find"
    )
    public String find(@RequestParam("query") String query, Model model) {
        model.addAttribute("entities", service.filterByName(query));
        model.addAttribute("entitytype", "first");
        return "entities";
    }
}
