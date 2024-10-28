package org.una.programmingIII.Assignment_Manager.Dto.Input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager.Dto.CourseDto;
import org.una.programmingIII.Assignment_Manager.Dto.PermissionDto;
import org.una.programmingIII.Assignment_Manager.Model.PermissionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInput {

    private Long id;
    private String name;
    private String email;
    private String identificationNumber;
    private String password;
    private boolean isActive;
    private Set<PermissionDto> permissions;
    private Long careerId;
    private List<CourseDto> courses;
}

