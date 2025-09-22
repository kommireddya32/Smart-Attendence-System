package com.san.sas.student;

import com.san.sas.attendance.AttendanceRecord;
import com.san.sas.attendance.AttendanceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;
    
    @Autowired 
    private AttendanceService attendanceService;

    /**
     * Handles the student login form submission.
     */
    @PostMapping("/login-process")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        Student authenticatedStudent = studentService.authenticate(email, password);

        if (authenticatedStudent != null) {
            session.setAttribute("studentId", authenticatedStudent.getId());
            return "redirect:/student/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid ID/email or password");
            return "redirect:/StudentLogin";
        }
    }

    /**
     * Displays the dashboard and loads the student's attendance history.
     */
    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            return "redirect:/StudentLogin";
        }

        // Fetch the attendance history for the logged-in student
        List<AttendanceRecord> history = attendanceService.getHistoryForStudent(studentId);
        
        // Add the history to the model for the JSP to display
        model.addAttribute("attendanceHistory", history);
        
        return "student-dashboard.html";
    }

    /**
     * Handles the QR code scan data from the student's dashboard.
     * This version is updated to return a Map for safer JSON conversion.
     */
    @PostMapping("/mark-attendance")
    @ResponseBody
    public Map<String, Object> markAttendance(@RequestBody QrDataRequest request, HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            return Map.of("success", false, "message", "Not logged in.");
        }
        
        String resultMessage = attendanceService.markAttendance(studentId, request.getQrData());

        if (resultMessage.contains("Successfully")) {
            return Map.of("success", true, "message", resultMessage);
        } else {
            return Map.of("success", false, "message", resultMessage);
        }
    }
    
    // Simple class to accept the JSON from the frontend
    static class QrDataRequest {
        private String qrData;
        public String getQrData() { return qrData; }
        public void setQrData(String qrData) { this.qrData = qrData; }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/StudentLogin";
    }
 // Add these imports to your StudentController
    @RequestMapping("student-profile")
    public String studentprofile(){
    	return "student-profile.html";
    }
    // Add this method inside your StudentController class
    @GetMapping("/history")
    @ResponseBody // This is crucial - it tells Spring to return JSON
    public List<AttendanceRecord> getAttendanceHistory(HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            // Return an empty list if not logged in
            return List.of(); 
        }
        // This calls the service method we already created
        return attendanceService.getHistoryForStudent(studentId);
    }
    @GetMapping("/profile/data")
    @ResponseBody
    public Student getProfileData(HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            return null; // Or handle error appropriately
        }
        return studentService.getStudentById(studentId);
    }

    // 2. POST endpoint to handle the profile update
    @PostMapping("/profile/update")
    @ResponseBody
    public Map<String, Object> updateProfile(@RequestBody ProfileUpdateRequest request, HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            return Map.of("success", false, "message", "Not logged in.");
        }

        try {
            boolean success = studentService.updateProfile(studentId, request.getName(), request.getCurrentPassword(), request.getNewPassword());
            if (success) {
                return Map.of("success", true, "message", "Profile updated successfully!");
            }
        } catch (Exception e) {
            return Map.of("success", false, "message", e.getMessage());
        }
        return Map.of("success", false, "message", "An unknown error occurred.");
    }
    static class ProfileUpdateRequest {
        private String name;
        private String currentPassword;
        private String newPassword;
        // Add getters and setters for all fields
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}