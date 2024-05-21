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
    public String index(Model model) { // показываем авторизованного пользователя и всех его подчиненных, т.е у которых он шеф
        model.addAttribute("people", peopleService.findAllByChief());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) { // показываем страницу авторизованного пользователя или одного из его подчиненных и список задач
        Person person = peopleService.findOneByChief(id);
        if (person != null) {
            model.addAttribute("person", person);
            model.addAttribute("chief", peopleService.findOne(person.getChiefId()));
            model.addAttribute("tasks", peopleService.getTaskByPersonId(id)); // добавляем в модель задачи пользователя
            taskService.checkExpired(peopleService.getTaskByPersonId(id));  // проверка просроченных задач
            return "people/show";
        } else return "forbidden";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {  // страница нового пользователя. Добавляем пустого человека в модель
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);  // проверка создаваемого пользователя
        if (bindingResult.hasErrors())
            return "people/new";

        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        Person person = peopleService.findOneByChief(id);
        if (person != null) {
            model.addAttribute("person", person);  // Добавляем в модель пользователя для его редактирования
            model.addAttribute("people", peopleService.findAll());  // добавляем список всех пользователей для назначения шефа
            return "people/edit";
        } else return "forbidden";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult, @PathVariable("id") int id) {
        personValidator.validate(person, bindingResult);  // проверка редактируемого пользователя
        if (bindingResult.hasErrors())
            return "people/edit";

        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {  // удаление выбранного пользователя
        peopleService.delete(id);
        return "redirect:/people";
    }

    @GetMapping("/{id}/change-pass")
    public String changePasswordPage(Model model,
                                     @PathVariable("id") int userId) {  // изменение пароля
        Person person = peopleService.findOneByChief(userId);
        if (person != null) {
            model.addAttribute("id", userId); // передаем в модель ID пользователя для которого меняем пароль
            return "people/change-pass";
        } else return "forbidden";
    }

    @PostMapping("/{id}/change-pass")
    public String changePasswordPage(@ModelAttribute("new-pass") @Valid String newPass,
                                     @PathVariable("id") int id) {
        peopleService.updatePassword(id, newPass);
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