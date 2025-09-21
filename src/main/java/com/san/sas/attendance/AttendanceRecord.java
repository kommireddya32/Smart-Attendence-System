package com.san.sas.attendance; // A new package for attendance-related classes

import com.san.sas.faculty.Faculty;
import com.san.sas.student.Student;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_records")
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // This creates a many-to-one relationship with the Student entity.
    // Many attendance records can belong to one student.
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // This creates a many-to-one relationship with the Faculty entity.
    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @Column(name = "attendance_date", nullable = false, updatable = false)
    private LocalDateTime attendanceDate;

    @Column(nullable = false)
    private String status;

    // This method automatically sets the date and status when a new record is created.
    @PrePersist
    protected void onCreate() {
        attendanceDate = LocalDateTime.now();
        status = "Present";
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public LocalDateTime getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDateTime attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}