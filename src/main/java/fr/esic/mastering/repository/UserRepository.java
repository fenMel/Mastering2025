package fr.esic.mastering.repository;

import java.util.List;
import java.util.Optional;

import fr.esic.mastering.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.esic.mastering.entities.Role;
import fr.esic.mastering.entities.RoleType;
import fr.esic.mastering.entities.User;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User, Long> {

	
	
	public Optional<User> findByEmailAndPassword(String email, String password);
	 
	@Query("select u from User u where u.email = ?1    and u.password =?2 ")
	public Optional<User> getByLoginAndPassword(String email, String password);

   public Optional<User> findByEmail(String email);

    // Find by token method
    Optional<User> findByResetToken(String resetToken);
   
   @Query("select u from User u where u.email = ?1 ")
   public Optional<User> getByEmail(String email);


    @Query("SELECT u FROM User u JOIN u.role r WHERE u.id = :id AND r.roleUtilisateur = 'JURY'")
    Optional<User> findJuryById(@Param("id") Long id);
    // Recherche des utilisateurs par type de rôle
   Page<User> findByRole_RoleUtilisateur(RoleType roleType, Pageable pageable);
    List<User> findByRole_RoleUtilisateur(String roleName);
    List<User> findByRoleRoleUtilisateur(RoleType roleUtilisateur);

    

//    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'ROLE_CANDIDAT'")
   
 @Query("SELECT u FROM User u WHERE u.role.roleUtilisateur = :roleType AND u.id IN :ids")
List<User> findCandidatsByRoleAndIds(@Param("roleType") RoleType roleType, @Param("ids") List<Long> ids);
// Version correcte pour récupérer tous les utilisateurs d'un rôle (ex : tous les candidats)
@Query("SELECT u FROM User u WHERE u.role.roleUtilisateur = :roleType")
List<User> findAllByRole(@Param("roleType") RoleType roleType);

List<User> findByRole(Role role); // ✅ CORRECTE

List<User> findByRole_RoleUtilisateur(RoleType roleUtilisateur);

@Query("SELECT u FROM User u WHERE u.role.roleUtilisateur = :roleType AND u.id IN :ids")
List<User> findAllByRoleAndIds(@Param("roleType") RoleType roleType, @Param("ids") List<Long> ids);
 
}





  



