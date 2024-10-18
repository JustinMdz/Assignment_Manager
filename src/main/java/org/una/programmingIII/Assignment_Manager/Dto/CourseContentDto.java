package org.una.programmingIII.Assignment_Manager.Dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import org.una.programmingIII.Assignment_Manager.Model.Course;
import org.una.programmingIII.Assignment_Manager.Model.File;

import java.time.LocalDate;
import java.util.List;

public class CourseContentDto {
    private Long id;
    private String title;
    private String description;
    private Course course;
    private List<FileDto> files;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
