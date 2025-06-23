package com.karl.projects.spring_gateway.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private static final String DELIMITER = ","; 

    @Override
    public String convertToDatabaseColumn(List<String> stringList) {
        return stringList != null ? String.join(DELIMITER, stringList) : null;
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return dbData != null ? Arrays.asList(dbData.split(DELIMITER))
                                     .stream()
                                     .map(String::trim) // Trim whitespace from each element
                                     .collect(Collectors.toList()) : null;
    }
}