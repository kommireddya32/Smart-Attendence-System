package com.san.sas.admin;

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
@RequestMapping("/admin") // All URLs in this class start with /admin
public class AdminController {

    private final AdminServent adminService;
    private final StudentService studentService;
    private final FacultyService facultyService;

    // A single constructor for all dependencies (best practice)
    @Autowired
    public AdminController(AdminServent adminService, StudentService studentService, FacultyService facultyService) {
        this.adminService = adminService;
        this.studentService = studentService;
        this.facultyService = facultyService;
    }

    // --- Login, Dashboard, Logout ---

    @PostMapping("/login")
    public String processLogin(@RequestParam String email, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes) {
        if (adminService.isAdmin(email, password)) {
            session.setAttribute("adminUser", email);
            return "redirect:/admin/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Incorrect Email or Password");
            return "redirect:/admin";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        
        model.addAttribute("totalStudents", studentService.getStudentCount());
        model.addAttribute("totalFaculty", facultyService.getFacultyCount());
        
        return "admin_dashboard"; // Renders admin_dashboard.jsp
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin";
    }

    // --- Student Management by Admin ---

    @GetMapping("/students")
    public String showManageStudents(@RequestParam(required = false) String keyword, Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("students", studentService.searchStudents(keyword));
        } else {
            model.addAttribute("students", studentService.getAllStudents());
        }
        return "manage-students";
    }

    @GetMapping("/students/add")
    public String showAddStudentForm(HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        return "add-student";
    }

    @PostMapping("/students/add")
    public String saveStudent(Student student, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        studentService.createStudent(student);
        return "redirect:/admin/students";
    }

    @GetMapping("/students/edit")
    public String showEditStudentForm(@RequestParam Long id, Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        model.addAttribute("student", studentService.getStudentById(id));
        return "edit-student";
    }

    @PostMapping("/students/update")
    public String updateStudent(Student student, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        studentService.updateStudent(student);
        return "redirect:/admin/students";
    }

    @PostMapping("/students/delete")
    public String deleteStudent(@RequestParam Long id, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        studentService.deleteStudent(id);
        return "redirect:/admin/students";
    }
    
    // --- Faculty Management by Admin ---

    @GetMapping("/faculty")
    public String showManageFaculty(@RequestParam(required = false) String keyword, Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";

        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("facultyList", facultyService.searchFaculty(keyword));
        } else {
            model.addAttribute("facultyList", facultyService.getAllFaculty());
        }
        return "manage-faculty";
    }
    
    @GetMapping("/faculty/add")
    public String showAddFacultyForm(HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        return "add-faculty";
    }

    @PostMapping("/faculty/add")
    public String saveFaculty(Faculty faculty, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        facultyService.createFaculty(faculty);
        return "redirect:/admin/faculty";
    }

    @GetMapping("/faculty/edit")
    public String showEditFacultyForm(@RequestParam Long id, Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        model.addAttribute("faculty", facultyService.getFacultyById(id));
        return "edit-faculty";
    }

    @PostMapping("/faculty/update")
    public String updateFaculty(Faculty faculty, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        facultyService.updateFaculty(faculty);
        return "redirect:/admin/faculty";
    }

    @PostMapping("/faculty/delete")
    public String deleteFaculty(@RequestParam Long id, HttpSession session) {
        if (session.getAttribute("adminUser") == null) return "redirect:/admin";
        facultyService.deleteFaculty(id);
        return "redirect:/admin/faculty";
    }
}