-- Players
INSERT INTO players(name) VALUES ('Novak Djokovic');
INSERT INTO players(name) VALUES ('Rafael Nadal');
INSERT INTO players(name) VALUES ('Roger Federer');
INSERT INTO players(name) VALUES ('Carlos Alcaraz');
INSERT INTO players(name) VALUES ('Jannik Sinner');
INSERT INTO players(name) VALUES ('Daniil Medvedev');
INSERT INTO players(name) VALUES ('Alexander Zverev');
INSERT INTO players(name) VALUES ('Stefanos Tsitsipas');
INSERT INTO players(name) VALUES ('Andrey Rublev');
INSERT INTO players(name) VALUES ('Casper Ruud');

-- Matches
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (1, 2, 1, '2024-01-01 10:00:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (3, 4, 4, '2024-01-08 12:30:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (5, 6, 5, '2024-01-15 14:15:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (7, 8, 7, '2024-01-22 09:45:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (9, 10, 10, '2024-01-29 16:20:00');

INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (1, 3, 1, '2024-02-05 11:10:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (2, 4, 2, '2024-02-12 13:40:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (5, 7, 5, '2024-02-19 15:55:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (6, 8, 8, '2024-02-26 08:30:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (9, 1, 1, '2024-03-04 17:05:00');

INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (10, 2, 2, '2024-03-11 10:25:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (3, 5, 5, '2024-03-18 12:50:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (4, 6, 4, '2024-03-25 14:35:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (7, 9, 7, '2024-04-01 09:15:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (8, 10, 8, '2024-04-08 16:45:00');

INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (1, 5, 1, '2024-04-15 11:30:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (2, 6, 6, '2024-04-22 13:20:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (3, 7, 7, '2024-04-29 15:10:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (4, 8, 4, '2024-05-06 08:55:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (9, 5, 5, '2024-05-13 17:40:00');

INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (10, 1, 1, '2024-05-20 10:00:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (2, 7, 7, '2024-05-27 12:15:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (3, 8, 8, '2024-06-03 14:45:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (4, 9, 4, '2024-06-10 09:20:00');
INSERT INTO matches(player1_id, player2_id, winner_id, finished_at) VALUES (6, 10, 6, '2024-06-17 16:30:00');
