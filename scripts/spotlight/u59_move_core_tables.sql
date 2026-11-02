BEGIN;

INSERT INTO spotlight_challenges (id, kind, name, blurb, icon, tone, cost, cost_from, min_age, status, sort_order, active)
SELECT id, kind, name, blurb, icon, tone, cost, cost_from, min_age, status, sort_order, active FROM quiz_challenges
ON CONFLICT DO NOTHING;

INSERT INTO spotlight_ranks (id, name, min_stars, active)
SELECT id, name, min_stars, active FROM quiz_ranks
ON CONFLICT DO NOTHING;

COMMIT;
