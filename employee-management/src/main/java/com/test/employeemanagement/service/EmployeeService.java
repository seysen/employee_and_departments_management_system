package com.test.employeemanagement.service;

import com.test.employeemanagement.model.Employee;

import java.util.List;

public interface EmployeeService {
    //Create
    void saveEmployee(Employee employee);

    //Read
    Employee getById(long id);

    //Read All
    List<Employee> findAll();

    //Update
    void updateEmployee(long id, Long departmentId, String name);

    //Delete
    void deleteEmployee(long id);
}
