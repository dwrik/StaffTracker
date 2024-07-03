package org.dwrik.department.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dwrik.department.entity.Department;
import org.dwrik.department.service.DepartmentService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTests {

    @MockBean
    private DepartmentService departmentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreate() throws Exception {
        var department = new Department(1L, "Sales");

        Mockito.when(departmentService.createDepartment(department)).thenReturn(department);
        var mvcResult = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(departmentToJson(department))).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);

        String body = mvcResult.getResponse().getContentAsString();
        Department actualDepartment = jsonToDepartment(body);
        assertEquals(department, actualDepartment);
    }

    @Test
    public void testGetOne() throws Exception {
        var department = new Department(1L, "Sales");

        Mockito.when(departmentService.getDepartmentById(anyLong())).thenReturn(department);
        var mvcResult = mockMvc.perform(get("/" + anyLong())
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);

        String body = mvcResult.getResponse().getContentAsString();
        Department actualDepartment = jsonToDepartment(body);
        assertEquals(department, actualDepartment);
    }

    @Test
    public void testGetOneWithInvalidId() throws Exception {
        Mockito.when(departmentService.getDepartmentById(anyLong())).thenReturn(null);
        var mvcResult = mockMvc.perform(get("/" + anyLong())
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    @Test
    public void testGetAll() throws Exception {
        var departmentList = List.of(
                new Department(1L, "Sales"),
                new Department(2L, "Accounting")
        );

        Mockito.when(departmentService.getAllDepartments()).thenReturn(departmentList);
        var mvcResult = mockMvc.perform(get("/")
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);

        String body = mvcResult.getResponse().getContentAsString();
        List<Department> departments = jsonToDepartmentList(body);
        assertEquals(departmentList, departments);
    }

    @Test
    public void testDelete() throws Exception {
        Mockito.doNothing().when(departmentService).deleteDepartment(anyLong());
        var mvcResult = mockMvc.perform(delete("/" + anyLong())).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.NO_CONTENT.value(), status);
        verify(departmentService, times(1)).deleteDepartment(anyLong());
    }

    private String departmentToJson(Department department) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(department);
    }

    private Department jsonToDepartment(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Department.class);
    }

    private List<Department> jsonToDepartmentList(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<>() {
        });
    }
}
