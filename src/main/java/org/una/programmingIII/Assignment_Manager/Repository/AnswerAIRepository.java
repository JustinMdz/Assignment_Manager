package org.una.programmingIII.Assignment_Manager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.una.programmingIII.Assignment_Manager.Model.AnswerAI;

@Repository
public interface AnswerAIRepository extends JpaRepository<AnswerAI, Long> {
}
