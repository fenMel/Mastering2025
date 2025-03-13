package fr.esic.mastering.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class  Role_update {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    // Enum to define all available roles
    public enum RoleType {
        ADMIN("ROLE_ADMIN"),
        ETUDIANT("ROLE_ETUDIANT"),
        JURY("ROLE_JURY"),
        COORDINATEUR("ROLE_COORDINATEUR");

        private final String value;

        RoleType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
