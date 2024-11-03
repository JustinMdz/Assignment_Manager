package org.una.programmingIII.Assignment_Manager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.una.programmingIII.Assignment_Manager.Model.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCareerId(Long careerId);

    @Query(value = "SELECT c.* FROM courses c " +
            "JOIN course_users cu ON c.id = cu.course_id " +
            "JOIN users u ON cu.user_id = u.id " +
            "WHERE u.id = :studentId",
            nativeQuery = true)
    List<Course> findCoursesEnrolledByStudentId(@Param("studentId") Long studentId);


    @Query(value = "SELECT c.* FROM courses c " +
            "LEFT JOIN course_users cu ON c.id = cu.course_id AND cu.user_id = :userId " +
            "WHERE c.career_id = :careerId AND cu.user_id IS NULL",
            nativeQuery = true)
    List<Course> findAvailableCoursesByCareerIdAndUserId(@Param("careerId") Long careerId, @Param("userId") Long userId);


    @Query(value = "SELECT c.* FROM courses c " +
            "LEFT JOIN course_users cu ON c.id = cu.course_id " +
            "AND cu.user_id = :studentId " +
            "WHERE cu.user_id IS NULL AND c.professor_id = :professorId",
            nativeQuery = true)

    List<Course> findAvailableCoursesByCareerIdUserIdAndProfessorId(@Param("professorId") Long professorId,@Param("studentId") Long studentId);

    @Query(value = "SELECT c.* FROM courses c JOIN course_users cu ON c.id = cu.course_id WHERE cu.user_id = :studentId AND c.professor_id = :professorId",
            nativeQuery = true)
    List<Course> findCoursesEnrolledByStudentIdAAndProfessorIs(@Param("professorId") Long professorId,@Param("studentId") Long studentId);


}