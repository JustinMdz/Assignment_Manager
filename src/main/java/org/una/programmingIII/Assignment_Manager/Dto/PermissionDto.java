package org.una.programmingIII.Assignment_Manager.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager.Model.PermissionType;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PermissionDto {
    private Long id;
    private PermissionType name;
}
