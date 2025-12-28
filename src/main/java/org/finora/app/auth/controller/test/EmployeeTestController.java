package org.finora.app.auth.controller.test;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/employee")
public class EmployeeTestController {

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String getEmployeeAccess() {
        return "Employee Access Granted";
    }
}
