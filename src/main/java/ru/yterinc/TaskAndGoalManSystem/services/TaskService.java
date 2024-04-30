package ru.yterinc.TaskAndGoalManSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yterinc.TaskAndGoalManSystem.models.Task;
import ru.yterinc.TaskAndGoalManSystem.repositories.PeopleRepository;
import ru.yterinc.TaskAndGoalManSystem.repositories.TaskRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;
    private final PeopleService peopleService;

    @Autowired
    public TaskService(TaskRepository taskRepository, PeopleService peopleService) {
        this.taskRepository = taskRepository;
        this.peopleService = peopleService;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findOne(int id) {
        Optional<Task> foundTask = taskRepository.findById(id);
        return foundTask.orElse(null);
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
        updatedTask.setId(id);
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

}
