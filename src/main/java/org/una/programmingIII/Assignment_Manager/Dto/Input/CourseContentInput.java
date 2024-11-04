package org.una.programmingIII.Assignment_Manager.Dto.Input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager.Dto.FileDto;
import org.una.programmingIII.Assignment_Manager.Model.Course;

import java.util.List;
 @Data
 @NoArgsConstructor
 @AllArgsConstructor
public class CourseContentInput {
    private Long id;
    private String address;
    private Long courseId;
   // private List<FileDto> files;
}
