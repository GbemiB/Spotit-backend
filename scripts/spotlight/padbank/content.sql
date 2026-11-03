BEGIN;

DELETE FROM padbank_visitors;
DELETE FROM padbank_packs;

INSERT INTO padbank_packs (id, name, cost, stock, color, contents, from_day, sort_order, active) VALUES
  ('starter', 'Starter Bag', 3, 4, 'pink', '10 day pads
Simple period guide
Small pouch', 1, 1, true),
  ('school', 'School Kit', 5, 3, 'peach', 'Pads for a whole term
Liners for light days
Discreet bag', 1, 2, true),
  ('comfort', 'Comfort Kit', 6, 2, 'rose', 'Extra heavy-flow pads
Heat patch
Comfort tips card', 1, 3, true),
  ('reusable', 'Reusable Year', 10, 1, 'green', '4 reusable pads
Wash bag
Care guide', 1, 4, true),
  ('liners', 'Light Days Pack', 2, 3, 'peach', 'Liners for light days
Small pouch', 3, 5, true),
  ('night', 'Night Pack', 5, 2, 'rose', 'Long overnight pads
Night tips card', 5, 6, true),
  ('sport', 'Active Pack', 6, 3, 'green', 'Period pants
Tampons for swimming
Sports guide', 7, 7, true),
  ('newmum', 'New Mum Pack', 7, 1, 'pink', 'Maternity pads
Comfort guide', 9, 8, true);

INSERT INTO padbank_visitors (id, day_number, sort_order, who, look, clues, packs, route, thanks, route_why, group_why, wrong_why, active) VALUES
  ('day1-girl', 1, 1, 'Girl, 13', 'girl', 'drop|First period, today
noMoney|No money on her
house|Lives 5 min away', 'starter', 'pickup', 'Thank you! I was so scared this morning.', 'Right pack! She lives 5 minutes away, so pickup is free. Save delivery for someone who can’t come.', NULL, 'school|A School Kit lasts a term. Today she needs the basics and a guide: the Starter Bag.
comfort|The Comfort Kit is for heavy or painful periods. For a first period, a Starter Bag explains the basics.
reusable|Reusable pads need clean water and a private place to dry. For a first period, a Starter Bag with a simple guide is kinder.', true),
  ('day1-woman', 1, 2, 'Woman, 27', 'woman', 'drops|Very heavy, cramps
tent|Camp, 20 km away
noWalk|Can’t travel this week', 'comfort', 'delivery', 'The heat patch helped so much. Thank you!', 'Right pack! She can’t travel this week, so pickup leaves her waiting. Delivery was the only way.', NULL, 'starter|The Starter Bag is for a first period. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
school|The School Kit is a term of pads for a student. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
reusable|There is no reliable clean water at the camp, so reusable pads are hard to keep safe. Heavy flow and cramps need the Comfort Kit.', true),
  ('day1-student', 1, 3, 'Student, SS2', 'student', 'book|Pads for the whole term
walk|Passes the bank daily
smile|Normal flow, no pain', 'school', 'pickup', 'I won’t miss school this term. Thank you!', 'Right pack! She walks past every day, so pickup is free. Delivery cost SP she didn’t need.', NULL, 'starter|The Starter Bag is for a first period. Here the need is a whole term of normal periods: the School Kit.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a whole term of normal periods: the School Kit.
reusable|A Reusable Year costs the most and she didn’t mention washing facilities. The School Kit is the safer match.', true),
  ('day1-mum', 1, 4, 'Woman, 34', 'woman', 'house|Water and a sunny yard
smile|Normal flow, no pain
walk|Passes the bank on Fridays', 'reusable', 'pickup', 'No more buying pads every month. Thank you!', 'Right pack! She passes on Fridays, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
school|The School Kit is a term of pads for a student. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
comfort|The Comfort Kit is for heavy or painful days. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.', true),
  ('day1-exam', 1, 5, 'Girl, 15', 'girl', 'drops|Heavy, painful days
book|Exams all week
noWalk|Too unwell to come back', 'comfort', 'delivery', 'Now I can focus on my exams. Thank you!', 'Right pack! She’s too unwell to come back, so delivery was the kind choice.', NULL, 'starter|The Starter Bag is for a first period. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
school|The School Kit is a term of pads for a student. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
reusable|Reusable pads need clean water and a place to dry them. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.', true),
  ('d2-1', 2, 1, 'Girl, 12', 'girl', 'drop|Started at school today
house|Lives behind the bank
noMoney|Mum is at work', 'starter', 'pickup', 'Thank you! I was so scared this morning.', 'Right pack! She lives right behind the bank, so pickup is free.', NULL, 'school|The School Kit is a term of pads for a student. Here the need is a first period: the Starter Bag with its simple guide.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a first period: the Starter Bag with its simple guide.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a first period: the Starter Bag with its simple guide.', true),
  ('d2-2', 2, 2, 'Student, SS1', 'student', 'book|Needs pads till July
walk|Walks past after class
smile|Light, regular periods', 'school', 'pickup', 'Ready for the whole term. Thank you!', 'Right pack! She walks past after class, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is a whole term of normal periods: the School Kit.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a whole term of normal periods: the School Kit.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a whole term of normal periods: the School Kit.', true),
  ('d2-3', 2, 3, 'Woman, 30', 'woman', 'drops|Heavy flow, bad cramps
noWalk|Sprained her ankle
house|Lives across town', 'comfort', 'delivery', 'This makes the heavy days easier. Thank you!', 'Right pack! She can’t walk on that ankle, so delivery was the kind choice.', NULL, 'starter|The Starter Bag is for a first period. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
school|The School Kit is a term of pads for a student. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
reusable|Reusable pads need clean water and a place to dry them. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.', true),
  ('d2-4', 2, 4, 'Woman, 41', 'woman', 'water|Tap water at home
smile|Steady, normal flow
walk|Shops here on Saturdays', 'reusable', 'pickup', 'No more buying pads every month. Thank you!', 'Right pack! She shops here every Saturday, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
school|The School Kit is a term of pads for a student. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
comfort|The Comfort Kit is for heavy or painful days. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.', true),
  ('d2-5', 2, 5, 'Girl, 14', 'girl', 'drop|Her second period ever
noWalk|At home with flu
house|Lives 15 min away', 'starter', 'delivery', 'Now I know what to do. Thank you!', 'Right pack! She’s at home with flu, so delivery saves her the trip.', NULL, 'school|The School Kit is a term of pads for a student. Here the need is a first period: the Starter Bag with its simple guide.
comfort|Flu makes her tired, but her flow is new and normal. The Starter Bag and its guide fit a second period.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a first period: the Starter Bag with its simple guide.', true),
  ('d3-1', 3, 1, 'Woman, 25', 'woman', 'smile|Just spotting
clock|Period ends tomorrow
walk|Works next door', 'liners', 'pickup', 'Just what I needed for the last days. Thank you!', 'Right pack! She works next door, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is light days only: the Light Days Pack.
school|The School Kit is a term of pads for a student. Here the need is light days only: the Light Days Pack.
comfort|The Comfort Kit is for heavy or painful days. Here the need is light days only: the Light Days Pack.
reusable|Reusable pads need clean water and a place to dry them. Here the need is light days only: the Light Days Pack.', true),
  ('d3-2', 3, 2, 'Student, JSS3', 'student', 'book|New term next week
smile|Normal flow
walk|Passes on the school bus', 'school', 'pickup', 'No more worrying in class. Thank you!', 'Right pack! The school bus stops here, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is a whole term of normal periods: the School Kit.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a whole term of normal periods: the School Kit.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a whole term of normal periods: the School Kit.
liners|Liners only cover light days. Here the need is a whole term of normal periods: the School Kit.', true),
  ('d3-3', 3, 3, 'Girl, 12', 'girl', 'drop|First period this morning
noMoney|No money for pads
walk|Mum will walk her over', 'starter', 'pickup', 'Thank you! I was so scared this morning.', 'Right pack! Her mum can walk her over, so pickup is free.', NULL, 'school|The School Kit is a term of pads for a student. Here the need is a first period: the Starter Bag with its simple guide.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a first period: the Starter Bag with its simple guide.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a first period: the Starter Bag with its simple guide.
liners|Liners only cover light days. Here the need is a first period: the Starter Bag with its simple guide.', true),
  ('d3-4', 3, 4, 'Woman, 36', 'woman', 'drops|Very heavy, cramps
tent|Lives at the camp
noWalk|No transport this week', 'comfort', 'delivery', 'I can finally rest. Thank you!', 'Right pack! She has no transport this week, so delivery was the only way.', NULL, 'starter|The Starter Bag is for a first period. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
school|The School Kit is a term of pads for a student. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
reusable|Camps rarely have clean water to wash reusables. Heavy flow and cramps need the Comfort Kit.
liners|Liners only cover light days. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.', true),
  ('d3-5', 3, 5, 'Student, 18', 'student', 'smile|Light days at the end
book|Lectures all day
noWalk|Can’t leave campus', 'liners', 'delivery', 'Just what I needed for the last days. Thank you!', 'Right pack! She can’t leave campus, so delivery gets them to her in time.', NULL, 'starter|The Starter Bag is for a first period. Here the need is light days only: the Light Days Pack.
school|The School Kit is a term of pads for a student. Here the need is light days only: the Light Days Pack.
comfort|The Comfort Kit is for heavy or painful days. Here the need is light days only: the Light Days Pack.
reusable|Reusable pads need clean water and a place to dry them. Here the need is light days only: the Light Days Pack.', true),
  ('d4-1', 4, 1, 'Woman, 29', 'woman', 'water|Water and a yard
smile|Normal flow
noWalk|Resting at home this week', 'reusable', 'delivery', 'These will last me all year. Thank you!', 'Right pack! She’s resting at home this week, so delivery was the kind choice.', NULL, 'starter|The Starter Bag is for a first period. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
school|The School Kit is a term of pads for a student. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
comfort|The Comfort Kit is for heavy or painful days. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
liners|Liners only cover light days. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.', true),
  ('d4-2', 4, 2, 'Girl, 13', 'girl', 'drop|Started at the market
noMoney|No money on her
walk|The market is next door', 'starter', 'pickup', 'Thank you! I was so scared this morning.', 'Right pack! The market is next door, so pickup is free.', NULL, 'school|The School Kit is a term of pads for a student. Here the need is a first period: the Starter Bag with its simple guide.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a first period: the Starter Bag with its simple guide.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a first period: the Starter Bag with its simple guide.
liners|Liners only cover light days. Here the need is a first period: the Starter Bag with its simple guide.', true),
  ('d4-3', 4, 3, 'Student, SS3', 'student', 'book|Pads for the term
smile|Normal flow
house|Boarding house nearby', 'school', 'pickup', 'Ready for the whole term. Thank you!', 'Right pack! Her boarding house is nearby, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is a whole term of normal periods: the School Kit.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a whole term of normal periods: the School Kit.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a whole term of normal periods: the School Kit.
liners|Liners only cover light days. Here the need is a whole term of normal periods: the School Kit.', true),
  ('d4-4', 4, 4, 'Woman, 45', 'woman', 'drops|Heavy, painful days
walk|Comes by on Mondays
house|Lives 5 min away', 'comfort', 'pickup', 'This makes the heavy days easier. Thank you!', 'Right pack! She lives 5 minutes away, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
school|The School Kit is a term of pads for a student. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
reusable|Reusable pads need clean water and a place to dry them. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
liners|Liners only cover light days. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.', true),
  ('d4-5', 4, 5, 'Woman, 22', 'woman', 'smile|Light days only
clock|Period nearly over
noWalk|Can’t leave her shop', 'liners', 'delivery', 'Light and easy. Thank you!', 'Right pack! She can’t leave her shop, so delivery gets them to her.', NULL, 'starter|The Starter Bag is for a first period. Here the need is light days only: the Light Days Pack.
school|The School Kit is a term of pads for a student. Here the need is light days only: the Light Days Pack.
comfort|The Comfort Kit is for heavy or painful days. Here the need is light days only: the Light Days Pack.
reusable|Reusable pads need clean water and a place to dry them. Here the need is light days only: the Light Days Pack.', true),
  ('d5-1', 5, 1, 'Nurse, 31', 'woman', 'moon|Leaks at night
water|Has water at home
walk|Passes after her shift', 'night', 'pickup', 'I can sleep through the night now. Thank you!', 'Right pack! She passes after her shift, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is leaks at night: the Night Pack, with long overnight pads.
school|The School Kit is a term of pads for a student. Here the need is leaks at night: the Night Pack, with long overnight pads.
comfort|The Comfort Kit is for heavy or painful days. Here the need is leaks at night: the Night Pack, with long overnight pads.
reusable|Water at home helps, but her problem is leaking at night. The Night Pack has long overnight pads.
liners|Liners only cover light days. Here the need is leaks at night: the Night Pack, with long overnight pads.', true),
  ('d5-2', 5, 2, 'Girl, 13', 'girl', 'drop|First period today
book|Exams next week
house|Lives next door', 'starter', 'pickup', 'Now I know what to do. Thank you!', 'Right pack! She lives next door, so pickup is free.', NULL, 'school|Exams are coming, but this is her first period. The Starter Bag with a simple guide comes first.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a first period: the Starter Bag with its simple guide.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a first period: the Starter Bag with its simple guide.
liners|Liners only cover light days. Here the need is a first period: the Starter Bag with its simple guide.
night|The Night Pack is for leaks at night. Here the need is a first period: the Starter Bag with its simple guide.', true),
  ('d5-3', 5, 3, 'Woman, 33', 'woman', 'drops|Very heavy, cramps
moon|Sleeps badly on period
noWalk|Can’t travel this week', 'comfort', 'delivery', 'This makes the heavy days easier. Thank you!', 'Right pack! She can’t travel this week, so delivery was the only way.', NULL, 'starter|The Starter Bag is for a first period. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
school|The School Kit is a term of pads for a student. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
reusable|Reusable pads need clean water and a place to dry them. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
liners|Liners only cover light days. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
night|Night pads help sleep, but heavy flow with cramps needs the Comfort Kit and its heat patch.', true),
  ('d5-4', 5, 4, 'Student, SS2', 'student', 'book|Pads for the term
smile|Light days at the end
walk|Walks past daily', 'school', 'pickup', 'I won’t miss school this term. Thank you!', 'Right pack! She walks past daily, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is a whole term of normal periods: the School Kit.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a whole term of normal periods: the School Kit.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a whole term of normal periods: the School Kit.
liners|Liners only cover the light days at the end. For a whole term she needs the School Kit.
night|The Night Pack is for leaks at night. Here the need is a whole term of normal periods: the School Kit.', true),
  ('d5-5', 5, 5, 'Woman, 38', 'woman', 'water|Water and a sunny yard
noMoney|Tired of buying pads
noWalk|Lives far, no transport', 'reusable', 'delivery', 'No more buying pads every month. Thank you!', 'Right pack! She lives far with no transport, so delivery was the only way.', NULL, 'starter|The Starter Bag is for a first period. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
school|The School Kit is a term of pads for a student. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
comfort|The Comfort Kit is for heavy or painful days. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
liners|Liners only cover light days. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
night|The Night Pack is for leaks at night. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.', true),
  ('d6-1', 6, 1, 'Woman, 27', 'woman', 'moon|Wakes up to leaks
smile|Days are fine
walk|Lives 5 min away', 'night', 'pickup', 'No more washing sheets at dawn. Thank you!', 'Right pack! She lives 5 minutes away, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is leaks at night: the Night Pack, with long overnight pads.
school|The School Kit is a term of pads for a student. Here the need is leaks at night: the Night Pack, with long overnight pads.
comfort|The Comfort Kit is for heavy or painful days. Here the need is leaks at night: the Night Pack, with long overnight pads.
reusable|Reusable pads need clean water and a place to dry them. Here the need is leaks at night: the Night Pack, with long overnight pads.
liners|Liners only cover light days. Here the need is leaks at night: the Night Pack, with long overnight pads.', true),
  ('d6-2', 6, 2, 'Student, 17', 'student', 'book|Term starts Monday
drop|Period due any day
walk|Passes on the way to school', 'school', 'pickup', 'No more worrying in class. Thank you!', 'Right pack! She passes on the way to school, so pickup is free.', NULL, 'starter|Her period is due, but she has had them for years. A whole term needs the School Kit.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a whole term of normal periods: the School Kit.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a whole term of normal periods: the School Kit.
liners|Liners only cover light days. Here the need is a whole term of normal periods: the School Kit.
night|The Night Pack is for leaks at night. Here the need is a whole term of normal periods: the School Kit.', true),
  ('d6-3', 6, 3, 'Girl, 15', 'girl', 'drops|Heavy, painful days
book|Exam week
noWalk|Too unwell to come', 'comfort', 'delivery', 'The heat patch helped so much. Thank you!', 'Right pack! She’s too unwell to come, so delivery was the kind choice.', NULL, 'starter|The Starter Bag is for a first period. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
school|It’s exam week, but heavy, painful days need the Comfort Kit first.
reusable|Reusable pads need clean water and a place to dry them. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
liners|Liners only cover light days. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
night|The Night Pack is for leaks at night. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.', true),
  ('d6-4', 6, 4, 'Woman, 50', 'woman', 'smile|Light and irregular now
clock|Spotting on and off
house|Lives upstairs', 'liners', 'pickup', 'Just what I needed for the last days. Thank you!', 'Right pack! She lives upstairs, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is light days only: the Light Days Pack.
school|The School Kit is a term of pads for a student. Here the need is light days only: the Light Days Pack.
comfort|The Comfort Kit is for heavy or painful days. Here the need is light days only: the Light Days Pack.
reusable|Reusable pads need clean water and a place to dry them. Here the need is light days only: the Light Days Pack.
night|The Night Pack is for leaks at night. Here the need is light days only: the Light Days Pack.', true),
  ('d6-5', 6, 5, 'Woman, 26', 'woman', 'noWater|No running water
drops|Heavy flow
tent|At the camp, can’t travel', 'comfort', 'delivery', 'This makes the heavy days easier. Thank you!', 'Right pack! She can’t leave the camp, so delivery was the only way.', NULL, 'starter|The Starter Bag is for a first period. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
school|The School Kit is a term of pads for a student. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
reusable|Reusables save money, but without clean water they are hard to keep safe. Heavy flow needs the Comfort Kit.
liners|Liners only cover light days. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
night|The Night Pack is for leaks at night. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.', true),
  ('d7-1', 7, 1, 'Student, 16', 'student', 'swim|On the swimming team
ball|Plays football too
walk|Trains next door', 'sport', 'pickup', 'I can play without worrying. Thank you!', 'Right pack! She trains next door, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
school|The School Kit is a term of pads for a student. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
comfort|The Comfort Kit is for heavy or painful days. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
reusable|Reusable pads need clean water and a place to dry them. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
liners|Liners only cover light days. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
night|The Night Pack is for leaks at night. Here the need is sport and swimming: the Active Pack, with period pants and tampons.', true),
  ('d7-2', 7, 2, 'Girl, 12', 'girl', 'drop|First period today
ball|Loves football
house|Lives close by', 'starter', 'pickup', 'Thank you! I was so scared this morning.', 'Right pack! She lives close by, so pickup is free.', NULL, 'school|The School Kit is a term of pads for a student. Here the need is a first period: the Starter Bag with its simple guide.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a first period: the Starter Bag with its simple guide.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a first period: the Starter Bag with its simple guide.
liners|Liners only cover light days. Here the need is a first period: the Starter Bag with its simple guide.
night|The Night Pack is for leaks at night. Here the need is a first period: the Starter Bag with its simple guide.
sport|Football can wait a day. A first period needs the Starter Bag and its simple guide.', true),
  ('d7-3', 7, 3, 'Woman, 32', 'woman', 'moon|Leaks at night
swim|Swims at weekends
walk|Picks up after work', 'night', 'pickup', 'I can sleep through the night now. Thank you!', 'Right pack! She picks up after work, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is leaks at night: the Night Pack, with long overnight pads.
school|The School Kit is a term of pads for a student. Here the need is leaks at night: the Night Pack, with long overnight pads.
comfort|The Comfort Kit is for heavy or painful days. Here the need is leaks at night: the Night Pack, with long overnight pads.
reusable|Reusable pads need clean water and a place to dry them. Here the need is leaks at night: the Night Pack, with long overnight pads.
liners|Liners only cover light days. Here the need is leaks at night: the Night Pack, with long overnight pads.
sport|A weekend swim is not the problem. Leaking at night is: the Night Pack.', true),
  ('d7-4', 7, 4, 'Student, SS1', 'student', 'book|Pads for the term
smile|Normal, no pain
noWalk|Lives far, no bus fare', 'school', 'delivery', 'No more worrying in class. Thank you!', 'Right pack! She has no bus fare to come back, so delivery was the kind choice.', NULL, 'starter|The Starter Bag is for a first period. Here the need is a whole term of normal periods: the School Kit.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a whole term of normal periods: the School Kit.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a whole term of normal periods: the School Kit.
liners|Liners only cover light days. Here the need is a whole term of normal periods: the School Kit.
night|The Night Pack is for leaks at night. Here the need is a whole term of normal periods: the School Kit.
sport|The Active Pack is for sport and swimming. Here the need is a whole term of normal periods: the School Kit.', true),
  ('d7-5', 7, 5, 'Woman, 28', 'woman', 'drops|Very heavy, cramps
ball|Coaches netball
noWalk|Can’t travel this week', 'comfort', 'delivery', 'The heat patch helped so much. Thank you!', 'Right pack! She can’t travel this week, so delivery was the only way.', NULL, 'starter|The Starter Bag is for a first period. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
school|The School Kit is a term of pads for a student. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
reusable|Reusable pads need clean water and a place to dry them. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
liners|Liners only cover light days. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
night|The Night Pack is for leaks at night. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
sport|She coaches but doesn’t play. Very heavy days with cramps need the Comfort Kit.', true),
  ('d8-1', 8, 1, 'Girl, 14', 'girl', 'swim|Swim gala on Friday
smile|Had periods for a year
walk|The pool is next door', 'sport', 'pickup', 'See you at the next match. Thank you!', 'Right pack! The pool is next door, so pickup is free.', NULL, 'starter|She has had periods for a year, so she knows the basics. A swim gala needs the Active Pack.
school|The School Kit is a term of pads for a student. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
comfort|The Comfort Kit is for heavy or painful days. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
reusable|Reusable pads need clean water and a place to dry them. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
liners|Liners only cover light days. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
night|The Night Pack is for leaks at night. Here the need is sport and swimming: the Active Pack, with period pants and tampons.', true),
  ('d8-2', 8, 2, 'Woman, 35', 'woman', 'smile|Spotting only
clock|Last day of her period
noWalk|Can’t leave her stall', 'liners', 'delivery', 'Just what I needed for the last days. Thank you!', 'Right pack! She can’t leave her stall, so delivery gets them to her.', NULL, 'starter|The Starter Bag is for a first period. Here the need is light days only: the Light Days Pack.
school|The School Kit is a term of pads for a student. Here the need is light days only: the Light Days Pack.
comfort|The Comfort Kit is for heavy or painful days. Here the need is light days only: the Light Days Pack.
reusable|Reusable pads need clean water and a place to dry them. Here the need is light days only: the Light Days Pack.
night|The Night Pack is for leaks at night. Here the need is light days only: the Light Days Pack.
sport|The Active Pack is for sport and swimming. Here the need is light days only: the Light Days Pack.', true),
  ('d8-3', 8, 3, 'Student, 19', 'student', 'water|Hostel has water
noMoney|Wants to stop buying pads
walk|Hostel is next door', 'reusable', 'pickup', 'These will last me all year. Thank you!', 'Right pack! Her hostel is next door, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
school|The School Kit is a term of pads for a student. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
comfort|The Comfort Kit is for heavy or painful days. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
liners|Liners only cover light days. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
night|The Night Pack is for leaks at night. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
sport|The Active Pack is for sport and swimming. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.', true),
  ('d8-4', 8, 4, 'Woman, 40', 'woman', 'moon|Leaks every night
tent|Sleeps at the camp
noWalk|No transport', 'night', 'delivery', 'I can sleep through the night now. Thank you!', 'Right pack! She has no transport, so delivery was the only way.', NULL, 'starter|The Starter Bag is for a first period. Here the need is leaks at night: the Night Pack, with long overnight pads.
school|The School Kit is a term of pads for a student. Here the need is leaks at night: the Night Pack, with long overnight pads.
comfort|The Comfort Kit is for heavy or painful days. Here the need is leaks at night: the Night Pack, with long overnight pads.
reusable|Reusable pads need clean water and a place to dry them. Here the need is leaks at night: the Night Pack, with long overnight pads.
liners|Liners only cover light days. Here the need is leaks at night: the Night Pack, with long overnight pads.
sport|The Active Pack is for sport and swimming. Here the need is leaks at night: the Night Pack, with long overnight pads.', true),
  ('d8-5', 8, 5, 'Girl, 15', 'girl', 'drops|Heavy and painful
swim|On the swim team
house|Lives 5 min away', 'comfort', 'pickup', 'I can finally rest. Thank you!', 'Right pack! She lives 5 minutes away, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
school|The School Kit is a term of pads for a student. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
reusable|Reusable pads need clean water and a place to dry them. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
liners|Liners only cover light days. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
night|The Night Pack is for leaks at night. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
sport|Swimming can wait. Heavy, painful days come first: the Comfort Kit.', true),
  ('d9-man', 9, 1, 'Man, 35 · for his wife', 'man', 'drops|Wife: very heavy days
house|Lives 10 min away
walk|Picks up after work', 'comfort', 'pickup', 'My wife will be so relieved. Thank you!', 'Right pack! He walks past after work, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
school|The School Kit is a term of pads for a student. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
reusable|Reusable pads need clean water and a place to dry them. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
liners|Liners only cover light days. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
night|Night pads help, but heavy days need the Comfort Kit with extra pads and a heat patch.
sport|The Active Pack is for sport and swimming. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.', true),
  ('d9-father', 9, 2, 'Father, 42 · for his daughter, 11', 'fatherDaughter', 'drop|Her first period, today
book|She’s in JSS 1
noWalk|Too shy to come in', 'starter', 'delivery', 'She feels much better now. Thank you!', 'Right pack! She’s too shy to come in, so delivery to home was kinder.', NULL, 'school|The School Kit is a term of pads for a student. Here the need is a first period: the Starter Bag with its simple guide.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a first period: the Starter Bag with its simple guide.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a first period: the Starter Bag with its simple guide.
liners|Liners only cover light days. Here the need is a first period: the Starter Bag with its simple guide.
night|The Night Pack is for leaks at night. Here the need is a first period: the Starter Bag with its simple guide.
sport|The Active Pack is for sport and swimming. Here the need is a first period: the Starter Bag with its simple guide.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is a first period: the Starter Bag with its simple guide.', true),
  ('d9-mother', 9, 3, 'Mother, 38 · for her daughter, 15', 'motherDaughter', 'book|Term starts Monday
smile|Normal flow, no pain
walk|They pass the bank daily', 'school', 'pickup', 'She’s ready for the whole term!', 'Right pack! They pass every day, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is a whole term of normal periods: the School Kit.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a whole term of normal periods: the School Kit.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a whole term of normal periods: the School Kit.
liners|Liners only cover light days. Here the need is a whole term of normal periods: the School Kit.
night|The Night Pack is for leaks at night. Here the need is a whole term of normal periods: the School Kit.
sport|The Active Pack is for sport and swimming. Here the need is a whole term of normal periods: the School Kit.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is a whole term of normal periods: the School Kit.', true),
  ('d9-4', 9, 4, 'Husband, 30 · for his wife', 'man', 'baby|Wife gave birth last week
house|Lives nearby
walk|Comes after work', 'newmum', 'pickup', 'One less thing to worry about with the baby. Thank you!', 'Right pack! He comes by after work, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
school|The School Kit is a term of pads for a student. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
comfort|The Comfort Kit is for heavy or painful days. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
reusable|Reusable pads need clean water and a place to dry them. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
liners|Liners only cover light days. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
night|The Night Pack is for leaks at night. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
sport|The Active Pack is for sport and swimming. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.', true),
  ('d9-5', 9, 5, 'Aunt, 45 · for her niece, 16', 'woman', 'swim|Niece swims for school
smile|Normal periods
noWalk|Niece lives in another town', 'sport', 'delivery', 'I can play without worrying. Thank you!', 'Right pack! Her niece lives in another town, so delivery gets it to her.', NULL, 'starter|The Starter Bag is for a first period. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
school|The School Kit is a term of pads for a student. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
comfort|The Comfort Kit is for heavy or painful days. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
reusable|Reusable pads need clean water and a place to dry them. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
liners|Liners only cover light days. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
night|The Night Pack is for leaks at night. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is sport and swimming: the Active Pack, with period pants and tampons.', true),
  ('d10-1', 10, 1, 'Brother, 20 · for his sister, 13', 'man', 'drop|Her first period
house|Lives 3 streets away
walk|He’ll carry it home', 'starter', 'pickup', 'The little guide really helps. Thank you!', 'Right pack! He can carry it home, so pickup is free.', NULL, 'school|The School Kit is a term of pads for a student. Here the need is a first period: the Starter Bag with its simple guide.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a first period: the Starter Bag with its simple guide.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a first period: the Starter Bag with its simple guide.
liners|Liners only cover light days. Here the need is a first period: the Starter Bag with its simple guide.
night|The Night Pack is for leaks at night. Here the need is a first period: the Starter Bag with its simple guide.
sport|The Active Pack is for sport and swimming. Here the need is a first period: the Starter Bag with its simple guide.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is a first period: the Starter Bag with its simple guide.', true),
  ('d10-2', 10, 2, 'Woman, 52 · for her daughter', 'woman', 'baby|Daughter just had a baby
noWalk|Daughter can’t travel yet
house|Lives across town', 'newmum', 'delivery', 'Just what a new mum needs. Thank you!', 'Right pack! Her daughter can’t travel yet, so delivery was the kind choice.', NULL, 'starter|The Starter Bag is for a first period. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
school|The School Kit is a term of pads for a student. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
comfort|The Comfort Kit is for heavy or painful days. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
reusable|Reusable pads need clean water and a place to dry them. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
liners|Liners only cover light days. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
night|The Night Pack is for leaks at night. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
sport|The Active Pack is for sport and swimming. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.', true),
  ('d10-3', 10, 3, 'Father, 39 · for his daughter, 16', 'fatherDaughter', 'ball|Plays in the football team
swim|Swims on Saturdays
walk|They pass daily', 'sport', 'pickup', 'See you at the next match. Thank you!', 'Right pack! They pass every day, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
school|The School Kit is a term of pads for a student. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
comfort|The Comfort Kit is for heavy or painful days. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
reusable|Reusable pads need clean water and a place to dry them. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
liners|Liners only cover light days. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
night|The Night Pack is for leaks at night. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is sport and swimming: the Active Pack, with period pants and tampons.', true),
  ('d10-4', 10, 4, 'Man, 28 · for his sister', 'man', 'moon|She leaks at night
smile|Her days are fine
house|She lives with him nearby', 'night', 'pickup', 'I can sleep through the night now. Thank you!', 'Right pack! They live nearby, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is leaks at night: the Night Pack, with long overnight pads.
school|The School Kit is a term of pads for a student. Here the need is leaks at night: the Night Pack, with long overnight pads.
comfort|The Comfort Kit is for heavy or painful days. Here the need is leaks at night: the Night Pack, with long overnight pads.
reusable|Reusable pads need clean water and a place to dry them. Here the need is leaks at night: the Night Pack, with long overnight pads.
liners|Liners only cover light days. Here the need is leaks at night: the Night Pack, with long overnight pads.
sport|The Active Pack is for sport and swimming. Here the need is leaks at night: the Night Pack, with long overnight pads.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is leaks at night: the Night Pack, with long overnight pads.', true),
  ('d10-5', 10, 5, 'Mother, 40 · for her daughter, 14', 'motherDaughter', 'drops|Daughter: heavy and painful
book|Misses school on period
walk|They pass daily', 'comfort', 'pickup', 'The heat patch helped so much. Thank you!', 'Right pack! They pass every day, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
school|She misses school because of heavy, painful days. Fix that first with the Comfort Kit.
reusable|Reusable pads need clean water and a place to dry them. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
liners|Liners only cover light days. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
night|The Night Pack is for leaks at night. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
sport|The Active Pack is for sport and swimming. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.', true),
  ('d11-1', 11, 1, 'Man, 33 · for his wife', 'man', 'water|Water and a yard at home
smile|Normal flow
walk|Shops here weekly', 'reusable', 'pickup', 'No more buying pads every month. Thank you!', 'Right pack! He shops here weekly, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
school|The School Kit is a term of pads for a student. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
comfort|The Comfort Kit is for heavy or painful days. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
liners|Liners only cover light days. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
night|The Night Pack is for leaks at night. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
sport|The Active Pack is for sport and swimming. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.', true),
  ('d11-2', 11, 2, 'Teacher, 38 · for a pupil, 12', 'woman', 'drop|Pupil’s first period at school
girls|Too shy to tell her mum
walk|School is next door', 'starter', 'pickup', 'Now I know what to do. Thank you!', 'Right pack! The school is next door, so pickup is free.', NULL, 'school|The School Kit is a term of pads for a student. Here the need is a first period: the Starter Bag with its simple guide.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a first period: the Starter Bag with its simple guide.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a first period: the Starter Bag with its simple guide.
liners|Liners only cover light days. Here the need is a first period: the Starter Bag with its simple guide.
night|The Night Pack is for leaks at night. Here the need is a first period: the Starter Bag with its simple guide.
sport|The Active Pack is for sport and swimming. Here the need is a first period: the Starter Bag with its simple guide.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is a first period: the Starter Bag with its simple guide.', true),
  ('d11-3', 11, 3, 'Father, 45 · for his daughter, 15', 'fatherDaughter', 'book|Boarding school term
smile|Normal flow
noWalk|She left for school today', 'school', 'delivery', 'No more worrying in class. Thank you!', 'Right pack! She has already left for school, so delivery gets it to her.', NULL, 'starter|The Starter Bag is for a first period. Here the need is a whole term of normal periods: the School Kit.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a whole term of normal periods: the School Kit.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a whole term of normal periods: the School Kit.
liners|Liners only cover light days. Here the need is a whole term of normal periods: the School Kit.
night|The Night Pack is for leaks at night. Here the need is a whole term of normal periods: the School Kit.
sport|The Active Pack is for sport and swimming. Here the need is a whole term of normal periods: the School Kit.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is a whole term of normal periods: the School Kit.', true),
  ('d11-4', 11, 4, 'Woman, 60 · for her son’s wife', 'woman', 'baby|New baby at home
noWalk|Mother can’t leave the baby
house|Lives far away', 'newmum', 'delivery', 'One less thing to worry about with the baby. Thank you!', 'Right pack! The new mother can’t leave the baby, so delivery was the kind choice.', NULL, 'starter|The Starter Bag is for a first period. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
school|The School Kit is a term of pads for a student. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
comfort|The Comfort Kit is for heavy or painful days. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
reusable|Reusable pads need clean water and a place to dry them. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
liners|Liners only cover light days. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
night|The Night Pack is for leaks at night. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
sport|The Active Pack is for sport and swimming. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.', true),
  ('d11-5', 11, 5, 'Man, 41 · for his wife', 'man', 'smile|Light days at the end
clock|Period nearly over
walk|Passes on his way home', 'liners', 'pickup', 'Just what I needed for the last days. Thank you!', 'Right pack! He passes on his way home, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is light days only: the Light Days Pack.
school|The School Kit is a term of pads for a student. Here the need is light days only: the Light Days Pack.
comfort|The Comfort Kit is for heavy or painful days. Here the need is light days only: the Light Days Pack.
reusable|Reusable pads need clean water and a place to dry them. Here the need is light days only: the Light Days Pack.
night|The Night Pack is for leaks at night. Here the need is light days only: the Light Days Pack.
sport|The Active Pack is for sport and swimming. Here the need is light days only: the Light Days Pack.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is light days only: the Light Days Pack.', true),
  ('d12-1', 12, 1, 'Mother, 37 · for her daughter, 12', 'motherDaughter', 'drop|Daughter’s first period
noWalk|Daughter won’t come in
walk|Mum will take it home', 'starter', 'pickup', 'Now I know what to do. Thank you!', 'Right pack! Her daughter won’t come in, but mum is here and can take it home. Pickup is free.', NULL, 'school|The School Kit is a term of pads for a student. Here the need is a first period: the Starter Bag with its simple guide.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a first period: the Starter Bag with its simple guide.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a first period: the Starter Bag with its simple guide.
liners|Liners only cover light days. Here the need is a first period: the Starter Bag with its simple guide.
night|The Night Pack is for leaks at night. Here the need is a first period: the Starter Bag with its simple guide.
sport|The Active Pack is for sport and swimming. Here the need is a first period: the Starter Bag with its simple guide.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is a first period: the Starter Bag with its simple guide.', true),
  ('d12-2', 12, 2, 'Man, 36 · for his wife', 'man', 'drops|Wife: heavy, cramps
house|Wife lives in another town
noWalk|He travels tonight', 'comfort', 'delivery', 'This makes the heavy days easier. Thank you!', 'Right pack! His wife is in another town, so delivery reaches her sooner.', NULL, 'starter|The Starter Bag is for a first period. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
school|The School Kit is a term of pads for a student. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
reusable|Reusable pads need clean water and a place to dry them. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
liners|Liners only cover light days. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
night|The Night Pack is for leaks at night. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
sport|The Active Pack is for sport and swimming. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.', true),
  ('d12-3', 12, 3, 'Father, 50 · for his daughter, 17', 'fatherDaughter', 'swim|Swimming lessons
ball|Plays basketball
walk|Picks up on the way home', 'sport', 'pickup', 'See you at the next match. Thank you!', 'Right pack! He picks up on the way home, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
school|The School Kit is a term of pads for a student. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
comfort|The Comfort Kit is for heavy or painful days. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
reusable|Reusable pads need clean water and a place to dry them. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
liners|Liners only cover light days. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
night|The Night Pack is for leaks at night. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is sport and swimming: the Active Pack, with period pants and tampons.', true),
  ('d12-4', 12, 4, 'Nurse, 35 · for a new mother', 'woman', 'baby|Mother gave birth today
house|The clinic is next door
walk|The nurse will carry it', 'newmum', 'pickup', 'Just what a new mum needs. Thank you!', 'Right pack! The clinic is next door, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
school|The School Kit is a term of pads for a student. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
comfort|The Comfort Kit is for heavy or painful days. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
reusable|Reusable pads need clean water and a place to dry them. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
liners|Liners only cover light days. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
night|The Night Pack is for leaks at night. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
sport|The Active Pack is for sport and swimming. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.', true),
  ('d12-5', 12, 5, 'Man, 27 · for his sister, 18', 'man', 'moon|Sister leaks at night
book|Lives in a uni hostel
noWalk|She can’t come into town', 'night', 'delivery', 'No more washing sheets at dawn. Thank you!', 'Right pack! His sister can’t come into town, so delivery gets it to her.', NULL, 'starter|The Starter Bag is for a first period. Here the need is leaks at night: the Night Pack, with long overnight pads.
school|The School Kit is a term of pads for a student. Here the need is leaks at night: the Night Pack, with long overnight pads.
comfort|The Comfort Kit is for heavy or painful days. Here the need is leaks at night: the Night Pack, with long overnight pads.
reusable|Reusable pads need clean water and a place to dry them. Here the need is leaks at night: the Night Pack, with long overnight pads.
liners|Liners only cover light days. Here the need is leaks at night: the Night Pack, with long overnight pads.
sport|The Active Pack is for sport and swimming. Here the need is leaks at night: the Night Pack, with long overnight pads.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is leaks at night: the Night Pack, with long overnight pads.', true),
  ('d13-grandma', 13, 1, 'Grandmother · with 2 granddaughters', 'grandma', 'drop|Both have just started
girls|Ages 11 and 12
house|Live next door', 'starter,starter', 'pickup', 'My girls are ready now. Thank you, my dear!', 'Right packs! They live next door, so pickup is free.', 'Each girl needs her own Starter Bag.', 'school|The School Kit is a term of pads for a student. This group needs 2 Starter Bags.
comfort|The Comfort Kit is for heavy or painful days. This group needs 2 Starter Bags.
reusable|Reusable pads need clean water and a place to dry them. This group needs 2 Starter Bags.
liners|Liners alone are not enough for a period. A Starter Bag each.
night|The Night Pack is for leaks at night. This group needs 2 Starter Bags.
sport|The Active Pack is for sport and swimming. This group needs 2 Starter Bags.
newmum|The New Mum Pack is for bleeding after a birth. This group needs 2 Starter Bags.', true),
  ('d13-teens', 13, 2, 'Group of 3 teens', 'teens', 'ball|Two swim and play football
drops|One has heavy days
tent|School camp tomorrow', 'sport,sport,comfort', 'pickup', 'Camp is going to be fine now. Thanks!', 'Right packs! They’re here now and leave tomorrow, so pickup today.', 'Two Active Packs for the sporty girls and one Comfort Kit for heavy days.', 'starter|The Starter Bag is for a first period. This group needs 2 Active Packs and a Comfort Kit.
school|Camp needs more than a school term pack: Active Packs for the swimmers and a Comfort Kit for heavy days.
reusable|Reusable pads need clean water and a place to dry them. This group needs 2 Active Packs and a Comfort Kit.
liners|Liners only cover light days. This group needs 2 Active Packs and a Comfort Kit.
night|The Night Pack is for leaks at night. This group needs 2 Active Packs and a Comfort Kit.
newmum|The New Mum Pack is for bleeding after a birth. This group needs 2 Active Packs and a Comfort Kit.', true),
  ('d13-3', 13, 3, 'Mother and daughter, 13', 'motherDaughter', 'drop|Daughter: first period
baby|Mother: new baby
house|Live nearby', 'starter,newmum', 'pickup', 'Both of us are sorted. Thank you!', 'Right packs! They live nearby, so pickup is free.', 'A Starter Bag for the daughter and the New Mum Pack for her mother.', 'school|The School Kit is a term of pads for a student. This group needs a Starter Bag and a New Mum Pack.
comfort|The Comfort Kit is for heavy or painful days. This group needs a Starter Bag and a New Mum Pack.
reusable|Reusable pads need clean water and a place to dry them. This group needs a Starter Bag and a New Mum Pack.
liners|Liners only cover light days. This group needs a Starter Bag and a New Mum Pack.
night|The Night Pack is for leaks at night. This group needs a Starter Bag and a New Mum Pack.
sport|The Active Pack is for sport and swimming. This group needs a Starter Bag and a New Mum Pack.', true),
  ('d13-4', 13, 4, 'Teacher · for 2 pupils', 'woman', 'book|Two pupils need term pads
smile|Both have normal periods
noWalk|Can’t leave her class', 'school,school', 'delivery', 'My girls won’t miss class now. Thank you!', 'Right packs! She can’t leave her class, so delivery to the school was right.', 'Each pupil needs her own School Kit.', 'starter|The Starter Bag is for a first period. This group needs 2 School Kits.
comfort|The Comfort Kit is for heavy or painful days. This group needs 2 School Kits.
reusable|Reusable pads need clean water and a place to dry them. This group needs 2 School Kits.
liners|Liners only cover light days. This group needs 2 School Kits.
night|The Night Pack is for leaks at night. This group needs 2 School Kits.
sport|The Active Pack is for sport and swimming. This group needs 2 School Kits.
newmum|The New Mum Pack is for bleeding after a birth. This group needs 2 School Kits.', true),
  ('d13-5', 13, 5, 'Man, 30 · for his wife', 'man', 'moon|Wife leaks at night
smile|Her days are fine
walk|He’s here now', 'night', 'pickup', 'I can sleep through the night now. Thank you!', 'Right pack! He’s here now and can take it home. Pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is leaks at night: the Night Pack, with long overnight pads.
school|The School Kit is a term of pads for a student. Here the need is leaks at night: the Night Pack, with long overnight pads.
comfort|The Comfort Kit is for heavy or painful days. Here the need is leaks at night: the Night Pack, with long overnight pads.
reusable|Reusable pads need clean water and a place to dry them. Here the need is leaks at night: the Night Pack, with long overnight pads.
liners|Liners only cover light days. Here the need is leaks at night: the Night Pack, with long overnight pads.
sport|The Active Pack is for sport and swimming. Here the need is leaks at night: the Night Pack, with long overnight pads.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is leaks at night: the Night Pack, with long overnight pads.', true),
  ('d14-1', 14, 1, 'Three friends', 'teens', 'book|All need term pads
smile|Normal periods
walk|Pass the bank daily', 'school,school,school', 'pickup', 'All three of us are ready for term!', 'Right packs! They pass every day, so pickup is free.', 'Each friend needs her own School Kit.', 'starter|The Starter Bag is for a first period. This group needs 3 School Kits.
comfort|The Comfort Kit is for heavy or painful days. This group needs 3 School Kits.
reusable|Reusable pads need clean water and a place to dry them. This group needs 3 School Kits.
liners|Liners only cover light days. This group needs 3 School Kits.
night|The Night Pack is for leaks at night. This group needs 3 School Kits.
sport|The Active Pack is for sport and swimming. This group needs 3 School Kits.
newmum|The New Mum Pack is for bleeding after a birth. This group needs 3 School Kits.', true),
  ('d14-2', 14, 2, 'Grandmother · with 2 granddaughters', 'grandma', 'swim|One swims
drops|One has heavy days
house|Live next door', 'sport,comfort', 'pickup', 'Both girls are sorted. Thank you!', 'Right packs! They live next door, so pickup is free.', 'The swimmer needs an Active Pack; the girl with heavy days needs a Comfort Kit.', 'starter|The Starter Bag is for a first period. This group needs an Active Pack and a Comfort Kit.
school|The School Kit is a term of pads for a student. This group needs an Active Pack and a Comfort Kit.
reusable|Reusable pads need clean water and a place to dry them. This group needs an Active Pack and a Comfort Kit.
liners|Liners only cover light days. This group needs an Active Pack and a Comfort Kit.
night|The Night Pack is for leaks at night. This group needs an Active Pack and a Comfort Kit.
newmum|The New Mum Pack is for bleeding after a birth. This group needs an Active Pack and a Comfort Kit.', true),
  ('d14-3', 14, 3, 'Mother and daughter, 16', 'motherDaughter', 'smile|Mother: spotting only
ball|Daughter plays football
noWalk|Live far out of town', 'liners,sport', 'delivery', 'Just what we both needed!', 'Right packs! They live far out of town, so delivery was the kind choice.', 'Light Days Pack for the mother and an Active Pack for her daughter.', 'starter|The Starter Bag is for a first period. This group needs a Light Days Pack and an Active Pack.
school|The School Kit is a term of pads for a student. This group needs a Light Days Pack and an Active Pack.
comfort|The Comfort Kit is for heavy or painful days. This group needs a Light Days Pack and an Active Pack.
reusable|Reusable pads need clean water and a place to dry them. This group needs a Light Days Pack and an Active Pack.
night|The Night Pack is for leaks at night. This group needs a Light Days Pack and an Active Pack.
newmum|The New Mum Pack is for bleeding after a birth. This group needs a Light Days Pack and an Active Pack.', true),
  ('d14-4', 14, 4, 'Woman, 30', 'woman', 'water|Water and a yard
smile|Normal flow
walk|Passes on Fridays', 'reusable', 'pickup', 'No more buying pads every month. Thank you!', 'Right pack! She passes on Fridays, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
school|The School Kit is a term of pads for a student. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
comfort|The Comfort Kit is for heavy or painful days. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
liners|Liners only cover light days. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
night|The Night Pack is for leaks at night. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
sport|The Active Pack is for sport and swimming. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.', true),
  ('d14-5', 14, 5, 'Girl, 12', 'girl', 'drop|First period today
noWalk|Sick in bed
house|Lives 20 min away', 'starter', 'delivery', 'Now I know what to do. Thank you!', 'Right pack! She’s sick in bed, so delivery saves her the trip.', NULL, 'school|The School Kit is a term of pads for a student. Here the need is a first period: the Starter Bag with its simple guide.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a first period: the Starter Bag with its simple guide.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a first period: the Starter Bag with its simple guide.
liners|Liners only cover light days. Here the need is a first period: the Starter Bag with its simple guide.
night|The Night Pack is for leaks at night. Here the need is a first period: the Starter Bag with its simple guide.
sport|The Active Pack is for sport and swimming. Here the need is a first period: the Starter Bag with its simple guide.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is a first period: the Starter Bag with its simple guide.', true),
  ('d15-1', 15, 1, 'Group of 3 teens', 'teens', 'drop|Two have just started
drops|One has heavy days
walk|Live on this street', 'starter,starter,comfort', 'pickup', 'Thanks! We feel so much better.', 'Right packs! They live on this street, so pickup is free.', 'Two Starter Bags for the girls who just started and a Comfort Kit for heavy days.', 'school|The School Kit is a term of pads for a student. This group needs 2 Starter Bags and a Comfort Kit.
reusable|Reusable pads need clean water and a place to dry them. This group needs 2 Starter Bags and a Comfort Kit.
liners|Liners only cover light days. This group needs 2 Starter Bags and a Comfort Kit.
night|The Night Pack is for leaks at night. This group needs 2 Starter Bags and a Comfort Kit.
sport|The Active Pack is for sport and swimming. This group needs 2 Starter Bags and a Comfort Kit.
newmum|The New Mum Pack is for bleeding after a birth. This group needs 2 Starter Bags and a Comfort Kit.', true),
  ('d15-2', 15, 2, 'Hostel warden · for 2 girls', 'woman', 'moon|Both leak at night
house|Hostel is 2 min away
walk|The warden will carry it', 'night,night', 'pickup', 'The girls will sleep well tonight. Thank you!', 'Right packs! The hostel is 2 minutes away, so pickup is free.', 'Each girl needs her own Night Pack.', 'starter|The Starter Bag is for a first period. This group needs 2 Night Packs.
school|The School Kit is a term of pads for a student. This group needs 2 Night Packs.
comfort|The Comfort Kit is for heavy or painful days. This group needs 2 Night Packs.
reusable|Reusable pads need clean water and a place to dry them. This group needs 2 Night Packs.
liners|Liners only cover light days. This group needs 2 Night Packs.
sport|The Active Pack is for sport and swimming. This group needs 2 Night Packs.
newmum|The New Mum Pack is for bleeding after a birth. This group needs 2 Night Packs.', true),
  ('d15-3', 15, 3, 'Grandmother · with 2 granddaughters', 'grandma', 'book|Both start term Monday
smile|Normal periods
noWalk|Live in the next town', 'school,school', 'delivery', 'They’re ready for the whole term!', 'Right packs! They live in the next town, so delivery was the kind choice.', 'Each girl needs her own School Kit.', 'starter|The Starter Bag is for a first period. This group needs 2 School Kits.
comfort|The Comfort Kit is for heavy or painful days. This group needs 2 School Kits.
reusable|Reusable pads need clean water and a place to dry them. This group needs 2 School Kits.
liners|Liners only cover light days. This group needs 2 School Kits.
night|The Night Pack is for leaks at night. This group needs 2 School Kits.
sport|The Active Pack is for sport and swimming. This group needs 2 School Kits.
newmum|The New Mum Pack is for bleeding after a birth. This group needs 2 School Kits.', true),
  ('d15-4', 15, 4, 'Man, 34 · for his wife', 'man', 'baby|Wife gave birth last week
house|Lives 10 min away
walk|He’s here now', 'newmum', 'pickup', 'One less thing to worry about with the baby. Thank you!', 'Right pack! He’s here now and can take it home, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
school|The School Kit is a term of pads for a student. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
comfort|The Comfort Kit is for heavy or painful days. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
reusable|Reusable pads need clean water and a place to dry them. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
liners|Liners only cover light days. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
night|The Night Pack is for leaks at night. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
sport|The Active Pack is for sport and swimming. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.', true),
  ('d15-5', 15, 5, 'Girl, 15', 'girl', 'smile|Spotting only
clock|End of her period
walk|Lives 5 min away', 'liners', 'pickup', 'Just what I needed for the last days. Thank you!', 'Right pack! She lives 5 minutes away, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is light days only: the Light Days Pack.
school|The School Kit is a term of pads for a student. Here the need is light days only: the Light Days Pack.
comfort|The Comfort Kit is for heavy or painful days. Here the need is light days only: the Light Days Pack.
reusable|Reusable pads need clean water and a place to dry them. Here the need is light days only: the Light Days Pack.
night|The Night Pack is for leaks at night. Here the need is light days only: the Light Days Pack.
sport|The Active Pack is for sport and swimming. Here the need is light days only: the Light Days Pack.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is light days only: the Light Days Pack.', true),
  ('d16-1', 16, 1, 'Netball team · 3 girls', 'teens', 'ball|All play netball
swim|Training includes swimming
walk|Train next door', 'sport,sport,sport', 'pickup', 'Game on! Thank you!', 'Right packs! They train next door, so pickup is free.', 'Each player needs her own Active Pack.', 'starter|The Starter Bag is for a first period. This group needs 3 Active Packs.
school|The School Kit is a term of pads for a student. This group needs 3 Active Packs.
comfort|The Comfort Kit is for heavy or painful days. This group needs 3 Active Packs.
reusable|Reusable pads need clean water and a place to dry them. This group needs 3 Active Packs.
liners|Liners only cover light days. This group needs 3 Active Packs.
night|The Night Pack is for leaks at night. This group needs 3 Active Packs.
newmum|The New Mum Pack is for bleeding after a birth. This group needs 3 Active Packs.', true),
  ('d16-2', 16, 2, 'Mother and daughter, 12', 'motherDaughter', 'drops|Mother: very heavy days
drop|Daughter: first period
walk|They live nearby', 'comfort,starter', 'pickup', 'Thank you for looking after both of us!', 'Right packs! They live nearby, so pickup is free.', 'A Comfort Kit for the mother and a Starter Bag for her daughter.', 'school|The School Kit is a term of pads for a student. This group needs a Comfort Kit and a Starter Bag.
reusable|Reusable pads need clean water and a place to dry them. This group needs a Comfort Kit and a Starter Bag.
liners|Liners only cover light days. This group needs a Comfort Kit and a Starter Bag.
night|The Night Pack is for leaks at night. This group needs a Comfort Kit and a Starter Bag.
sport|The Active Pack is for sport and swimming. This group needs a Comfort Kit and a Starter Bag.
newmum|The New Mum Pack is for bleeding after a birth. This group needs a Comfort Kit and a Starter Bag.', true),
  ('d16-3', 16, 3, 'Grandmother · with 2 granddaughters', 'grandma', 'moon|One leaks at night
smile|One has light days
noWalk|Live far away', 'night,liners', 'delivery', 'Both girls are sorted. Thank you, my dear!', 'Right packs! They live far away, so delivery was the kind choice.', 'A Night Pack for night leaks and a Light Days Pack for light days.', 'starter|The Starter Bag is for a first period. This group needs a Night Pack and a Light Days Pack.
school|The School Kit is a term of pads for a student. This group needs a Night Pack and a Light Days Pack.
comfort|The Comfort Kit is for heavy or painful days. This group needs a Night Pack and a Light Days Pack.
reusable|Reusable pads need clean water and a place to dry them. This group needs a Night Pack and a Light Days Pack.
sport|The Active Pack is for sport and swimming. This group needs a Night Pack and a Light Days Pack.
newmum|The New Mum Pack is for bleeding after a birth. This group needs a Night Pack and a Light Days Pack.', true),
  ('d16-4', 16, 4, 'Woman, 37', 'woman', 'drops|Heavy, painful days
noWalk|Can’t travel this week
house|Lives across town', 'comfort', 'delivery', 'This makes the heavy days easier. Thank you!', 'Right pack! She can’t travel this week, so delivery was the only way.', NULL, 'starter|The Starter Bag is for a first period. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
school|The School Kit is a term of pads for a student. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
reusable|Reusable pads need clean water and a place to dry them. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
liners|Liners only cover light days. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
night|The Night Pack is for leaks at night. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
sport|The Active Pack is for sport and swimming. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.', true),
  ('d16-5', 16, 5, 'Student, SS2', 'student', 'book|Pads for the term
smile|Normal flow
walk|Walks past daily', 'school', 'pickup', 'I won’t miss school this term. Thank you!', 'Right pack! She walks past daily, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is a whole term of normal periods: the School Kit.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a whole term of normal periods: the School Kit.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a whole term of normal periods: the School Kit.
liners|Liners only cover light days. Here the need is a whole term of normal periods: the School Kit.
night|The Night Pack is for leaks at night. Here the need is a whole term of normal periods: the School Kit.
sport|The Active Pack is for sport and swimming. Here the need is a whole term of normal periods: the School Kit.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is a whole term of normal periods: the School Kit.', true),
  ('d17-1', 17, 1, 'Woman, 31', 'woman', 'noWater|No water at home
drops|Heavy flow
walk|Asks for reusables', 'comfort', 'pickup', 'The heat patch helped so much. Thank you!', 'Right pack! She is here now, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
school|The School Kit is a term of pads for a student. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
reusable|She asked for reusables, but with no water at home they’re hard to keep clean. Save the only Reusable Year for someone who can wash them.
liners|Liners only cover light days. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
night|The Night Pack is for leaks at night. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
sport|The Active Pack is for sport and swimming. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.', true),
  ('d17-2', 17, 2, 'Woman, 29', 'woman', 'water|Water and a sunny yard
smile|Normal flow
walk|Passes on Fridays', 'reusable', 'pickup', 'These will last me all year. Thank you!', 'Right pack! She passes on Fridays, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
school|The School Kit is a term of pads for a student. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
comfort|The Comfort Kit is for heavy or painful days. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
liners|Liners only cover light days. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
night|The Night Pack is for leaks at night. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
sport|The Active Pack is for sport and swimming. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.', true),
  ('d17-3', 17, 3, 'Girl, 13', 'girl', 'drop|First period
noWalk|Too unwell to come back
house|Lives 20 min away', 'starter', 'delivery', 'The little guide really helps. Thank you!', 'Right pack! She’s too unwell to come back, so delivery was the kind choice.', NULL, 'school|The School Kit is a term of pads for a student. Here the need is a first period: the Starter Bag with its simple guide.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a first period: the Starter Bag with its simple guide.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a first period: the Starter Bag with its simple guide.
liners|Liners only cover light days. Here the need is a first period: the Starter Bag with its simple guide.
night|The Night Pack is for leaks at night. Here the need is a first period: the Starter Bag with its simple guide.
sport|The Active Pack is for sport and swimming. Here the need is a first period: the Starter Bag with its simple guide.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is a first period: the Starter Bag with its simple guide.', true),
  ('d17-4', 17, 4, 'Student, SS3', 'student', 'book|Pads for the term
smile|Normal flow
walk|Walks past daily', 'school', 'pickup', 'I won’t miss school this term. Thank you!', 'Right pack! She walks past daily, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is a whole term of normal periods: the School Kit.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a whole term of normal periods: the School Kit.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a whole term of normal periods: the School Kit.
liners|Liners only cover light days. Here the need is a whole term of normal periods: the School Kit.
night|The Night Pack is for leaks at night. Here the need is a whole term of normal periods: the School Kit.
sport|The Active Pack is for sport and swimming. Here the need is a whole term of normal periods: the School Kit.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is a whole term of normal periods: the School Kit.', true),
  ('d17-5', 17, 5, 'Woman, 42', 'woman', 'moon|Leaks at night
clock|Needs them tonight
house|Lives 30 min away', 'night', 'delivery', 'I can sleep through the night now. Thank you!', 'Right pack! She needs them tonight and lives 30 minutes away. Delivery was worth the SP.', NULL, 'starter|The Starter Bag is for a first period. Here the need is leaks at night: the Night Pack, with long overnight pads.
school|The School Kit is a term of pads for a student. Here the need is leaks at night: the Night Pack, with long overnight pads.
comfort|The Comfort Kit is for heavy or painful days. Here the need is leaks at night: the Night Pack, with long overnight pads.
reusable|Reusable pads need clean water and a place to dry them. Here the need is leaks at night: the Night Pack, with long overnight pads.
liners|Liners only cover light days. Here the need is leaks at night: the Night Pack, with long overnight pads.
sport|The Active Pack is for sport and swimming. Here the need is leaks at night: the Night Pack, with long overnight pads.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is leaks at night: the Night Pack, with long overnight pads.', true),
  ('d18-1', 18, 1, 'Woman, 26', 'woman', 'drops|Heavy and painful
walk|Could come back Friday
clock|Needs them today', 'comfort', 'delivery', 'I can finally rest. Thank you!', 'Right pack! She could come Friday, but she needs them today. Delivery was worth the SP.', NULL, 'starter|The Starter Bag is for a first period. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
school|The School Kit is a term of pads for a student. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
reusable|Reusable pads need clean water and a place to dry them. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
liners|Liners only cover light days. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
night|The Night Pack is for leaks at night. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
sport|The Active Pack is for sport and swimming. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.', true),
  ('d18-2', 18, 2, 'Student, 17', 'student', 'book|Pads for the term
noWalk|Asks for delivery
walk|Walks past daily', 'school', 'pickup', 'No more worrying in class. Thank you!', 'Right pack! She asked for delivery, but she walks past every day. Pickup is free and just as quick.', NULL, 'starter|The Starter Bag is for a first period. Here the need is a whole term of normal periods: the School Kit.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a whole term of normal periods: the School Kit.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a whole term of normal periods: the School Kit.
liners|Liners only cover light days. Here the need is a whole term of normal periods: the School Kit.
night|The Night Pack is for leaks at night. Here the need is a whole term of normal periods: the School Kit.
sport|The Active Pack is for sport and swimming. Here the need is a whole term of normal periods: the School Kit.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is a whole term of normal periods: the School Kit.', true),
  ('d18-3', 18, 3, 'Man, 35 · for his wife', 'man', 'baby|Wife had a baby yesterday
house|Lives 2 min away
walk|Waiting at the counter', 'newmum', 'pickup', 'One less thing to worry about with the baby. Thank you!', 'Right pack! He’s at the counter and lives 2 minutes away, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
school|The School Kit is a term of pads for a student. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
comfort|The Comfort Kit is for heavy or painful days. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
reusable|Reusable pads need clean water and a place to dry them. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
liners|Liners only cover light days. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
night|The Night Pack is for leaks at night. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.
sport|The Active Pack is for sport and swimming. Here the need is bleeding after a birth: the New Mum Pack, with maternity pads.', true),
  ('d18-4', 18, 4, 'Girl, 14', 'girl', 'swim|Swim trials tomorrow
smile|Regular periods
walk|Lives next door', 'sport', 'pickup', 'I can play without worrying. Thank you!', 'Right pack! She lives next door, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
school|The School Kit is a term of pads for a student. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
comfort|The Comfort Kit is for heavy or painful days. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
reusable|Reusable pads need clean water and a place to dry them. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
liners|Liners only cover light days. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
night|The Night Pack is for leaks at night. Here the need is sport and swimming: the Active Pack, with period pants and tampons.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is sport and swimming: the Active Pack, with period pants and tampons.', true),
  ('d18-5', 18, 5, 'Woman, 55', 'woman', 'smile|Spotting now and then
noMoney|Wants the cheapest pack
walk|Passes daily', 'liners', 'pickup', 'Light and easy. Thank you!', 'Right pack! She passes daily, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is light days only: the Light Days Pack.
school|The School Kit is a term of pads for a student. Here the need is light days only: the Light Days Pack.
comfort|The Comfort Kit is for heavy or painful days. Here the need is light days only: the Light Days Pack.
reusable|Reusable pads need clean water and a place to dry them. Here the need is light days only: the Light Days Pack.
night|The Night Pack is for leaks at night. Here the need is light days only: the Light Days Pack.
sport|The Active Pack is for sport and swimming. Here the need is light days only: the Light Days Pack.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is light days only: the Light Days Pack.', true),
  ('d19-1', 19, 1, 'Three cousins', 'teens', 'drop|Two have just started
ball|One plays football
house|Live on this street', 'starter,starter,sport', 'pickup', 'All three of us are sorted. Thanks!', 'Right packs! They live on this street, so pickup is free.', 'Two Starter Bags for the cousins who just started and an Active Pack for the footballer.', 'school|The School Kit is a term of pads for a student. This group needs 2 Starter Bags and an Active Pack.
comfort|The Comfort Kit is for heavy or painful days. This group needs 2 Starter Bags and an Active Pack.
reusable|Reusable pads need clean water and a place to dry them. This group needs 2 Starter Bags and an Active Pack.
liners|Liners only cover light days. This group needs 2 Starter Bags and an Active Pack.
night|The Night Pack is for leaks at night. This group needs 2 Starter Bags and an Active Pack.
newmum|The New Mum Pack is for bleeding after a birth. This group needs 2 Starter Bags and an Active Pack.', true),
  ('d19-2', 19, 2, 'Woman, 30', 'woman', 'noWater|No running water
drops|Heavy flow
noWalk|No transport', 'comfort', 'delivery', 'The heat patch helped so much. Thank you!', 'Right pack! She has no transport, so delivery was the only way.', NULL, 'starter|The Starter Bag is for a first period. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
school|The School Kit is a term of pads for a student. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
reusable|Reusable pads need clean water and a place to dry them. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
liners|Liners only cover light days. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
night|The Night Pack is for leaks at night. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
sport|The Active Pack is for sport and swimming. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.', true),
  ('d19-3', 19, 3, 'Mother and daughter, 15', 'motherDaughter', 'baby|Mother: new baby
book|Daughter: new term
walk|They’re here now', 'newmum,school', 'pickup', 'You thought of both of us. Thank you!', 'Right packs! They’re here now, so pickup is free.', 'The New Mum Pack for the mother and a School Kit for her daughter.', 'starter|The Starter Bag is for a first period. This group needs a New Mum Pack and a School Kit.
comfort|The Comfort Kit is for heavy or painful days. This group needs a New Mum Pack and a School Kit.
reusable|Reusable pads need clean water and a place to dry them. This group needs a New Mum Pack and a School Kit.
liners|Liners only cover light days. This group needs a New Mum Pack and a School Kit.
night|The Night Pack is for leaks at night. This group needs a New Mum Pack and a School Kit.
sport|The Active Pack is for sport and swimming. This group needs a New Mum Pack and a School Kit.', true),
  ('d19-4', 19, 4, 'Father, 40 · for his daughter, 12', 'fatherDaughter', 'drop|Her first period
noWalk|She stayed at home
walk|He’ll take it home', 'starter', 'pickup', 'The little guide really helps. Thank you!', 'Right pack! She stayed home, but he can take it to her. Pickup is free.', NULL, 'school|The School Kit is a term of pads for a student. Here the need is a first period: the Starter Bag with its simple guide.
comfort|The Comfort Kit is for heavy or painful days. Here the need is a first period: the Starter Bag with its simple guide.
reusable|Reusable pads need clean water and a place to dry them. Here the need is a first period: the Starter Bag with its simple guide.
liners|Liners only cover light days. Here the need is a first period: the Starter Bag with its simple guide.
night|The Night Pack is for leaks at night. Here the need is a first period: the Starter Bag with its simple guide.
sport|The Active Pack is for sport and swimming. Here the need is a first period: the Starter Bag with its simple guide.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is a first period: the Starter Bag with its simple guide.', true),
  ('d19-5', 19, 5, 'Woman, 34', 'woman', 'moon|Leaks at night
clock|Can come back Monday
smile|Days are fine', 'night', 'pickup', 'I can sleep through the night now. Thank you!', 'Right pack! She can come back on Monday, so pickup saves the SP.', NULL, 'starter|The Starter Bag is for a first period. Here the need is leaks at night: the Night Pack, with long overnight pads.
school|The School Kit is a term of pads for a student. Here the need is leaks at night: the Night Pack, with long overnight pads.
comfort|The Comfort Kit is for heavy or painful days. Here the need is leaks at night: the Night Pack, with long overnight pads.
reusable|Reusable pads need clean water and a place to dry them. Here the need is leaks at night: the Night Pack, with long overnight pads.
liners|Liners only cover light days. Here the need is leaks at night: the Night Pack, with long overnight pads.
sport|The Active Pack is for sport and swimming. Here the need is leaks at night: the Night Pack, with long overnight pads.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is leaks at night: the Night Pack, with long overnight pads.', true),
  ('d20-1', 20, 1, 'Grandmother · with 2 granddaughters', 'grandma', 'drop|Both just started
noWalk|Live far, no bus
girls|Ages 10 and 12', 'starter,starter', 'delivery', 'My girls are ready now. Thank you!', 'Right packs! They live far with no bus, so delivery was the kind choice.', 'Each girl needs her own Starter Bag.', 'school|The School Kit is a term of pads for a student. This group needs 2 Starter Bags.
comfort|The Comfort Kit is for heavy or painful days. This group needs 2 Starter Bags.
reusable|Reusable pads need clean water and a place to dry them. This group needs 2 Starter Bags.
liners|Liners only cover light days. This group needs 2 Starter Bags.
night|The Night Pack is for leaks at night. This group needs 2 Starter Bags.
sport|The Active Pack is for sport and swimming. This group needs 2 Starter Bags.
newmum|The New Mum Pack is for bleeding after a birth. This group needs 2 Starter Bags.', true),
  ('d20-2', 20, 2, 'Swim team · 3 girls', 'teens', 'swim|All on the swim team
smile|Regular periods
walk|The pool is next door', 'sport,sport,sport', 'pickup', 'See you at the gala!', 'Right packs! The pool is next door, so pickup is free.', 'Each swimmer needs her own Active Pack.', 'starter|The Starter Bag is for a first period. This group needs 3 Active Packs.
school|The School Kit is a term of pads for a student. This group needs 3 Active Packs.
comfort|The Comfort Kit is for heavy or painful days. This group needs 3 Active Packs.
reusable|Reusable pads need clean water and a place to dry them. This group needs 3 Active Packs.
liners|Liners only cover light days. This group needs 3 Active Packs.
night|The Night Pack is for leaks at night. This group needs 3 Active Packs.
newmum|The New Mum Pack is for bleeding after a birth. This group needs 3 Active Packs.', true),
  ('d20-3', 20, 3, 'Woman, 28', 'woman', 'water|Water and a yard
smile|Normal flow
noWalk|Moving house this week', 'reusable', 'delivery', 'These will last me all year. Thank you!', 'Right pack! She’s busy moving house, so delivery gets it to her.', NULL, 'starter|The Starter Bag is for a first period. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
school|The School Kit is a term of pads for a student. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
comfort|The Comfort Kit is for heavy or painful days. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
liners|Liners only cover light days. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
night|The Night Pack is for leaks at night. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
sport|The Active Pack is for sport and swimming. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is pads that last, with clean water and a place to dry them: the Reusable Year.', true),
  ('d20-4', 20, 4, 'Man, 31 · for his wife', 'man', 'drops|Wife: heavy, cramps
walk|Waiting outside
house|Lives 5 min away', 'comfort', 'pickup', 'The heat patch helped so much. Thank you!', 'Right pack! He’s waiting outside and lives close, so pickup is free.', NULL, 'starter|The Starter Bag is for a first period. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
school|The School Kit is a term of pads for a student. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
reusable|Reusable pads need clean water and a place to dry them. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
liners|Liners only cover light days. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
night|The Night Pack is for leaks at night. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
sport|The Active Pack is for sport and swimming. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is heavy or painful days: the Comfort Kit, with extra pads and a heat patch.', true),
  ('d20-5', 20, 5, 'Student, 20', 'student', 'smile|Spotting only
book|Exams all week
noWalk|Can’t leave campus', 'liners', 'delivery', 'Light and easy. Thank you!', 'Right pack! She can’t leave campus, so delivery gets them to her.', NULL, 'starter|The Starter Bag is for a first period. Here the need is light days only: the Light Days Pack.
school|The School Kit is a term of pads for a student. Here the need is light days only: the Light Days Pack.
comfort|The Comfort Kit is for heavy or painful days. Here the need is light days only: the Light Days Pack.
reusable|Reusable pads need clean water and a place to dry them. Here the need is light days only: the Light Days Pack.
night|The Night Pack is for leaks at night. Here the need is light days only: the Light Days Pack.
sport|The Active Pack is for sport and swimming. Here the need is light days only: the Light Days Pack.
newmum|The New Mum Pack is for bleeding after a birth. Here the need is light days only: the Light Days Pack.', true);

COMMIT;
