package fr.esic.mastering.entities;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nom;
	private String prenom;
	private Date DateNaissance;
	private String tel;

	@Column(unique = true, nullable = false)
	private String email;
	private String lieuxDeNaissance;

	@NotBlank(message = "Le mot de passe est obligatoire.")
	private String password;
//   samy
	@ManyToOne
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;


    // Add getters and setters
    @Setter
    @Getter
    @Column(name = "reset_token")
	private String resetToken;

	@Setter
    @Getter
    @Column(name = "reset_token_expiry")
	private LocalDateTime resetTokenExpiry;



	// Constructor matching the arguments in MasteringApplication.java
	public User(Long id, String nom, String prenom, Date DateNaissance, String tel, String email,
				String lieuxDeNaissance, String password, Role role) {
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.DateNaissance = DateNaissance;
		this.tel = tel;
		this.email = email;
		this.lieuxDeNaissance = lieuxDeNaissance;
		this.password = password;
		this.role = role;
	}

}
