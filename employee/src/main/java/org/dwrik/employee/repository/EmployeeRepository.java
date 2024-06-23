package org.dwrik.employee.repository;

import org.dwrik.employee.entity.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    Iterable<Employee> findByDepartmentId(Long departmentId);

    void deleteByDepartmentId(Long departmentId);

}
