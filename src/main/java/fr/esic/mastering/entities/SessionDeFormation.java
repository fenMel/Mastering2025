package fr.esic.mastering.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionDeFormation {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false)
private String nom;

@Column(nullable = false)
private String information;

@Column(nullable = false)
private LocalDateTime dateDebut;

@Column(nullable = false)
private LocalDateTime dateFin;

@OneToMany(mappedBy = "sessionDeFormation", cascade = CascadeType.ALL)
@Builder.Default
@JsonManagedReference(value = "session-soutenance")
private List<Soutenance> soutenance = new ArrayList<>();


}
