package org.una.programmingIII.Assignment_Manager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.una.programmingIII.Assignment_Manager.Model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
   @Query("select u from User u join u.permissions p where p.name = :permission")
    User findByPermissionName(@Param("permission") String permission);
}
