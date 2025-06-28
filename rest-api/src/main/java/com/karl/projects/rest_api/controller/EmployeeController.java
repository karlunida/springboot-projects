package com.karl.projects.rest_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karl.projects.rest_api.dto.EmployeeDTO;
import com.karl.projects.rest_api.entity.Employee;
import com.karl.projects.rest_api.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping
	public ResponseEntity<List<Employee>> findAll(){
		return ResponseEntity.status(HttpStatus.OK).body(employeeService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Employee> find(@PathVariable Long id){
		return ResponseEntity.status(HttpStatus.OK).body(employeeService.find(id));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		employeeService.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@PostMapping
	public ResponseEntity<Employee> save(@Valid @RequestBody EmployeeDTO employeeDto){
		return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.save(employeeDto));
	}
	
	@PutMapping
	public ResponseEntity<Employee> put(@Valid @RequestBody EmployeeDTO employeeDto){
		Pair<HttpStatus, Employee> res = employeeService.put(employeeDto);
		return ResponseEntity.status(res.getFirst()).body(res.getSecond());
	}

}
