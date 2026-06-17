package service.support;

public record PageContext(
        int page,
        int totalPages,
        String playerNameFilter) {
}
