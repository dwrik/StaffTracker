package org.dwrik.employee.controller;

import jakarta.validation.Valid;
import org.dwrik.employee.entity.Employee;
import org.dwrik.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody @Valid Employee employee) {
        var saved = employeeService.createEmployee(employee);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<Iterable<Employee>> getAllOrByDepartment(@RequestParam(required = false) Long departmentId) {
        Iterable<Employee> employees;
        if (departmentId == null) {
            employees = employeeService.getAllEmployees();
        } else {
            employees = employeeService.getEmployeesByDepartmentId(departmentId);
        }
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getOne(@PathVariable Long id) {
        var employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Employee> delete(@PathVariable Long id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Employee> deleteByDepartment(@RequestParam Long departmentId) {
        employeeService.deleteEmployeesByDepartmentId(departmentId);
        return ResponseEntity.noContent().build();
    }

}
