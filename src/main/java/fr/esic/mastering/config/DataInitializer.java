package fr.esic.mastering.config;

import fr.esic.mastering.entities.Role_update;
import fr.esic.mastering.repository.Role_update_repository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initRoles(Role_update_repository roleRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role_update(null, "ROLE_ADMIN"));
                roleRepository.save(new Role_update(null, "ROLE_JURY"));
                roleRepository.save(new Role_update(null, "ROLE_ETUDIANT"));
                roleRepository.save(new Role_update(null, "ROLE_COORDINATEUR"));
                System.out.println("Rôles initialisés.");
            }
        };
    }
}
