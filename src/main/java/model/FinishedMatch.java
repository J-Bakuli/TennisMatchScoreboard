package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class FinishedMatch {
    private Integer player1Id;
    private Integer player2Id;
    private Integer winnerId;
    private LocalDateTime finishedAt;
}
