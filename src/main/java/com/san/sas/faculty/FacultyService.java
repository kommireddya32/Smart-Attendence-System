package com.san.sas.faculty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service // Marks this as a Spring service component
public class FacultyService {

    @Autowired // Injects the FacultyRepository so we can use it
    private FacultyRepository facultyRepository;

    /**
     * Saves a new faculty to the database.
     * This is the method called when the admin adds a new faculty.
     */
    public void createFaculty(Faculty faculty) {
        // If you were hashing passwords, you would do it here before saving.
        facultyRepository.save(faculty);
    }

    /**
     * Retrieves a list of all faculty from the database.
     */
    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    /**
     * Searches for faculty whose name contains the keyword.
     */
    public List<Faculty> searchFaculty(String keyword) {
        return facultyRepository.findByNameContainingIgnoreCase(keyword);
    }

    /**
     * Deletes a faculty by their unique ID.
     */
    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    /**
     * Gets the total count of faculty for the dashboard.
     */
    public long getFacultyCount() {
        return facultyRepository.count();
    }

    public Faculty getFacultyById(Long id) {
        Optional<Faculty> faculty = facultyRepository.findById(id);
        if (faculty.isPresent()) {
            return faculty.get();
        } else {
            throw new RuntimeException("Faculty not found for id :: " + id);
        }
    }
    public boolean updateProfile(Long facultyId, String name, String department, String currentPassword, String newPassword) throws Exception {
        Faculty faculty = getFacultyById(facultyId);

        // Update basic details
        faculty.setName(name);
        faculty.setDepartment(department);

        // Check if the user wants to update their password
        if (newPassword != null && !newPassword.isEmpty()) {
            // First, verify their current password is correct
            if (!faculty.getPassword().equals(currentPassword)) {
                throw new Exception("Incorrect current password.");
            }
            // If it's correct, set the new password
            faculty.setPassword(newPassword);
        }

        facultyRepository.save(faculty);
        return true;
    }

    /**
     * Updates an existing faculty's details.
     */
    public void updateFaculty(Faculty facultyFromForm) {
        // Find the existing Faculty from the database
        Faculty existingFaculty = getFacultyById(facultyFromForm.getId());

        // Update the fields with the new values from the form
        existingFaculty.setName(facultyFromForm.getName());
        existingFaculty.setEmail(facultyFromForm.getEmail());
        existingFaculty.setDepartment(facultyFromForm.getDepartment());

        // IMPORTANT: Only update the password if a new one was actually entered
        if (facultyFromForm.getPassword() != null && !facultyFromForm.getPassword().isEmpty()) {
            existingFaculty.setPassword(facultyFromForm.getPassword());
        }

        // Save the updated faculty object back to the database
        facultyRepository.save(existingFaculty);
    }
 // Inside FacultyService.java

    public String generateAndSaveQrData(Long facultyId, String selectedDepartment) {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        // ✅ The timestamp is correctly generated here, on the backend.
        long currentTimeInstance = System.currentTimeMillis();

        String qrData = faculty.getId() + "_" + selectedDepartment + "_" + currentTimeInstance;

        faculty.setQrData(qrData);
        facultyRepository.save(faculty);

        return qrData;
    }
    public Faculty authenticate(String email, String password) {
        // Find the faculty by their email address
        Faculty faculty = facultyRepository.findByEmail(email);

        // Check if faculty exists and if the password matches
        if (faculty != null && faculty.getPassword().equals(password)) {
            return faculty; // Credentials are correct
        }

        return null; // Credentials are incorrect
    }
}