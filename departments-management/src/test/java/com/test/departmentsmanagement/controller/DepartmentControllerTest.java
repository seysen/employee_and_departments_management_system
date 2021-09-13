package com.test.departmentsmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import com.test.departmentsmanagement.model.Department;
import com.test.departmentsmanagement.model.Employee;
import com.test.departmentsmanagement.model.Employees;
import com.test.departmentsmanagement.repository.DepartmentRepository;
import com.test.departmentsmanagement.service.DepartmentService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/schema.sql", "/data.sql"})
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentService departmentService;

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost",8081),0);
        server.createContext("/employees", exchange -> {
            if (exchange.getRequestMethod().equals("GET")) {
                OutputStream outputStream = exchange.getResponseBody();
                List<Employee> employees = new ArrayList<>();
                employees.add(new Employee(1,"CEO"));
                employees.add(new Employee(2,"Security"));
                employees.add(new Employee(3,"Frontend"));
                String response = mapper.writeValueAsString(new Employees(employees));
                exchange.sendResponseHeaders(200,response.length());
                outputStream.write(response.getBytes());
                outputStream.flush();
                outputStream.close();
            }
        });
        server.createContext("/employee", exchange -> {
            if (exchange.getRequestMethod().equals("PUT")) {
                String id = exchange.getRequestURI().toString().split("\\?")[1].split("=")[1];
                OutputStream outputStream = exchange.getResponseBody();
                String response = "1";
                exchange.sendResponseHeaders(200,response.length());
                outputStream.write(response.getBytes());
                outputStream.flush();
                outputStream.close();
            }
        });
        server.setExecutor(Executors.newFixedThreadPool(5));
        server.start();
    }

    @Test
    public void addDepartmentTest() throws Exception {
        this.mockMvc
                .perform(post("/department")
                        .param("name","TestDepartment")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        this.mockMvc
                .perform(get("/departments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(4)))
                .andExpect(jsonPath("$[*].name",containsInAnyOrder("Administration","Security","Development","TestDepartment" )))
                .andReturn();
        System.out.println(departmentRepository.findAll());
    }

    @Test
    public void getByIdTest() throws Exception {
        this.mockMvc
                .perform(get("/department")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "2")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Security"))
                .andReturn();
        System.out.println(departmentRepository.findAll());
    }

    @Test
    public void deleteDepartmentTest() throws Exception {
        this.mockMvc
                .perform(delete("/department")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "3")
                        .param("newDepartmentId", "1")
                )
                .andExpect(status().isOk())
                .andReturn();
        this.mockMvc
                .perform(get("/departments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[*].name",containsInAnyOrder("Administration","Security")))
                .andReturn();
        System.out.println(departmentRepository.findAll());
    }

    @Test
    public void findAllTest() throws Exception {
        this.mockMvc
                .perform(get("/departments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[*].name",containsInAnyOrder("Administration","Security","Development")))
                .andReturn();
        System.out.println(departmentRepository.findAll());
    }

    @Test
    public void updateDepartmentTest() throws Exception {
        this.mockMvc
                .perform(put("/department")
                        .param("id","1")
                        .param("name","UpdatedDepartment")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        this.mockMvc
                .perform(get("/departments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[*].name",containsInAnyOrder("UpdatedDepartment","Security","Development" )))
                .andReturn();
        System.out.println(departmentRepository.findAll());
    }

    @Test
    public void departmentNotFoundTest() throws Exception {
        this.mockMvc
                .perform(get("/department")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "10")
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }
}