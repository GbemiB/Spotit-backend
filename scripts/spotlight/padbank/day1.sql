BEGIN;

INSERT INTO padbank_packs (id, name, cost, stock, color, sort_order, active) VALUES
  ('starter', 'Starter Bag', 3, 4, 'pink', 1, true),
  ('school', 'School Kit', 5, 3, 'peach', 2, true),
  ('comfort', 'Comfort Kit', 6, 2, 'rose', 3, true),
  ('reusable', 'Reusable Year', 10, 1, 'green', 4, true)
ON CONFLICT DO NOTHING;

INSERT INTO padbank_visitors (id, day_number, sort_order, who, look, clues, pack, route, thanks, route_why, wrong_why, active) VALUES
  ('day1-girl', 1, 1, 'Girl, 13', 'girl', 'drop|First period, today
noMoney|No money on her
house|Lives 5 min away', 'starter', 'pickup', 'Thank you! I was so scared this morning.', 'Right pack! She lives 5 minutes away, so pickup is free. Save delivery for someone who can’t come.', 'reusable|Reusable pads need clean water and a private place to dry. For a first period, a Starter Bag with a simple guide is kinder.
comfort|The Comfort Kit is for heavy or painful periods. For a first period, a Starter Bag explains the basics.
school|A School Kit lasts a term. Today she needs the basics and a guide: the Starter Bag.', true),
  ('day1-woman', 1, 2, 'Woman, 27', 'woman', 'drops|Very heavy, cramps
tent|Camp, 20 km away
noWalk|Can’t travel this week', 'comfort', 'delivery', 'The heat patch helped so much. Thank you!', 'Right pack! But she can’t travel this week, so pickup leaves her waiting. Delivery was the only way.', 'reusable|There is no reliable clean water at the camp, so reusable pads are hard to keep safe. Heavy flow and cramps need the Comfort Kit.
starter|A Starter Bag is for a first period. With heavy flow and cramps, the Comfort Kit has extra pads and a heat patch.
school|A School Kit is made for a student. Heavy flow and cramps call for the Comfort Kit.', true),
  ('day1-student', 1, 3, 'Student, SS2', 'student', 'book|Pads for the whole term
walk|Passes the bank daily
smile|Normal flow, no pain', 'school', 'pickup', 'I won’t miss school this term. Thank you!', 'Right pack! She walks past every day, so pickup is free. Delivery cost SP she didn’t need.', 'starter|A Starter Bag is for a first period. For a whole term, the School Kit has enough pads and a discreet bag.
comfort|The Comfort Kit is for heavy or painful periods. A School Kit covers a normal term.
reusable|A Reusable Year costs the most and she didn’t mention washing facilities. The School Kit is the safer match.', true),
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
