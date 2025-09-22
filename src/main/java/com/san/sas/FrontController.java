package com.san.sas;

import com.san.sas.admin.AdminServent;
import com.san.sas.faculty.Faculty;
import com.san.sas.faculty.FacultyService;
import com.san.sas.student.Student;
import com.san.sas.student.StudentService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FrontController {

    private final FacultyService facultyService;
    private final StudentService studentService;
    private final AdminServent adminService;

    @Autowired
    public FrontController(FacultyService facultyService,
                           StudentService studentService,
                           AdminServent adminService) {
        this.facultyService = facultyService;
        this.studentService = studentService;
        this.adminService   = adminService;
    }

    /* ---------------------  PUBLIC PAGES (HTML in /static) --------------------- */

    // index.html
    @GetMapping("/")
    public String index() {
        return "index.html";   // served directly from /static
    }

    @GetMapping("/FacultyLogin")
    public String facultyLogin() {
        return "faculty_login.html";
    }

    @GetMapping("/StudentLogin")
    public String studentLogin() {
        return "student_login.html";
    }

    /* ---------------------  ADMIN LOGIN (JSP) --------------------- */

    @GetMapping("/admin")
    public String adminLoginPage() {
        return "admin_login";  // -> /WEB-INF/jsp/admin_login.jsp
    }

    @PostMapping("/admin/login")
    public String adminLogin(@RequestParam String email,
                             @RequestParam String password,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        if (adminService.isAdmin(email, password)) {
            session.setAttribute("adminUser", email);
            return "redirect:/admin/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "Incorrect Email or Password, please try again");
            return "redirect:/admin";
        }
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin";
        }
        model.addAttribute("totalStudents", studentService.getStudentCount());
        model.addAttribute("totalFaculty",  facultyService.getFacultyCount());
        return "admin_dashboard"; // -> /WEB-INF/jsp/admin_dashboard.jsp
    }

    /* ---------------------  STUDENT MANAGEMENT (JSP) --------------------- */

    @GetMapping("/admin/students")
    public String manageStudents(@RequestParam(required = false) String keyword,
                                 Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";

        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("students", studentService.searchStudents(keyword));
        } else {
            model.addAttribute("students", studentService.getAllStudents());
        }
        return "manage-students";
    }

    @GetMapping("/admin/students/add")
    public String addStudentForm(HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        return "add-student";
    }

    @PostMapping("/admin/students/add")
    public String saveStudent(Student student, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        studentService.createStudent(student);
        return "redirect:/admin/students";
    }

    @PostMapping("/admin/students/delete")
    public String deleteStudent(@RequestParam Long id, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        studentService.deleteStudent(id);
        return "redirect:/admin/students";
    }

    @GetMapping("/admin/students/edit")
    public String editStudentForm(@RequestParam Long id, Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        model.addAttribute("student", studentService.getStudentById(id));
        return "edit-student";
    }

    @PostMapping("/admin/students/update")
    public String updateStudent(Student student, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        studentService.updateStudent(student);
        return "redirect:/admin/students";
    }

    /* ---------------------  FACULTY MANAGEMENT (JSP) --------------------- */

    @GetMapping("/admin/faculty")
    public String manageFaculty(@RequestParam(required = false) String keyword,
                                Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";

        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("facultyList", facultyService.searchFaculty(keyword));
        } else {
            model.addAttribute("facultyList", facultyService.getAllFaculty());
        }
        return "manage-faculty";
    }

    @GetMapping("/admin/faculty/add")
    public String addFacultyForm(HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        return "add-faculty";
    }

    @PostMapping("/admin/faculty/add")
    public String saveFaculty(Faculty faculty, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        facultyService.createFaculty(faculty);
        return "redirect:/admin/faculty";
    }

    @PostMapping("/admin/faculty/delete")
    public String deleteFaculty(@RequestParam Long id, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        facultyService.deleteFaculty(id);
        return "redirect:/admin/faculty";
    }

    @GetMapping("/admin/faculty/edit")
    public String editFacultyForm(@RequestParam Long id, Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        model.addAttribute("faculty", facultyService.getFacultyById(id));
        return "edit-faculty";
    }

    @PostMapping("/admin/faculty/update")
    public String updateFaculty(Faculty faculty, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        facultyService.updateFaculty(faculty);
        return "redirect:/admin/faculty";
    }

    /* ---------------------  LOGOUT --------------------- */

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin";
    }
}
