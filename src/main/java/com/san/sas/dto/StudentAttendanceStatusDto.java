package com.san.sas.dto; // Create a new 'dto' package if you don't have one

public class StudentAttendanceStatusDto {
    private Long studentId;
    private String studentName;
    private String status;

    public StudentAttendanceStatusDto(Long studentId, String studentName, String status) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.status = status;
    }
    
    // Add Getters
    public Long getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getStatus() { return status; }
}