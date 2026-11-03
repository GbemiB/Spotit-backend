BEGIN;

INSERT INTO spotlight_challenges (id, kind, name, blurb, icon, tone, cost, cost_from, min_age, status, sort_order, active, free_plays) VALUES
  ('know-yourself', 'quiz', 'Know Yourself', 'Myth-or-fact rounds on 10 topics. Levels 1–2 free.', NULL, 'pink', 0, false, 13, 'live', 1, true, 0),
  ('pad-bank-run', 'game', 'Pad Bank Run', 'Serve each visitor the right pack. The first visitors each day are free.', 'bag', 'peach', 0, false, 13, 'live', 3, true, 1),
  ('give-day', 'game', 'Give Day', 'Travel across Lagos and give period kits where they’re needed.', 'pin', 'green', 0, false, 13, 'live', 4, true, 1)
ON CONFLICT DO NOTHING;

INSERT INTO spotlight_ranks (id, name, min_stars, active) VALUES
  ('curious', 'Curious', 0, true),
  ('explorer', 'Explorer', 15, true),
  ('myth-buster', 'Myth Buster', 45, true),
  ('know-it-all', 'Know-It-All', 90, true)
ON CONFLICT DO NOTHING;

COMMIT;
