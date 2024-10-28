package org.una.programmingIII.Assignment_Manager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.una.programmingIII.Assignment_Manager.Model.Assignment;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Assignment findAllByCourseId(Long courseId);
    Assignment findByCourseIdAndAddress(Long courseId, String address);
}
