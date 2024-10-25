package org.una.programmingIII.Assignment_Manager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.una.programmingIII.Assignment_Manager.Model.CourseContent;

@Repository
public interface CourseContentRepository extends JpaRepository<CourseContent, Long> {
    CourseContent findAllByCourseId(Long courseId);
}

