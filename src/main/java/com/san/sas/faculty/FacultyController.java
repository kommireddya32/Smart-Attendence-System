package com.san.sas.faculty;

import com.google.zxing.WriterException;
import com.san.sas.attendance.AttendanceRecord;
import com.san.sas.attendance.AttendanceService;
import com.san.sas.dto.StudentAttendanceStatusDto;
import com.san.sas.util.QRCodeGenerator;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/faculty")
public class FacultyController {

    // --- UPDATED: Standardized Dependency Injection ---
    private final FacultyService facultyService;
    private final AttendanceService attendanceService;

    @Autowired
    public FacultyController(FacultyService facultyService, AttendanceService attendanceService) {
        this.facultyService = facultyService;
        this.attendanceService = attendanceService;
    }

    // --- Login and Dashboard ---

    @PostMapping("/login")
    public String processLogin(@RequestParam String email, 
                               @RequestParam String password, 
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        Faculty authenticatedFaculty = facultyService.authenticate(email, password);

        if (authenticatedFaculty != null) {
            session.setAttribute("facultyId", authenticatedFaculty.getId());
            return "redirect:/faculty/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/FacultyLogin";
        }
    }

    // --- UPDATED: Now loads history data for the dashboard ---
    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        Long facultyId = (Long) session.getAttribute("facultyId");
        if (facultyId == null) {
            return "redirect:/FacultyLogin";
        }
        
        // Fetch the session history for the logged-in faculty
        List<AttendanceRecord> previousSessions = attendanceService.getHistoryForFaculty(facultyId);
        
        // Add the history to the model for the JSP to use
        model.addAttribute("previousSessions", previousSessions);

        return "faculty-dashboard.jsp";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/FacultyLogin";
    }

    // --- Profile Management ---

    @GetMapping("/profile")
    public String showProfilePage(Model model, HttpSession session) {
        Long facultyId = (Long) session.getAttribute("facultyId");
        if (facultyId == null) {
            return "redirect:/FacultyLogin";
        }
        
        Faculty faculty = facultyService.getFacultyById(facultyId);
        model.addAttribute("faculty", faculty);
        
        return "faculty-profile.jsp";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String name,
                                @RequestParam String department,
                                @RequestParam String currentPassword,
                                @RequestParam String newPassword,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
                                    
        Long facultyId = (Long) session.getAttribute("facultyId");
        if (facultyId == null) {
            return "redirect:/FacultyLogin";
        }

        try {
            boolean success = facultyService.updateProfile(facultyId, name, department, currentPassword, newPassword);
            if (success) {
                redirectAttributes.addFlashAttribute("success", true);
                redirectAttributes.addFlashAttribute("message", "Profile updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        
        return "redirect:/faculty/profile";
    }



    // --- QR Code API ---

    @GetMapping(value = "/api/qrcode/generate", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] generateQrCode(@RequestParam String department, HttpSession session) throws WriterException, IOException {
        
        Long facultyId = (Long) session.getAttribute("facultyId");
        if (facultyId == null) {
            return null;
        }

        String qrDataString = facultyService.generateAndSaveQrData(facultyId, department);
        return QRCodeGenerator.generateQRCodeImage(qrDataString, 250, 250);
    }
    @GetMapping("/history-page")
    public String showHistoryPage(HttpSession session) {
        if (session.getAttribute("facultyId") == null) {
            return "redirect:/FacultyLogin";
        }
        return "faculty-history.html";
    }

    // This API endpoint returns the attendance data as JSON
    @GetMapping("/history")
    @ResponseBody
    public List<StudentAttendanceStatusDto> getAttendanceHistory(
            @RequestParam String department,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, // <-- THIS IS THE FIX
            HttpSession session) {
        
        Long facultyId = (Long) session.getAttribute("facultyId");
        if (facultyId == null) {
            return List.of(); 
        }
        return attendanceService.getAttendanceStatusForClass(facultyId, department, date);
    }
}