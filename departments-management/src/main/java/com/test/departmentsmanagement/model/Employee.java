package com.test.departmentsmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {
    private long id;
    @NonNull
    private long departmentId;
    @NonNull
    private String name;
}
