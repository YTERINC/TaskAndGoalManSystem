package ru.yterinc.TaskAndGoalManSystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yterinc.TaskAndGoalManSystem.models.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByOwner_ChiefId(int chiefId);
}
