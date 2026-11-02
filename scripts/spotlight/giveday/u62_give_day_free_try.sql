BEGIN;

UPDATE spotlight_challenges SET free_plays = 1 WHERE id = 'give-day' AND free_plays = 0;

COMMIT;
