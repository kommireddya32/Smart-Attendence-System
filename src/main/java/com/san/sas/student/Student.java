package com.san.sas.student; // Or your appropriate package

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // Marks this class as a JPA entity (a database table)
@Table(name = "students") // Specifies the table name in the database
public class Student {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configures the ID to be auto-incremented
    private Long id;

    @Column(nullable = false) // Marks this column as non-nullable
    private String name;

    @Column(nullable = false, unique = true) // Email should be required and unique
    private String email;

    @Column(nullable = false)
    private String password;

    private String department;

    // A no-argument constructor is required by JPA
    public Student() {
    }

    // Constructor for creating new students
    public Student(String name, String email, String password, String department) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.department = department;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}