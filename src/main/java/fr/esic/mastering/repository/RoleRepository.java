package fr.esic.mastering.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.esic.mastering.entities.Role;
import fr.esic.mastering.entities.RoleType;

public interface RoleRepository extends JpaRepository<Role, Long>{
  
	// Optional<Role> findByName(String name);
    Optional<Role> findByRoleUtilisateur(RoleType roleUtilisateur);
}
