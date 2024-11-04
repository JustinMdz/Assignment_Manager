package org.una.programmingIII.Assignment_Manager.Dto.Input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager.Dto.FileDto;
import org.una.programmingIII.Assignment_Manager.Model.AssignmentType;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentInput {
    private Long id;
    private String title;
    private AssignmentType type;
    private String description;
    private String address;
    private LocalDate dueDate;
    private Long courseId;
    private List<FileDto> files;
}
