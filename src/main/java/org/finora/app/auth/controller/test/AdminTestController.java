package org.finora.app.auth.controller.test;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/admin")
public class AdminTestController {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminAccess() {
        return "Admin Access Granted";
    }
}
