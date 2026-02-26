package com.tperons.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tperons.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
