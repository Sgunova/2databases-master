package org.communis.javawebintro.controller.view;

import org.communis.javawebintro.second.entity.SecondDataSourceEntity;
import org.communis.javawebintro.second.repository.SecondDatabaseEntityRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class SecondDatasourceEntityController {
    private final SecondDatabaseEntityRepository repository;

    public SecondDatasourceEntityController(SecondDatabaseEntityRepository repository) {
        this.repository = repository;
    }

    @GetMapping(
            "/second"
    )
    public String dataList(Model model) {
        model.addAttribute("entities", repository.findAll());
        model.addAttribute("entitytype", "second");

        return "entities";
    }

    @GetMapping(
            "/second/{id}"
    )
    public String data(Model model, @PathVariable("id") Long id) {
        model.addAttribute("entity", repository.findOne(id));
        model.addAttribute("entitytype", "second");

        model.addAttribute("method", "POST");
        return "entity";
    }

    @PostMapping(
            value = "/second")
    public String save(@ModelAttribute("entity") SecondDataSourceEntity entity, Model model) {
        repository.save(entity);
        return dataList(model);
    }

    @GetMapping(
            "/second/delete/{id}"
    )
    public String delete(@PathVariable("id") Long id, Model model) {
        repository.delete(id);
        return dataList(model);
    }

    @GetMapping(
            "/second/new"
    )
    public String form(Model model) {
        model.addAttribute("entity", new SecondDataSourceEntity());
        model.addAttribute("entitytype", "second");

        model.addAttribute("method", "POST");
        return "entity";
    }

    @GetMapping(
            "/second/find"
    )
    public String find(@RequestParam("query") String query, Model model) {
        model.addAttribute("entities", repository.findAllByNameContainsOrderByName(query));
        model.addAttribute("entitytype", "second");
        return "entities";
    }
}
