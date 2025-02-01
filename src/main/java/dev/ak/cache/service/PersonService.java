package dev.ak.cache.service;

import dev.ak.cache.entity.Person;
import dev.ak.cache.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@CacheConfig(cacheNames = "pagedPersons")  // This annotation allows us to specify some of the cache configurations at the class level, so we do not have to repeat them multiple times over each method.
public class PersonService {

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

    @Cacheable(value = "persons", key = "#id")
    public Person findById(Integer id) {
        return personRepository.findById(id).orElse(null);
    }


    @Cacheable(value = "pagedPersons", key = "#page + '-' + #size")
    public Page<Person> getPagedPersons(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        log.info("Getting persons for Page {} and size {} ", page, size);
        Page<Person> personPage = personRepository.findAll(pageRequest);
        log.info("Retrieved persons for Page {} and size {} ", page, size);
        return personPage;
    }

    @CachePut(value = "persons", key = "#person.id")
    public Person save(Person person) {
        return personRepository.save(person);
    }

    @CacheEvict(value = "persons", key = "#id")
    public void deletePersonById(Integer id) {
        personRepository.deleteById(id);
    }

    @CacheEvict(value = "persons", allEntries = true)  // The @CacheEvict annotation offers an extra parameter ‘allEntries’ for evicting the whole specified cache, rather than a key in the cache.
    public void deleteAllPersons() {
        personRepository.deleteAll();
    }
}
