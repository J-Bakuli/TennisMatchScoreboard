# Роадмап рефакторинга по файлам

Это упорядоченный список файлов, которые следует исправлять в соответствии с замечаниями в комментариях. Рекомендую двигаться последовательно.

Файлы, не указанные в списке, можно исправлять в любом порядке.

### Шаг 1: Entity и слой доступа к данным (DAO)

- `/persistence/entity/PlayerEntity.java`
- `/persistence/entity/FinishedMatchEntity.java`
- `/dao/AbstractH2Dao.java`
- `/dao/PlayerDao.java`
- `/dao/H2PlayerDao.java`
- `/dao/MatchesDao.java`
- `/dao/H2MatchesDao.java`
- `/dao/OngoingMatchDao.java`
- `/dao/InMemoryOngoingMatchDao.java`

### Шаг 2: Доменные модели

- `/model/GamePoint.java`
- `/model/RegularGameScore.java`
- `/model/TieBreakScore.java`
- `/model/MatchState.java`
- `/model/OngoingMatch.java`
- `/service/MatchScoreCalculationService.java`

### Шаг 3: Сервисный слой

- `/service/NewMatchService.java`
- `/service/OngoingMatchService.java`
- `/service/FinishedMatchesService.java`

### Шаг 4: DTO (Data Transfer Object)

- `/dto/MatchScoreDto.java`

### Шаг 5: Сервлеты

- `/controller/StartServlet.java`
- `/controller/NewMatchServlet.java`
- `/controller/MatchScoreServlet.java`
- `/controller/MatchesServlet.java`

### Шаг 6: Тесты и JSP

- `src/test/java/service/MatchScoreCalculationTest.java`
- `src/main/webapp/WEB-INF/views/match-score.jsp`
- `src/main/webapp/WEB-INF/views/matches.jsp`
