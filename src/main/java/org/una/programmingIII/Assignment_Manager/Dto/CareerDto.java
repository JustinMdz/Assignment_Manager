package org.una.programmingIII.Assignment_Manager.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerDto {
    private Long id;
    private String name;
    private String description;
    private Long departmentId;
    private List<CourseDto> courses;
    private List<UserDto> users;
}
