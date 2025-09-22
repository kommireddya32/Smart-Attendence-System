package com.san.sas;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {

    // Serves the main homepage (index.html is served automatically from /static)
    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    // Serves the admin login JSP page
    @GetMapping("/admin")
    public String adminLoginPage() {
        return "admin_login"; // Logical name for admin_login.jsp
    }

    // NOTE: Spring Boot serves faculty_login.html and student_login.html
    // automatically from the /static folder, so no controller mappings are needed for them.
}