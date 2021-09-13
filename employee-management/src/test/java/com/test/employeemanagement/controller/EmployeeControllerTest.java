package com.test.employeemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import com.test.employeemanagement.exception.EmployeeNotFoundException;
import com.test.employeemanagement.model.Department;
import com.test.employeemanagement.model.Employee;
import com.test.employeemanagement.repository.EmployeeRepository;
import com.test.employeemanagement.service.EmployeeService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/schema.sql", "/data.sql"})
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void createServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost",8080),0);
        server.createContext("/department", exchange -> {
            if (exchange.getRequestMethod().equals("GET")) {
                long id = Long.parseLong(exchange.getRequestURI().toString().split("\\?")[1].split("=")[1]);
                OutputStream outputStream = exchange.getResponseBody();
                String response = null;
                if (id < 10) {
                    response = mapper.writeValueAsString(new Department(id, "SomeDepartment"));
                    exchange.sendResponseHeaders(200, response.length());
                } else {
                    response = "1";
                    exchange.sendResponseHeaders(404,response.length());
                }
                outputStream.write(response.getBytes());
                outputStream.flush();
                outputStream.close();
            }
        });
        server.setExecutor(Executors.newFixedThreadPool(5));
        server.start();
    }

    @Test
    public void addEmployeeTest() throws Exception {
        this.mockMvc
                .perform(post("/employee")
                        .param("department","1")
                        .param("name","TestEmployee")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        this.mockMvc
                .perform(get("/employee")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "4")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentId").value("1"))
                .andExpect(jsonPath("$.name").value("TestEmployee"))
                .andReturn();
        System.out.println(employeeRepository.findAll());
    }


    @Test
    public void findByIdTest() throws Exception {
        this.mockMvc
                .perform(get("/employee")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "2")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentId").value("2"))
                .andExpect(jsonPath("$.name").value("Security"))
                .andReturn();
        System.out.println(employeeRepository.findAll());
    }

    @Test
    public void findAllTest() throws Exception {
        this.mockMvc
                .perform(get("/employees")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employees",hasSize(3)))
                .andExpect(jsonPath("$.employees[*].name",containsInAnyOrder("CEO","Security","Developer")))
                .andReturn();
        System.out.println(employeeRepository.findAll());
    }

    @Test
    public void updateEmployeeTest() throws Exception {
        this.mockMvc
                .perform(put("/employee")
                        .param("id","1")
                        .param("department","2")
                        .param("name","UpdatedEmployee")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        this.mockMvc
                .perform(get("/employee")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentId").value("2"))
                .andExpect(jsonPath("$.name").value("UpdatedEmployee"))
                .andReturn();
        System.out.println(employeeRepository.findAll());
    }

    @Test
    public void deleteEmployeeTest() throws Exception {
        this.mockMvc
                .perform(delete("/employee")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "3")
                )
                .andExpect(status().isOk())
                .andReturn();
        this.mockMvc
                .perform(get("/employees")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employees",hasSize(2)))
                .andExpect(jsonPath("$.employees[*].name",containsInAnyOrder("CEO","Security")))
                .andReturn();
        System.out.println(employeeRepository.findAll());
    }

    @Test
    public void employeeNotFoundExceptionTest() throws Exception {
        this.mockMvc
                .perform(get("/employee")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "10")
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void notValidDepartmentIdTest() throws Exception {
        this.mockMvc
                .perform(post("/employee")
                        .param("department","10")
                        .param("name","TestEmployee")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(employeeRepository.findAll());
    }
}