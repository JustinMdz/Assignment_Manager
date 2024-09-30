package org.una.programmingIII.Assignment_Manager.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionDto {
    private Long id;
    private AssignmentDto assignment;
    private UserDto student;
    private List<FileDto> files;
    private UserDto reviewedBy;
    private Double grade;
    private String feedback;
    private LocalDateTime createdAt;
    private LocalDate reviewedAt;
}
