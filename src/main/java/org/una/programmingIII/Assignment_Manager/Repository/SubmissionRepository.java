package org.una.programmingIII.Assignment_Manager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.una.programmingIII.Assignment_Manager.Model.Submission;
@Repository
public interface SubmissionRepository extends JpaRepository<Submission,Long> {
}
