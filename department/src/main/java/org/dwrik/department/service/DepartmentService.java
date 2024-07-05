package org.dwrik.department.service;

import lombok.extern.slf4j.Slf4j;
import org.dwrik.department.entity.Department;
import org.dwrik.department.exception.DepartmentAlreadyExistsException;
import org.dwrik.department.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DepartmentService {

    private final StreamBridge streamBridge;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(StreamBridge streamBridge, DepartmentRepository departmentRepository) {
        this.streamBridge = streamBridge;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public Department createDepartment(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            log.info("Department with name '{}' already exists, skipping creation...", department.getName());
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
        streamBridge.send("departmentDeletedEvents", id);
        log.info("Deletion of employees with department id '{}' has been initiated...", id);
    }

}
