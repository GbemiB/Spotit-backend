BEGIN;

INSERT INTO padbank_packs (id, name, cost, stock, color, sort_order, active) VALUES
  ('starter', 'Starter Bag', 10, 4, 'pink', 1, true),
  ('school', 'School Kit', 15, 3, 'peach', 2, true),
  ('comfort', 'Comfort Kit', 20, 2, 'rose', 3, true),
  ('reusable', 'Reusable Year', 40, 1, 'green', 4, true)
ON CONFLICT DO NOTHING;

INSERT INTO padbank_visitors (id, day_number, sort_order, who, look, clues, pack, route, thanks, route_why, wrong_why, active) VALUES
  ('day1-girl', 1, 1, 'Girl, 13', 'girl', 'drop|First period, today
noMoney|No money on her
house|Lives 5 min away', 'starter', 'pickup', 'Thank you! I was so scared this morning.', 'Right pack! She lives 5 minutes away, so pickup is free. Save delivery for someone far away.', 'reusable|Reusable pads need clean water and a private place to dry. For a first period, a Starter Bag with a simple guide is kinder.
comfort|The Comfort Kit is for heavy or painful periods. For a first period, a Starter Bag explains the basics.
school|A School Kit lasts a term. Today she needs the basics and a guide: the Starter Bag.', true),
  ('day1-woman', 1, 2, 'Woman, 27', 'woman', 'drops|Very heavy, cramps
tent|Camp, 20 km away
noWalk|Can’t travel this week', 'comfort', 'delivery', 'The heat patch helped so much. Thank you!', 'Right pack! But she can’t travel this week, so pickup leaves her waiting. Delivery was the only way.', 'reusable|There is no reliable clean water at the camp, so reusable pads are hard to keep safe. Heavy flow and cramps need the Comfort Kit.
starter|A Starter Bag is for a first period. With heavy flow and cramps, the Comfort Kit has extra pads and a heat patch.
school|A School Kit is made for a student. Heavy flow and cramps call for the Comfort Kit.', true),
  ('day1-student', 1, 3, 'Student, SS2', 'student', 'book|Pads for the whole term
walk|Passes the bank daily
smile|Normal flow, no pain', 'school', 'pickup', 'I won’t miss school this term. Thank you!', 'Right pack! She walks past every day, so pickup is free. Delivery used budget she did not need.', 'starter|A Starter Bag is for a first period. For a whole term, the School Kit has enough pads and a discreet bag.
comfort|The Comfort Kit is for heavy or painful periods. A School Kit covers a normal term.
reusable|A Reusable Year costs most of the budget and she didn’t mention washing facilities. The School Kit is the safer match.', true)
ON CONFLICT DO NOTHING;

COMMIT;
