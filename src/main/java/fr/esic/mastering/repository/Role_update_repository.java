package fr.esic.mastering.repository;

import fr.esic.mastering.entities.Role_update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Role_update_repository extends JpaRepository<Role_update, Long> {
    Optional<Role_update> findByName(String name);
}
