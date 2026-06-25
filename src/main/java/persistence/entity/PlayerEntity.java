package persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "players", indexes = @Index(name = "idx_players_name", columnList = "name"))
public class PlayerEntity {

    // Пакет entity можно разместить на одном уровне с другими пакетами (dao, model, dto и тд): persistence.entity —> entity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false, unique = true, length = 20)
    private String name;

    public PlayerEntity(String name) {

        // В проекте уже есть валидация, которая проверяет имя на null, длину, формат и т.д.
            // Проверка в конструкторе дублирует только часть этой логики. Достаточно оставить её только в одном месте.
        this.name = Objects.requireNonNull(name);
    }
}
