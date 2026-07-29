package service;

import dao.MatchesDao;
import dto.FinishedMatchDto;
import dto.FinishedMatchesPageDto;
import entity.FinishedMatchEntity;
import lombok.RequiredArgsConstructor;
import mapper.FinishedMatchDtoMapper;
import model.OngoingMatch;
import org.mapstruct.factory.Mappers;
import service.support.PageContext;
import util.PageUtil;
import util.StringUtils;
import validation.PageValidation;

import java.util.List;

@RequiredArgsConstructor
public class FinishedMatchesService {

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // Размер страницы и номер по умолчанию более уместно хранить в сервлете, так как в идеале он должен приходить с фронтенда.
        // А сервис должен принимать это значение в качестве аргумента в методы.

    // Логику, связанную с расчётом данных для пагинации тоже можно вынести во вспомогательный класс.

    // Валидация и парсинг данных от пользователя происходит внутри сервисного слоя, а не на "входе" в приложение.
        // Это не соответствует принципу быстрого отказа ("Fail Fast"):
        // Проверку корректности данных, пришедших от пользователя, следует проводить как можно раньше.
        // Валидация на уровне сервлета позволяет немедленно прервать обработку некорректного запроса и вернуть клиенту ошибку `400 Bad Request`.
        // Текущий подход заставляет приложение выполнять лишнюю работу, передавая невалидные данные дальше в сервисный слой.
        // Стоит запускать логику валидации из сервлета и там же парсить данные.
    private static final int PAGE_SIZE = 10;
    private final MatchesDao matchesDao;
    private final FinishedMatchDtoMapper finishedMatchDtoMapper = Mappers.getMapper(FinishedMatchDtoMapper.class);

    public void saveFinishedMatch(OngoingMatch ongoingMatch) {
        if (ongoingMatch == null) {
            throw new IllegalArgumentException("ongoingMatch cannot be null");
        }
        ongoingMatch.assertFinished();
        matchesDao.save(ongoingMatch);
    }

    public FinishedMatchesPageDto getFinishedMatchesPage(String pageParam, String playerNameParam) {
        String normalizedPlayerName = (playerNameParam == null || playerNameParam.isBlank())
                ? null
                : StringUtils.normalizeInput(playerNameParam);
        PageContext context = buildPageContext(pageParam, normalizedPlayerName);
        List<FinishedMatchDto> matchesDto = findMatchesForPage(context);

        return new FinishedMatchesPageDto(
                matchesDto,
                context.page(),
                context.totalPages(),
                playerNameParam);
    }

    private PageContext buildPageContext(String pageParam, String playerNameFilter) {
        PageValidation.validatePage(pageParam);
        int page = PageUtil.parsePage(pageParam);
        long totalMatches = calculateTotalMatches(playerNameFilter);
        int totalPages = totalMatches == 0 ? 0 : (int) Math.ceil((double) totalMatches / PAGE_SIZE);
        if (totalPages > 0 && page > totalPages) {
            page = totalPages;
        }
        return new PageContext(page, totalPages, playerNameFilter);
    }

    private List<FinishedMatchDto> findMatchesForPage(PageContext context) {
        int offset = calculatePageOffset(context.page());
        List<FinishedMatchEntity> matchEntities = context.playerNameFilter() == null
                ? matchesDao.findAllMatches(offset, PAGE_SIZE)
                : matchesDao.findMatchesByPlayerName(context.playerNameFilter(), offset, PAGE_SIZE);
        return finishedMatchDtoMapper.toDto(matchEntities);
    }

    private int calculatePageOffset(int page) {
        return (page - 1) * PAGE_SIZE;
    }

    private Long calculateTotalMatches(String playerNameFilter) {
        return playerNameFilter == null ? matchesDao.countAll() : matchesDao.countByPlayerName(playerNameFilter);
    }
}
