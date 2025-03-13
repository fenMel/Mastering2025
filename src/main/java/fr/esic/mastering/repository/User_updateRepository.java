package fr.esic.mastering.repository;

import fr.esic.mastering.entities.User_update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;



    @Repository
    public interface User_updateRepository extends JpaRepository<User_update, Long> {
        Optional<User_update> findByUsername(String username);
        Optional<User_update> findByEmail(String email);
        boolean existsByUsername(String username);
        boolean existsByEmail(String email);


}
