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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FrontController {

    private final FacultyService facultyService;

    @Autowired
    private AdminServent as; // Your Admin Service

    // TODO: Autowire your StudentService and FacultyService here
    // @Autowired
    // private StudentService studentService;

    // --- Public Page Mappings ---
    @Autowired
    private StudentService studentService;

    FrontController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
    
    @RequestMapping("/admin") // Changed to GetMapping for clarity
    public String adminLoginPage() {
        return "admin_login.jsp";
    }

    @RequestMapping("/FacultyLogin")
    public String facultyLogin() {
        return "faculty_login.html";
    }

    @RequestMapping("/StudentLogin")
    public String studentLogin() {
        return "student_login.html";
    }

    // --- Admin Logic ---

    /**
     * Handles the admin login form submission. Renamed from /IsAdmin.
     */
    @PostMapping("/admin/login") // Use POST for login actions
    public String isAdmin(@RequestParam String email, 
                          @RequestParam String password, 
                          HttpSession session, 
                          RedirectAttributes redirectAttributes) {

        if (as.isAdmin(email, password)) {
            // If login is successful, create a session to remember the user
            session.setAttribute("adminUser", email);
            // Redirect to the dashboard URL to prevent form resubmission on refresh
            return "redirect:/admin/dashboard";
        } else {
            // If login fails, add an error message and redirect back
            redirectAttributes.addFlashAttribute("error", "Incorrect Email or Password, please try again");
            return "redirect:/admin";
        }
    }

    /**
     * Displays the main admin dashboard page.
     */
    @GetMapping("/admin/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        // SECURITY CHECK: Is the admin user logged in?
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin"; // If not, send them to the login page
        }

        // TODO: Get real data from your services
         model.addAttribute("totalStudents", studentService.getStudentCount());
         model.addAttribute("totalFaculty", facultyService.getFacultyCount());
        
        return "admin_dashboard.jsp";
    }

    /**
     * Displays the "Manage Students" page with a list of all students.
     */
 // Inside FrontController.java

    @GetMapping("/admin/students")
    public String showManageStudents(@RequestParam(required = false) String keyword, Model model, HttpSession session) {
        // SECURITY CHECK: Is the admin user logged in?
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin";
        }
        
        // Check if a search keyword was provided
        if (keyword != null && !keyword.isEmpty()) {
            // If yes, call the search service method
            model.addAttribute("students", studentService.searchStudents(keyword));
        } else {
            // If no keyword, get all students
            model.addAttribute("students", studentService.getAllStudents());
        }

        return "manage-students.jsp";
    }
    
    // TODO: Add other methods here for deleting/adding students, managing faculty, etc.
    
 // Inside your FrontController.java

 // 1. This method shows the "Add New Student" page/form.
 @GetMapping("/admin/students/add")
 public String showAddStudentForm(HttpSession session) {
     // Security Check
     if (session.getAttribute("adminUser") == null) {
         return "redirect:/admin";
     }
     return "add-student.jsp";
 }

 // 2. This method processes the form submission.
 @PostMapping("/admin/students/add")
 public String saveStudent(Student student, HttpSession session) {
     // Security Check
     if (session.getAttribute("adminUser") == null) {
         return "redirect:/admin";
     }
     
     // Spring automatically creates a Student object from the form fields.
     // Make sure you have a StudentService to handle saving.
     studentService.createStudent(student); 
     
     return "redirect:/admin/students"; // Go back to the student list
 }
//Inside your FrontController.java

@PostMapping("/admin/students/delete")
public String deleteStudent(@RequestParam Long id, HttpSession session) {
  // Security Check
  if (session.getAttribute("adminUser") == null) {
      return "redirect:/admin";
  }

  studentService.deleteStudent(id); // Assumes this method exists in your StudentService
  
  return "redirect:/admin/students"; // Redirect back to the student list
}
//Inside your FrontController.java

@GetMapping("/admin/students/edit")
public String showEditStudentForm(@RequestParam Long id, Model model, HttpSession session) {
 if (session.getAttribute("adminUser") == null) {
     return "redirect:/admin";
 }

 // You will need a 'getStudentById' method in your StudentService
 Student student = studentService.getStudentById(id); 
 model.addAttribute("student", student);
 
 return "edit-student.jsp";
}
//Inside your FrontController.java

@PostMapping("/admin/students/update")
public String updateStudent(Student student, HttpSession session) {
 if (session.getAttribute("adminUser") == null) {
     return "redirect:/admin";
 }

 // You will need an 'updateStudent' method in your StudentService
 studentService.updateStudent(student);
 
 return "redirect:/admin/students";
}
/**
 * Displays the "Manage Faculty" page with a list of all faculty.
 */
//Inside FrontController.java

@GetMapping("/admin/faculty")
public String showManageFaculty(@RequestParam(required = false) String keyword, Model model, HttpSession session) {
 // SECURITY CHECK: Is the admin user logged in?
 if (session.getAttribute("adminUser") == null) {
     return "redirect:/admin";
 }

 // Check if a search keyword was provided
 if (keyword != null && !keyword.isEmpty()) {
     // If yes, call the search service method
     model.addAttribute("facultyList", facultyService.searchFaculty(keyword));
 } else {
     // If no keyword, get all faculty
     model.addAttribute("facultyList", facultyService.getAllFaculty());
 }

 return "manage-faculty.jsp";
}

// 1. This method shows the "Add New Faculty" page/form.
@GetMapping("/admin/faculty/add")
public String showAddFacultyForm(HttpSession session) {
    // Security Check
    if (session.getAttribute("adminUser") == null) {
        return "redirect:/admin";
    }
    return "add-faculty.jsp";
}

// 2. This method processes the form submission.
@PostMapping("/admin/faculty/add")
public String saveFaculty(Faculty faculty, HttpSession session) {
    // Security Check
    if (session.getAttribute("adminUser") == null) {
        return "redirect:/admin";
    }

    // Spring automatically creates a Faculty object from the form fields.
    facultyService.createFaculty(faculty);

    return "redirect:/admin/faculty"; // Go back to the faculty list
}

// Deletes a faculty member
@PostMapping("/admin/faculty/delete")
public String deleteFaculty(@RequestParam Long id, HttpSession session) {
    // Security Check
    if (session.getAttribute("adminUser") == null) {
        return "redirect:/admin";
    }

    facultyService.deleteFaculty(id);

    return "redirect:/admin/faculty"; // Redirect back to the faculty list
}

// Shows the edit form for a faculty member
@GetMapping("/admin/faculty/edit")
public String showEditFacultyForm(@RequestParam Long id, Model model, HttpSession session) {
    if (session.getAttribute("adminUser") == null) {
        return "redirect:/admin";
    }

    Faculty faculty = facultyService.getFacultyById(id);
    model.addAttribute("faculty", faculty);

    return "edit-faculty.jsp";
}

// Processes the update form submission
@PostMapping("/admin/faculty/update")
public String updateFaculty(Faculty faculty, HttpSession session) {
    if (session.getAttribute("adminUser") == null) {
        return "redirect:/admin";
    }

    facultyService.updateFaculty(faculty);

    return "redirect:/admin/faculty";
}
//Inside your FrontController.java

@GetMapping("/logout")
public String logout(HttpSession session) {
 // Invalidate the session to log the user out
 session.invalidate();
 
 // Redirect to the admin login page
 return "redirect:/admin";
}

}