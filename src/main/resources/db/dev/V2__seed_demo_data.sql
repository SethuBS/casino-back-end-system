INSERT INTO players (id, username, balance, free_wagers, version)
VALUES (1, 'test_player', 500.00, 0, 0);

ALTER TABLE players ALTER COLUMN id RESTART WITH 2;

INSERT INTO transactions (transaction_id, player_id, amount, type, created_at)
SELECT 'unique-transaction-id-01', id, 50.00, 'WIN', TIMESTAMP '2026-06-11 08:20:00'
FROM players
WHERE username = 'test_player';

INSERT INTO transactions (transaction_id, player_id, amount, type, created_at)
SELECT 'unique-transaction-id-02', id, -50.00, 'WAGER', TIMESTAMP '2026-06-11 08:21:00'
FROM players
WHERE username = 'test_player';

INSERT INTO transactions (transaction_id, player_id, amount, type, created_at)
SELECT 'unique-transaction-id-03', id, 50.00, 'WIN', TIMESTAMP '2026-06-11 08:22:00'
FROM players
WHERE username = 'test_player';

INSERT INTO transactions (transaction_id, player_id, amount, type, created_at)
SELECT 'unique-transaction-id-04', id, -50.00, 'WAGER', TIMESTAMP '2026-06-11 08:23:00'
FROM players
WHERE username = 'test_player';

INSERT INTO transactions (transaction_id, player_id, amount, type, created_at)
SELECT 'unique-transaction-id-05', id, 50.00, 'WIN', TIMESTAMP '2026-06-11 08:24:00'
FROM players
WHERE username = 'test_player';

INSERT INTO transactions (transaction_id, player_id, amount, type, created_at)
SELECT 'unique-transaction-id-06', id, -50.00, 'WAGER', TIMESTAMP '2026-06-11 08:25:00'
FROM players
WHERE username = 'test_player';

INSERT INTO transactions (transaction_id, player_id, amount, type, created_at)
SELECT 'unique-transaction-id-07', id, 50.00, 'WIN', TIMESTAMP '2026-06-11 08:26:00'
FROM players
WHERE username = 'test_player';

INSERT INTO transactions (transaction_id, player_id, amount, type, created_at)
SELECT 'unique-transaction-id-08', id, -50.00, 'WAGER', TIMESTAMP '2026-06-11 08:27:00'
FROM players
WHERE username = 'test_player';

INSERT INTO transactions (transaction_id, player_id, amount, type, created_at)
SELECT 'unique-transaction-id-09', id, 50.00, 'WIN', TIMESTAMP '2026-06-11 08:28:00'
FROM players
WHERE username = 'test_player';

INSERT INTO transactions (transaction_id, player_id, amount, type, created_at)
SELECT 'unique-transaction-id-10', id, -50.00, 'WAGER', TIMESTAMP '2026-06-11 08:29:00'
FROM players
WHERE username = 'test_player';

INSERT INTO transactions (transaction_id, player_id, amount, type, created_at)
SELECT 'unique-transaction-id-11', id, 50.00, 'WIN', TIMESTAMP '2026-06-11 08:30:00'
FROM players
WHERE username = 'test_player';
