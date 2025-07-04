package org.una.programmingIII.Assignment_Manager.Dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacultyDto {

    private Long id;
    private String name;
    private List<DepartmentDto> departments;
    private Long universityId;
}

