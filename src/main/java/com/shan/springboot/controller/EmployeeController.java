package com.shan.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shan.springboot.exception.ResourceNotFoundExeption;
import com.shan.springboot.model.Employee;
import com.shan.springboot.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

	@Autowired 
	private EmployeeRepository employeeRepository;
	
	// get employees
	@GetMapping("/employees")
	public List<Employee> getAllEmployee(){
		
		return this.employeeRepository.findAll();
	}
	
	// get employee by id
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId) throws ResourceNotFoundExeption{
	
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow( ()-> new ResourceNotFoundExeption("Employee not found for this id : " + employeeId));
		
		return ResponseEntity.ok().body(employee);
	}
	
	// save employee
	@PostMapping("/employees")
	public Employee createEmployee(@RequestBody Employee employee) {
		
		return this.employeeRepository.save(employee);
	}
	
	// update employee
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId, 
			@Validated @RequestBody Employee employeeDetails) throws ResourceNotFoundExeption{
		
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow( ()-> new ResourceNotFoundExeption("Employee not found for this id : " + employeeId));
		
		employee.setFirstName(employeeDetails.getFirstName());
		employee.setLastName(employeeDetails.getLastName());
		employee.setEmail(employeeDetails.getEmail());
		
		return ResponseEntity.ok(this.employeeRepository.save(employee));
	}
	
	// delete employee
	@DeleteMapping("/employees/{id}")
	public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId) throws ResourceNotFoundExeption{
		
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow( ()-> new ResourceNotFoundExeption("Employee not found for this id : " + employeeId));
	
		this.employeeRepository.delete(employee);
		
		Map<String, Boolean> response = new HashMap<>();
		
		response.put("deleted", Boolean.TRUE);
	
		return response;
	}
}
