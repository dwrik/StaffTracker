package org.dwrik.department.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepartmentController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from the department service!";
    }

}
