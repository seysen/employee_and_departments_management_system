package com.test.employeemanagement.service;

import com.test.employeemanagement.exception.EmployeeNotFoundException;
import com.test.employeemanagement.model.Employee;
import com.test.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    //Create
    @Override
    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    //Read
    @Override
    public Employee getById(long id) {
        Employee employee = null;
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) employee = employeeOptional.get();
        return employee;
    }

    //Read All
    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    //Update
    @Override
    public void updateEmployee(long id, Long departmentId, String name) {
        Employee employee = employeeRepository.getById(id);
        employee.setDepartmentId(departmentId);
        employee.setName(name);
        employeeRepository.save(employee);
    }

    //Delete
    @Override
    public void deleteEmployee(long id) {
        employeeRepository.deleteById(id);
    }
}
