BEGIN;

UPDATE spotlight_challenges SET free_plays = 1 WHERE id = 'pad-bank-run' AND free_plays = 0;

COMMIT;
