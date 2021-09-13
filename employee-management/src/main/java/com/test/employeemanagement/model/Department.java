package com.test.employeemanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Department {
    private @NonNull long id;
    private @NonNull String name;
}
