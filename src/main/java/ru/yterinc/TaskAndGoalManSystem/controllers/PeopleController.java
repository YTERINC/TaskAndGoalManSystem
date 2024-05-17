package ru.yterinc.TaskAndGoalManSystem.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.yterinc.TaskAndGoalManSystem.models.Person;
import ru.yterinc.TaskAndGoalManSystem.services.PeopleService;
import ru.yterinc.TaskAndGoalManSystem.services.TaskService;
import ru.yterinc.TaskAndGoalManSystem.util.PersonValidator;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final PersonValidator personValidator;
    private final TaskService taskService;


    @Autowired
    public PeopleController(PeopleService peopleService, PersonValidator personValidator, TaskService taskService) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
        this.taskService = taskService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAllByChief());
        return "people/index";
    }


    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Person person = peopleService.findOneByChief(id);
        if (person != null) {
            model.addAttribute("person", person);
            model.addAttribute("tasks", peopleService.getTaskByPersonId(id));
            taskService.checkExpired(peopleService.getTaskByPersonId(id));
            return "people/show";
        } else return "forbidden";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {

        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/new";

        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        Person person = peopleService.findOneByChief(id);
        if (person != null) {
            model.addAttribute("person", person);
            model.addAttribute("people", peopleService.findAll());
            return "people/edit";
        } else return "forbidden";


    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult, @PathVariable("id") int id) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/edit";

        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) { // используется, чтобы можно было нулевую дату передавать
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        // true passed to CustomDateEditor constructor means convert empty String to null
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  // true - значит можно передать null
    }

}
