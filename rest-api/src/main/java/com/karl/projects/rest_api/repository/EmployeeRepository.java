package com.karl.projects.rest_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karl.projects.rest_api.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
