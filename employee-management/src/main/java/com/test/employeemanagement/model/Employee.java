package com.test.employeemanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Data
@Entity
@Table(name = "employees")
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Transactional
public class Employee  {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long id;
    @Column(name = "department_id")
    @NonNull private long departmentId;
    @NonNull private String name;

}
