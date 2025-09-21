package com.san.sas.student; // Or your appropriate package

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository // Marks this interface as a Spring Data repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * By extending JpaRepository, you get all standard CRUD operations for free:
     * save(), findById(), findAll(), deleteById(), etc.
     */

    /**
     * Custom query method to find students by name.
     * Spring Data JPA automatically creates the database query based on the method name.
     * This will find any student whose name contains the keyword, ignoring case.
     */
    List<Student> findByNameContainingIgnoreCase(String keyword);
 
    Student findByEmail(String email);
    List<Student> findByDepartment(String department);

}
