package org.una.programmingIII.Assignment_Manager.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @OneToMany(mappedBy = "submission")
    private List<File> files;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "reviewed_by", nullable = true)
    private User reviewedBy;
    private Double grade;
    private String feedback;
    private LocalDateTime reviewedAt;
}
