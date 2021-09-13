package com.test.departmentsmanagement.controller;

import com.test.departmentsmanagement.model.Department;
import com.test.departmentsmanagement.model.Employee;
import com.test.departmentsmanagement.model.Employees;
import com.test.departmentsmanagement.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    //Create
    @PostMapping("/department")
    public void addDepartment(@RequestParam String name) {
        departmentService.saveDepartment(new Department(name));
    }

    //Read
    @GetMapping("/department")
    public Department getById(@RequestParam long id) {
        Department department = departmentService.getById(id);
        if (department == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        return department;
    }

    //Read All
    @GetMapping("/departments")
    public List<Department> findAll() {
        return departmentService.findAll();
    }

    //Update
    @PutMapping("/department")
    public void updateDepartment(@RequestParam long id, @RequestParam String name) {
        departmentService.updateDepartment(id, name);
    }

    //Delete
    @DeleteMapping("/department")
    public void deleteDepartment(@RequestParam long id, @RequestParam long newDepartmentId) throws URISyntaxException {
        if (isValidDepartment(newDepartmentId)) {
            RestTemplate restTemplate = new RestTemplate();

            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
            messageConverters.add(converter);
            restTemplate.setMessageConverters(messageConverters);

            URI url = new URI("http://localhost:8081/employees");
            Employees employees = restTemplate.getForObject(url, Employees.class);
            for (Employee employee: employees.getEmployees()) {
                RestTemplate putRestTemplate = new RestTemplate();
                if (employee.getDepartmentId() == id) {
                    url = new URI("http://localhost:8081/employee?id=" + employee.getId() +
                            "&department=" + newDepartmentId +
                            "&name=" + employee.getName());
                    Employee newEmployee = new Employee(employee.getId(),newDepartmentId, employee.getName());
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
                    HttpEntity<Employee> requestBody = new HttpEntity<>(newEmployee, headers);
                    putRestTemplate.put(String.valueOf(url), requestBody);
                }
            }
            departmentService.deleteDepartment(id);
        }
    }

    private boolean isValidDepartment(long departmentId) {
        Department department = departmentService.getById(departmentId);
        if (department != null) {
            return true;
        } else {
            return false;
        }
    }
}
