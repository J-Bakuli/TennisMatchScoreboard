package servlet;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ServletPaths {
    public final String MATCH_SCORE = "/match-score";
    public final String MATCHES = "/matches";
    public final String MATCH_SCORE_WITH_UUID = MATCH_SCORE + "?uuid=";
}
