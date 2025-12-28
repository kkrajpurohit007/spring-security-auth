package org.finora.app.auth.controller.test;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/customer")
public class CustomerTestController {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public String getCustomerAccess() {
        return "Customer (User) Access Granted";
    }
}
