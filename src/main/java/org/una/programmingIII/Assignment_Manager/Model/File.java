package org.una.programmingIII.Assignment_Manager.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "original_name")
    private String originalName;
    private String name;
    private Long fileSize;
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "submission_id", nullable = true)
    private Submission submission;
    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = true)
    private Assignment assignment;
    @ManyToOne
    @JoinColumn(name = "course_content_id", nullable = true)
    private CourseContent courseContent;
}
