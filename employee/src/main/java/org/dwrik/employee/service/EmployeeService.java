package org.dwrik.employee.service;

import lombok.extern.slf4j.Slf4j;
import org.dwrik.employee.entity.Employee;
import org.dwrik.employee.exception.InvalidDepartmentException;
import org.dwrik.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.io.IOException;

@Slf4j
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
        long startTime = System.currentTimeMillis();

        restClient.get()
                .uri("lb://department/{departmentId}", departmentId)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    long duration = System.currentTimeMillis() - startTime;
                    logAPIRequest(request, response, duration, "Server error when connecting with department service, skipping creation...");
                    throw new InvalidDepartmentException(
                            String.format("unable to confirm existence of department with id '%d', try again later", departmentId)
                    );
                })
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    long duration = System.currentTimeMillis() - startTime;
                    logAPIRequest(request, response, duration, "Unable to confirm existence of department with id '" + departmentId + "', skipping creation...");
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

    private void logAPIRequest(HttpRequest request, ClientHttpResponse response, long duration, String message) throws IOException {
        var path = "/" + request.getURI().getHost() + request.getURI().getPath();
        log.info("{} {} {} {} {} {}",
                fixedWidth(request.getMethod(), 6),
                fixedWidth(path, 20),
                fixedWidth("HTTP/1.1", 8),
                fixedWidth(response.getStatusCode().value(), 3),
                fixedWidth(duration + "ms", 6),
                message
        );
    }

    private static String fixedWidth(Object object, int width) {
        return String.format("%-" + width + "s", object);
    }

}
