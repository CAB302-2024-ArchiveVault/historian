package com.example.historian.models.person;

import java.util.List;

public interface IPersonDAO {
    public void addPerson(Person person);

    public void removePerson(Person person);

    public Person getPerson(int id);

    public List<Person> getAllPersons();
}
