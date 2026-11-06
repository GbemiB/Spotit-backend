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

COMMIT;
