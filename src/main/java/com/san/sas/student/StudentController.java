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

    /** Handles the student login form submission. */
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
            return "redirect:/StudentLogin"; // public login page
        }
    }

    /** Displays the dashboard and loads the student's attendance history. */
    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            return "redirect:/StudentLogin";
        }

        List<AttendanceRecord> history = attendanceService.getHistoryForStudent(studentId);
        model.addAttribute("attendanceHistory", history);

        // points to /WEB-INF/views/student/dashboard.jsp
        return "student/dashboard";
    }

    /** Handles the QR code scan data from the student's dashboard. */
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

    /** Simple class to accept the JSON from the frontend. */
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

    /** Show profile page. */
    @GetMapping("/profile")
    public String studentProfile() {
        // points to /WEB-INF/views/student/profile.jsp
        return "student/profile";
    }

    /** Return attendance history as JSON. */
    @GetMapping("/history")
    @ResponseBody
    public List<AttendanceRecord> getAttendanceHistory(HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            return List.of();
        }
        return attendanceService.getHistoryForStudent(studentId);
    }

    /** Fetch profile data as JSON. */
    @GetMapping("/profile/data")
    @ResponseBody
    public Student getProfileData(HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            return null;
        }
        return studentService.getStudentById(studentId);
    }

    /** Update profile data. */
    @PostMapping("/profile/update")
    @ResponseBody
    public Map<String, Object> updateProfile(@RequestBody ProfileUpdateRequest request,
                                             HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            return Map.of("success", false, "message", "Not logged in.");
        }

        try {
            boolean success = studentService.updateProfile(
                    studentId,
                    request.getName(),
                    request.getCurrentPassword(),
                    request.getNewPassword()
            );
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

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}
