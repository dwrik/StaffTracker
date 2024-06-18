package org.dwrik.department.service;

import org.dwrik.department.entity.Department;
import org.dwrik.department.exception.DepartmentAlreadyExistsException;
import org.dwrik.department.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department createDepartment(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new DepartmentAlreadyExistsException("department with name '" + department.getName() + "' already exists");
        }
        department.setId(null);
        return departmentRepository.save(department);
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public Iterable<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

}
