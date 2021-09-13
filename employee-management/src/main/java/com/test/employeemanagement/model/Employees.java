package com.test.employeemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Employees {
    List<Employee> employees;
}
