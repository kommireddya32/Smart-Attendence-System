package com.san.sas.faculty;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    /**
     * By extending JpaRepository, you get all standard CRUD operations for free:
     * save(), findById(), findAll(), deleteById(), etc.
     */

    /**
     * Custom query method to find students by name.
     * Spring Data JPA automatically creates the database query based on the method name.
     * This will find any student whose name contains the keyword, ignoring case.
     */
    List<Faculty> findByNameContainingIgnoreCase(String keyword);
    Faculty findByEmail(String email);

}