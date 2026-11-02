BEGIN;

INSERT INTO giveday_products (id, name, cost, absorbent, sort_order, active) VALUES
  ('pads', 'Pads', 4, true, 1, true),
  ('liners', 'Liners', 2, false, 2, true),
  ('tampons', 'Tampons', 5, true, 3, true),
  ('cup', 'Cup', 12, true, 4, true),
  ('pants', 'Period pants', 10, true, 5, true),
  ('reusable', 'Reusable pads', 8, true, 6, true),
  ('heat', 'Heat patch', 3, false, 7, true),
  ('wipes', 'Wipes', 2, false, 8, true),
  ('washbag', 'Wash bag', 3, false, 9, true),
  ('guide', 'Period guide', 1, false, 10, true)
ON CONFLICT DO NOTHING;

INSERT INTO giveday_places (id, name, kind, teaser, quote, host_name, host_look, who, people_total, people_options, thanks, facts, map_x, map_y, for_self, min_age, sort_order, active) VALUES
  ('hope-house', 'Hope House', 'Motherless home', 'A motherless home for 18 girls. Some have never had pads of their own.', 'Welcome! Our girls share everything. Something of their own would mean so much.', 'Mama Ronke', 'woman', 'girls', 18, '6,12,18', 'Every one of my girls has her own kit now!', 'girls|pink|18 girls live here
cake|peach|Ages 10 to 17; 6 not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 70, 205, false, 13, 1, true),
  ('bright-school', 'Bright School', 'School', 'Girls stay home on period days because the toilets have no bins.', 'Some girls miss three days every month. Help them stay in class!', 'Miss Ada', 'woman', 'girls', 40, '10,20,40', 'Now my girls can stay in class all month!', 'girls|pink|40 girls in JSS 1 to 3
cake|peach|Ages 11 to 14
noDrop|green|No running water at school
run|pink|PE twice a week; no bins', 320, 205, false, 13, 2, true),
  ('hope-camp', 'Hope Camp', 'IDP camp', '30 women and girls share tents, water points and dark latrines.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Hauwa', 'woman', 'women and girls', 30, '10,20,30', 'You have given us dignity today.', 'girls|pink|30 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 70, 305, false, 13, 3, true),
  ('girls-fc', 'Girls FC', 'Football club', '16 players train every day. The big tournament is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ify', 'student', 'players', 16, '6,12,16', 'Nobody sits out on Saturday now!', 'girls|pink|16 players
cake|peach|Ages 12 to 17
ball|green|Train daily; tournament Saturday
drop|pink|Changing room with showers', 320, 305, false, 13, 4, true),
  ('oshodi-road', 'Oshodi Road', 'Roadside outreach', 'Young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Bisi', 'student', 'women', 12, '4,8,12', 'They felt seen today. Thank you.', 'girls|pink|12 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Sleep outdoors, always moving', 70, 505, false, 13, 5, true),
  ('balogun-market', 'Balogun Market', 'Market', 'Girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'woman', 'girls', 15, '5,10,15', 'My girls can work without worry now!', 'girls|pink|15 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 320, 505, false, 13, 6, true),
  ('my-kit', 'Pharmacy', 'Stock my own kit', 'Stock up for your own next period. Pick what suits your body and your week.', 'My period is due next week. What do I need?', 'You', 'girl', 'you', 1, '1', 'Ready for next week. I’ve got this!', 'drop|pink|Heavy flow on days 1 to 2
pain|peach|Cramps on day 1
run|green|PE on Tuesday
swim|pink|Swimming on Saturday', 195, 615, true, 13, 7, true),
  ('ikire-village', 'Ikire Village', 'Rural village', '25 girls live 2 hours from the nearest shop. Water comes from the river.', 'When pads run out, the girls use leaves and old cloth. They need something that lasts.', 'Nurse Grace', 'woman', 'girls', 25, '10,15,25', 'These will last them a whole year!', 'girls|pink|25 girls
cake|peach|Ages 10 to 16
river|green|River water only
road|pink|Shop is 2 hours away', 320, 615, false, 13, 8, true)
ON CONFLICT DO NOTHING;

INSERT INTO giveday_needs (id, place_id, product_id, rating, why) VALUES
  ('hope-house:pads', 'hope-house', 'pads', 'good', 'Easy to use for girls new to periods.'),
  ('hope-house:guide', 'hope-house', 'guide', 'good', '6 girls haven’t started: a simple guide helps them get ready.'),
  ('hope-house:reusable', 'hope-house', 'reusable', 'good', 'Clean water and a laundry line mean reusables can last a year.'),
  ('hope-house:washbag', 'hope-house', 'washbag', 'good', 'With one shared bathroom, a bag keeps things private.'),
  ('hope-house:liners', 'hope-house', 'liners', 'ok', 'Handy for light days, but not essential.'),
  ('hope-house:heat', 'hope-house', 'heat', 'ok', 'Nice for cramps, but pads matter more here.'),
  ('hope-house:pants', 'hope-house', 'pants', 'ok', 'Comfortable, but costly for 18 girls.'),
  ('hope-house:tampons', 'hope-house', 'tampons', 'ok', 'Fine for older girls once someone shows them how; pads are easier to start with.'),
  ('hope-house:cup', 'hope-house', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard with one shared bathroom.'),
  ('hope-house:wipes', 'hope-house', 'wipes', 'ok', 'There is running water here, so wipes are less needed.'),
  ('bright-school:pads', 'bright-school', 'pads', 'good', 'Simple and quick to change between lessons.'),
  ('bright-school:guide', 'bright-school', 'guide', 'good', 'Young girls learn what is normal and what to do.'),
  ('bright-school:washbag', 'bright-school', 'washbag', 'good', 'No bins at school: a bag carries used pads home discreetly.'),
  ('bright-school:liners', 'bright-school', 'liners', 'good', 'Good for light days and discharge at this age.'),
  ('bright-school:wipes', 'bright-school', 'wipes', 'good', 'No running water at school, so wipes help.'),
  ('bright-school:heat', 'bright-school', 'heat', 'ok', 'Helps cramps, but less important in class.'),
  ('bright-school:pants', 'bright-school', 'pants', 'ok', 'Great for PE, but costly for many girls.'),
  ('bright-school:reusable', 'bright-school', 'reusable', 'poor', 'There is nowhere at school to wash or dry them.'),
  ('bright-school:tampons', 'bright-school', 'tampons', 'ok', 'Possible for some, but most young girls start with pads.'),
  ('bright-school:cup', 'bright-school', 'cup', 'poor', 'A cup needs clean water to rinse, and the school has none.'),
  ('hope-camp:pads', 'hope-camp', 'pads', 'good', 'No washing needed in a crowded camp.'),
  ('hope-camp:washbag', 'hope-camp', 'washbag', 'good', 'Privacy in a shared tent.'),
  ('hope-camp:wipes', 'hope-camp', 'wipes', 'good', 'Water points are shared and queued.'),
  ('hope-camp:heat', 'hope-camp', 'heat', 'ok', 'Comforting, but pads come first.'),
  ('hope-camp:reusable', 'hope-camp', 'reusable', 'ok', 'Water exists, but drying privately in a crowded camp is hard.'),
  ('hope-camp:guide', 'hope-camp', 'guide', 'ok', 'Most women know the basics; girls may need it.'),
  ('hope-camp:liners', 'hope-camp', 'liners', 'ok', 'Useful, not essential.'),
  ('hope-camp:tampons', 'hope-camp', 'tampons', 'ok', 'Works without much water, but hands must be clean.'),
  ('hope-camp:cup', 'hope-camp', 'cup', 'poor', 'Needs boiling and privacy, which the camp lacks.'),
  ('hope-camp:pants', 'hope-camp', 'pants', 'ok', 'Need private drying space.'),
  ('girls-fc:pants', 'girls-fc', 'pants', 'good', 'Leak-proof backup under match kit.'),
  ('girls-fc:pads', 'girls-fc', 'pads', 'good', 'A reliable everyday choice.'),
  ('girls-fc:heat', 'girls-fc', 'heat', 'good', 'Cramps before a match are common.'),
  ('girls-fc:washbag', 'girls-fc', 'washbag', 'good', 'Privacy in a shared changing room.'),
  ('girls-fc:tampons', 'girls-fc', 'tampons', 'ok', 'Good for sport once a player is comfortable using them.'),
  ('girls-fc:cup', 'girls-fc', 'cup', 'ok', 'Good for sport, but takes practice; showers help.'),
  ('girls-fc:liners', 'girls-fc', 'liners', 'ok', 'Fine for light days.'),
  ('girls-fc:wipes', 'girls-fc', 'wipes', 'ok', 'They have showers, so wipes matter less.'),
  ('girls-fc:guide', 'girls-fc', 'guide', 'ok', 'Most players know the basics.'),
  ('girls-fc:reusable', 'girls-fc', 'reusable', 'ok', 'Fine on home days, less so on match day.'),
  ('oshodi-road:pads', 'oshodi-road', 'pads', 'good', 'No water needed, and easy to change anywhere.'),
  ('oshodi-road:wipes', 'oshodi-road', 'wipes', 'good', 'Clean up without water.'),
  ('oshodi-road:washbag', 'oshodi-road', 'washbag', 'good', 'Privacy, and a clean place to keep pads.'),
  ('oshodi-road:heat', 'oshodi-road', 'heat', 'good', 'Cramps are hard with nowhere to rest.'),
  ('oshodi-road:liners', 'oshodi-road', 'liners', 'ok', 'Useful, but pads matter more.'),
  ('oshodi-road:guide', 'oshodi-road', 'guide', 'ok', 'Most already know the basics.'),
  ('oshodi-road:tampons', 'oshodi-road', 'tampons', 'ok', 'Works without water, but hands need to be clean.'),
  ('oshodi-road:reusable', 'oshodi-road', 'reusable', 'poor', 'There is nowhere to wash and dry them.'),
  ('oshodi-road:cup', 'oshodi-road', 'cup', 'poor', 'A cup needs clean water and boiling.'),
  ('oshodi-road:pants', 'oshodi-road', 'pants', 'poor', 'Need washing and drying, which is hard on the road.'),
  ('balogun-market:pads', 'balogun-market', 'pads', 'good', 'Holds up through long hours at the stall.'),
  ('balogun-market:washbag', 'balogun-market', 'washbag', 'good', 'Carry spares and used pads discreetly.'),
  ('balogun-market:wipes', 'balogun-market', 'wipes', 'good', 'Toilets cost money, so wipes help them clean up anywhere.'),
  ('balogun-market:heat', 'balogun-market', 'heat', 'good', 'Long hours on their feet with cramps.'),
  ('balogun-market:liners', 'balogun-market', 'liners', 'ok', 'Handy, not essential.'),
  ('balogun-market:guide', 'balogun-market', 'guide', 'ok', 'Most know the basics.'),
  ('balogun-market:tampons', 'balogun-market', 'tampons', 'ok', 'Possible, but hands must be clean and toilets are far.'),
  ('balogun-market:cup', 'balogun-market', 'cup', 'poor', 'Hard to rinse in a paid public toilet.'),
  ('balogun-market:reusable', 'balogun-market', 'reusable', 'poor', 'No time or place to wash during the day.'),
  ('balogun-market:pants', 'balogun-market', 'pants', 'ok', 'Good backup, but must be washed every night.'),
  ('my-kit:pads', 'my-kit', 'pads', 'good', 'Heavy flow on days 1 to 2 needs a good pad.'),
  ('my-kit:tampons', 'my-kit', 'tampons', 'good', 'Lets you swim on Saturday, at any age once you are comfortable.'),
  ('my-kit:cup', 'my-kit', 'cup', 'good', 'Lets you swim, and one cup lasts for years.'),
  ('my-kit:heat', 'my-kit', 'heat', 'good', 'Helps with cramps on day 1.'),
  ('my-kit:pants', 'my-kit', 'pants', 'good', 'Overnight backup on heavy days.'),
  ('my-kit:liners', 'my-kit', 'liners', 'ok', 'Fine for the last light days.'),
  ('my-kit:guide', 'my-kit', 'guide', 'ok', 'You already know the basics.'),
  ('my-kit:wipes', 'my-kit', 'wipes', 'ok', 'Handy, but optional.'),
  ('my-kit:washbag', 'my-kit', 'washbag', 'ok', 'Handy for school, but optional.'),
  ('my-kit:reusable', 'my-kit', 'reusable', 'ok', 'A good choice if you can wash them at home.'),
  ('ikire-village:reusable', 'ikire-village', 'reusable', 'good', 'Far from shops: reusables last a year. Boil river water to wash them.'),
  ('ikire-village:washbag', 'ikire-village', 'washbag', 'good', 'Keeps reusables clean between uses.'),
  ('ikire-village:guide', 'ikire-village', 'guide', 'good', 'Teaches how to wash and sun-dry reusables safely.'),
  ('ikire-village:pants', 'ikire-village', 'pants', 'good', 'Washable and long-lasting.'),
  ('ikire-village:pads', 'ikire-village', 'pads', 'ok', 'Helpful, but they run out and the shop is 2 hours away.'),
  ('ikire-village:liners', 'ikire-village', 'liners', 'ok', 'Run out quickly.'),
  ('ikire-village:heat', 'ikire-village', 'heat', 'ok', 'Comforting, not essential.'),
  ('ikire-village:wipes', 'ikire-village', 'wipes', 'ok', 'Run out quickly.'),
  ('ikire-village:tampons', 'ikire-village', 'tampons', 'poor', 'Run out fast, far from any shop.'),
  ('ikire-village:cup', 'ikire-village', 'cup', 'ok', 'Lasts years, but needs boiled water every cycle.')
ON CONFLICT DO NOTHING;

COMMIT;
