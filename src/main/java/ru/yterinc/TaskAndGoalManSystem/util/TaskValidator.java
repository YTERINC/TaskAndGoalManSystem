package ru.yterinc.TaskAndGoalManSystem.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.yterinc.TaskAndGoalManSystem.models.Task;

import java.time.LocalDateTime;

@Component
public class TaskValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Task.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Task task = (Task) target;

        if (task.getTaskName() == null) {
            errors.rejectValue("taskName", "", "Необходимо указать наименование задачи");
        }

        if (task.getDeadline() == null) {
            errors.rejectValue("deadline", "", "Необходимо указать крайнее время исполнения задачи");
        }

        if (task.getDeadline() == null || task.getDeadline().isBefore(LocalDateTime.now())) {
            errors.rejectValue("deadline", "", "Время исполнения задачи должно быть позднее текущей даты");
        }

    }
}
