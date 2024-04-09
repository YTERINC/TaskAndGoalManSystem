package ru.yterinc.TaskAndGoalManSystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yterinc.TaskAndGoalManSystem.models.Task;
import ru.yterinc.TaskAndGoalManSystem.services.TaskService;


@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("tasks", taskService.findAll());

        return "tasks/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("task", taskService.findOne(id));
        return "tasks/show";
    }

    @GetMapping("/new")
    public String newTask(Model model) {
        Task task = new Task();
        model.addAttribute("task", task);
//        System.out.println("из GET /new --- id = " + id);
        return "tasks/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("task") Task task,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "tasks/new";
//        System.out.println("из POST --- id = " + id);
//        model.addAttribute("task", task);
        taskService.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("task", taskService.findOne(id));
        return "tasks/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("task") Task task,
                         BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "tasks/edit";

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
        model.addAttribute("task", taskService.findOne(id));
        return "tasks/report";
    }

    @PatchMapping("/{id}/report")
    public String report(@ModelAttribute("description") String description,
                         BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "redirect:/tasks";
        taskService.createReport(id, description);
        return "redirect:/tasks";
    }



}
