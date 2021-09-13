package com.test.employeemanagement.controller;

import com.test.employeemanagement.exception.EmployeeNotFoundException;
import com.test.employeemanagement.model.Department;
import com.test.employeemanagement.model.Employee;
import com.test.employeemanagement.model.Employees;
import com.test.employeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;
    //private final RestTemplate restTemplate;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //Create
    @PostMapping("/employee")
    public void addEmployee(@RequestParam long department, @RequestParam String name){
        if (isValidDepartment(department)){
            employeeService.saveEmployee(new Employee(department, name));
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isValidDepartment(long departmentId) {
        RestTemplate restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);

        try {
            URI url = new URI("http://localhost:8080/department?id=" + departmentId);
            ResponseEntity<Department> entry = restTemplate.getForEntity(url, Department.class);
            if (entry.getStatusCode() == HttpStatus.OK) {
                Department department = entry.getBody();
                assert department != null;
                if (department.getId() == departmentId) return true;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (HttpClientErrorException e) {
            return false;
        }
        return false;
    }

    //Read
    @GetMapping("/employee")
    public Employee findById(@RequestParam long id) {
        Employee employee = employeeService.getById(id);
        if (employee == null) throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
        return employee;
    }

    //Read All
    @GetMapping("/employees")
    public Employees findAll() {
        return new Employees(employeeService.findAll());
    }

    //Update
    @PutMapping("/employee")
    public void updateEmployee(@RequestParam long id, @RequestParam long department, @RequestParam String name) {
        if (isValidDepartment(department)){
            employeeService.updateEmployee(id, department, name);
        }
    }

    //Delete
    @DeleteMapping("/employee")
    public void deleteEmployee(@RequestParam long id) {
        employeeService.deleteEmployee(id);
    }
}
