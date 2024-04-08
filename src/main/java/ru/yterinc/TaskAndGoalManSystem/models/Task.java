package ru.yterinc.TaskAndGoalManSystem.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Date;

@Entity
@Table(name = "Task")
public class Task {
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
    @NotNull
    private Boolean status;

    @Column(name = "created_at")
//    @NotNull
    private Date createdAt;

    @Column(name = "deadline")
//    @NotNull
    private Date deadline;

    @Column(name = "execution_at")
//    @NotNull
    private Date executionAt;

    @Column(name = "description")
    private String description;

    @Column(name = "report")
    private String report;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    public Task() {
    }

    public Task(String taskName, Integer priority, Boolean status, Date createdAt, Date deadline, Date executionAt, String description, String report, Person owner) {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getExecutionAt() {
        return executionAt;
    }

    public void setExecutionAt(Date executionAt) {
        this.executionAt = executionAt;
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


}
