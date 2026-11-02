BEGIN;

INSERT INTO spotlight_challenges (id, kind, name, blurb, icon, tone, cost, cost_from, min_age, status, sort_order, active) VALUES
  ('know-yourself', 'quiz', 'Know Yourself', 'Myth-or-fact rounds on 10 topics. Levels 1–2 free.', NULL, 'pink', 0, false, 13, 'live', 1, true),
  ('pad-factory', 'game', 'Pad Factory', 'Design pads with SP, test them, collect badges.', 'pad', 'pink', 10, true, 13, 'soon', 2, true),
  ('pad-bank-run', 'game', 'Pad Bank Run', 'Hand the right kit to each visitor before time runs out.', 'bag', 'peach', 10, false, 13, 'soon', 3, true),
  ('give-day', 'game', 'Give Day', 'Walk the town and give period kits where they’re needed.', 'pin', 'green', 15, false, 13, 'soon', 4, true),
  ('grow-a-maker', 'game', 'Grow a Maker', 'Help a young maker grow her pad business.', 'spool', 'gold', 10, false, 13, 'soon', 5, true),
  ('clinic-shift', 'game', 'Clinic Shift', 'Run a pretend clinic shift and answer patients’ questions.', 'steth', 'pink', 15, false, 18, 'soon', 6, true)
ON CONFLICT DO NOTHING;

INSERT INTO spotlight_ranks (id, name, min_stars, active) VALUES
  ('curious', 'Curious', 0, true),
  ('explorer', 'Explorer', 15, true),
  ('myth-buster', 'Myth Buster', 45, true),
  ('know-it-all', 'Know-It-All', 90, true)
ON CONFLICT DO NOTHING;

COMMIT;
