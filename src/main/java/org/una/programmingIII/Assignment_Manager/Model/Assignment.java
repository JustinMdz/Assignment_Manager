package org.una.programmingIII.Assignment_Manager.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Table(name = "assignments")
@Data
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private AssignmentType type;

    private String description;

    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
