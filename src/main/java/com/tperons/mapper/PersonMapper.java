package com.tperons.mapper;

import java.util.List;

import org.mapstruct.Mapper;
// import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.tperons.data.dto.PersonDTO;
import com.tperons.entity.Person;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonDTO toDTO(Person person);

    Person toEntity(PersonDTO dto);

    List<PersonDTO> toDTOList(List<Person> people);

    List<Person> toEntityList(List<PersonDTO> dtos);

}
