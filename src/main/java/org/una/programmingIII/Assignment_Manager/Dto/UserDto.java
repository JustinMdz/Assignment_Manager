package org.una.programmingIII.Assignment_Manager.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String identificationNumber;
    private boolean isActive;
    private Set<PermissionDto> permissions;
    private Long careerId;
    private List<CourseDto> courses;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;
}

