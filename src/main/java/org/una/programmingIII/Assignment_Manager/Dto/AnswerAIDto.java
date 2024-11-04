package org.una.programmingIII.Assignment_Manager.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class AnswerAIDto {
    private Long id;
    private String feedback;
    private float grade;
}
