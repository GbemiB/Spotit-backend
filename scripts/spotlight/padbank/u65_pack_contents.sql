BEGIN;

UPDATE padbank_packs SET contents = E'10 day pads\nSimple period guide\nSmall pouch' WHERE id = 'starter' AND contents IS NULL;
UPDATE padbank_packs SET contents = E'Pads for a whole term\nLiners for light days\nDiscreet bag' WHERE id = 'school' AND contents IS NULL;
UPDATE padbank_packs SET contents = E'Extra heavy-flow pads\nHeat patch\nComfort tips card' WHERE id = 'comfort' AND contents IS NULL;
UPDATE padbank_packs SET contents = E'4 reusable pads\nWash bag\nCare guide' WHERE id = 'reusable' AND contents IS NULL;

COMMIT;
