package util;

import lombok.experimental.UtilityClass;
import service.support.UrlNavigation;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class UrlNavigationUtils {
    private static final String MATCHES_PATH = "/matches";

    public String buildMatchesPageUrl(int targetPage, String playerNameParam) {
        StringBuilder url = new StringBuilder(MATCHES_PATH)
                .append("?page=")
                .append(targetPage);

        if (playerNameParam != null && !playerNameParam.isBlank()) {
            url.append("&filter_by_player_name=")
                    .append(URLEncoder.encode(playerNameParam.trim(), StandardCharsets.UTF_8));
        }

        return url.toString();
    }

    public UrlNavigation buildPaginationNavigation(int currentPage, int totalPages, String playerNameParam) {
        String previousPageUrl = currentPage > 1
                ? buildMatchesPageUrl(currentPage - 1, playerNameParam)
                : null;
        String nextPageUrl = currentPage < totalPages
                ? buildMatchesPageUrl(currentPage + 1, playerNameParam)
                : null;
        return new UrlNavigation(previousPageUrl, nextPageUrl);
    }
}
