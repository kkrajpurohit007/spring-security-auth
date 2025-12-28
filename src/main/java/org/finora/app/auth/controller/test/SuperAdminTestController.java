package org.finora.app.auth.controller.test;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/super-admin")
public class SuperAdminTestController {

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String getSuperAdminAccess() {
        return "Super Admin Access Granted";
    }
}
