package ru.yterinc.TaskAndGoalManSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yterinc.TaskAndGoalManSystem.models.Task;
import ru.yterinc.TaskAndGoalManSystem.repositories.PeopleRepository;
import ru.yterinc.TaskAndGoalManSystem.repositories.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;
    private final PeopleRepository peopleRepository;
    private final PeopleService peopleService;

    @Autowired
    public TaskService(TaskRepository taskRepository, PeopleRepository peopleRepository, PeopleService peopleService) {
        this.taskRepository = taskRepository;
        this.peopleRepository = peopleRepository;
        this.peopleService = peopleService;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> findAllByChief() { // показываем только задачи пользователей у авторизированного пользователя и у тех у кого он шеф
        if (peopleRepository.findById(getIdAuthUser()).get().getRole().equals("ROLE_ADMIN"))
            return taskRepository.findAll();
        else return taskRepository.findAllByOwner_Chief(peopleService.findOne(getIdAuthUser()).getFullName());
    }

    public Task findOne(int id) {
        Optional<Task> foundTask = taskRepository.findById(id);
        return foundTask.orElse(null);
    }


    public Task findOneByChief(int id) {
        Optional<Task> foundTask = taskRepository.findById(id);
        if (foundTask.isPresent() && ((foundTask.get().getOwner().getId() == getIdAuthUser())
                || foundTask.get().getOwner().getChief().equals(peopleService.findOne(getIdAuthUser()).getFullName()))
                || peopleRepository.findById(getIdAuthUser()).get().getRole().equals("ROLE_ADMIN")) {
            return foundTask.orElse(null);
        } else return null;
    }

    @Transactional
    public void save(Task task, int id) {
        task.setCreatedAt(LocalDateTime.now());  // устанавливаем дату и время создания задачи
        task.setStatus(true);  // При создании задачи, она будут активна
        task.setOwner(peopleService.findOne(id));  // назначаем пользователя для задачи
        taskRepository.save(task);
    }

    @Transactional
    public void update(int id, Task updatedTask) {
        System.out.println("Service!!!!!!!!!!!!!!!!!!!");
        updatedTask.setId(id);
        updatedTask.setStatus(true);
        updatedTask.setCreatedAt(findOne(id).getCreatedAt());
        updatedTask.setOwner(findOne(id).getOwner());
        taskRepository.save(updatedTask);
    }

    @Transactional
    public void delete(int id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    public void createReport(int id, String description) {
        Task task = findOne(id);
        task.setStatus(false);
        task.setExecutionAt(LocalDateTime.now());
        task.setDescription(description);
    }

    private int getIdAuthUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return peopleRepository.findByFullName(username).get().getId();
    }

}
