package com.jb.tennismatchscoreboard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OngoingMatch {
    private Integer id;
    private Integer player1;
    private Integer player2;
    private Integer winner;
}
