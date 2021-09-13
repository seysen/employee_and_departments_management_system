package com.test.departmentsmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Data
@Entity
@Table(name = "departments")
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Department {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long id;
    @NonNull private String name;
}
