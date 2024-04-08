package ru.yterinc.TaskAndGoalManSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yterinc.TaskAndGoalManSystem.models.Person;
import ru.yterinc.TaskAndGoalManSystem.models.Task;
import ru.yterinc.TaskAndGoalManSystem.repositories.PeopleRepository;
import org.hibernate.Hibernate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    public Optional<Person> getPersonByEmail(String email) {
        return peopleRepository.findByEmail(email);
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        person.setRole("ROLE_USER");
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        updatedPerson.setRole("ROLE_USER");
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public List<Task> getTaskByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getTasks());
            // не мешает всегда вызывать Hibernate.initialize()
            // на случай, например, если код в дальнейшем поменяется
            return person.get().getTasks();
        } else {
            return Collections.emptyList();
        }
    }

}
