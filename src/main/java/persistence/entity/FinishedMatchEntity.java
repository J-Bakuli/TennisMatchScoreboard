package persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "matches")
public class FinishedMatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "player1_id", nullable = false)
    private PlayerEntity player1;
    @ManyToOne
    @JoinColumn(name = "player2_id", nullable = false)
    private PlayerEntity player2;
    @ManyToOne
    @JoinColumn(name = "winner_id")
    private PlayerEntity winner;
}
