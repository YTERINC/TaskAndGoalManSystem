package ru.yterinc.TaskAndGoalManSystem.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.yterinc.TaskAndGoalManSystem.models.Person;
import ru.yterinc.TaskAndGoalManSystem.services.PeopleService;

@Component
public class PersonValidator implements Validator {
    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (person.getId() != null) { // для редактирования существующих пользователей
            int id = person.getId();
            if (peopleService.getPersonByFullName(person.getFullName()).isPresent()
                    && peopleService.getPersonByFullName(person.getFullName()).get().getId() != id) // валидация после && сделано для проверки при редактировании пользователя
                errors.rejectValue("fullName", "", "Пользователь с таким ФИО уже существует");

            if (peopleService.getPersonByEmail(person.getEmail()).isPresent()
                    && peopleService.getPersonByEmail(person.getEmail()).get().getId() != id) // валидация после && сделано для проверки при редактировании пользователя
                errors.rejectValue("email", "", "Пользователь с таким Email уже существует");
        } else { // для новых пользователей
            if (peopleService.getPersonByFullName(person.getFullName()).isPresent())
                errors.rejectValue("fullName", "", "Пользователь с таким ФИО уже существует");

            if (peopleService.getPersonByEmail(person.getEmail()).isPresent())
                errors.rejectValue("email", "", "Пользователь с таким Email уже существует");
        }

        if (person.getYearOfBirth() == null)
            errors.rejectValue("yearOfBirth", "", "Необходимо указать дату рождения");

        if (person.getRole() != null && person.getRole().equals("ROLE_ADMIN"))
            errors.rejectValue("fullName", "", "Имя админа менять нельзя. Прошу обновить страницу");
    }
}
