package com.tperons.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tperons.controller.PersonController;
import com.tperons.data.dto.PersonDTO;
import com.tperons.entity.Person;
import com.tperons.exception.RequiredObjectIsNullException;
import com.tperons.exception.ResourceNotFoundException;
import com.tperons.mapper.PersonMapper;
import com.tperons.repository.PersonRepository;

import jakarta.transaction.Transactional;

@Service
public class PersonService {

    private Logger logger = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    private PersonRepository repository;

    @Autowired
    private PersonMapper mapper;

    public Page<PersonDTO> findAll(Pageable pageable) {
        logger.info("Finding all People!");
        Page<Person> personPage = repository.findAll(pageable);
        Page<PersonDTO> dtoPage = personPage.map(p -> mapper.toDTO(p));
        dtoPage.forEach(p -> addHateoasLinks(p));
        return dtoPage;
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one Person!");
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var dto = mapper.toDTO(entity);
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO create(PersonDTO obj) {
        if (obj == null)
            throw new RequiredObjectIsNullException();
        logger.info("Creating one Person!");
        var entity = mapper.toEntity(obj);
        var dto = mapper.toDTO(repository.save(entity));
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO update(Long id, PersonDTO obj) {
        if (obj == null)
            throw new RequiredObjectIsNullException();
        logger.info("Updating one Person!");
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        entity.setFirstName(obj.getFirstName());
        entity.setLastName(obj.getLastName());
        entity.setAddress(obj.getAddress());
        entity.setGender(obj.getGender());
        var dto = mapper.toDTO(repository.save(entity));
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting one Person!");
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);

    }

    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("Disabling one Person");
        repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for the ID!"));
        repository.disablePerson(id);
        var entity = repository.findById(id).get();
        var dto = mapper.toDTO(repository.save(entity));
        addHateoasLinks(dto);
        return dto;
    }

    private static void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(null, null, null, null)).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto.getId(), dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
    }

}
