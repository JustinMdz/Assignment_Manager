package org.una.programmingIII.Assignment_Manager.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long id;
    private String name;
    private String description;
    private UserDto professor;
    private List<UserDto> students;
    private Long careerId;
    private LocalDate startDate;
    private LocalDate endDate;
}
