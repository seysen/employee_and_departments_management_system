package com.test.departmentsmanagement.service;

import com.test.departmentsmanagement.model.Department;

import java.util.List;

public interface DepartmentService {
    //Create
    void saveDepartment(Department department);

    //Read
    Department getById(long id);

    //Read All
    List<Department> findAll();

    //Update
    void updateDepartment(long id, String name);

    //Delete
    void deleteDepartment(long id);
}
