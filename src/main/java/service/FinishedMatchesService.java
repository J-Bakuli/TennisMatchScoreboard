package service;

import dao.MatchesDao;
import dto.FinishedMatchDto;
import dto.FinishedMatchesPageDto;
import model.OngoingMatch;
import model.PageContext;
import model.UrlNavigation;
import util.MatchesQueryUtils;
import validation.MatchValidation;
import validation.MatchesQueryValidation;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FinishedMatchesService {
    private static final String MATCHES_PATH = "/matches";
    private static final int PAGE_SIZE = 10;
    private final MatchesDao matchesDao;

    public FinishedMatchesService(MatchesDao matchesDao) {
        this.matchesDao = matchesDao;
    }

    public void saveFinishedMatch(OngoingMatch ongoingMatch) {
        MatchValidation.validateOngoingMatch(ongoingMatch);
        matchesDao.save(ongoingMatch);
    }

    public FinishedMatchesPageDto getFinishedMatchesPage(String pageParam, String playerNameParam) {
        String normalizedPlayerName = MatchesQueryUtils.normalizeFilter(playerNameParam);
        PageContext context = buildPageContext(pageParam, normalizedPlayerName);
        List<FinishedMatchDto> matchesDto = findMatchesForPage(context);
        UrlNavigation navigation = buildPaginationNavigation(context, playerNameParam);

        return new FinishedMatchesPageDto(
                matchesDto,
                context.page(),
                context.totalPages(),
                playerNameParam,
                navigation.previousPageUrl(),
                navigation.nextPageUrl());
    }

    private PageContext buildPageContext(String pageParam, String playerNameFilter) {
        int page = MatchesQueryValidation.parsePage(pageParam);
        int totalMatches = calculateTotalMatches(playerNameFilter);
        int totalPages = totalMatches == 0 ? 0 : (int) Math.ceil((double) totalMatches / PAGE_SIZE);
        if (totalPages > 0 && page > totalPages) {
            page = totalPages;
        }
        return new PageContext(page, totalPages, playerNameFilter);
    }

    private List<FinishedMatchDto> findMatchesForPage(PageContext context) {
        int offset = calculatePageOffset(context.page());
        return context.playerNameFilter() == null ?
                matchesDao.findAllMatches(offset, PAGE_SIZE) :
                matchesDao.findMatchesByPlayerName(context.playerNameFilter(), offset, PAGE_SIZE);
    }

    private UrlNavigation buildPaginationNavigation(PageContext context, String playerNameParam) {
        int page = context.page();
        String previousPageUrl = page > 1 ? buildMatchesPageUrl(page - 1, playerNameParam) : null;
        String nextPageUrl = page < context.totalPages() ? buildMatchesPageUrl(page + 1, playerNameParam) : null;
        return new UrlNavigation(previousPageUrl, nextPageUrl);
    }

    private String buildMatchesPageUrl(int targetPage, String playerNameParam) {
        StringBuilder url = new StringBuilder(MATCHES_PATH)
                .append("?page=")
                .append(targetPage);

        if (playerNameParam != null && !playerNameParam.isBlank()) {
            url.append("&filter_by_player_name=")
                    .append(URLEncoder.encode(playerNameParam.trim(), StandardCharsets.UTF_8));
        }

        return url.toString();
    }

    private int calculatePageOffset(int page) {
        return (page - 1) * PAGE_SIZE;
    }

    private int calculateTotalMatches(String playerNameFilter) {
        return playerNameFilter == null ? matchesDao.countAllMatches() : matchesDao.countMatchesByPlayerName(playerNameFilter);
    }
}
