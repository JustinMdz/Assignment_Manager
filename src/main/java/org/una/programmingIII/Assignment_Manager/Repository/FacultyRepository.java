package org.una.programmingIII.Assignment_Manager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.una.programmingIII.Assignment_Manager.Model.Department;
import org.una.programmingIII.Assignment_Manager.Model.Faculty;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
}