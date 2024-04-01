package ru.yterinc.TaskAndGoalManSystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yterinc.TaskAndGoalManSystem.models.Task;

public interface TaskRepository extends JpaRepository<Task,Integer> {




}
