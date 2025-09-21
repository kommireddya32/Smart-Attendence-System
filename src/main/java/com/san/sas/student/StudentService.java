package com.san.sas.student; // Or your appropriate package

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service // Marks this as a Spring service component
public class StudentService {

    @Autowired // Injects the StudentRepository so we can use it
    private StudentRepository studentRepository;

    /**
     * Saves a new student to the database.
     * This is the method called when the admin adds a new student.
     */
    public void createStudent(Student student) {
        // If you were hashing passwords, you would do it here before saving.
        studentRepository.save(student);
    }

    /**
     * Retrieves a list of all students from the database.
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Searches for students whose name contains the keyword.
     */
    public List<Student> searchStudents(String keyword) {
        return studentRepository.findByNameContainingIgnoreCase(keyword);
    }

    /**
     * Deletes a student by their unique ID.
     */
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    /**
     * Gets the total count of students for the dashboard.
     */
    public long getStudentCount() {
        return studentRepository.count();
    }

    public Student getStudentById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            return student.get();
        } else {
            throw new RuntimeException("Student not found for id :: " + id);
        }
    }

    /**
     * Updates an existing student's details.
     */
    public void updateStudent(Student studentFromForm) {
        // Find the existing student from the database
        Student existingStudent = getStudentById(studentFromForm.getId());

        // Update the fields with the new values from the form
        existingStudent.setName(studentFromForm.getName());
        existingStudent.setEmail(studentFromForm.getEmail());
        existingStudent.setDepartment(studentFromForm.getDepartment());

        // IMPORTANT: Only update the password if a new one was actually entered
        if (studentFromForm.getPassword() != null && !studentFromForm.getPassword().isEmpty()) {
            existingStudent.setPassword(studentFromForm.getPassword());
        }

        // Save the updated student object back to the database
        studentRepository.save(existingStudent);
    }
    public Student authenticate(String email, String password) {
        Student student = studentRepository.findByEmail(email);

        // Check if a student with that email exists and if the password matches
        if (student != null && student.getPassword().equals(password)) {
            return student; // Login successful
        }

        return null; // Login failed
    }
    public boolean updateProfile(Long studentId, String name, String currentPassword, String newPassword) throws Exception {
        // 1. Fetch the student record from the database
        Student student = getStudentById(studentId);

        // 2. Update the name
        student.setName(name);

        // 3. Check if the user wants to update their password
        if (newPassword != null && !newPassword.isEmpty()) {
            // 4. First, verify their current password is correct
            if (!student.getPassword().equals(currentPassword)) {
                throw new Exception("Incorrect current password.");
            }
            // 5. If it's correct, set the new password
            student.setPassword(newPassword);
        }

        // 6. Save the updated student object
        studentRepository.save(student);
        return true;
    }
}
