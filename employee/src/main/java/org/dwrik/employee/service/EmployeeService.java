package org.dwrik.employee.service;

import org.dwrik.employee.entity.Employee;
import org.dwrik.employee.exception.InvalidDepartmentException;
import org.dwrik.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
public class EmployeeService {

    private final RestClient restClient;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(RestClient.Builder restClientBuilder, EmployeeRepository employeeRepository) {
        this.restClient = restClientBuilder.build();
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Employee createEmployee(Employee employee) {
        Long departmentId = employee.getDepartmentId();

        restClient.get()
                .uri("lb://department/{departmentId}", departmentId)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new InvalidDepartmentException(
                            String.format("unable to confirm existence of department with id '%d', please try again later", departmentId)
                    );
                })
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new InvalidDepartmentException(
                            String.format("department with id '%d' does not exist", departmentId)
                    );
                })
                .toBodilessEntity();

        employee.setId(null);
        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Iterable<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Iterable<Employee> getEmployeesByDepartmentId(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    @Transactional
    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }

    @Transactional
    public void deleteEmployeesByDepartmentId(Long departmentId) {
        employeeRepository.deleteByDepartmentId(departmentId);
    }

}
