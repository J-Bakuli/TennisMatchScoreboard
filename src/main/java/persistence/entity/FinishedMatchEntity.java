package persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Check(constraints = "player1_id <> player2_id")
@Check(constraints = "winner_id = player1_id OR winner_id = player2_id")
@Table(name = "matches") // "matches" является зарезервированным словом в некоторых СУБД.
    // Здесь проблем не будет, но лучше не выбирать такие названия. (см. файл "sql-keywords.md" в этом же пакете)
public class FinishedMatchEntity {

    // Пакет entity можно разместить на одном уровне с другими пакетами (dao, model, dto и тд): persistence.entity —> entity

    // Аннотация @Check помечена как @Deprecated(since = "7") вместо неё лучше использовать @CheckConstraint в @Table:
        // В таком духе: @Table(name = "matches", check = @CheckConstraint(name = "constraint_name", constraint = "constraint condition"))

    // Связи `@ManyToOne` не имеют явного указания о стратегии загрузки.
        // По умолчанию для `@ManyToOne` используется `FetchType.EAGER`, что приводит к немедленной загрузке связанных сущностей при загрузке `Match`.
        // Это может вызывать проблемы производительности (N+1 запросов) и излишнюю загрузку данных, особенно если связанные объекты не всегда нужны.

    // Поскольку в качестве ID используется тип Integer, то можно не вводить дополнительное поле
        // (LocalDateTime finishedAt), для сортировки матчей и просто сортировать по ID.
        // Хотя на фронтенде текущий вариант смотрится хорошо.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(optional = false) // Стоит добавить FetchType.LAZY
    @JoinColumn(name = "player1_id", nullable = false) // Можно добавить updatable = false
    private PlayerEntity player1;
    @ManyToOne(optional = false) // Стоит добавить FetchType.LAZY
    @JoinColumn(name = "player2_id", nullable = false) // Можно добавить updatable = false
    private PlayerEntity player2;
    @ManyToOne(optional = false) // Стоит добавить FetchType.LAZY
    @JoinColumn(name = "winner_id", nullable = false) // Можно добавить updatable = false
    private PlayerEntity winner;
    @Column(name = "finished_at", nullable = false) // Можно добавить updatable = false
    private LocalDateTime finishedAt;

    public FinishedMatchEntity(PlayerEntity player1, PlayerEntity player2,
                               PlayerEntity winner, LocalDateTime finishedAt) {
        this.player1 = Objects.requireNonNull(player1);
        this.player2 = Objects.requireNonNull(player2);
        this.winner = Objects.requireNonNull(winner);
        this.finishedAt = Objects.requireNonNull(finishedAt);
    }
}
