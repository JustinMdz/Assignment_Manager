package org.una.programmingIII.Assignment_Manager.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "answers_ai")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class AnswerAI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String feedback;
   private float grade;
}
