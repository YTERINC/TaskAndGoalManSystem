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
import java.util.Objects;
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
        else if (peopleRepository.findById(getIdAuthUser()).get().getRole().equals("ROLE_USER")
                && Objects.equals(peopleRepository.findById(getIdAuthUser()).get().getChief(), peopleRepository.findById(getIdAuthUser()).get().getFullName()))
            return peopleRepository.findByChief(peopleRepository.findById(getIdAuthUser()).get().getFullName());
        else { //  если пользователь имеет шефа, его добавляем в список пользователей отдельно, иначе его не будет видно
            List<Person> people = peopleRepository.findByChief(peopleRepository.findById(getIdAuthUser()).get().getFullName());
            people.add(peopleRepository.findById(getIdAuthUser()).get());
            return people;
        }
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
                || peopleRepository.findById(getIdAuthUser()).get().getRole().equals("ROLE_ADMIN")
                || foundPerson.get().getFullName().equals(peopleRepository.findById(getIdAuthUser()).get().getFullName()))) // для того чтобы пользователь видел свою страницу, если у него есть шеф
            return foundPerson.orElse(null);
        else return null;
    }

    @Transactional
    public void save(Person person) {
        String encodedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);
        person.setRole("ROLE_USER");
        person.setChief(person.getFullName());
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        List<Person> people = peopleRepository.findByChief(peopleRepository.findById(id).get().getFullName());
        for (Person person : people) {
            person.setChief(updatedPerson.getFullName());
            peopleRepository.save(person);
        }

        updatedPerson.setId(id);

        if (updatedPerson.getChief().equals(findOne(id).getFullName()))
            updatedPerson.setChief(updatedPerson.getFullName());

        updatedPerson.setPassword(findOne(id).getPassword());
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        List<Person> people = peopleRepository.findByChief(peopleRepository.findById(id).get().getFullName());
        for (Person person : people) {
            person.setChief(person.getFullName());
            peopleRepository.save(person);
        }
        peopleRepository.deleteById(id);
    }

    public List<Task> getTaskByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getTasks());
            // вызываем Hibernate.initialize()
            // на случай, например, если код в дальнейшем поменяется
            return person.get().getTasks();
        } else {
            return Collections.emptyList();
        }
    }

    public int getIdAuthUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return peopleRepository.findByFullName(username).get().getId();
    }

    @Transactional
    public void updatePassword(int id, String newPass) {
        Person person = findOneByChief(id);

        if (person != null) {
            person.setPassword(passwordEncoder.encode(newPass));
            peopleRepository.save(person);
        }
    }
}
