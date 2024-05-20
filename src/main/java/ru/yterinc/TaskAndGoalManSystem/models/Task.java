package ru.yterinc.TaskAndGoalManSystem.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Task")
public class Task implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 200, message = "Имя должно быть от 2 до 200 символов длиной")
    @Column(name = "task_name")
    private String taskName;

    @NotNull
    @Min(1)
    @Max(3)
    @Column(name = "priority")
    private Integer priority;

    @Column(name = "status")
//    @NotNull  // на NULL не проверяем, т.к. на уровне сервиса устанавливаем значение
    private Boolean status;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "deadline")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deadline;

    @Column(name = "execution_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime executionAt;

    @Column(name = "description")
    private String description;

    @Column(name = "report")
    private String report;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Transient
    private boolean expired;

    public Task() {
    }

    public Task(String taskName, Integer priority, Boolean status,
                LocalDateTime createdAt, LocalDateTime deadline,
                LocalDateTime executionAt, String description,
                String report, Person owner) {
        this.taskName = taskName;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
        this.deadline = deadline;
        this.executionAt = executionAt;
        this.description = description;
        this.report = report;
        this.owner = owner;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getExecutionAt() {
        return executionAt;
    }

    public void setExecutionAt(LocalDateTime executionAt) {
        this.executionAt = executionAt;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
