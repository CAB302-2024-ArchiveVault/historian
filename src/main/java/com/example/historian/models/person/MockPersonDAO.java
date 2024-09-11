package com.example.historian.models.person;

import java.util.ArrayList;
import java.util.List;

public class MockPersonDAO implements IPersonDAO {
    private static ArrayList<Person> people = new ArrayList<>();
    private static int autoIncrementId = 0;

    public MockPersonDAO() {}

    @Override
    public void addPerson(Person person) {
        person.setId(autoIncrementId);
        autoIncrementId++;
        people.add(person);
    }

    @Override
    public void removePerson(Person person) { people.remove(person); }

    @Override
    public Person getPerson(int id) {
        for (Person person : people) {
            if (person.getId() == id) {
                return person;
            }
        }
        return null;
    }

    @Override
    public List<Person> getAllPersons() { return new ArrayList<>(people); }
}
