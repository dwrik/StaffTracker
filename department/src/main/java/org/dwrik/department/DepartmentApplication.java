package org.dwrik.department;

import org.dwrik.department.entity.Department;
import org.dwrik.department.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class DepartmentApplication implements CommandLineRunner {

    @Autowired
    private DepartmentRepository departmentRepository;

    public static void main(String[] args) {
        SpringApplication.run(DepartmentApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // pre-populating the database
        departmentRepository.saveAll(List.of(
                new Department(1L, "Sales"),
                new Department(2L, "Accounting"),
                new Department(3L, "Quality Assurance"),
                new Department(4L, "Customer Relations"),
                new Department(5L, "Customer Service"),
                new Department(6L, "HR")
        ));
    }

}
