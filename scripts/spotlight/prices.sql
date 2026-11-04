BEGIN;

UPDATE quiz_levels SET cost = 0 WHERE number IN (1, 2);
UPDATE quiz_levels SET cost = 2 WHERE number = 3;
UPDATE quiz_levels SET cost = 3 WHERE number = 4;
UPDATE quiz_levels SET cost = 5 WHERE number = 5;

UPDATE padbank_packs SET cost = 1 WHERE id IN ('liners', 'starter');
UPDATE padbank_packs SET cost = 2 WHERE id IN ('school', 'night');
UPDATE padbank_packs SET cost = 3 WHERE id IN ('comfort', 'sport', 'newmum');
UPDATE padbank_packs SET cost = 5 WHERE id = 'reusable';

UPDATE giveday_travel SET cost = 0 WHERE id = 'walk';
UPDATE giveday_travel SET cost = 1 WHERE id = 'bike';
UPDATE giveday_travel SET cost = 2 WHERE id = 'car';

UPDATE giveday_levels SET pass_cost = 0 WHERE level_number IN (1, 2);
UPDATE giveday_levels SET pass_cost = 2 WHERE level_number = 3;
UPDATE giveday_levels SET pass_cost = 3 WHERE level_number = 4;
UPDATE giveday_levels SET pass_cost = 5 WHERE level_number = 5;
UPDATE giveday_levels SET gift_budget = 5;
UPDATE giveday_levels SET gift_max = 10;
UPDATE giveday_levels SET extras_free = (level_number <= 2);

UPDATE global_config SET quiz_hint_cost = 1, padbank_visitor_fee = 1, padbank_delivery_cost = 1;

COMMIT;
