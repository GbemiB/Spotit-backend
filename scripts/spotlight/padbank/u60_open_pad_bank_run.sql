BEGIN;

UPDATE spotlight_challenges SET status = 'live' WHERE id = 'pad-bank-run' AND status = 'soon';

COMMIT;
