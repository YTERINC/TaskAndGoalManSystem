package ru.yterinc.TaskAndGoalManSystem.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yterinc.TaskAndGoalManSystem.models.Task;
import ru.yterinc.TaskAndGoalManSystem.services.PeopleService;
import ru.yterinc.TaskAndGoalManSystem.services.TaskService;
import ru.yterinc.TaskAndGoalManSystem.util.TaskValidator;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final PeopleService peopleService;
    private final TaskValidator taskValidator;

    @Autowired
    public TaskController(TaskService taskService, PeopleService peopleService, TaskValidator taskValidator) {
        this.taskService = taskService;
        this.peopleService = peopleService;
        this.taskValidator = taskValidator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("tasks", taskService.findAllByChief());
        taskService.checkExpired(taskService.findAllByChief());
        return "tasks/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Task task = taskService.findOneByChief(id);
        if (task != null) {
            model.addAttribute("task", task);
            return "tasks/show";
        } else return "forbidden";
    }

    @GetMapping("/new/{userid}")
    public String newTask(Model model, @PathVariable("userid") int userId) {  // прокидываем ID пользователя
        Task task = new Task();
        model.addAttribute("task", task);
        model.addAttribute("userid", userId);
        return "tasks/new";
    }

    @PostMapping("/new/{userid}")
    public String create(@ModelAttribute("task") @Valid Task task,
                         BindingResult bindingResult,
                         @PathVariable("userid") int userId) {    // прокидываем ID пользователя
        taskValidator.validate(task, bindingResult);
        if (bindingResult.hasErrors())
            return "tasks/new";
        taskService.save(task, userId);
        return "redirect:/people/" + userId;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        Task task = taskService.findOneByChief(id);
        if (task != null) {
            model.addAttribute("task", task);
            model.addAttribute("people", peopleService.findAll());
            return "tasks/edit";
        } else return "forbidden";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("task") @Valid Task task,
                         BindingResult bindingResult, @PathVariable("id") int id) {
        taskValidator.validate(task, bindingResult);
        if (bindingResult.hasErrors()) {
            return "tasks/edit";
        }
        taskService.update(id, task);
        return "redirect:/tasks";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        taskService.delete(id);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/report")
    public String report(Model model,
                         @PathVariable("id") int id) {
        Task task = taskService.findOneByChief(id);
        if (task != null) {
            model.addAttribute("task", task);
            return "tasks/report";
        } else return "forbidden";
    }

    @PatchMapping("/{id}/report")
    public String report(@ModelAttribute("description") String description,
                         @PathVariable("id") int id) {
        taskService.createReport(id, description);
        return "redirect:/tasks";
    }
}
