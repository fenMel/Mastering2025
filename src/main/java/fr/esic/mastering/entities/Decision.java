package fr.esic.mastering.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Decision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User candidat;

    @ManyToOne
    private User jury;

    @OneToOne
    @JoinColumn(name = "evaluation_id", nullable = false, unique = true)
    private Evaluation evaluation;

    private String commentaireFinal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerdictDecision verdict;

    public Long getEvaluationId() {
        return evaluation != null ? evaluation.getId() : null;
    }
}
