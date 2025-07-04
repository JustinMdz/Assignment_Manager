package org.una.programmingIII.Assignment_Manager.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    private Long id;
    private String provisionalName;
    private String name;
    private Long fileSize;
    private String filePath;
    private Long submissionId;
    private Long assignmentId;
    private Long courseContentId;

}
