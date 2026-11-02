BEGIN;

DELETE FROM spotlight_challenges WHERE id IN ('pad-factory', 'grow-a-maker', 'clinic-shift');

COMMIT;
