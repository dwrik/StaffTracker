package org.dwrik.employee.service;

import org.dwrik.employee.entity.Employee;
import org.dwrik.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RestClient.Builder restClientBuilder;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void testGetEmployeeById() {
        var employee = new Employee(1L, 1L, "Jim");
        Mockito.when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        var actual = employeeService.getEmployeeById(anyLong());
        assertEquals(employee, actual);
    }

    @Test
    public void testGetEmployeeByInvalidId() {
        Mockito.when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        var actual = employeeService.getEmployeeById(anyLong());
        assertNull(actual);
    }

    @Test
    public void testGetAllEmployees() {
        var employees = List.of(
                new Employee(1L, 1L, "Jim"),
                new Employee(2L, 1L, "Dwight"),
                new Employee(5L, 2L, "Kevin")
        );
        Mockito.when(employeeRepository.findAll()).thenReturn(employees);
        var actual = employeeService.getAllEmployees();
        assertEquals(employees, actual);
    }

    @Test
    public void testGetEmployeesByDepartmentId() {
        var employees = List.of(
                new Employee(1L, 1L, "Jim"),
                new Employee(2L, 1L, "Dwight"),
                new Employee(3L, 1L, "Stanley"),
                new Employee(4L, 1L, "Phyllis")
        );
        Mockito.when(employeeRepository.findByDepartmentId(anyLong())).thenReturn(employees);
        var actual = employeeService.getEmployeesByDepartmentId(anyLong());
        assertEquals(employees, actual);
    }

    @Test
    public void testDeleteEmployeeById() {
        Mockito.doNothing().when(employeeRepository).deleteById(anyLong());
        employeeService.deleteEmployeeById(anyLong());
        verify(employeeRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testDeleteEmployeesByDepartmentId() {
        Mockito.doNothing().when(employeeRepository).deleteByDepartmentId(anyLong());
        employeeService.deleteEmployeesByDepartmentId(anyLong());
        verify(employeeRepository, times(1)).deleteByDepartmentId(anyLong());
    }

}
