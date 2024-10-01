package org.una.programmingIII.Assignment_Manager.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.una.programmingIII.Assignment_Manager.Model.PermissionType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String identificationNumber;
    private PermissionType role;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;
}

