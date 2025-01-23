package dev.ak.cache.service;

import dev.ak.cache.entity.Person;
import dev.ak.cache.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PersonService {

    private static final Logger log = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        log.info("Getting all persons");
        List<Person> personList = personRepository.findAll();
        log.info("Retrieved persons from DB");
        return personList;
    }

    public Person findById(Integer id) {
        return personRepository.findById(id).orElse(null);
    }

    @Cacheable("pagedPersons")
    public Page<Person> getPagedPersons(Pageable pageable) {
        log.info("Getting persons for Page {} and size {} ", pageable.getPageNumber(), pageable.getPageSize());
        Page<Person> personPage = personRepository.findAll(pageable);
        log.info("Retrieved persons for Page {} and size {} ", pageable.getPageNumber(), pageable.getPageSize());
        return personPage;
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }
}
