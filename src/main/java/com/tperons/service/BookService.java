package com.tperons.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tperons.controller.BookController;
import com.tperons.dto.BookDTO;
import com.tperons.entity.Book;
import com.tperons.exception.RequiredObjectIsNullException;
import com.tperons.exception.ResourceNotFoundException;
import com.tperons.mapper.BookMapper;
import com.tperons.repository.BookRepository;

@Service
public class BookService {

    private Logger logger = LoggerFactory.getLogger(BookService.class);

    private final BookRepository repository;
    private final BookMapper mapper;

    public BookService(BookRepository repository, BookMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<BookDTO> findAll(Pageable pageable) {
        logger.info("Finding all Books!");
        Page<Book> bookPage = repository.findAll(pageable);
        Page<BookDTO> dtoPage = bookPage.map(b -> mapper.toDTO(b));
        dtoPage.forEach(b -> addHateoasLinks(b));
        return dtoPage;
    }

    public BookDTO findById(Long id) {
        logger.info("Finding one Book!");
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var dto = mapper.toDTO(entity);
        addHateoasLinks(dto);
        return dto;
    }

    public Page<BookDTO> findByTitle(String title, Pageable pageable) {
        logger.info("Finding Books by Title!");
        Page<Book> bookPage = repository.findByTitle(title, pageable);
        Page<BookDTO> dtoPage = bookPage.map(p -> mapper.toDTO(p));
        dtoPage.forEach(p -> addHateoasLinks(p));
        return dtoPage;
    }

    public BookDTO create(BookDTO obj) {
        if (obj == null)
            throw new RequiredObjectIsNullException();
        logger.info("Creating one Book!");
        var entity = mapper.toEntity(obj);
        var dto = mapper.toDTO(repository.save(entity));
        addHateoasLinks(dto);
        return dto;
    }

    public BookDTO update(Long id, BookDTO obj) {
        if (obj == null)
            throw new RequiredObjectIsNullException();
        logger.info("Updating one book!");
        Book entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        entity.setTitle(obj.getTitle());
        entity.setAuthor(obj.getAuthor());
        entity.setLaunchDate(obj.getLaunchDate());
        entity.setPrice(obj.getPrice());
        var dto = mapper.toDTO(repository.save(entity));
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting one Book!");
        Book entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);
    }

    private static void addHateoasLinks(BookDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findAll(null, null, null, null)).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findByTitle(null, null, null, null, null)).withRel("findByTitle").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto.getId(), dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }

}
