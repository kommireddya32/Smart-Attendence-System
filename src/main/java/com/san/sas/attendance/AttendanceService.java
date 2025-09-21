package com.san.sas.attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.san.sas.dto.StudentAttendanceStatusDto;
import com.san.sas.faculty.Faculty;
import com.san.sas.faculty.FacultyRepository;
import com.san.sas.student.Student;
import com.san.sas.student.StudentRepository;

@Service
public class AttendanceService {

    @Autowired private StudentRepository studentRepository;
    @Autowired private FacultyRepository facultyRepository;
    @Autowired private AttendanceRecordRepository attendanceRecordRepository;

    public String markAttendance(Long studentId, String scannedQrData) {
        try {
            String[] qrParts = scannedQrData.split("_");
            if (qrParts.length < 2) {
                return "Error: Invalid QR Code format.";
            }
            Long facultyId = Long.parseLong(qrParts[0]);
            String qrDepartment = qrParts[1];
            
            Student student = studentRepository.findById(studentId).orElse(null);
            Faculty faculty = facultyRepository.findById(facultyId).orElse(null);

            if (student == null || faculty == null) {
                return "Error: Invalid user data.";
            }

            if (!scannedQrData.equals(faculty.getQrData())) {
                return "Invalid or Expired QR Code. Please try again.";
            }

            if (!student.getDepartment().equalsIgnoreCase(qrDepartment)) {
                return "Attendance failed: You are not in the correct department for this class.";
            }

            // --- THIS IS THE UPDATED LINE ---
            // It now calls the new, more reliable query method.
            boolean alreadyMarked = attendanceRecordRepository.hasAttendedToday(student, faculty);

            if (alreadyMarked) {
                return "You have already marked attendance for this faculty today.";
            }

            AttendanceRecord record = new AttendanceRecord();
            record.setStudent(student);
            record.setFaculty(faculty);
            attendanceRecordRepository.save(record);

            return "Attendance Marked Successfully!";

        } catch (Exception e) {
            e.printStackTrace(); // Good for seeing errors in your server console
            return "Error: An unexpected error occurred.";
        }
    }
 // Inside AttendanceService.java

    public List<AttendanceRecord> getHistoryForStudent(Long studentId) {
        // This uses the findByStudentId method we already created in the repository
        return attendanceRecordRepository.findByStudentId(studentId);
    }
    
    public List<StudentAttendanceStatusDto> getAttendanceStatusForClass(Long facultyId, String department, LocalDate date) {
        // 1. Get all students in the selected department
        List<Student> allStudentsInDept = studentRepository.findByDepartment(department);

        // 2. Get attendance records for this faculty on the selected date
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        List<AttendanceRecord> presentRecords = attendanceRecordRepository.findByFacultyIdAndAttendanceDateBetween(facultyId, startOfDay, endOfDay);
        
        // 3. Create a quick lookup Set of IDs for students who were present
        Set<Long> presentStudentIds = presentRecords.stream()
                                        .map(record -> record.getStudent().getId())
                                        .collect(Collectors.toSet());

        // 4. Compare the full student list with the present list to determine status
        List<StudentAttendanceStatusDto> attendanceList = new ArrayList<>();
        for (Student student : allStudentsInDept) {
            if (presentStudentIds.contains(student.getId())) {
                attendanceList.add(new StudentAttendanceStatusDto(student.getId(), student.getName(), "Present"));
            } else {
                attendanceList.add(new StudentAttendanceStatusDto(student.getId(), student.getName(), "Absent"));
            }
        }
        
        return attendanceList;
    }
    public List<AttendanceRecord> getHistoryForFaculty(Long facultyId) {
        // This uses the findByFacultyId method from your repository
        return attendanceRecordRepository.findByFacultyId(facultyId);
    }
    
}