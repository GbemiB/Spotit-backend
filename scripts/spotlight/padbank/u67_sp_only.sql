BEGIN;

UPDATE padbank_packs SET cost = 3 WHERE id = 'starter';
UPDATE padbank_packs SET cost = 5 WHERE id = 'school';
UPDATE padbank_packs SET cost = 6 WHERE id = 'comfort';
UPDATE padbank_packs SET cost = 10 WHERE id = 'reusable';

UPDATE global_config SET padbank_delivery_cost = 2 WHERE padbank_delivery_cost = 10;

UPDATE spotlight_challenges SET cost = 0, blurb = 'Serve each visitor the right pack. The first visitors each day are free.' WHERE id = 'pad-bank-run';

UPDATE padbank_visitors SET route_why = 'Right pack! She lives 5 minutes away, so pickup is free. Save delivery for someone who can’t come.' WHERE id = 'day1-girl';
UPDATE padbank_visitors SET route_why = 'Right pack! She walks past every day, so pickup is free. Delivery cost SP she didn’t need.',
  wrong_why = replace(wrong_why, 'A Reusable Year costs most of the budget and she', 'A Reusable Year costs the most and she') WHERE id = 'day1-student';

INSERT INTO padbank_visitors (id, day_number, sort_order, who, look, clues, pack, route, thanks, route_why, wrong_why, active) VALUES
  ('day1-mum', 1, 4, 'Woman, 34', 'woman', 'house|Water and a sunny yard
smile|Normal flow, no pain
walk|Passes the bank on Fridays', 'reusable', 'pickup', 'No more buying pads every month. Thank you!', 'Right pack! She passes on Fridays, so pickup is free.', 'starter|A Starter Bag is for a first period. With clean water and a yard to dry them, reusable pads will last her a year.
school|A School Kit is made for a student. She has water and a sunny yard: the Reusable Year fits.
comfort|The Comfort Kit is for heavy or painful periods. Her flow is normal, and she can wash and dry reusables at home.', true),
  ('day1-exam', 1, 5, 'Girl, 15', 'girl', 'drops|Heavy, painful days
book|Exams all week
noWalk|Too unwell to come back', 'comfort', 'delivery', 'Now I can focus on my exams. Thank you!', 'Right pack! But she’s too unwell to come back, so delivery was the kind choice.', 'starter|A Starter Bag is for a first period. Heavy, painful days need the Comfort Kit.
school|The School Kit is for a normal term. With heavy flow and pain in exam week, the Comfort Kit helps most.
reusable|Reusables need washing and drying, which is hard in exam week when she feels unwell. The Comfort Kit fits.', true)
ON CONFLICT DO NOTHING;

COMMIT;
