package org.una.programmingIII.Assignment_Manager.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
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
