package org.dwrik.department.service;

import org.dwrik.department.entity.Department;
import org.dwrik.department.exception.DepartmentAlreadyExistsException;
import org.dwrik.department.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public Department createDepartment(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new DepartmentAlreadyExistsException("department with name '" + department.getName() + "' already exists");
        }
        department.setId(null);
        return departmentRepository.save(department);
    }

    @Transactional(readOnly = true)
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Iterable<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Transactional
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

}
