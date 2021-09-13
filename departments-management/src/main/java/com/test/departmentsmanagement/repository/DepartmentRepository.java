package com.test.departmentsmanagement.repository;

import com.test.departmentsmanagement.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
