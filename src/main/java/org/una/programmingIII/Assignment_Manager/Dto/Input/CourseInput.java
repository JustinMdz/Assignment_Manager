package org.una.programmingIII.Assignment_Manager.Dto.Input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager.Dto.UserDto;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseInput {
    private Long id;
    private String name;
    private String description;
    private UserDto professor;
    private Long departmentId;
    private LocalDate startDate;
    private LocalDate endDate;
}
