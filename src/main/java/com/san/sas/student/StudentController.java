package com.san.sas.student;

import com.san.sas.attendance.AttendanceRecord;
import com.san.sas.attendance.AttendanceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final AttendanceService attendanceService;

    @Autowired
    public StudentController(StudentService studentService, AttendanceService attendanceService) {
        this.studentService = studentService;
        this.attendanceService = attendanceService;
    }

    // --- Login & Logout ---

    @PostMapping("/login-process")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        Student authenticatedStudent = studentService.authenticate(email, password);

        if (authenticatedStudent != null) {
            session.setAttribute("studentId", authenticatedStudent.getId());
            return "redirect:/student-dashboard.html";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid ID/email or password");
            return "redirect:/student_login.html";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/student_login.html";
    }

    // --- Student APIs for JavaScript ---

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

    @GetMapping("/history")
    @ResponseBody
    public List<AttendanceRecord> getAttendanceHistory(HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            return List.of(); 
        }
        return attendanceService.getHistoryForStudent(studentId);
    }
    
    @GetMapping("/profile/data")
    @ResponseBody
    public Student getProfileData(HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            return null;
        }
        return studentService.getStudentById(studentId);
    }

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

    // --- DTO Inner Classes ---

    static class QrDataRequest {
        private String qrData;
        public String getQrData() { return qrData; }
        public void setQrData(String qrData) { this.qrData = qrData; }
    }
    
    static class ProfileUpdateRequest {
        private String name;
        private String currentPassword;
        private String newPassword;
        // Add Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}