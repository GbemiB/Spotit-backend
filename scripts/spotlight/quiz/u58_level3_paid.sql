BEGIN;

UPDATE quiz_levels SET cost = 10 WHERE number = 3 AND cost = 0 AND topic_id IN ('my-cycle', 'period-myths', 'body-changes', 'period-care', 'hormones-101', 'skin-hormones', 'mood-hormones', 'sleep-cycle', 'pcos', 'food-cycle');

UPDATE quiz_challenges SET blurb = 'Myth-or-fact rounds on 10 topics. Levels 1–2 free.'
WHERE id = 'know-yourself' AND blurb = 'Myth-or-fact rounds on 10 topics. Levels 1–3 free.';

COMMIT;
