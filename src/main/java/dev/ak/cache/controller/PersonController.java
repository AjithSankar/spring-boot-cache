package dev.ak.cache.controller;


import dev.ak.cache.entity.Address;
import dev.ak.cache.entity.Person;
import dev.ak.cache.repository.PersonRepository;
import dev.ak.cache.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<Person> findAll() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public Person findById(@PathVariable Integer id) {
        return personService.findById(id);
    }

    @GetMapping("/page")
    public ResponseEntity<List<Person>> allPeoplePaged (
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size" , defaultValue = "10") int size) {

        Page<Person> personPage = personService.getPagedPersons(page, size);
        return ResponseEntity.ok(personPage.getContent());
    }

    @PostMapping
    public Person create(@RequestBody Person input) {
        Person person = new Person();
        person.setFirstName(input.getFirstName());
        person.setLastName(input.getLastName());
        person.setEmail(input.getEmail());
        person.setPhoneNumber(input.getPhoneNumber());
        return personService.save(person);
    }

    @PutMapping("/update")
    public Person updatePerson(@RequestBody Person input) {
        Person person = null;
        Person optionalPerson = personService.findById(input.getId());
        if (optionalPerson != null) {
            person = optionalPerson;
            person.setFirstName(input.getFirstName());
            person.setLastName(input.getLastName());
            person.setEmail(input.getEmail());
            person.setPhoneNumber(input.getPhoneNumber());
            Address address = input.getAddress();
            person.setAddress(new Address(address.getAddress(), address.getCity(), address.getState(), address.getZip()));
        }
        if (person != null) {
            personService.save(person);
        }
        return person;
    }

/*    @PutMapping()
    public Person updatePerson(@RequestBody Person input) {
        Person person = null;
        Optional<Person> optionalPerson = personRepository.findById(input.getId());
        if (optionalPerson.isPresent()) {
            person = optionalPerson.get();
            person.setFirstName(input.getFirstName());
            person.setLastName(input.getLastName());
            person.setEmail(input.getEmail());
            person.setPhoneNumber(input.getPhoneNumber());
            Address address = input.getAddress();
            person.setAddress(new Address(address.getAddress(), address.getCity(), address.getState(), address.getZip()));
        }
        if (person != null) {
            personRepository.save(person);
        }
        return person;
    }

    @PutMapping()
    public Person update(int id, String phoneNumber) {
        Person person = null;
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            person = optionalPerson.get();
            person.setPhoneNumber(phoneNumber);
        }
        if (person != null) {
            personRepository.save(person);
        }
        return person;
    } */

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        personService.deletePersonById(id);
    }
}
