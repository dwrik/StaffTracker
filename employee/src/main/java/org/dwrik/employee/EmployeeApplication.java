package org.dwrik.employee;

import org.dwrik.employee.entity.Employee;
import org.dwrik.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class EmployeeApplication implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;

    public static void main(String[] args) {
        SpringApplication.run(EmployeeApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // pre-populating the database
        employeeRepository.saveAll(List.of(
                new Employee(1L, 1L, "Jim"),
                new Employee(2L, 1L, "Dwight"),
                new Employee(3L, 1L, "Stanley"),
                new Employee(4L, 1L, "Phyllis"),
                new Employee(5L, 2L, "Kevin"),
                new Employee(6L, 2L, "Angela"),
                new Employee(7L, 2L, "Oscar"),
                new Employee(8L, 3L, "Creed"),
                new Employee(9L, 4L, "Meredith"),
                new Employee(10L, 5L, "Kelly"),
                new Employee(11L, 6L, "Toby")
        ));
    }

}
