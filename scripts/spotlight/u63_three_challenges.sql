BEGIN;

UPDATE spotlight_challenges SET active = false WHERE id IN ('pad-factory', 'grow-a-maker', 'clinic-shift');

COMMIT;
