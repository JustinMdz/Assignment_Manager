package org.una.programmingIII.Assignment_Manager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.una.programmingIII.Assignment_Manager.Model.File;
@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    File findBySubmissionId(Long id);
}
