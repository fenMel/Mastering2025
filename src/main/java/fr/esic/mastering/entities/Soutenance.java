package fr.esic.mastering.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Soutenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateHeure;

    @Column(nullable = false)
    private String lieu;

    private String sujet;

    @OneToMany(mappedBy = "soutenance", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonManagedReference(value = "soutenance-inscription")  // Add this
    private List<Inscription> inscriptions = new ArrayList<>();

    //SessionDeformation mapping

    @ManyToOne
    @JoinColumn(name = "session_de_formation_id")
    private SessionDeFormation sessionDeFormation;


    // Getters and setters
    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.LAZY)
    @Builder.Default
    @JoinTable(
            name = "soutenance_jury",
            joinColumns = @JoinColumn(name = "soutenance_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User_update> juryMembers = new ArrayList<>();


    // Getters and setters
    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coordinateur_id")

    private User_update coordinateur;


}


