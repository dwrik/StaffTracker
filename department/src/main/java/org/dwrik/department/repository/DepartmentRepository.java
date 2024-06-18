package org.dwrik.department.repository;

import org.dwrik.department.entity.Department;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Long> {

    boolean existsByName(String name);

}
