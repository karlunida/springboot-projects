package com.karl.projects.rest_api.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.karl.projects.rest_api.dto.EmployeeDTO;
import com.karl.projects.rest_api.entity.Employee;
import com.karl.projects.rest_api.repository.EmployeeRepository;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	private TypeMap<EmployeeDTO, Employee> mapper;
	
	public List<Employee> findAll(){
		return employeeRepository.findAll();
	}
	
	public Employee find(Long id) {
		return employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found!"));
	}
	
	@Transactional
	public void delete(Long id) {
		employeeRepository.deleteById(id);
	}
	
	@Transactional
	public Employee save(EmployeeDTO employeeDto) {
		return employeeRepository.save(modelMapper.map(employeeDto, Employee.class));
	}
	
	@Transactional
	public Pair<HttpStatus, Employee> put(EmployeeDTO employeeDto) {

		if(employeeDto.getId() == null) {
			return Pair.of(HttpStatus.CREATED, save(employeeDto));
		} else {
			return employeeRepository.findById(employeeDto.getId())
					.map(e -> {
						modelMapper.map(employeeDto, e);
						return Pair.of(HttpStatus.NO_CONTENT,  employeeRepository.save(e));
					})
					.orElseGet(() -> {
						return Pair.of(HttpStatus.CREATED,  employeeRepository.save(modelMapper.map(employeeDto, Employee.class)));
					});
		}
	}
	
	@PostConstruct
	private void init() {
		mapper = this.modelMapper.createTypeMap(EmployeeDTO.class,Employee.class);
		mapper.addMappings(m -> m.skip(Employee::setId));
	}

}
