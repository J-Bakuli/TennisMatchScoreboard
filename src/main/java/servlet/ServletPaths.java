package servlet;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ServletPaths {
    public static final String MATCH_SCORE = "/match-score";
    public static final String MATCHES = "/matches";
    public static final String MATCH_SCORE_WITH_UUID = MATCH_SCORE + "?uuid=";
}
