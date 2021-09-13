package com.test.departmentsmanagement.service;

import com.test.departmentsmanagement.model.Department;
import com.test.departmentsmanagement.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    //Create
    @Override
    public void saveDepartment(Department department) {
        departmentRepository.save(department);
    }

    //Read
    @Override
    public Department getById(long id) {
        Optional<Department> departmentOptional = departmentRepository.findById(id);
        Department department = null;
        if (departmentOptional.isPresent()) {
            department = departmentOptional.get();
        }
        return department;
    }

    //Read All
    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    //Update
    @Override
    public void updateDepartment(long id, String name) {
        Department department = departmentRepository.getById(id);
        department.setName(name);
        departmentRepository.save(department);
    }

    //Delete
    @Override
    public void deleteDepartment(long id) {
        departmentRepository.deleteById(id);
    }
}
