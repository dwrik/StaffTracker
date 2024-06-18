package org.dwrik.department.controller;

import jakarta.validation.Valid;
import org.dwrik.department.entity.Department;
import org.dwrik.department.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<Department> create(@RequestBody @Valid Department department) {
        return ResponseEntity.ok(departmentService.createDepartment(department));
    }

    @GetMapping
    public ResponseEntity<Iterable<Department>> getAll() {
        var departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getOne(@PathVariable Long id) {
        var department = departmentService.getDepartmentById(id);
        if (department == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(department);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Department> delete(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

}
