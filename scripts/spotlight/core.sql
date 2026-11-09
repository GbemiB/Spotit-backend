BEGIN;

INSERT INTO spotlight_challenges (id, kind, name, blurb, icon, tone, cost, cost_from, min_age, status, sort_order, active, free_plays) VALUES
  ('know-yourself', 'quiz', 'Know Yourself', 'Myth-or-fact rounds on 10 topics. Levels 1–2 free.', NULL, 'pink', 0, false, 13, 'live', 1, true, 0),
  ('pad-bank-run', 'game', 'Pad Bank Run', 'Serve each visitor the right pack. The first visitors on every level are free.', 'bag', 'peach', 0, false, 13, 'live', 3, true, 1),
  ('give-day', 'game', 'Give Day', 'Travel across Lagos and give period kits where they’re needed.', 'pin', 'green', 0, false, 13, 'live', 4, true, 1)
ON CONFLICT DO NOTHING;

UPDATE spotlight_challenges SET blurb = 'Serve each visitor the right pack. The first visitors on every level are free.' WHERE id = 'pad-bank-run' AND blurb = 'Serve each visitor the right pack. The first visitors each day are free.';

UPDATE spotlight_challenges SET partner_name = 'Know {name} better' WHERE id = 'know-yourself' AND partner_name IS NULL;

INSERT INTO spotlight_ranks (id, name, min_stars, active) VALUES
  ('curious', 'Curious', 0, true),
  ('explorer', 'Explorer', 50, true),
  ('myth-buster', 'Myth Buster',100, true),
  ('know-it-all', 'Know-It-All', 200, true)
ON CONFLICT DO NOTHING;

UPDATE spotlight_ranks SET min_stars = 50 WHERE id = 'explorer' AND min_stars = 15;
UPDATE spotlight_ranks SET min_stars = 100 WHERE id = 'myth-buster' AND min_stars = 45;
UPDATE spotlight_ranks SET min_stars = 200 WHERE id = 'know-it-all' AND min_stars = 90;

UPDATE global_config SET padbank_day_title_perfect = 'Perfect level!' WHERE padbank_day_title_perfect = 'Perfect day!';
UPDATE global_config SET padbank_day_title_done = 'Bank closed' WHERE padbank_day_title_done = 'Bank closed for today';

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
