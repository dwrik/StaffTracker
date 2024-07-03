package org.dwrik.department;

import org.dwrik.department.entity.Department;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@Testcontainers
@SpringBootTest(
        properties = {"eureka.client.enabled=false"},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DepartmentApplicationTests {

    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private Integer port;

    private String baseURL;

    @BeforeEach
    void setup() {
        baseURL = "http://localhost:" + port;
    }

    @Test
    @Order(0)
    public void contextLoads() {
    }

    @Test
    @Order(1)
    public void testDepartmentNotCreatedWithNullName() {
        var expected = Map.of("name", "name cannot be null");
        var response = restTemplate.postForEntity(baseURL, new Department(), Map.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    @Order(2)
    public void testDepartmentCreatedWithValidName() {
        var expected = new Department(null, "Sales");
        var response = restTemplate.postForEntity(baseURL, expected, Department.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected.getName(), Objects.requireNonNull(response.getBody()).getName());
    }

    @Test
    @Order(3)
    public void testDepartmentNotCreatedWithExistingName() {
        var existing = new Department(null, "Sales");
        var expected = Map.of("error", "department with name 'Sales' already exists");
        var response = restTemplate.postForEntity(baseURL, existing, Map.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    @Order(4)
    public void testGettingAllDepartments() throws IOException {
        var expected = List.of(new Department(1L, "Sales"));
        var response = restTemplate.getForEntity(baseURL, String.class);
        List<Department> list = jsonToDepartmentList(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, list.size());
        assertIterableEquals(expected, list);
    }

    @Test
    @Order(5)
    public void testGettingOneDepartment() {
        var expected = new Department(1L, "Sales");
        var response = restTemplate.getForEntity(baseURL + "/1", Department.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    @Order(6)
    public void testDeletingOneDepartment() {
        var beforeDeleteResponse = restTemplate.getForEntity(baseURL + "/1", Department.class);
        assertEquals(HttpStatus.OK, beforeDeleteResponse.getStatusCode());

        restTemplate.delete(baseURL + "/1");

        var afterDeleteResponse = restTemplate.getForEntity(baseURL + "/1", Department.class);
        assertEquals(HttpStatus.NOT_FOUND, afterDeleteResponse.getStatusCode());
    }

    private List<Department> jsonToDepartmentList(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<List<Department>>() {
        });
    }

}
