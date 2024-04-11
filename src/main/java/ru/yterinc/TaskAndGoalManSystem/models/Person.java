package ru.yterinc.TaskAndGoalManSystem.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

// Модель пользователя
@Entity
@Table(name = "Person")
public class Person implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов длиной")
    @Column(name = "full_name")
    private String fullName;

    @Size(min = 2, max = 100, message = "Email должен быть от 2 до 100 символов длиной")
    @Email(message = "Неверный формат")
    @Column(name = "email")
    private String email;

    @Temporal(TemporalType.DATE)
    @Column(name = "year_of_birth")
    @Past
    private Date yearOfBirth;

    @Column(name = "role")
    private String role;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "owner")
    private List<Task> tasks;

    public Person() {
    }

    public Person(String fullName, String email, Date yearOfBirth, String role, String description) {
        this.fullName = fullName;
        this.email = email;
        this.yearOfBirth = yearOfBirth;
        this.role = role;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Date yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
