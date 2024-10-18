package org.una.programmingIII.Assignment_Manager.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.una.programmingIII.Assignment_Manager.Model.AssignmentType;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDto {

    private Long id;
    private String title;
    private AssignmentType type;
    private String description;
    private LocalDate dueDate;
    private CourseDto course;
    private List<FileDto> files;
}

