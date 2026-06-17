package model;

public record PageContext(
        int page,
        int totalPages,
        String playerNameFilter) {
}
