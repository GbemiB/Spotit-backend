BEGIN;

UPDATE spotlight_challenges SET status = 'live' WHERE id = 'give-day' AND status = 'soon';

COMMIT;
