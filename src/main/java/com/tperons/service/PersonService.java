package com.tperons.service;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tperons.entity.Person;
import com.tperons.exception.ResourceNotFoundException;
import com.tperons.repository.PersonRepository;

@Service
public class PersonService {

    private Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    private PersonRepository repository;

    public List<Person> findAll() {
        logger.info("Finding all People!");
        return repository.findAll();
    }

    public Person findById(Long id) {
        logger.info("Finding one Person!");
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
    }

    public Person create(Person obj) {
        logger.info("Creating one Person!");
        return repository.save(obj);
    }

    public Person update(Long id, Person obj) {
        logger.info("Updating one Person!");
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        entity.setFirstName(obj.getFirstName());
        entity.setLastName(obj.getLastName());
        entity.setAddress(obj.getAddress());
        entity.setGender(obj.getGender());
        return repository.save(entity);
    }

    public void delete(Long id) {
        logger.info("Deleting one Person!");
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);

    }

}
