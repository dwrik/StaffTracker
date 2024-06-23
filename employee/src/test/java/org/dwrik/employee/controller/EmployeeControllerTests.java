package org.dwrik.employee.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dwrik.employee.entity.Employee;
import org.dwrik.employee.repository.EmployeeRepository;
import org.dwrik.employee.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = EmployeeController.class)
public class EmployeeControllerTests {

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreate() throws Exception {
        var expected = new Employee(1L, 1L, "Jim");
        var employee = new Employee(null, 1L, "Jim");
        Mockito.when(employeeService.createEmployee(employee)).thenReturn(expected);

        var mvcResult = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeToJson(employee))).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);

        String body = mvcResult.getResponse().getContentAsString();
        Employee actualEmployee = jsonToEmployee(body);
        assertEquals(expected, actualEmployee);
    }

    private String employeeToJson(Employee employee) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(employee);
    }

    private Employee jsonToEmployee(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Employee.class);
    }
}
