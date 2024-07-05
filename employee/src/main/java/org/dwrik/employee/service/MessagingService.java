package org.dwrik.employee.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
public class MessagingService {

    private final EmployeeService employeeService;

    @Autowired
    public MessagingService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Bean
    public Consumer<Long> performEmployeeDeletionBasedOnDepartment() {
        return id -> {
            log.info("Processing delete request for employees with department id '{}'...", id);
            employeeService.deleteEmployeesByDepartmentId(id);
        };
    }

}
