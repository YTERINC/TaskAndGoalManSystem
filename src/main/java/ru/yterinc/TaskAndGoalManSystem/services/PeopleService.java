package ru.yterinc.TaskAndGoalManSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yterinc.TaskAndGoalManSystem.models.Person;
import ru.yterinc.TaskAndGoalManSystem.models.Task;
import ru.yterinc.TaskAndGoalManSystem.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public List<Person> findAllByChief() {
        if (peopleRepository.findById(getIdAuthUser()).get().getRole().equals("ROLE_ADMIN"))
            return peopleRepository.findAll();
        else return peopleRepository.findByChief(peopleRepository.findById(getIdAuthUser()).get().getFullName());
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    public Optional<Person> getPersonByEmail(String email) {
        return peopleRepository.findByEmail(email);
    }

    public Person findOne(int id) {    // всегда возвращает пользователя, независимо от прав
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    public Person findOneByChief(int id) {  // возвращает пользователя, если авторизованный пользователь у него шеф
        Optional<Person> foundPerson = peopleRepository.findById(id);
        if (foundPerson.isPresent()
                && (foundPerson.get().getChief().equals(peopleRepository.findById(getIdAuthUser()).get().getFullName())
                || peopleRepository.findById(getIdAuthUser()).get().getRole().equals("ROLE_ADMIN"))
        )
            return foundPerson.orElse(null);
        else return null;
    }

    @Transactional
    public void save(Person person) {
        String encodedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);
        person.setRole("ROLE_USER");

        System.out.println(person.getFullName());
        person.setChief(person.getFullName());
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        updatedPerson.setPassword(findOne(id).getPassword());
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

    private int getIdAuthUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return peopleRepository.findByFullName(username).get().getId();
    }
}
