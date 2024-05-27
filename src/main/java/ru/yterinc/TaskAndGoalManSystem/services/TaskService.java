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
        else return taskRepository.findAllByOwner_ChiefId(getIdAuthUser());
    }

    public Task findOne(int id) {
        Optional<Task> foundTask = taskRepository.findById(id);
        return foundTask.orElse(null);
    }


    public Task findOneByChief(int id) {
        Optional<Task> foundTask = taskRepository.findById(id);
        if (foundTask.isPresent() && foundTask.get().getOwner() != null
                && (foundTask.get().getOwner().getId() == getIdAuthUser()
                || foundTask.get().getOwner().getChiefId().equals(getIdAuthUser())
                || peopleRepository.findById(getIdAuthUser()).get().getRole().equals("ROLE_ADMIN")))
            return foundTask.orElse(null);

        else if (foundTask.isPresent()
                && peopleRepository.findById(getIdAuthUser()).get().getRole().equals("ROLE_ADMIN")
                && foundTask.get().getOwner() == null)
            return foundTask.orElse(null);
        else return null;

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
        updatedTask.setStatus(true);
        updatedTask.setCreatedAt(findOne(id).getCreatedAt());
        taskRepository.save(updatedTask);
    }

    @Transactional
    public void delete(int id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    public void createReport(int id, String report) {
        Task task = findOne(id);
        task.setStatus(false);
        task.setExecutionAt(LocalDateTime.now());
        task.setReport(report);
        taskRepository.save(task);
    }

    private int getIdAuthUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return peopleRepository.findByFullName(username).get().getId();
    }

    public void checkExpired(List<Task> tasks) {
        for (Task task : tasks) {
            if (task.getStatus() && (task.getDeadline().isBefore(LocalDateTime.now()))) {
                task.setExpired(true);
            }
        }
    }
}