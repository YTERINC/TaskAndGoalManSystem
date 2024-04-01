package ru.yterinc.TaskAndGoalManSystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yterinc.TaskAndGoalManSystem.models.Person;

import java.util.Optional;

public interface PeopleRepository extends JpaRepository<Person,Integer> {
    Optional<Person> findByFullName(String fullName);

    Optional<Person> findByEmail(String email);

}
