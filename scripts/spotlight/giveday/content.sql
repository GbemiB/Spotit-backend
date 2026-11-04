BEGIN;

DELETE FROM giveday_needs;
DELETE FROM giveday_places;
DELETE FROM giveday_areas;
DELETE FROM giveday_products;
DELETE FROM giveday_levels;
DELETE FROM giveday_travel;

INSERT INTO giveday_products (id, name, cost, absorbent, sort_order, active) VALUES
  ('pads', 'Pads', 2, true, 1, true),
  ('liners', 'Liners', 1, false, 2, true),
  ('tampons', 'Tampons', 2, true, 3, true),
  ('cup', 'Cup', 3, true, 4, true),
  ('pants', 'Period pants', 3, true, 5, true),
  ('reusable', 'Reusable pads', 3, true, 6, true),
  ('heat', 'Heat patch', 1, false, 7, true),
  ('wipes', 'Wipes', 1, false, 8, true),
  ('washbag', 'Wash bag', 1, false, 9, true),
  ('guide', 'Period guide', 1, false, 10, true);

INSERT INTO giveday_levels (level_number, name, radius_km, stars_needed, pass_cost, gift_budget, active) VALUES
  (1, 'Near home', 3, 0, 0, 5, true),
  (2, 'Your side of town', 8, 3, 0, 5, true),
  (3, 'Across the city', 15, 10, 2, 5, true),
  (4, 'Far side of Lagos', 30, 20, 3, 5, true),
  (5, 'All of Lagos', 999, 30, 5, 5, true);

INSERT INTO giveday_travel (id, label, cost, units_per_minute, sort_order, active) VALUES
  ('walk', 'Walk', 0, 8, 1, true),
  ('bike', 'Bike', 1, 25, 2, true),
  ('car', 'Car', 2, 50, 3, true);

INSERT INTO giveday_areas (id, name, region, map_x, map_y, sort_order, active) VALUES
  ('badagry', 'Badagry', 'Badagry', 2264, 3705, 1, true),
  ('ajido', 'Ajido', 'Badagry', 2857, 3672, 2, true),
  ('agbara', 'Agbara', 'Badagry', 4616, 2710, 3, true),
  ('ojo', 'Ojo', 'Ojo', 5440, 3185, 4, true),
  ('okokomaiko', 'Okokomaiko', 'Ojo', 5715, 2898, 5, true),
  ('iba', 'Iba', 'Ojo', 5880, 2876, 6, true),
  ('igando', 'Igando', 'Alimosho', 6209, 2190, 7, true),
  ('ikotun', 'Ikotun', 'Alimosho', 6451, 2223, 8, true),
  ('idimu', 'Idimu', 'Alimosho', 6352, 1847, 9, true),
  ('ayobo', 'Ayobo', 'Alimosho', 6176, 1571, 10, true),
  ('egbeda', 'Egbeda', 'Alimosho', 6704, 1747, 11, true),
  ('iyana-ipaja', 'Iyana Ipaja', 'Alimosho', 6737, 1526, 12, true),
  ('satellite-town', 'Satellite Town', 'Amuwo Odofin', 6264, 3340, 13, true),
  ('festac', 'Festac', 'Amuwo Odofin', 6627, 3130, 14, true),
  ('amuwo', 'Amuwo Odofin', 'Amuwo Odofin', 6836, 3241, 15, true),
  ('mile-2', 'Mile 2', 'Amuwo Odofin', 7012, 3185, 16, true),
  ('ajegunle', 'Ajegunle', 'Ajeromi', 7231, 3263, 17, true),
  ('apapa', 'Apapa', 'Apapa', 7495, 3329, 18, true),
  ('orile', 'Orile', 'Surulere', 7341, 2986, 19, true),
  ('ijora', 'Ijora', 'Apapa', 7583, 3097, 20, true),
  ('surulere', 'Surulere', 'Surulere', 7418, 2765, 21, true),
  ('ebute-metta', 'Ebute Metta', 'Mainland', 7715, 2931, 22, true),
  ('yaba', 'Yaba', 'Mainland', 7671, 2599, 23, true),
  ('akoka', 'Akoka', 'Mainland', 7825, 2522, 24, true),
  ('shomolu', 'Shomolu', 'Shomolu', 7726, 2334, 25, true),
  ('bariga', 'Bariga', 'Shomolu', 7825, 2300, 26, true),
  ('mushin', 'Mushin', 'Mushin', 7363, 2455, 27, true),
  ('isolo', 'Isolo', 'Oshodi-Isolo', 7056, 2433, 28, true),
  ('ejigbo', 'Ejigbo', 'Oshodi-Isolo', 6814, 2179, 29, true),
  ('oshodi', 'Oshodi', 'Oshodi-Isolo', 7253, 2157, 30, true),
  ('ilupeju', 'Ilupeju', 'Mushin', 7451, 2179, 31, true),
  ('palmgrove', 'Palmgrove', 'Shomolu', 7583, 2289, 32, true),
  ('gbagada', 'Gbagada', 'Kosofe', 7781, 2157, 33, true),
  ('oworonshoki', 'Oworonshoki', 'Kosofe', 7935, 2101, 34, true),
  ('maryland', 'Maryland', 'Ikeja', 7550, 1991, 35, true),
  ('ojota', 'Ojota', 'Kosofe', 7693, 1825, 36, true),
  ('ketu', 'Ketu', 'Kosofe', 7803, 1692, 37, true),
  ('mile-12', 'Mile 12', 'Kosofe', 7913, 1582, 38, true),
  ('ikosi', 'Ikosi', 'Kosofe', 7836, 1626, 39, true),
  ('magodo', 'Magodo', 'Kosofe', 7748, 1438, 40, true),
  ('ikeja', 'Ikeja', 'Ikeja', 7374, 1648, 41, true),
  ('ogba', 'Ogba', 'Ikeja', 7253, 1327, 42, true),
  ('agege', 'Agege', 'Agege', 7012, 1382, 43, true),
  ('ifako', 'Ifako-Ijaiye', 'Ifako-Ijaiye', 7143, 1128, 44, true),
  ('ojodu', 'Ojodu', 'Ikeja', 7561, 1250, 45, true),
  ('berger', 'Berger', 'Kosofe', 7671, 1150, 46, true),
  ('ikorodu', 'Ikorodu', 'Ikorodu', 9067, 1493, 47, true),
  ('igbogbo', 'Igbogbo', 'Ikorodu', 9287, 1825, 48, true),
  ('ijede', 'Ijede', 'Ikorodu', 10001, 1991, 49, true),
  ('imota', 'Imota', 'Ikorodu', 10660, 940, 50, true),
  ('lagos-island', 'Lagos Island', 'Island', 7858, 3263, 51, true),
  ('obalende', 'Obalende', 'Island', 7968, 3340, 52, true),
  ('ikoyi', 'Ikoyi', 'Island', 8297, 3296, 53, true),
  ('victoria-island', 'Victoria Island', 'Island', 8144, 3561, 54, true),
  ('oniru', 'Oniru', 'Eti-Osa', 8407, 3506, 55, true),
  ('lekki-phase-1', 'Lekki Phase 1', 'Eti-Osa', 8737, 3373, 56, true),
  ('ikate', 'Ikate', 'Eti-Osa', 8924, 3462, 57, true),
  ('chevron', 'Chevron', 'Eti-Osa', 9232, 3406, 58, true),
  ('osapa', 'Osapa', 'Eti-Osa', 9418, 3340, 59, true),
  ('ajah', 'Ajah', 'Eti-Osa', 9781, 3119, 60, true),
  ('sangotedo', 'Sangotedo', 'Eti-Osa', 10440, 3097, 61, true),
  ('abijo', 'Abijo', 'Ibeju-Lekki', 10825, 3119, 62, true),
  ('lakowe', 'Lakowe', 'Ibeju-Lekki', 11210, 3119, 63, true),
  ('eleko', 'Eleko', 'Ibeju-Lekki', 12419, 3351, 64, true),
  ('ibeju', 'Ibeju', 'Ibeju-Lekki', 13738, 3207, 65, true),
  ('epe', 'Epe', 'Epe', 14287, 1825, 66, true);

INSERT INTO giveday_places (id, area_id, name, kind, teaser, quote, host_name, host_look, who, people_total, thanks, facts, map_x, map_y, closes_at, for_self, min_age, sort_order, active) VALUES
  ('badagry-school', 'badagry', 'Bright Stars School', 'School', '24 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Miss Ada', 'teacher', 'girls', 24, 'Now my girls can stay in class all month!', 'girls|pink|24 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 2334, 3705, 780, false, 13, 1, true),
  ('badagry-market', 'badagry', 'Sunrise Market', 'Market', '21 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'market', 'girl traders', 21, 'My girls can work without worry now!', 'girls|pink|21 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 2321, 3746, 1050, false, 13, 2, true),
  ('badagry-busstop', 'badagry', 'Bus Stop Outreach', 'Roadside outreach', '12 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Kemi', 'outreach', 'young women', 12, 'They felt seen today. Thank you.', 'girls|pink|12 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 2286, 3772, 690, false, 13, 3, true),
  ('badagry-clinic', 'badagry', 'Teen Care Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 12, 'We can help every girl who walks in today.', 'girls|pink|About 12 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 2242, 3772, 660, false, 13, 4, true),
  ('badagry-youth', 'badagry', 'After-School Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Ronke', 'outreach', 'girls', 27, 'Now our girls are ready, any day.', 'girls|pink|27 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 2207, 3746, 1050, false, 13, 5, true),
  ('badagry-waterside', 'badagry', 'Creekside Settlement', 'Waterside community', '28 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Mama Efe', 'nurse', 'girls', 28, 'These will last them a whole year!', 'girls|pink|28 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 2194, 3705, 720, false, 13, 6, true),
  ('badagry-community', 'badagry', 'Family Help Centre', 'Community centre', 'Women and girls meet here weekly. Many share one tap and buy pads only when they can.', 'If a pack could last, it would change things for our families.', 'Mrs Hauwa', 'woman', 'women and girls', 21, 'This will last our families a long time.', 'girls|pink|21 women and girls
cake|peach|Ages 12 to 45
noDrop|green|No running water
coin|pink|Money is tight', 2207, 3664, 1020, false, 13, 7, true),
  ('badagry-football', 'badagry', 'Lagoon Strikers', 'Football club', '14 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 14, 'Nobody sits out on Saturday now!', 'girls|pink|14 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 2242, 3638, 630, false, 13, 8, true),
  ('badagry-shelter', 'badagry', 'Hope Camp', 'Temporary shelter', 'Families moved here after floods. 29 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Aunty Grace', 'woman', 'women and girls', 29, 'You have given us dignity today.', 'girls|pink|29 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 2286, 3638, 1020, false, 13, 9, true),
  ('badagry-community-2', 'badagry', 'Family Help Centre', 'Community centre', 'Women and girls meet here weekly. Many share one tap and buy pads only when they can.', 'If a pack could last, it would change things for our families.', 'Aunty Sade', 'woman', 'women and girls', 22, 'This will last our families a long time.', 'girls|pink|22 women and girls
cake|peach|Ages 12 to 45
drop|green|Running water on site
coin|pink|Money is tight', 2321, 3664, 960, false, 13, 10, true),
  ('ajido-school', 'ajido', 'Excel Girls’ School', 'School', '29 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Miss Zainab', 'teacher', 'girls', 29, 'Now my girls can stay in class all month!', 'girls|pink|29 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 2927, 3672, 810, false, 13, 11, true),
  ('ajido-market', 'ajido', 'Fresh Foods Market', 'Market', '20 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'market', 'girl traders', 20, 'My girls can work without worry now!', 'girls|pink|20 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 2911, 3717, 960, false, 13, 12, true),
  ('ajido-busstop', 'ajido', 'Roadside Help Point', 'Roadside outreach', '14 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Kemi', 'outreach', 'young women', 14, 'They felt seen today. Thank you.', 'girls|pink|14 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 2869, 3741, 690, false, 13, 13, true),
  ('ajido-clinic', 'ajido', 'Youth Health Corner', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Kemi', 'nurse', 'girls', 21, 'We can help every girl who walks in today.', 'girls|pink|About 21 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 2822, 3733, 720, false, 13, 14, true),
  ('ajido-youth', 'ajido', 'Study Corner', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 30, 'Now our girls are ready, any day.', 'girls|pink|30 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 2791, 3696, 1050, false, 13, 15, true),
  ('ajido-waterside', 'ajido', 'Canoe Landing Community', 'Waterside community', '27 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Aunty Toyin', 'nurse', 'girls', 27, 'These will last them a whole year!', 'girls|pink|27 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 2791, 3648, 750, false, 13, 16, true),
  ('ajido-community', 'ajido', 'Women’s Circle', 'Community centre', 'Women and girls meet here weekly. Many share one tap and buy pads only when they can.', 'If a pack could last, it would change things for our families.', 'Mrs Obi', 'woman', 'women and girls', 18, 'This will last our families a long time.', 'girls|pink|18 women and girls
cake|peach|Ages 12 to 45
noDrop|green|No running water
coin|pink|Money is tight', 2822, 3611, 930, false, 13, 17, true),
  ('ajido-football', 'ajido', 'Lagoon Strikers', 'Football club', '17 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 17, 'Nobody sits out on Saturday now!', 'girls|pink|17 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 2869, 3603, 570, false, 13, 18, true),
  ('ajido-shelter', 'ajido', 'Hope Camp', 'Temporary shelter', 'Families moved here after floods. 20 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Aunty Grace', 'woman', 'women and girls', 20, 'You have given us dignity today.', 'girls|pink|20 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 2911, 3627, 1050, false, 13, 19, true),
  ('agbara-school', 'agbara', 'Morning Star Academy', 'School', '28 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Okafor', 'teacher', 'girls', 28, 'Now my girls can stay in class all month!', 'girls|pink|28 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 4686, 2710, 810, false, 13, 20, true),
  ('agbara-market', 'agbara', 'Corner Market', 'Market', '12 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Nkechi', 'market', 'girl traders', 12, 'My girls can work without worry now!', 'girls|pink|12 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 4670, 2755, 960, false, 13, 21, true),
  ('agbara-busstop', 'agbara', 'Roadside Help Point', 'Roadside outreach', '13 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Bisi', 'outreach', 'young women', 13, 'They felt seen today. Thank you.', 'girls|pink|13 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 4628, 2779, 630, false, 13, 22, true),
  ('agbara-clinic', 'agbara', 'Family Health Centre', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 18, 'We can help every girl who walks in today.', 'girls|pink|About 18 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 4581, 2771, 720, false, 13, 23, true),
  ('agbara-youth', 'agbara', 'Library Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 19, 'Now our girls are ready, any day.', 'girls|pink|19 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 4550, 2734, 1050, false, 13, 24, true),
  ('agbara-hostel', 'agbara', 'Kingsway Girls’ Hostel', 'Girls’ hostel', '24 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Yemi', 'mamaRonke', 'girls', 24, 'The girls feel at home now. Thank you!', 'girls|pink|24 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 4550, 2686, 1080, false, 13, 25, true),
  ('agbara-home', 'agbara', 'Haven Home', 'Motherless home', 'A home for 16 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ronke', 'mama', 'girls', 16, 'Every one of my girls has her own kit now!', 'girls|pink|16 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 4581, 2649, 990, false, 13, 26, true),
  ('agbara-football', 'agbara', 'Girls FC', 'Football club', '21 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Halima', 'coach', 'players', 21, 'Nobody sits out on Saturday now!', 'girls|pink|21 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 4628, 2641, 570, false, 13, 27, true),
  ('agbara-training', 'agbara', 'Crafts Workshop', 'Training school', '11 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Adeola', 'teacher', 'apprentices', 11, 'My students can work in comfort now.', 'girls|pink|11 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 4670, 2665, 960, false, 13, 28, true),
  ('ojo-school', 'ojo', 'Morning Star Academy', 'School', '22 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Okafor', 'teacher', 'girls', 22, 'Now my girls can stay in class all month!', 'girls|pink|22 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 5510, 3185, 840, false, 13, 29, true),
  ('ojo-market', 'ojo', 'Sunrise Market', 'Market', '21 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'market', 'girl traders', 21, 'My girls can work without worry now!', 'girls|pink|21 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 5494, 3230, 1050, false, 13, 30, true),
  ('ojo-busstop', 'ojo', 'Roadside Help Point', 'Roadside outreach', '13 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Bisi', 'outreach', 'young women', 13, 'They felt seen today. Thank you.', 'girls|pink|13 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 5452, 3254, 630, false, 13, 31, true),
  ('ojo-clinic', 'ojo', 'Community Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Chika', 'nurse', 'girls', 13, 'We can help every girl who walks in today.', 'girls|pink|About 13 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 5405, 3246, 690, false, 13, 32, true),
  ('ojo-youth', 'ojo', 'Youth Hub', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Bimpe', 'outreach', 'girls', 15, 'Now our girls are ready, any day.', 'girls|pink|15 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 5374, 3209, 1080, false, 13, 33, true),
  ('ojo-waterside', 'ojo', 'Creekside Settlement', 'Waterside community', '24 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Mama Efe', 'nurse', 'girls', 24, 'These will last them a whole year!', 'girls|pink|24 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 5374, 3161, 780, false, 13, 34, true),
  ('ojo-community', 'ojo', 'Family Help Centre', 'Community centre', 'Women and girls meet here weekly. Many share one tap and buy pads only when they can.', 'If a pack could last, it would change things for our families.', 'Mrs Adebayo', 'woman', 'women and girls', 22, 'This will last our families a long time.', 'girls|pink|22 women and girls
cake|peach|Ages 12 to 45
noDrop|green|No running water
coin|pink|Money is tight', 5405, 3124, 930, false, 13, 35, true),
  ('ojo-home', 'ojo', 'Haven Home', 'Motherless home', 'A home for 16 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Asabe', 'mama', 'girls', 16, 'Every one of my girls has her own kit now!', 'girls|pink|16 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 5452, 3116, 1020, false, 13, 36, true),
  ('ojo-training', 'ojo', 'Bright Skills Centre', 'Training school', '10 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 10, 'My students can work in comfort now.', 'girls|pink|10 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 5494, 3140, 930, false, 13, 37, true),
  ('okokomaiko-school', 'okokomaiko', 'Sunrise Grammar School', 'School', '31 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Adeyemi', 'teacher', 'girls', 31, 'Now my girls can stay in class all month!', 'girls|pink|31 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 5785, 2898, 780, false, 13, 38, true),
  ('okokomaiko-market', 'okokomaiko', 'Fresh Foods Market', 'Market', '17 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Nkechi', 'market', 'girl traders', 17, 'My girls can work without worry now!', 'girls|pink|17 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 5769, 2943, 1020, false, 13, 39, true),
  ('okokomaiko-busstop', 'okokomaiko', 'Under-Bridge Outreach', 'Roadside outreach', '9 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Sister Amaka', 'outreach', 'young women', 9, 'They felt seen today. Thank you.', 'girls|pink|9 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 5727, 2967, 600, false, 13, 40, true),
  ('okokomaiko-clinic', 'okokomaiko', 'Girls’ Health Point', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Grace', 'nurse', 'girls', 25, 'We can help every girl who walks in today.', 'girls|pink|About 25 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 5680, 2959, 750, false, 13, 41, true),
  ('okokomaiko-youth', 'okokomaiko', 'After-School Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 20, 'Now our girls are ready, any day.', 'girls|pink|20 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 5649, 2922, 1050, false, 13, 42, true),
  ('okokomaiko-hostel', 'okokomaiko', 'Palm House Hostel', 'Girls’ hostel', '24 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 24, 'The girls feel at home now. Thank you!', 'girls|pink|24 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 5649, 2874, 1020, false, 13, 43, true),
  ('okokomaiko-home', 'okokomaiko', 'Bright Path Home', 'Motherless home', 'A home for 10 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Asabe', 'mama', 'girls', 10, 'Every one of my girls has her own kit now!', 'girls|pink|10 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 5680, 2837, 990, false, 13, 44, true),
  ('okokomaiko-football', 'okokomaiko', 'Queens Football Club', 'Football club', '21 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 21, 'Nobody sits out on Saturday now!', 'girls|pink|21 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 5727, 2829, 570, false, 13, 45, true),
  ('okokomaiko-training', 'okokomaiko', 'Bright Skills Centre', 'Training school', '10 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Bukky', 'teacher', 'apprentices', 10, 'My students can work in comfort now.', 'girls|pink|10 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 5769, 2853, 930, false, 13, 46, true),
  ('iba-school', 'iba', 'Golden Gate School', 'School', '26 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Eze', 'teacher', 'girls', 26, 'Now my girls can stay in class all month!', 'girls|pink|26 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 5950, 2876, 840, false, 13, 47, true),
  ('iba-market', 'iba', 'Sunrise Market', 'Market', '18 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Bose', 'market', 'girl traders', 18, 'My girls can work without worry now!', 'girls|pink|18 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 5937, 2917, 1050, false, 13, 48, true),
  ('iba-busstop', 'iba', 'Junction Outreach', 'Roadside outreach', '14 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Joy', 'outreach', 'young women', 14, 'They felt seen today. Thank you.', 'girls|pink|14 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 5902, 2943, 720, false, 13, 49, true),
  ('iba-clinic', 'iba', 'Girls’ Health Point', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Grace', 'nurse', 'girls', 20, 'We can help every girl who walks in today.', 'girls|pink|About 20 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 5858, 2943, 780, false, 13, 50, true),
  ('iba-youth', 'iba', 'Youth Hub', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 15, 'Now our girls are ready, any day.', 'girls|pink|15 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 5823, 2917, 1080, false, 13, 51, true),
  ('iba-hostel', 'iba', 'Bluebell Hostel', 'Girls’ hostel', '29 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Funmi', 'mamaRonke', 'girls', 29, 'The girls feel at home now. Thank you!', 'girls|pink|29 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 5810, 2876, 1050, false, 13, 52, true),
  ('iba-home', 'iba', 'Hope House', 'Motherless home', 'A home for 20 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ronke', 'mama', 'girls', 20, 'Every one of my girls has her own kit now!', 'girls|pink|20 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 5823, 2835, 990, false, 13, 53, true),
  ('iba-football', 'iba', 'Girls FC', 'Football club', '17 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ify', 'coach', 'players', 17, 'Nobody sits out on Saturday now!', 'girls|pink|17 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 5858, 2809, 660, false, 13, 54, true),
  ('iba-training', 'iba', 'Bright Skills Centre', 'Training school', '18 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 18, 'My students can work in comfort now.', 'girls|pink|18 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 5902, 2809, 930, false, 13, 55, true),
  ('iba-shelter', 'iba', 'Safe Stay Shelter', 'Temporary shelter', 'Families moved here after floods. 23 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Bello', 'woman', 'women and girls', 23, 'You have given us dignity today.', 'girls|pink|23 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 5937, 2835, 1080, false, 13, 56, true),
  ('igando-school', 'igando', 'Morning Star Academy', 'School', '26 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Miss Ada', 'teacher', 'girls', 26, 'Now my girls can stay in class all month!', 'girls|pink|26 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 6279, 2190, 870, false, 13, 57, true),
  ('igando-market', 'igando', 'Textile Lane', 'Market', '24 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Nkechi', 'market', 'girl traders', 24, 'My girls can work without worry now!', 'girls|pink|24 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 6263, 2235, 1050, false, 13, 58, true),
  ('igando-busstop', 'igando', 'Bus Stop Outreach', 'Roadside outreach', '7 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Joy', 'outreach', 'young women', 7, 'They felt seen today. Thank you.', 'girls|pink|7 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 6221, 2259, 660, false, 13, 59, true),
  ('igando-clinic', 'igando', 'Teen Care Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Chika', 'nurse', 'girls', 14, 'We can help every girl who walks in today.', 'girls|pink|About 14 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 6174, 2251, 690, false, 13, 60, true),
  ('igando-youth', 'igando', 'Youth Hub', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Ronke', 'outreach', 'girls', 18, 'Now our girls are ready, any day.', 'girls|pink|18 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 6143, 2214, 1020, false, 13, 61, true),
  ('igando-hostel', 'igando', 'Bluebell Hostel', 'Girls’ hostel', '29 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Yemi', 'mamaRonke', 'girls', 29, 'The girls feel at home now. Thank you!', 'girls|pink|29 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 6143, 2166, 1050, false, 13, 62, true),
  ('igando-home', 'igando', 'Little Hearts Home', 'Motherless home', 'A home for 16 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ifeoma', 'mama', 'girls', 16, 'Every one of my girls has her own kit now!', 'girls|pink|16 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 6174, 2129, 960, false, 13, 63, true),
  ('igando-football', 'igando', 'Stars United Girls', 'Football club', '19 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 19, 'Nobody sits out on Saturday now!', 'girls|pink|19 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 6221, 2121, 660, false, 13, 64, true),
  ('igando-training', 'igando', 'Crafts Workshop', 'Training school', '10 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 10, 'My students can work in comfort now.', 'girls|pink|10 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 6263, 2145, 900, false, 13, 65, true),
  ('ikotun-school', 'ikotun', 'Unity Girls’ College', 'School', '45 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Adeyemi', 'teacher', 'girls', 45, 'Now my girls can stay in class all month!', 'girls|pink|45 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 6521, 2223, 840, false, 13, 66, true),
  ('ikotun-market', 'ikotun', 'Fruit Line Market', 'Market', '11 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Nkechi', 'market', 'girl traders', 11, 'My girls can work without worry now!', 'girls|pink|11 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 6508, 2264, 1050, false, 13, 67, true),
  ('ikotun-busstop', 'ikotun', 'Junction Outreach', 'Roadside outreach', '10 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Kemi', 'outreach', 'young women', 10, 'They felt seen today. Thank you.', 'girls|pink|10 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 6473, 2290, 720, false, 13, 68, true),
  ('ikotun-clinic', 'ikotun', 'Youth Health Corner', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Amina', 'nurse', 'girls', 19, 'We can help every girl who walks in today.', 'girls|pink|About 19 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 6429, 2290, 660, false, 13, 69, true),
  ('ikotun-youth', 'ikotun', 'Study Corner', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Bimpe', 'outreach', 'girls', 20, 'Now our girls are ready, any day.', 'girls|pink|20 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 6394, 2264, 1080, false, 13, 70, true),
  ('ikotun-hostel', 'ikotun', 'Kingsway Girls’ Hostel', 'Girls’ hostel', '19 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 19, 'The girls feel at home now. Thank you!', 'girls|pink|19 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 6381, 2223, 1020, false, 13, 71, true),
  ('ikotun-home', 'ikotun', 'Open Arms Home', 'Motherless home', 'A home for 14 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ronke', 'mama', 'girls', 14, 'Every one of my girls has her own kit now!', 'girls|pink|14 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 6394, 2182, 1020, false, 13, 72, true),
  ('ikotun-football', 'ikotun', 'Stars United Girls', 'Football club', '22 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 22, 'Nobody sits out on Saturday now!', 'girls|pink|22 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 6429, 2156, 570, false, 13, 73, true),
  ('ikotun-training', 'ikotun', 'Style Hair Academy', 'Training school', '18 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 18, 'My students can work in comfort now.', 'girls|pink|18 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 6473, 2156, 900, false, 13, 74, true),
  ('ikotun-shelter', 'ikotun', 'Hope Camp', 'Temporary shelter', 'Families moved here after floods. 20 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Aunty Grace', 'woman', 'women and girls', 20, 'You have given us dignity today.', 'girls|pink|20 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 6508, 2182, 1050, false, 13, 75, true),
  ('idimu-school', 'idimu', 'Sunrise Grammar School', 'School', '41 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Miss Ada', 'teacher', 'girls', 41, 'Now my girls can stay in class all month!', 'girls|pink|41 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 6422, 1847, 840, false, 13, 76, true),
  ('idimu-market', 'idimu', 'Textile Lane', 'Market', '11 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Sade', 'market', 'girl traders', 11, 'My girls can work without worry now!', 'girls|pink|11 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 6409, 1888, 1080, false, 13, 77, true),
  ('idimu-busstop', 'idimu', 'Under-Bridge Outreach', 'Roadside outreach', '9 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Bisi', 'outreach', 'young women', 9, 'They felt seen today. Thank you.', 'girls|pink|9 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 6374, 1914, 690, false, 13, 78, true),
  ('idimu-clinic', 'idimu', 'Family Health Centre', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Chika', 'nurse', 'girls', 22, 'We can help every girl who walks in today.', 'girls|pink|About 22 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 6330, 1914, 780, false, 13, 79, true),
  ('idimu-youth', 'idimu', 'Study Corner', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Ronke', 'outreach', 'girls', 24, 'Now our girls are ready, any day.', 'girls|pink|24 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 6295, 1888, 1080, false, 13, 80, true),
  ('idimu-hostel', 'idimu', 'Sunflower Hostel', 'Girls’ hostel', '29 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Rukky', 'mamaRonke', 'girls', 29, 'The girls feel at home now. Thank you!', 'girls|pink|29 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 6282, 1847, 1020, false, 13, 81, true),
  ('idimu-home', 'idimu', 'Hope House', 'Motherless home', 'A home for 12 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ifeoma', 'mama', 'girls', 12, 'Every one of my girls has her own kit now!', 'girls|pink|12 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 6295, 1806, 1050, false, 13, 82, true),
  ('idimu-football', 'idimu', 'Girls FC', 'Football club', '16 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 16, 'Nobody sits out on Saturday now!', 'girls|pink|16 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 6330, 1780, 630, false, 13, 83, true),
  ('idimu-training', 'idimu', 'Adeola Tailoring School', 'Training school', '19 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Folake', 'teacher', 'apprentices', 19, 'My students can work in comfort now.', 'girls|pink|19 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 6374, 1780, 930, false, 13, 84, true),
  ('idimu-shelter', 'idimu', 'Flood Relief Shelter', 'Temporary shelter', 'Families moved here after floods. 20 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Bello', 'woman', 'women and girls', 20, 'You have given us dignity today.', 'girls|pink|20 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 6409, 1806, 1050, false, 13, 85, true),
  ('ayobo-school', 'ayobo', 'Bright Stars School', 'School', '37 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Adeyemi', 'teacher', 'girls', 37, 'Now my girls can stay in class all month!', 'girls|pink|37 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 6246, 1571, 810, false, 13, 86, true),
  ('ayobo-market', 'ayobo', 'Evening Market', 'Market', '21 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Ngozi', 'market', 'girl traders', 21, 'My girls can work without worry now!', 'girls|pink|21 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 6230, 1616, 960, false, 13, 87, true),
  ('ayobo-busstop', 'ayobo', 'Motor Park Outreach', 'Roadside outreach', '13 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Kemi', 'outreach', 'young women', 13, 'They felt seen today. Thank you.', 'girls|pink|13 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 6188, 1640, 600, false, 13, 88, true),
  ('ayobo-clinic', 'ayobo', 'Youth Health Corner', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Grace', 'nurse', 'girls', 13, 'We can help every girl who walks in today.', 'girls|pink|About 13 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 6141, 1632, 750, false, 13, 89, true),
  ('ayobo-youth', 'ayobo', 'Study Corner', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Fatima', 'outreach', 'girls', 16, 'Now our girls are ready, any day.', 'girls|pink|16 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 6110, 1595, 1080, false, 13, 90, true),
  ('ayobo-hostel', 'ayobo', 'Bluebell Hostel', 'Girls’ hostel', '21 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 21, 'The girls feel at home now. Thank you!', 'girls|pink|21 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 6110, 1547, 1080, false, 13, 91, true),
  ('ayobo-home', 'ayobo', 'Little Hearts Home', 'Motherless home', 'A home for 19 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Asabe', 'mama', 'girls', 19, 'Every one of my girls has her own kit now!', 'girls|pink|19 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 6141, 1510, 990, false, 13, 92, true),
  ('ayobo-football', 'ayobo', 'Girls FC', 'Football club', '18 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 18, 'Nobody sits out on Saturday now!', 'girls|pink|18 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 6188, 1502, 600, false, 13, 93, true),
  ('ayobo-training', 'ayobo', 'Adeola Tailoring School', 'Training school', '18 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Adeola', 'teacher', 'apprentices', 18, 'My students can work in comfort now.', 'girls|pink|18 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 6230, 1526, 900, false, 13, 94, true),
  ('egbeda-school', 'egbeda', 'Rainbow College', 'School', '31 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Eze', 'teacher', 'girls', 31, 'Now my girls can stay in class all month!', 'girls|pink|31 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 6774, 1747, 780, false, 13, 95, true),
  ('egbeda-market', 'egbeda', 'Evening Market', 'Market', '10 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'market', 'girl traders', 10, 'My girls can work without worry now!', 'girls|pink|10 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 6761, 1788, 1050, false, 13, 96, true),
  ('egbeda-busstop', 'egbeda', 'Under-Bridge Outreach', 'Roadside outreach', '7 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Sister Amaka', 'outreach', 'young women', 7, 'They felt seen today. Thank you.', 'girls|pink|7 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 6726, 1814, 600, false, 13, 97, true),
  ('egbeda-clinic', 'egbeda', 'Teen Care Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Grace', 'nurse', 'girls', 25, 'We can help every girl who walks in today.', 'girls|pink|About 25 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 6682, 1814, 690, false, 13, 98, true),
  ('egbeda-youth', 'egbeda', 'After-School Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 29, 'Now our girls are ready, any day.', 'girls|pink|29 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 6647, 1788, 1080, false, 13, 99, true),
  ('egbeda-hostel', 'egbeda', 'Bluebell Hostel', 'Girls’ hostel', '22 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 22, 'The girls feel at home now. Thank you!', 'girls|pink|22 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 6634, 1747, 1020, false, 13, 100, true),
  ('egbeda-home', 'egbeda', 'Little Hearts Home', 'Motherless home', 'A home for 19 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ifeoma', 'mama', 'girls', 19, 'Every one of my girls has her own kit now!', 'girls|pink|19 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 6647, 1706, 1020, false, 13, 101, true),
  ('egbeda-football', 'egbeda', 'Lagoon Strikers', 'Football club', '16 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ify', 'coach', 'players', 16, 'Nobody sits out on Saturday now!', 'girls|pink|16 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 6682, 1680, 660, false, 13, 102, true),
  ('egbeda-training', 'egbeda', 'Bright Skills Centre', 'Training school', '14 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 14, 'My students can work in comfort now.', 'girls|pink|14 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 6726, 1680, 960, false, 13, 103, true),
  ('egbeda-shelter', 'egbeda', 'Flood Relief Shelter', 'Temporary shelter', 'Families moved here after floods. 29 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Aunty Grace', 'woman', 'women and girls', 29, 'You have given us dignity today.', 'girls|pink|29 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 6761, 1706, 1050, false, 13, 104, true),
  ('iyana-ipaja-school', 'iyana-ipaja', 'Rainbow College', 'School', '35 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Miss Ada', 'teacher', 'girls', 35, 'Now my girls can stay in class all month!', 'girls|pink|35 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 6807, 1526, 870, false, 13, 105, true),
  ('iyana-ipaja-market', 'iyana-ipaja', 'Corner Market', 'Market', '18 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'market', 'girl traders', 18, 'My girls can work without worry now!', 'girls|pink|18 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 6791, 1571, 1080, false, 13, 106, true),
  ('iyana-ipaja-busstop', 'iyana-ipaja', 'Roadside Help Point', 'Roadside outreach', '7 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Joy', 'outreach', 'young women', 7, 'They felt seen today. Thank you.', 'girls|pink|7 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 6749, 1595, 720, false, 13, 107, true),
  ('iyana-ipaja-clinic', 'iyana-ipaja', 'Youth Health Corner', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Amina', 'nurse', 'girls', 25, 'We can help every girl who walks in today.', 'girls|pink|About 25 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 6702, 1587, 690, false, 13, 108, true),
  ('iyana-ipaja-youth', 'iyana-ipaja', 'After-School Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Ronke', 'outreach', 'girls', 30, 'Now our girls are ready, any day.', 'girls|pink|30 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 6671, 1550, 1020, false, 13, 109, true),
  ('iyana-ipaja-hostel', 'iyana-ipaja', 'Sunflower Hostel', 'Girls’ hostel', '25 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Rukky', 'mamaRonke', 'girls', 25, 'The girls feel at home now. Thank you!', 'girls|pink|25 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 6671, 1502, 1080, false, 13, 110, true),
  ('iyana-ipaja-home', 'iyana-ipaja', 'Little Hearts Home', 'Motherless home', 'A home for 12 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Asabe', 'mama', 'girls', 12, 'Every one of my girls has her own kit now!', 'girls|pink|12 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 6702, 1465, 1020, false, 13, 111, true),
  ('iyana-ipaja-football', 'iyana-ipaja', 'Girls FC', 'Football club', '15 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Halima', 'coach', 'players', 15, 'Nobody sits out on Saturday now!', 'girls|pink|15 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 6749, 1457, 630, false, 13, 112, true),
  ('iyana-ipaja-training', 'iyana-ipaja', 'Adeola Tailoring School', 'Training school', '16 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 16, 'My students can work in comfort now.', 'girls|pink|16 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 6791, 1481, 960, false, 13, 113, true),
  ('satellite-town-school', 'satellite-town', 'Unity Girls’ College', 'School', '32 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Adeyemi', 'teacher', 'girls', 32, 'Now my girls can stay in class all month!', 'girls|pink|32 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 6334, 3340, 870, false, 13, 114, true),
  ('satellite-town-market', 'satellite-town', 'Sunrise Market', 'Market', '18 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'market', 'girl traders', 18, 'My girls can work without worry now!', 'girls|pink|18 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 6321, 3381, 1020, false, 13, 115, true),
  ('satellite-town-busstop', 'satellite-town', 'Under-Bridge Outreach', 'Roadside outreach', '8 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Kemi', 'outreach', 'young women', 8, 'They felt seen today. Thank you.', 'girls|pink|8 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 6286, 3407, 660, false, 13, 116, true),
  ('satellite-town-clinic', 'satellite-town', 'Girls’ Health Point', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Kemi', 'nurse', 'girls', 15, 'We can help every girl who walks in today.', 'girls|pink|About 15 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 6242, 3407, 750, false, 13, 117, true),
  ('satellite-town-youth', 'satellite-town', 'Teen Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 21, 'Now our girls are ready, any day.', 'girls|pink|21 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 6207, 3381, 1080, false, 13, 118, true),
  ('satellite-town-waterside', 'satellite-town', 'Lagoonside Community', 'Waterside community', '19 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Mama Efe', 'nurse', 'girls', 19, 'These will last them a whole year!', 'girls|pink|19 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 6194, 3340, 750, false, 13, 119, true),
  ('satellite-town-community', 'satellite-town', 'Neighbourhood Hall', 'Community centre', 'Women and girls meet here weekly. Many share one tap and buy pads only when they can.', 'If a pack could last, it would change things for our families.', 'Mrs Obi', 'woman', 'women and girls', 30, 'This will last our families a long time.', 'girls|pink|30 women and girls
cake|peach|Ages 12 to 45
noDrop|green|No running water
coin|pink|Money is tight', 6207, 3299, 960, false, 13, 120, true),
  ('satellite-town-home', 'satellite-town', 'Open Arms Home', 'Motherless home', 'A home for 20 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Titi', 'mama', 'girls', 20, 'Every one of my girls has her own kit now!', 'girls|pink|20 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 6242, 3273, 1020, false, 13, 121, true),
  ('satellite-town-training', 'satellite-town', 'Style Hair Academy', 'Training school', '17 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Bukky', 'teacher', 'apprentices', 17, 'My students can work in comfort now.', 'girls|pink|17 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 6286, 3273, 900, false, 13, 122, true),
  ('satellite-town-shelter', 'satellite-town', 'Rest Point Shelter', 'Temporary shelter', 'Families moved here after floods. 21 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Bello', 'woman', 'women and girls', 21, 'You have given us dignity today.', 'girls|pink|21 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 6321, 3299, 1050, false, 13, 123, true),
  ('festac-school', 'festac', 'Unity Girls’ College', 'School', '37 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Okafor', 'teacher', 'girls', 37, 'Now my girls can stay in class all month!', 'girls|pink|37 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 6697, 3130, 840, false, 13, 124, true),
  ('festac-market', 'festac', 'Evening Market', 'Market', '12 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Bose', 'market', 'girl traders', 12, 'My girls can work without worry now!', 'girls|pink|12 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 6684, 3171, 1080, false, 13, 125, true),
  ('festac-busstop', 'festac', 'Under-Bridge Outreach', 'Roadside outreach', '9 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Rita', 'outreach', 'young women', 9, 'They felt seen today. Thank you.', 'girls|pink|9 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 6649, 3197, 720, false, 13, 126, true),
  ('festac-clinic', 'festac', 'Community Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Amina', 'nurse', 'girls', 15, 'We can help every girl who walks in today.', 'girls|pink|About 15 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 6605, 3197, 660, false, 13, 127, true),
  ('festac-youth', 'festac', 'Youth Hub', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Ronke', 'outreach', 'girls', 30, 'Now our girls are ready, any day.', 'girls|pink|30 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 6570, 3171, 1020, false, 13, 128, true),
  ('festac-hostel', 'festac', 'Sunflower Hostel', 'Girls’ hostel', '22 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Yemi', 'mamaRonke', 'girls', 22, 'The girls feel at home now. Thank you!', 'girls|pink|22 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 6557, 3130, 1080, false, 13, 129, true),
  ('festac-home', 'festac', 'Little Hearts Home', 'Motherless home', 'A home for 17 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ronke', 'mama', 'girls', 17, 'Every one of my girls has her own kit now!', 'girls|pink|17 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 6570, 3089, 990, false, 13, 130, true),
  ('festac-football', 'festac', 'Lagoon Strikers', 'Football club', '14 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 14, 'Nobody sits out on Saturday now!', 'girls|pink|14 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 6605, 3063, 570, false, 13, 131, true),
  ('festac-training', 'festac', 'Bright Skills Centre', 'Training school', '11 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Bukky', 'teacher', 'apprentices', 11, 'My students can work in comfort now.', 'girls|pink|11 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 6649, 3063, 900, false, 13, 132, true),
  ('festac-shelter', 'festac', 'Flood Relief Shelter', 'Temporary shelter', 'Families moved here after floods. 22 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Nwosu', 'woman', 'women and girls', 22, 'You have given us dignity today.', 'girls|pink|22 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 6684, 3089, 1020, false, 13, 133, true),
  ('amuwo-school', 'amuwo', 'Golden Gate School', 'School', '21 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Eze', 'teacher', 'girls', 21, 'Now my girls can stay in class all month!', 'girls|pink|21 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 6906, 3241, 780, false, 13, 134, true),
  ('amuwo-market', 'amuwo', 'Waterside Market', 'Market', '19 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Ngozi', 'market', 'girl traders', 19, 'My girls can work without worry now!', 'girls|pink|19 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 6890, 3286, 960, false, 13, 135, true),
  ('amuwo-busstop', 'amuwo', 'Junction Outreach', 'Roadside outreach', '7 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Kemi', 'outreach', 'young women', 7, 'They felt seen today. Thank you.', 'girls|pink|7 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 6848, 3310, 630, false, 13, 136, true),
  ('amuwo-clinic', 'amuwo', 'Teen Care Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 18, 'We can help every girl who walks in today.', 'girls|pink|About 18 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 6801, 3302, 660, false, 13, 137, true),
  ('amuwo-youth', 'amuwo', 'Study Corner', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Fatima', 'outreach', 'girls', 15, 'Now our girls are ready, any day.', 'girls|pink|15 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 6770, 3265, 1080, false, 13, 138, true),
  ('amuwo-hostel', 'amuwo', 'Bluebell Hostel', 'Girls’ hostel', '21 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 21, 'The girls feel at home now. Thank you!', 'girls|pink|21 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 6770, 3217, 1080, false, 13, 139, true),
  ('amuwo-home', 'amuwo', 'Open Arms Home', 'Motherless home', 'A home for 13 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ifeoma', 'mama', 'girls', 13, 'Every one of my girls has her own kit now!', 'girls|pink|13 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 6801, 3180, 960, false, 13, 140, true),
  ('amuwo-football', 'amuwo', 'Lagoon Strikers', 'Football club', '14 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 14, 'Nobody sits out on Saturday now!', 'girls|pink|14 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 6848, 3172, 570, false, 13, 141, true),
  ('amuwo-training', 'amuwo', 'Needle and Thread Studio', 'Training school', '14 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Bukky', 'teacher', 'apprentices', 14, 'My students can work in comfort now.', 'girls|pink|14 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 6890, 3196, 990, false, 13, 142, true),
  ('mile-2-school', 'mile-2', 'Morning Star Academy', 'School', '30 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Okafor', 'teacher', 'girls', 30, 'Now my girls can stay in class all month!', 'girls|pink|30 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 7082, 3185, 780, false, 13, 143, true),
  ('mile-2-market', 'mile-2', 'Fresh Foods Market', 'Market', '19 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Nkechi', 'market', 'girl traders', 19, 'My girls can work without worry now!', 'girls|pink|19 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7069, 3226, 1020, false, 13, 144, true),
  ('mile-2-busstop', 'mile-2', 'Motor Park Outreach', 'Roadside outreach', '9 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Sister Amaka', 'outreach', 'young women', 9, 'They felt seen today. Thank you.', 'girls|pink|9 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7034, 3252, 660, false, 13, 145, true),
  ('mile-2-clinic', 'mile-2', 'Family Health Centre', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Grace', 'nurse', 'girls', 13, 'We can help every girl who walks in today.', 'girls|pink|About 13 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 6990, 3252, 690, false, 13, 146, true),
  ('mile-2-youth', 'mile-2', 'Library Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Ronke', 'outreach', 'girls', 30, 'Now our girls are ready, any day.', 'girls|pink|30 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 6955, 3226, 1050, false, 13, 147, true),
  ('mile-2-hostel', 'mile-2', 'Kingsway Girls’ Hostel', 'Girls’ hostel', '18 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 18, 'The girls feel at home now. Thank you!', 'girls|pink|18 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 6942, 3185, 1050, false, 13, 148, true),
  ('mile-2-home', 'mile-2', 'Haven Home', 'Motherless home', 'A home for 10 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Asabe', 'mama', 'girls', 10, 'Every one of my girls has her own kit now!', 'girls|pink|10 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 6955, 3144, 1050, false, 13, 149, true),
  ('mile-2-football', 'mile-2', 'Girls FC', 'Football club', '19 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 19, 'Nobody sits out on Saturday now!', 'girls|pink|19 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 6990, 3118, 630, false, 13, 150, true),
  ('mile-2-training', 'mile-2', 'Adeola Tailoring School', 'Training school', '13 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 13, 'My students can work in comfort now.', 'girls|pink|13 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7034, 3118, 990, false, 13, 151, true),
  ('mile-2-shelter', 'mile-2', 'Safe Stay Shelter', 'Temporary shelter', 'Families moved here after floods. 26 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Hauwa', 'woman', 'women and girls', 26, 'You have given us dignity today.', 'girls|pink|26 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 7069, 3144, 1020, false, 13, 152, true),
  ('ajegunle-school', 'ajegunle', 'Unity Girls’ College', 'School', '22 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Eze', 'teacher', 'girls', 22, 'Now my girls can stay in class all month!', 'girls|pink|22 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 7301, 3263, 810, false, 13, 153, true),
  ('ajegunle-market', 'ajegunle', 'Fruit Line Market', 'Market', '22 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Ngozi', 'market', 'girl traders', 22, 'My girls can work without worry now!', 'girls|pink|22 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7288, 3304, 960, false, 13, 154, true),
  ('ajegunle-busstop', 'ajegunle', 'Roadside Help Point', 'Roadside outreach', '6 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Kemi', 'outreach', 'young women', 6, 'They felt seen today. Thank you.', 'girls|pink|6 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7253, 3330, 690, false, 13, 155, true),
  ('ajegunle-clinic', 'ajegunle', 'Community Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Chika', 'nurse', 'girls', 15, 'We can help every girl who walks in today.', 'girls|pink|About 15 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7209, 3330, 660, false, 13, 156, true),
  ('ajegunle-youth', 'ajegunle', 'After-School Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 28, 'Now our girls are ready, any day.', 'girls|pink|28 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7174, 3304, 1020, false, 13, 157, true),
  ('ajegunle-waterside', 'ajegunle', 'Creekside Settlement', 'Waterside community', '29 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Mama Efe', 'nurse', 'girls', 29, 'These will last them a whole year!', 'girls|pink|29 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 7161, 3263, 870, false, 13, 158, true),
  ('ajegunle-hostel', 'ajegunle', 'Harmony Hostel', 'Girls’ hostel', '19 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Rukky', 'mamaRonke', 'girls', 19, 'The girls feel at home now. Thank you!', 'girls|pink|19 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7174, 3222, 1050, false, 13, 159, true),
  ('ajegunle-football', 'ajegunle', 'Stars United Girls', 'Football club', '14 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Halima', 'coach', 'players', 14, 'Nobody sits out on Saturday now!', 'girls|pink|14 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7209, 3196, 630, false, 13, 160, true),
  ('ajegunle-training', 'ajegunle', 'Style Hair Academy', 'Training school', '10 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Adeola', 'teacher', 'apprentices', 10, 'My students can work in comfort now.', 'girls|pink|10 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7253, 3196, 990, false, 13, 161, true),
  ('ajegunle-shelter', 'ajegunle', 'Rest Point Shelter', 'Temporary shelter', 'Families moved here after floods. 29 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Nwosu', 'woman', 'women and girls', 29, 'You have given us dignity today.', 'girls|pink|29 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 7288, 3222, 1020, false, 13, 162, true),
  ('apapa-school', 'apapa', 'Royal Pearl College', 'School', '25 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Miss Tolu', 'teacher', 'girls', 25, 'Now my girls can stay in class all month!', 'girls|pink|25 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7565, 3329, 810, false, 13, 163, true),
  ('apapa-market', 'apapa', 'Fresh Foods Market', 'Market', '17 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Bose', 'market', 'girl traders', 17, 'My girls can work without worry now!', 'girls|pink|17 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7549, 3374, 1080, false, 13, 164, true),
  ('apapa-busstop', 'apapa', 'Motor Park Outreach', 'Roadside outreach', '7 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Joy', 'outreach', 'young women', 7, 'They felt seen today. Thank you.', 'girls|pink|7 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7507, 3398, 690, false, 13, 165, true),
  ('apapa-clinic', 'apapa', 'Girls’ Health Point', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 13, 'We can help every girl who walks in today.', 'girls|pink|About 13 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7460, 3390, 780, false, 13, 166, true),
  ('apapa-youth', 'apapa', 'After-School Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Bimpe', 'outreach', 'girls', 25, 'Now our girls are ready, any day.', 'girls|pink|25 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7429, 3353, 1050, false, 13, 167, true),
  ('apapa-waterside', 'apapa', 'Canoe Landing Community', 'Waterside community', '17 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Mrs Ajayi', 'nurse', 'girls', 17, 'These will last them a whole year!', 'girls|pink|17 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 7429, 3305, 870, false, 13, 168, true),
  ('apapa-hostel', 'apapa', 'Palm House Hostel', 'Girls’ hostel', '25 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 25, 'The girls feel at home now. Thank you!', 'girls|pink|25 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7460, 3268, 1050, false, 13, 169, true),
  ('apapa-football', 'apapa', 'Young Lionesses FC', 'Football club', '21 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ify', 'coach', 'players', 21, 'Nobody sits out on Saturday now!', 'girls|pink|21 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7507, 3260, 630, false, 13, 170, true),
  ('apapa-training', 'apapa', 'Needle and Thread Studio', 'Training school', '14 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 14, 'My students can work in comfort now.', 'girls|pink|14 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7549, 3284, 960, false, 13, 171, true),
  ('orile-school', 'orile', 'Golden Gate School', 'School', '27 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Okafor', 'teacher', 'girls', 27, 'Now my girls can stay in class all month!', 'girls|pink|27 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7411, 2986, 840, false, 13, 172, true),
  ('orile-market', 'orile', 'Fresh Foods Market', 'Market', '16 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Bose', 'market', 'girl traders', 16, 'My girls can work without worry now!', 'girls|pink|16 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7398, 3027, 990, false, 13, 173, true),
  ('orile-busstop', 'orile', 'Under-Bridge Outreach', 'Roadside outreach', '14 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Bisi', 'outreach', 'young women', 14, 'They felt seen today. Thank you.', 'girls|pink|14 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7363, 3053, 600, false, 13, 174, true),
  ('orile-clinic', 'orile', 'Teen Care Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Chika', 'nurse', 'girls', 12, 'We can help every girl who walks in today.', 'girls|pink|About 12 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7319, 3053, 720, false, 13, 175, true),
  ('orile-youth', 'orile', 'After-School Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Bimpe', 'outreach', 'girls', 16, 'Now our girls are ready, any day.', 'girls|pink|16 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7284, 3027, 1020, false, 13, 176, true),
  ('orile-hostel', 'orile', 'Kingsway Girls’ Hostel', 'Girls’ hostel', '27 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Rukky', 'mamaRonke', 'girls', 27, 'The girls feel at home now. Thank you!', 'girls|pink|27 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7271, 2986, 1080, false, 13, 177, true),
  ('orile-home', 'orile', 'Bright Path Home', 'Motherless home', 'A home for 12 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ifeoma', 'mama', 'girls', 12, 'Every one of my girls has her own kit now!', 'girls|pink|12 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7284, 2945, 960, false, 13, 178, true),
  ('orile-football', 'orile', 'Queens Football Club', 'Football club', '15 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ify', 'coach', 'players', 15, 'Nobody sits out on Saturday now!', 'girls|pink|15 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7319, 2919, 630, false, 13, 179, true),
  ('orile-training', 'orile', 'Style Hair Academy', 'Training school', '15 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 15, 'My students can work in comfort now.', 'girls|pink|15 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7363, 2919, 900, false, 13, 180, true),
  ('orile-shelter', 'orile', 'Hope Camp', 'Temporary shelter', 'Families moved here after floods. 34 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Nwosu', 'woman', 'women and girls', 34, 'You have given us dignity today.', 'girls|pink|34 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 7398, 2945, 1080, false, 13, 181, true),
  ('ijora-school', 'ijora', 'Golden Gate School', 'School', '39 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Miss Ada', 'teacher', 'girls', 39, 'Now my girls can stay in class all month!', 'girls|pink|39 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 7653, 3097, 870, false, 13, 182, true),
  ('ijora-market', 'ijora', 'Sunrise Market', 'Market', '25 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Ngozi', 'market', 'girl traders', 25, 'My girls can work without worry now!', 'girls|pink|25 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7640, 3138, 1080, false, 13, 183, true),
  ('ijora-busstop', 'ijora', 'Under-Bridge Outreach', 'Roadside outreach', '8 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Sister Amaka', 'outreach', 'young women', 8, 'They felt seen today. Thank you.', 'girls|pink|8 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7605, 3164, 690, false, 13, 184, true),
  ('ijora-clinic', 'ijora', 'Girls’ Health Point', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Kemi', 'nurse', 'girls', 16, 'We can help every girl who walks in today.', 'girls|pink|About 16 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7561, 3164, 720, false, 13, 185, true),
  ('ijora-youth', 'ijora', 'After-School Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Fatima', 'outreach', 'girls', 26, 'Now our girls are ready, any day.', 'girls|pink|26 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7526, 3138, 1080, false, 13, 186, true),
  ('ijora-waterside', 'ijora', 'Fishing Village', 'Waterside community', '21 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Nurse Grace', 'nurse', 'girls', 21, 'These will last them a whole year!', 'girls|pink|21 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 7513, 3097, 810, false, 13, 187, true),
  ('ijora-hostel', 'ijora', 'Kingsway Girls’ Hostel', 'Girls’ hostel', '20 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Funmi', 'mamaRonke', 'girls', 20, 'The girls feel at home now. Thank you!', 'girls|pink|20 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7526, 3056, 1050, false, 13, 188, true),
  ('ijora-football', 'ijora', 'Queens Football Club', 'Football club', '19 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 19, 'Nobody sits out on Saturday now!', 'girls|pink|19 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7561, 3030, 570, false, 13, 189, true),
  ('ijora-training', 'ijora', 'Adeola Tailoring School', 'Training school', '10 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 10, 'My students can work in comfort now.', 'girls|pink|10 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7605, 3030, 930, false, 13, 190, true),
  ('ijora-shelter', 'ijora', 'Safe Stay Shelter', 'Temporary shelter', 'Families moved here after floods. 19 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Aunty Grace', 'woman', 'women and girls', 19, 'You have given us dignity today.', 'girls|pink|19 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 7640, 3056, 1020, false, 13, 191, true),
  ('surulere-school', 'surulere', 'New Dawn Academy', 'School', '35 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Miss Ada', 'teacher', 'girls', 35, 'Now my girls can stay in class all month!', 'girls|pink|35 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7488, 2765, 870, false, 13, 192, true),
  ('surulere-market', 'surulere', 'Sunrise Market', 'Market', '20 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Sade', 'market', 'girl traders', 20, 'My girls can work without worry now!', 'girls|pink|20 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7475, 2806, 990, false, 13, 193, true),
  ('surulere-busstop', 'surulere', 'Under-Bridge Outreach', 'Roadside outreach', '9 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Joy', 'outreach', 'young women', 9, 'They felt seen today. Thank you.', 'girls|pink|9 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7440, 2832, 720, false, 13, 194, true),
  ('surulere-clinic', 'surulere', 'Girls’ Health Point', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 15, 'We can help every girl who walks in today.', 'girls|pink|About 15 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7396, 2832, 690, false, 13, 195, true),
  ('surulere-youth', 'surulere', 'Youth Hub', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Ronke', 'outreach', 'girls', 27, 'Now our girls are ready, any day.', 'girls|pink|27 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7361, 2806, 1080, false, 13, 196, true),
  ('surulere-hostel', 'surulere', 'Bluebell Hostel', 'Girls’ hostel', '21 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Funmi', 'mamaRonke', 'girls', 21, 'The girls feel at home now. Thank you!', 'girls|pink|21 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7348, 2765, 1080, false, 13, 197, true),
  ('surulere-home', 'surulere', 'Open Arms Home', 'Motherless home', 'A home for 15 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Asabe', 'mama', 'girls', 15, 'Every one of my girls has her own kit now!', 'girls|pink|15 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7361, 2724, 1020, false, 13, 198, true),
  ('surulere-football', 'surulere', 'Young Lionesses FC', 'Football club', '20 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 20, 'Nobody sits out on Saturday now!', 'girls|pink|20 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7396, 2698, 660, false, 13, 199, true),
  ('surulere-training', 'surulere', 'Crafts Workshop', 'Training school', '16 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Folake', 'teacher', 'apprentices', 16, 'My students can work in comfort now.', 'girls|pink|16 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7440, 2698, 930, false, 13, 200, true),
  ('surulere-shelter', 'surulere', 'Rest Point Shelter', 'Temporary shelter', 'Families moved here after floods. 15 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Aunty Grace', 'woman', 'women and girls', 15, 'You have given us dignity today.', 'girls|pink|15 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 7475, 2724, 1050, false, 13, 201, true),
  ('ebute-metta-school', 'ebute-metta', 'Rainbow College', 'School', '44 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Miss Zainab', 'teacher', 'girls', 44, 'Now my girls can stay in class all month!', 'girls|pink|44 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 7785, 2931, 780, false, 13, 202, true),
  ('ebute-metta-market', 'ebute-metta', 'Fresh Foods Market', 'Market', '12 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Bose', 'market', 'girl traders', 12, 'My girls can work without worry now!', 'girls|pink|12 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7769, 2976, 960, false, 13, 203, true),
  ('ebute-metta-busstop', 'ebute-metta', 'Motor Park Outreach', 'Roadside outreach', '13 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Bisi', 'outreach', 'young women', 13, 'They felt seen today. Thank you.', 'girls|pink|13 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7727, 3000, 660, false, 13, 204, true),
  ('ebute-metta-clinic', 'ebute-metta', 'Youth Health Corner', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 24, 'We can help every girl who walks in today.', 'girls|pink|About 24 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7680, 2992, 750, false, 13, 205, true),
  ('ebute-metta-youth', 'ebute-metta', 'Library Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Bimpe', 'outreach', 'girls', 19, 'Now our girls are ready, any day.', 'girls|pink|19 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7649, 2955, 1020, false, 13, 206, true),
  ('ebute-metta-waterside', 'ebute-metta', 'Fishing Village', 'Waterside community', '19 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Mama Efe', 'nurse', 'girls', 19, 'These will last them a whole year!', 'girls|pink|19 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 7649, 2907, 720, false, 13, 207, true),
  ('ebute-metta-hostel', 'ebute-metta', 'Palm House Hostel', 'Girls’ hostel', '23 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Rukky', 'mamaRonke', 'girls', 23, 'The girls feel at home now. Thank you!', 'girls|pink|23 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7680, 2870, 1080, false, 13, 208, true),
  ('ebute-metta-football', 'ebute-metta', 'Queens Football Club', 'Football club', '18 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Halima', 'coach', 'players', 18, 'Nobody sits out on Saturday now!', 'girls|pink|18 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7727, 2862, 660, false, 13, 209, true),
  ('ebute-metta-training', 'ebute-metta', 'Needle and Thread Studio', 'Training school', '13 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 13, 'My students can work in comfort now.', 'girls|pink|13 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7769, 2886, 900, false, 13, 210, true),
  ('yaba-school', 'yaba', 'Royal Pearl College', 'School', '32 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Miss Tolu', 'teacher', 'girls', 32, 'Now my girls can stay in class all month!', 'girls|pink|32 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7741, 2599, 840, false, 13, 211, true),
  ('yaba-market', 'yaba', 'Sunrise Market', 'Market', '22 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Sade', 'market', 'girl traders', 22, 'My girls can work without worry now!', 'girls|pink|22 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7728, 2640, 960, false, 13, 212, true),
  ('yaba-busstop', 'yaba', 'Roadside Help Point', 'Roadside outreach', '11 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Kemi', 'outreach', 'young women', 11, 'They felt seen today. Thank you.', 'girls|pink|11 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7693, 2666, 690, false, 13, 213, true),
  ('yaba-clinic', 'yaba', 'Community Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Chika', 'nurse', 'girls', 23, 'We can help every girl who walks in today.', 'girls|pink|About 23 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7649, 2666, 720, false, 13, 214, true),
  ('yaba-youth', 'yaba', 'After-School Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 19, 'Now our girls are ready, any day.', 'girls|pink|19 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7614, 2640, 1020, false, 13, 215, true),
  ('yaba-hostel', 'yaba', 'Harmony Hostel', 'Girls’ hostel', '20 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Rukky', 'mamaRonke', 'girls', 20, 'The girls feel at home now. Thank you!', 'girls|pink|20 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7601, 2599, 1050, false, 13, 216, true),
  ('yaba-home', 'yaba', 'Hope House', 'Motherless home', 'A home for 20 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ronke', 'mama', 'girls', 20, 'Every one of my girls has her own kit now!', 'girls|pink|20 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7614, 2558, 990, false, 13, 217, true),
  ('yaba-football', 'yaba', 'Young Lionesses FC', 'Football club', '16 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 16, 'Nobody sits out on Saturday now!', 'girls|pink|16 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7649, 2532, 660, false, 13, 218, true),
  ('yaba-training', 'yaba', 'Style Hair Academy', 'Training school', '18 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Folake', 'teacher', 'apprentices', 18, 'My students can work in comfort now.', 'girls|pink|18 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7693, 2532, 930, false, 13, 219, true),
  ('yaba-shelter', 'yaba', 'Flood Relief Shelter', 'Temporary shelter', 'Families moved here after floods. 34 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Hauwa', 'woman', 'women and girls', 34, 'You have given us dignity today.', 'girls|pink|34 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 7728, 2558, 1080, false, 13, 220, true),
  ('akoka-school', 'akoka', 'Golden Gate School', 'School', '23 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Miss Zainab', 'teacher', 'girls', 23, 'Now my girls can stay in class all month!', 'girls|pink|23 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 7895, 2522, 840, false, 13, 221, true),
  ('akoka-market', 'akoka', 'Waterside Market', 'Market', '14 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Ngozi', 'market', 'girl traders', 14, 'My girls can work without worry now!', 'girls|pink|14 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7882, 2563, 990, false, 13, 222, true),
  ('akoka-busstop', 'akoka', 'Motor Park Outreach', 'Roadside outreach', '8 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Rita', 'outreach', 'young women', 8, 'They felt seen today. Thank you.', 'girls|pink|8 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7847, 2589, 600, false, 13, 223, true),
  ('akoka-clinic', 'akoka', 'Community Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Chika', 'nurse', 'girls', 13, 'We can help every girl who walks in today.', 'girls|pink|About 13 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7803, 2589, 720, false, 13, 224, true),
  ('akoka-youth', 'akoka', 'Library Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 15, 'Now our girls are ready, any day.', 'girls|pink|15 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7768, 2563, 1080, false, 13, 225, true),
  ('akoka-waterside', 'akoka', 'Lagoonside Community', 'Waterside community', '30 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Nurse Grace', 'nurse', 'girls', 30, 'These will last them a whole year!', 'girls|pink|30 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 7755, 2522, 720, false, 13, 226, true),
  ('akoka-hostel', 'akoka', 'Sunflower Hostel', 'Girls’ hostel', '26 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 26, 'The girls feel at home now. Thank you!', 'girls|pink|26 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7768, 2481, 1020, false, 13, 227, true),
  ('akoka-football', 'akoka', 'Girls FC', 'Football club', '16 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ify', 'coach', 'players', 16, 'Nobody sits out on Saturday now!', 'girls|pink|16 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7803, 2455, 600, false, 13, 228, true),
  ('akoka-training', 'akoka', 'Crafts Workshop', 'Training school', '12 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Folake', 'teacher', 'apprentices', 12, 'My students can work in comfort now.', 'girls|pink|12 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7847, 2455, 930, false, 13, 229, true),
  ('akoka-shelter', 'akoka', 'Hope Camp', 'Temporary shelter', 'Families moved here after floods. 31 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Hauwa', 'woman', 'women and girls', 31, 'You have given us dignity today.', 'girls|pink|31 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 7882, 2481, 1080, false, 13, 230, true),
  ('shomolu-school', 'shomolu', 'Morning Star Academy', 'School', '42 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Miss Tolu', 'teacher', 'girls', 42, 'Now my girls can stay in class all month!', 'girls|pink|42 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7796, 2334, 870, false, 13, 231, true),
  ('shomolu-market', 'shomolu', 'Corner Market', 'Market', '12 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'market', 'girl traders', 12, 'My girls can work without worry now!', 'girls|pink|12 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7780, 2379, 1020, false, 13, 232, true),
  ('shomolu-busstop', 'shomolu', 'Roadside Help Point', 'Roadside outreach', '9 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Sister Amaka', 'outreach', 'young women', 9, 'They felt seen today. Thank you.', 'girls|pink|9 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7738, 2403, 600, false, 13, 233, true),
  ('shomolu-clinic', 'shomolu', 'Family Health Centre', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Amina', 'nurse', 'girls', 16, 'We can help every girl who walks in today.', 'girls|pink|About 16 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7691, 2395, 720, false, 13, 234, true),
  ('shomolu-youth', 'shomolu', 'Youth Hub', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Fatima', 'outreach', 'girls', 21, 'Now our girls are ready, any day.', 'girls|pink|21 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7660, 2358, 1020, false, 13, 235, true),
  ('shomolu-hostel', 'shomolu', 'Palm House Hostel', 'Girls’ hostel', '29 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Rukky', 'mamaRonke', 'girls', 29, 'The girls feel at home now. Thank you!', 'girls|pink|29 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7660, 2310, 1020, false, 13, 236, true),
  ('shomolu-home', 'shomolu', 'Bright Path Home', 'Motherless home', 'A home for 16 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Titi', 'mama', 'girls', 16, 'Every one of my girls has her own kit now!', 'girls|pink|16 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7691, 2273, 1050, false, 13, 237, true),
  ('shomolu-football', 'shomolu', 'Lagoon Strikers', 'Football club', '22 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ify', 'coach', 'players', 22, 'Nobody sits out on Saturday now!', 'girls|pink|22 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7738, 2265, 570, false, 13, 238, true),
  ('shomolu-training', 'shomolu', 'Style Hair Academy', 'Training school', '16 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Folake', 'teacher', 'apprentices', 16, 'My students can work in comfort now.', 'girls|pink|16 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7780, 2289, 900, false, 13, 239, true),
  ('bariga-school', 'bariga', 'Bright Stars School', 'School', '25 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Miss Ada', 'teacher', 'girls', 25, 'Now my girls can stay in class all month!', 'girls|pink|25 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7895, 2300, 780, false, 13, 240, true),
  ('bariga-market', 'bariga', 'Textile Lane', 'Market', '15 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'market', 'girl traders', 15, 'My girls can work without worry now!', 'girls|pink|15 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7879, 2345, 960, false, 13, 241, true),
  ('bariga-busstop', 'bariga', 'Bus Stop Outreach', 'Roadside outreach', '6 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Bisi', 'outreach', 'young women', 6, 'They felt seen today. Thank you.', 'girls|pink|6 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7837, 2369, 600, false, 13, 242, true),
  ('bariga-clinic', 'bariga', 'Teen Care Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 25, 'We can help every girl who walks in today.', 'girls|pink|About 25 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7790, 2361, 660, false, 13, 243, true),
  ('bariga-youth', 'bariga', 'Youth Hub', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Ronke', 'outreach', 'girls', 27, 'Now our girls are ready, any day.', 'girls|pink|27 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7759, 2324, 1020, false, 13, 244, true),
  ('bariga-waterside', 'bariga', 'Creekside Settlement', 'Waterside community', '16 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Mrs Ajayi', 'nurse', 'girls', 16, 'These will last them a whole year!', 'girls|pink|16 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 7759, 2276, 720, false, 13, 245, true),
  ('bariga-hostel', 'bariga', 'Palm House Hostel', 'Girls’ hostel', '20 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Rukky', 'mamaRonke', 'girls', 20, 'The girls feel at home now. Thank you!', 'girls|pink|20 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7790, 2239, 1050, false, 13, 246, true),
  ('bariga-football', 'bariga', 'Stars United Girls', 'Football club', '19 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ify', 'coach', 'players', 19, 'Nobody sits out on Saturday now!', 'girls|pink|19 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7837, 2231, 630, false, 13, 247, true),
  ('bariga-training', 'bariga', 'Needle and Thread Studio', 'Training school', '14 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 14, 'My students can work in comfort now.', 'girls|pink|14 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7879, 2255, 990, false, 13, 248, true),
  ('mushin-school', 'mushin', 'Bright Stars School', 'School', '39 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Miss Zainab', 'teacher', 'girls', 39, 'Now my girls can stay in class all month!', 'girls|pink|39 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7433, 2455, 780, false, 13, 249, true),
  ('mushin-market', 'mushin', 'Waterside Market', 'Market', '21 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Sade', 'market', 'girl traders', 21, 'My girls can work without worry now!', 'girls|pink|21 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7417, 2500, 990, false, 13, 250, true),
  ('mushin-busstop', 'mushin', 'Under-Bridge Outreach', 'Roadside outreach', '7 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Bisi', 'outreach', 'young women', 7, 'They felt seen today. Thank you.', 'girls|pink|7 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7375, 2524, 720, false, 13, 251, true),
  ('mushin-clinic', 'mushin', 'Youth Health Corner', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Kemi', 'nurse', 'girls', 15, 'We can help every girl who walks in today.', 'girls|pink|About 15 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7328, 2516, 720, false, 13, 252, true),
  ('mushin-youth', 'mushin', 'Youth Hub', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 30, 'Now our girls are ready, any day.', 'girls|pink|30 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7297, 2479, 1080, false, 13, 253, true),
  ('mushin-hostel', 'mushin', 'Bluebell Hostel', 'Girls’ hostel', '23 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 23, 'The girls feel at home now. Thank you!', 'girls|pink|23 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7297, 2431, 1050, false, 13, 254, true),
  ('mushin-home', 'mushin', 'Open Arms Home', 'Motherless home', 'A home for 13 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Titi', 'mama', 'girls', 13, 'Every one of my girls has her own kit now!', 'girls|pink|13 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7328, 2394, 960, false, 13, 255, true),
  ('mushin-football', 'mushin', 'Young Lionesses FC', 'Football club', '15 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ify', 'coach', 'players', 15, 'Nobody sits out on Saturday now!', 'girls|pink|15 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7375, 2386, 630, false, 13, 256, true),
  ('mushin-training', 'mushin', 'Bright Skills Centre', 'Training school', '15 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Adeola', 'teacher', 'apprentices', 15, 'My students can work in comfort now.', 'girls|pink|15 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7417, 2410, 990, false, 13, 257, true),
  ('isolo-school', 'isolo', 'Royal Pearl College', 'School', '20 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Adeyemi', 'teacher', 'girls', 20, 'Now my girls can stay in class all month!', 'girls|pink|20 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 7126, 2433, 870, false, 13, 258, true),
  ('isolo-market', 'isolo', 'Fresh Foods Market', 'Market', '15 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Nkechi', 'market', 'girl traders', 15, 'My girls can work without worry now!', 'girls|pink|15 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7110, 2478, 1080, false, 13, 259, true),
  ('isolo-busstop', 'isolo', 'Junction Outreach', 'Roadside outreach', '6 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Joy', 'outreach', 'young women', 6, 'They felt seen today. Thank you.', 'girls|pink|6 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7068, 2502, 690, false, 13, 260, true),
  ('isolo-clinic', 'isolo', 'Teen Care Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Chika', 'nurse', 'girls', 22, 'We can help every girl who walks in today.', 'girls|pink|About 22 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7021, 2494, 750, false, 13, 261, true),
  ('isolo-youth', 'isolo', 'Study Corner', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 23, 'Now our girls are ready, any day.', 'girls|pink|23 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 6990, 2457, 1080, false, 13, 262, true),
  ('isolo-hostel', 'isolo', 'Bluebell Hostel', 'Girls’ hostel', '29 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 29, 'The girls feel at home now. Thank you!', 'girls|pink|29 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 6990, 2409, 1050, false, 13, 263, true),
  ('isolo-home', 'isolo', 'Haven Home', 'Motherless home', 'A home for 14 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Titi', 'mama', 'girls', 14, 'Every one of my girls has her own kit now!', 'girls|pink|14 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7021, 2372, 1020, false, 13, 264, true),
  ('isolo-football', 'isolo', 'Queens Football Club', 'Football club', '22 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 22, 'Nobody sits out on Saturday now!', 'girls|pink|22 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7068, 2364, 630, false, 13, 265, true),
  ('isolo-training', 'isolo', 'Style Hair Academy', 'Training school', '13 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Adeola', 'teacher', 'apprentices', 13, 'My students can work in comfort now.', 'girls|pink|13 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7110, 2388, 930, false, 13, 266, true),
  ('ejigbo-school', 'ejigbo', 'Royal Pearl College', 'School', '24 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Miss Zainab', 'teacher', 'girls', 24, 'Now my girls can stay in class all month!', 'girls|pink|24 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 6884, 2179, 840, false, 13, 267, true),
  ('ejigbo-market', 'ejigbo', 'Evening Market', 'Market', '16 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Nkechi', 'market', 'girl traders', 16, 'My girls can work without worry now!', 'girls|pink|16 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 6871, 2220, 1050, false, 13, 268, true),
  ('ejigbo-busstop', 'ejigbo', 'Under-Bridge Outreach', 'Roadside outreach', '13 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Rita', 'outreach', 'young women', 13, 'They felt seen today. Thank you.', 'girls|pink|13 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 6836, 2246, 630, false, 13, 269, true),
  ('ejigbo-clinic', 'ejigbo', 'Girls’ Health Point', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Kemi', 'nurse', 'girls', 20, 'We can help every girl who walks in today.', 'girls|pink|About 20 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 6792, 2246, 690, false, 13, 270, true),
  ('ejigbo-youth', 'ejigbo', 'Youth Hub', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 23, 'Now our girls are ready, any day.', 'girls|pink|23 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 6757, 2220, 1080, false, 13, 271, true),
  ('ejigbo-hostel', 'ejigbo', 'Palm House Hostel', 'Girls’ hostel', '27 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 27, 'The girls feel at home now. Thank you!', 'girls|pink|27 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 6744, 2179, 1080, false, 13, 272, true),
  ('ejigbo-home', 'ejigbo', 'Open Arms Home', 'Motherless home', 'A home for 12 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Asabe', 'mama', 'girls', 12, 'Every one of my girls has her own kit now!', 'girls|pink|12 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 6757, 2138, 1020, false, 13, 273, true),
  ('ejigbo-football', 'ejigbo', 'Lagoon Strikers', 'Football club', '18 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 18, 'Nobody sits out on Saturday now!', 'girls|pink|18 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 6792, 2112, 660, false, 13, 274, true),
  ('ejigbo-training', 'ejigbo', 'Bright Skills Centre', 'Training school', '20 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Folake', 'teacher', 'apprentices', 20, 'My students can work in comfort now.', 'girls|pink|20 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 6836, 2112, 990, false, 13, 275, true),
  ('ejigbo-shelter', 'ejigbo', 'Hope Camp', 'Temporary shelter', 'Families moved here after floods. 15 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Hauwa', 'woman', 'women and girls', 15, 'You have given us dignity today.', 'girls|pink|15 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 6871, 2138, 1050, false, 13, 276, true),
  ('oshodi-school', 'oshodi', 'Excel Girls’ School', 'School', '23 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Okafor', 'teacher', 'girls', 23, 'Now my girls can stay in class all month!', 'girls|pink|23 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7323, 2157, 810, false, 13, 277, true),
  ('oshodi-market', 'oshodi', 'Fruit Line Market', 'Market', '16 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Sade', 'market', 'girl traders', 16, 'My girls can work without worry now!', 'girls|pink|16 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7307, 2202, 1050, false, 13, 278, true),
  ('oshodi-busstop', 'oshodi', 'Junction Outreach', 'Roadside outreach', '14 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Bisi', 'outreach', 'young women', 14, 'They felt seen today. Thank you.', 'girls|pink|14 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7265, 2226, 660, false, 13, 279, true),
  ('oshodi-clinic', 'oshodi', 'Girls’ Health Point', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Chika', 'nurse', 'girls', 20, 'We can help every girl who walks in today.', 'girls|pink|About 20 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7218, 2218, 690, false, 13, 280, true),
  ('oshodi-youth', 'oshodi', 'Library Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Fatima', 'outreach', 'girls', 20, 'Now our girls are ready, any day.', 'girls|pink|20 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7187, 2181, 1080, false, 13, 281, true),
  ('oshodi-hostel', 'oshodi', 'Harmony Hostel', 'Girls’ hostel', '18 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Yemi', 'mamaRonke', 'girls', 18, 'The girls feel at home now. Thank you!', 'girls|pink|18 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7187, 2133, 1050, false, 13, 282, true),
  ('oshodi-home', 'oshodi', 'Hope House', 'Motherless home', 'A home for 10 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Asabe', 'mama', 'girls', 10, 'Every one of my girls has her own kit now!', 'girls|pink|10 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7218, 2096, 1050, false, 13, 283, true),
  ('oshodi-football', 'oshodi', 'Girls FC', 'Football club', '19 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 19, 'Nobody sits out on Saturday now!', 'girls|pink|19 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7265, 2088, 630, false, 13, 284, true),
  ('oshodi-training', 'oshodi', 'Bright Skills Centre', 'Training school', '16 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Bukky', 'teacher', 'apprentices', 16, 'My students can work in comfort now.', 'girls|pink|16 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7307, 2112, 930, false, 13, 285, true),
  ('ilupeju-school', 'ilupeju', 'Golden Gate School', 'School', '44 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Miss Zainab', 'teacher', 'girls', 44, 'Now my girls can stay in class all month!', 'girls|pink|44 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7521, 2179, 810, false, 13, 286, true),
  ('ilupeju-market', 'ilupeju', 'Fresh Foods Market', 'Market', '14 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Bose', 'market', 'girl traders', 14, 'My girls can work without worry now!', 'girls|pink|14 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7508, 2220, 1080, false, 13, 287, true),
  ('ilupeju-busstop', 'ilupeju', 'Motor Park Outreach', 'Roadside outreach', '8 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Sister Amaka', 'outreach', 'young women', 8, 'They felt seen today. Thank you.', 'girls|pink|8 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7473, 2246, 690, false, 13, 288, true),
  ('ilupeju-clinic', 'ilupeju', 'Girls’ Health Point', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Grace', 'nurse', 'girls', 22, 'We can help every girl who walks in today.', 'girls|pink|About 22 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7429, 2246, 750, false, 13, 289, true),
  ('ilupeju-youth', 'ilupeju', 'Youth Hub', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Fatima', 'outreach', 'girls', 15, 'Now our girls are ready, any day.', 'girls|pink|15 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7394, 2220, 1050, false, 13, 290, true),
  ('ilupeju-hostel', 'ilupeju', 'Sunflower Hostel', 'Girls’ hostel', '25 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Funmi', 'mamaRonke', 'girls', 25, 'The girls feel at home now. Thank you!', 'girls|pink|25 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7381, 2179, 1080, false, 13, 291, true),
  ('ilupeju-home', 'ilupeju', 'Little Hearts Home', 'Motherless home', 'A home for 15 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Asabe', 'mama', 'girls', 15, 'Every one of my girls has her own kit now!', 'girls|pink|15 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7394, 2138, 960, false, 13, 292, true),
  ('ilupeju-football', 'ilupeju', 'Queens Football Club', 'Football club', '15 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Halima', 'coach', 'players', 15, 'Nobody sits out on Saturday now!', 'girls|pink|15 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7429, 2112, 570, false, 13, 293, true),
  ('ilupeju-training', 'ilupeju', 'Adeola Tailoring School', 'Training school', '20 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 20, 'My students can work in comfort now.', 'girls|pink|20 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7473, 2112, 960, false, 13, 294, true),
  ('ilupeju-shelter', 'ilupeju', 'Safe Stay Shelter', 'Temporary shelter', 'Families moved here after floods. 34 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Aunty Grace', 'woman', 'women and girls', 34, 'You have given us dignity today.', 'girls|pink|34 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 7508, 2138, 1050, false, 13, 295, true),
  ('palmgrove-school', 'palmgrove', 'Excel Girls’ School', 'School', '24 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Okafor', 'teacher', 'girls', 24, 'Now my girls can stay in class all month!', 'girls|pink|24 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7653, 2289, 780, false, 13, 296, true),
  ('palmgrove-market', 'palmgrove', 'Textile Lane', 'Market', '19 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Nkechi', 'market', 'girl traders', 19, 'My girls can work without worry now!', 'girls|pink|19 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7640, 2330, 1080, false, 13, 297, true),
  ('palmgrove-busstop', 'palmgrove', 'Junction Outreach', 'Roadside outreach', '7 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Bisi', 'outreach', 'young women', 7, 'They felt seen today. Thank you.', 'girls|pink|7 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7605, 2356, 660, false, 13, 298, true),
  ('palmgrove-clinic', 'palmgrove', 'Teen Care Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Chika', 'nurse', 'girls', 18, 'We can help every girl who walks in today.', 'girls|pink|About 18 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7561, 2356, 750, false, 13, 299, true),
  ('palmgrove-youth', 'palmgrove', 'Youth Hub', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 16, 'Now our girls are ready, any day.', 'girls|pink|16 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7526, 2330, 1020, false, 13, 300, true),
  ('palmgrove-hostel', 'palmgrove', 'Bluebell Hostel', 'Girls’ hostel', '25 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Funmi', 'mamaRonke', 'girls', 25, 'The girls feel at home now. Thank you!', 'girls|pink|25 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7513, 2289, 1020, false, 13, 301, true),
  ('palmgrove-home', 'palmgrove', 'Bright Path Home', 'Motherless home', 'A home for 15 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Asabe', 'mama', 'girls', 15, 'Every one of my girls has her own kit now!', 'girls|pink|15 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7526, 2248, 1020, false, 13, 302, true),
  ('palmgrove-football', 'palmgrove', 'Lagoon Strikers', 'Football club', '21 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 21, 'Nobody sits out on Saturday now!', 'girls|pink|21 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7561, 2222, 570, false, 13, 303, true),
  ('palmgrove-training', 'palmgrove', 'Adeola Tailoring School', 'Training school', '12 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Adeola', 'teacher', 'apprentices', 12, 'My students can work in comfort now.', 'girls|pink|12 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7605, 2222, 960, false, 13, 304, true),
  ('palmgrove-shelter', 'palmgrove', 'Safe Stay Shelter', 'Temporary shelter', 'Families moved here after floods. 18 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Hauwa', 'woman', 'women and girls', 18, 'You have given us dignity today.', 'girls|pink|18 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 7640, 2248, 1020, false, 13, 305, true),
  ('gbagada-school', 'gbagada', 'Unity Girls’ College', 'School', '40 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Miss Tolu', 'teacher', 'girls', 40, 'Now my girls can stay in class all month!', 'girls|pink|40 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7851, 2157, 840, false, 13, 306, true),
  ('gbagada-market', 'gbagada', 'Evening Market', 'Market', '20 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Ngozi', 'market', 'girl traders', 20, 'My girls can work without worry now!', 'girls|pink|20 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7835, 2202, 1020, false, 13, 307, true),
  ('gbagada-busstop', 'gbagada', 'Roadside Help Point', 'Roadside outreach', '12 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Sister Amaka', 'outreach', 'young women', 12, 'They felt seen today. Thank you.', 'girls|pink|12 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7793, 2226, 660, false, 13, 308, true),
  ('gbagada-clinic', 'gbagada', 'Girls’ Health Point', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Amina', 'nurse', 'girls', 25, 'We can help every girl who walks in today.', 'girls|pink|About 25 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7746, 2218, 780, false, 13, 309, true),
  ('gbagada-youth', 'gbagada', 'Teen Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Bimpe', 'outreach', 'girls', 23, 'Now our girls are ready, any day.', 'girls|pink|23 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7715, 2181, 1050, false, 13, 310, true),
  ('gbagada-hostel', 'gbagada', 'Harmony Hostel', 'Girls’ hostel', '21 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 21, 'The girls feel at home now. Thank you!', 'girls|pink|21 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7715, 2133, 1080, false, 13, 311, true),
  ('gbagada-home', 'gbagada', 'Hope House', 'Motherless home', 'A home for 20 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Asabe', 'mama', 'girls', 20, 'Every one of my girls has her own kit now!', 'girls|pink|20 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7746, 2096, 1050, false, 13, 312, true),
  ('gbagada-football', 'gbagada', 'Lagoon Strikers', 'Football club', '22 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Halima', 'coach', 'players', 22, 'Nobody sits out on Saturday now!', 'girls|pink|22 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7793, 2088, 570, false, 13, 313, true),
  ('gbagada-training', 'gbagada', 'Bright Skills Centre', 'Training school', '10 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Adeola', 'teacher', 'apprentices', 10, 'My students can work in comfort now.', 'girls|pink|10 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7835, 2112, 990, false, 13, 314, true),
  ('oworonshoki-school', 'oworonshoki', 'Bright Stars School', 'School', '40 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Miss Tolu', 'teacher', 'girls', 40, 'Now my girls can stay in class all month!', 'girls|pink|40 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 8005, 2101, 870, false, 13, 315, true),
  ('oworonshoki-market', 'oworonshoki', 'Fruit Line Market', 'Market', '15 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'market', 'girl traders', 15, 'My girls can work without worry now!', 'girls|pink|15 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7989, 2146, 1050, false, 13, 316, true),
  ('oworonshoki-busstop', 'oworonshoki', 'Motor Park Outreach', 'Roadside outreach', '7 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Sister Amaka', 'outreach', 'young women', 7, 'They felt seen today. Thank you.', 'girls|pink|7 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7947, 2170, 720, false, 13, 317, true),
  ('oworonshoki-clinic', 'oworonshoki', 'Community Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Grace', 'nurse', 'girls', 23, 'We can help every girl who walks in today.', 'girls|pink|About 23 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7900, 2162, 750, false, 13, 318, true),
  ('oworonshoki-youth', 'oworonshoki', 'Teen Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Bimpe', 'outreach', 'girls', 16, 'Now our girls are ready, any day.', 'girls|pink|16 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7869, 2125, 1050, false, 13, 319, true),
  ('oworonshoki-waterside', 'oworonshoki', 'Canoe Landing Community', 'Waterside community', '16 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Mrs Ajayi', 'nurse', 'girls', 16, 'These will last them a whole year!', 'girls|pink|16 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 7869, 2077, 720, false, 13, 320, true),
  ('oworonshoki-hostel', 'oworonshoki', 'Bluebell Hostel', 'Girls’ hostel', '18 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 18, 'The girls feel at home now. Thank you!', 'girls|pink|18 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7900, 2040, 1050, false, 13, 321, true),
  ('oworonshoki-football', 'oworonshoki', 'Girls FC', 'Football club', '20 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 20, 'Nobody sits out on Saturday now!', 'girls|pink|20 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7947, 2032, 600, false, 13, 322, true),
  ('oworonshoki-training', 'oworonshoki', 'Adeola Tailoring School', 'Training school', '12 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Adeola', 'teacher', 'apprentices', 12, 'My students can work in comfort now.', 'girls|pink|12 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7989, 2056, 900, false, 13, 323, true),
  ('maryland-school', 'maryland', 'New Dawn Academy', 'School', '22 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Miss Zainab', 'teacher', 'girls', 22, 'Now my girls can stay in class all month!', 'girls|pink|22 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7620, 1991, 780, false, 13, 324, true),
  ('maryland-market', 'maryland', 'Textile Lane', 'Market', '18 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Nkechi', 'market', 'girl traders', 18, 'My girls can work without worry now!', 'girls|pink|18 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7604, 2036, 960, false, 13, 325, true),
  ('maryland-busstop', 'maryland', 'Roadside Help Point', 'Roadside outreach', '11 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Kemi', 'outreach', 'young women', 11, 'They felt seen today. Thank you.', 'girls|pink|11 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7562, 2060, 690, false, 13, 326, true),
  ('maryland-clinic', 'maryland', 'Youth Health Corner', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Kemi', 'nurse', 'girls', 19, 'We can help every girl who walks in today.', 'girls|pink|About 19 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7515, 2052, 660, false, 13, 327, true),
  ('maryland-youth', 'maryland', 'Teen Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Fatima', 'outreach', 'girls', 16, 'Now our girls are ready, any day.', 'girls|pink|16 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7484, 2015, 1050, false, 13, 328, true),
  ('maryland-hostel', 'maryland', 'Sunflower Hostel', 'Girls’ hostel', '29 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Funmi', 'mamaRonke', 'girls', 29, 'The girls feel at home now. Thank you!', 'girls|pink|29 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7484, 1967, 1050, false, 13, 329, true),
  ('maryland-home', 'maryland', 'Open Arms Home', 'Motherless home', 'A home for 15 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Titi', 'mama', 'girls', 15, 'Every one of my girls has her own kit now!', 'girls|pink|15 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7515, 1930, 990, false, 13, 330, true),
  ('maryland-football', 'maryland', 'Queens Football Club', 'Football club', '15 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 15, 'Nobody sits out on Saturday now!', 'girls|pink|15 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7562, 1922, 660, false, 13, 331, true),
  ('maryland-training', 'maryland', 'Crafts Workshop', 'Training school', '16 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 16, 'My students can work in comfort now.', 'girls|pink|16 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7604, 1946, 960, false, 13, 332, true),
  ('ojota-school', 'ojota', 'Bright Stars School', 'School', '39 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Okafor', 'teacher', 'girls', 39, 'Now my girls can stay in class all month!', 'girls|pink|39 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 7763, 1825, 840, false, 13, 333, true),
  ('ojota-market', 'ojota', 'Fresh Foods Market', 'Market', '23 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Ngozi', 'market', 'girl traders', 23, 'My girls can work without worry now!', 'girls|pink|23 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7750, 1866, 1080, false, 13, 334, true),
  ('ojota-busstop', 'ojota', 'Bus Stop Outreach', 'Roadside outreach', '9 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Sister Amaka', 'outreach', 'young women', 9, 'They felt seen today. Thank you.', 'girls|pink|9 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7715, 1892, 660, false, 13, 335, true),
  ('ojota-clinic', 'ojota', 'Teen Care Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 16, 'We can help every girl who walks in today.', 'girls|pink|About 16 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7671, 1892, 660, false, 13, 336, true),
  ('ojota-youth', 'ojota', 'Study Corner', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 24, 'Now our girls are ready, any day.', 'girls|pink|24 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7636, 1866, 1050, false, 13, 337, true),
  ('ojota-hostel', 'ojota', 'Bluebell Hostel', 'Girls’ hostel', '26 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Yemi', 'mamaRonke', 'girls', 26, 'The girls feel at home now. Thank you!', 'girls|pink|26 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7623, 1825, 1050, false, 13, 338, true),
  ('ojota-home', 'ojota', 'Little Hearts Home', 'Motherless home', 'A home for 13 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ronke', 'mama', 'girls', 13, 'Every one of my girls has her own kit now!', 'girls|pink|13 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7636, 1784, 1050, false, 13, 339, true),
  ('ojota-football', 'ojota', 'Stars United Girls', 'Football club', '21 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ify', 'coach', 'players', 21, 'Nobody sits out on Saturday now!', 'girls|pink|21 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7671, 1758, 660, false, 13, 340, true),
  ('ojota-training', 'ojota', 'Adeola Tailoring School', 'Training school', '17 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Bukky', 'teacher', 'apprentices', 17, 'My students can work in comfort now.', 'girls|pink|17 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7715, 1758, 990, false, 13, 341, true),
  ('ojota-shelter', 'ojota', 'Rest Point Shelter', 'Temporary shelter', 'Families moved here after floods. 33 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Aunty Grace', 'woman', 'women and girls', 33, 'You have given us dignity today.', 'girls|pink|33 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 7750, 1784, 1020, false, 13, 342, true),
  ('ketu-school', 'ketu', 'Royal Pearl College', 'School', '22 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Adeyemi', 'teacher', 'girls', 22, 'Now my girls can stay in class all month!', 'girls|pink|22 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7873, 1692, 840, false, 13, 343, true),
  ('ketu-market', 'ketu', 'Sunrise Market', 'Market', '22 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Ngozi', 'market', 'girl traders', 22, 'My girls can work without worry now!', 'girls|pink|22 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7860, 1733, 1020, false, 13, 344, true),
  ('ketu-busstop', 'ketu', 'Bus Stop Outreach', 'Roadside outreach', '7 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Joy', 'outreach', 'young women', 7, 'They felt seen today. Thank you.', 'girls|pink|7 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7825, 1759, 660, false, 13, 345, true),
  ('ketu-clinic', 'ketu', 'Community Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Amina', 'nurse', 'girls', 21, 'We can help every girl who walks in today.', 'girls|pink|About 21 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7781, 1759, 780, false, 13, 346, true),
  ('ketu-youth', 'ketu', 'Youth Hub', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 15, 'Now our girls are ready, any day.', 'girls|pink|15 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7746, 1733, 1080, false, 13, 347, true),
  ('ketu-hostel', 'ketu', 'Harmony Hostel', 'Girls’ hostel', '27 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Rukky', 'mamaRonke', 'girls', 27, 'The girls feel at home now. Thank you!', 'girls|pink|27 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7733, 1692, 1050, false, 13, 348, true),
  ('ketu-home', 'ketu', 'Bright Path Home', 'Motherless home', 'A home for 11 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Titi', 'mama', 'girls', 11, 'Every one of my girls has her own kit now!', 'girls|pink|11 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7746, 1651, 1020, false, 13, 349, true),
  ('ketu-football', 'ketu', 'Queens Football Club', 'Football club', '14 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 14, 'Nobody sits out on Saturday now!', 'girls|pink|14 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7781, 1625, 660, false, 13, 350, true),
  ('ketu-training', 'ketu', 'Adeola Tailoring School', 'Training school', '11 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 11, 'My students can work in comfort now.', 'girls|pink|11 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7825, 1625, 990, false, 13, 351, true),
  ('ketu-shelter', 'ketu', 'Flood Relief Shelter', 'Temporary shelter', 'Families moved here after floods. 30 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Hauwa', 'woman', 'women and girls', 30, 'You have given us dignity today.', 'girls|pink|30 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 7860, 1651, 1050, false, 13, 352, true),
  ('mile-12-school', 'mile-12', 'Excel Girls’ School', 'School', '27 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Miss Zainab', 'teacher', 'girls', 27, 'Now my girls can stay in class all month!', 'girls|pink|27 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7983, 1582, 810, false, 13, 353, true),
  ('mile-12-market', 'mile-12', 'Corner Market', 'Market', '24 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Nkechi', 'market', 'girl traders', 24, 'My girls can work without worry now!', 'girls|pink|24 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7970, 1623, 990, false, 13, 354, true),
  ('mile-12-busstop', 'mile-12', 'Bus Stop Outreach', 'Roadside outreach', '6 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Kemi', 'outreach', 'young women', 6, 'They felt seen today. Thank you.', 'girls|pink|6 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7935, 1649, 600, false, 13, 355, true),
  ('mile-12-clinic', 'mile-12', 'Community Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 25, 'We can help every girl who walks in today.', 'girls|pink|About 25 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7891, 1649, 750, false, 13, 356, true),
  ('mile-12-youth', 'mile-12', 'Study Corner', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Bimpe', 'outreach', 'girls', 16, 'Now our girls are ready, any day.', 'girls|pink|16 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7856, 1623, 1020, false, 13, 357, true),
  ('mile-12-waterside', 'mile-12', 'Canoe Landing Community', 'Waterside community', '24 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Aunty Toyin', 'nurse', 'girls', 24, 'These will last them a whole year!', 'girls|pink|24 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 7843, 1582, 780, false, 13, 358, true),
  ('mile-12-hostel', 'mile-12', 'Kingsway Girls’ Hostel', 'Girls’ hostel', '22 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Rukky', 'mamaRonke', 'girls', 22, 'The girls feel at home now. Thank you!', 'girls|pink|22 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7856, 1541, 1050, false, 13, 359, true),
  ('mile-12-football', 'mile-12', 'Lagoon Strikers', 'Football club', '20 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 20, 'Nobody sits out on Saturday now!', 'girls|pink|20 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7891, 1515, 600, false, 13, 360, true),
  ('mile-12-training', 'mile-12', 'Style Hair Academy', 'Training school', '20 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Adeola', 'teacher', 'apprentices', 20, 'My students can work in comfort now.', 'girls|pink|20 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7935, 1515, 930, false, 13, 361, true),
  ('mile-12-shelter', 'mile-12', 'Safe Stay Shelter', 'Temporary shelter', 'Families moved here after floods. 22 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Nwosu', 'woman', 'women and girls', 22, 'You have given us dignity today.', 'girls|pink|22 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 7970, 1541, 1020, false, 13, 362, true),
  ('ikosi-school', 'ikosi', 'Unity Girls’ College', 'School', '32 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Miss Zainab', 'teacher', 'girls', 32, 'Now my girls can stay in class all month!', 'girls|pink|32 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7906, 1626, 840, false, 13, 363, true),
  ('ikosi-market', 'ikosi', 'Sunrise Market', 'Market', '20 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Bose', 'market', 'girl traders', 20, 'My girls can work without worry now!', 'girls|pink|20 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7890, 1671, 990, false, 13, 364, true),
  ('ikosi-busstop', 'ikosi', 'Motor Park Outreach', 'Roadside outreach', '11 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Rita', 'outreach', 'young women', 11, 'They felt seen today. Thank you.', 'girls|pink|11 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7848, 1695, 720, false, 13, 365, true),
  ('ikosi-clinic', 'ikosi', 'Teen Care Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Amina', 'nurse', 'girls', 14, 'We can help every girl who walks in today.', 'girls|pink|About 14 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7801, 1687, 750, false, 13, 366, true),
  ('ikosi-youth', 'ikosi', 'After-School Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Fatima', 'outreach', 'girls', 28, 'Now our girls are ready, any day.', 'girls|pink|28 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7770, 1650, 1080, false, 13, 367, true),
  ('ikosi-hostel', 'ikosi', 'Palm House Hostel', 'Girls’ hostel', '22 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Rukky', 'mamaRonke', 'girls', 22, 'The girls feel at home now. Thank you!', 'girls|pink|22 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7770, 1602, 1050, false, 13, 368, true),
  ('ikosi-home', 'ikosi', 'Open Arms Home', 'Motherless home', 'A home for 11 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Asabe', 'mama', 'girls', 11, 'Every one of my girls has her own kit now!', 'girls|pink|11 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7801, 1565, 960, false, 13, 369, true),
  ('ikosi-football', 'ikosi', 'Queens Football Club', 'Football club', '16 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 16, 'Nobody sits out on Saturday now!', 'girls|pink|16 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7848, 1557, 630, false, 13, 370, true),
  ('ikosi-training', 'ikosi', 'Needle and Thread Studio', 'Training school', '11 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Folake', 'teacher', 'apprentices', 11, 'My students can work in comfort now.', 'girls|pink|11 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7890, 1581, 960, false, 13, 371, true),
  ('magodo-school', 'magodo', 'Royal Pearl College', 'School', '27 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Miss Zainab', 'teacher', 'girls', 27, 'Now my girls can stay in class all month!', 'girls|pink|27 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7818, 1438, 810, false, 13, 372, true),
  ('magodo-market', 'magodo', 'Textile Lane', 'Market', '11 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'market', 'girl traders', 11, 'My girls can work without worry now!', 'girls|pink|11 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7802, 1483, 1050, false, 13, 373, true),
  ('magodo-busstop', 'magodo', 'Under-Bridge Outreach', 'Roadside outreach', '14 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Bisi', 'outreach', 'young women', 14, 'They felt seen today. Thank you.', 'girls|pink|14 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7760, 1507, 720, false, 13, 374, true),
  ('magodo-clinic', 'magodo', 'Community Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Chika', 'nurse', 'girls', 16, 'We can help every girl who walks in today.', 'girls|pink|About 16 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7713, 1499, 660, false, 13, 375, true),
  ('magodo-youth', 'magodo', 'Library Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Ronke', 'outreach', 'girls', 28, 'Now our girls are ready, any day.', 'girls|pink|28 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7682, 1462, 1020, false, 13, 376, true),
  ('magodo-hostel', 'magodo', 'Palm House Hostel', 'Girls’ hostel', '20 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 20, 'The girls feel at home now. Thank you!', 'girls|pink|20 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7682, 1414, 1080, false, 13, 377, true),
  ('magodo-home', 'magodo', 'Bright Path Home', 'Motherless home', 'A home for 11 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Asabe', 'mama', 'girls', 11, 'Every one of my girls has her own kit now!', 'girls|pink|11 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7713, 1377, 1020, false, 13, 378, true),
  ('magodo-football', 'magodo', 'Queens Football Club', 'Football club', '16 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 16, 'Nobody sits out on Saturday now!', 'girls|pink|16 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7760, 1369, 630, false, 13, 379, true),
  ('magodo-training', 'magodo', 'Crafts Workshop', 'Training school', '13 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Folake', 'teacher', 'apprentices', 13, 'My students can work in comfort now.', 'girls|pink|13 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7802, 1393, 900, false, 13, 380, true),
  ('ikeja-school', 'ikeja', 'Rainbow College', 'School', '31 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Miss Ada', 'teacher', 'girls', 31, 'Now my girls can stay in class all month!', 'girls|pink|31 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 7444, 1648, 780, false, 13, 381, true),
  ('ikeja-market', 'ikeja', 'Fruit Line Market', 'Market', '23 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Bose', 'market', 'girl traders', 23, 'My girls can work without worry now!', 'girls|pink|23 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7428, 1693, 990, false, 13, 382, true),
  ('ikeja-busstop', 'ikeja', 'Motor Park Outreach', 'Roadside outreach', '8 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Sister Amaka', 'outreach', 'young women', 8, 'They felt seen today. Thank you.', 'girls|pink|8 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7386, 1717, 720, false, 13, 383, true),
  ('ikeja-clinic', 'ikeja', 'Community Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 21, 'We can help every girl who walks in today.', 'girls|pink|About 21 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7339, 1709, 750, false, 13, 384, true),
  ('ikeja-youth', 'ikeja', 'Youth Hub', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Fatima', 'outreach', 'girls', 17, 'Now our girls are ready, any day.', 'girls|pink|17 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7308, 1672, 1080, false, 13, 385, true),
  ('ikeja-hostel', 'ikeja', 'Harmony Hostel', 'Girls’ hostel', '24 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Funmi', 'mamaRonke', 'girls', 24, 'The girls feel at home now. Thank you!', 'girls|pink|24 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7308, 1624, 1080, false, 13, 386, true),
  ('ikeja-home', 'ikeja', 'Bright Path Home', 'Motherless home', 'A home for 17 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ronke', 'mama', 'girls', 17, 'Every one of my girls has her own kit now!', 'girls|pink|17 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7339, 1587, 990, false, 13, 387, true),
  ('ikeja-football', 'ikeja', 'Girls FC', 'Football club', '14 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 14, 'Nobody sits out on Saturday now!', 'girls|pink|14 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7386, 1579, 600, false, 13, 388, true),
  ('ikeja-training', 'ikeja', 'Needle and Thread Studio', 'Training school', '12 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Adeola', 'teacher', 'apprentices', 12, 'My students can work in comfort now.', 'girls|pink|12 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7428, 1603, 900, false, 13, 389, true),
  ('ogba-school', 'ogba', 'Bright Stars School', 'School', '42 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Eze', 'teacher', 'girls', 42, 'Now my girls can stay in class all month!', 'girls|pink|42 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7323, 1327, 870, false, 13, 390, true),
  ('ogba-market', 'ogba', 'Evening Market', 'Market', '17 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'market', 'girl traders', 17, 'My girls can work without worry now!', 'girls|pink|17 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7310, 1368, 990, false, 13, 391, true),
  ('ogba-busstop', 'ogba', 'Under-Bridge Outreach', 'Roadside outreach', '6 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Rita', 'outreach', 'young women', 6, 'They felt seen today. Thank you.', 'girls|pink|6 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7275, 1394, 720, false, 13, 392, true),
  ('ogba-clinic', 'ogba', 'Youth Health Corner', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Kemi', 'nurse', 'girls', 20, 'We can help every girl who walks in today.', 'girls|pink|About 20 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7231, 1394, 660, false, 13, 393, true),
  ('ogba-youth', 'ogba', 'Youth Hub', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Ronke', 'outreach', 'girls', 27, 'Now our girls are ready, any day.', 'girls|pink|27 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7196, 1368, 1080, false, 13, 394, true),
  ('ogba-hostel', 'ogba', 'Sunflower Hostel', 'Girls’ hostel', '27 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 27, 'The girls feel at home now. Thank you!', 'girls|pink|27 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7183, 1327, 1020, false, 13, 395, true),
  ('ogba-home', 'ogba', 'Open Arms Home', 'Motherless home', 'A home for 20 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ronke', 'mama', 'girls', 20, 'Every one of my girls has her own kit now!', 'girls|pink|20 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7196, 1286, 1050, false, 13, 396, true),
  ('ogba-football', 'ogba', 'Stars United Girls', 'Football club', '14 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 14, 'Nobody sits out on Saturday now!', 'girls|pink|14 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7231, 1260, 600, false, 13, 397, true),
  ('ogba-training', 'ogba', 'Bright Skills Centre', 'Training school', '15 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Adeola', 'teacher', 'apprentices', 15, 'My students can work in comfort now.', 'girls|pink|15 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7275, 1260, 960, false, 13, 398, true),
  ('ogba-shelter', 'ogba', 'Safe Stay Shelter', 'Temporary shelter', 'Families moved here after floods. 31 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Nwosu', 'woman', 'women and girls', 31, 'You have given us dignity today.', 'girls|pink|31 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 7310, 1286, 1080, false, 13, 399, true),
  ('agege-school', 'agege', 'New Dawn Academy', 'School', '31 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Miss Ada', 'teacher', 'girls', 31, 'Now my girls can stay in class all month!', 'girls|pink|31 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7082, 1382, 840, false, 13, 400, true),
  ('agege-market', 'agege', 'Corner Market', 'Market', '23 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Nkechi', 'market', 'girl traders', 23, 'My girls can work without worry now!', 'girls|pink|23 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7066, 1427, 1050, false, 13, 401, true),
  ('agege-busstop', 'agege', 'Bus Stop Outreach', 'Roadside outreach', '12 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Bisi', 'outreach', 'young women', 12, 'They felt seen today. Thank you.', 'girls|pink|12 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7024, 1451, 720, false, 13, 402, true),
  ('agege-clinic', 'agege', 'Family Health Centre', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Kemi', 'nurse', 'girls', 16, 'We can help every girl who walks in today.', 'girls|pink|About 16 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 6977, 1443, 780, false, 13, 403, true),
  ('agege-youth', 'agege', 'Library Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Bimpe', 'outreach', 'girls', 18, 'Now our girls are ready, any day.', 'girls|pink|18 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 6946, 1406, 1050, false, 13, 404, true),
  ('agege-hostel', 'agege', 'Harmony Hostel', 'Girls’ hostel', '21 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Funmi', 'mamaRonke', 'girls', 21, 'The girls feel at home now. Thank you!', 'girls|pink|21 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 6946, 1358, 1050, false, 13, 405, true),
  ('agege-home', 'agege', 'Hope House', 'Motherless home', 'A home for 15 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ronke', 'mama', 'girls', 15, 'Every one of my girls has her own kit now!', 'girls|pink|15 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 6977, 1321, 1020, false, 13, 406, true),
  ('agege-football', 'agege', 'Young Lionesses FC', 'Football club', '15 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 15, 'Nobody sits out on Saturday now!', 'girls|pink|15 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7024, 1313, 660, false, 13, 407, true),
  ('agege-training', 'agege', 'Bright Skills Centre', 'Training school', '11 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 11, 'My students can work in comfort now.', 'girls|pink|11 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7066, 1337, 960, false, 13, 408, true),
  ('ifako-school', 'ifako', 'Lighthouse Model School', 'School', '22 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Miss Tolu', 'teacher', 'girls', 22, 'Now my girls can stay in class all month!', 'girls|pink|22 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 7213, 1128, 810, false, 13, 409, true),
  ('ifako-market', 'ifako', 'Fresh Foods Market', 'Market', '22 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Sade', 'market', 'girl traders', 22, 'My girls can work without worry now!', 'girls|pink|22 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7200, 1169, 1020, false, 13, 410, true),
  ('ifako-busstop', 'ifako', 'Bus Stop Outreach', 'Roadside outreach', '13 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Joy', 'outreach', 'young women', 13, 'They felt seen today. Thank you.', 'girls|pink|13 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7165, 1195, 660, false, 13, 411, true),
  ('ifako-clinic', 'ifako', 'Family Health Centre', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 15, 'We can help every girl who walks in today.', 'girls|pink|About 15 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7121, 1195, 750, false, 13, 412, true),
  ('ifako-youth', 'ifako', 'Youth Hub', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Ronke', 'outreach', 'girls', 27, 'Now our girls are ready, any day.', 'girls|pink|27 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7086, 1169, 1050, false, 13, 413, true),
  ('ifako-hostel', 'ifako', 'Sunflower Hostel', 'Girls’ hostel', '26 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Rukky', 'mamaRonke', 'girls', 26, 'The girls feel at home now. Thank you!', 'girls|pink|26 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7073, 1128, 1050, false, 13, 414, true),
  ('ifako-home', 'ifako', 'Hope House', 'Motherless home', 'A home for 13 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ronke', 'mama', 'girls', 13, 'Every one of my girls has her own kit now!', 'girls|pink|13 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7086, 1087, 990, false, 13, 415, true),
  ('ifako-football', 'ifako', 'Young Lionesses FC', 'Football club', '22 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Halima', 'coach', 'players', 22, 'Nobody sits out on Saturday now!', 'girls|pink|22 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7121, 1061, 660, false, 13, 416, true),
  ('ifako-training', 'ifako', 'Bright Skills Centre', 'Training school', '20 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 20, 'My students can work in comfort now.', 'girls|pink|20 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7165, 1061, 900, false, 13, 417, true),
  ('ifako-shelter', 'ifako', 'Rest Point Shelter', 'Temporary shelter', 'Families moved here after floods. 31 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Bello', 'woman', 'women and girls', 31, 'You have given us dignity today.', 'girls|pink|31 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 7200, 1087, 1080, false, 13, 418, true),
  ('ojodu-school', 'ojodu', 'Lighthouse Model School', 'School', '41 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Adeyemi', 'teacher', 'girls', 41, 'Now my girls can stay in class all month!', 'girls|pink|41 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 7631, 1250, 780, false, 13, 419, true),
  ('ojodu-market', 'ojodu', 'Fresh Foods Market', 'Market', '25 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'market', 'girl traders', 25, 'My girls can work without worry now!', 'girls|pink|25 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7618, 1291, 960, false, 13, 420, true),
  ('ojodu-busstop', 'ojodu', 'Under-Bridge Outreach', 'Roadside outreach', '12 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Kemi', 'outreach', 'young women', 12, 'They felt seen today. Thank you.', 'girls|pink|12 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7583, 1317, 630, false, 13, 421, true),
  ('ojodu-clinic', 'ojodu', 'Family Health Centre', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 18, 'We can help every girl who walks in today.', 'girls|pink|About 18 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7539, 1317, 660, false, 13, 422, true),
  ('ojodu-youth', 'ojodu', 'Study Corner', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Fatima', 'outreach', 'girls', 27, 'Now our girls are ready, any day.', 'girls|pink|27 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7504, 1291, 1050, false, 13, 423, true),
  ('ojodu-hostel', 'ojodu', 'Bluebell Hostel', 'Girls’ hostel', '23 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Yemi', 'mamaRonke', 'girls', 23, 'The girls feel at home now. Thank you!', 'girls|pink|23 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7491, 1250, 1080, false, 13, 424, true),
  ('ojodu-home', 'ojodu', 'Open Arms Home', 'Motherless home', 'A home for 15 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Asabe', 'mama', 'girls', 15, 'Every one of my girls has her own kit now!', 'girls|pink|15 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7504, 1209, 1050, false, 13, 425, true),
  ('ojodu-football', 'ojodu', 'Young Lionesses FC', 'Football club', '18 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Halima', 'coach', 'players', 18, 'Nobody sits out on Saturday now!', 'girls|pink|18 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7539, 1183, 600, false, 13, 426, true),
  ('ojodu-training', 'ojodu', 'Adeola Tailoring School', 'Training school', '16 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 16, 'My students can work in comfort now.', 'girls|pink|16 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7583, 1183, 960, false, 13, 427, true),
  ('ojodu-shelter', 'ojodu', 'Safe Stay Shelter', 'Temporary shelter', 'Families moved here after floods. 34 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Nwosu', 'woman', 'women and girls', 34, 'You have given us dignity today.', 'girls|pink|34 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 7618, 1209, 1020, false, 13, 428, true),
  ('berger-school', 'berger', 'Excel Girls’ School', 'School', '28 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Adeyemi', 'teacher', 'girls', 28, 'Now my girls can stay in class all month!', 'girls|pink|28 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 7741, 1150, 870, false, 13, 429, true),
  ('berger-market', 'berger', 'Evening Market', 'Market', '23 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'market', 'girl traders', 23, 'My girls can work without worry now!', 'girls|pink|23 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7728, 1191, 1080, false, 13, 430, true),
  ('berger-busstop', 'berger', 'Bus Stop Outreach', 'Roadside outreach', '11 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Kemi', 'outreach', 'young women', 11, 'They felt seen today. Thank you.', 'girls|pink|11 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7693, 1217, 630, false, 13, 431, true),
  ('berger-clinic', 'berger', 'Community Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 13, 'We can help every girl who walks in today.', 'girls|pink|About 13 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7649, 1217, 750, false, 13, 432, true),
  ('berger-youth', 'berger', 'Teen Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 19, 'Now our girls are ready, any day.', 'girls|pink|19 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7614, 1191, 1050, false, 13, 433, true),
  ('berger-hostel', 'berger', 'Bluebell Hostel', 'Girls’ hostel', '30 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Funmi', 'mamaRonke', 'girls', 30, 'The girls feel at home now. Thank you!', 'girls|pink|30 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7601, 1150, 1020, false, 13, 434, true),
  ('berger-home', 'berger', 'Little Hearts Home', 'Motherless home', 'A home for 15 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ronke', 'mama', 'girls', 15, 'Every one of my girls has her own kit now!', 'girls|pink|15 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7614, 1109, 1020, false, 13, 435, true),
  ('berger-football', 'berger', 'Girls FC', 'Football club', '22 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Halima', 'coach', 'players', 22, 'Nobody sits out on Saturday now!', 'girls|pink|22 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7649, 1083, 630, false, 13, 436, true),
  ('berger-training', 'berger', 'Crafts Workshop', 'Training school', '18 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 18, 'My students can work in comfort now.', 'girls|pink|18 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7693, 1083, 930, false, 13, 437, true),
  ('berger-shelter', 'berger', 'Safe Stay Shelter', 'Temporary shelter', 'Families moved here after floods. 31 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Hauwa', 'woman', 'women and girls', 31, 'You have given us dignity today.', 'girls|pink|31 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 7728, 1109, 1080, false, 13, 438, true),
  ('ikorodu-school', 'ikorodu', 'Bright Stars School', 'School', '23 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Miss Tolu', 'teacher', 'girls', 23, 'Now my girls can stay in class all month!', 'girls|pink|23 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 9137, 1493, 870, false, 13, 439, true),
  ('ikorodu-market', 'ikorodu', 'Textile Lane', 'Market', '10 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Sade', 'market', 'girl traders', 10, 'My girls can work without worry now!', 'girls|pink|10 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 9121, 1538, 960, false, 13, 440, true),
  ('ikorodu-busstop', 'ikorodu', 'Junction Outreach', 'Roadside outreach', '10 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Bisi', 'outreach', 'young women', 10, 'They felt seen today. Thank you.', 'girls|pink|10 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 9079, 1562, 600, false, 13, 441, true),
  ('ikorodu-clinic', 'ikorodu', 'Girls’ Health Point', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 15, 'We can help every girl who walks in today.', 'girls|pink|About 15 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 9032, 1554, 780, false, 13, 442, true),
  ('ikorodu-youth', 'ikorodu', 'Teen Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Bimpe', 'outreach', 'girls', 23, 'Now our girls are ready, any day.', 'girls|pink|23 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 9001, 1517, 1020, false, 13, 443, true),
  ('ikorodu-hostel', 'ikorodu', 'Kingsway Girls’ Hostel', 'Girls’ hostel', '20 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Funmi', 'mamaRonke', 'girls', 20, 'The girls feel at home now. Thank you!', 'girls|pink|20 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 9001, 1469, 1020, false, 13, 444, true),
  ('ikorodu-home', 'ikorodu', 'Open Arms Home', 'Motherless home', 'A home for 12 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Asabe', 'mama', 'girls', 12, 'Every one of my girls has her own kit now!', 'girls|pink|12 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 9032, 1432, 960, false, 13, 445, true),
  ('ikorodu-football', 'ikorodu', 'Queens Football Club', 'Football club', '14 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 14, 'Nobody sits out on Saturday now!', 'girls|pink|14 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 9079, 1424, 630, false, 13, 446, true),
  ('ikorodu-training', 'ikorodu', 'Needle and Thread Studio', 'Training school', '14 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Adeola', 'teacher', 'apprentices', 14, 'My students can work in comfort now.', 'girls|pink|14 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 9121, 1448, 900, false, 13, 447, true),
  ('igbogbo-school', 'igbogbo', 'Bright Stars School', 'School', '34 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Okafor', 'teacher', 'girls', 34, 'Now my girls can stay in class all month!', 'girls|pink|34 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 9357, 1825, 870, false, 13, 448, true),
  ('igbogbo-market', 'igbogbo', 'Waterside Market', 'Market', '11 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Nkechi', 'market', 'girl traders', 11, 'My girls can work without worry now!', 'girls|pink|11 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 9344, 1866, 990, false, 13, 449, true),
  ('igbogbo-busstop', 'igbogbo', 'Junction Outreach', 'Roadside outreach', '9 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Joy', 'outreach', 'young women', 9, 'They felt seen today. Thank you.', 'girls|pink|9 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 9309, 1892, 660, false, 13, 450, true),
  ('igbogbo-clinic', 'igbogbo', 'Community Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Chika', 'nurse', 'girls', 12, 'We can help every girl who walks in today.', 'girls|pink|About 12 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 9265, 1892, 780, false, 13, 451, true),
  ('igbogbo-youth', 'igbogbo', 'Youth Hub', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 23, 'Now our girls are ready, any day.', 'girls|pink|23 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 9230, 1866, 1080, false, 13, 452, true),
  ('igbogbo-waterside', 'igbogbo', 'Canoe Landing Community', 'Waterside community', '22 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Mrs Ajayi', 'nurse', 'girls', 22, 'These will last them a whole year!', 'girls|pink|22 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 9217, 1825, 720, false, 13, 453, true),
  ('igbogbo-hostel', 'igbogbo', 'Kingsway Girls’ Hostel', 'Girls’ hostel', '30 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 30, 'The girls feel at home now. Thank you!', 'girls|pink|30 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 9230, 1784, 1020, false, 13, 454, true),
  ('igbogbo-football', 'igbogbo', 'Queens Football Club', 'Football club', '19 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ify', 'coach', 'players', 19, 'Nobody sits out on Saturday now!', 'girls|pink|19 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 9265, 1758, 630, false, 13, 455, true),
  ('igbogbo-training', 'igbogbo', 'Needle and Thread Studio', 'Training school', '16 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Folake', 'teacher', 'apprentices', 16, 'My students can work in comfort now.', 'girls|pink|16 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 9309, 1758, 960, false, 13, 456, true),
  ('igbogbo-shelter', 'igbogbo', 'Rest Point Shelter', 'Temporary shelter', 'Families moved here after floods. 27 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Bello', 'woman', 'women and girls', 27, 'You have given us dignity today.', 'girls|pink|27 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 9344, 1784, 1080, false, 13, 457, true),
  ('ijede-school', 'ijede', 'Sunrise Grammar School', 'School', '26 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Okafor', 'teacher', 'girls', 26, 'Now my girls can stay in class all month!', 'girls|pink|26 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 10071, 1991, 870, false, 13, 458, true),
  ('ijede-market', 'ijede', 'Evening Market', 'Market', '11 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Nkechi', 'market', 'girl traders', 11, 'My girls can work without worry now!', 'girls|pink|11 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 10058, 2032, 990, false, 13, 459, true),
  ('ijede-busstop', 'ijede', 'Roadside Help Point', 'Roadside outreach', '8 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Kemi', 'outreach', 'young women', 8, 'They felt seen today. Thank you.', 'girls|pink|8 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 10023, 2058, 630, false, 13, 460, true),
  ('ijede-clinic', 'ijede', 'Girls’ Health Point', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Grace', 'nurse', 'girls', 20, 'We can help every girl who walks in today.', 'girls|pink|About 20 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 9979, 2058, 690, false, 13, 461, true),
  ('ijede-youth', 'ijede', 'Teen Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 26, 'Now our girls are ready, any day.', 'girls|pink|26 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 9944, 2032, 1080, false, 13, 462, true),
  ('ijede-waterside', 'ijede', 'Fishing Village', 'Waterside community', '21 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Mama Efe', 'nurse', 'girls', 21, 'These will last them a whole year!', 'girls|pink|21 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 9931, 1991, 900, false, 13, 463, true),
  ('ijede-hostel', 'ijede', 'Harmony Hostel', 'Girls’ hostel', '25 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Yemi', 'mamaRonke', 'girls', 25, 'The girls feel at home now. Thank you!', 'girls|pink|25 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 9944, 1950, 1080, false, 13, 464, true),
  ('ijede-football', 'ijede', 'Lagoon Strikers', 'Football club', '19 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 19, 'Nobody sits out on Saturday now!', 'girls|pink|19 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 9979, 1924, 600, false, 13, 465, true),
  ('ijede-training', 'ijede', 'Bright Skills Centre', 'Training school', '11 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Adeola', 'teacher', 'apprentices', 11, 'My students can work in comfort now.', 'girls|pink|11 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 10023, 1924, 930, false, 13, 466, true),
  ('ijede-shelter', 'ijede', 'Flood Relief Shelter', 'Temporary shelter', 'Families moved here after floods. 24 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Aunty Grace', 'woman', 'women and girls', 24, 'You have given us dignity today.', 'girls|pink|24 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 10058, 1950, 1020, false, 13, 467, true),
  ('imota-school', 'imota', 'Excel Girls’ School', 'School', '41 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Adeyemi', 'teacher', 'girls', 41, 'Now my girls can stay in class all month!', 'girls|pink|41 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 10730, 940, 840, false, 13, 468, true),
  ('imota-market', 'imota', 'Sunrise Market', 'Market', '16 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Nkechi', 'market', 'girl traders', 16, 'My girls can work without worry now!', 'girls|pink|16 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 10717, 981, 1020, false, 13, 469, true),
  ('imota-busstop', 'imota', 'Roadside Help Point', 'Roadside outreach', '8 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Rita', 'outreach', 'young women', 8, 'They felt seen today. Thank you.', 'girls|pink|8 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 10682, 1007, 690, false, 13, 470, true),
  ('imota-clinic', 'imota', 'Community Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Grace', 'nurse', 'girls', 24, 'We can help every girl who walks in today.', 'girls|pink|About 24 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 10638, 1007, 660, false, 13, 471, true),
  ('imota-youth', 'imota', 'After-School Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 26, 'Now our girls are ready, any day.', 'girls|pink|26 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 10603, 981, 1020, false, 13, 472, true),
  ('imota-hostel', 'imota', 'Kingsway Girls’ Hostel', 'Girls’ hostel', '24 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 24, 'The girls feel at home now. Thank you!', 'girls|pink|24 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 10590, 940, 1050, false, 13, 473, true),
  ('imota-home', 'imota', 'Bright Path Home', 'Motherless home', 'A home for 11 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Titi', 'mama', 'girls', 11, 'Every one of my girls has her own kit now!', 'girls|pink|11 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 10603, 899, 960, false, 13, 474, true),
  ('imota-football', 'imota', 'Young Lionesses FC', 'Football club', '20 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 20, 'Nobody sits out on Saturday now!', 'girls|pink|20 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 10638, 873, 660, false, 13, 475, true),
  ('imota-training', 'imota', 'Bright Skills Centre', 'Training school', '13 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Adeola', 'teacher', 'apprentices', 13, 'My students can work in comfort now.', 'girls|pink|13 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 10682, 873, 960, false, 13, 476, true),
  ('imota-shelter', 'imota', 'Rest Point Shelter', 'Temporary shelter', 'Families moved here after floods. 35 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Bello', 'woman', 'women and girls', 35, 'You have given us dignity today.', 'girls|pink|35 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 10717, 899, 1050, false, 13, 477, true),
  ('lagos-island-school', 'lagos-island', 'Unity Girls’ College', 'School', '38 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Miss Tolu', 'teacher', 'girls', 38, 'Now my girls can stay in class all month!', 'girls|pink|38 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 7928, 3263, 840, false, 13, 478, true),
  ('lagos-island-market', 'lagos-island', 'Textile Lane', 'Market', '11 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'market', 'girl traders', 11, 'My girls can work without worry now!', 'girls|pink|11 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 7912, 3308, 960, false, 13, 479, true),
  ('lagos-island-busstop', 'lagos-island', 'Bus Stop Outreach', 'Roadside outreach', '11 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Rita', 'outreach', 'young women', 11, 'They felt seen today. Thank you.', 'girls|pink|11 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7870, 3332, 690, false, 13, 480, true),
  ('lagos-island-clinic', 'lagos-island', 'Community Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 23, 'We can help every girl who walks in today.', 'girls|pink|About 23 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7823, 3324, 660, false, 13, 481, true),
  ('lagos-island-youth', 'lagos-island', 'Study Corner', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 26, 'Now our girls are ready, any day.', 'girls|pink|26 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7792, 3287, 1080, false, 13, 482, true),
  ('lagos-island-waterside', 'lagos-island', 'Fishing Village', 'Waterside community', '16 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Mrs Ajayi', 'nurse', 'girls', 16, 'These will last them a whole year!', 'girls|pink|16 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 7792, 3239, 900, false, 13, 483, true),
  ('lagos-island-hostel', 'lagos-island', 'Bluebell Hostel', 'Girls’ hostel', '21 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Rukky', 'mamaRonke', 'girls', 21, 'The girls feel at home now. Thank you!', 'girls|pink|21 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7823, 3202, 1020, false, 13, 484, true),
  ('lagos-island-football', 'lagos-island', 'Queens Football Club', 'Football club', '22 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Halima', 'coach', 'players', 22, 'Nobody sits out on Saturday now!', 'girls|pink|22 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7870, 3194, 600, false, 13, 485, true),
  ('lagos-island-training', 'lagos-island', 'Needle and Thread Studio', 'Training school', '10 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Folake', 'teacher', 'apprentices', 10, 'My students can work in comfort now.', 'girls|pink|10 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7912, 3218, 900, false, 13, 486, true),
  ('obalende-school', 'obalende', 'Rainbow College', 'School', '29 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Miss Tolu', 'teacher', 'girls', 29, 'Now my girls can stay in class all month!', 'girls|pink|29 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 8038, 3340, 870, false, 13, 487, true),
  ('obalende-market', 'obalende', 'Waterside Market', 'Market', '17 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Ngozi', 'market', 'girl traders', 17, 'My girls can work without worry now!', 'girls|pink|17 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 8025, 3381, 990, false, 13, 488, true),
  ('obalende-busstop', 'obalende', 'Motor Park Outreach', 'Roadside outreach', '11 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Kemi', 'outreach', 'young women', 11, 'They felt seen today. Thank you.', 'girls|pink|11 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 7990, 3407, 720, false, 13, 489, true),
  ('obalende-clinic', 'obalende', 'Youth Health Corner', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 15, 'We can help every girl who walks in today.', 'girls|pink|About 15 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 7946, 3407, 750, false, 13, 490, true),
  ('obalende-youth', 'obalende', 'Teen Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Ronke', 'outreach', 'girls', 20, 'Now our girls are ready, any day.', 'girls|pink|20 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 7911, 3381, 1020, false, 13, 491, true),
  ('obalende-hostel', 'obalende', 'Kingsway Girls’ Hostel', 'Girls’ hostel', '29 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Rukky', 'mamaRonke', 'girls', 29, 'The girls feel at home now. Thank you!', 'girls|pink|29 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 7898, 3340, 1050, false, 13, 492, true),
  ('obalende-home', 'obalende', 'Hope House', 'Motherless home', 'A home for 13 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ifeoma', 'mama', 'girls', 13, 'Every one of my girls has her own kit now!', 'girls|pink|13 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 7911, 3299, 1020, false, 13, 493, true),
  ('obalende-football', 'obalende', 'Stars United Girls', 'Football club', '17 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 17, 'Nobody sits out on Saturday now!', 'girls|pink|17 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 7946, 3273, 570, false, 13, 494, true),
  ('obalende-training', 'obalende', 'Bright Skills Centre', 'Training school', '12 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 12, 'My students can work in comfort now.', 'girls|pink|12 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 7990, 3273, 960, false, 13, 495, true),
  ('obalende-shelter', 'obalende', 'Flood Relief Shelter', 'Temporary shelter', 'Families moved here after floods. 20 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Nwosu', 'woman', 'women and girls', 20, 'You have given us dignity today.', 'girls|pink|20 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 8025, 3299, 1050, false, 13, 496, true),
  ('ikoyi-school', 'ikoyi', 'Lighthouse Model School', 'School', '42 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Adeyemi', 'teacher', 'girls', 42, 'Now my girls can stay in class all month!', 'girls|pink|42 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 8367, 3296, 780, false, 13, 497, true),
  ('ikoyi-market', 'ikoyi', 'Corner Market', 'Market', '25 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Sade', 'market', 'girl traders', 25, 'My girls can work without worry now!', 'girls|pink|25 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 8354, 3337, 1020, false, 13, 498, true),
  ('ikoyi-busstop', 'ikoyi', 'Roadside Help Point', 'Roadside outreach', '6 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Kemi', 'outreach', 'young women', 6, 'They felt seen today. Thank you.', 'girls|pink|6 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 8319, 3363, 660, false, 13, 499, true),
  ('ikoyi-clinic', 'ikoyi', 'Youth Health Corner', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Grace', 'nurse', 'girls', 22, 'We can help every girl who walks in today.', 'girls|pink|About 22 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 8275, 3363, 660, false, 13, 500, true),
  ('ikoyi-youth', 'ikoyi', 'Study Corner', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Fatima', 'outreach', 'girls', 15, 'Now our girls are ready, any day.', 'girls|pink|15 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 8240, 3337, 1020, false, 13, 501, true),
  ('ikoyi-waterside', 'ikoyi', 'Creekside Settlement', 'Waterside community', '20 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Mrs Ajayi', 'nurse', 'girls', 20, 'These will last them a whole year!', 'girls|pink|20 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 8227, 3296, 750, false, 13, 502, true),
  ('ikoyi-hostel', 'ikoyi', 'Harmony Hostel', 'Girls’ hostel', '28 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 28, 'The girls feel at home now. Thank you!', 'girls|pink|28 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 8240, 3255, 1050, false, 13, 503, true),
  ('ikoyi-football', 'ikoyi', 'Stars United Girls', 'Football club', '16 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 16, 'Nobody sits out on Saturday now!', 'girls|pink|16 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 8275, 3229, 570, false, 13, 504, true),
  ('ikoyi-training', 'ikoyi', 'Adeola Tailoring School', 'Training school', '10 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Bukky', 'teacher', 'apprentices', 10, 'My students can work in comfort now.', 'girls|pink|10 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 8319, 3229, 990, false, 13, 505, true),
  ('ikoyi-shelter', 'ikoyi', 'Hope Camp', 'Temporary shelter', 'Families moved here after floods. 28 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Hauwa', 'woman', 'women and girls', 28, 'You have given us dignity today.', 'girls|pink|28 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 8354, 3255, 1020, false, 13, 506, true),
  ('victoria-island-school', 'victoria-island', 'Morning Star Academy', 'School', '25 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Miss Ada', 'teacher', 'girls', 25, 'Now my girls can stay in class all month!', 'girls|pink|25 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 8214, 3561, 780, false, 13, 507, true),
  ('victoria-island-market', 'victoria-island', 'Corner Market', 'Market', '24 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Bose', 'market', 'girl traders', 24, 'My girls can work without worry now!', 'girls|pink|24 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 8198, 3606, 960, false, 13, 508, true),
  ('victoria-island-busstop', 'victoria-island', 'Roadside Help Point', 'Roadside outreach', '6 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Bisi', 'outreach', 'young women', 6, 'They felt seen today. Thank you.', 'girls|pink|6 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 8156, 3630, 600, false, 13, 509, true),
  ('victoria-island-clinic', 'victoria-island', 'Community Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Kemi', 'nurse', 'girls', 20, 'We can help every girl who walks in today.', 'girls|pink|About 20 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 8109, 3622, 750, false, 13, 510, true),
  ('victoria-island-youth', 'victoria-island', 'Teen Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Fatima', 'outreach', 'girls', 15, 'Now our girls are ready, any day.', 'girls|pink|15 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 8078, 3585, 1020, false, 13, 511, true),
  ('victoria-island-waterside', 'victoria-island', 'Canoe Landing Community', 'Waterside community', '29 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Nurse Grace', 'nurse', 'girls', 29, 'These will last them a whole year!', 'girls|pink|29 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 8078, 3537, 840, false, 13, 512, true),
  ('victoria-island-community', 'victoria-island', 'Family Help Centre', 'Community centre', 'Women and girls meet here weekly. Many share one tap and buy pads only when they can.', 'If a pack could last, it would change things for our families.', 'Mrs Adebayo', 'woman', 'women and girls', 25, 'This will last our families a long time.', 'girls|pink|25 women and girls
cake|peach|Ages 12 to 45
noDrop|green|No running water
coin|pink|Money is tight', 8109, 3500, 990, false, 13, 513, true),
  ('victoria-island-football', 'victoria-island', 'Stars United Girls', 'Football club', '15 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Halima', 'coach', 'players', 15, 'Nobody sits out on Saturday now!', 'girls|pink|15 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 8156, 3492, 660, false, 13, 514, true),
  ('victoria-island-shelter', 'victoria-island', 'Hope Camp', 'Temporary shelter', 'Families moved here after floods. 26 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Bello', 'woman', 'women and girls', 26, 'You have given us dignity today.', 'girls|pink|26 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 8198, 3516, 1080, false, 13, 515, true),
  ('oniru-school', 'oniru', 'Rainbow College', 'School', '20 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Miss Tolu', 'teacher', 'girls', 20, 'Now my girls can stay in class all month!', 'girls|pink|20 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 8477, 3506, 780, false, 13, 516, true),
  ('oniru-market', 'oniru', 'Evening Market', 'Market', '14 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Sade', 'market', 'girl traders', 14, 'My girls can work without worry now!', 'girls|pink|14 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 8461, 3551, 990, false, 13, 517, true),
  ('oniru-busstop', 'oniru', 'Bus Stop Outreach', 'Roadside outreach', '13 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Joy', 'outreach', 'young women', 13, 'They felt seen today. Thank you.', 'girls|pink|13 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 8419, 3575, 630, false, 13, 518, true),
  ('oniru-clinic', 'oniru', 'Teen Care Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Amina', 'nurse', 'girls', 15, 'We can help every girl who walks in today.', 'girls|pink|About 15 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 8372, 3567, 660, false, 13, 519, true),
  ('oniru-youth', 'oniru', 'Youth Hub', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 21, 'Now our girls are ready, any day.', 'girls|pink|21 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 8341, 3530, 1020, false, 13, 520, true),
  ('oniru-waterside', 'oniru', 'Lagoonside Community', 'Waterside community', '18 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Mrs Ajayi', 'nurse', 'girls', 18, 'These will last them a whole year!', 'girls|pink|18 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 8341, 3482, 750, false, 13, 521, true),
  ('oniru-community', 'oniru', 'Sisters’ Corner', 'Community centre', 'Women and girls meet here weekly. Many share one tap and buy pads only when they can.', 'If a pack could last, it would change things for our families.', 'Mrs Adebayo', 'woman', 'women and girls', 18, 'This will last our families a long time.', 'girls|pink|18 women and girls
cake|peach|Ages 12 to 45
drop|green|Running water on site
coin|pink|Money is tight', 8372, 3445, 930, false, 13, 522, true),
  ('oniru-football', 'oniru', 'Queens Football Club', 'Football club', '21 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Halima', 'coach', 'players', 21, 'Nobody sits out on Saturday now!', 'girls|pink|21 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 8419, 3437, 570, false, 13, 523, true),
  ('oniru-shelter', 'oniru', 'Hope Camp', 'Temporary shelter', 'Families moved here after floods. 27 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Hauwa', 'woman', 'women and girls', 27, 'You have given us dignity today.', 'girls|pink|27 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 8461, 3461, 1020, false, 13, 524, true),
  ('lekki-phase-1-school', 'lekki-phase-1', 'Unity Girls’ College', 'School', '26 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Miss Tolu', 'teacher', 'girls', 26, 'Now my girls can stay in class all month!', 'girls|pink|26 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 8807, 3373, 810, false, 13, 525, true),
  ('lekki-phase-1-market', 'lekki-phase-1', 'Waterside Market', 'Market', '23 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'market', 'girl traders', 23, 'My girls can work without worry now!', 'girls|pink|23 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 8791, 3418, 1020, false, 13, 526, true),
  ('lekki-phase-1-busstop', 'lekki-phase-1', 'Bus Stop Outreach', 'Roadside outreach', '8 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Sister Amaka', 'outreach', 'young women', 8, 'They felt seen today. Thank you.', 'girls|pink|8 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 8749, 3442, 600, false, 13, 527, true),
  ('lekki-phase-1-clinic', 'lekki-phase-1', 'Family Health Centre', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 15, 'We can help every girl who walks in today.', 'girls|pink|About 15 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 8702, 3434, 720, false, 13, 528, true),
  ('lekki-phase-1-youth', 'lekki-phase-1', 'After-School Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Fatima', 'outreach', 'girls', 30, 'Now our girls are ready, any day.', 'girls|pink|30 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 8671, 3397, 1080, false, 13, 529, true),
  ('lekki-phase-1-hostel', 'lekki-phase-1', 'Kingsway Girls’ Hostel', 'Girls’ hostel', '23 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Uche', 'mamaRonke', 'girls', 23, 'The girls feel at home now. Thank you!', 'girls|pink|23 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 8671, 3349, 1050, false, 13, 530, true),
  ('lekki-phase-1-home', 'lekki-phase-1', 'Open Arms Home', 'Motherless home', 'A home for 15 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ronke', 'mama', 'girls', 15, 'Every one of my girls has her own kit now!', 'girls|pink|15 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 8702, 3312, 1050, false, 13, 531, true),
  ('lekki-phase-1-football', 'lekki-phase-1', 'Lagoon Strikers', 'Football club', '15 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ify', 'coach', 'players', 15, 'Nobody sits out on Saturday now!', 'girls|pink|15 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 8749, 3304, 570, false, 13, 532, true),
  ('lekki-phase-1-training', 'lekki-phase-1', 'Crafts Workshop', 'Training school', '14 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 14, 'My students can work in comfort now.', 'girls|pink|14 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 8791, 3328, 990, false, 13, 533, true),
  ('ikate-school', 'ikate', 'Royal Pearl College', 'School', '38 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Miss Zainab', 'teacher', 'girls', 38, 'Now my girls can stay in class all month!', 'girls|pink|38 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 8994, 3462, 780, false, 13, 534, true),
  ('ikate-market', 'ikate', 'Fresh Foods Market', 'Market', '10 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Sade', 'market', 'girl traders', 10, 'My girls can work without worry now!', 'girls|pink|10 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 8978, 3507, 1050, false, 13, 535, true),
  ('ikate-busstop', 'ikate', 'Motor Park Outreach', 'Roadside outreach', '6 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Kemi', 'outreach', 'young women', 6, 'They felt seen today. Thank you.', 'girls|pink|6 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 8936, 3531, 720, false, 13, 536, true),
  ('ikate-clinic', 'ikate', 'Teen Care Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Chika', 'nurse', 'girls', 18, 'We can help every girl who walks in today.', 'girls|pink|About 18 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 8889, 3523, 750, false, 13, 537, true),
  ('ikate-youth', 'ikate', 'Study Corner', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Ronke', 'outreach', 'girls', 22, 'Now our girls are ready, any day.', 'girls|pink|22 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 8858, 3486, 1050, false, 13, 538, true),
  ('ikate-waterside', 'ikate', 'Fishing Village', 'Waterside community', '19 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Aunty Toyin', 'nurse', 'girls', 19, 'These will last them a whole year!', 'girls|pink|19 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 8858, 3438, 870, false, 13, 539, true),
  ('ikate-community', 'ikate', 'Sisters’ Corner', 'Community centre', 'Women and girls meet here weekly. Many share one tap and buy pads only when they can.', 'If a pack could last, it would change things for our families.', 'Mrs Obi', 'woman', 'women and girls', 29, 'This will last our families a long time.', 'girls|pink|29 women and girls
cake|peach|Ages 12 to 45
noDrop|green|No running water
coin|pink|Money is tight', 8889, 3401, 960, false, 13, 540, true),
  ('ikate-football', 'ikate', 'Young Lionesses FC', 'Football club', '14 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 14, 'Nobody sits out on Saturday now!', 'girls|pink|14 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 8936, 3393, 630, false, 13, 541, true),
  ('ikate-shelter', 'ikate', 'Rest Point Shelter', 'Temporary shelter', 'Families moved here after floods. 22 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Hauwa', 'woman', 'women and girls', 22, 'You have given us dignity today.', 'girls|pink|22 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 8978, 3417, 1080, false, 13, 542, true),
  ('chevron-school', 'chevron', 'Morning Star Academy', 'School', '41 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Adeyemi', 'teacher', 'girls', 41, 'Now my girls can stay in class all month!', 'girls|pink|41 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 9302, 3406, 780, false, 13, 543, true),
  ('chevron-market', 'chevron', 'Textile Lane', 'Market', '18 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'market', 'girl traders', 18, 'My girls can work without worry now!', 'girls|pink|18 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 9289, 3447, 1080, false, 13, 544, true),
  ('chevron-busstop', 'chevron', 'Junction Outreach', 'Roadside outreach', '7 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Bisi', 'outreach', 'young women', 7, 'They felt seen today. Thank you.', 'girls|pink|7 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 9254, 3473, 660, false, 13, 545, true),
  ('chevron-clinic', 'chevron', 'Teen Care Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Grace', 'nurse', 'girls', 24, 'We can help every girl who walks in today.', 'girls|pink|About 24 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 9210, 3473, 660, false, 13, 546, true),
  ('chevron-youth', 'chevron', 'Library Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Ronke', 'outreach', 'girls', 24, 'Now our girls are ready, any day.', 'girls|pink|24 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 9175, 3447, 1050, false, 13, 547, true),
  ('chevron-hostel', 'chevron', 'Harmony Hostel', 'Girls’ hostel', '26 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Funmi', 'mamaRonke', 'girls', 26, 'The girls feel at home now. Thank you!', 'girls|pink|26 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 9162, 3406, 1080, false, 13, 548, true),
  ('chevron-home', 'chevron', 'Open Arms Home', 'Motherless home', 'A home for 15 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ifeoma', 'mama', 'girls', 15, 'Every one of my girls has her own kit now!', 'girls|pink|15 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 9175, 3365, 990, false, 13, 549, true),
  ('chevron-football', 'chevron', 'Queens Football Club', 'Football club', '17 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 17, 'Nobody sits out on Saturday now!', 'girls|pink|17 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 9210, 3339, 600, false, 13, 550, true),
  ('chevron-training', 'chevron', 'Bright Skills Centre', 'Training school', '10 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Folake', 'teacher', 'apprentices', 10, 'My students can work in comfort now.', 'girls|pink|10 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 9254, 3339, 960, false, 13, 551, true),
  ('chevron-shelter', 'chevron', 'Hope Camp', 'Temporary shelter', 'Families moved here after floods. 20 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Bello', 'woman', 'women and girls', 20, 'You have given us dignity today.', 'girls|pink|20 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 9289, 3365, 1050, false, 13, 552, true),
  ('osapa-school', 'osapa', 'Golden Gate School', 'School', '37 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Eze', 'teacher', 'girls', 37, 'Now my girls can stay in class all month!', 'girls|pink|37 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 9488, 3340, 780, false, 13, 553, true),
  ('osapa-market', 'osapa', 'Fresh Foods Market', 'Market', '15 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Bose', 'market', 'girl traders', 15, 'My girls can work without worry now!', 'girls|pink|15 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 9472, 3385, 1050, false, 13, 554, true),
  ('osapa-busstop', 'osapa', 'Motor Park Outreach', 'Roadside outreach', '7 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Sister Amaka', 'outreach', 'young women', 7, 'They felt seen today. Thank you.', 'girls|pink|7 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 9430, 3409, 660, false, 13, 555, true),
  ('osapa-clinic', 'osapa', 'Teen Care Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Amina', 'nurse', 'girls', 20, 'We can help every girl who walks in today.', 'girls|pink|About 20 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 9383, 3401, 690, false, 13, 556, true),
  ('osapa-youth', 'osapa', 'Teen Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 15, 'Now our girls are ready, any day.', 'girls|pink|15 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 9352, 3364, 1020, false, 13, 557, true),
  ('osapa-waterside', 'osapa', 'Fishing Village', 'Waterside community', '24 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Nurse Grace', 'nurse', 'girls', 24, 'These will last them a whole year!', 'girls|pink|24 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 9352, 3316, 750, false, 13, 558, true),
  ('osapa-hostel', 'osapa', 'Kingsway Girls’ Hostel', 'Girls’ hostel', '27 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Funmi', 'mamaRonke', 'girls', 27, 'The girls feel at home now. Thank you!', 'girls|pink|27 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 9383, 3279, 1080, false, 13, 559, true),
  ('osapa-football', 'osapa', 'Girls FC', 'Football club', '19 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 19, 'Nobody sits out on Saturday now!', 'girls|pink|19 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 9430, 3271, 630, false, 13, 560, true),
  ('osapa-training', 'osapa', 'Adeola Tailoring School', 'Training school', '18 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Bukky', 'teacher', 'apprentices', 18, 'My students can work in comfort now.', 'girls|pink|18 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 9472, 3295, 960, false, 13, 561, true),
  ('ajah-school', 'ajah', 'Rainbow College', 'School', '43 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Miss Zainab', 'teacher', 'girls', 43, 'Now my girls can stay in class all month!', 'girls|pink|43 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 9851, 3119, 840, false, 13, 562, true),
  ('ajah-market', 'ajah', 'Fruit Line Market', 'Market', '15 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'market', 'girl traders', 15, 'My girls can work without worry now!', 'girls|pink|15 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 9838, 3160, 960, false, 13, 563, true),
  ('ajah-busstop', 'ajah', 'Roadside Help Point', 'Roadside outreach', '11 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Rita', 'outreach', 'young women', 11, 'They felt seen today. Thank you.', 'girls|pink|11 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 9803, 3186, 600, false, 13, 564, true),
  ('ajah-clinic', 'ajah', 'Family Health Centre', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Amina', 'nurse', 'girls', 12, 'We can help every girl who walks in today.', 'girls|pink|About 12 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 9759, 3186, 660, false, 13, 565, true),
  ('ajah-youth', 'ajah', 'After-School Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Bimpe', 'outreach', 'girls', 28, 'Now our girls are ready, any day.', 'girls|pink|28 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 9724, 3160, 1020, false, 13, 566, true),
  ('ajah-waterside', 'ajah', 'Creekside Settlement', 'Waterside community', '21 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Aunty Toyin', 'nurse', 'girls', 21, 'These will last them a whole year!', 'girls|pink|21 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 9711, 3119, 810, false, 13, 567, true),
  ('ajah-hostel', 'ajah', 'Bluebell Hostel', 'Girls’ hostel', '23 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Rukky', 'mamaRonke', 'girls', 23, 'The girls feel at home now. Thank you!', 'girls|pink|23 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 9724, 3078, 1020, false, 13, 568, true),
  ('ajah-football', 'ajah', 'Girls FC', 'Football club', '18 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Halima', 'coach', 'players', 18, 'Nobody sits out on Saturday now!', 'girls|pink|18 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 9759, 3052, 660, false, 13, 569, true),
  ('ajah-training', 'ajah', 'Crafts Workshop', 'Training school', '18 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 18, 'My students can work in comfort now.', 'girls|pink|18 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 9803, 3052, 930, false, 13, 570, true),
  ('ajah-shelter', 'ajah', 'Flood Relief Shelter', 'Temporary shelter', 'Families moved here after floods. 23 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Bello', 'woman', 'women and girls', 23, 'You have given us dignity today.', 'girls|pink|23 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 9838, 3078, 1020, false, 13, 571, true),
  ('sangotedo-school', 'sangotedo', 'Bright Stars School', 'School', '27 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Eze', 'teacher', 'girls', 27, 'Now my girls can stay in class all month!', 'girls|pink|27 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 10510, 3097, 810, false, 13, 572, true),
  ('sangotedo-market', 'sangotedo', 'Waterside Market', 'Market', '13 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Nkechi', 'market', 'girl traders', 13, 'My girls can work without worry now!', 'girls|pink|13 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 10494, 3142, 1020, false, 13, 573, true),
  ('sangotedo-busstop', 'sangotedo', 'Motor Park Outreach', 'Roadside outreach', '11 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Kemi', 'outreach', 'young women', 11, 'They felt seen today. Thank you.', 'girls|pink|11 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 10452, 3166, 600, false, 13, 574, true),
  ('sangotedo-clinic', 'sangotedo', 'Girls’ Health Point', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Chika', 'nurse', 'girls', 17, 'We can help every girl who walks in today.', 'girls|pink|About 17 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 10405, 3158, 690, false, 13, 575, true),
  ('sangotedo-youth', 'sangotedo', 'Youth Hub', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Fatima', 'outreach', 'girls', 26, 'Now our girls are ready, any day.', 'girls|pink|26 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 10374, 3121, 1020, false, 13, 576, true),
  ('sangotedo-hostel', 'sangotedo', 'Kingsway Girls’ Hostel', 'Girls’ hostel', '19 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Yemi', 'mamaRonke', 'girls', 19, 'The girls feel at home now. Thank you!', 'girls|pink|19 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 10374, 3073, 1080, false, 13, 577, true),
  ('sangotedo-home', 'sangotedo', 'Haven Home', 'Motherless home', 'A home for 15 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Asabe', 'mama', 'girls', 15, 'Every one of my girls has her own kit now!', 'girls|pink|15 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 10405, 3036, 1050, false, 13, 578, true),
  ('sangotedo-football', 'sangotedo', 'Girls FC', 'Football club', '20 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Halima', 'coach', 'players', 20, 'Nobody sits out on Saturday now!', 'girls|pink|20 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 10452, 3028, 570, false, 13, 579, true),
  ('sangotedo-training', 'sangotedo', 'Style Hair Academy', 'Training school', '14 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Ijeoma', 'teacher', 'apprentices', 14, 'My students can work in comfort now.', 'girls|pink|14 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 10494, 3052, 930, false, 13, 580, true),
  ('abijo-school', 'abijo', 'Rainbow College', 'School', '33 girls in JSS. Some stay home on period days because they have nothing to use.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Okafor', 'teacher', 'girls', 33, 'Now my girls can stay in class all month!', 'girls|pink|33 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 10895, 3119, 810, false, 13, 581, true),
  ('abijo-market', 'abijo', 'Fruit Line Market', 'Market', '25 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Bose', 'market', 'girl traders', 25, 'My girls can work without worry now!', 'girls|pink|25 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 10882, 3160, 1020, false, 13, 582, true),
  ('abijo-busstop', 'abijo', 'Under-Bridge Outreach', 'Roadside outreach', '7 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Joy', 'outreach', 'young women', 7, 'They felt seen today. Thank you.', 'girls|pink|7 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 10847, 3186, 630, false, 13, 583, true),
  ('abijo-clinic', 'abijo', 'Teen Care Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 17, 'We can help every girl who walks in today.', 'girls|pink|About 17 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 10803, 3186, 660, false, 13, 584, true),
  ('abijo-youth', 'abijo', 'Teen Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Ronke', 'outreach', 'girls', 21, 'Now our girls are ready, any day.', 'girls|pink|21 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 10768, 3160, 1050, false, 13, 585, true),
  ('abijo-hostel', 'abijo', 'Kingsway Girls’ Hostel', 'Girls’ hostel', '20 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Funmi', 'mamaRonke', 'girls', 20, 'The girls feel at home now. Thank you!', 'girls|pink|20 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 10755, 3119, 1020, false, 13, 586, true),
  ('abijo-home', 'abijo', 'Little Hearts Home', 'Motherless home', 'A home for 10 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Titi', 'mama', 'girls', 10, 'Every one of my girls has her own kit now!', 'girls|pink|10 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 10768, 3078, 1020, false, 13, 587, true),
  ('abijo-football', 'abijo', 'Lagoon Strikers', 'Football club', '15 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 15, 'Nobody sits out on Saturday now!', 'girls|pink|15 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 10803, 3052, 570, false, 13, 588, true),
  ('abijo-training', 'abijo', 'Needle and Thread Studio', 'Training school', '16 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Adeola', 'teacher', 'apprentices', 16, 'My students can work in comfort now.', 'girls|pink|16 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 10847, 3052, 930, false, 13, 589, true),
  ('abijo-shelter', 'abijo', 'Safe Stay Shelter', 'Temporary shelter', 'Families moved here after floods. 21 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Aunty Grace', 'woman', 'women and girls', 21, 'You have given us dignity today.', 'girls|pink|21 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 10882, 3078, 1080, false, 13, 590, true),
  ('lakowe-school', 'lakowe', 'Sunrise Grammar School', 'School', '23 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Miss Ada', 'teacher', 'girls', 23, 'Now my girls can stay in class all month!', 'girls|pink|23 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 11280, 3119, 780, false, 13, 591, true),
  ('lakowe-market', 'lakowe', 'Waterside Market', 'Market', '13 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Ngozi', 'market', 'girl traders', 13, 'My girls can work without worry now!', 'girls|pink|13 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 11267, 3160, 960, false, 13, 592, true),
  ('lakowe-busstop', 'lakowe', 'Junction Outreach', 'Roadside outreach', '8 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Kemi', 'outreach', 'young women', 8, 'They felt seen today. Thank you.', 'girls|pink|8 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 11232, 3186, 600, false, 13, 593, true),
  ('lakowe-clinic', 'lakowe', 'Girls’ Health Point', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Kemi', 'nurse', 'girls', 20, 'We can help every girl who walks in today.', 'girls|pink|About 20 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 11188, 3186, 720, false, 13, 594, true),
  ('lakowe-youth', 'lakowe', 'Study Corner', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Ronke', 'outreach', 'girls', 21, 'Now our girls are ready, any day.', 'girls|pink|21 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 11153, 3160, 1020, false, 13, 595, true),
  ('lakowe-hostel', 'lakowe', 'Kingsway Girls’ Hostel', 'Girls’ hostel', '22 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Funmi', 'mamaRonke', 'girls', 22, 'The girls feel at home now. Thank you!', 'girls|pink|22 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 11140, 3119, 1020, false, 13, 596, true),
  ('lakowe-home', 'lakowe', 'Open Arms Home', 'Motherless home', 'A home for 18 girls. Some have never had pads of their own.', 'Our girls share everything. Something of their own would mean so much.', 'Mama Ifeoma', 'mama', 'girls', 18, 'Every one of my girls has her own kit now!', 'girls|pink|18 girls live here
cake|peach|Ages 10 to 17; some not started
drop|green|Clean water and a laundry line
door|pink|One shared bathroom', 11153, 3078, 1020, false, 13, 597, true),
  ('lakowe-football', 'lakowe', 'Girls FC', 'Football club', '14 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 14, 'Nobody sits out on Saturday now!', 'girls|pink|14 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 11188, 3052, 630, false, 13, 598, true),
  ('lakowe-training', 'lakowe', 'Needle and Thread Studio', 'Training school', '18 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Folake', 'teacher', 'apprentices', 18, 'My students can work in comfort now.', 'girls|pink|18 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 11232, 3052, 990, false, 13, 599, true),
  ('lakowe-shelter', 'lakowe', 'Safe Stay Shelter', 'Temporary shelter', 'Families moved here after floods. 25 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Nwosu', 'woman', 'women and girls', 25, 'You have given us dignity today.', 'girls|pink|25 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 11267, 3078, 1050, false, 13, 600, true),
  ('eleko-school', 'eleko', 'Bright Stars School', 'School', '45 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Okafor', 'teacher', 'girls', 45, 'Now my girls can stay in class all month!', 'girls|pink|45 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 12489, 3351, 840, false, 13, 601, true),
  ('eleko-market', 'eleko', 'Textile Lane', 'Market', '22 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Iya Tunde', 'market', 'girl traders', 22, 'My girls can work without worry now!', 'girls|pink|22 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 12473, 3396, 960, false, 13, 602, true),
  ('eleko-busstop', 'eleko', 'Junction Outreach', 'Roadside outreach', '6 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Sister Amaka', 'outreach', 'young women', 6, 'They felt seen today. Thank you.', 'girls|pink|6 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 12431, 3420, 690, false, 13, 603, true),
  ('eleko-clinic', 'eleko', 'Girls’ Health Point', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Lola', 'nurse', 'girls', 20, 'We can help every girl who walks in today.', 'girls|pink|About 20 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 12384, 3412, 660, false, 13, 604, true),
  ('eleko-youth', 'eleko', 'Library Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 30, 'Now our girls are ready, any day.', 'girls|pink|30 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 12353, 3375, 1020, false, 13, 605, true),
  ('eleko-waterside', 'eleko', 'Canoe Landing Community', 'Waterside community', '27 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Aunty Toyin', 'nurse', 'girls', 27, 'These will last them a whole year!', 'girls|pink|27 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 12353, 3327, 840, false, 13, 606, true),
  ('eleko-community', 'eleko', 'Family Help Centre', 'Community centre', 'Women and girls meet here weekly. Many share one tap and buy pads only when they can.', 'If a pack could last, it would change things for our families.', 'Mrs Obi', 'woman', 'women and girls', 25, 'This will last our families a long time.', 'girls|pink|25 women and girls
cake|peach|Ages 12 to 45
drop|green|Running water on site
coin|pink|Money is tight', 12384, 3290, 960, false, 13, 607, true),
  ('eleko-football', 'eleko', 'Young Lionesses FC', 'Football club', '21 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 21, 'Nobody sits out on Saturday now!', 'girls|pink|21 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 12431, 3282, 600, false, 13, 608, true),
  ('eleko-shelter', 'eleko', 'Safe Stay Shelter', 'Temporary shelter', 'Families moved here after floods. 19 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Aunty Grace', 'woman', 'women and girls', 19, 'You have given us dignity today.', 'girls|pink|19 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 12473, 3306, 1050, false, 13, 609, true),
  ('ibeju-school', 'ibeju', 'Rainbow College', 'School', '25 girls in JSS. Some stay home on period days because they fear stains in class.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Okafor', 'teacher', 'girls', 25, 'Now my girls can stay in class all month!', 'girls|pink|25 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
drop|green|Running water on site
run|pink|PE twice a week', 13808, 3207, 780, false, 13, 610, true),
  ('ibeju-market', 'ibeju', 'Fruit Line Market', 'Market', '20 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Ngozi', 'market', 'girl traders', 20, 'My girls can work without worry now!', 'girls|pink|20 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 13795, 3248, 960, false, 13, 611, true),
  ('ibeju-busstop', 'ibeju', 'Under-Bridge Outreach', 'Roadside outreach', '12 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Bisi', 'outreach', 'young women', 12, 'They felt seen today. Thank you.', 'girls|pink|12 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 13760, 3274, 660, false, 13, 612, true),
  ('ibeju-clinic', 'ibeju', 'Girls’ Health Point', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Kemi', 'nurse', 'girls', 17, 'We can help every girl who walks in today.', 'girls|pink|About 17 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 13716, 3274, 720, false, 13, 613, true),
  ('ibeju-youth', 'ibeju', 'After-School Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Miss Ebere', 'outreach', 'girls', 27, 'Now our girls are ready, any day.', 'girls|pink|27 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 13681, 3248, 1080, false, 13, 614, true),
  ('ibeju-waterside', 'ibeju', 'Lagoonside Community', 'Waterside community', '30 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Mama Efe', 'nurse', 'girls', 30, 'These will last them a whole year!', 'girls|pink|30 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 13668, 3207, 780, false, 13, 615, true),
  ('ibeju-community', 'ibeju', 'Family Help Centre', 'Community centre', 'Women and girls meet here weekly. Many share one tap and buy pads only when they can.', 'If a pack could last, it would change things for our families.', 'Mrs Obi', 'woman', 'women and girls', 30, 'This will last our families a long time.', 'girls|pink|30 women and girls
cake|peach|Ages 12 to 45
drop|green|Running water on site
coin|pink|Money is tight', 13681, 3166, 990, false, 13, 616, true),
  ('ibeju-football', 'ibeju', 'Young Lionesses FC', 'Football club', '18 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Bola', 'coach', 'players', 18, 'Nobody sits out on Saturday now!', 'girls|pink|18 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 13716, 3140, 570, false, 13, 617, true),
  ('ibeju-shelter', 'ibeju', 'Safe Stay Shelter', 'Temporary shelter', 'Families moved here after floods. 33 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Bello', 'woman', 'women and girls', 33, 'You have given us dignity today.', 'girls|pink|33 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 13760, 3140, 1080, false, 13, 618, true),
  ('ibeju-community-2', 'ibeju', 'Women’s Circle', 'Community centre', 'Women and girls meet here weekly. Many share one tap and buy pads only when they can.', 'If a pack could last, it would change things for our families.', 'Mrs Obi', 'woman', 'women and girls', 23, 'This will last our families a long time.', 'girls|pink|23 women and girls
cake|peach|Ages 12 to 45
noDrop|green|No running water
coin|pink|Money is tight', 13795, 3166, 990, false, 13, 619, true),
  ('epe-school', 'epe', 'Golden Gate School', 'School', '40 girls in JSS. Some stay home on period days because the toilets have no bins.', 'Some girls miss days every month. Help them stay in class!', 'Mrs Okafor', 'teacher', 'girls', 40, 'Now my girls can stay in class all month!', 'girls|pink|40 girls in JSS 1 to 3
cake|peach|Ages 11 to 15
noDrop|green|No running water
run|pink|PE twice a week', 14357, 1825, 870, false, 13, 620, true),
  ('epe-market', 'epe', 'Textile Lane', 'Market', '19 girls sell all day. Public toilets cost money and are far away.', 'Our girls stand from morning till night. Periods make it so hard.', 'Mama Ngozi', 'market', 'girl traders', 19, 'My girls can work without worry now!', 'girls|pink|19 girl traders
cake|peach|Ages 14 to 19
coin|green|Toilets cost ₦50 a visit
shop|pink|On their feet all day', 14344, 1866, 1050, false, 13, 621, true),
  ('epe-busstop', 'epe', 'Under-Bridge Outreach', 'Roadside outreach', '11 young women live and work by the road. Water and privacy are hard to find.', 'They use rags or paper. Something they can carry and change anywhere would help.', 'Aunty Rita', 'outreach', 'young women', 11, 'They felt seen today. Thank you.', 'girls|pink|11 young women
cake|peach|Ages 16 to 24
noDrop|green|No steady water
moon|pink|Always on the move', 14309, 1892, 720, false, 13, 622, true),
  ('epe-clinic', 'epe', 'Community Clinic', 'Youth clinic', 'Girls come with heavy, painful periods. The clinic runs out of supplies by noon.', 'We see girls who miss school with pain. Help us give them something today.', 'Nurse Grace', 'nurse', 'girls', 24, 'We can help every girl who walks in today.', 'girls|pink|About 24 girls a morning
pain|peach|Many with painful periods
drop|green|Clean water on site
light|pink|Morning clinic only', 14265, 1892, 750, false, 13, 623, true),
  ('epe-youth', 'epe', 'After-School Club', 'Youth club', 'Girls come after school to study and play. Many go home late.', 'They stay here till dark. A period can catch them by surprise.', 'Aunty Ronke', 'outreach', 'girls', 19, 'Now our girls are ready, any day.', 'girls|pink|19 girls each afternoon
cake|peach|Ages 11 to 18
run|green|Sport and study
moon|pink|Open till evening', 14230, 1866, 1050, false, 13, 624, true),
  ('epe-waterside', 'epe', 'Canoe Landing Community', 'Waterside community', '17 girls live by the water. The nearest shop is a canoe ride away.', 'When pads run out, the girls use old cloth. They need something that lasts.', 'Mrs Ajayi', 'nurse', 'girls', 17, 'These will last them a whole year!', 'girls|pink|17 girls
cake|peach|Ages 10 to 16
river|green|Boiled lagoon water
road|pink|Shop is a canoe ride away', 14217, 1825, 840, false, 13, 625, true),
  ('epe-hostel', 'epe', 'Kingsway Girls’ Hostel', 'Girls’ hostel', '24 girls from other states board here for school. Shops are far from the hostel.', 'Our girls are far from home. Some feel shy to ask for pads.', 'Matron Funmi', 'mamaRonke', 'girls', 24, 'The girls feel at home now. Thank you!', 'girls|pink|24 girls board here
cake|peach|Ages 12 to 17
drop|green|Running water, shared laundry
door|pink|Lights out at 9pm', 14230, 1784, 1080, false, 13, 626, true),
  ('epe-football', 'epe', 'Lagoon Strikers', 'Football club', '21 players train every morning. The big match is on Saturday.', 'Two of my best players nearly skipped the final last year. Help them play!', 'Coach Ebun', 'coach', 'players', 21, 'Nobody sits out on Saturday now!', 'girls|pink|21 players
cake|peach|Ages 12 to 17
ball|green|Train daily; match Saturday
drop|pink|Changing room with showers', 14265, 1758, 660, false, 13, 627, true),
  ('epe-training', 'epe', 'Bright Skills Centre', 'Training school', '12 young women learn a trade all day. Breaks are short.', 'They sit at the machines from morning. They need protection that lasts.', 'Madam Folake', 'teacher', 'apprentices', 12, 'My students can work in comfort now.', 'girls|pink|12 apprentices
cake|peach|Ages 16 to 22
shop|green|Long hours, short breaks
drop|pink|Small shared toilet', 14309, 1758, 930, false, 13, 628, true),
  ('epe-shelter', 'epe', 'Safe Stay Shelter', 'Temporary shelter', 'Families moved here after floods. 31 women and girls share tents and a few water points.', 'The latrines have no light. Our girls are afraid to go at night.', 'Mrs Nwosu', 'woman', 'women and girls', 31, 'You have given us dignity today.', 'girls|pink|31 women and girls
tent|peach|Crowded shared tents
drop|green|Shared water points
light|pink|No light in latrines', 14344, 1784, 1080, false, 13, 629, true);

INSERT INTO giveday_needs (id, place_id, product_id, rating, why) VALUES
  ('badagry-school:pads', 'badagry-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('badagry-school:liners', 'badagry-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('badagry-school:tampons', 'badagry-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('badagry-school:cup', 'badagry-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('badagry-school:pants', 'badagry-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('badagry-school:reusable', 'badagry-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('badagry-school:heat', 'badagry-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('badagry-school:wipes', 'badagry-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('badagry-school:washbag', 'badagry-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('badagry-school:guide', 'badagry-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('badagry-market:pads', 'badagry-market', 'pads', 'good', 'Quick to change between customers.'),
  ('badagry-market:liners', 'badagry-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('badagry-market:tampons', 'badagry-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('badagry-market:cup', 'badagry-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('badagry-market:pants', 'badagry-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('badagry-market:reusable', 'badagry-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('badagry-market:heat', 'badagry-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('badagry-market:wipes', 'badagry-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('badagry-market:washbag', 'badagry-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('badagry-market:guide', 'badagry-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('badagry-busstop:pads', 'badagry-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('badagry-busstop:liners', 'badagry-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('badagry-busstop:tampons', 'badagry-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('badagry-busstop:cup', 'badagry-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('badagry-busstop:pants', 'badagry-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('badagry-busstop:reusable', 'badagry-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('badagry-busstop:heat', 'badagry-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('badagry-busstop:wipes', 'badagry-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('badagry-busstop:washbag', 'badagry-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('badagry-busstop:guide', 'badagry-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('badagry-clinic:pads', 'badagry-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('badagry-clinic:liners', 'badagry-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('badagry-clinic:tampons', 'badagry-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('badagry-clinic:cup', 'badagry-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('badagry-clinic:pants', 'badagry-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('badagry-clinic:reusable', 'badagry-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('badagry-clinic:heat', 'badagry-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('badagry-clinic:wipes', 'badagry-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('badagry-clinic:washbag', 'badagry-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('badagry-clinic:guide', 'badagry-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('badagry-youth:pads', 'badagry-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('badagry-youth:liners', 'badagry-youth', 'liners', 'good', 'For light days at the club.'),
  ('badagry-youth:tampons', 'badagry-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('badagry-youth:cup', 'badagry-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('badagry-youth:pants', 'badagry-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('badagry-youth:reusable', 'badagry-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('badagry-youth:heat', 'badagry-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('badagry-youth:wipes', 'badagry-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('badagry-youth:washbag', 'badagry-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('badagry-youth:guide', 'badagry-youth', 'guide', 'good', 'Answers questions they have.'),
  ('badagry-waterside:pads', 'badagry-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('badagry-waterside:liners', 'badagry-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('badagry-waterside:tampons', 'badagry-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('badagry-waterside:cup', 'badagry-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('badagry-waterside:pants', 'badagry-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('badagry-waterside:reusable', 'badagry-waterside', 'reusable', 'good', 'They last a year when the shop is far.'),
  ('badagry-waterside:heat', 'badagry-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('badagry-waterside:wipes', 'badagry-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('badagry-waterside:washbag', 'badagry-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('badagry-waterside:guide', 'badagry-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('badagry-community:pads', 'badagry-community', 'pads', 'good', 'Something for everyone today.'),
  ('badagry-community:liners', 'badagry-community', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('badagry-community:tampons', 'badagry-community', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('badagry-community:cup', 'badagry-community', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('badagry-community:pants', 'badagry-community', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('badagry-community:reusable', 'badagry-community', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('badagry-community:heat', 'badagry-community', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('badagry-community:wipes', 'badagry-community', 'wipes', 'good', 'There is no running water to wash.'),
  ('badagry-community:washbag', 'badagry-community', 'washbag', 'good', 'Keeps reusables clean.'),
  ('badagry-community:guide', 'badagry-community', 'guide', 'good', 'Helps mothers talk to daughters.'),
  ('badagry-football:pads', 'badagry-football', 'pads', 'good', 'A simple backup for match day.'),
  ('badagry-football:liners', 'badagry-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('badagry-football:tampons', 'badagry-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('badagry-football:cup', 'badagry-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('badagry-football:pants', 'badagry-football', 'pants', 'good', 'Stay put while running.'),
  ('badagry-football:reusable', 'badagry-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('badagry-football:heat', 'badagry-football', 'heat', 'good', 'Helps cramps after training.'),
  ('badagry-football:wipes', 'badagry-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('badagry-football:washbag', 'badagry-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('badagry-football:guide', 'badagry-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('badagry-shelter:pads', 'badagry-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('badagry-shelter:liners', 'badagry-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('badagry-shelter:tampons', 'badagry-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('badagry-shelter:cup', 'badagry-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('badagry-shelter:pants', 'badagry-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('badagry-shelter:reusable', 'badagry-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('badagry-shelter:heat', 'badagry-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('badagry-shelter:wipes', 'badagry-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('badagry-shelter:washbag', 'badagry-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('badagry-shelter:guide', 'badagry-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('badagry-community-2:pads', 'badagry-community-2', 'pads', 'good', 'Something for everyone today.'),
  ('badagry-community-2:liners', 'badagry-community-2', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('badagry-community-2:tampons', 'badagry-community-2', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('badagry-community-2:cup', 'badagry-community-2', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('badagry-community-2:pants', 'badagry-community-2', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('badagry-community-2:reusable', 'badagry-community-2', 'reusable', 'good', 'With water, these save money for a year.'),
  ('badagry-community-2:heat', 'badagry-community-2', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('badagry-community-2:wipes', 'badagry-community-2', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('badagry-community-2:washbag', 'badagry-community-2', 'washbag', 'good', 'Keeps reusables clean.'),
  ('badagry-community-2:guide', 'badagry-community-2', 'guide', 'good', 'Helps mothers talk to daughters.'),
  ('ajido-school:pads', 'ajido-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ajido-school:liners', 'ajido-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ajido-school:tampons', 'ajido-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajido-school:cup', 'ajido-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ajido-school:pants', 'ajido-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ajido-school:reusable', 'ajido-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ajido-school:heat', 'ajido-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ajido-school:wipes', 'ajido-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('ajido-school:washbag', 'ajido-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ajido-school:guide', 'ajido-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ajido-market:pads', 'ajido-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ajido-market:liners', 'ajido-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajido-market:tampons', 'ajido-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajido-market:cup', 'ajido-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ajido-market:pants', 'ajido-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ajido-market:reusable', 'ajido-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ajido-market:heat', 'ajido-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ajido-market:wipes', 'ajido-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ajido-market:washbag', 'ajido-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ajido-market:guide', 'ajido-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ajido-busstop:pads', 'ajido-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ajido-busstop:liners', 'ajido-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajido-busstop:tampons', 'ajido-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajido-busstop:cup', 'ajido-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ajido-busstop:pants', 'ajido-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ajido-busstop:reusable', 'ajido-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ajido-busstop:heat', 'ajido-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ajido-busstop:wipes', 'ajido-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ajido-busstop:washbag', 'ajido-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ajido-busstop:guide', 'ajido-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ajido-clinic:pads', 'ajido-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ajido-clinic:liners', 'ajido-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajido-clinic:tampons', 'ajido-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajido-clinic:cup', 'ajido-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ajido-clinic:pants', 'ajido-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ajido-clinic:reusable', 'ajido-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ajido-clinic:heat', 'ajido-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ajido-clinic:wipes', 'ajido-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ajido-clinic:washbag', 'ajido-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ajido-clinic:guide', 'ajido-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ajido-youth:pads', 'ajido-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ajido-youth:liners', 'ajido-youth', 'liners', 'good', 'For light days at the club.'),
  ('ajido-youth:tampons', 'ajido-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajido-youth:cup', 'ajido-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ajido-youth:pants', 'ajido-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ajido-youth:reusable', 'ajido-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ajido-youth:heat', 'ajido-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ajido-youth:wipes', 'ajido-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ajido-youth:washbag', 'ajido-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ajido-youth:guide', 'ajido-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ajido-waterside:pads', 'ajido-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('ajido-waterside:liners', 'ajido-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajido-waterside:tampons', 'ajido-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('ajido-waterside:cup', 'ajido-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ajido-waterside:pants', 'ajido-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('ajido-waterside:reusable', 'ajido-waterside', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ajido-waterside:heat', 'ajido-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ajido-waterside:wipes', 'ajido-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ajido-waterside:washbag', 'ajido-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('ajido-waterside:guide', 'ajido-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('ajido-community:pads', 'ajido-community', 'pads', 'good', 'Something for everyone today.'),
  ('ajido-community:liners', 'ajido-community', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajido-community:tampons', 'ajido-community', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajido-community:cup', 'ajido-community', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ajido-community:pants', 'ajido-community', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ajido-community:reusable', 'ajido-community', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ajido-community:heat', 'ajido-community', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ajido-community:wipes', 'ajido-community', 'wipes', 'good', 'There is no running water to wash.'),
  ('ajido-community:washbag', 'ajido-community', 'washbag', 'good', 'Keeps reusables clean.'),
  ('ajido-community:guide', 'ajido-community', 'guide', 'good', 'Helps mothers talk to daughters.'),
  ('ajido-football:pads', 'ajido-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ajido-football:liners', 'ajido-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajido-football:tampons', 'ajido-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajido-football:cup', 'ajido-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ajido-football:pants', 'ajido-football', 'pants', 'good', 'Stay put while running.'),
  ('ajido-football:reusable', 'ajido-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ajido-football:heat', 'ajido-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ajido-football:wipes', 'ajido-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ajido-football:washbag', 'ajido-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ajido-football:guide', 'ajido-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ajido-shelter:pads', 'ajido-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('ajido-shelter:liners', 'ajido-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajido-shelter:tampons', 'ajido-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajido-shelter:cup', 'ajido-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ajido-shelter:pants', 'ajido-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ajido-shelter:reusable', 'ajido-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ajido-shelter:heat', 'ajido-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('ajido-shelter:wipes', 'ajido-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('ajido-shelter:washbag', 'ajido-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('ajido-shelter:guide', 'ajido-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('agbara-school:pads', 'agbara-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('agbara-school:liners', 'agbara-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('agbara-school:tampons', 'agbara-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('agbara-school:cup', 'agbara-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('agbara-school:pants', 'agbara-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('agbara-school:reusable', 'agbara-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('agbara-school:heat', 'agbara-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('agbara-school:wipes', 'agbara-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('agbara-school:washbag', 'agbara-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('agbara-school:guide', 'agbara-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('agbara-market:pads', 'agbara-market', 'pads', 'good', 'Quick to change between customers.'),
  ('agbara-market:liners', 'agbara-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('agbara-market:tampons', 'agbara-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('agbara-market:cup', 'agbara-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('agbara-market:pants', 'agbara-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('agbara-market:reusable', 'agbara-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('agbara-market:heat', 'agbara-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('agbara-market:wipes', 'agbara-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('agbara-market:washbag', 'agbara-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('agbara-market:guide', 'agbara-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('agbara-busstop:pads', 'agbara-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('agbara-busstop:liners', 'agbara-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('agbara-busstop:tampons', 'agbara-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('agbara-busstop:cup', 'agbara-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('agbara-busstop:pants', 'agbara-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('agbara-busstop:reusable', 'agbara-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('agbara-busstop:heat', 'agbara-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('agbara-busstop:wipes', 'agbara-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('agbara-busstop:washbag', 'agbara-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('agbara-busstop:guide', 'agbara-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('agbara-clinic:pads', 'agbara-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('agbara-clinic:liners', 'agbara-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('agbara-clinic:tampons', 'agbara-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('agbara-clinic:cup', 'agbara-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('agbara-clinic:pants', 'agbara-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('agbara-clinic:reusable', 'agbara-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('agbara-clinic:heat', 'agbara-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('agbara-clinic:wipes', 'agbara-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('agbara-clinic:washbag', 'agbara-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('agbara-clinic:guide', 'agbara-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('agbara-youth:pads', 'agbara-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('agbara-youth:liners', 'agbara-youth', 'liners', 'good', 'For light days at the club.'),
  ('agbara-youth:tampons', 'agbara-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('agbara-youth:cup', 'agbara-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('agbara-youth:pants', 'agbara-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('agbara-youth:reusable', 'agbara-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('agbara-youth:heat', 'agbara-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('agbara-youth:wipes', 'agbara-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('agbara-youth:washbag', 'agbara-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('agbara-youth:guide', 'agbara-youth', 'guide', 'good', 'Answers questions they have.'),
  ('agbara-hostel:pads', 'agbara-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('agbara-hostel:liners', 'agbara-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('agbara-hostel:tampons', 'agbara-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('agbara-hostel:cup', 'agbara-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('agbara-hostel:pants', 'agbara-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('agbara-hostel:reusable', 'agbara-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('agbara-hostel:heat', 'agbara-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('agbara-hostel:wipes', 'agbara-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('agbara-hostel:washbag', 'agbara-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('agbara-hostel:guide', 'agbara-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('agbara-home:pads', 'agbara-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('agbara-home:liners', 'agbara-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('agbara-home:tampons', 'agbara-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('agbara-home:cup', 'agbara-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('agbara-home:pants', 'agbara-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('agbara-home:reusable', 'agbara-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('agbara-home:heat', 'agbara-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('agbara-home:wipes', 'agbara-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('agbara-home:washbag', 'agbara-home', 'washbag', 'good', 'A private place for her things.'),
  ('agbara-home:guide', 'agbara-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('agbara-football:pads', 'agbara-football', 'pads', 'good', 'A simple backup for match day.'),
  ('agbara-football:liners', 'agbara-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('agbara-football:tampons', 'agbara-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('agbara-football:cup', 'agbara-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('agbara-football:pants', 'agbara-football', 'pants', 'good', 'Stay put while running.'),
  ('agbara-football:reusable', 'agbara-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('agbara-football:heat', 'agbara-football', 'heat', 'good', 'Helps cramps after training.'),
  ('agbara-football:wipes', 'agbara-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('agbara-football:washbag', 'agbara-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('agbara-football:guide', 'agbara-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('agbara-training:pads', 'agbara-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('agbara-training:liners', 'agbara-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('agbara-training:tampons', 'agbara-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('agbara-training:cup', 'agbara-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('agbara-training:pants', 'agbara-training', 'pants', 'good', 'Last through long sessions.'),
  ('agbara-training:reusable', 'agbara-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('agbara-training:heat', 'agbara-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('agbara-training:wipes', 'agbara-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('agbara-training:washbag', 'agbara-training', 'washbag', 'good', 'Private storage at work.'),
  ('agbara-training:guide', 'agbara-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ojo-school:pads', 'ojo-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ojo-school:liners', 'ojo-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ojo-school:tampons', 'ojo-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojo-school:cup', 'ojo-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojo-school:pants', 'ojo-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ojo-school:reusable', 'ojo-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ojo-school:heat', 'ojo-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ojo-school:wipes', 'ojo-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('ojo-school:washbag', 'ojo-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ojo-school:guide', 'ojo-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ojo-market:pads', 'ojo-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ojo-market:liners', 'ojo-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojo-market:tampons', 'ojo-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojo-market:cup', 'ojo-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojo-market:pants', 'ojo-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ojo-market:reusable', 'ojo-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ojo-market:heat', 'ojo-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ojo-market:wipes', 'ojo-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ojo-market:washbag', 'ojo-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ojo-market:guide', 'ojo-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ojo-busstop:pads', 'ojo-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ojo-busstop:liners', 'ojo-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojo-busstop:tampons', 'ojo-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojo-busstop:cup', 'ojo-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojo-busstop:pants', 'ojo-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ojo-busstop:reusable', 'ojo-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ojo-busstop:heat', 'ojo-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ojo-busstop:wipes', 'ojo-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ojo-busstop:washbag', 'ojo-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ojo-busstop:guide', 'ojo-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ojo-clinic:pads', 'ojo-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ojo-clinic:liners', 'ojo-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojo-clinic:tampons', 'ojo-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojo-clinic:cup', 'ojo-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ojo-clinic:pants', 'ojo-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ojo-clinic:reusable', 'ojo-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ojo-clinic:heat', 'ojo-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ojo-clinic:wipes', 'ojo-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ojo-clinic:washbag', 'ojo-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ojo-clinic:guide', 'ojo-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ojo-youth:pads', 'ojo-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ojo-youth:liners', 'ojo-youth', 'liners', 'good', 'For light days at the club.'),
  ('ojo-youth:tampons', 'ojo-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojo-youth:cup', 'ojo-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojo-youth:pants', 'ojo-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ojo-youth:reusable', 'ojo-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ojo-youth:heat', 'ojo-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ojo-youth:wipes', 'ojo-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ojo-youth:washbag', 'ojo-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ojo-youth:guide', 'ojo-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ojo-waterside:pads', 'ojo-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('ojo-waterside:liners', 'ojo-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojo-waterside:tampons', 'ojo-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('ojo-waterside:cup', 'ojo-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ojo-waterside:pants', 'ojo-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('ojo-waterside:reusable', 'ojo-waterside', 'reusable', 'good', 'They last a year when the shop is far.'),
  ('ojo-waterside:heat', 'ojo-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ojo-waterside:wipes', 'ojo-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ojo-waterside:washbag', 'ojo-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('ojo-waterside:guide', 'ojo-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('ojo-community:pads', 'ojo-community', 'pads', 'good', 'Something for everyone today.'),
  ('ojo-community:liners', 'ojo-community', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojo-community:tampons', 'ojo-community', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojo-community:cup', 'ojo-community', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojo-community:pants', 'ojo-community', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ojo-community:reusable', 'ojo-community', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ojo-community:heat', 'ojo-community', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ojo-community:wipes', 'ojo-community', 'wipes', 'good', 'There is no running water to wash.'),
  ('ojo-community:washbag', 'ojo-community', 'washbag', 'good', 'Keeps reusables clean.'),
  ('ojo-community:guide', 'ojo-community', 'guide', 'good', 'Helps mothers talk to daughters.'),
  ('ojo-home:pads', 'ojo-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('ojo-home:liners', 'ojo-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojo-home:tampons', 'ojo-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojo-home:cup', 'ojo-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojo-home:pants', 'ojo-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ojo-home:reusable', 'ojo-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('ojo-home:heat', 'ojo-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ojo-home:wipes', 'ojo-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ojo-home:washbag', 'ojo-home', 'washbag', 'good', 'A private place for her things.'),
  ('ojo-home:guide', 'ojo-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('ojo-training:pads', 'ojo-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ojo-training:liners', 'ojo-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojo-training:tampons', 'ojo-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojo-training:cup', 'ojo-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ojo-training:pants', 'ojo-training', 'pants', 'good', 'Last through long sessions.'),
  ('ojo-training:reusable', 'ojo-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ojo-training:heat', 'ojo-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ojo-training:wipes', 'ojo-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ojo-training:washbag', 'ojo-training', 'washbag', 'good', 'Private storage at work.'),
  ('ojo-training:guide', 'ojo-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('okokomaiko-school:pads', 'okokomaiko-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('okokomaiko-school:liners', 'okokomaiko-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('okokomaiko-school:tampons', 'okokomaiko-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('okokomaiko-school:cup', 'okokomaiko-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('okokomaiko-school:pants', 'okokomaiko-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('okokomaiko-school:reusable', 'okokomaiko-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('okokomaiko-school:heat', 'okokomaiko-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('okokomaiko-school:wipes', 'okokomaiko-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('okokomaiko-school:washbag', 'okokomaiko-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('okokomaiko-school:guide', 'okokomaiko-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('okokomaiko-market:pads', 'okokomaiko-market', 'pads', 'good', 'Quick to change between customers.'),
  ('okokomaiko-market:liners', 'okokomaiko-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('okokomaiko-market:tampons', 'okokomaiko-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('okokomaiko-market:cup', 'okokomaiko-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('okokomaiko-market:pants', 'okokomaiko-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('okokomaiko-market:reusable', 'okokomaiko-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('okokomaiko-market:heat', 'okokomaiko-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('okokomaiko-market:wipes', 'okokomaiko-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('okokomaiko-market:washbag', 'okokomaiko-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('okokomaiko-market:guide', 'okokomaiko-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('okokomaiko-busstop:pads', 'okokomaiko-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('okokomaiko-busstop:liners', 'okokomaiko-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('okokomaiko-busstop:tampons', 'okokomaiko-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('okokomaiko-busstop:cup', 'okokomaiko-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('okokomaiko-busstop:pants', 'okokomaiko-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('okokomaiko-busstop:reusable', 'okokomaiko-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('okokomaiko-busstop:heat', 'okokomaiko-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('okokomaiko-busstop:wipes', 'okokomaiko-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('okokomaiko-busstop:washbag', 'okokomaiko-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('okokomaiko-busstop:guide', 'okokomaiko-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('okokomaiko-clinic:pads', 'okokomaiko-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('okokomaiko-clinic:liners', 'okokomaiko-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('okokomaiko-clinic:tampons', 'okokomaiko-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('okokomaiko-clinic:cup', 'okokomaiko-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('okokomaiko-clinic:pants', 'okokomaiko-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('okokomaiko-clinic:reusable', 'okokomaiko-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('okokomaiko-clinic:heat', 'okokomaiko-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('okokomaiko-clinic:wipes', 'okokomaiko-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('okokomaiko-clinic:washbag', 'okokomaiko-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('okokomaiko-clinic:guide', 'okokomaiko-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('okokomaiko-youth:pads', 'okokomaiko-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('okokomaiko-youth:liners', 'okokomaiko-youth', 'liners', 'good', 'For light days at the club.'),
  ('okokomaiko-youth:tampons', 'okokomaiko-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('okokomaiko-youth:cup', 'okokomaiko-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('okokomaiko-youth:pants', 'okokomaiko-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('okokomaiko-youth:reusable', 'okokomaiko-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('okokomaiko-youth:heat', 'okokomaiko-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('okokomaiko-youth:wipes', 'okokomaiko-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('okokomaiko-youth:washbag', 'okokomaiko-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('okokomaiko-youth:guide', 'okokomaiko-youth', 'guide', 'good', 'Answers questions they have.'),
  ('okokomaiko-hostel:pads', 'okokomaiko-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('okokomaiko-hostel:liners', 'okokomaiko-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('okokomaiko-hostel:tampons', 'okokomaiko-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('okokomaiko-hostel:cup', 'okokomaiko-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('okokomaiko-hostel:pants', 'okokomaiko-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('okokomaiko-hostel:reusable', 'okokomaiko-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('okokomaiko-hostel:heat', 'okokomaiko-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('okokomaiko-hostel:wipes', 'okokomaiko-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('okokomaiko-hostel:washbag', 'okokomaiko-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('okokomaiko-hostel:guide', 'okokomaiko-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('okokomaiko-home:pads', 'okokomaiko-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('okokomaiko-home:liners', 'okokomaiko-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('okokomaiko-home:tampons', 'okokomaiko-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('okokomaiko-home:cup', 'okokomaiko-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('okokomaiko-home:pants', 'okokomaiko-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('okokomaiko-home:reusable', 'okokomaiko-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('okokomaiko-home:heat', 'okokomaiko-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('okokomaiko-home:wipes', 'okokomaiko-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('okokomaiko-home:washbag', 'okokomaiko-home', 'washbag', 'good', 'A private place for her things.'),
  ('okokomaiko-home:guide', 'okokomaiko-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('okokomaiko-football:pads', 'okokomaiko-football', 'pads', 'good', 'A simple backup for match day.'),
  ('okokomaiko-football:liners', 'okokomaiko-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('okokomaiko-football:tampons', 'okokomaiko-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('okokomaiko-football:cup', 'okokomaiko-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('okokomaiko-football:pants', 'okokomaiko-football', 'pants', 'good', 'Stay put while running.'),
  ('okokomaiko-football:reusable', 'okokomaiko-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('okokomaiko-football:heat', 'okokomaiko-football', 'heat', 'good', 'Helps cramps after training.'),
  ('okokomaiko-football:wipes', 'okokomaiko-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('okokomaiko-football:washbag', 'okokomaiko-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('okokomaiko-football:guide', 'okokomaiko-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('okokomaiko-training:pads', 'okokomaiko-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('okokomaiko-training:liners', 'okokomaiko-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('okokomaiko-training:tampons', 'okokomaiko-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('okokomaiko-training:cup', 'okokomaiko-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('okokomaiko-training:pants', 'okokomaiko-training', 'pants', 'good', 'Last through long sessions.'),
  ('okokomaiko-training:reusable', 'okokomaiko-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('okokomaiko-training:heat', 'okokomaiko-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('okokomaiko-training:wipes', 'okokomaiko-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('okokomaiko-training:washbag', 'okokomaiko-training', 'washbag', 'good', 'Private storage at work.'),
  ('okokomaiko-training:guide', 'okokomaiko-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('iba-school:pads', 'iba-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('iba-school:liners', 'iba-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('iba-school:tampons', 'iba-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iba-school:cup', 'iba-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('iba-school:pants', 'iba-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('iba-school:reusable', 'iba-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('iba-school:heat', 'iba-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('iba-school:wipes', 'iba-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('iba-school:washbag', 'iba-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('iba-school:guide', 'iba-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('iba-market:pads', 'iba-market', 'pads', 'good', 'Quick to change between customers.'),
  ('iba-market:liners', 'iba-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('iba-market:tampons', 'iba-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iba-market:cup', 'iba-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('iba-market:pants', 'iba-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('iba-market:reusable', 'iba-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('iba-market:heat', 'iba-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('iba-market:wipes', 'iba-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('iba-market:washbag', 'iba-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('iba-market:guide', 'iba-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('iba-busstop:pads', 'iba-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('iba-busstop:liners', 'iba-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('iba-busstop:tampons', 'iba-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iba-busstop:cup', 'iba-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('iba-busstop:pants', 'iba-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('iba-busstop:reusable', 'iba-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('iba-busstop:heat', 'iba-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('iba-busstop:wipes', 'iba-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('iba-busstop:washbag', 'iba-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('iba-busstop:guide', 'iba-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('iba-clinic:pads', 'iba-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('iba-clinic:liners', 'iba-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('iba-clinic:tampons', 'iba-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iba-clinic:cup', 'iba-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('iba-clinic:pants', 'iba-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('iba-clinic:reusable', 'iba-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('iba-clinic:heat', 'iba-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('iba-clinic:wipes', 'iba-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('iba-clinic:washbag', 'iba-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('iba-clinic:guide', 'iba-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('iba-youth:pads', 'iba-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('iba-youth:liners', 'iba-youth', 'liners', 'good', 'For light days at the club.'),
  ('iba-youth:tampons', 'iba-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iba-youth:cup', 'iba-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('iba-youth:pants', 'iba-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('iba-youth:reusable', 'iba-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('iba-youth:heat', 'iba-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('iba-youth:wipes', 'iba-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('iba-youth:washbag', 'iba-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('iba-youth:guide', 'iba-youth', 'guide', 'good', 'Answers questions they have.'),
  ('iba-hostel:pads', 'iba-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('iba-hostel:liners', 'iba-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('iba-hostel:tampons', 'iba-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iba-hostel:cup', 'iba-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('iba-hostel:pants', 'iba-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('iba-hostel:reusable', 'iba-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('iba-hostel:heat', 'iba-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('iba-hostel:wipes', 'iba-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('iba-hostel:washbag', 'iba-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('iba-hostel:guide', 'iba-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('iba-home:pads', 'iba-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('iba-home:liners', 'iba-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('iba-home:tampons', 'iba-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iba-home:cup', 'iba-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('iba-home:pants', 'iba-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('iba-home:reusable', 'iba-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('iba-home:heat', 'iba-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('iba-home:wipes', 'iba-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('iba-home:washbag', 'iba-home', 'washbag', 'good', 'A private place for her things.'),
  ('iba-home:guide', 'iba-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('iba-football:pads', 'iba-football', 'pads', 'good', 'A simple backup for match day.'),
  ('iba-football:liners', 'iba-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('iba-football:tampons', 'iba-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iba-football:cup', 'iba-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('iba-football:pants', 'iba-football', 'pants', 'good', 'Stay put while running.'),
  ('iba-football:reusable', 'iba-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('iba-football:heat', 'iba-football', 'heat', 'good', 'Helps cramps after training.'),
  ('iba-football:wipes', 'iba-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('iba-football:washbag', 'iba-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('iba-football:guide', 'iba-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('iba-training:pads', 'iba-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('iba-training:liners', 'iba-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('iba-training:tampons', 'iba-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iba-training:cup', 'iba-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('iba-training:pants', 'iba-training', 'pants', 'good', 'Last through long sessions.'),
  ('iba-training:reusable', 'iba-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('iba-training:heat', 'iba-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('iba-training:wipes', 'iba-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('iba-training:washbag', 'iba-training', 'washbag', 'good', 'Private storage at work.'),
  ('iba-training:guide', 'iba-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('iba-shelter:pads', 'iba-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('iba-shelter:liners', 'iba-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('iba-shelter:tampons', 'iba-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iba-shelter:cup', 'iba-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('iba-shelter:pants', 'iba-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('iba-shelter:reusable', 'iba-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('iba-shelter:heat', 'iba-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('iba-shelter:wipes', 'iba-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('iba-shelter:washbag', 'iba-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('iba-shelter:guide', 'iba-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('igando-school:pads', 'igando-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('igando-school:liners', 'igando-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('igando-school:tampons', 'igando-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('igando-school:cup', 'igando-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('igando-school:pants', 'igando-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('igando-school:reusable', 'igando-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('igando-school:heat', 'igando-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('igando-school:wipes', 'igando-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('igando-school:washbag', 'igando-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('igando-school:guide', 'igando-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('igando-market:pads', 'igando-market', 'pads', 'good', 'Quick to change between customers.'),
  ('igando-market:liners', 'igando-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('igando-market:tampons', 'igando-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('igando-market:cup', 'igando-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('igando-market:pants', 'igando-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('igando-market:reusable', 'igando-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('igando-market:heat', 'igando-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('igando-market:wipes', 'igando-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('igando-market:washbag', 'igando-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('igando-market:guide', 'igando-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('igando-busstop:pads', 'igando-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('igando-busstop:liners', 'igando-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('igando-busstop:tampons', 'igando-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('igando-busstop:cup', 'igando-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('igando-busstop:pants', 'igando-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('igando-busstop:reusable', 'igando-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('igando-busstop:heat', 'igando-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('igando-busstop:wipes', 'igando-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('igando-busstop:washbag', 'igando-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('igando-busstop:guide', 'igando-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('igando-clinic:pads', 'igando-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('igando-clinic:liners', 'igando-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('igando-clinic:tampons', 'igando-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('igando-clinic:cup', 'igando-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('igando-clinic:pants', 'igando-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('igando-clinic:reusable', 'igando-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('igando-clinic:heat', 'igando-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('igando-clinic:wipes', 'igando-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('igando-clinic:washbag', 'igando-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('igando-clinic:guide', 'igando-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('igando-youth:pads', 'igando-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('igando-youth:liners', 'igando-youth', 'liners', 'good', 'For light days at the club.'),
  ('igando-youth:tampons', 'igando-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('igando-youth:cup', 'igando-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('igando-youth:pants', 'igando-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('igando-youth:reusable', 'igando-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('igando-youth:heat', 'igando-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('igando-youth:wipes', 'igando-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('igando-youth:washbag', 'igando-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('igando-youth:guide', 'igando-youth', 'guide', 'good', 'Answers questions they have.'),
  ('igando-hostel:pads', 'igando-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('igando-hostel:liners', 'igando-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('igando-hostel:tampons', 'igando-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('igando-hostel:cup', 'igando-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('igando-hostel:pants', 'igando-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('igando-hostel:reusable', 'igando-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('igando-hostel:heat', 'igando-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('igando-hostel:wipes', 'igando-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('igando-hostel:washbag', 'igando-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('igando-hostel:guide', 'igando-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('igando-home:pads', 'igando-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('igando-home:liners', 'igando-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('igando-home:tampons', 'igando-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('igando-home:cup', 'igando-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('igando-home:pants', 'igando-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('igando-home:reusable', 'igando-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('igando-home:heat', 'igando-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('igando-home:wipes', 'igando-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('igando-home:washbag', 'igando-home', 'washbag', 'good', 'A private place for her things.'),
  ('igando-home:guide', 'igando-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('igando-football:pads', 'igando-football', 'pads', 'good', 'A simple backup for match day.'),
  ('igando-football:liners', 'igando-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('igando-football:tampons', 'igando-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('igando-football:cup', 'igando-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('igando-football:pants', 'igando-football', 'pants', 'good', 'Stay put while running.'),
  ('igando-football:reusable', 'igando-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('igando-football:heat', 'igando-football', 'heat', 'good', 'Helps cramps after training.'),
  ('igando-football:wipes', 'igando-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('igando-football:washbag', 'igando-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('igando-football:guide', 'igando-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('igando-training:pads', 'igando-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('igando-training:liners', 'igando-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('igando-training:tampons', 'igando-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('igando-training:cup', 'igando-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('igando-training:pants', 'igando-training', 'pants', 'good', 'Last through long sessions.'),
  ('igando-training:reusable', 'igando-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('igando-training:heat', 'igando-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('igando-training:wipes', 'igando-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('igando-training:washbag', 'igando-training', 'washbag', 'good', 'Private storage at work.'),
  ('igando-training:guide', 'igando-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikotun-school:pads', 'ikotun-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ikotun-school:liners', 'ikotun-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ikotun-school:tampons', 'ikotun-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikotun-school:cup', 'ikotun-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikotun-school:pants', 'ikotun-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikotun-school:reusable', 'ikotun-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikotun-school:heat', 'ikotun-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikotun-school:wipes', 'ikotun-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('ikotun-school:washbag', 'ikotun-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ikotun-school:guide', 'ikotun-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ikotun-market:pads', 'ikotun-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ikotun-market:liners', 'ikotun-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikotun-market:tampons', 'ikotun-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikotun-market:cup', 'ikotun-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikotun-market:pants', 'ikotun-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikotun-market:reusable', 'ikotun-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikotun-market:heat', 'ikotun-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ikotun-market:wipes', 'ikotun-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ikotun-market:washbag', 'ikotun-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ikotun-market:guide', 'ikotun-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikotun-busstop:pads', 'ikotun-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ikotun-busstop:liners', 'ikotun-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikotun-busstop:tampons', 'ikotun-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikotun-busstop:cup', 'ikotun-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikotun-busstop:pants', 'ikotun-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ikotun-busstop:reusable', 'ikotun-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikotun-busstop:heat', 'ikotun-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ikotun-busstop:wipes', 'ikotun-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ikotun-busstop:washbag', 'ikotun-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ikotun-busstop:guide', 'ikotun-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikotun-clinic:pads', 'ikotun-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ikotun-clinic:liners', 'ikotun-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikotun-clinic:tampons', 'ikotun-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikotun-clinic:cup', 'ikotun-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikotun-clinic:pants', 'ikotun-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ikotun-clinic:reusable', 'ikotun-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikotun-clinic:heat', 'ikotun-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ikotun-clinic:wipes', 'ikotun-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikotun-clinic:washbag', 'ikotun-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ikotun-clinic:guide', 'ikotun-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ikotun-youth:pads', 'ikotun-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ikotun-youth:liners', 'ikotun-youth', 'liners', 'good', 'For light days at the club.'),
  ('ikotun-youth:tampons', 'ikotun-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikotun-youth:cup', 'ikotun-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikotun-youth:pants', 'ikotun-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikotun-youth:reusable', 'ikotun-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikotun-youth:heat', 'ikotun-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikotun-youth:wipes', 'ikotun-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikotun-youth:washbag', 'ikotun-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ikotun-youth:guide', 'ikotun-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ikotun-hostel:pads', 'ikotun-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('ikotun-hostel:liners', 'ikotun-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikotun-hostel:tampons', 'ikotun-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikotun-hostel:cup', 'ikotun-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikotun-hostel:pants', 'ikotun-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikotun-hostel:reusable', 'ikotun-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('ikotun-hostel:heat', 'ikotun-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikotun-hostel:wipes', 'ikotun-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikotun-hostel:washbag', 'ikotun-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('ikotun-hostel:guide', 'ikotun-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('ikotun-home:pads', 'ikotun-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('ikotun-home:liners', 'ikotun-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikotun-home:tampons', 'ikotun-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikotun-home:cup', 'ikotun-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikotun-home:pants', 'ikotun-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikotun-home:reusable', 'ikotun-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('ikotun-home:heat', 'ikotun-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikotun-home:wipes', 'ikotun-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikotun-home:washbag', 'ikotun-home', 'washbag', 'good', 'A private place for her things.'),
  ('ikotun-home:guide', 'ikotun-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('ikotun-football:pads', 'ikotun-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ikotun-football:liners', 'ikotun-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikotun-football:tampons', 'ikotun-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikotun-football:cup', 'ikotun-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikotun-football:pants', 'ikotun-football', 'pants', 'good', 'Stay put while running.'),
  ('ikotun-football:reusable', 'ikotun-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikotun-football:heat', 'ikotun-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ikotun-football:wipes', 'ikotun-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikotun-football:washbag', 'ikotun-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ikotun-football:guide', 'ikotun-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikotun-training:pads', 'ikotun-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ikotun-training:liners', 'ikotun-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikotun-training:tampons', 'ikotun-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikotun-training:cup', 'ikotun-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikotun-training:pants', 'ikotun-training', 'pants', 'good', 'Last through long sessions.'),
  ('ikotun-training:reusable', 'ikotun-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikotun-training:heat', 'ikotun-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ikotun-training:wipes', 'ikotun-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikotun-training:washbag', 'ikotun-training', 'washbag', 'good', 'Private storage at work.'),
  ('ikotun-training:guide', 'ikotun-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikotun-shelter:pads', 'ikotun-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('ikotun-shelter:liners', 'ikotun-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikotun-shelter:tampons', 'ikotun-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikotun-shelter:cup', 'ikotun-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikotun-shelter:pants', 'ikotun-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikotun-shelter:reusable', 'ikotun-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikotun-shelter:heat', 'ikotun-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('ikotun-shelter:wipes', 'ikotun-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('ikotun-shelter:washbag', 'ikotun-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('ikotun-shelter:guide', 'ikotun-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('idimu-school:pads', 'idimu-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('idimu-school:liners', 'idimu-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('idimu-school:tampons', 'idimu-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('idimu-school:cup', 'idimu-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('idimu-school:pants', 'idimu-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('idimu-school:reusable', 'idimu-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('idimu-school:heat', 'idimu-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('idimu-school:wipes', 'idimu-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('idimu-school:washbag', 'idimu-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('idimu-school:guide', 'idimu-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('idimu-market:pads', 'idimu-market', 'pads', 'good', 'Quick to change between customers.'),
  ('idimu-market:liners', 'idimu-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('idimu-market:tampons', 'idimu-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('idimu-market:cup', 'idimu-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('idimu-market:pants', 'idimu-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('idimu-market:reusable', 'idimu-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('idimu-market:heat', 'idimu-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('idimu-market:wipes', 'idimu-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('idimu-market:washbag', 'idimu-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('idimu-market:guide', 'idimu-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('idimu-busstop:pads', 'idimu-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('idimu-busstop:liners', 'idimu-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('idimu-busstop:tampons', 'idimu-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('idimu-busstop:cup', 'idimu-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('idimu-busstop:pants', 'idimu-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('idimu-busstop:reusable', 'idimu-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('idimu-busstop:heat', 'idimu-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('idimu-busstop:wipes', 'idimu-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('idimu-busstop:washbag', 'idimu-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('idimu-busstop:guide', 'idimu-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('idimu-clinic:pads', 'idimu-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('idimu-clinic:liners', 'idimu-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('idimu-clinic:tampons', 'idimu-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('idimu-clinic:cup', 'idimu-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('idimu-clinic:pants', 'idimu-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('idimu-clinic:reusable', 'idimu-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('idimu-clinic:heat', 'idimu-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('idimu-clinic:wipes', 'idimu-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('idimu-clinic:washbag', 'idimu-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('idimu-clinic:guide', 'idimu-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('idimu-youth:pads', 'idimu-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('idimu-youth:liners', 'idimu-youth', 'liners', 'good', 'For light days at the club.'),
  ('idimu-youth:tampons', 'idimu-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('idimu-youth:cup', 'idimu-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('idimu-youth:pants', 'idimu-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('idimu-youth:reusable', 'idimu-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('idimu-youth:heat', 'idimu-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('idimu-youth:wipes', 'idimu-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('idimu-youth:washbag', 'idimu-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('idimu-youth:guide', 'idimu-youth', 'guide', 'good', 'Answers questions they have.'),
  ('idimu-hostel:pads', 'idimu-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('idimu-hostel:liners', 'idimu-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('idimu-hostel:tampons', 'idimu-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('idimu-hostel:cup', 'idimu-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('idimu-hostel:pants', 'idimu-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('idimu-hostel:reusable', 'idimu-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('idimu-hostel:heat', 'idimu-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('idimu-hostel:wipes', 'idimu-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('idimu-hostel:washbag', 'idimu-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('idimu-hostel:guide', 'idimu-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('idimu-home:pads', 'idimu-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('idimu-home:liners', 'idimu-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('idimu-home:tampons', 'idimu-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('idimu-home:cup', 'idimu-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('idimu-home:pants', 'idimu-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('idimu-home:reusable', 'idimu-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('idimu-home:heat', 'idimu-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('idimu-home:wipes', 'idimu-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('idimu-home:washbag', 'idimu-home', 'washbag', 'good', 'A private place for her things.'),
  ('idimu-home:guide', 'idimu-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('idimu-football:pads', 'idimu-football', 'pads', 'good', 'A simple backup for match day.'),
  ('idimu-football:liners', 'idimu-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('idimu-football:tampons', 'idimu-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('idimu-football:cup', 'idimu-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('idimu-football:pants', 'idimu-football', 'pants', 'good', 'Stay put while running.'),
  ('idimu-football:reusable', 'idimu-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('idimu-football:heat', 'idimu-football', 'heat', 'good', 'Helps cramps after training.'),
  ('idimu-football:wipes', 'idimu-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('idimu-football:washbag', 'idimu-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('idimu-football:guide', 'idimu-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('idimu-training:pads', 'idimu-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('idimu-training:liners', 'idimu-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('idimu-training:tampons', 'idimu-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('idimu-training:cup', 'idimu-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('idimu-training:pants', 'idimu-training', 'pants', 'good', 'Last through long sessions.'),
  ('idimu-training:reusable', 'idimu-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('idimu-training:heat', 'idimu-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('idimu-training:wipes', 'idimu-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('idimu-training:washbag', 'idimu-training', 'washbag', 'good', 'Private storage at work.'),
  ('idimu-training:guide', 'idimu-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('idimu-shelter:pads', 'idimu-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('idimu-shelter:liners', 'idimu-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('idimu-shelter:tampons', 'idimu-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('idimu-shelter:cup', 'idimu-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('idimu-shelter:pants', 'idimu-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('idimu-shelter:reusable', 'idimu-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('idimu-shelter:heat', 'idimu-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('idimu-shelter:wipes', 'idimu-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('idimu-shelter:washbag', 'idimu-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('idimu-shelter:guide', 'idimu-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ayobo-school:pads', 'ayobo-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ayobo-school:liners', 'ayobo-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ayobo-school:tampons', 'ayobo-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ayobo-school:cup', 'ayobo-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ayobo-school:pants', 'ayobo-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ayobo-school:reusable', 'ayobo-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ayobo-school:heat', 'ayobo-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ayobo-school:wipes', 'ayobo-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('ayobo-school:washbag', 'ayobo-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ayobo-school:guide', 'ayobo-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ayobo-market:pads', 'ayobo-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ayobo-market:liners', 'ayobo-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ayobo-market:tampons', 'ayobo-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ayobo-market:cup', 'ayobo-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ayobo-market:pants', 'ayobo-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ayobo-market:reusable', 'ayobo-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ayobo-market:heat', 'ayobo-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ayobo-market:wipes', 'ayobo-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ayobo-market:washbag', 'ayobo-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ayobo-market:guide', 'ayobo-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ayobo-busstop:pads', 'ayobo-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ayobo-busstop:liners', 'ayobo-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ayobo-busstop:tampons', 'ayobo-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ayobo-busstop:cup', 'ayobo-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ayobo-busstop:pants', 'ayobo-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ayobo-busstop:reusable', 'ayobo-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ayobo-busstop:heat', 'ayobo-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ayobo-busstop:wipes', 'ayobo-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ayobo-busstop:washbag', 'ayobo-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ayobo-busstop:guide', 'ayobo-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ayobo-clinic:pads', 'ayobo-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ayobo-clinic:liners', 'ayobo-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ayobo-clinic:tampons', 'ayobo-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ayobo-clinic:cup', 'ayobo-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ayobo-clinic:pants', 'ayobo-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ayobo-clinic:reusable', 'ayobo-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ayobo-clinic:heat', 'ayobo-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ayobo-clinic:wipes', 'ayobo-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ayobo-clinic:washbag', 'ayobo-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ayobo-clinic:guide', 'ayobo-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ayobo-youth:pads', 'ayobo-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ayobo-youth:liners', 'ayobo-youth', 'liners', 'good', 'For light days at the club.'),
  ('ayobo-youth:tampons', 'ayobo-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ayobo-youth:cup', 'ayobo-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ayobo-youth:pants', 'ayobo-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ayobo-youth:reusable', 'ayobo-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ayobo-youth:heat', 'ayobo-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ayobo-youth:wipes', 'ayobo-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ayobo-youth:washbag', 'ayobo-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ayobo-youth:guide', 'ayobo-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ayobo-hostel:pads', 'ayobo-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('ayobo-hostel:liners', 'ayobo-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ayobo-hostel:tampons', 'ayobo-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ayobo-hostel:cup', 'ayobo-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ayobo-hostel:pants', 'ayobo-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ayobo-hostel:reusable', 'ayobo-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('ayobo-hostel:heat', 'ayobo-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ayobo-hostel:wipes', 'ayobo-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ayobo-hostel:washbag', 'ayobo-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('ayobo-hostel:guide', 'ayobo-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('ayobo-home:pads', 'ayobo-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('ayobo-home:liners', 'ayobo-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ayobo-home:tampons', 'ayobo-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ayobo-home:cup', 'ayobo-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ayobo-home:pants', 'ayobo-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ayobo-home:reusable', 'ayobo-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('ayobo-home:heat', 'ayobo-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ayobo-home:wipes', 'ayobo-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ayobo-home:washbag', 'ayobo-home', 'washbag', 'good', 'A private place for her things.'),
  ('ayobo-home:guide', 'ayobo-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('ayobo-football:pads', 'ayobo-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ayobo-football:liners', 'ayobo-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ayobo-football:tampons', 'ayobo-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ayobo-football:cup', 'ayobo-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ayobo-football:pants', 'ayobo-football', 'pants', 'good', 'Stay put while running.'),
  ('ayobo-football:reusable', 'ayobo-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ayobo-football:heat', 'ayobo-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ayobo-football:wipes', 'ayobo-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ayobo-football:washbag', 'ayobo-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ayobo-football:guide', 'ayobo-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ayobo-training:pads', 'ayobo-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ayobo-training:liners', 'ayobo-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ayobo-training:tampons', 'ayobo-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ayobo-training:cup', 'ayobo-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ayobo-training:pants', 'ayobo-training', 'pants', 'good', 'Last through long sessions.'),
  ('ayobo-training:reusable', 'ayobo-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ayobo-training:heat', 'ayobo-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ayobo-training:wipes', 'ayobo-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ayobo-training:washbag', 'ayobo-training', 'washbag', 'good', 'Private storage at work.'),
  ('ayobo-training:guide', 'ayobo-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('egbeda-school:pads', 'egbeda-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('egbeda-school:liners', 'egbeda-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('egbeda-school:tampons', 'egbeda-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('egbeda-school:cup', 'egbeda-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('egbeda-school:pants', 'egbeda-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('egbeda-school:reusable', 'egbeda-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('egbeda-school:heat', 'egbeda-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('egbeda-school:wipes', 'egbeda-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('egbeda-school:washbag', 'egbeda-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('egbeda-school:guide', 'egbeda-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('egbeda-market:pads', 'egbeda-market', 'pads', 'good', 'Quick to change between customers.'),
  ('egbeda-market:liners', 'egbeda-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('egbeda-market:tampons', 'egbeda-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('egbeda-market:cup', 'egbeda-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('egbeda-market:pants', 'egbeda-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('egbeda-market:reusable', 'egbeda-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('egbeda-market:heat', 'egbeda-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('egbeda-market:wipes', 'egbeda-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('egbeda-market:washbag', 'egbeda-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('egbeda-market:guide', 'egbeda-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('egbeda-busstop:pads', 'egbeda-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('egbeda-busstop:liners', 'egbeda-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('egbeda-busstop:tampons', 'egbeda-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('egbeda-busstop:cup', 'egbeda-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('egbeda-busstop:pants', 'egbeda-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('egbeda-busstop:reusable', 'egbeda-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('egbeda-busstop:heat', 'egbeda-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('egbeda-busstop:wipes', 'egbeda-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('egbeda-busstop:washbag', 'egbeda-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('egbeda-busstop:guide', 'egbeda-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('egbeda-clinic:pads', 'egbeda-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('egbeda-clinic:liners', 'egbeda-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('egbeda-clinic:tampons', 'egbeda-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('egbeda-clinic:cup', 'egbeda-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('egbeda-clinic:pants', 'egbeda-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('egbeda-clinic:reusable', 'egbeda-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('egbeda-clinic:heat', 'egbeda-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('egbeda-clinic:wipes', 'egbeda-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('egbeda-clinic:washbag', 'egbeda-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('egbeda-clinic:guide', 'egbeda-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('egbeda-youth:pads', 'egbeda-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('egbeda-youth:liners', 'egbeda-youth', 'liners', 'good', 'For light days at the club.'),
  ('egbeda-youth:tampons', 'egbeda-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('egbeda-youth:cup', 'egbeda-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('egbeda-youth:pants', 'egbeda-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('egbeda-youth:reusable', 'egbeda-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('egbeda-youth:heat', 'egbeda-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('egbeda-youth:wipes', 'egbeda-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('egbeda-youth:washbag', 'egbeda-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('egbeda-youth:guide', 'egbeda-youth', 'guide', 'good', 'Answers questions they have.'),
  ('egbeda-hostel:pads', 'egbeda-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('egbeda-hostel:liners', 'egbeda-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('egbeda-hostel:tampons', 'egbeda-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('egbeda-hostel:cup', 'egbeda-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('egbeda-hostel:pants', 'egbeda-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('egbeda-hostel:reusable', 'egbeda-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('egbeda-hostel:heat', 'egbeda-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('egbeda-hostel:wipes', 'egbeda-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('egbeda-hostel:washbag', 'egbeda-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('egbeda-hostel:guide', 'egbeda-hostel', 'guide', 'good', 'Answers questions they are shy to ask.');

INSERT INTO giveday_needs (id, place_id, product_id, rating, why) VALUES
  ('egbeda-home:pads', 'egbeda-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('egbeda-home:liners', 'egbeda-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('egbeda-home:tampons', 'egbeda-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('egbeda-home:cup', 'egbeda-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('egbeda-home:pants', 'egbeda-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('egbeda-home:reusable', 'egbeda-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('egbeda-home:heat', 'egbeda-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('egbeda-home:wipes', 'egbeda-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('egbeda-home:washbag', 'egbeda-home', 'washbag', 'good', 'A private place for her things.'),
  ('egbeda-home:guide', 'egbeda-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('egbeda-football:pads', 'egbeda-football', 'pads', 'good', 'A simple backup for match day.'),
  ('egbeda-football:liners', 'egbeda-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('egbeda-football:tampons', 'egbeda-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('egbeda-football:cup', 'egbeda-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('egbeda-football:pants', 'egbeda-football', 'pants', 'good', 'Stay put while running.'),
  ('egbeda-football:reusable', 'egbeda-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('egbeda-football:heat', 'egbeda-football', 'heat', 'good', 'Helps cramps after training.'),
  ('egbeda-football:wipes', 'egbeda-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('egbeda-football:washbag', 'egbeda-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('egbeda-football:guide', 'egbeda-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('egbeda-training:pads', 'egbeda-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('egbeda-training:liners', 'egbeda-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('egbeda-training:tampons', 'egbeda-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('egbeda-training:cup', 'egbeda-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('egbeda-training:pants', 'egbeda-training', 'pants', 'good', 'Last through long sessions.'),
  ('egbeda-training:reusable', 'egbeda-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('egbeda-training:heat', 'egbeda-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('egbeda-training:wipes', 'egbeda-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('egbeda-training:washbag', 'egbeda-training', 'washbag', 'good', 'Private storage at work.'),
  ('egbeda-training:guide', 'egbeda-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('egbeda-shelter:pads', 'egbeda-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('egbeda-shelter:liners', 'egbeda-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('egbeda-shelter:tampons', 'egbeda-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('egbeda-shelter:cup', 'egbeda-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('egbeda-shelter:pants', 'egbeda-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('egbeda-shelter:reusable', 'egbeda-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('egbeda-shelter:heat', 'egbeda-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('egbeda-shelter:wipes', 'egbeda-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('egbeda-shelter:washbag', 'egbeda-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('egbeda-shelter:guide', 'egbeda-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('iyana-ipaja-school:pads', 'iyana-ipaja-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('iyana-ipaja-school:liners', 'iyana-ipaja-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('iyana-ipaja-school:tampons', 'iyana-ipaja-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iyana-ipaja-school:cup', 'iyana-ipaja-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('iyana-ipaja-school:pants', 'iyana-ipaja-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('iyana-ipaja-school:reusable', 'iyana-ipaja-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('iyana-ipaja-school:heat', 'iyana-ipaja-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('iyana-ipaja-school:wipes', 'iyana-ipaja-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('iyana-ipaja-school:washbag', 'iyana-ipaja-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('iyana-ipaja-school:guide', 'iyana-ipaja-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('iyana-ipaja-market:pads', 'iyana-ipaja-market', 'pads', 'good', 'Quick to change between customers.'),
  ('iyana-ipaja-market:liners', 'iyana-ipaja-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('iyana-ipaja-market:tampons', 'iyana-ipaja-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iyana-ipaja-market:cup', 'iyana-ipaja-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('iyana-ipaja-market:pants', 'iyana-ipaja-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('iyana-ipaja-market:reusable', 'iyana-ipaja-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('iyana-ipaja-market:heat', 'iyana-ipaja-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('iyana-ipaja-market:wipes', 'iyana-ipaja-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('iyana-ipaja-market:washbag', 'iyana-ipaja-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('iyana-ipaja-market:guide', 'iyana-ipaja-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('iyana-ipaja-busstop:pads', 'iyana-ipaja-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('iyana-ipaja-busstop:liners', 'iyana-ipaja-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('iyana-ipaja-busstop:tampons', 'iyana-ipaja-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iyana-ipaja-busstop:cup', 'iyana-ipaja-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('iyana-ipaja-busstop:pants', 'iyana-ipaja-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('iyana-ipaja-busstop:reusable', 'iyana-ipaja-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('iyana-ipaja-busstop:heat', 'iyana-ipaja-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('iyana-ipaja-busstop:wipes', 'iyana-ipaja-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('iyana-ipaja-busstop:washbag', 'iyana-ipaja-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('iyana-ipaja-busstop:guide', 'iyana-ipaja-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('iyana-ipaja-clinic:pads', 'iyana-ipaja-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('iyana-ipaja-clinic:liners', 'iyana-ipaja-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('iyana-ipaja-clinic:tampons', 'iyana-ipaja-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iyana-ipaja-clinic:cup', 'iyana-ipaja-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('iyana-ipaja-clinic:pants', 'iyana-ipaja-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('iyana-ipaja-clinic:reusable', 'iyana-ipaja-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('iyana-ipaja-clinic:heat', 'iyana-ipaja-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('iyana-ipaja-clinic:wipes', 'iyana-ipaja-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('iyana-ipaja-clinic:washbag', 'iyana-ipaja-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('iyana-ipaja-clinic:guide', 'iyana-ipaja-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('iyana-ipaja-youth:pads', 'iyana-ipaja-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('iyana-ipaja-youth:liners', 'iyana-ipaja-youth', 'liners', 'good', 'For light days at the club.'),
  ('iyana-ipaja-youth:tampons', 'iyana-ipaja-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iyana-ipaja-youth:cup', 'iyana-ipaja-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('iyana-ipaja-youth:pants', 'iyana-ipaja-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('iyana-ipaja-youth:reusable', 'iyana-ipaja-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('iyana-ipaja-youth:heat', 'iyana-ipaja-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('iyana-ipaja-youth:wipes', 'iyana-ipaja-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('iyana-ipaja-youth:washbag', 'iyana-ipaja-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('iyana-ipaja-youth:guide', 'iyana-ipaja-youth', 'guide', 'good', 'Answers questions they have.'),
  ('iyana-ipaja-hostel:pads', 'iyana-ipaja-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('iyana-ipaja-hostel:liners', 'iyana-ipaja-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('iyana-ipaja-hostel:tampons', 'iyana-ipaja-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iyana-ipaja-hostel:cup', 'iyana-ipaja-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('iyana-ipaja-hostel:pants', 'iyana-ipaja-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('iyana-ipaja-hostel:reusable', 'iyana-ipaja-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('iyana-ipaja-hostel:heat', 'iyana-ipaja-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('iyana-ipaja-hostel:wipes', 'iyana-ipaja-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('iyana-ipaja-hostel:washbag', 'iyana-ipaja-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('iyana-ipaja-hostel:guide', 'iyana-ipaja-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('iyana-ipaja-home:pads', 'iyana-ipaja-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('iyana-ipaja-home:liners', 'iyana-ipaja-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('iyana-ipaja-home:tampons', 'iyana-ipaja-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iyana-ipaja-home:cup', 'iyana-ipaja-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('iyana-ipaja-home:pants', 'iyana-ipaja-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('iyana-ipaja-home:reusable', 'iyana-ipaja-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('iyana-ipaja-home:heat', 'iyana-ipaja-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('iyana-ipaja-home:wipes', 'iyana-ipaja-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('iyana-ipaja-home:washbag', 'iyana-ipaja-home', 'washbag', 'good', 'A private place for her things.'),
  ('iyana-ipaja-home:guide', 'iyana-ipaja-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('iyana-ipaja-football:pads', 'iyana-ipaja-football', 'pads', 'good', 'A simple backup for match day.'),
  ('iyana-ipaja-football:liners', 'iyana-ipaja-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('iyana-ipaja-football:tampons', 'iyana-ipaja-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iyana-ipaja-football:cup', 'iyana-ipaja-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('iyana-ipaja-football:pants', 'iyana-ipaja-football', 'pants', 'good', 'Stay put while running.'),
  ('iyana-ipaja-football:reusable', 'iyana-ipaja-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('iyana-ipaja-football:heat', 'iyana-ipaja-football', 'heat', 'good', 'Helps cramps after training.'),
  ('iyana-ipaja-football:wipes', 'iyana-ipaja-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('iyana-ipaja-football:washbag', 'iyana-ipaja-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('iyana-ipaja-football:guide', 'iyana-ipaja-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('iyana-ipaja-training:pads', 'iyana-ipaja-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('iyana-ipaja-training:liners', 'iyana-ipaja-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('iyana-ipaja-training:tampons', 'iyana-ipaja-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('iyana-ipaja-training:cup', 'iyana-ipaja-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('iyana-ipaja-training:pants', 'iyana-ipaja-training', 'pants', 'good', 'Last through long sessions.'),
  ('iyana-ipaja-training:reusable', 'iyana-ipaja-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('iyana-ipaja-training:heat', 'iyana-ipaja-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('iyana-ipaja-training:wipes', 'iyana-ipaja-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('iyana-ipaja-training:washbag', 'iyana-ipaja-training', 'washbag', 'good', 'Private storage at work.'),
  ('iyana-ipaja-training:guide', 'iyana-ipaja-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('satellite-town-school:pads', 'satellite-town-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('satellite-town-school:liners', 'satellite-town-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('satellite-town-school:tampons', 'satellite-town-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('satellite-town-school:cup', 'satellite-town-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('satellite-town-school:pants', 'satellite-town-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('satellite-town-school:reusable', 'satellite-town-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('satellite-town-school:heat', 'satellite-town-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('satellite-town-school:wipes', 'satellite-town-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('satellite-town-school:washbag', 'satellite-town-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('satellite-town-school:guide', 'satellite-town-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('satellite-town-market:pads', 'satellite-town-market', 'pads', 'good', 'Quick to change between customers.'),
  ('satellite-town-market:liners', 'satellite-town-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('satellite-town-market:tampons', 'satellite-town-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('satellite-town-market:cup', 'satellite-town-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('satellite-town-market:pants', 'satellite-town-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('satellite-town-market:reusable', 'satellite-town-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('satellite-town-market:heat', 'satellite-town-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('satellite-town-market:wipes', 'satellite-town-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('satellite-town-market:washbag', 'satellite-town-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('satellite-town-market:guide', 'satellite-town-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('satellite-town-busstop:pads', 'satellite-town-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('satellite-town-busstop:liners', 'satellite-town-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('satellite-town-busstop:tampons', 'satellite-town-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('satellite-town-busstop:cup', 'satellite-town-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('satellite-town-busstop:pants', 'satellite-town-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('satellite-town-busstop:reusable', 'satellite-town-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('satellite-town-busstop:heat', 'satellite-town-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('satellite-town-busstop:wipes', 'satellite-town-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('satellite-town-busstop:washbag', 'satellite-town-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('satellite-town-busstop:guide', 'satellite-town-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('satellite-town-clinic:pads', 'satellite-town-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('satellite-town-clinic:liners', 'satellite-town-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('satellite-town-clinic:tampons', 'satellite-town-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('satellite-town-clinic:cup', 'satellite-town-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('satellite-town-clinic:pants', 'satellite-town-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('satellite-town-clinic:reusable', 'satellite-town-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('satellite-town-clinic:heat', 'satellite-town-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('satellite-town-clinic:wipes', 'satellite-town-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('satellite-town-clinic:washbag', 'satellite-town-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('satellite-town-clinic:guide', 'satellite-town-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('satellite-town-youth:pads', 'satellite-town-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('satellite-town-youth:liners', 'satellite-town-youth', 'liners', 'good', 'For light days at the club.'),
  ('satellite-town-youth:tampons', 'satellite-town-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('satellite-town-youth:cup', 'satellite-town-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('satellite-town-youth:pants', 'satellite-town-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('satellite-town-youth:reusable', 'satellite-town-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('satellite-town-youth:heat', 'satellite-town-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('satellite-town-youth:wipes', 'satellite-town-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('satellite-town-youth:washbag', 'satellite-town-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('satellite-town-youth:guide', 'satellite-town-youth', 'guide', 'good', 'Answers questions they have.'),
  ('satellite-town-waterside:pads', 'satellite-town-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('satellite-town-waterside:liners', 'satellite-town-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('satellite-town-waterside:tampons', 'satellite-town-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('satellite-town-waterside:cup', 'satellite-town-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('satellite-town-waterside:pants', 'satellite-town-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('satellite-town-waterside:reusable', 'satellite-town-waterside', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('satellite-town-waterside:heat', 'satellite-town-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('satellite-town-waterside:wipes', 'satellite-town-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('satellite-town-waterside:washbag', 'satellite-town-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('satellite-town-waterside:guide', 'satellite-town-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('satellite-town-community:pads', 'satellite-town-community', 'pads', 'good', 'Something for everyone today.'),
  ('satellite-town-community:liners', 'satellite-town-community', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('satellite-town-community:tampons', 'satellite-town-community', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('satellite-town-community:cup', 'satellite-town-community', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('satellite-town-community:pants', 'satellite-town-community', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('satellite-town-community:reusable', 'satellite-town-community', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('satellite-town-community:heat', 'satellite-town-community', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('satellite-town-community:wipes', 'satellite-town-community', 'wipes', 'good', 'There is no running water to wash.'),
  ('satellite-town-community:washbag', 'satellite-town-community', 'washbag', 'good', 'Keeps reusables clean.'),
  ('satellite-town-community:guide', 'satellite-town-community', 'guide', 'good', 'Helps mothers talk to daughters.'),
  ('satellite-town-home:pads', 'satellite-town-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('satellite-town-home:liners', 'satellite-town-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('satellite-town-home:tampons', 'satellite-town-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('satellite-town-home:cup', 'satellite-town-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('satellite-town-home:pants', 'satellite-town-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('satellite-town-home:reusable', 'satellite-town-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('satellite-town-home:heat', 'satellite-town-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('satellite-town-home:wipes', 'satellite-town-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('satellite-town-home:washbag', 'satellite-town-home', 'washbag', 'good', 'A private place for her things.'),
  ('satellite-town-home:guide', 'satellite-town-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('satellite-town-training:pads', 'satellite-town-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('satellite-town-training:liners', 'satellite-town-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('satellite-town-training:tampons', 'satellite-town-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('satellite-town-training:cup', 'satellite-town-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('satellite-town-training:pants', 'satellite-town-training', 'pants', 'good', 'Last through long sessions.'),
  ('satellite-town-training:reusable', 'satellite-town-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('satellite-town-training:heat', 'satellite-town-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('satellite-town-training:wipes', 'satellite-town-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('satellite-town-training:washbag', 'satellite-town-training', 'washbag', 'good', 'Private storage at work.'),
  ('satellite-town-training:guide', 'satellite-town-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('satellite-town-shelter:pads', 'satellite-town-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('satellite-town-shelter:liners', 'satellite-town-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('satellite-town-shelter:tampons', 'satellite-town-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('satellite-town-shelter:cup', 'satellite-town-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('satellite-town-shelter:pants', 'satellite-town-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('satellite-town-shelter:reusable', 'satellite-town-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('satellite-town-shelter:heat', 'satellite-town-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('satellite-town-shelter:wipes', 'satellite-town-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('satellite-town-shelter:washbag', 'satellite-town-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('satellite-town-shelter:guide', 'satellite-town-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('festac-school:pads', 'festac-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('festac-school:liners', 'festac-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('festac-school:tampons', 'festac-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('festac-school:cup', 'festac-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('festac-school:pants', 'festac-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('festac-school:reusable', 'festac-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('festac-school:heat', 'festac-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('festac-school:wipes', 'festac-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('festac-school:washbag', 'festac-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('festac-school:guide', 'festac-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('festac-market:pads', 'festac-market', 'pads', 'good', 'Quick to change between customers.'),
  ('festac-market:liners', 'festac-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('festac-market:tampons', 'festac-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('festac-market:cup', 'festac-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('festac-market:pants', 'festac-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('festac-market:reusable', 'festac-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('festac-market:heat', 'festac-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('festac-market:wipes', 'festac-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('festac-market:washbag', 'festac-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('festac-market:guide', 'festac-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('festac-busstop:pads', 'festac-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('festac-busstop:liners', 'festac-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('festac-busstop:tampons', 'festac-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('festac-busstop:cup', 'festac-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('festac-busstop:pants', 'festac-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('festac-busstop:reusable', 'festac-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('festac-busstop:heat', 'festac-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('festac-busstop:wipes', 'festac-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('festac-busstop:washbag', 'festac-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('festac-busstop:guide', 'festac-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('festac-clinic:pads', 'festac-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('festac-clinic:liners', 'festac-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('festac-clinic:tampons', 'festac-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('festac-clinic:cup', 'festac-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('festac-clinic:pants', 'festac-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('festac-clinic:reusable', 'festac-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('festac-clinic:heat', 'festac-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('festac-clinic:wipes', 'festac-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('festac-clinic:washbag', 'festac-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('festac-clinic:guide', 'festac-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('festac-youth:pads', 'festac-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('festac-youth:liners', 'festac-youth', 'liners', 'good', 'For light days at the club.'),
  ('festac-youth:tampons', 'festac-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('festac-youth:cup', 'festac-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('festac-youth:pants', 'festac-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('festac-youth:reusable', 'festac-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('festac-youth:heat', 'festac-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('festac-youth:wipes', 'festac-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('festac-youth:washbag', 'festac-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('festac-youth:guide', 'festac-youth', 'guide', 'good', 'Answers questions they have.'),
  ('festac-hostel:pads', 'festac-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('festac-hostel:liners', 'festac-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('festac-hostel:tampons', 'festac-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('festac-hostel:cup', 'festac-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('festac-hostel:pants', 'festac-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('festac-hostel:reusable', 'festac-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('festac-hostel:heat', 'festac-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('festac-hostel:wipes', 'festac-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('festac-hostel:washbag', 'festac-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('festac-hostel:guide', 'festac-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('festac-home:pads', 'festac-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('festac-home:liners', 'festac-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('festac-home:tampons', 'festac-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('festac-home:cup', 'festac-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('festac-home:pants', 'festac-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('festac-home:reusable', 'festac-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('festac-home:heat', 'festac-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('festac-home:wipes', 'festac-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('festac-home:washbag', 'festac-home', 'washbag', 'good', 'A private place for her things.'),
  ('festac-home:guide', 'festac-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('festac-football:pads', 'festac-football', 'pads', 'good', 'A simple backup for match day.'),
  ('festac-football:liners', 'festac-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('festac-football:tampons', 'festac-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('festac-football:cup', 'festac-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('festac-football:pants', 'festac-football', 'pants', 'good', 'Stay put while running.'),
  ('festac-football:reusable', 'festac-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('festac-football:heat', 'festac-football', 'heat', 'good', 'Helps cramps after training.'),
  ('festac-football:wipes', 'festac-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('festac-football:washbag', 'festac-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('festac-football:guide', 'festac-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('festac-training:pads', 'festac-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('festac-training:liners', 'festac-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('festac-training:tampons', 'festac-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('festac-training:cup', 'festac-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('festac-training:pants', 'festac-training', 'pants', 'good', 'Last through long sessions.'),
  ('festac-training:reusable', 'festac-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('festac-training:heat', 'festac-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('festac-training:wipes', 'festac-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('festac-training:washbag', 'festac-training', 'washbag', 'good', 'Private storage at work.'),
  ('festac-training:guide', 'festac-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('festac-shelter:pads', 'festac-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('festac-shelter:liners', 'festac-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('festac-shelter:tampons', 'festac-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('festac-shelter:cup', 'festac-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('festac-shelter:pants', 'festac-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('festac-shelter:reusable', 'festac-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('festac-shelter:heat', 'festac-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('festac-shelter:wipes', 'festac-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('festac-shelter:washbag', 'festac-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('festac-shelter:guide', 'festac-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('amuwo-school:pads', 'amuwo-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('amuwo-school:liners', 'amuwo-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('amuwo-school:tampons', 'amuwo-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('amuwo-school:cup', 'amuwo-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('amuwo-school:pants', 'amuwo-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('amuwo-school:reusable', 'amuwo-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('amuwo-school:heat', 'amuwo-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('amuwo-school:wipes', 'amuwo-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('amuwo-school:washbag', 'amuwo-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('amuwo-school:guide', 'amuwo-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('amuwo-market:pads', 'amuwo-market', 'pads', 'good', 'Quick to change between customers.'),
  ('amuwo-market:liners', 'amuwo-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('amuwo-market:tampons', 'amuwo-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('amuwo-market:cup', 'amuwo-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('amuwo-market:pants', 'amuwo-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('amuwo-market:reusable', 'amuwo-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('amuwo-market:heat', 'amuwo-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('amuwo-market:wipes', 'amuwo-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('amuwo-market:washbag', 'amuwo-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('amuwo-market:guide', 'amuwo-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('amuwo-busstop:pads', 'amuwo-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('amuwo-busstop:liners', 'amuwo-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('amuwo-busstop:tampons', 'amuwo-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('amuwo-busstop:cup', 'amuwo-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('amuwo-busstop:pants', 'amuwo-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('amuwo-busstop:reusable', 'amuwo-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('amuwo-busstop:heat', 'amuwo-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('amuwo-busstop:wipes', 'amuwo-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('amuwo-busstop:washbag', 'amuwo-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('amuwo-busstop:guide', 'amuwo-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('amuwo-clinic:pads', 'amuwo-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('amuwo-clinic:liners', 'amuwo-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('amuwo-clinic:tampons', 'amuwo-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('amuwo-clinic:cup', 'amuwo-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('amuwo-clinic:pants', 'amuwo-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('amuwo-clinic:reusable', 'amuwo-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('amuwo-clinic:heat', 'amuwo-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('amuwo-clinic:wipes', 'amuwo-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('amuwo-clinic:washbag', 'amuwo-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('amuwo-clinic:guide', 'amuwo-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('amuwo-youth:pads', 'amuwo-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('amuwo-youth:liners', 'amuwo-youth', 'liners', 'good', 'For light days at the club.'),
  ('amuwo-youth:tampons', 'amuwo-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('amuwo-youth:cup', 'amuwo-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('amuwo-youth:pants', 'amuwo-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('amuwo-youth:reusable', 'amuwo-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('amuwo-youth:heat', 'amuwo-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('amuwo-youth:wipes', 'amuwo-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('amuwo-youth:washbag', 'amuwo-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('amuwo-youth:guide', 'amuwo-youth', 'guide', 'good', 'Answers questions they have.'),
  ('amuwo-hostel:pads', 'amuwo-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('amuwo-hostel:liners', 'amuwo-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('amuwo-hostel:tampons', 'amuwo-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('amuwo-hostel:cup', 'amuwo-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('amuwo-hostel:pants', 'amuwo-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('amuwo-hostel:reusable', 'amuwo-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('amuwo-hostel:heat', 'amuwo-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('amuwo-hostel:wipes', 'amuwo-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('amuwo-hostel:washbag', 'amuwo-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('amuwo-hostel:guide', 'amuwo-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('amuwo-home:pads', 'amuwo-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('amuwo-home:liners', 'amuwo-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('amuwo-home:tampons', 'amuwo-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('amuwo-home:cup', 'amuwo-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('amuwo-home:pants', 'amuwo-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('amuwo-home:reusable', 'amuwo-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('amuwo-home:heat', 'amuwo-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('amuwo-home:wipes', 'amuwo-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('amuwo-home:washbag', 'amuwo-home', 'washbag', 'good', 'A private place for her things.'),
  ('amuwo-home:guide', 'amuwo-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('amuwo-football:pads', 'amuwo-football', 'pads', 'good', 'A simple backup for match day.'),
  ('amuwo-football:liners', 'amuwo-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('amuwo-football:tampons', 'amuwo-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('amuwo-football:cup', 'amuwo-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('amuwo-football:pants', 'amuwo-football', 'pants', 'good', 'Stay put while running.'),
  ('amuwo-football:reusable', 'amuwo-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('amuwo-football:heat', 'amuwo-football', 'heat', 'good', 'Helps cramps after training.'),
  ('amuwo-football:wipes', 'amuwo-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('amuwo-football:washbag', 'amuwo-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('amuwo-football:guide', 'amuwo-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('amuwo-training:pads', 'amuwo-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('amuwo-training:liners', 'amuwo-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('amuwo-training:tampons', 'amuwo-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('amuwo-training:cup', 'amuwo-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('amuwo-training:pants', 'amuwo-training', 'pants', 'good', 'Last through long sessions.'),
  ('amuwo-training:reusable', 'amuwo-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('amuwo-training:heat', 'amuwo-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('amuwo-training:wipes', 'amuwo-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('amuwo-training:washbag', 'amuwo-training', 'washbag', 'good', 'Private storage at work.'),
  ('amuwo-training:guide', 'amuwo-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('mile-2-school:pads', 'mile-2-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('mile-2-school:liners', 'mile-2-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('mile-2-school:tampons', 'mile-2-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-2-school:cup', 'mile-2-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('mile-2-school:pants', 'mile-2-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('mile-2-school:reusable', 'mile-2-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('mile-2-school:heat', 'mile-2-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('mile-2-school:wipes', 'mile-2-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('mile-2-school:washbag', 'mile-2-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('mile-2-school:guide', 'mile-2-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('mile-2-market:pads', 'mile-2-market', 'pads', 'good', 'Quick to change between customers.'),
  ('mile-2-market:liners', 'mile-2-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mile-2-market:tampons', 'mile-2-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-2-market:cup', 'mile-2-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('mile-2-market:pants', 'mile-2-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('mile-2-market:reusable', 'mile-2-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('mile-2-market:heat', 'mile-2-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('mile-2-market:wipes', 'mile-2-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('mile-2-market:washbag', 'mile-2-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('mile-2-market:guide', 'mile-2-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('mile-2-busstop:pads', 'mile-2-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('mile-2-busstop:liners', 'mile-2-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mile-2-busstop:tampons', 'mile-2-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-2-busstop:cup', 'mile-2-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('mile-2-busstop:pants', 'mile-2-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('mile-2-busstop:reusable', 'mile-2-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('mile-2-busstop:heat', 'mile-2-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('mile-2-busstop:wipes', 'mile-2-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('mile-2-busstop:washbag', 'mile-2-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('mile-2-busstop:guide', 'mile-2-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('mile-2-clinic:pads', 'mile-2-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('mile-2-clinic:liners', 'mile-2-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mile-2-clinic:tampons', 'mile-2-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-2-clinic:cup', 'mile-2-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('mile-2-clinic:pants', 'mile-2-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('mile-2-clinic:reusable', 'mile-2-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('mile-2-clinic:heat', 'mile-2-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('mile-2-clinic:wipes', 'mile-2-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('mile-2-clinic:washbag', 'mile-2-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('mile-2-clinic:guide', 'mile-2-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('mile-2-youth:pads', 'mile-2-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('mile-2-youth:liners', 'mile-2-youth', 'liners', 'good', 'For light days at the club.'),
  ('mile-2-youth:tampons', 'mile-2-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-2-youth:cup', 'mile-2-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('mile-2-youth:pants', 'mile-2-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('mile-2-youth:reusable', 'mile-2-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('mile-2-youth:heat', 'mile-2-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('mile-2-youth:wipes', 'mile-2-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('mile-2-youth:washbag', 'mile-2-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('mile-2-youth:guide', 'mile-2-youth', 'guide', 'good', 'Answers questions they have.'),
  ('mile-2-hostel:pads', 'mile-2-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('mile-2-hostel:liners', 'mile-2-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mile-2-hostel:tampons', 'mile-2-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-2-hostel:cup', 'mile-2-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('mile-2-hostel:pants', 'mile-2-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('mile-2-hostel:reusable', 'mile-2-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('mile-2-hostel:heat', 'mile-2-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('mile-2-hostel:wipes', 'mile-2-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('mile-2-hostel:washbag', 'mile-2-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('mile-2-hostel:guide', 'mile-2-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('mile-2-home:pads', 'mile-2-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('mile-2-home:liners', 'mile-2-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mile-2-home:tampons', 'mile-2-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-2-home:cup', 'mile-2-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('mile-2-home:pants', 'mile-2-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('mile-2-home:reusable', 'mile-2-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('mile-2-home:heat', 'mile-2-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('mile-2-home:wipes', 'mile-2-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('mile-2-home:washbag', 'mile-2-home', 'washbag', 'good', 'A private place for her things.'),
  ('mile-2-home:guide', 'mile-2-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('mile-2-football:pads', 'mile-2-football', 'pads', 'good', 'A simple backup for match day.'),
  ('mile-2-football:liners', 'mile-2-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mile-2-football:tampons', 'mile-2-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-2-football:cup', 'mile-2-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('mile-2-football:pants', 'mile-2-football', 'pants', 'good', 'Stay put while running.'),
  ('mile-2-football:reusable', 'mile-2-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('mile-2-football:heat', 'mile-2-football', 'heat', 'good', 'Helps cramps after training.'),
  ('mile-2-football:wipes', 'mile-2-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('mile-2-football:washbag', 'mile-2-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('mile-2-football:guide', 'mile-2-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('mile-2-training:pads', 'mile-2-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('mile-2-training:liners', 'mile-2-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mile-2-training:tampons', 'mile-2-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-2-training:cup', 'mile-2-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('mile-2-training:pants', 'mile-2-training', 'pants', 'good', 'Last through long sessions.'),
  ('mile-2-training:reusable', 'mile-2-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('mile-2-training:heat', 'mile-2-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('mile-2-training:wipes', 'mile-2-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('mile-2-training:washbag', 'mile-2-training', 'washbag', 'good', 'Private storage at work.'),
  ('mile-2-training:guide', 'mile-2-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('mile-2-shelter:pads', 'mile-2-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('mile-2-shelter:liners', 'mile-2-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mile-2-shelter:tampons', 'mile-2-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-2-shelter:cup', 'mile-2-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('mile-2-shelter:pants', 'mile-2-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('mile-2-shelter:reusable', 'mile-2-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('mile-2-shelter:heat', 'mile-2-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('mile-2-shelter:wipes', 'mile-2-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('mile-2-shelter:washbag', 'mile-2-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('mile-2-shelter:guide', 'mile-2-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ajegunle-school:pads', 'ajegunle-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ajegunle-school:liners', 'ajegunle-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ajegunle-school:tampons', 'ajegunle-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajegunle-school:cup', 'ajegunle-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ajegunle-school:pants', 'ajegunle-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ajegunle-school:reusable', 'ajegunle-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ajegunle-school:heat', 'ajegunle-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ajegunle-school:wipes', 'ajegunle-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('ajegunle-school:washbag', 'ajegunle-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ajegunle-school:guide', 'ajegunle-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ajegunle-market:pads', 'ajegunle-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ajegunle-market:liners', 'ajegunle-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajegunle-market:tampons', 'ajegunle-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajegunle-market:cup', 'ajegunle-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ajegunle-market:pants', 'ajegunle-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ajegunle-market:reusable', 'ajegunle-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ajegunle-market:heat', 'ajegunle-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ajegunle-market:wipes', 'ajegunle-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ajegunle-market:washbag', 'ajegunle-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ajegunle-market:guide', 'ajegunle-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ajegunle-busstop:pads', 'ajegunle-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ajegunle-busstop:liners', 'ajegunle-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajegunle-busstop:tampons', 'ajegunle-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajegunle-busstop:cup', 'ajegunle-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ajegunle-busstop:pants', 'ajegunle-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ajegunle-busstop:reusable', 'ajegunle-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ajegunle-busstop:heat', 'ajegunle-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ajegunle-busstop:wipes', 'ajegunle-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ajegunle-busstop:washbag', 'ajegunle-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ajegunle-busstop:guide', 'ajegunle-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ajegunle-clinic:pads', 'ajegunle-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ajegunle-clinic:liners', 'ajegunle-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajegunle-clinic:tampons', 'ajegunle-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajegunle-clinic:cup', 'ajegunle-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ajegunle-clinic:pants', 'ajegunle-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ajegunle-clinic:reusable', 'ajegunle-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ajegunle-clinic:heat', 'ajegunle-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ajegunle-clinic:wipes', 'ajegunle-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ajegunle-clinic:washbag', 'ajegunle-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ajegunle-clinic:guide', 'ajegunle-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ajegunle-youth:pads', 'ajegunle-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ajegunle-youth:liners', 'ajegunle-youth', 'liners', 'good', 'For light days at the club.'),
  ('ajegunle-youth:tampons', 'ajegunle-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajegunle-youth:cup', 'ajegunle-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ajegunle-youth:pants', 'ajegunle-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ajegunle-youth:reusable', 'ajegunle-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ajegunle-youth:heat', 'ajegunle-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ajegunle-youth:wipes', 'ajegunle-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ajegunle-youth:washbag', 'ajegunle-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ajegunle-youth:guide', 'ajegunle-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ajegunle-waterside:pads', 'ajegunle-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('ajegunle-waterside:liners', 'ajegunle-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajegunle-waterside:tampons', 'ajegunle-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('ajegunle-waterside:cup', 'ajegunle-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ajegunle-waterside:pants', 'ajegunle-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('ajegunle-waterside:reusable', 'ajegunle-waterside', 'reusable', 'good', 'They last a year when the shop is far.'),
  ('ajegunle-waterside:heat', 'ajegunle-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ajegunle-waterside:wipes', 'ajegunle-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ajegunle-waterside:washbag', 'ajegunle-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('ajegunle-waterside:guide', 'ajegunle-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('ajegunle-hostel:pads', 'ajegunle-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('ajegunle-hostel:liners', 'ajegunle-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajegunle-hostel:tampons', 'ajegunle-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajegunle-hostel:cup', 'ajegunle-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ajegunle-hostel:pants', 'ajegunle-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ajegunle-hostel:reusable', 'ajegunle-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('ajegunle-hostel:heat', 'ajegunle-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ajegunle-hostel:wipes', 'ajegunle-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ajegunle-hostel:washbag', 'ajegunle-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('ajegunle-hostel:guide', 'ajegunle-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('ajegunle-football:pads', 'ajegunle-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ajegunle-football:liners', 'ajegunle-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajegunle-football:tampons', 'ajegunle-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajegunle-football:cup', 'ajegunle-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ajegunle-football:pants', 'ajegunle-football', 'pants', 'good', 'Stay put while running.'),
  ('ajegunle-football:reusable', 'ajegunle-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ajegunle-football:heat', 'ajegunle-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ajegunle-football:wipes', 'ajegunle-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ajegunle-football:washbag', 'ajegunle-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ajegunle-football:guide', 'ajegunle-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ajegunle-training:pads', 'ajegunle-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ajegunle-training:liners', 'ajegunle-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajegunle-training:tampons', 'ajegunle-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajegunle-training:cup', 'ajegunle-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ajegunle-training:pants', 'ajegunle-training', 'pants', 'good', 'Last through long sessions.'),
  ('ajegunle-training:reusable', 'ajegunle-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ajegunle-training:heat', 'ajegunle-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ajegunle-training:wipes', 'ajegunle-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ajegunle-training:washbag', 'ajegunle-training', 'washbag', 'good', 'Private storage at work.'),
  ('ajegunle-training:guide', 'ajegunle-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ajegunle-shelter:pads', 'ajegunle-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('ajegunle-shelter:liners', 'ajegunle-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajegunle-shelter:tampons', 'ajegunle-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajegunle-shelter:cup', 'ajegunle-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ajegunle-shelter:pants', 'ajegunle-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ajegunle-shelter:reusable', 'ajegunle-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ajegunle-shelter:heat', 'ajegunle-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('ajegunle-shelter:wipes', 'ajegunle-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('ajegunle-shelter:washbag', 'ajegunle-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('ajegunle-shelter:guide', 'ajegunle-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('apapa-school:pads', 'apapa-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('apapa-school:liners', 'apapa-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('apapa-school:tampons', 'apapa-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('apapa-school:cup', 'apapa-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('apapa-school:pants', 'apapa-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('apapa-school:reusable', 'apapa-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('apapa-school:heat', 'apapa-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('apapa-school:wipes', 'apapa-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('apapa-school:washbag', 'apapa-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('apapa-school:guide', 'apapa-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('apapa-market:pads', 'apapa-market', 'pads', 'good', 'Quick to change between customers.'),
  ('apapa-market:liners', 'apapa-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('apapa-market:tampons', 'apapa-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('apapa-market:cup', 'apapa-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('apapa-market:pants', 'apapa-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('apapa-market:reusable', 'apapa-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('apapa-market:heat', 'apapa-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('apapa-market:wipes', 'apapa-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('apapa-market:washbag', 'apapa-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('apapa-market:guide', 'apapa-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('apapa-busstop:pads', 'apapa-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('apapa-busstop:liners', 'apapa-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('apapa-busstop:tampons', 'apapa-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('apapa-busstop:cup', 'apapa-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('apapa-busstop:pants', 'apapa-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('apapa-busstop:reusable', 'apapa-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('apapa-busstop:heat', 'apapa-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('apapa-busstop:wipes', 'apapa-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('apapa-busstop:washbag', 'apapa-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('apapa-busstop:guide', 'apapa-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('apapa-clinic:pads', 'apapa-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('apapa-clinic:liners', 'apapa-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('apapa-clinic:tampons', 'apapa-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('apapa-clinic:cup', 'apapa-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('apapa-clinic:pants', 'apapa-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('apapa-clinic:reusable', 'apapa-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('apapa-clinic:heat', 'apapa-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('apapa-clinic:wipes', 'apapa-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('apapa-clinic:washbag', 'apapa-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('apapa-clinic:guide', 'apapa-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('apapa-youth:pads', 'apapa-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('apapa-youth:liners', 'apapa-youth', 'liners', 'good', 'For light days at the club.'),
  ('apapa-youth:tampons', 'apapa-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('apapa-youth:cup', 'apapa-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('apapa-youth:pants', 'apapa-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('apapa-youth:reusable', 'apapa-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('apapa-youth:heat', 'apapa-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('apapa-youth:wipes', 'apapa-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('apapa-youth:washbag', 'apapa-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('apapa-youth:guide', 'apapa-youth', 'guide', 'good', 'Answers questions they have.'),
  ('apapa-waterside:pads', 'apapa-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('apapa-waterside:liners', 'apapa-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('apapa-waterside:tampons', 'apapa-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('apapa-waterside:cup', 'apapa-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('apapa-waterside:pants', 'apapa-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('apapa-waterside:reusable', 'apapa-waterside', 'reusable', 'good', 'They last a year when the shop is far.'),
  ('apapa-waterside:heat', 'apapa-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('apapa-waterside:wipes', 'apapa-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('apapa-waterside:washbag', 'apapa-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('apapa-waterside:guide', 'apapa-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('apapa-hostel:pads', 'apapa-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('apapa-hostel:liners', 'apapa-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('apapa-hostel:tampons', 'apapa-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('apapa-hostel:cup', 'apapa-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('apapa-hostel:pants', 'apapa-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('apapa-hostel:reusable', 'apapa-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('apapa-hostel:heat', 'apapa-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('apapa-hostel:wipes', 'apapa-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('apapa-hostel:washbag', 'apapa-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('apapa-hostel:guide', 'apapa-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('apapa-football:pads', 'apapa-football', 'pads', 'good', 'A simple backup for match day.'),
  ('apapa-football:liners', 'apapa-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('apapa-football:tampons', 'apapa-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('apapa-football:cup', 'apapa-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('apapa-football:pants', 'apapa-football', 'pants', 'good', 'Stay put while running.'),
  ('apapa-football:reusable', 'apapa-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('apapa-football:heat', 'apapa-football', 'heat', 'good', 'Helps cramps after training.'),
  ('apapa-football:wipes', 'apapa-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('apapa-football:washbag', 'apapa-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('apapa-football:guide', 'apapa-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('apapa-training:pads', 'apapa-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('apapa-training:liners', 'apapa-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('apapa-training:tampons', 'apapa-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('apapa-training:cup', 'apapa-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('apapa-training:pants', 'apapa-training', 'pants', 'good', 'Last through long sessions.'),
  ('apapa-training:reusable', 'apapa-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('apapa-training:heat', 'apapa-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('apapa-training:wipes', 'apapa-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('apapa-training:washbag', 'apapa-training', 'washbag', 'good', 'Private storage at work.'),
  ('apapa-training:guide', 'apapa-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('orile-school:pads', 'orile-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('orile-school:liners', 'orile-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('orile-school:tampons', 'orile-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('orile-school:cup', 'orile-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('orile-school:pants', 'orile-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('orile-school:reusable', 'orile-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('orile-school:heat', 'orile-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('orile-school:wipes', 'orile-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('orile-school:washbag', 'orile-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('orile-school:guide', 'orile-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('orile-market:pads', 'orile-market', 'pads', 'good', 'Quick to change between customers.'),
  ('orile-market:liners', 'orile-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('orile-market:tampons', 'orile-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('orile-market:cup', 'orile-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('orile-market:pants', 'orile-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('orile-market:reusable', 'orile-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('orile-market:heat', 'orile-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('orile-market:wipes', 'orile-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('orile-market:washbag', 'orile-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('orile-market:guide', 'orile-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('orile-busstop:pads', 'orile-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('orile-busstop:liners', 'orile-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('orile-busstop:tampons', 'orile-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('orile-busstop:cup', 'orile-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('orile-busstop:pants', 'orile-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('orile-busstop:reusable', 'orile-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('orile-busstop:heat', 'orile-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('orile-busstop:wipes', 'orile-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('orile-busstop:washbag', 'orile-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('orile-busstop:guide', 'orile-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('orile-clinic:pads', 'orile-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('orile-clinic:liners', 'orile-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('orile-clinic:tampons', 'orile-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('orile-clinic:cup', 'orile-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('orile-clinic:pants', 'orile-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('orile-clinic:reusable', 'orile-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('orile-clinic:heat', 'orile-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('orile-clinic:wipes', 'orile-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('orile-clinic:washbag', 'orile-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('orile-clinic:guide', 'orile-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('orile-youth:pads', 'orile-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('orile-youth:liners', 'orile-youth', 'liners', 'good', 'For light days at the club.'),
  ('orile-youth:tampons', 'orile-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('orile-youth:cup', 'orile-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('orile-youth:pants', 'orile-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('orile-youth:reusable', 'orile-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('orile-youth:heat', 'orile-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('orile-youth:wipes', 'orile-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('orile-youth:washbag', 'orile-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('orile-youth:guide', 'orile-youth', 'guide', 'good', 'Answers questions they have.'),
  ('orile-hostel:pads', 'orile-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('orile-hostel:liners', 'orile-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('orile-hostel:tampons', 'orile-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('orile-hostel:cup', 'orile-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('orile-hostel:pants', 'orile-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('orile-hostel:reusable', 'orile-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('orile-hostel:heat', 'orile-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('orile-hostel:wipes', 'orile-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('orile-hostel:washbag', 'orile-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('orile-hostel:guide', 'orile-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('orile-home:pads', 'orile-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('orile-home:liners', 'orile-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('orile-home:tampons', 'orile-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('orile-home:cup', 'orile-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('orile-home:pants', 'orile-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('orile-home:reusable', 'orile-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('orile-home:heat', 'orile-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('orile-home:wipes', 'orile-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('orile-home:washbag', 'orile-home', 'washbag', 'good', 'A private place for her things.'),
  ('orile-home:guide', 'orile-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('orile-football:pads', 'orile-football', 'pads', 'good', 'A simple backup for match day.'),
  ('orile-football:liners', 'orile-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('orile-football:tampons', 'orile-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('orile-football:cup', 'orile-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('orile-football:pants', 'orile-football', 'pants', 'good', 'Stay put while running.'),
  ('orile-football:reusable', 'orile-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('orile-football:heat', 'orile-football', 'heat', 'good', 'Helps cramps after training.'),
  ('orile-football:wipes', 'orile-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('orile-football:washbag', 'orile-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('orile-football:guide', 'orile-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('orile-training:pads', 'orile-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('orile-training:liners', 'orile-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('orile-training:tampons', 'orile-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('orile-training:cup', 'orile-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('orile-training:pants', 'orile-training', 'pants', 'good', 'Last through long sessions.'),
  ('orile-training:reusable', 'orile-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('orile-training:heat', 'orile-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('orile-training:wipes', 'orile-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('orile-training:washbag', 'orile-training', 'washbag', 'good', 'Private storage at work.'),
  ('orile-training:guide', 'orile-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('orile-shelter:pads', 'orile-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('orile-shelter:liners', 'orile-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('orile-shelter:tampons', 'orile-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('orile-shelter:cup', 'orile-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('orile-shelter:pants', 'orile-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('orile-shelter:reusable', 'orile-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('orile-shelter:heat', 'orile-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('orile-shelter:wipes', 'orile-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('orile-shelter:washbag', 'orile-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('orile-shelter:guide', 'orile-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ijora-school:pads', 'ijora-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ijora-school:liners', 'ijora-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ijora-school:tampons', 'ijora-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ijora-school:cup', 'ijora-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ijora-school:pants', 'ijora-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ijora-school:reusable', 'ijora-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ijora-school:heat', 'ijora-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ijora-school:wipes', 'ijora-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('ijora-school:washbag', 'ijora-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ijora-school:guide', 'ijora-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ijora-market:pads', 'ijora-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ijora-market:liners', 'ijora-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ijora-market:tampons', 'ijora-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ijora-market:cup', 'ijora-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ijora-market:pants', 'ijora-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ijora-market:reusable', 'ijora-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ijora-market:heat', 'ijora-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ijora-market:wipes', 'ijora-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ijora-market:washbag', 'ijora-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ijora-market:guide', 'ijora-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ijora-busstop:pads', 'ijora-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ijora-busstop:liners', 'ijora-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ijora-busstop:tampons', 'ijora-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ijora-busstop:cup', 'ijora-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ijora-busstop:pants', 'ijora-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ijora-busstop:reusable', 'ijora-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ijora-busstop:heat', 'ijora-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ijora-busstop:wipes', 'ijora-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ijora-busstop:washbag', 'ijora-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ijora-busstop:guide', 'ijora-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ijora-clinic:pads', 'ijora-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ijora-clinic:liners', 'ijora-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ijora-clinic:tampons', 'ijora-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ijora-clinic:cup', 'ijora-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ijora-clinic:pants', 'ijora-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ijora-clinic:reusable', 'ijora-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ijora-clinic:heat', 'ijora-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ijora-clinic:wipes', 'ijora-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ijora-clinic:washbag', 'ijora-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ijora-clinic:guide', 'ijora-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ijora-youth:pads', 'ijora-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ijora-youth:liners', 'ijora-youth', 'liners', 'good', 'For light days at the club.'),
  ('ijora-youth:tampons', 'ijora-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ijora-youth:cup', 'ijora-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ijora-youth:pants', 'ijora-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ijora-youth:reusable', 'ijora-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ijora-youth:heat', 'ijora-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ijora-youth:wipes', 'ijora-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ijora-youth:washbag', 'ijora-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ijora-youth:guide', 'ijora-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ijora-waterside:pads', 'ijora-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('ijora-waterside:liners', 'ijora-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ijora-waterside:tampons', 'ijora-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('ijora-waterside:cup', 'ijora-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ijora-waterside:pants', 'ijora-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('ijora-waterside:reusable', 'ijora-waterside', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ijora-waterside:heat', 'ijora-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ijora-waterside:wipes', 'ijora-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ijora-waterside:washbag', 'ijora-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('ijora-waterside:guide', 'ijora-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('ijora-hostel:pads', 'ijora-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('ijora-hostel:liners', 'ijora-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ijora-hostel:tampons', 'ijora-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ijora-hostel:cup', 'ijora-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ijora-hostel:pants', 'ijora-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ijora-hostel:reusable', 'ijora-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('ijora-hostel:heat', 'ijora-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ijora-hostel:wipes', 'ijora-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ijora-hostel:washbag', 'ijora-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('ijora-hostel:guide', 'ijora-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('ijora-football:pads', 'ijora-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ijora-football:liners', 'ijora-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ijora-football:tampons', 'ijora-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ijora-football:cup', 'ijora-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ijora-football:pants', 'ijora-football', 'pants', 'good', 'Stay put while running.'),
  ('ijora-football:reusable', 'ijora-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ijora-football:heat', 'ijora-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ijora-football:wipes', 'ijora-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ijora-football:washbag', 'ijora-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ijora-football:guide', 'ijora-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ijora-training:pads', 'ijora-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ijora-training:liners', 'ijora-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ijora-training:tampons', 'ijora-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ijora-training:cup', 'ijora-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ijora-training:pants', 'ijora-training', 'pants', 'good', 'Last through long sessions.'),
  ('ijora-training:reusable', 'ijora-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ijora-training:heat', 'ijora-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ijora-training:wipes', 'ijora-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ijora-training:washbag', 'ijora-training', 'washbag', 'good', 'Private storage at work.'),
  ('ijora-training:guide', 'ijora-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ijora-shelter:pads', 'ijora-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('ijora-shelter:liners', 'ijora-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ijora-shelter:tampons', 'ijora-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ijora-shelter:cup', 'ijora-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ijora-shelter:pants', 'ijora-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ijora-shelter:reusable', 'ijora-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ijora-shelter:heat', 'ijora-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('ijora-shelter:wipes', 'ijora-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('ijora-shelter:washbag', 'ijora-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('ijora-shelter:guide', 'ijora-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('surulere-school:pads', 'surulere-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('surulere-school:liners', 'surulere-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('surulere-school:tampons', 'surulere-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('surulere-school:cup', 'surulere-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('surulere-school:pants', 'surulere-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('surulere-school:reusable', 'surulere-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('surulere-school:heat', 'surulere-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('surulere-school:wipes', 'surulere-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('surulere-school:washbag', 'surulere-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('surulere-school:guide', 'surulere-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('surulere-market:pads', 'surulere-market', 'pads', 'good', 'Quick to change between customers.'),
  ('surulere-market:liners', 'surulere-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('surulere-market:tampons', 'surulere-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('surulere-market:cup', 'surulere-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('surulere-market:pants', 'surulere-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('surulere-market:reusable', 'surulere-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('surulere-market:heat', 'surulere-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('surulere-market:wipes', 'surulere-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('surulere-market:washbag', 'surulere-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('surulere-market:guide', 'surulere-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('surulere-busstop:pads', 'surulere-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('surulere-busstop:liners', 'surulere-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('surulere-busstop:tampons', 'surulere-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('surulere-busstop:cup', 'surulere-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('surulere-busstop:pants', 'surulere-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('surulere-busstop:reusable', 'surulere-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('surulere-busstop:heat', 'surulere-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('surulere-busstop:wipes', 'surulere-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('surulere-busstop:washbag', 'surulere-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('surulere-busstop:guide', 'surulere-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('surulere-clinic:pads', 'surulere-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('surulere-clinic:liners', 'surulere-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('surulere-clinic:tampons', 'surulere-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('surulere-clinic:cup', 'surulere-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('surulere-clinic:pants', 'surulere-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('surulere-clinic:reusable', 'surulere-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('surulere-clinic:heat', 'surulere-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('surulere-clinic:wipes', 'surulere-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('surulere-clinic:washbag', 'surulere-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('surulere-clinic:guide', 'surulere-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('surulere-youth:pads', 'surulere-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('surulere-youth:liners', 'surulere-youth', 'liners', 'good', 'For light days at the club.'),
  ('surulere-youth:tampons', 'surulere-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('surulere-youth:cup', 'surulere-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('surulere-youth:pants', 'surulere-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('surulere-youth:reusable', 'surulere-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('surulere-youth:heat', 'surulere-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('surulere-youth:wipes', 'surulere-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('surulere-youth:washbag', 'surulere-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('surulere-youth:guide', 'surulere-youth', 'guide', 'good', 'Answers questions they have.'),
  ('surulere-hostel:pads', 'surulere-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('surulere-hostel:liners', 'surulere-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('surulere-hostel:tampons', 'surulere-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('surulere-hostel:cup', 'surulere-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('surulere-hostel:pants', 'surulere-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('surulere-hostel:reusable', 'surulere-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('surulere-hostel:heat', 'surulere-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('surulere-hostel:wipes', 'surulere-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('surulere-hostel:washbag', 'surulere-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('surulere-hostel:guide', 'surulere-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('surulere-home:pads', 'surulere-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('surulere-home:liners', 'surulere-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('surulere-home:tampons', 'surulere-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('surulere-home:cup', 'surulere-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('surulere-home:pants', 'surulere-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('surulere-home:reusable', 'surulere-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('surulere-home:heat', 'surulere-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('surulere-home:wipes', 'surulere-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('surulere-home:washbag', 'surulere-home', 'washbag', 'good', 'A private place for her things.'),
  ('surulere-home:guide', 'surulere-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('surulere-football:pads', 'surulere-football', 'pads', 'good', 'A simple backup for match day.'),
  ('surulere-football:liners', 'surulere-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('surulere-football:tampons', 'surulere-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('surulere-football:cup', 'surulere-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('surulere-football:pants', 'surulere-football', 'pants', 'good', 'Stay put while running.'),
  ('surulere-football:reusable', 'surulere-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('surulere-football:heat', 'surulere-football', 'heat', 'good', 'Helps cramps after training.'),
  ('surulere-football:wipes', 'surulere-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('surulere-football:washbag', 'surulere-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('surulere-football:guide', 'surulere-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('surulere-training:pads', 'surulere-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('surulere-training:liners', 'surulere-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('surulere-training:tampons', 'surulere-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('surulere-training:cup', 'surulere-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('surulere-training:pants', 'surulere-training', 'pants', 'good', 'Last through long sessions.'),
  ('surulere-training:reusable', 'surulere-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('surulere-training:heat', 'surulere-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('surulere-training:wipes', 'surulere-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('surulere-training:washbag', 'surulere-training', 'washbag', 'good', 'Private storage at work.'),
  ('surulere-training:guide', 'surulere-training', 'guide', 'ok', 'Helpful, but not the main need here.');

INSERT INTO giveday_needs (id, place_id, product_id, rating, why) VALUES
  ('surulere-shelter:pads', 'surulere-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('surulere-shelter:liners', 'surulere-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('surulere-shelter:tampons', 'surulere-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('surulere-shelter:cup', 'surulere-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('surulere-shelter:pants', 'surulere-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('surulere-shelter:reusable', 'surulere-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('surulere-shelter:heat', 'surulere-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('surulere-shelter:wipes', 'surulere-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('surulere-shelter:washbag', 'surulere-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('surulere-shelter:guide', 'surulere-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ebute-metta-school:pads', 'ebute-metta-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ebute-metta-school:liners', 'ebute-metta-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ebute-metta-school:tampons', 'ebute-metta-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ebute-metta-school:cup', 'ebute-metta-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ebute-metta-school:pants', 'ebute-metta-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ebute-metta-school:reusable', 'ebute-metta-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ebute-metta-school:heat', 'ebute-metta-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ebute-metta-school:wipes', 'ebute-metta-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('ebute-metta-school:washbag', 'ebute-metta-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ebute-metta-school:guide', 'ebute-metta-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ebute-metta-market:pads', 'ebute-metta-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ebute-metta-market:liners', 'ebute-metta-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ebute-metta-market:tampons', 'ebute-metta-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ebute-metta-market:cup', 'ebute-metta-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ebute-metta-market:pants', 'ebute-metta-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ebute-metta-market:reusable', 'ebute-metta-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ebute-metta-market:heat', 'ebute-metta-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ebute-metta-market:wipes', 'ebute-metta-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ebute-metta-market:washbag', 'ebute-metta-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ebute-metta-market:guide', 'ebute-metta-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ebute-metta-busstop:pads', 'ebute-metta-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ebute-metta-busstop:liners', 'ebute-metta-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ebute-metta-busstop:tampons', 'ebute-metta-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ebute-metta-busstop:cup', 'ebute-metta-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ebute-metta-busstop:pants', 'ebute-metta-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ebute-metta-busstop:reusable', 'ebute-metta-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ebute-metta-busstop:heat', 'ebute-metta-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ebute-metta-busstop:wipes', 'ebute-metta-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ebute-metta-busstop:washbag', 'ebute-metta-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ebute-metta-busstop:guide', 'ebute-metta-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ebute-metta-clinic:pads', 'ebute-metta-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ebute-metta-clinic:liners', 'ebute-metta-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ebute-metta-clinic:tampons', 'ebute-metta-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ebute-metta-clinic:cup', 'ebute-metta-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ebute-metta-clinic:pants', 'ebute-metta-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ebute-metta-clinic:reusable', 'ebute-metta-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ebute-metta-clinic:heat', 'ebute-metta-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ebute-metta-clinic:wipes', 'ebute-metta-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ebute-metta-clinic:washbag', 'ebute-metta-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ebute-metta-clinic:guide', 'ebute-metta-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ebute-metta-youth:pads', 'ebute-metta-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ebute-metta-youth:liners', 'ebute-metta-youth', 'liners', 'good', 'For light days at the club.'),
  ('ebute-metta-youth:tampons', 'ebute-metta-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ebute-metta-youth:cup', 'ebute-metta-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ebute-metta-youth:pants', 'ebute-metta-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ebute-metta-youth:reusable', 'ebute-metta-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ebute-metta-youth:heat', 'ebute-metta-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ebute-metta-youth:wipes', 'ebute-metta-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ebute-metta-youth:washbag', 'ebute-metta-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ebute-metta-youth:guide', 'ebute-metta-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ebute-metta-waterside:pads', 'ebute-metta-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('ebute-metta-waterside:liners', 'ebute-metta-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ebute-metta-waterside:tampons', 'ebute-metta-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('ebute-metta-waterside:cup', 'ebute-metta-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ebute-metta-waterside:pants', 'ebute-metta-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('ebute-metta-waterside:reusable', 'ebute-metta-waterside', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ebute-metta-waterside:heat', 'ebute-metta-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ebute-metta-waterside:wipes', 'ebute-metta-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ebute-metta-waterside:washbag', 'ebute-metta-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('ebute-metta-waterside:guide', 'ebute-metta-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('ebute-metta-hostel:pads', 'ebute-metta-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('ebute-metta-hostel:liners', 'ebute-metta-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ebute-metta-hostel:tampons', 'ebute-metta-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ebute-metta-hostel:cup', 'ebute-metta-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ebute-metta-hostel:pants', 'ebute-metta-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ebute-metta-hostel:reusable', 'ebute-metta-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('ebute-metta-hostel:heat', 'ebute-metta-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ebute-metta-hostel:wipes', 'ebute-metta-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ebute-metta-hostel:washbag', 'ebute-metta-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('ebute-metta-hostel:guide', 'ebute-metta-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('ebute-metta-football:pads', 'ebute-metta-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ebute-metta-football:liners', 'ebute-metta-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ebute-metta-football:tampons', 'ebute-metta-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ebute-metta-football:cup', 'ebute-metta-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ebute-metta-football:pants', 'ebute-metta-football', 'pants', 'good', 'Stay put while running.'),
  ('ebute-metta-football:reusable', 'ebute-metta-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ebute-metta-football:heat', 'ebute-metta-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ebute-metta-football:wipes', 'ebute-metta-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ebute-metta-football:washbag', 'ebute-metta-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ebute-metta-football:guide', 'ebute-metta-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ebute-metta-training:pads', 'ebute-metta-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ebute-metta-training:liners', 'ebute-metta-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ebute-metta-training:tampons', 'ebute-metta-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ebute-metta-training:cup', 'ebute-metta-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ebute-metta-training:pants', 'ebute-metta-training', 'pants', 'good', 'Last through long sessions.'),
  ('ebute-metta-training:reusable', 'ebute-metta-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ebute-metta-training:heat', 'ebute-metta-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ebute-metta-training:wipes', 'ebute-metta-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ebute-metta-training:washbag', 'ebute-metta-training', 'washbag', 'good', 'Private storage at work.'),
  ('ebute-metta-training:guide', 'ebute-metta-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('yaba-school:pads', 'yaba-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('yaba-school:liners', 'yaba-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('yaba-school:tampons', 'yaba-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('yaba-school:cup', 'yaba-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('yaba-school:pants', 'yaba-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('yaba-school:reusable', 'yaba-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('yaba-school:heat', 'yaba-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('yaba-school:wipes', 'yaba-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('yaba-school:washbag', 'yaba-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('yaba-school:guide', 'yaba-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('yaba-market:pads', 'yaba-market', 'pads', 'good', 'Quick to change between customers.'),
  ('yaba-market:liners', 'yaba-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('yaba-market:tampons', 'yaba-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('yaba-market:cup', 'yaba-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('yaba-market:pants', 'yaba-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('yaba-market:reusable', 'yaba-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('yaba-market:heat', 'yaba-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('yaba-market:wipes', 'yaba-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('yaba-market:washbag', 'yaba-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('yaba-market:guide', 'yaba-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('yaba-busstop:pads', 'yaba-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('yaba-busstop:liners', 'yaba-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('yaba-busstop:tampons', 'yaba-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('yaba-busstop:cup', 'yaba-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('yaba-busstop:pants', 'yaba-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('yaba-busstop:reusable', 'yaba-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('yaba-busstop:heat', 'yaba-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('yaba-busstop:wipes', 'yaba-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('yaba-busstop:washbag', 'yaba-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('yaba-busstop:guide', 'yaba-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('yaba-clinic:pads', 'yaba-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('yaba-clinic:liners', 'yaba-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('yaba-clinic:tampons', 'yaba-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('yaba-clinic:cup', 'yaba-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('yaba-clinic:pants', 'yaba-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('yaba-clinic:reusable', 'yaba-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('yaba-clinic:heat', 'yaba-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('yaba-clinic:wipes', 'yaba-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('yaba-clinic:washbag', 'yaba-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('yaba-clinic:guide', 'yaba-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('yaba-youth:pads', 'yaba-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('yaba-youth:liners', 'yaba-youth', 'liners', 'good', 'For light days at the club.'),
  ('yaba-youth:tampons', 'yaba-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('yaba-youth:cup', 'yaba-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('yaba-youth:pants', 'yaba-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('yaba-youth:reusable', 'yaba-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('yaba-youth:heat', 'yaba-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('yaba-youth:wipes', 'yaba-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('yaba-youth:washbag', 'yaba-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('yaba-youth:guide', 'yaba-youth', 'guide', 'good', 'Answers questions they have.'),
  ('yaba-hostel:pads', 'yaba-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('yaba-hostel:liners', 'yaba-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('yaba-hostel:tampons', 'yaba-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('yaba-hostel:cup', 'yaba-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('yaba-hostel:pants', 'yaba-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('yaba-hostel:reusable', 'yaba-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('yaba-hostel:heat', 'yaba-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('yaba-hostel:wipes', 'yaba-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('yaba-hostel:washbag', 'yaba-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('yaba-hostel:guide', 'yaba-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('yaba-home:pads', 'yaba-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('yaba-home:liners', 'yaba-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('yaba-home:tampons', 'yaba-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('yaba-home:cup', 'yaba-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('yaba-home:pants', 'yaba-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('yaba-home:reusable', 'yaba-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('yaba-home:heat', 'yaba-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('yaba-home:wipes', 'yaba-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('yaba-home:washbag', 'yaba-home', 'washbag', 'good', 'A private place for her things.'),
  ('yaba-home:guide', 'yaba-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('yaba-football:pads', 'yaba-football', 'pads', 'good', 'A simple backup for match day.'),
  ('yaba-football:liners', 'yaba-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('yaba-football:tampons', 'yaba-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('yaba-football:cup', 'yaba-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('yaba-football:pants', 'yaba-football', 'pants', 'good', 'Stay put while running.'),
  ('yaba-football:reusable', 'yaba-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('yaba-football:heat', 'yaba-football', 'heat', 'good', 'Helps cramps after training.'),
  ('yaba-football:wipes', 'yaba-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('yaba-football:washbag', 'yaba-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('yaba-football:guide', 'yaba-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('yaba-training:pads', 'yaba-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('yaba-training:liners', 'yaba-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('yaba-training:tampons', 'yaba-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('yaba-training:cup', 'yaba-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('yaba-training:pants', 'yaba-training', 'pants', 'good', 'Last through long sessions.'),
  ('yaba-training:reusable', 'yaba-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('yaba-training:heat', 'yaba-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('yaba-training:wipes', 'yaba-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('yaba-training:washbag', 'yaba-training', 'washbag', 'good', 'Private storage at work.'),
  ('yaba-training:guide', 'yaba-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('yaba-shelter:pads', 'yaba-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('yaba-shelter:liners', 'yaba-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('yaba-shelter:tampons', 'yaba-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('yaba-shelter:cup', 'yaba-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('yaba-shelter:pants', 'yaba-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('yaba-shelter:reusable', 'yaba-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('yaba-shelter:heat', 'yaba-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('yaba-shelter:wipes', 'yaba-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('yaba-shelter:washbag', 'yaba-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('yaba-shelter:guide', 'yaba-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('akoka-school:pads', 'akoka-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('akoka-school:liners', 'akoka-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('akoka-school:tampons', 'akoka-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('akoka-school:cup', 'akoka-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('akoka-school:pants', 'akoka-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('akoka-school:reusable', 'akoka-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('akoka-school:heat', 'akoka-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('akoka-school:wipes', 'akoka-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('akoka-school:washbag', 'akoka-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('akoka-school:guide', 'akoka-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('akoka-market:pads', 'akoka-market', 'pads', 'good', 'Quick to change between customers.'),
  ('akoka-market:liners', 'akoka-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('akoka-market:tampons', 'akoka-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('akoka-market:cup', 'akoka-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('akoka-market:pants', 'akoka-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('akoka-market:reusable', 'akoka-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('akoka-market:heat', 'akoka-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('akoka-market:wipes', 'akoka-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('akoka-market:washbag', 'akoka-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('akoka-market:guide', 'akoka-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('akoka-busstop:pads', 'akoka-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('akoka-busstop:liners', 'akoka-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('akoka-busstop:tampons', 'akoka-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('akoka-busstop:cup', 'akoka-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('akoka-busstop:pants', 'akoka-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('akoka-busstop:reusable', 'akoka-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('akoka-busstop:heat', 'akoka-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('akoka-busstop:wipes', 'akoka-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('akoka-busstop:washbag', 'akoka-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('akoka-busstop:guide', 'akoka-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('akoka-clinic:pads', 'akoka-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('akoka-clinic:liners', 'akoka-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('akoka-clinic:tampons', 'akoka-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('akoka-clinic:cup', 'akoka-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('akoka-clinic:pants', 'akoka-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('akoka-clinic:reusable', 'akoka-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('akoka-clinic:heat', 'akoka-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('akoka-clinic:wipes', 'akoka-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('akoka-clinic:washbag', 'akoka-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('akoka-clinic:guide', 'akoka-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('akoka-youth:pads', 'akoka-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('akoka-youth:liners', 'akoka-youth', 'liners', 'good', 'For light days at the club.'),
  ('akoka-youth:tampons', 'akoka-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('akoka-youth:cup', 'akoka-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('akoka-youth:pants', 'akoka-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('akoka-youth:reusable', 'akoka-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('akoka-youth:heat', 'akoka-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('akoka-youth:wipes', 'akoka-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('akoka-youth:washbag', 'akoka-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('akoka-youth:guide', 'akoka-youth', 'guide', 'good', 'Answers questions they have.'),
  ('akoka-waterside:pads', 'akoka-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('akoka-waterside:liners', 'akoka-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('akoka-waterside:tampons', 'akoka-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('akoka-waterside:cup', 'akoka-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('akoka-waterside:pants', 'akoka-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('akoka-waterside:reusable', 'akoka-waterside', 'reusable', 'good', 'They last a year when the shop is far.'),
  ('akoka-waterside:heat', 'akoka-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('akoka-waterside:wipes', 'akoka-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('akoka-waterside:washbag', 'akoka-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('akoka-waterside:guide', 'akoka-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('akoka-hostel:pads', 'akoka-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('akoka-hostel:liners', 'akoka-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('akoka-hostel:tampons', 'akoka-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('akoka-hostel:cup', 'akoka-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('akoka-hostel:pants', 'akoka-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('akoka-hostel:reusable', 'akoka-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('akoka-hostel:heat', 'akoka-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('akoka-hostel:wipes', 'akoka-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('akoka-hostel:washbag', 'akoka-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('akoka-hostel:guide', 'akoka-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('akoka-football:pads', 'akoka-football', 'pads', 'good', 'A simple backup for match day.'),
  ('akoka-football:liners', 'akoka-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('akoka-football:tampons', 'akoka-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('akoka-football:cup', 'akoka-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('akoka-football:pants', 'akoka-football', 'pants', 'good', 'Stay put while running.'),
  ('akoka-football:reusable', 'akoka-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('akoka-football:heat', 'akoka-football', 'heat', 'good', 'Helps cramps after training.'),
  ('akoka-football:wipes', 'akoka-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('akoka-football:washbag', 'akoka-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('akoka-football:guide', 'akoka-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('akoka-training:pads', 'akoka-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('akoka-training:liners', 'akoka-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('akoka-training:tampons', 'akoka-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('akoka-training:cup', 'akoka-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('akoka-training:pants', 'akoka-training', 'pants', 'good', 'Last through long sessions.'),
  ('akoka-training:reusable', 'akoka-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('akoka-training:heat', 'akoka-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('akoka-training:wipes', 'akoka-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('akoka-training:washbag', 'akoka-training', 'washbag', 'good', 'Private storage at work.'),
  ('akoka-training:guide', 'akoka-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('akoka-shelter:pads', 'akoka-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('akoka-shelter:liners', 'akoka-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('akoka-shelter:tampons', 'akoka-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('akoka-shelter:cup', 'akoka-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('akoka-shelter:pants', 'akoka-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('akoka-shelter:reusable', 'akoka-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('akoka-shelter:heat', 'akoka-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('akoka-shelter:wipes', 'akoka-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('akoka-shelter:washbag', 'akoka-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('akoka-shelter:guide', 'akoka-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('shomolu-school:pads', 'shomolu-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('shomolu-school:liners', 'shomolu-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('shomolu-school:tampons', 'shomolu-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('shomolu-school:cup', 'shomolu-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('shomolu-school:pants', 'shomolu-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('shomolu-school:reusable', 'shomolu-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('shomolu-school:heat', 'shomolu-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('shomolu-school:wipes', 'shomolu-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('shomolu-school:washbag', 'shomolu-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('shomolu-school:guide', 'shomolu-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('shomolu-market:pads', 'shomolu-market', 'pads', 'good', 'Quick to change between customers.'),
  ('shomolu-market:liners', 'shomolu-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('shomolu-market:tampons', 'shomolu-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('shomolu-market:cup', 'shomolu-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('shomolu-market:pants', 'shomolu-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('shomolu-market:reusable', 'shomolu-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('shomolu-market:heat', 'shomolu-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('shomolu-market:wipes', 'shomolu-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('shomolu-market:washbag', 'shomolu-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('shomolu-market:guide', 'shomolu-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('shomolu-busstop:pads', 'shomolu-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('shomolu-busstop:liners', 'shomolu-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('shomolu-busstop:tampons', 'shomolu-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('shomolu-busstop:cup', 'shomolu-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('shomolu-busstop:pants', 'shomolu-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('shomolu-busstop:reusable', 'shomolu-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('shomolu-busstop:heat', 'shomolu-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('shomolu-busstop:wipes', 'shomolu-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('shomolu-busstop:washbag', 'shomolu-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('shomolu-busstop:guide', 'shomolu-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('shomolu-clinic:pads', 'shomolu-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('shomolu-clinic:liners', 'shomolu-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('shomolu-clinic:tampons', 'shomolu-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('shomolu-clinic:cup', 'shomolu-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('shomolu-clinic:pants', 'shomolu-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('shomolu-clinic:reusable', 'shomolu-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('shomolu-clinic:heat', 'shomolu-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('shomolu-clinic:wipes', 'shomolu-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('shomolu-clinic:washbag', 'shomolu-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('shomolu-clinic:guide', 'shomolu-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('shomolu-youth:pads', 'shomolu-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('shomolu-youth:liners', 'shomolu-youth', 'liners', 'good', 'For light days at the club.'),
  ('shomolu-youth:tampons', 'shomolu-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('shomolu-youth:cup', 'shomolu-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('shomolu-youth:pants', 'shomolu-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('shomolu-youth:reusable', 'shomolu-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('shomolu-youth:heat', 'shomolu-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('shomolu-youth:wipes', 'shomolu-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('shomolu-youth:washbag', 'shomolu-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('shomolu-youth:guide', 'shomolu-youth', 'guide', 'good', 'Answers questions they have.'),
  ('shomolu-hostel:pads', 'shomolu-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('shomolu-hostel:liners', 'shomolu-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('shomolu-hostel:tampons', 'shomolu-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('shomolu-hostel:cup', 'shomolu-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('shomolu-hostel:pants', 'shomolu-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('shomolu-hostel:reusable', 'shomolu-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('shomolu-hostel:heat', 'shomolu-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('shomolu-hostel:wipes', 'shomolu-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('shomolu-hostel:washbag', 'shomolu-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('shomolu-hostel:guide', 'shomolu-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('shomolu-home:pads', 'shomolu-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('shomolu-home:liners', 'shomolu-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('shomolu-home:tampons', 'shomolu-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('shomolu-home:cup', 'shomolu-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('shomolu-home:pants', 'shomolu-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('shomolu-home:reusable', 'shomolu-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('shomolu-home:heat', 'shomolu-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('shomolu-home:wipes', 'shomolu-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('shomolu-home:washbag', 'shomolu-home', 'washbag', 'good', 'A private place for her things.'),
  ('shomolu-home:guide', 'shomolu-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('shomolu-football:pads', 'shomolu-football', 'pads', 'good', 'A simple backup for match day.'),
  ('shomolu-football:liners', 'shomolu-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('shomolu-football:tampons', 'shomolu-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('shomolu-football:cup', 'shomolu-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('shomolu-football:pants', 'shomolu-football', 'pants', 'good', 'Stay put while running.'),
  ('shomolu-football:reusable', 'shomolu-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('shomolu-football:heat', 'shomolu-football', 'heat', 'good', 'Helps cramps after training.'),
  ('shomolu-football:wipes', 'shomolu-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('shomolu-football:washbag', 'shomolu-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('shomolu-football:guide', 'shomolu-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('shomolu-training:pads', 'shomolu-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('shomolu-training:liners', 'shomolu-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('shomolu-training:tampons', 'shomolu-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('shomolu-training:cup', 'shomolu-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('shomolu-training:pants', 'shomolu-training', 'pants', 'good', 'Last through long sessions.'),
  ('shomolu-training:reusable', 'shomolu-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('shomolu-training:heat', 'shomolu-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('shomolu-training:wipes', 'shomolu-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('shomolu-training:washbag', 'shomolu-training', 'washbag', 'good', 'Private storage at work.'),
  ('shomolu-training:guide', 'shomolu-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('bariga-school:pads', 'bariga-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('bariga-school:liners', 'bariga-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('bariga-school:tampons', 'bariga-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('bariga-school:cup', 'bariga-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('bariga-school:pants', 'bariga-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('bariga-school:reusable', 'bariga-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('bariga-school:heat', 'bariga-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('bariga-school:wipes', 'bariga-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('bariga-school:washbag', 'bariga-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('bariga-school:guide', 'bariga-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('bariga-market:pads', 'bariga-market', 'pads', 'good', 'Quick to change between customers.'),
  ('bariga-market:liners', 'bariga-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('bariga-market:tampons', 'bariga-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('bariga-market:cup', 'bariga-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('bariga-market:pants', 'bariga-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('bariga-market:reusable', 'bariga-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('bariga-market:heat', 'bariga-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('bariga-market:wipes', 'bariga-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('bariga-market:washbag', 'bariga-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('bariga-market:guide', 'bariga-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('bariga-busstop:pads', 'bariga-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('bariga-busstop:liners', 'bariga-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('bariga-busstop:tampons', 'bariga-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('bariga-busstop:cup', 'bariga-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('bariga-busstop:pants', 'bariga-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('bariga-busstop:reusable', 'bariga-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('bariga-busstop:heat', 'bariga-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('bariga-busstop:wipes', 'bariga-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('bariga-busstop:washbag', 'bariga-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('bariga-busstop:guide', 'bariga-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('bariga-clinic:pads', 'bariga-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('bariga-clinic:liners', 'bariga-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('bariga-clinic:tampons', 'bariga-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('bariga-clinic:cup', 'bariga-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('bariga-clinic:pants', 'bariga-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('bariga-clinic:reusable', 'bariga-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('bariga-clinic:heat', 'bariga-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('bariga-clinic:wipes', 'bariga-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('bariga-clinic:washbag', 'bariga-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('bariga-clinic:guide', 'bariga-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('bariga-youth:pads', 'bariga-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('bariga-youth:liners', 'bariga-youth', 'liners', 'good', 'For light days at the club.'),
  ('bariga-youth:tampons', 'bariga-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('bariga-youth:cup', 'bariga-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('bariga-youth:pants', 'bariga-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('bariga-youth:reusable', 'bariga-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('bariga-youth:heat', 'bariga-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('bariga-youth:wipes', 'bariga-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('bariga-youth:washbag', 'bariga-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('bariga-youth:guide', 'bariga-youth', 'guide', 'good', 'Answers questions they have.'),
  ('bariga-waterside:pads', 'bariga-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('bariga-waterside:liners', 'bariga-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('bariga-waterside:tampons', 'bariga-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('bariga-waterside:cup', 'bariga-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('bariga-waterside:pants', 'bariga-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('bariga-waterside:reusable', 'bariga-waterside', 'reusable', 'good', 'They last a year when the shop is far.'),
  ('bariga-waterside:heat', 'bariga-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('bariga-waterside:wipes', 'bariga-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('bariga-waterside:washbag', 'bariga-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('bariga-waterside:guide', 'bariga-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('bariga-hostel:pads', 'bariga-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('bariga-hostel:liners', 'bariga-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('bariga-hostel:tampons', 'bariga-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('bariga-hostel:cup', 'bariga-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('bariga-hostel:pants', 'bariga-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('bariga-hostel:reusable', 'bariga-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('bariga-hostel:heat', 'bariga-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('bariga-hostel:wipes', 'bariga-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('bariga-hostel:washbag', 'bariga-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('bariga-hostel:guide', 'bariga-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('bariga-football:pads', 'bariga-football', 'pads', 'good', 'A simple backup for match day.'),
  ('bariga-football:liners', 'bariga-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('bariga-football:tampons', 'bariga-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('bariga-football:cup', 'bariga-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('bariga-football:pants', 'bariga-football', 'pants', 'good', 'Stay put while running.'),
  ('bariga-football:reusable', 'bariga-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('bariga-football:heat', 'bariga-football', 'heat', 'good', 'Helps cramps after training.'),
  ('bariga-football:wipes', 'bariga-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('bariga-football:washbag', 'bariga-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('bariga-football:guide', 'bariga-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('bariga-training:pads', 'bariga-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('bariga-training:liners', 'bariga-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('bariga-training:tampons', 'bariga-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('bariga-training:cup', 'bariga-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('bariga-training:pants', 'bariga-training', 'pants', 'good', 'Last through long sessions.'),
  ('bariga-training:reusable', 'bariga-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('bariga-training:heat', 'bariga-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('bariga-training:wipes', 'bariga-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('bariga-training:washbag', 'bariga-training', 'washbag', 'good', 'Private storage at work.'),
  ('bariga-training:guide', 'bariga-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('mushin-school:pads', 'mushin-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('mushin-school:liners', 'mushin-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('mushin-school:tampons', 'mushin-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mushin-school:cup', 'mushin-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('mushin-school:pants', 'mushin-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('mushin-school:reusable', 'mushin-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('mushin-school:heat', 'mushin-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('mushin-school:wipes', 'mushin-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('mushin-school:washbag', 'mushin-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('mushin-school:guide', 'mushin-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('mushin-market:pads', 'mushin-market', 'pads', 'good', 'Quick to change between customers.'),
  ('mushin-market:liners', 'mushin-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mushin-market:tampons', 'mushin-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mushin-market:cup', 'mushin-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('mushin-market:pants', 'mushin-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('mushin-market:reusable', 'mushin-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('mushin-market:heat', 'mushin-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('mushin-market:wipes', 'mushin-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('mushin-market:washbag', 'mushin-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('mushin-market:guide', 'mushin-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('mushin-busstop:pads', 'mushin-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('mushin-busstop:liners', 'mushin-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mushin-busstop:tampons', 'mushin-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mushin-busstop:cup', 'mushin-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('mushin-busstop:pants', 'mushin-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('mushin-busstop:reusable', 'mushin-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('mushin-busstop:heat', 'mushin-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('mushin-busstop:wipes', 'mushin-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('mushin-busstop:washbag', 'mushin-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('mushin-busstop:guide', 'mushin-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('mushin-clinic:pads', 'mushin-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('mushin-clinic:liners', 'mushin-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mushin-clinic:tampons', 'mushin-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mushin-clinic:cup', 'mushin-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('mushin-clinic:pants', 'mushin-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('mushin-clinic:reusable', 'mushin-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('mushin-clinic:heat', 'mushin-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('mushin-clinic:wipes', 'mushin-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('mushin-clinic:washbag', 'mushin-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('mushin-clinic:guide', 'mushin-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('mushin-youth:pads', 'mushin-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('mushin-youth:liners', 'mushin-youth', 'liners', 'good', 'For light days at the club.'),
  ('mushin-youth:tampons', 'mushin-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mushin-youth:cup', 'mushin-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('mushin-youth:pants', 'mushin-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('mushin-youth:reusable', 'mushin-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('mushin-youth:heat', 'mushin-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('mushin-youth:wipes', 'mushin-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('mushin-youth:washbag', 'mushin-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('mushin-youth:guide', 'mushin-youth', 'guide', 'good', 'Answers questions they have.'),
  ('mushin-hostel:pads', 'mushin-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('mushin-hostel:liners', 'mushin-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mushin-hostel:tampons', 'mushin-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mushin-hostel:cup', 'mushin-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('mushin-hostel:pants', 'mushin-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('mushin-hostel:reusable', 'mushin-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('mushin-hostel:heat', 'mushin-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('mushin-hostel:wipes', 'mushin-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('mushin-hostel:washbag', 'mushin-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('mushin-hostel:guide', 'mushin-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('mushin-home:pads', 'mushin-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('mushin-home:liners', 'mushin-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mushin-home:tampons', 'mushin-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mushin-home:cup', 'mushin-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('mushin-home:pants', 'mushin-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('mushin-home:reusable', 'mushin-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('mushin-home:heat', 'mushin-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('mushin-home:wipes', 'mushin-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('mushin-home:washbag', 'mushin-home', 'washbag', 'good', 'A private place for her things.'),
  ('mushin-home:guide', 'mushin-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('mushin-football:pads', 'mushin-football', 'pads', 'good', 'A simple backup for match day.'),
  ('mushin-football:liners', 'mushin-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mushin-football:tampons', 'mushin-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mushin-football:cup', 'mushin-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('mushin-football:pants', 'mushin-football', 'pants', 'good', 'Stay put while running.'),
  ('mushin-football:reusable', 'mushin-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('mushin-football:heat', 'mushin-football', 'heat', 'good', 'Helps cramps after training.'),
  ('mushin-football:wipes', 'mushin-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('mushin-football:washbag', 'mushin-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('mushin-football:guide', 'mushin-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('mushin-training:pads', 'mushin-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('mushin-training:liners', 'mushin-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mushin-training:tampons', 'mushin-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mushin-training:cup', 'mushin-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('mushin-training:pants', 'mushin-training', 'pants', 'good', 'Last through long sessions.'),
  ('mushin-training:reusable', 'mushin-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('mushin-training:heat', 'mushin-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('mushin-training:wipes', 'mushin-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('mushin-training:washbag', 'mushin-training', 'washbag', 'good', 'Private storage at work.'),
  ('mushin-training:guide', 'mushin-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('isolo-school:pads', 'isolo-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('isolo-school:liners', 'isolo-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('isolo-school:tampons', 'isolo-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('isolo-school:cup', 'isolo-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('isolo-school:pants', 'isolo-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('isolo-school:reusable', 'isolo-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('isolo-school:heat', 'isolo-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('isolo-school:wipes', 'isolo-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('isolo-school:washbag', 'isolo-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('isolo-school:guide', 'isolo-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('isolo-market:pads', 'isolo-market', 'pads', 'good', 'Quick to change between customers.'),
  ('isolo-market:liners', 'isolo-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('isolo-market:tampons', 'isolo-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('isolo-market:cup', 'isolo-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('isolo-market:pants', 'isolo-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('isolo-market:reusable', 'isolo-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('isolo-market:heat', 'isolo-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('isolo-market:wipes', 'isolo-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('isolo-market:washbag', 'isolo-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('isolo-market:guide', 'isolo-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('isolo-busstop:pads', 'isolo-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('isolo-busstop:liners', 'isolo-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('isolo-busstop:tampons', 'isolo-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('isolo-busstop:cup', 'isolo-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('isolo-busstop:pants', 'isolo-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('isolo-busstop:reusable', 'isolo-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('isolo-busstop:heat', 'isolo-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('isolo-busstop:wipes', 'isolo-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('isolo-busstop:washbag', 'isolo-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('isolo-busstop:guide', 'isolo-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('isolo-clinic:pads', 'isolo-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('isolo-clinic:liners', 'isolo-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('isolo-clinic:tampons', 'isolo-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('isolo-clinic:cup', 'isolo-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('isolo-clinic:pants', 'isolo-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('isolo-clinic:reusable', 'isolo-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('isolo-clinic:heat', 'isolo-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('isolo-clinic:wipes', 'isolo-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('isolo-clinic:washbag', 'isolo-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('isolo-clinic:guide', 'isolo-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('isolo-youth:pads', 'isolo-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('isolo-youth:liners', 'isolo-youth', 'liners', 'good', 'For light days at the club.'),
  ('isolo-youth:tampons', 'isolo-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('isolo-youth:cup', 'isolo-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('isolo-youth:pants', 'isolo-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('isolo-youth:reusable', 'isolo-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('isolo-youth:heat', 'isolo-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('isolo-youth:wipes', 'isolo-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('isolo-youth:washbag', 'isolo-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('isolo-youth:guide', 'isolo-youth', 'guide', 'good', 'Answers questions they have.'),
  ('isolo-hostel:pads', 'isolo-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('isolo-hostel:liners', 'isolo-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('isolo-hostel:tampons', 'isolo-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('isolo-hostel:cup', 'isolo-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('isolo-hostel:pants', 'isolo-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('isolo-hostel:reusable', 'isolo-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('isolo-hostel:heat', 'isolo-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('isolo-hostel:wipes', 'isolo-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('isolo-hostel:washbag', 'isolo-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('isolo-hostel:guide', 'isolo-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('isolo-home:pads', 'isolo-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('isolo-home:liners', 'isolo-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('isolo-home:tampons', 'isolo-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('isolo-home:cup', 'isolo-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('isolo-home:pants', 'isolo-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('isolo-home:reusable', 'isolo-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('isolo-home:heat', 'isolo-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('isolo-home:wipes', 'isolo-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('isolo-home:washbag', 'isolo-home', 'washbag', 'good', 'A private place for her things.'),
  ('isolo-home:guide', 'isolo-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('isolo-football:pads', 'isolo-football', 'pads', 'good', 'A simple backup for match day.'),
  ('isolo-football:liners', 'isolo-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('isolo-football:tampons', 'isolo-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('isolo-football:cup', 'isolo-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('isolo-football:pants', 'isolo-football', 'pants', 'good', 'Stay put while running.'),
  ('isolo-football:reusable', 'isolo-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('isolo-football:heat', 'isolo-football', 'heat', 'good', 'Helps cramps after training.'),
  ('isolo-football:wipes', 'isolo-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('isolo-football:washbag', 'isolo-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('isolo-football:guide', 'isolo-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('isolo-training:pads', 'isolo-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('isolo-training:liners', 'isolo-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('isolo-training:tampons', 'isolo-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('isolo-training:cup', 'isolo-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('isolo-training:pants', 'isolo-training', 'pants', 'good', 'Last through long sessions.'),
  ('isolo-training:reusable', 'isolo-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('isolo-training:heat', 'isolo-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('isolo-training:wipes', 'isolo-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('isolo-training:washbag', 'isolo-training', 'washbag', 'good', 'Private storage at work.'),
  ('isolo-training:guide', 'isolo-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ejigbo-school:pads', 'ejigbo-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ejigbo-school:liners', 'ejigbo-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ejigbo-school:tampons', 'ejigbo-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ejigbo-school:cup', 'ejigbo-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ejigbo-school:pants', 'ejigbo-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ejigbo-school:reusable', 'ejigbo-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ejigbo-school:heat', 'ejigbo-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ejigbo-school:wipes', 'ejigbo-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('ejigbo-school:washbag', 'ejigbo-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ejigbo-school:guide', 'ejigbo-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ejigbo-market:pads', 'ejigbo-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ejigbo-market:liners', 'ejigbo-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ejigbo-market:tampons', 'ejigbo-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ejigbo-market:cup', 'ejigbo-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ejigbo-market:pants', 'ejigbo-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ejigbo-market:reusable', 'ejigbo-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ejigbo-market:heat', 'ejigbo-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ejigbo-market:wipes', 'ejigbo-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ejigbo-market:washbag', 'ejigbo-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ejigbo-market:guide', 'ejigbo-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ejigbo-busstop:pads', 'ejigbo-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ejigbo-busstop:liners', 'ejigbo-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ejigbo-busstop:tampons', 'ejigbo-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ejigbo-busstop:cup', 'ejigbo-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ejigbo-busstop:pants', 'ejigbo-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ejigbo-busstop:reusable', 'ejigbo-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ejigbo-busstop:heat', 'ejigbo-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ejigbo-busstop:wipes', 'ejigbo-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ejigbo-busstop:washbag', 'ejigbo-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ejigbo-busstop:guide', 'ejigbo-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ejigbo-clinic:pads', 'ejigbo-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ejigbo-clinic:liners', 'ejigbo-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ejigbo-clinic:tampons', 'ejigbo-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ejigbo-clinic:cup', 'ejigbo-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ejigbo-clinic:pants', 'ejigbo-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ejigbo-clinic:reusable', 'ejigbo-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ejigbo-clinic:heat', 'ejigbo-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ejigbo-clinic:wipes', 'ejigbo-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ejigbo-clinic:washbag', 'ejigbo-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ejigbo-clinic:guide', 'ejigbo-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ejigbo-youth:pads', 'ejigbo-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ejigbo-youth:liners', 'ejigbo-youth', 'liners', 'good', 'For light days at the club.'),
  ('ejigbo-youth:tampons', 'ejigbo-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ejigbo-youth:cup', 'ejigbo-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ejigbo-youth:pants', 'ejigbo-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ejigbo-youth:reusable', 'ejigbo-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ejigbo-youth:heat', 'ejigbo-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ejigbo-youth:wipes', 'ejigbo-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ejigbo-youth:washbag', 'ejigbo-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ejigbo-youth:guide', 'ejigbo-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ejigbo-hostel:pads', 'ejigbo-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('ejigbo-hostel:liners', 'ejigbo-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ejigbo-hostel:tampons', 'ejigbo-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ejigbo-hostel:cup', 'ejigbo-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ejigbo-hostel:pants', 'ejigbo-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ejigbo-hostel:reusable', 'ejigbo-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('ejigbo-hostel:heat', 'ejigbo-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ejigbo-hostel:wipes', 'ejigbo-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ejigbo-hostel:washbag', 'ejigbo-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('ejigbo-hostel:guide', 'ejigbo-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('ejigbo-home:pads', 'ejigbo-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('ejigbo-home:liners', 'ejigbo-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ejigbo-home:tampons', 'ejigbo-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ejigbo-home:cup', 'ejigbo-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ejigbo-home:pants', 'ejigbo-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ejigbo-home:reusable', 'ejigbo-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('ejigbo-home:heat', 'ejigbo-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ejigbo-home:wipes', 'ejigbo-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ejigbo-home:washbag', 'ejigbo-home', 'washbag', 'good', 'A private place for her things.'),
  ('ejigbo-home:guide', 'ejigbo-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('ejigbo-football:pads', 'ejigbo-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ejigbo-football:liners', 'ejigbo-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ejigbo-football:tampons', 'ejigbo-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ejigbo-football:cup', 'ejigbo-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ejigbo-football:pants', 'ejigbo-football', 'pants', 'good', 'Stay put while running.'),
  ('ejigbo-football:reusable', 'ejigbo-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ejigbo-football:heat', 'ejigbo-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ejigbo-football:wipes', 'ejigbo-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ejigbo-football:washbag', 'ejigbo-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ejigbo-football:guide', 'ejigbo-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ejigbo-training:pads', 'ejigbo-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ejigbo-training:liners', 'ejigbo-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ejigbo-training:tampons', 'ejigbo-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ejigbo-training:cup', 'ejigbo-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ejigbo-training:pants', 'ejigbo-training', 'pants', 'good', 'Last through long sessions.'),
  ('ejigbo-training:reusable', 'ejigbo-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ejigbo-training:heat', 'ejigbo-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ejigbo-training:wipes', 'ejigbo-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ejigbo-training:washbag', 'ejigbo-training', 'washbag', 'good', 'Private storage at work.'),
  ('ejigbo-training:guide', 'ejigbo-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ejigbo-shelter:pads', 'ejigbo-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('ejigbo-shelter:liners', 'ejigbo-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ejigbo-shelter:tampons', 'ejigbo-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ejigbo-shelter:cup', 'ejigbo-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ejigbo-shelter:pants', 'ejigbo-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ejigbo-shelter:reusable', 'ejigbo-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ejigbo-shelter:heat', 'ejigbo-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('ejigbo-shelter:wipes', 'ejigbo-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('ejigbo-shelter:washbag', 'ejigbo-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('ejigbo-shelter:guide', 'ejigbo-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('oshodi-school:pads', 'oshodi-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('oshodi-school:liners', 'oshodi-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('oshodi-school:tampons', 'oshodi-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oshodi-school:cup', 'oshodi-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('oshodi-school:pants', 'oshodi-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('oshodi-school:reusable', 'oshodi-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('oshodi-school:heat', 'oshodi-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('oshodi-school:wipes', 'oshodi-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('oshodi-school:washbag', 'oshodi-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('oshodi-school:guide', 'oshodi-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('oshodi-market:pads', 'oshodi-market', 'pads', 'good', 'Quick to change between customers.'),
  ('oshodi-market:liners', 'oshodi-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oshodi-market:tampons', 'oshodi-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oshodi-market:cup', 'oshodi-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('oshodi-market:pants', 'oshodi-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('oshodi-market:reusable', 'oshodi-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('oshodi-market:heat', 'oshodi-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('oshodi-market:wipes', 'oshodi-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('oshodi-market:washbag', 'oshodi-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('oshodi-market:guide', 'oshodi-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('oshodi-busstop:pads', 'oshodi-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('oshodi-busstop:liners', 'oshodi-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oshodi-busstop:tampons', 'oshodi-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oshodi-busstop:cup', 'oshodi-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('oshodi-busstop:pants', 'oshodi-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('oshodi-busstop:reusable', 'oshodi-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('oshodi-busstop:heat', 'oshodi-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('oshodi-busstop:wipes', 'oshodi-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('oshodi-busstop:washbag', 'oshodi-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('oshodi-busstop:guide', 'oshodi-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('oshodi-clinic:pads', 'oshodi-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('oshodi-clinic:liners', 'oshodi-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oshodi-clinic:tampons', 'oshodi-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oshodi-clinic:cup', 'oshodi-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('oshodi-clinic:pants', 'oshodi-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('oshodi-clinic:reusable', 'oshodi-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('oshodi-clinic:heat', 'oshodi-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('oshodi-clinic:wipes', 'oshodi-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('oshodi-clinic:washbag', 'oshodi-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('oshodi-clinic:guide', 'oshodi-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('oshodi-youth:pads', 'oshodi-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('oshodi-youth:liners', 'oshodi-youth', 'liners', 'good', 'For light days at the club.'),
  ('oshodi-youth:tampons', 'oshodi-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oshodi-youth:cup', 'oshodi-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('oshodi-youth:pants', 'oshodi-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('oshodi-youth:reusable', 'oshodi-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('oshodi-youth:heat', 'oshodi-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('oshodi-youth:wipes', 'oshodi-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('oshodi-youth:washbag', 'oshodi-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('oshodi-youth:guide', 'oshodi-youth', 'guide', 'good', 'Answers questions they have.'),
  ('oshodi-hostel:pads', 'oshodi-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('oshodi-hostel:liners', 'oshodi-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oshodi-hostel:tampons', 'oshodi-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oshodi-hostel:cup', 'oshodi-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('oshodi-hostel:pants', 'oshodi-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('oshodi-hostel:reusable', 'oshodi-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('oshodi-hostel:heat', 'oshodi-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('oshodi-hostel:wipes', 'oshodi-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('oshodi-hostel:washbag', 'oshodi-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('oshodi-hostel:guide', 'oshodi-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('oshodi-home:pads', 'oshodi-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('oshodi-home:liners', 'oshodi-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oshodi-home:tampons', 'oshodi-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oshodi-home:cup', 'oshodi-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('oshodi-home:pants', 'oshodi-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('oshodi-home:reusable', 'oshodi-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('oshodi-home:heat', 'oshodi-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('oshodi-home:wipes', 'oshodi-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('oshodi-home:washbag', 'oshodi-home', 'washbag', 'good', 'A private place for her things.'),
  ('oshodi-home:guide', 'oshodi-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('oshodi-football:pads', 'oshodi-football', 'pads', 'good', 'A simple backup for match day.'),
  ('oshodi-football:liners', 'oshodi-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oshodi-football:tampons', 'oshodi-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oshodi-football:cup', 'oshodi-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('oshodi-football:pants', 'oshodi-football', 'pants', 'good', 'Stay put while running.'),
  ('oshodi-football:reusable', 'oshodi-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('oshodi-football:heat', 'oshodi-football', 'heat', 'good', 'Helps cramps after training.'),
  ('oshodi-football:wipes', 'oshodi-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('oshodi-football:washbag', 'oshodi-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('oshodi-football:guide', 'oshodi-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('oshodi-training:pads', 'oshodi-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('oshodi-training:liners', 'oshodi-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oshodi-training:tampons', 'oshodi-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oshodi-training:cup', 'oshodi-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('oshodi-training:pants', 'oshodi-training', 'pants', 'good', 'Last through long sessions.'),
  ('oshodi-training:reusable', 'oshodi-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('oshodi-training:heat', 'oshodi-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('oshodi-training:wipes', 'oshodi-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('oshodi-training:washbag', 'oshodi-training', 'washbag', 'good', 'Private storage at work.'),
  ('oshodi-training:guide', 'oshodi-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ilupeju-school:pads', 'ilupeju-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ilupeju-school:liners', 'ilupeju-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ilupeju-school:tampons', 'ilupeju-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ilupeju-school:cup', 'ilupeju-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ilupeju-school:pants', 'ilupeju-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ilupeju-school:reusable', 'ilupeju-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ilupeju-school:heat', 'ilupeju-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ilupeju-school:wipes', 'ilupeju-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('ilupeju-school:washbag', 'ilupeju-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ilupeju-school:guide', 'ilupeju-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ilupeju-market:pads', 'ilupeju-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ilupeju-market:liners', 'ilupeju-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ilupeju-market:tampons', 'ilupeju-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ilupeju-market:cup', 'ilupeju-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ilupeju-market:pants', 'ilupeju-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ilupeju-market:reusable', 'ilupeju-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ilupeju-market:heat', 'ilupeju-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ilupeju-market:wipes', 'ilupeju-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ilupeju-market:washbag', 'ilupeju-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ilupeju-market:guide', 'ilupeju-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ilupeju-busstop:pads', 'ilupeju-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ilupeju-busstop:liners', 'ilupeju-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ilupeju-busstop:tampons', 'ilupeju-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ilupeju-busstop:cup', 'ilupeju-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ilupeju-busstop:pants', 'ilupeju-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ilupeju-busstop:reusable', 'ilupeju-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ilupeju-busstop:heat', 'ilupeju-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ilupeju-busstop:wipes', 'ilupeju-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ilupeju-busstop:washbag', 'ilupeju-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ilupeju-busstop:guide', 'ilupeju-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ilupeju-clinic:pads', 'ilupeju-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ilupeju-clinic:liners', 'ilupeju-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ilupeju-clinic:tampons', 'ilupeju-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ilupeju-clinic:cup', 'ilupeju-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ilupeju-clinic:pants', 'ilupeju-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ilupeju-clinic:reusable', 'ilupeju-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ilupeju-clinic:heat', 'ilupeju-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ilupeju-clinic:wipes', 'ilupeju-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ilupeju-clinic:washbag', 'ilupeju-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ilupeju-clinic:guide', 'ilupeju-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ilupeju-youth:pads', 'ilupeju-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ilupeju-youth:liners', 'ilupeju-youth', 'liners', 'good', 'For light days at the club.'),
  ('ilupeju-youth:tampons', 'ilupeju-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ilupeju-youth:cup', 'ilupeju-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ilupeju-youth:pants', 'ilupeju-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ilupeju-youth:reusable', 'ilupeju-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ilupeju-youth:heat', 'ilupeju-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ilupeju-youth:wipes', 'ilupeju-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ilupeju-youth:washbag', 'ilupeju-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ilupeju-youth:guide', 'ilupeju-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ilupeju-hostel:pads', 'ilupeju-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('ilupeju-hostel:liners', 'ilupeju-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ilupeju-hostel:tampons', 'ilupeju-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ilupeju-hostel:cup', 'ilupeju-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ilupeju-hostel:pants', 'ilupeju-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ilupeju-hostel:reusable', 'ilupeju-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('ilupeju-hostel:heat', 'ilupeju-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ilupeju-hostel:wipes', 'ilupeju-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ilupeju-hostel:washbag', 'ilupeju-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('ilupeju-hostel:guide', 'ilupeju-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('ilupeju-home:pads', 'ilupeju-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('ilupeju-home:liners', 'ilupeju-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ilupeju-home:tampons', 'ilupeju-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ilupeju-home:cup', 'ilupeju-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ilupeju-home:pants', 'ilupeju-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ilupeju-home:reusable', 'ilupeju-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('ilupeju-home:heat', 'ilupeju-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ilupeju-home:wipes', 'ilupeju-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ilupeju-home:washbag', 'ilupeju-home', 'washbag', 'good', 'A private place for her things.'),
  ('ilupeju-home:guide', 'ilupeju-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('ilupeju-football:pads', 'ilupeju-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ilupeju-football:liners', 'ilupeju-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ilupeju-football:tampons', 'ilupeju-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ilupeju-football:cup', 'ilupeju-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ilupeju-football:pants', 'ilupeju-football', 'pants', 'good', 'Stay put while running.'),
  ('ilupeju-football:reusable', 'ilupeju-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ilupeju-football:heat', 'ilupeju-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ilupeju-football:wipes', 'ilupeju-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ilupeju-football:washbag', 'ilupeju-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ilupeju-football:guide', 'ilupeju-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ilupeju-training:pads', 'ilupeju-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ilupeju-training:liners', 'ilupeju-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ilupeju-training:tampons', 'ilupeju-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ilupeju-training:cup', 'ilupeju-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ilupeju-training:pants', 'ilupeju-training', 'pants', 'good', 'Last through long sessions.'),
  ('ilupeju-training:reusable', 'ilupeju-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ilupeju-training:heat', 'ilupeju-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ilupeju-training:wipes', 'ilupeju-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ilupeju-training:washbag', 'ilupeju-training', 'washbag', 'good', 'Private storage at work.'),
  ('ilupeju-training:guide', 'ilupeju-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ilupeju-shelter:pads', 'ilupeju-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('ilupeju-shelter:liners', 'ilupeju-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ilupeju-shelter:tampons', 'ilupeju-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ilupeju-shelter:cup', 'ilupeju-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ilupeju-shelter:pants', 'ilupeju-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ilupeju-shelter:reusable', 'ilupeju-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ilupeju-shelter:heat', 'ilupeju-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('ilupeju-shelter:wipes', 'ilupeju-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('ilupeju-shelter:washbag', 'ilupeju-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('ilupeju-shelter:guide', 'ilupeju-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('palmgrove-school:pads', 'palmgrove-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('palmgrove-school:liners', 'palmgrove-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('palmgrove-school:tampons', 'palmgrove-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('palmgrove-school:cup', 'palmgrove-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('palmgrove-school:pants', 'palmgrove-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('palmgrove-school:reusable', 'palmgrove-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('palmgrove-school:heat', 'palmgrove-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('palmgrove-school:wipes', 'palmgrove-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('palmgrove-school:washbag', 'palmgrove-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('palmgrove-school:guide', 'palmgrove-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('palmgrove-market:pads', 'palmgrove-market', 'pads', 'good', 'Quick to change between customers.'),
  ('palmgrove-market:liners', 'palmgrove-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('palmgrove-market:tampons', 'palmgrove-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('palmgrove-market:cup', 'palmgrove-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('palmgrove-market:pants', 'palmgrove-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('palmgrove-market:reusable', 'palmgrove-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('palmgrove-market:heat', 'palmgrove-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('palmgrove-market:wipes', 'palmgrove-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('palmgrove-market:washbag', 'palmgrove-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('palmgrove-market:guide', 'palmgrove-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('palmgrove-busstop:pads', 'palmgrove-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('palmgrove-busstop:liners', 'palmgrove-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('palmgrove-busstop:tampons', 'palmgrove-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('palmgrove-busstop:cup', 'palmgrove-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('palmgrove-busstop:pants', 'palmgrove-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('palmgrove-busstop:reusable', 'palmgrove-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('palmgrove-busstop:heat', 'palmgrove-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('palmgrove-busstop:wipes', 'palmgrove-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('palmgrove-busstop:washbag', 'palmgrove-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('palmgrove-busstop:guide', 'palmgrove-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('palmgrove-clinic:pads', 'palmgrove-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('palmgrove-clinic:liners', 'palmgrove-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('palmgrove-clinic:tampons', 'palmgrove-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('palmgrove-clinic:cup', 'palmgrove-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('palmgrove-clinic:pants', 'palmgrove-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('palmgrove-clinic:reusable', 'palmgrove-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('palmgrove-clinic:heat', 'palmgrove-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('palmgrove-clinic:wipes', 'palmgrove-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('palmgrove-clinic:washbag', 'palmgrove-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('palmgrove-clinic:guide', 'palmgrove-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('palmgrove-youth:pads', 'palmgrove-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('palmgrove-youth:liners', 'palmgrove-youth', 'liners', 'good', 'For light days at the club.'),
  ('palmgrove-youth:tampons', 'palmgrove-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('palmgrove-youth:cup', 'palmgrove-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('palmgrove-youth:pants', 'palmgrove-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('palmgrove-youth:reusable', 'palmgrove-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('palmgrove-youth:heat', 'palmgrove-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('palmgrove-youth:wipes', 'palmgrove-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('palmgrove-youth:washbag', 'palmgrove-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('palmgrove-youth:guide', 'palmgrove-youth', 'guide', 'good', 'Answers questions they have.');

INSERT INTO giveday_needs (id, place_id, product_id, rating, why) VALUES
  ('palmgrove-hostel:pads', 'palmgrove-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('palmgrove-hostel:liners', 'palmgrove-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('palmgrove-hostel:tampons', 'palmgrove-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('palmgrove-hostel:cup', 'palmgrove-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('palmgrove-hostel:pants', 'palmgrove-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('palmgrove-hostel:reusable', 'palmgrove-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('palmgrove-hostel:heat', 'palmgrove-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('palmgrove-hostel:wipes', 'palmgrove-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('palmgrove-hostel:washbag', 'palmgrove-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('palmgrove-hostel:guide', 'palmgrove-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('palmgrove-home:pads', 'palmgrove-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('palmgrove-home:liners', 'palmgrove-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('palmgrove-home:tampons', 'palmgrove-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('palmgrove-home:cup', 'palmgrove-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('palmgrove-home:pants', 'palmgrove-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('palmgrove-home:reusable', 'palmgrove-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('palmgrove-home:heat', 'palmgrove-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('palmgrove-home:wipes', 'palmgrove-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('palmgrove-home:washbag', 'palmgrove-home', 'washbag', 'good', 'A private place for her things.'),
  ('palmgrove-home:guide', 'palmgrove-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('palmgrove-football:pads', 'palmgrove-football', 'pads', 'good', 'A simple backup for match day.'),
  ('palmgrove-football:liners', 'palmgrove-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('palmgrove-football:tampons', 'palmgrove-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('palmgrove-football:cup', 'palmgrove-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('palmgrove-football:pants', 'palmgrove-football', 'pants', 'good', 'Stay put while running.'),
  ('palmgrove-football:reusable', 'palmgrove-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('palmgrove-football:heat', 'palmgrove-football', 'heat', 'good', 'Helps cramps after training.'),
  ('palmgrove-football:wipes', 'palmgrove-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('palmgrove-football:washbag', 'palmgrove-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('palmgrove-football:guide', 'palmgrove-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('palmgrove-training:pads', 'palmgrove-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('palmgrove-training:liners', 'palmgrove-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('palmgrove-training:tampons', 'palmgrove-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('palmgrove-training:cup', 'palmgrove-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('palmgrove-training:pants', 'palmgrove-training', 'pants', 'good', 'Last through long sessions.'),
  ('palmgrove-training:reusable', 'palmgrove-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('palmgrove-training:heat', 'palmgrove-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('palmgrove-training:wipes', 'palmgrove-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('palmgrove-training:washbag', 'palmgrove-training', 'washbag', 'good', 'Private storage at work.'),
  ('palmgrove-training:guide', 'palmgrove-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('palmgrove-shelter:pads', 'palmgrove-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('palmgrove-shelter:liners', 'palmgrove-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('palmgrove-shelter:tampons', 'palmgrove-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('palmgrove-shelter:cup', 'palmgrove-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('palmgrove-shelter:pants', 'palmgrove-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('palmgrove-shelter:reusable', 'palmgrove-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('palmgrove-shelter:heat', 'palmgrove-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('palmgrove-shelter:wipes', 'palmgrove-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('palmgrove-shelter:washbag', 'palmgrove-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('palmgrove-shelter:guide', 'palmgrove-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('gbagada-school:pads', 'gbagada-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('gbagada-school:liners', 'gbagada-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('gbagada-school:tampons', 'gbagada-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('gbagada-school:cup', 'gbagada-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('gbagada-school:pants', 'gbagada-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('gbagada-school:reusable', 'gbagada-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('gbagada-school:heat', 'gbagada-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('gbagada-school:wipes', 'gbagada-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('gbagada-school:washbag', 'gbagada-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('gbagada-school:guide', 'gbagada-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('gbagada-market:pads', 'gbagada-market', 'pads', 'good', 'Quick to change between customers.'),
  ('gbagada-market:liners', 'gbagada-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('gbagada-market:tampons', 'gbagada-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('gbagada-market:cup', 'gbagada-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('gbagada-market:pants', 'gbagada-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('gbagada-market:reusable', 'gbagada-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('gbagada-market:heat', 'gbagada-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('gbagada-market:wipes', 'gbagada-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('gbagada-market:washbag', 'gbagada-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('gbagada-market:guide', 'gbagada-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('gbagada-busstop:pads', 'gbagada-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('gbagada-busstop:liners', 'gbagada-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('gbagada-busstop:tampons', 'gbagada-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('gbagada-busstop:cup', 'gbagada-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('gbagada-busstop:pants', 'gbagada-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('gbagada-busstop:reusable', 'gbagada-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('gbagada-busstop:heat', 'gbagada-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('gbagada-busstop:wipes', 'gbagada-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('gbagada-busstop:washbag', 'gbagada-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('gbagada-busstop:guide', 'gbagada-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('gbagada-clinic:pads', 'gbagada-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('gbagada-clinic:liners', 'gbagada-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('gbagada-clinic:tampons', 'gbagada-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('gbagada-clinic:cup', 'gbagada-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('gbagada-clinic:pants', 'gbagada-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('gbagada-clinic:reusable', 'gbagada-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('gbagada-clinic:heat', 'gbagada-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('gbagada-clinic:wipes', 'gbagada-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('gbagada-clinic:washbag', 'gbagada-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('gbagada-clinic:guide', 'gbagada-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('gbagada-youth:pads', 'gbagada-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('gbagada-youth:liners', 'gbagada-youth', 'liners', 'good', 'For light days at the club.'),
  ('gbagada-youth:tampons', 'gbagada-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('gbagada-youth:cup', 'gbagada-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('gbagada-youth:pants', 'gbagada-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('gbagada-youth:reusable', 'gbagada-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('gbagada-youth:heat', 'gbagada-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('gbagada-youth:wipes', 'gbagada-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('gbagada-youth:washbag', 'gbagada-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('gbagada-youth:guide', 'gbagada-youth', 'guide', 'good', 'Answers questions they have.'),
  ('gbagada-hostel:pads', 'gbagada-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('gbagada-hostel:liners', 'gbagada-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('gbagada-hostel:tampons', 'gbagada-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('gbagada-hostel:cup', 'gbagada-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('gbagada-hostel:pants', 'gbagada-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('gbagada-hostel:reusable', 'gbagada-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('gbagada-hostel:heat', 'gbagada-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('gbagada-hostel:wipes', 'gbagada-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('gbagada-hostel:washbag', 'gbagada-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('gbagada-hostel:guide', 'gbagada-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('gbagada-home:pads', 'gbagada-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('gbagada-home:liners', 'gbagada-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('gbagada-home:tampons', 'gbagada-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('gbagada-home:cup', 'gbagada-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('gbagada-home:pants', 'gbagada-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('gbagada-home:reusable', 'gbagada-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('gbagada-home:heat', 'gbagada-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('gbagada-home:wipes', 'gbagada-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('gbagada-home:washbag', 'gbagada-home', 'washbag', 'good', 'A private place for her things.'),
  ('gbagada-home:guide', 'gbagada-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('gbagada-football:pads', 'gbagada-football', 'pads', 'good', 'A simple backup for match day.'),
  ('gbagada-football:liners', 'gbagada-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('gbagada-football:tampons', 'gbagada-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('gbagada-football:cup', 'gbagada-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('gbagada-football:pants', 'gbagada-football', 'pants', 'good', 'Stay put while running.'),
  ('gbagada-football:reusable', 'gbagada-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('gbagada-football:heat', 'gbagada-football', 'heat', 'good', 'Helps cramps after training.'),
  ('gbagada-football:wipes', 'gbagada-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('gbagada-football:washbag', 'gbagada-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('gbagada-football:guide', 'gbagada-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('gbagada-training:pads', 'gbagada-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('gbagada-training:liners', 'gbagada-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('gbagada-training:tampons', 'gbagada-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('gbagada-training:cup', 'gbagada-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('gbagada-training:pants', 'gbagada-training', 'pants', 'good', 'Last through long sessions.'),
  ('gbagada-training:reusable', 'gbagada-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('gbagada-training:heat', 'gbagada-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('gbagada-training:wipes', 'gbagada-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('gbagada-training:washbag', 'gbagada-training', 'washbag', 'good', 'Private storage at work.'),
  ('gbagada-training:guide', 'gbagada-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('oworonshoki-school:pads', 'oworonshoki-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('oworonshoki-school:liners', 'oworonshoki-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('oworonshoki-school:tampons', 'oworonshoki-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oworonshoki-school:cup', 'oworonshoki-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('oworonshoki-school:pants', 'oworonshoki-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('oworonshoki-school:reusable', 'oworonshoki-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('oworonshoki-school:heat', 'oworonshoki-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('oworonshoki-school:wipes', 'oworonshoki-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('oworonshoki-school:washbag', 'oworonshoki-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('oworonshoki-school:guide', 'oworonshoki-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('oworonshoki-market:pads', 'oworonshoki-market', 'pads', 'good', 'Quick to change between customers.'),
  ('oworonshoki-market:liners', 'oworonshoki-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oworonshoki-market:tampons', 'oworonshoki-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oworonshoki-market:cup', 'oworonshoki-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('oworonshoki-market:pants', 'oworonshoki-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('oworonshoki-market:reusable', 'oworonshoki-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('oworonshoki-market:heat', 'oworonshoki-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('oworonshoki-market:wipes', 'oworonshoki-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('oworonshoki-market:washbag', 'oworonshoki-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('oworonshoki-market:guide', 'oworonshoki-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('oworonshoki-busstop:pads', 'oworonshoki-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('oworonshoki-busstop:liners', 'oworonshoki-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oworonshoki-busstop:tampons', 'oworonshoki-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oworonshoki-busstop:cup', 'oworonshoki-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('oworonshoki-busstop:pants', 'oworonshoki-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('oworonshoki-busstop:reusable', 'oworonshoki-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('oworonshoki-busstop:heat', 'oworonshoki-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('oworonshoki-busstop:wipes', 'oworonshoki-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('oworonshoki-busstop:washbag', 'oworonshoki-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('oworonshoki-busstop:guide', 'oworonshoki-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('oworonshoki-clinic:pads', 'oworonshoki-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('oworonshoki-clinic:liners', 'oworonshoki-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oworonshoki-clinic:tampons', 'oworonshoki-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oworonshoki-clinic:cup', 'oworonshoki-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('oworonshoki-clinic:pants', 'oworonshoki-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('oworonshoki-clinic:reusable', 'oworonshoki-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('oworonshoki-clinic:heat', 'oworonshoki-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('oworonshoki-clinic:wipes', 'oworonshoki-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('oworonshoki-clinic:washbag', 'oworonshoki-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('oworonshoki-clinic:guide', 'oworonshoki-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('oworonshoki-youth:pads', 'oworonshoki-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('oworonshoki-youth:liners', 'oworonshoki-youth', 'liners', 'good', 'For light days at the club.'),
  ('oworonshoki-youth:tampons', 'oworonshoki-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oworonshoki-youth:cup', 'oworonshoki-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('oworonshoki-youth:pants', 'oworonshoki-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('oworonshoki-youth:reusable', 'oworonshoki-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('oworonshoki-youth:heat', 'oworonshoki-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('oworonshoki-youth:wipes', 'oworonshoki-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('oworonshoki-youth:washbag', 'oworonshoki-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('oworonshoki-youth:guide', 'oworonshoki-youth', 'guide', 'good', 'Answers questions they have.'),
  ('oworonshoki-waterside:pads', 'oworonshoki-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('oworonshoki-waterside:liners', 'oworonshoki-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oworonshoki-waterside:tampons', 'oworonshoki-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('oworonshoki-waterside:cup', 'oworonshoki-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('oworonshoki-waterside:pants', 'oworonshoki-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('oworonshoki-waterside:reusable', 'oworonshoki-waterside', 'reusable', 'good', 'They last a year when the shop is far.'),
  ('oworonshoki-waterside:heat', 'oworonshoki-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('oworonshoki-waterside:wipes', 'oworonshoki-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('oworonshoki-waterside:washbag', 'oworonshoki-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('oworonshoki-waterside:guide', 'oworonshoki-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('oworonshoki-hostel:pads', 'oworonshoki-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('oworonshoki-hostel:liners', 'oworonshoki-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oworonshoki-hostel:tampons', 'oworonshoki-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oworonshoki-hostel:cup', 'oworonshoki-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('oworonshoki-hostel:pants', 'oworonshoki-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('oworonshoki-hostel:reusable', 'oworonshoki-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('oworonshoki-hostel:heat', 'oworonshoki-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('oworonshoki-hostel:wipes', 'oworonshoki-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('oworonshoki-hostel:washbag', 'oworonshoki-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('oworonshoki-hostel:guide', 'oworonshoki-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('oworonshoki-football:pads', 'oworonshoki-football', 'pads', 'good', 'A simple backup for match day.'),
  ('oworonshoki-football:liners', 'oworonshoki-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oworonshoki-football:tampons', 'oworonshoki-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oworonshoki-football:cup', 'oworonshoki-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('oworonshoki-football:pants', 'oworonshoki-football', 'pants', 'good', 'Stay put while running.'),
  ('oworonshoki-football:reusable', 'oworonshoki-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('oworonshoki-football:heat', 'oworonshoki-football', 'heat', 'good', 'Helps cramps after training.'),
  ('oworonshoki-football:wipes', 'oworonshoki-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('oworonshoki-football:washbag', 'oworonshoki-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('oworonshoki-football:guide', 'oworonshoki-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('oworonshoki-training:pads', 'oworonshoki-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('oworonshoki-training:liners', 'oworonshoki-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oworonshoki-training:tampons', 'oworonshoki-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oworonshoki-training:cup', 'oworonshoki-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('oworonshoki-training:pants', 'oworonshoki-training', 'pants', 'good', 'Last through long sessions.'),
  ('oworonshoki-training:reusable', 'oworonshoki-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('oworonshoki-training:heat', 'oworonshoki-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('oworonshoki-training:wipes', 'oworonshoki-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('oworonshoki-training:washbag', 'oworonshoki-training', 'washbag', 'good', 'Private storage at work.'),
  ('oworonshoki-training:guide', 'oworonshoki-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('maryland-school:pads', 'maryland-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('maryland-school:liners', 'maryland-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('maryland-school:tampons', 'maryland-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('maryland-school:cup', 'maryland-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('maryland-school:pants', 'maryland-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('maryland-school:reusable', 'maryland-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('maryland-school:heat', 'maryland-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('maryland-school:wipes', 'maryland-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('maryland-school:washbag', 'maryland-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('maryland-school:guide', 'maryland-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('maryland-market:pads', 'maryland-market', 'pads', 'good', 'Quick to change between customers.'),
  ('maryland-market:liners', 'maryland-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('maryland-market:tampons', 'maryland-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('maryland-market:cup', 'maryland-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('maryland-market:pants', 'maryland-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('maryland-market:reusable', 'maryland-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('maryland-market:heat', 'maryland-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('maryland-market:wipes', 'maryland-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('maryland-market:washbag', 'maryland-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('maryland-market:guide', 'maryland-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('maryland-busstop:pads', 'maryland-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('maryland-busstop:liners', 'maryland-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('maryland-busstop:tampons', 'maryland-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('maryland-busstop:cup', 'maryland-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('maryland-busstop:pants', 'maryland-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('maryland-busstop:reusable', 'maryland-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('maryland-busstop:heat', 'maryland-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('maryland-busstop:wipes', 'maryland-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('maryland-busstop:washbag', 'maryland-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('maryland-busstop:guide', 'maryland-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('maryland-clinic:pads', 'maryland-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('maryland-clinic:liners', 'maryland-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('maryland-clinic:tampons', 'maryland-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('maryland-clinic:cup', 'maryland-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('maryland-clinic:pants', 'maryland-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('maryland-clinic:reusable', 'maryland-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('maryland-clinic:heat', 'maryland-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('maryland-clinic:wipes', 'maryland-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('maryland-clinic:washbag', 'maryland-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('maryland-clinic:guide', 'maryland-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('maryland-youth:pads', 'maryland-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('maryland-youth:liners', 'maryland-youth', 'liners', 'good', 'For light days at the club.'),
  ('maryland-youth:tampons', 'maryland-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('maryland-youth:cup', 'maryland-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('maryland-youth:pants', 'maryland-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('maryland-youth:reusable', 'maryland-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('maryland-youth:heat', 'maryland-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('maryland-youth:wipes', 'maryland-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('maryland-youth:washbag', 'maryland-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('maryland-youth:guide', 'maryland-youth', 'guide', 'good', 'Answers questions they have.'),
  ('maryland-hostel:pads', 'maryland-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('maryland-hostel:liners', 'maryland-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('maryland-hostel:tampons', 'maryland-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('maryland-hostel:cup', 'maryland-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('maryland-hostel:pants', 'maryland-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('maryland-hostel:reusable', 'maryland-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('maryland-hostel:heat', 'maryland-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('maryland-hostel:wipes', 'maryland-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('maryland-hostel:washbag', 'maryland-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('maryland-hostel:guide', 'maryland-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('maryland-home:pads', 'maryland-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('maryland-home:liners', 'maryland-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('maryland-home:tampons', 'maryland-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('maryland-home:cup', 'maryland-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('maryland-home:pants', 'maryland-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('maryland-home:reusable', 'maryland-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('maryland-home:heat', 'maryland-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('maryland-home:wipes', 'maryland-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('maryland-home:washbag', 'maryland-home', 'washbag', 'good', 'A private place for her things.'),
  ('maryland-home:guide', 'maryland-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('maryland-football:pads', 'maryland-football', 'pads', 'good', 'A simple backup for match day.'),
  ('maryland-football:liners', 'maryland-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('maryland-football:tampons', 'maryland-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('maryland-football:cup', 'maryland-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('maryland-football:pants', 'maryland-football', 'pants', 'good', 'Stay put while running.'),
  ('maryland-football:reusable', 'maryland-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('maryland-football:heat', 'maryland-football', 'heat', 'good', 'Helps cramps after training.'),
  ('maryland-football:wipes', 'maryland-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('maryland-football:washbag', 'maryland-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('maryland-football:guide', 'maryland-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('maryland-training:pads', 'maryland-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('maryland-training:liners', 'maryland-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('maryland-training:tampons', 'maryland-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('maryland-training:cup', 'maryland-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('maryland-training:pants', 'maryland-training', 'pants', 'good', 'Last through long sessions.'),
  ('maryland-training:reusable', 'maryland-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('maryland-training:heat', 'maryland-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('maryland-training:wipes', 'maryland-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('maryland-training:washbag', 'maryland-training', 'washbag', 'good', 'Private storage at work.'),
  ('maryland-training:guide', 'maryland-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ojota-school:pads', 'ojota-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ojota-school:liners', 'ojota-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ojota-school:tampons', 'ojota-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojota-school:cup', 'ojota-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojota-school:pants', 'ojota-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ojota-school:reusable', 'ojota-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ojota-school:heat', 'ojota-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ojota-school:wipes', 'ojota-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('ojota-school:washbag', 'ojota-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ojota-school:guide', 'ojota-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ojota-market:pads', 'ojota-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ojota-market:liners', 'ojota-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojota-market:tampons', 'ojota-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojota-market:cup', 'ojota-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojota-market:pants', 'ojota-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ojota-market:reusable', 'ojota-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ojota-market:heat', 'ojota-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ojota-market:wipes', 'ojota-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ojota-market:washbag', 'ojota-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ojota-market:guide', 'ojota-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ojota-busstop:pads', 'ojota-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ojota-busstop:liners', 'ojota-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojota-busstop:tampons', 'ojota-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojota-busstop:cup', 'ojota-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojota-busstop:pants', 'ojota-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ojota-busstop:reusable', 'ojota-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ojota-busstop:heat', 'ojota-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ojota-busstop:wipes', 'ojota-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ojota-busstop:washbag', 'ojota-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ojota-busstop:guide', 'ojota-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ojota-clinic:pads', 'ojota-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ojota-clinic:liners', 'ojota-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojota-clinic:tampons', 'ojota-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojota-clinic:cup', 'ojota-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ojota-clinic:pants', 'ojota-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ojota-clinic:reusable', 'ojota-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ojota-clinic:heat', 'ojota-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ojota-clinic:wipes', 'ojota-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ojota-clinic:washbag', 'ojota-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ojota-clinic:guide', 'ojota-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ojota-youth:pads', 'ojota-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ojota-youth:liners', 'ojota-youth', 'liners', 'good', 'For light days at the club.'),
  ('ojota-youth:tampons', 'ojota-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojota-youth:cup', 'ojota-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojota-youth:pants', 'ojota-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ojota-youth:reusable', 'ojota-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ojota-youth:heat', 'ojota-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ojota-youth:wipes', 'ojota-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ojota-youth:washbag', 'ojota-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ojota-youth:guide', 'ojota-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ojota-hostel:pads', 'ojota-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('ojota-hostel:liners', 'ojota-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojota-hostel:tampons', 'ojota-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojota-hostel:cup', 'ojota-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ojota-hostel:pants', 'ojota-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ojota-hostel:reusable', 'ojota-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('ojota-hostel:heat', 'ojota-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ojota-hostel:wipes', 'ojota-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ojota-hostel:washbag', 'ojota-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('ojota-hostel:guide', 'ojota-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('ojota-home:pads', 'ojota-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('ojota-home:liners', 'ojota-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojota-home:tampons', 'ojota-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojota-home:cup', 'ojota-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojota-home:pants', 'ojota-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ojota-home:reusable', 'ojota-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('ojota-home:heat', 'ojota-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ojota-home:wipes', 'ojota-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ojota-home:washbag', 'ojota-home', 'washbag', 'good', 'A private place for her things.'),
  ('ojota-home:guide', 'ojota-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('ojota-football:pads', 'ojota-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ojota-football:liners', 'ojota-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojota-football:tampons', 'ojota-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojota-football:cup', 'ojota-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ojota-football:pants', 'ojota-football', 'pants', 'good', 'Stay put while running.'),
  ('ojota-football:reusable', 'ojota-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ojota-football:heat', 'ojota-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ojota-football:wipes', 'ojota-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ojota-football:washbag', 'ojota-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ojota-football:guide', 'ojota-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ojota-training:pads', 'ojota-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ojota-training:liners', 'ojota-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojota-training:tampons', 'ojota-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojota-training:cup', 'ojota-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ojota-training:pants', 'ojota-training', 'pants', 'good', 'Last through long sessions.'),
  ('ojota-training:reusable', 'ojota-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ojota-training:heat', 'ojota-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ojota-training:wipes', 'ojota-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ojota-training:washbag', 'ojota-training', 'washbag', 'good', 'Private storage at work.'),
  ('ojota-training:guide', 'ojota-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ojota-shelter:pads', 'ojota-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('ojota-shelter:liners', 'ojota-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojota-shelter:tampons', 'ojota-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojota-shelter:cup', 'ojota-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojota-shelter:pants', 'ojota-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ojota-shelter:reusable', 'ojota-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ojota-shelter:heat', 'ojota-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('ojota-shelter:wipes', 'ojota-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('ojota-shelter:washbag', 'ojota-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('ojota-shelter:guide', 'ojota-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ketu-school:pads', 'ketu-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ketu-school:liners', 'ketu-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ketu-school:tampons', 'ketu-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ketu-school:cup', 'ketu-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ketu-school:pants', 'ketu-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ketu-school:reusable', 'ketu-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ketu-school:heat', 'ketu-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ketu-school:wipes', 'ketu-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('ketu-school:washbag', 'ketu-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ketu-school:guide', 'ketu-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ketu-market:pads', 'ketu-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ketu-market:liners', 'ketu-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ketu-market:tampons', 'ketu-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ketu-market:cup', 'ketu-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ketu-market:pants', 'ketu-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ketu-market:reusable', 'ketu-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ketu-market:heat', 'ketu-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ketu-market:wipes', 'ketu-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ketu-market:washbag', 'ketu-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ketu-market:guide', 'ketu-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ketu-busstop:pads', 'ketu-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ketu-busstop:liners', 'ketu-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ketu-busstop:tampons', 'ketu-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ketu-busstop:cup', 'ketu-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ketu-busstop:pants', 'ketu-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ketu-busstop:reusable', 'ketu-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ketu-busstop:heat', 'ketu-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ketu-busstop:wipes', 'ketu-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ketu-busstop:washbag', 'ketu-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ketu-busstop:guide', 'ketu-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ketu-clinic:pads', 'ketu-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ketu-clinic:liners', 'ketu-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ketu-clinic:tampons', 'ketu-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ketu-clinic:cup', 'ketu-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ketu-clinic:pants', 'ketu-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ketu-clinic:reusable', 'ketu-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ketu-clinic:heat', 'ketu-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ketu-clinic:wipes', 'ketu-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ketu-clinic:washbag', 'ketu-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ketu-clinic:guide', 'ketu-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ketu-youth:pads', 'ketu-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ketu-youth:liners', 'ketu-youth', 'liners', 'good', 'For light days at the club.'),
  ('ketu-youth:tampons', 'ketu-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ketu-youth:cup', 'ketu-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ketu-youth:pants', 'ketu-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ketu-youth:reusable', 'ketu-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ketu-youth:heat', 'ketu-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ketu-youth:wipes', 'ketu-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ketu-youth:washbag', 'ketu-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ketu-youth:guide', 'ketu-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ketu-hostel:pads', 'ketu-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('ketu-hostel:liners', 'ketu-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ketu-hostel:tampons', 'ketu-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ketu-hostel:cup', 'ketu-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ketu-hostel:pants', 'ketu-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ketu-hostel:reusable', 'ketu-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('ketu-hostel:heat', 'ketu-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ketu-hostel:wipes', 'ketu-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ketu-hostel:washbag', 'ketu-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('ketu-hostel:guide', 'ketu-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('ketu-home:pads', 'ketu-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('ketu-home:liners', 'ketu-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ketu-home:tampons', 'ketu-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ketu-home:cup', 'ketu-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ketu-home:pants', 'ketu-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ketu-home:reusable', 'ketu-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('ketu-home:heat', 'ketu-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ketu-home:wipes', 'ketu-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ketu-home:washbag', 'ketu-home', 'washbag', 'good', 'A private place for her things.'),
  ('ketu-home:guide', 'ketu-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('ketu-football:pads', 'ketu-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ketu-football:liners', 'ketu-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ketu-football:tampons', 'ketu-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ketu-football:cup', 'ketu-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ketu-football:pants', 'ketu-football', 'pants', 'good', 'Stay put while running.'),
  ('ketu-football:reusable', 'ketu-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ketu-football:heat', 'ketu-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ketu-football:wipes', 'ketu-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ketu-football:washbag', 'ketu-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ketu-football:guide', 'ketu-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ketu-training:pads', 'ketu-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ketu-training:liners', 'ketu-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ketu-training:tampons', 'ketu-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ketu-training:cup', 'ketu-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ketu-training:pants', 'ketu-training', 'pants', 'good', 'Last through long sessions.'),
  ('ketu-training:reusable', 'ketu-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ketu-training:heat', 'ketu-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ketu-training:wipes', 'ketu-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ketu-training:washbag', 'ketu-training', 'washbag', 'good', 'Private storage at work.'),
  ('ketu-training:guide', 'ketu-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ketu-shelter:pads', 'ketu-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('ketu-shelter:liners', 'ketu-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ketu-shelter:tampons', 'ketu-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ketu-shelter:cup', 'ketu-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ketu-shelter:pants', 'ketu-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ketu-shelter:reusable', 'ketu-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ketu-shelter:heat', 'ketu-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('ketu-shelter:wipes', 'ketu-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('ketu-shelter:washbag', 'ketu-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('ketu-shelter:guide', 'ketu-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('mile-12-school:pads', 'mile-12-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('mile-12-school:liners', 'mile-12-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('mile-12-school:tampons', 'mile-12-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-12-school:cup', 'mile-12-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('mile-12-school:pants', 'mile-12-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('mile-12-school:reusable', 'mile-12-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('mile-12-school:heat', 'mile-12-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('mile-12-school:wipes', 'mile-12-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('mile-12-school:washbag', 'mile-12-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('mile-12-school:guide', 'mile-12-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('mile-12-market:pads', 'mile-12-market', 'pads', 'good', 'Quick to change between customers.'),
  ('mile-12-market:liners', 'mile-12-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mile-12-market:tampons', 'mile-12-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-12-market:cup', 'mile-12-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('mile-12-market:pants', 'mile-12-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('mile-12-market:reusable', 'mile-12-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('mile-12-market:heat', 'mile-12-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('mile-12-market:wipes', 'mile-12-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('mile-12-market:washbag', 'mile-12-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('mile-12-market:guide', 'mile-12-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('mile-12-busstop:pads', 'mile-12-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('mile-12-busstop:liners', 'mile-12-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mile-12-busstop:tampons', 'mile-12-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-12-busstop:cup', 'mile-12-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('mile-12-busstop:pants', 'mile-12-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('mile-12-busstop:reusable', 'mile-12-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('mile-12-busstop:heat', 'mile-12-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('mile-12-busstop:wipes', 'mile-12-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('mile-12-busstop:washbag', 'mile-12-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('mile-12-busstop:guide', 'mile-12-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('mile-12-clinic:pads', 'mile-12-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('mile-12-clinic:liners', 'mile-12-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mile-12-clinic:tampons', 'mile-12-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-12-clinic:cup', 'mile-12-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('mile-12-clinic:pants', 'mile-12-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('mile-12-clinic:reusable', 'mile-12-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('mile-12-clinic:heat', 'mile-12-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('mile-12-clinic:wipes', 'mile-12-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('mile-12-clinic:washbag', 'mile-12-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('mile-12-clinic:guide', 'mile-12-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('mile-12-youth:pads', 'mile-12-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('mile-12-youth:liners', 'mile-12-youth', 'liners', 'good', 'For light days at the club.'),
  ('mile-12-youth:tampons', 'mile-12-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-12-youth:cup', 'mile-12-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('mile-12-youth:pants', 'mile-12-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('mile-12-youth:reusable', 'mile-12-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('mile-12-youth:heat', 'mile-12-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('mile-12-youth:wipes', 'mile-12-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('mile-12-youth:washbag', 'mile-12-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('mile-12-youth:guide', 'mile-12-youth', 'guide', 'good', 'Answers questions they have.'),
  ('mile-12-waterside:pads', 'mile-12-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('mile-12-waterside:liners', 'mile-12-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mile-12-waterside:tampons', 'mile-12-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('mile-12-waterside:cup', 'mile-12-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('mile-12-waterside:pants', 'mile-12-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('mile-12-waterside:reusable', 'mile-12-waterside', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('mile-12-waterside:heat', 'mile-12-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('mile-12-waterside:wipes', 'mile-12-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('mile-12-waterside:washbag', 'mile-12-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('mile-12-waterside:guide', 'mile-12-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('mile-12-hostel:pads', 'mile-12-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('mile-12-hostel:liners', 'mile-12-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mile-12-hostel:tampons', 'mile-12-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-12-hostel:cup', 'mile-12-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('mile-12-hostel:pants', 'mile-12-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('mile-12-hostel:reusable', 'mile-12-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('mile-12-hostel:heat', 'mile-12-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('mile-12-hostel:wipes', 'mile-12-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('mile-12-hostel:washbag', 'mile-12-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('mile-12-hostel:guide', 'mile-12-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('mile-12-football:pads', 'mile-12-football', 'pads', 'good', 'A simple backup for match day.'),
  ('mile-12-football:liners', 'mile-12-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mile-12-football:tampons', 'mile-12-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-12-football:cup', 'mile-12-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('mile-12-football:pants', 'mile-12-football', 'pants', 'good', 'Stay put while running.'),
  ('mile-12-football:reusable', 'mile-12-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('mile-12-football:heat', 'mile-12-football', 'heat', 'good', 'Helps cramps after training.'),
  ('mile-12-football:wipes', 'mile-12-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('mile-12-football:washbag', 'mile-12-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('mile-12-football:guide', 'mile-12-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('mile-12-training:pads', 'mile-12-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('mile-12-training:liners', 'mile-12-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mile-12-training:tampons', 'mile-12-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-12-training:cup', 'mile-12-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('mile-12-training:pants', 'mile-12-training', 'pants', 'good', 'Last through long sessions.'),
  ('mile-12-training:reusable', 'mile-12-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('mile-12-training:heat', 'mile-12-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('mile-12-training:wipes', 'mile-12-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('mile-12-training:washbag', 'mile-12-training', 'washbag', 'good', 'Private storage at work.'),
  ('mile-12-training:guide', 'mile-12-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('mile-12-shelter:pads', 'mile-12-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('mile-12-shelter:liners', 'mile-12-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('mile-12-shelter:tampons', 'mile-12-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('mile-12-shelter:cup', 'mile-12-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('mile-12-shelter:pants', 'mile-12-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('mile-12-shelter:reusable', 'mile-12-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('mile-12-shelter:heat', 'mile-12-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('mile-12-shelter:wipes', 'mile-12-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('mile-12-shelter:washbag', 'mile-12-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('mile-12-shelter:guide', 'mile-12-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikosi-school:pads', 'ikosi-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ikosi-school:liners', 'ikosi-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ikosi-school:tampons', 'ikosi-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikosi-school:cup', 'ikosi-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikosi-school:pants', 'ikosi-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikosi-school:reusable', 'ikosi-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikosi-school:heat', 'ikosi-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikosi-school:wipes', 'ikosi-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('ikosi-school:washbag', 'ikosi-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ikosi-school:guide', 'ikosi-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ikosi-market:pads', 'ikosi-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ikosi-market:liners', 'ikosi-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikosi-market:tampons', 'ikosi-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikosi-market:cup', 'ikosi-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikosi-market:pants', 'ikosi-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikosi-market:reusable', 'ikosi-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikosi-market:heat', 'ikosi-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ikosi-market:wipes', 'ikosi-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ikosi-market:washbag', 'ikosi-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ikosi-market:guide', 'ikosi-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikosi-busstop:pads', 'ikosi-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ikosi-busstop:liners', 'ikosi-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikosi-busstop:tampons', 'ikosi-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikosi-busstop:cup', 'ikosi-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikosi-busstop:pants', 'ikosi-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ikosi-busstop:reusable', 'ikosi-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikosi-busstop:heat', 'ikosi-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ikosi-busstop:wipes', 'ikosi-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ikosi-busstop:washbag', 'ikosi-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ikosi-busstop:guide', 'ikosi-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikosi-clinic:pads', 'ikosi-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ikosi-clinic:liners', 'ikosi-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikosi-clinic:tampons', 'ikosi-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikosi-clinic:cup', 'ikosi-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikosi-clinic:pants', 'ikosi-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ikosi-clinic:reusable', 'ikosi-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikosi-clinic:heat', 'ikosi-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ikosi-clinic:wipes', 'ikosi-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikosi-clinic:washbag', 'ikosi-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ikosi-clinic:guide', 'ikosi-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ikosi-youth:pads', 'ikosi-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ikosi-youth:liners', 'ikosi-youth', 'liners', 'good', 'For light days at the club.'),
  ('ikosi-youth:tampons', 'ikosi-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikosi-youth:cup', 'ikosi-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikosi-youth:pants', 'ikosi-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikosi-youth:reusable', 'ikosi-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikosi-youth:heat', 'ikosi-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikosi-youth:wipes', 'ikosi-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikosi-youth:washbag', 'ikosi-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ikosi-youth:guide', 'ikosi-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ikosi-hostel:pads', 'ikosi-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('ikosi-hostel:liners', 'ikosi-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikosi-hostel:tampons', 'ikosi-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikosi-hostel:cup', 'ikosi-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikosi-hostel:pants', 'ikosi-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikosi-hostel:reusable', 'ikosi-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('ikosi-hostel:heat', 'ikosi-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikosi-hostel:wipes', 'ikosi-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikosi-hostel:washbag', 'ikosi-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('ikosi-hostel:guide', 'ikosi-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('ikosi-home:pads', 'ikosi-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('ikosi-home:liners', 'ikosi-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikosi-home:tampons', 'ikosi-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikosi-home:cup', 'ikosi-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikosi-home:pants', 'ikosi-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikosi-home:reusable', 'ikosi-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('ikosi-home:heat', 'ikosi-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikosi-home:wipes', 'ikosi-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikosi-home:washbag', 'ikosi-home', 'washbag', 'good', 'A private place for her things.'),
  ('ikosi-home:guide', 'ikosi-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('ikosi-football:pads', 'ikosi-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ikosi-football:liners', 'ikosi-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikosi-football:tampons', 'ikosi-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikosi-football:cup', 'ikosi-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikosi-football:pants', 'ikosi-football', 'pants', 'good', 'Stay put while running.'),
  ('ikosi-football:reusable', 'ikosi-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikosi-football:heat', 'ikosi-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ikosi-football:wipes', 'ikosi-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikosi-football:washbag', 'ikosi-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ikosi-football:guide', 'ikosi-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikosi-training:pads', 'ikosi-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ikosi-training:liners', 'ikosi-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikosi-training:tampons', 'ikosi-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikosi-training:cup', 'ikosi-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikosi-training:pants', 'ikosi-training', 'pants', 'good', 'Last through long sessions.'),
  ('ikosi-training:reusable', 'ikosi-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikosi-training:heat', 'ikosi-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ikosi-training:wipes', 'ikosi-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikosi-training:washbag', 'ikosi-training', 'washbag', 'good', 'Private storage at work.'),
  ('ikosi-training:guide', 'ikosi-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('magodo-school:pads', 'magodo-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('magodo-school:liners', 'magodo-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('magodo-school:tampons', 'magodo-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('magodo-school:cup', 'magodo-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('magodo-school:pants', 'magodo-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('magodo-school:reusable', 'magodo-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('magodo-school:heat', 'magodo-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('magodo-school:wipes', 'magodo-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('magodo-school:washbag', 'magodo-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('magodo-school:guide', 'magodo-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('magodo-market:pads', 'magodo-market', 'pads', 'good', 'Quick to change between customers.'),
  ('magodo-market:liners', 'magodo-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('magodo-market:tampons', 'magodo-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('magodo-market:cup', 'magodo-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('magodo-market:pants', 'magodo-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('magodo-market:reusable', 'magodo-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('magodo-market:heat', 'magodo-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('magodo-market:wipes', 'magodo-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('magodo-market:washbag', 'magodo-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('magodo-market:guide', 'magodo-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('magodo-busstop:pads', 'magodo-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('magodo-busstop:liners', 'magodo-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('magodo-busstop:tampons', 'magodo-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('magodo-busstop:cup', 'magodo-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('magodo-busstop:pants', 'magodo-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('magodo-busstop:reusable', 'magodo-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('magodo-busstop:heat', 'magodo-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('magodo-busstop:wipes', 'magodo-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('magodo-busstop:washbag', 'magodo-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('magodo-busstop:guide', 'magodo-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('magodo-clinic:pads', 'magodo-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('magodo-clinic:liners', 'magodo-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('magodo-clinic:tampons', 'magodo-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('magodo-clinic:cup', 'magodo-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('magodo-clinic:pants', 'magodo-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('magodo-clinic:reusable', 'magodo-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('magodo-clinic:heat', 'magodo-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('magodo-clinic:wipes', 'magodo-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('magodo-clinic:washbag', 'magodo-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('magodo-clinic:guide', 'magodo-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('magodo-youth:pads', 'magodo-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('magodo-youth:liners', 'magodo-youth', 'liners', 'good', 'For light days at the club.'),
  ('magodo-youth:tampons', 'magodo-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('magodo-youth:cup', 'magodo-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('magodo-youth:pants', 'magodo-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('magodo-youth:reusable', 'magodo-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('magodo-youth:heat', 'magodo-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('magodo-youth:wipes', 'magodo-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('magodo-youth:washbag', 'magodo-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('magodo-youth:guide', 'magodo-youth', 'guide', 'good', 'Answers questions they have.'),
  ('magodo-hostel:pads', 'magodo-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('magodo-hostel:liners', 'magodo-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('magodo-hostel:tampons', 'magodo-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('magodo-hostel:cup', 'magodo-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('magodo-hostel:pants', 'magodo-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('magodo-hostel:reusable', 'magodo-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('magodo-hostel:heat', 'magodo-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('magodo-hostel:wipes', 'magodo-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('magodo-hostel:washbag', 'magodo-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('magodo-hostel:guide', 'magodo-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('magodo-home:pads', 'magodo-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('magodo-home:liners', 'magodo-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('magodo-home:tampons', 'magodo-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('magodo-home:cup', 'magodo-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('magodo-home:pants', 'magodo-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('magodo-home:reusable', 'magodo-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('magodo-home:heat', 'magodo-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('magodo-home:wipes', 'magodo-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('magodo-home:washbag', 'magodo-home', 'washbag', 'good', 'A private place for her things.'),
  ('magodo-home:guide', 'magodo-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('magodo-football:pads', 'magodo-football', 'pads', 'good', 'A simple backup for match day.'),
  ('magodo-football:liners', 'magodo-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('magodo-football:tampons', 'magodo-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('magodo-football:cup', 'magodo-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('magodo-football:pants', 'magodo-football', 'pants', 'good', 'Stay put while running.'),
  ('magodo-football:reusable', 'magodo-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('magodo-football:heat', 'magodo-football', 'heat', 'good', 'Helps cramps after training.'),
  ('magodo-football:wipes', 'magodo-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('magodo-football:washbag', 'magodo-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('magodo-football:guide', 'magodo-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('magodo-training:pads', 'magodo-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('magodo-training:liners', 'magodo-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('magodo-training:tampons', 'magodo-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('magodo-training:cup', 'magodo-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('magodo-training:pants', 'magodo-training', 'pants', 'good', 'Last through long sessions.'),
  ('magodo-training:reusable', 'magodo-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('magodo-training:heat', 'magodo-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('magodo-training:wipes', 'magodo-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('magodo-training:washbag', 'magodo-training', 'washbag', 'good', 'Private storage at work.'),
  ('magodo-training:guide', 'magodo-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikeja-school:pads', 'ikeja-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ikeja-school:liners', 'ikeja-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ikeja-school:tampons', 'ikeja-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikeja-school:cup', 'ikeja-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikeja-school:pants', 'ikeja-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikeja-school:reusable', 'ikeja-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikeja-school:heat', 'ikeja-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikeja-school:wipes', 'ikeja-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('ikeja-school:washbag', 'ikeja-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ikeja-school:guide', 'ikeja-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ikeja-market:pads', 'ikeja-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ikeja-market:liners', 'ikeja-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikeja-market:tampons', 'ikeja-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikeja-market:cup', 'ikeja-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikeja-market:pants', 'ikeja-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikeja-market:reusable', 'ikeja-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikeja-market:heat', 'ikeja-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ikeja-market:wipes', 'ikeja-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ikeja-market:washbag', 'ikeja-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ikeja-market:guide', 'ikeja-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikeja-busstop:pads', 'ikeja-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ikeja-busstop:liners', 'ikeja-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikeja-busstop:tampons', 'ikeja-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikeja-busstop:cup', 'ikeja-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikeja-busstop:pants', 'ikeja-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ikeja-busstop:reusable', 'ikeja-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikeja-busstop:heat', 'ikeja-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ikeja-busstop:wipes', 'ikeja-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ikeja-busstop:washbag', 'ikeja-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ikeja-busstop:guide', 'ikeja-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikeja-clinic:pads', 'ikeja-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ikeja-clinic:liners', 'ikeja-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikeja-clinic:tampons', 'ikeja-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikeja-clinic:cup', 'ikeja-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikeja-clinic:pants', 'ikeja-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ikeja-clinic:reusable', 'ikeja-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikeja-clinic:heat', 'ikeja-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ikeja-clinic:wipes', 'ikeja-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikeja-clinic:washbag', 'ikeja-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ikeja-clinic:guide', 'ikeja-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ikeja-youth:pads', 'ikeja-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ikeja-youth:liners', 'ikeja-youth', 'liners', 'good', 'For light days at the club.'),
  ('ikeja-youth:tampons', 'ikeja-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikeja-youth:cup', 'ikeja-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikeja-youth:pants', 'ikeja-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikeja-youth:reusable', 'ikeja-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikeja-youth:heat', 'ikeja-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikeja-youth:wipes', 'ikeja-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikeja-youth:washbag', 'ikeja-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ikeja-youth:guide', 'ikeja-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ikeja-hostel:pads', 'ikeja-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('ikeja-hostel:liners', 'ikeja-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikeja-hostel:tampons', 'ikeja-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikeja-hostel:cup', 'ikeja-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikeja-hostel:pants', 'ikeja-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikeja-hostel:reusable', 'ikeja-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('ikeja-hostel:heat', 'ikeja-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikeja-hostel:wipes', 'ikeja-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikeja-hostel:washbag', 'ikeja-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('ikeja-hostel:guide', 'ikeja-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('ikeja-home:pads', 'ikeja-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('ikeja-home:liners', 'ikeja-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikeja-home:tampons', 'ikeja-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikeja-home:cup', 'ikeja-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikeja-home:pants', 'ikeja-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikeja-home:reusable', 'ikeja-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('ikeja-home:heat', 'ikeja-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikeja-home:wipes', 'ikeja-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikeja-home:washbag', 'ikeja-home', 'washbag', 'good', 'A private place for her things.'),
  ('ikeja-home:guide', 'ikeja-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('ikeja-football:pads', 'ikeja-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ikeja-football:liners', 'ikeja-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikeja-football:tampons', 'ikeja-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikeja-football:cup', 'ikeja-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikeja-football:pants', 'ikeja-football', 'pants', 'good', 'Stay put while running.'),
  ('ikeja-football:reusable', 'ikeja-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikeja-football:heat', 'ikeja-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ikeja-football:wipes', 'ikeja-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikeja-football:washbag', 'ikeja-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ikeja-football:guide', 'ikeja-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikeja-training:pads', 'ikeja-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ikeja-training:liners', 'ikeja-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikeja-training:tampons', 'ikeja-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikeja-training:cup', 'ikeja-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikeja-training:pants', 'ikeja-training', 'pants', 'good', 'Last through long sessions.'),
  ('ikeja-training:reusable', 'ikeja-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikeja-training:heat', 'ikeja-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ikeja-training:wipes', 'ikeja-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikeja-training:washbag', 'ikeja-training', 'washbag', 'good', 'Private storage at work.'),
  ('ikeja-training:guide', 'ikeja-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ogba-school:pads', 'ogba-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ogba-school:liners', 'ogba-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ogba-school:tampons', 'ogba-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ogba-school:cup', 'ogba-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ogba-school:pants', 'ogba-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ogba-school:reusable', 'ogba-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ogba-school:heat', 'ogba-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ogba-school:wipes', 'ogba-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('ogba-school:washbag', 'ogba-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ogba-school:guide', 'ogba-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ogba-market:pads', 'ogba-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ogba-market:liners', 'ogba-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ogba-market:tampons', 'ogba-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ogba-market:cup', 'ogba-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ogba-market:pants', 'ogba-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ogba-market:reusable', 'ogba-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ogba-market:heat', 'ogba-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ogba-market:wipes', 'ogba-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ogba-market:washbag', 'ogba-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ogba-market:guide', 'ogba-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ogba-busstop:pads', 'ogba-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ogba-busstop:liners', 'ogba-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ogba-busstop:tampons', 'ogba-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ogba-busstop:cup', 'ogba-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ogba-busstop:pants', 'ogba-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ogba-busstop:reusable', 'ogba-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ogba-busstop:heat', 'ogba-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ogba-busstop:wipes', 'ogba-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ogba-busstop:washbag', 'ogba-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ogba-busstop:guide', 'ogba-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ogba-clinic:pads', 'ogba-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ogba-clinic:liners', 'ogba-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ogba-clinic:tampons', 'ogba-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ogba-clinic:cup', 'ogba-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ogba-clinic:pants', 'ogba-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ogba-clinic:reusable', 'ogba-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ogba-clinic:heat', 'ogba-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ogba-clinic:wipes', 'ogba-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ogba-clinic:washbag', 'ogba-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ogba-clinic:guide', 'ogba-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ogba-youth:pads', 'ogba-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ogba-youth:liners', 'ogba-youth', 'liners', 'good', 'For light days at the club.'),
  ('ogba-youth:tampons', 'ogba-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ogba-youth:cup', 'ogba-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ogba-youth:pants', 'ogba-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ogba-youth:reusable', 'ogba-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ogba-youth:heat', 'ogba-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ogba-youth:wipes', 'ogba-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ogba-youth:washbag', 'ogba-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ogba-youth:guide', 'ogba-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ogba-hostel:pads', 'ogba-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('ogba-hostel:liners', 'ogba-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ogba-hostel:tampons', 'ogba-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ogba-hostel:cup', 'ogba-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ogba-hostel:pants', 'ogba-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ogba-hostel:reusable', 'ogba-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('ogba-hostel:heat', 'ogba-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ogba-hostel:wipes', 'ogba-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ogba-hostel:washbag', 'ogba-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('ogba-hostel:guide', 'ogba-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('ogba-home:pads', 'ogba-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('ogba-home:liners', 'ogba-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ogba-home:tampons', 'ogba-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ogba-home:cup', 'ogba-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ogba-home:pants', 'ogba-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ogba-home:reusable', 'ogba-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('ogba-home:heat', 'ogba-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ogba-home:wipes', 'ogba-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ogba-home:washbag', 'ogba-home', 'washbag', 'good', 'A private place for her things.'),
  ('ogba-home:guide', 'ogba-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('ogba-football:pads', 'ogba-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ogba-football:liners', 'ogba-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ogba-football:tampons', 'ogba-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ogba-football:cup', 'ogba-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ogba-football:pants', 'ogba-football', 'pants', 'good', 'Stay put while running.'),
  ('ogba-football:reusable', 'ogba-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ogba-football:heat', 'ogba-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ogba-football:wipes', 'ogba-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ogba-football:washbag', 'ogba-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ogba-football:guide', 'ogba-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ogba-training:pads', 'ogba-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ogba-training:liners', 'ogba-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ogba-training:tampons', 'ogba-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ogba-training:cup', 'ogba-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ogba-training:pants', 'ogba-training', 'pants', 'good', 'Last through long sessions.'),
  ('ogba-training:reusable', 'ogba-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ogba-training:heat', 'ogba-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ogba-training:wipes', 'ogba-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ogba-training:washbag', 'ogba-training', 'washbag', 'good', 'Private storage at work.'),
  ('ogba-training:guide', 'ogba-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ogba-shelter:pads', 'ogba-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('ogba-shelter:liners', 'ogba-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ogba-shelter:tampons', 'ogba-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ogba-shelter:cup', 'ogba-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ogba-shelter:pants', 'ogba-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ogba-shelter:reusable', 'ogba-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ogba-shelter:heat', 'ogba-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('ogba-shelter:wipes', 'ogba-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('ogba-shelter:washbag', 'ogba-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('ogba-shelter:guide', 'ogba-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('agege-school:pads', 'agege-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('agege-school:liners', 'agege-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('agege-school:tampons', 'agege-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('agege-school:cup', 'agege-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('agege-school:pants', 'agege-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('agege-school:reusable', 'agege-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('agege-school:heat', 'agege-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('agege-school:wipes', 'agege-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('agege-school:washbag', 'agege-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('agege-school:guide', 'agege-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.');

INSERT INTO giveday_needs (id, place_id, product_id, rating, why) VALUES
  ('agege-market:pads', 'agege-market', 'pads', 'good', 'Quick to change between customers.'),
  ('agege-market:liners', 'agege-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('agege-market:tampons', 'agege-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('agege-market:cup', 'agege-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('agege-market:pants', 'agege-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('agege-market:reusable', 'agege-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('agege-market:heat', 'agege-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('agege-market:wipes', 'agege-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('agege-market:washbag', 'agege-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('agege-market:guide', 'agege-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('agege-busstop:pads', 'agege-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('agege-busstop:liners', 'agege-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('agege-busstop:tampons', 'agege-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('agege-busstop:cup', 'agege-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('agege-busstop:pants', 'agege-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('agege-busstop:reusable', 'agege-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('agege-busstop:heat', 'agege-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('agege-busstop:wipes', 'agege-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('agege-busstop:washbag', 'agege-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('agege-busstop:guide', 'agege-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('agege-clinic:pads', 'agege-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('agege-clinic:liners', 'agege-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('agege-clinic:tampons', 'agege-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('agege-clinic:cup', 'agege-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('agege-clinic:pants', 'agege-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('agege-clinic:reusable', 'agege-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('agege-clinic:heat', 'agege-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('agege-clinic:wipes', 'agege-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('agege-clinic:washbag', 'agege-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('agege-clinic:guide', 'agege-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('agege-youth:pads', 'agege-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('agege-youth:liners', 'agege-youth', 'liners', 'good', 'For light days at the club.'),
  ('agege-youth:tampons', 'agege-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('agege-youth:cup', 'agege-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('agege-youth:pants', 'agege-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('agege-youth:reusable', 'agege-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('agege-youth:heat', 'agege-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('agege-youth:wipes', 'agege-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('agege-youth:washbag', 'agege-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('agege-youth:guide', 'agege-youth', 'guide', 'good', 'Answers questions they have.'),
  ('agege-hostel:pads', 'agege-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('agege-hostel:liners', 'agege-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('agege-hostel:tampons', 'agege-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('agege-hostel:cup', 'agege-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('agege-hostel:pants', 'agege-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('agege-hostel:reusable', 'agege-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('agege-hostel:heat', 'agege-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('agege-hostel:wipes', 'agege-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('agege-hostel:washbag', 'agege-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('agege-hostel:guide', 'agege-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('agege-home:pads', 'agege-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('agege-home:liners', 'agege-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('agege-home:tampons', 'agege-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('agege-home:cup', 'agege-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('agege-home:pants', 'agege-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('agege-home:reusable', 'agege-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('agege-home:heat', 'agege-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('agege-home:wipes', 'agege-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('agege-home:washbag', 'agege-home', 'washbag', 'good', 'A private place for her things.'),
  ('agege-home:guide', 'agege-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('agege-football:pads', 'agege-football', 'pads', 'good', 'A simple backup for match day.'),
  ('agege-football:liners', 'agege-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('agege-football:tampons', 'agege-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('agege-football:cup', 'agege-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('agege-football:pants', 'agege-football', 'pants', 'good', 'Stay put while running.'),
  ('agege-football:reusable', 'agege-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('agege-football:heat', 'agege-football', 'heat', 'good', 'Helps cramps after training.'),
  ('agege-football:wipes', 'agege-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('agege-football:washbag', 'agege-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('agege-football:guide', 'agege-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('agege-training:pads', 'agege-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('agege-training:liners', 'agege-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('agege-training:tampons', 'agege-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('agege-training:cup', 'agege-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('agege-training:pants', 'agege-training', 'pants', 'good', 'Last through long sessions.'),
  ('agege-training:reusable', 'agege-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('agege-training:heat', 'agege-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('agege-training:wipes', 'agege-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('agege-training:washbag', 'agege-training', 'washbag', 'good', 'Private storage at work.'),
  ('agege-training:guide', 'agege-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ifako-school:pads', 'ifako-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ifako-school:liners', 'ifako-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ifako-school:tampons', 'ifako-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ifako-school:cup', 'ifako-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ifako-school:pants', 'ifako-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ifako-school:reusable', 'ifako-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ifako-school:heat', 'ifako-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ifako-school:wipes', 'ifako-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('ifako-school:washbag', 'ifako-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ifako-school:guide', 'ifako-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ifako-market:pads', 'ifako-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ifako-market:liners', 'ifako-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ifako-market:tampons', 'ifako-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ifako-market:cup', 'ifako-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ifako-market:pants', 'ifako-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ifako-market:reusable', 'ifako-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ifako-market:heat', 'ifako-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ifako-market:wipes', 'ifako-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ifako-market:washbag', 'ifako-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ifako-market:guide', 'ifako-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ifako-busstop:pads', 'ifako-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ifako-busstop:liners', 'ifako-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ifako-busstop:tampons', 'ifako-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ifako-busstop:cup', 'ifako-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ifako-busstop:pants', 'ifako-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ifako-busstop:reusable', 'ifako-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ifako-busstop:heat', 'ifako-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ifako-busstop:wipes', 'ifako-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ifako-busstop:washbag', 'ifako-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ifako-busstop:guide', 'ifako-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ifako-clinic:pads', 'ifako-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ifako-clinic:liners', 'ifako-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ifako-clinic:tampons', 'ifako-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ifako-clinic:cup', 'ifako-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ifako-clinic:pants', 'ifako-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ifako-clinic:reusable', 'ifako-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ifako-clinic:heat', 'ifako-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ifako-clinic:wipes', 'ifako-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ifako-clinic:washbag', 'ifako-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ifako-clinic:guide', 'ifako-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ifako-youth:pads', 'ifako-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ifako-youth:liners', 'ifako-youth', 'liners', 'good', 'For light days at the club.'),
  ('ifako-youth:tampons', 'ifako-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ifako-youth:cup', 'ifako-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ifako-youth:pants', 'ifako-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ifako-youth:reusable', 'ifako-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ifako-youth:heat', 'ifako-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ifako-youth:wipes', 'ifako-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ifako-youth:washbag', 'ifako-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ifako-youth:guide', 'ifako-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ifako-hostel:pads', 'ifako-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('ifako-hostel:liners', 'ifako-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ifako-hostel:tampons', 'ifako-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ifako-hostel:cup', 'ifako-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ifako-hostel:pants', 'ifako-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ifako-hostel:reusable', 'ifako-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('ifako-hostel:heat', 'ifako-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ifako-hostel:wipes', 'ifako-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ifako-hostel:washbag', 'ifako-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('ifako-hostel:guide', 'ifako-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('ifako-home:pads', 'ifako-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('ifako-home:liners', 'ifako-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ifako-home:tampons', 'ifako-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ifako-home:cup', 'ifako-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ifako-home:pants', 'ifako-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ifako-home:reusable', 'ifako-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('ifako-home:heat', 'ifako-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ifako-home:wipes', 'ifako-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ifako-home:washbag', 'ifako-home', 'washbag', 'good', 'A private place for her things.'),
  ('ifako-home:guide', 'ifako-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('ifako-football:pads', 'ifako-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ifako-football:liners', 'ifako-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ifako-football:tampons', 'ifako-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ifako-football:cup', 'ifako-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ifako-football:pants', 'ifako-football', 'pants', 'good', 'Stay put while running.'),
  ('ifako-football:reusable', 'ifako-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ifako-football:heat', 'ifako-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ifako-football:wipes', 'ifako-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ifako-football:washbag', 'ifako-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ifako-football:guide', 'ifako-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ifako-training:pads', 'ifako-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ifako-training:liners', 'ifako-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ifako-training:tampons', 'ifako-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ifako-training:cup', 'ifako-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ifako-training:pants', 'ifako-training', 'pants', 'good', 'Last through long sessions.'),
  ('ifako-training:reusable', 'ifako-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ifako-training:heat', 'ifako-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ifako-training:wipes', 'ifako-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ifako-training:washbag', 'ifako-training', 'washbag', 'good', 'Private storage at work.'),
  ('ifako-training:guide', 'ifako-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ifako-shelter:pads', 'ifako-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('ifako-shelter:liners', 'ifako-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ifako-shelter:tampons', 'ifako-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ifako-shelter:cup', 'ifako-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ifako-shelter:pants', 'ifako-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ifako-shelter:reusable', 'ifako-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ifako-shelter:heat', 'ifako-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('ifako-shelter:wipes', 'ifako-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('ifako-shelter:washbag', 'ifako-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('ifako-shelter:guide', 'ifako-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ojodu-school:pads', 'ojodu-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ojodu-school:liners', 'ojodu-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ojodu-school:tampons', 'ojodu-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojodu-school:cup', 'ojodu-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojodu-school:pants', 'ojodu-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ojodu-school:reusable', 'ojodu-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ojodu-school:heat', 'ojodu-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ojodu-school:wipes', 'ojodu-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('ojodu-school:washbag', 'ojodu-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ojodu-school:guide', 'ojodu-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ojodu-market:pads', 'ojodu-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ojodu-market:liners', 'ojodu-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojodu-market:tampons', 'ojodu-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojodu-market:cup', 'ojodu-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojodu-market:pants', 'ojodu-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ojodu-market:reusable', 'ojodu-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ojodu-market:heat', 'ojodu-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ojodu-market:wipes', 'ojodu-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ojodu-market:washbag', 'ojodu-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ojodu-market:guide', 'ojodu-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ojodu-busstop:pads', 'ojodu-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ojodu-busstop:liners', 'ojodu-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojodu-busstop:tampons', 'ojodu-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojodu-busstop:cup', 'ojodu-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojodu-busstop:pants', 'ojodu-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ojodu-busstop:reusable', 'ojodu-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ojodu-busstop:heat', 'ojodu-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ojodu-busstop:wipes', 'ojodu-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ojodu-busstop:washbag', 'ojodu-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ojodu-busstop:guide', 'ojodu-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ojodu-clinic:pads', 'ojodu-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ojodu-clinic:liners', 'ojodu-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojodu-clinic:tampons', 'ojodu-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojodu-clinic:cup', 'ojodu-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ojodu-clinic:pants', 'ojodu-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ojodu-clinic:reusable', 'ojodu-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ojodu-clinic:heat', 'ojodu-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ojodu-clinic:wipes', 'ojodu-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ojodu-clinic:washbag', 'ojodu-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ojodu-clinic:guide', 'ojodu-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ojodu-youth:pads', 'ojodu-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ojodu-youth:liners', 'ojodu-youth', 'liners', 'good', 'For light days at the club.'),
  ('ojodu-youth:tampons', 'ojodu-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojodu-youth:cup', 'ojodu-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojodu-youth:pants', 'ojodu-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ojodu-youth:reusable', 'ojodu-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ojodu-youth:heat', 'ojodu-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ojodu-youth:wipes', 'ojodu-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ojodu-youth:washbag', 'ojodu-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ojodu-youth:guide', 'ojodu-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ojodu-hostel:pads', 'ojodu-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('ojodu-hostel:liners', 'ojodu-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojodu-hostel:tampons', 'ojodu-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojodu-hostel:cup', 'ojodu-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ojodu-hostel:pants', 'ojodu-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ojodu-hostel:reusable', 'ojodu-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('ojodu-hostel:heat', 'ojodu-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ojodu-hostel:wipes', 'ojodu-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ojodu-hostel:washbag', 'ojodu-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('ojodu-hostel:guide', 'ojodu-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('ojodu-home:pads', 'ojodu-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('ojodu-home:liners', 'ojodu-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojodu-home:tampons', 'ojodu-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojodu-home:cup', 'ojodu-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojodu-home:pants', 'ojodu-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ojodu-home:reusable', 'ojodu-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('ojodu-home:heat', 'ojodu-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ojodu-home:wipes', 'ojodu-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ojodu-home:washbag', 'ojodu-home', 'washbag', 'good', 'A private place for her things.'),
  ('ojodu-home:guide', 'ojodu-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('ojodu-football:pads', 'ojodu-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ojodu-football:liners', 'ojodu-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojodu-football:tampons', 'ojodu-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojodu-football:cup', 'ojodu-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ojodu-football:pants', 'ojodu-football', 'pants', 'good', 'Stay put while running.'),
  ('ojodu-football:reusable', 'ojodu-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ojodu-football:heat', 'ojodu-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ojodu-football:wipes', 'ojodu-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ojodu-football:washbag', 'ojodu-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ojodu-football:guide', 'ojodu-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ojodu-training:pads', 'ojodu-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ojodu-training:liners', 'ojodu-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojodu-training:tampons', 'ojodu-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojodu-training:cup', 'ojodu-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojodu-training:pants', 'ojodu-training', 'pants', 'good', 'Last through long sessions.'),
  ('ojodu-training:reusable', 'ojodu-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ojodu-training:heat', 'ojodu-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ojodu-training:wipes', 'ojodu-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ojodu-training:washbag', 'ojodu-training', 'washbag', 'good', 'Private storage at work.'),
  ('ojodu-training:guide', 'ojodu-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ojodu-shelter:pads', 'ojodu-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('ojodu-shelter:liners', 'ojodu-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ojodu-shelter:tampons', 'ojodu-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ojodu-shelter:cup', 'ojodu-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ojodu-shelter:pants', 'ojodu-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ojodu-shelter:reusable', 'ojodu-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ojodu-shelter:heat', 'ojodu-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('ojodu-shelter:wipes', 'ojodu-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('ojodu-shelter:washbag', 'ojodu-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('ojodu-shelter:guide', 'ojodu-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('berger-school:pads', 'berger-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('berger-school:liners', 'berger-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('berger-school:tampons', 'berger-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('berger-school:cup', 'berger-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('berger-school:pants', 'berger-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('berger-school:reusable', 'berger-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('berger-school:heat', 'berger-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('berger-school:wipes', 'berger-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('berger-school:washbag', 'berger-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('berger-school:guide', 'berger-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('berger-market:pads', 'berger-market', 'pads', 'good', 'Quick to change between customers.'),
  ('berger-market:liners', 'berger-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('berger-market:tampons', 'berger-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('berger-market:cup', 'berger-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('berger-market:pants', 'berger-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('berger-market:reusable', 'berger-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('berger-market:heat', 'berger-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('berger-market:wipes', 'berger-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('berger-market:washbag', 'berger-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('berger-market:guide', 'berger-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('berger-busstop:pads', 'berger-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('berger-busstop:liners', 'berger-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('berger-busstop:tampons', 'berger-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('berger-busstop:cup', 'berger-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('berger-busstop:pants', 'berger-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('berger-busstop:reusable', 'berger-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('berger-busstop:heat', 'berger-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('berger-busstop:wipes', 'berger-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('berger-busstop:washbag', 'berger-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('berger-busstop:guide', 'berger-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('berger-clinic:pads', 'berger-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('berger-clinic:liners', 'berger-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('berger-clinic:tampons', 'berger-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('berger-clinic:cup', 'berger-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('berger-clinic:pants', 'berger-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('berger-clinic:reusable', 'berger-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('berger-clinic:heat', 'berger-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('berger-clinic:wipes', 'berger-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('berger-clinic:washbag', 'berger-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('berger-clinic:guide', 'berger-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('berger-youth:pads', 'berger-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('berger-youth:liners', 'berger-youth', 'liners', 'good', 'For light days at the club.'),
  ('berger-youth:tampons', 'berger-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('berger-youth:cup', 'berger-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('berger-youth:pants', 'berger-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('berger-youth:reusable', 'berger-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('berger-youth:heat', 'berger-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('berger-youth:wipes', 'berger-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('berger-youth:washbag', 'berger-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('berger-youth:guide', 'berger-youth', 'guide', 'good', 'Answers questions they have.'),
  ('berger-hostel:pads', 'berger-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('berger-hostel:liners', 'berger-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('berger-hostel:tampons', 'berger-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('berger-hostel:cup', 'berger-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('berger-hostel:pants', 'berger-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('berger-hostel:reusable', 'berger-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('berger-hostel:heat', 'berger-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('berger-hostel:wipes', 'berger-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('berger-hostel:washbag', 'berger-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('berger-hostel:guide', 'berger-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('berger-home:pads', 'berger-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('berger-home:liners', 'berger-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('berger-home:tampons', 'berger-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('berger-home:cup', 'berger-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('berger-home:pants', 'berger-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('berger-home:reusable', 'berger-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('berger-home:heat', 'berger-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('berger-home:wipes', 'berger-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('berger-home:washbag', 'berger-home', 'washbag', 'good', 'A private place for her things.'),
  ('berger-home:guide', 'berger-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('berger-football:pads', 'berger-football', 'pads', 'good', 'A simple backup for match day.'),
  ('berger-football:liners', 'berger-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('berger-football:tampons', 'berger-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('berger-football:cup', 'berger-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('berger-football:pants', 'berger-football', 'pants', 'good', 'Stay put while running.'),
  ('berger-football:reusable', 'berger-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('berger-football:heat', 'berger-football', 'heat', 'good', 'Helps cramps after training.'),
  ('berger-football:wipes', 'berger-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('berger-football:washbag', 'berger-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('berger-football:guide', 'berger-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('berger-training:pads', 'berger-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('berger-training:liners', 'berger-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('berger-training:tampons', 'berger-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('berger-training:cup', 'berger-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('berger-training:pants', 'berger-training', 'pants', 'good', 'Last through long sessions.'),
  ('berger-training:reusable', 'berger-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('berger-training:heat', 'berger-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('berger-training:wipes', 'berger-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('berger-training:washbag', 'berger-training', 'washbag', 'good', 'Private storage at work.'),
  ('berger-training:guide', 'berger-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('berger-shelter:pads', 'berger-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('berger-shelter:liners', 'berger-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('berger-shelter:tampons', 'berger-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('berger-shelter:cup', 'berger-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('berger-shelter:pants', 'berger-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('berger-shelter:reusable', 'berger-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('berger-shelter:heat', 'berger-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('berger-shelter:wipes', 'berger-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('berger-shelter:washbag', 'berger-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('berger-shelter:guide', 'berger-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikorodu-school:pads', 'ikorodu-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ikorodu-school:liners', 'ikorodu-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ikorodu-school:tampons', 'ikorodu-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikorodu-school:cup', 'ikorodu-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikorodu-school:pants', 'ikorodu-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikorodu-school:reusable', 'ikorodu-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikorodu-school:heat', 'ikorodu-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikorodu-school:wipes', 'ikorodu-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('ikorodu-school:washbag', 'ikorodu-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ikorodu-school:guide', 'ikorodu-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ikorodu-market:pads', 'ikorodu-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ikorodu-market:liners', 'ikorodu-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikorodu-market:tampons', 'ikorodu-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikorodu-market:cup', 'ikorodu-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikorodu-market:pants', 'ikorodu-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikorodu-market:reusable', 'ikorodu-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikorodu-market:heat', 'ikorodu-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ikorodu-market:wipes', 'ikorodu-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ikorodu-market:washbag', 'ikorodu-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ikorodu-market:guide', 'ikorodu-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikorodu-busstop:pads', 'ikorodu-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ikorodu-busstop:liners', 'ikorodu-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikorodu-busstop:tampons', 'ikorodu-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikorodu-busstop:cup', 'ikorodu-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikorodu-busstop:pants', 'ikorodu-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ikorodu-busstop:reusable', 'ikorodu-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikorodu-busstop:heat', 'ikorodu-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ikorodu-busstop:wipes', 'ikorodu-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ikorodu-busstop:washbag', 'ikorodu-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ikorodu-busstop:guide', 'ikorodu-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikorodu-clinic:pads', 'ikorodu-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ikorodu-clinic:liners', 'ikorodu-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikorodu-clinic:tampons', 'ikorodu-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikorodu-clinic:cup', 'ikorodu-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikorodu-clinic:pants', 'ikorodu-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ikorodu-clinic:reusable', 'ikorodu-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikorodu-clinic:heat', 'ikorodu-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ikorodu-clinic:wipes', 'ikorodu-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikorodu-clinic:washbag', 'ikorodu-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ikorodu-clinic:guide', 'ikorodu-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ikorodu-youth:pads', 'ikorodu-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ikorodu-youth:liners', 'ikorodu-youth', 'liners', 'good', 'For light days at the club.'),
  ('ikorodu-youth:tampons', 'ikorodu-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikorodu-youth:cup', 'ikorodu-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikorodu-youth:pants', 'ikorodu-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikorodu-youth:reusable', 'ikorodu-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikorodu-youth:heat', 'ikorodu-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikorodu-youth:wipes', 'ikorodu-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikorodu-youth:washbag', 'ikorodu-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ikorodu-youth:guide', 'ikorodu-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ikorodu-hostel:pads', 'ikorodu-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('ikorodu-hostel:liners', 'ikorodu-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikorodu-hostel:tampons', 'ikorodu-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikorodu-hostel:cup', 'ikorodu-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikorodu-hostel:pants', 'ikorodu-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikorodu-hostel:reusable', 'ikorodu-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('ikorodu-hostel:heat', 'ikorodu-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikorodu-hostel:wipes', 'ikorodu-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikorodu-hostel:washbag', 'ikorodu-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('ikorodu-hostel:guide', 'ikorodu-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('ikorodu-home:pads', 'ikorodu-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('ikorodu-home:liners', 'ikorodu-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikorodu-home:tampons', 'ikorodu-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikorodu-home:cup', 'ikorodu-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikorodu-home:pants', 'ikorodu-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikorodu-home:reusable', 'ikorodu-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('ikorodu-home:heat', 'ikorodu-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikorodu-home:wipes', 'ikorodu-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikorodu-home:washbag', 'ikorodu-home', 'washbag', 'good', 'A private place for her things.'),
  ('ikorodu-home:guide', 'ikorodu-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('ikorodu-football:pads', 'ikorodu-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ikorodu-football:liners', 'ikorodu-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikorodu-football:tampons', 'ikorodu-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikorodu-football:cup', 'ikorodu-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikorodu-football:pants', 'ikorodu-football', 'pants', 'good', 'Stay put while running.'),
  ('ikorodu-football:reusable', 'ikorodu-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikorodu-football:heat', 'ikorodu-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ikorodu-football:wipes', 'ikorodu-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikorodu-football:washbag', 'ikorodu-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ikorodu-football:guide', 'ikorodu-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikorodu-training:pads', 'ikorodu-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ikorodu-training:liners', 'ikorodu-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikorodu-training:tampons', 'ikorodu-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikorodu-training:cup', 'ikorodu-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikorodu-training:pants', 'ikorodu-training', 'pants', 'good', 'Last through long sessions.'),
  ('ikorodu-training:reusable', 'ikorodu-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikorodu-training:heat', 'ikorodu-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ikorodu-training:wipes', 'ikorodu-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikorodu-training:washbag', 'ikorodu-training', 'washbag', 'good', 'Private storage at work.'),
  ('ikorodu-training:guide', 'ikorodu-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('igbogbo-school:pads', 'igbogbo-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('igbogbo-school:liners', 'igbogbo-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('igbogbo-school:tampons', 'igbogbo-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('igbogbo-school:cup', 'igbogbo-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('igbogbo-school:pants', 'igbogbo-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('igbogbo-school:reusable', 'igbogbo-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('igbogbo-school:heat', 'igbogbo-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('igbogbo-school:wipes', 'igbogbo-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('igbogbo-school:washbag', 'igbogbo-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('igbogbo-school:guide', 'igbogbo-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('igbogbo-market:pads', 'igbogbo-market', 'pads', 'good', 'Quick to change between customers.'),
  ('igbogbo-market:liners', 'igbogbo-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('igbogbo-market:tampons', 'igbogbo-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('igbogbo-market:cup', 'igbogbo-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('igbogbo-market:pants', 'igbogbo-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('igbogbo-market:reusable', 'igbogbo-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('igbogbo-market:heat', 'igbogbo-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('igbogbo-market:wipes', 'igbogbo-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('igbogbo-market:washbag', 'igbogbo-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('igbogbo-market:guide', 'igbogbo-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('igbogbo-busstop:pads', 'igbogbo-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('igbogbo-busstop:liners', 'igbogbo-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('igbogbo-busstop:tampons', 'igbogbo-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('igbogbo-busstop:cup', 'igbogbo-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('igbogbo-busstop:pants', 'igbogbo-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('igbogbo-busstop:reusable', 'igbogbo-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('igbogbo-busstop:heat', 'igbogbo-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('igbogbo-busstop:wipes', 'igbogbo-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('igbogbo-busstop:washbag', 'igbogbo-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('igbogbo-busstop:guide', 'igbogbo-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('igbogbo-clinic:pads', 'igbogbo-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('igbogbo-clinic:liners', 'igbogbo-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('igbogbo-clinic:tampons', 'igbogbo-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('igbogbo-clinic:cup', 'igbogbo-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('igbogbo-clinic:pants', 'igbogbo-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('igbogbo-clinic:reusable', 'igbogbo-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('igbogbo-clinic:heat', 'igbogbo-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('igbogbo-clinic:wipes', 'igbogbo-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('igbogbo-clinic:washbag', 'igbogbo-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('igbogbo-clinic:guide', 'igbogbo-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('igbogbo-youth:pads', 'igbogbo-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('igbogbo-youth:liners', 'igbogbo-youth', 'liners', 'good', 'For light days at the club.'),
  ('igbogbo-youth:tampons', 'igbogbo-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('igbogbo-youth:cup', 'igbogbo-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('igbogbo-youth:pants', 'igbogbo-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('igbogbo-youth:reusable', 'igbogbo-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('igbogbo-youth:heat', 'igbogbo-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('igbogbo-youth:wipes', 'igbogbo-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('igbogbo-youth:washbag', 'igbogbo-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('igbogbo-youth:guide', 'igbogbo-youth', 'guide', 'good', 'Answers questions they have.'),
  ('igbogbo-waterside:pads', 'igbogbo-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('igbogbo-waterside:liners', 'igbogbo-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('igbogbo-waterside:tampons', 'igbogbo-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('igbogbo-waterside:cup', 'igbogbo-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('igbogbo-waterside:pants', 'igbogbo-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('igbogbo-waterside:reusable', 'igbogbo-waterside', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('igbogbo-waterside:heat', 'igbogbo-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('igbogbo-waterside:wipes', 'igbogbo-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('igbogbo-waterside:washbag', 'igbogbo-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('igbogbo-waterside:guide', 'igbogbo-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('igbogbo-hostel:pads', 'igbogbo-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('igbogbo-hostel:liners', 'igbogbo-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('igbogbo-hostel:tampons', 'igbogbo-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('igbogbo-hostel:cup', 'igbogbo-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('igbogbo-hostel:pants', 'igbogbo-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('igbogbo-hostel:reusable', 'igbogbo-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('igbogbo-hostel:heat', 'igbogbo-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('igbogbo-hostel:wipes', 'igbogbo-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('igbogbo-hostel:washbag', 'igbogbo-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('igbogbo-hostel:guide', 'igbogbo-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('igbogbo-football:pads', 'igbogbo-football', 'pads', 'good', 'A simple backup for match day.'),
  ('igbogbo-football:liners', 'igbogbo-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('igbogbo-football:tampons', 'igbogbo-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('igbogbo-football:cup', 'igbogbo-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('igbogbo-football:pants', 'igbogbo-football', 'pants', 'good', 'Stay put while running.'),
  ('igbogbo-football:reusable', 'igbogbo-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('igbogbo-football:heat', 'igbogbo-football', 'heat', 'good', 'Helps cramps after training.'),
  ('igbogbo-football:wipes', 'igbogbo-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('igbogbo-football:washbag', 'igbogbo-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('igbogbo-football:guide', 'igbogbo-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('igbogbo-training:pads', 'igbogbo-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('igbogbo-training:liners', 'igbogbo-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('igbogbo-training:tampons', 'igbogbo-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('igbogbo-training:cup', 'igbogbo-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('igbogbo-training:pants', 'igbogbo-training', 'pants', 'good', 'Last through long sessions.'),
  ('igbogbo-training:reusable', 'igbogbo-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('igbogbo-training:heat', 'igbogbo-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('igbogbo-training:wipes', 'igbogbo-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('igbogbo-training:washbag', 'igbogbo-training', 'washbag', 'good', 'Private storage at work.'),
  ('igbogbo-training:guide', 'igbogbo-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('igbogbo-shelter:pads', 'igbogbo-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('igbogbo-shelter:liners', 'igbogbo-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('igbogbo-shelter:tampons', 'igbogbo-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('igbogbo-shelter:cup', 'igbogbo-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('igbogbo-shelter:pants', 'igbogbo-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('igbogbo-shelter:reusable', 'igbogbo-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('igbogbo-shelter:heat', 'igbogbo-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('igbogbo-shelter:wipes', 'igbogbo-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('igbogbo-shelter:washbag', 'igbogbo-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('igbogbo-shelter:guide', 'igbogbo-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ijede-school:pads', 'ijede-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ijede-school:liners', 'ijede-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ijede-school:tampons', 'ijede-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ijede-school:cup', 'ijede-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ijede-school:pants', 'ijede-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ijede-school:reusable', 'ijede-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ijede-school:heat', 'ijede-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ijede-school:wipes', 'ijede-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('ijede-school:washbag', 'ijede-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ijede-school:guide', 'ijede-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ijede-market:pads', 'ijede-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ijede-market:liners', 'ijede-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ijede-market:tampons', 'ijede-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ijede-market:cup', 'ijede-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ijede-market:pants', 'ijede-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ijede-market:reusable', 'ijede-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ijede-market:heat', 'ijede-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ijede-market:wipes', 'ijede-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ijede-market:washbag', 'ijede-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ijede-market:guide', 'ijede-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ijede-busstop:pads', 'ijede-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ijede-busstop:liners', 'ijede-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ijede-busstop:tampons', 'ijede-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ijede-busstop:cup', 'ijede-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ijede-busstop:pants', 'ijede-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ijede-busstop:reusable', 'ijede-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ijede-busstop:heat', 'ijede-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ijede-busstop:wipes', 'ijede-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ijede-busstop:washbag', 'ijede-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ijede-busstop:guide', 'ijede-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ijede-clinic:pads', 'ijede-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ijede-clinic:liners', 'ijede-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ijede-clinic:tampons', 'ijede-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ijede-clinic:cup', 'ijede-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ijede-clinic:pants', 'ijede-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ijede-clinic:reusable', 'ijede-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ijede-clinic:heat', 'ijede-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ijede-clinic:wipes', 'ijede-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ijede-clinic:washbag', 'ijede-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ijede-clinic:guide', 'ijede-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ijede-youth:pads', 'ijede-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ijede-youth:liners', 'ijede-youth', 'liners', 'good', 'For light days at the club.'),
  ('ijede-youth:tampons', 'ijede-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ijede-youth:cup', 'ijede-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ijede-youth:pants', 'ijede-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ijede-youth:reusable', 'ijede-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ijede-youth:heat', 'ijede-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ijede-youth:wipes', 'ijede-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ijede-youth:washbag', 'ijede-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ijede-youth:guide', 'ijede-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ijede-waterside:pads', 'ijede-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('ijede-waterside:liners', 'ijede-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ijede-waterside:tampons', 'ijede-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('ijede-waterside:cup', 'ijede-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ijede-waterside:pants', 'ijede-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('ijede-waterside:reusable', 'ijede-waterside', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ijede-waterside:heat', 'ijede-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ijede-waterside:wipes', 'ijede-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ijede-waterside:washbag', 'ijede-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('ijede-waterside:guide', 'ijede-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('ijede-hostel:pads', 'ijede-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('ijede-hostel:liners', 'ijede-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ijede-hostel:tampons', 'ijede-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ijede-hostel:cup', 'ijede-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ijede-hostel:pants', 'ijede-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ijede-hostel:reusable', 'ijede-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('ijede-hostel:heat', 'ijede-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ijede-hostel:wipes', 'ijede-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ijede-hostel:washbag', 'ijede-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('ijede-hostel:guide', 'ijede-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('ijede-football:pads', 'ijede-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ijede-football:liners', 'ijede-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ijede-football:tampons', 'ijede-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ijede-football:cup', 'ijede-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ijede-football:pants', 'ijede-football', 'pants', 'good', 'Stay put while running.'),
  ('ijede-football:reusable', 'ijede-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ijede-football:heat', 'ijede-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ijede-football:wipes', 'ijede-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ijede-football:washbag', 'ijede-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ijede-football:guide', 'ijede-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ijede-training:pads', 'ijede-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ijede-training:liners', 'ijede-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ijede-training:tampons', 'ijede-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ijede-training:cup', 'ijede-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ijede-training:pants', 'ijede-training', 'pants', 'good', 'Last through long sessions.'),
  ('ijede-training:reusable', 'ijede-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ijede-training:heat', 'ijede-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ijede-training:wipes', 'ijede-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ijede-training:washbag', 'ijede-training', 'washbag', 'good', 'Private storage at work.'),
  ('ijede-training:guide', 'ijede-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ijede-shelter:pads', 'ijede-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('ijede-shelter:liners', 'ijede-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ijede-shelter:tampons', 'ijede-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ijede-shelter:cup', 'ijede-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ijede-shelter:pants', 'ijede-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ijede-shelter:reusable', 'ijede-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ijede-shelter:heat', 'ijede-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('ijede-shelter:wipes', 'ijede-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('ijede-shelter:washbag', 'ijede-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('ijede-shelter:guide', 'ijede-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('imota-school:pads', 'imota-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('imota-school:liners', 'imota-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('imota-school:tampons', 'imota-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('imota-school:cup', 'imota-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('imota-school:pants', 'imota-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('imota-school:reusable', 'imota-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('imota-school:heat', 'imota-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('imota-school:wipes', 'imota-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('imota-school:washbag', 'imota-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('imota-school:guide', 'imota-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('imota-market:pads', 'imota-market', 'pads', 'good', 'Quick to change between customers.'),
  ('imota-market:liners', 'imota-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('imota-market:tampons', 'imota-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('imota-market:cup', 'imota-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('imota-market:pants', 'imota-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('imota-market:reusable', 'imota-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('imota-market:heat', 'imota-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('imota-market:wipes', 'imota-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('imota-market:washbag', 'imota-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('imota-market:guide', 'imota-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('imota-busstop:pads', 'imota-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('imota-busstop:liners', 'imota-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('imota-busstop:tampons', 'imota-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('imota-busstop:cup', 'imota-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('imota-busstop:pants', 'imota-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('imota-busstop:reusable', 'imota-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('imota-busstop:heat', 'imota-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('imota-busstop:wipes', 'imota-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('imota-busstop:washbag', 'imota-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('imota-busstop:guide', 'imota-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('imota-clinic:pads', 'imota-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('imota-clinic:liners', 'imota-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('imota-clinic:tampons', 'imota-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('imota-clinic:cup', 'imota-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('imota-clinic:pants', 'imota-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('imota-clinic:reusable', 'imota-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('imota-clinic:heat', 'imota-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('imota-clinic:wipes', 'imota-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('imota-clinic:washbag', 'imota-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('imota-clinic:guide', 'imota-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('imota-youth:pads', 'imota-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('imota-youth:liners', 'imota-youth', 'liners', 'good', 'For light days at the club.'),
  ('imota-youth:tampons', 'imota-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('imota-youth:cup', 'imota-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('imota-youth:pants', 'imota-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('imota-youth:reusable', 'imota-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('imota-youth:heat', 'imota-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('imota-youth:wipes', 'imota-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('imota-youth:washbag', 'imota-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('imota-youth:guide', 'imota-youth', 'guide', 'good', 'Answers questions they have.'),
  ('imota-hostel:pads', 'imota-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('imota-hostel:liners', 'imota-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('imota-hostel:tampons', 'imota-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('imota-hostel:cup', 'imota-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('imota-hostel:pants', 'imota-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('imota-hostel:reusable', 'imota-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('imota-hostel:heat', 'imota-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('imota-hostel:wipes', 'imota-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('imota-hostel:washbag', 'imota-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('imota-hostel:guide', 'imota-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('imota-home:pads', 'imota-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('imota-home:liners', 'imota-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('imota-home:tampons', 'imota-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('imota-home:cup', 'imota-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('imota-home:pants', 'imota-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('imota-home:reusable', 'imota-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('imota-home:heat', 'imota-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('imota-home:wipes', 'imota-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('imota-home:washbag', 'imota-home', 'washbag', 'good', 'A private place for her things.'),
  ('imota-home:guide', 'imota-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('imota-football:pads', 'imota-football', 'pads', 'good', 'A simple backup for match day.'),
  ('imota-football:liners', 'imota-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('imota-football:tampons', 'imota-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('imota-football:cup', 'imota-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('imota-football:pants', 'imota-football', 'pants', 'good', 'Stay put while running.'),
  ('imota-football:reusable', 'imota-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('imota-football:heat', 'imota-football', 'heat', 'good', 'Helps cramps after training.'),
  ('imota-football:wipes', 'imota-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('imota-football:washbag', 'imota-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('imota-football:guide', 'imota-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('imota-training:pads', 'imota-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('imota-training:liners', 'imota-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('imota-training:tampons', 'imota-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('imota-training:cup', 'imota-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('imota-training:pants', 'imota-training', 'pants', 'good', 'Last through long sessions.'),
  ('imota-training:reusable', 'imota-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('imota-training:heat', 'imota-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('imota-training:wipes', 'imota-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('imota-training:washbag', 'imota-training', 'washbag', 'good', 'Private storage at work.'),
  ('imota-training:guide', 'imota-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('imota-shelter:pads', 'imota-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('imota-shelter:liners', 'imota-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('imota-shelter:tampons', 'imota-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('imota-shelter:cup', 'imota-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('imota-shelter:pants', 'imota-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('imota-shelter:reusable', 'imota-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('imota-shelter:heat', 'imota-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('imota-shelter:wipes', 'imota-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('imota-shelter:washbag', 'imota-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('imota-shelter:guide', 'imota-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('lagos-island-school:pads', 'lagos-island-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('lagos-island-school:liners', 'lagos-island-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('lagos-island-school:tampons', 'lagos-island-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lagos-island-school:cup', 'lagos-island-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('lagos-island-school:pants', 'lagos-island-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('lagos-island-school:reusable', 'lagos-island-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('lagos-island-school:heat', 'lagos-island-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('lagos-island-school:wipes', 'lagos-island-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('lagos-island-school:washbag', 'lagos-island-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('lagos-island-school:guide', 'lagos-island-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('lagos-island-market:pads', 'lagos-island-market', 'pads', 'good', 'Quick to change between customers.'),
  ('lagos-island-market:liners', 'lagos-island-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lagos-island-market:tampons', 'lagos-island-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lagos-island-market:cup', 'lagos-island-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('lagos-island-market:pants', 'lagos-island-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('lagos-island-market:reusable', 'lagos-island-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('lagos-island-market:heat', 'lagos-island-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('lagos-island-market:wipes', 'lagos-island-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('lagos-island-market:washbag', 'lagos-island-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('lagos-island-market:guide', 'lagos-island-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('lagos-island-busstop:pads', 'lagos-island-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('lagos-island-busstop:liners', 'lagos-island-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lagos-island-busstop:tampons', 'lagos-island-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lagos-island-busstop:cup', 'lagos-island-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('lagos-island-busstop:pants', 'lagos-island-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('lagos-island-busstop:reusable', 'lagos-island-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('lagos-island-busstop:heat', 'lagos-island-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('lagos-island-busstop:wipes', 'lagos-island-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('lagos-island-busstop:washbag', 'lagos-island-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('lagos-island-busstop:guide', 'lagos-island-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('lagos-island-clinic:pads', 'lagos-island-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('lagos-island-clinic:liners', 'lagos-island-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lagos-island-clinic:tampons', 'lagos-island-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lagos-island-clinic:cup', 'lagos-island-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('lagos-island-clinic:pants', 'lagos-island-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('lagos-island-clinic:reusable', 'lagos-island-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('lagos-island-clinic:heat', 'lagos-island-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('lagos-island-clinic:wipes', 'lagos-island-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('lagos-island-clinic:washbag', 'lagos-island-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('lagos-island-clinic:guide', 'lagos-island-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('lagos-island-youth:pads', 'lagos-island-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('lagos-island-youth:liners', 'lagos-island-youth', 'liners', 'good', 'For light days at the club.'),
  ('lagos-island-youth:tampons', 'lagos-island-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lagos-island-youth:cup', 'lagos-island-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('lagos-island-youth:pants', 'lagos-island-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('lagos-island-youth:reusable', 'lagos-island-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('lagos-island-youth:heat', 'lagos-island-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('lagos-island-youth:wipes', 'lagos-island-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('lagos-island-youth:washbag', 'lagos-island-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('lagos-island-youth:guide', 'lagos-island-youth', 'guide', 'good', 'Answers questions they have.'),
  ('lagos-island-waterside:pads', 'lagos-island-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('lagos-island-waterside:liners', 'lagos-island-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lagos-island-waterside:tampons', 'lagos-island-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('lagos-island-waterside:cup', 'lagos-island-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('lagos-island-waterside:pants', 'lagos-island-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('lagos-island-waterside:reusable', 'lagos-island-waterside', 'reusable', 'good', 'They last a year when the shop is far.'),
  ('lagos-island-waterside:heat', 'lagos-island-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('lagos-island-waterside:wipes', 'lagos-island-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('lagos-island-waterside:washbag', 'lagos-island-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('lagos-island-waterside:guide', 'lagos-island-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('lagos-island-hostel:pads', 'lagos-island-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('lagos-island-hostel:liners', 'lagos-island-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lagos-island-hostel:tampons', 'lagos-island-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lagos-island-hostel:cup', 'lagos-island-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('lagos-island-hostel:pants', 'lagos-island-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('lagos-island-hostel:reusable', 'lagos-island-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('lagos-island-hostel:heat', 'lagos-island-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('lagos-island-hostel:wipes', 'lagos-island-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('lagos-island-hostel:washbag', 'lagos-island-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('lagos-island-hostel:guide', 'lagos-island-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('lagos-island-football:pads', 'lagos-island-football', 'pads', 'good', 'A simple backup for match day.'),
  ('lagos-island-football:liners', 'lagos-island-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lagos-island-football:tampons', 'lagos-island-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lagos-island-football:cup', 'lagos-island-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('lagos-island-football:pants', 'lagos-island-football', 'pants', 'good', 'Stay put while running.'),
  ('lagos-island-football:reusable', 'lagos-island-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('lagos-island-football:heat', 'lagos-island-football', 'heat', 'good', 'Helps cramps after training.'),
  ('lagos-island-football:wipes', 'lagos-island-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('lagos-island-football:washbag', 'lagos-island-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('lagos-island-football:guide', 'lagos-island-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('lagos-island-training:pads', 'lagos-island-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('lagos-island-training:liners', 'lagos-island-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lagos-island-training:tampons', 'lagos-island-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lagos-island-training:cup', 'lagos-island-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('lagos-island-training:pants', 'lagos-island-training', 'pants', 'good', 'Last through long sessions.'),
  ('lagos-island-training:reusable', 'lagos-island-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('lagos-island-training:heat', 'lagos-island-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('lagos-island-training:wipes', 'lagos-island-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('lagos-island-training:washbag', 'lagos-island-training', 'washbag', 'good', 'Private storage at work.'),
  ('lagos-island-training:guide', 'lagos-island-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('obalende-school:pads', 'obalende-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('obalende-school:liners', 'obalende-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('obalende-school:tampons', 'obalende-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('obalende-school:cup', 'obalende-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('obalende-school:pants', 'obalende-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('obalende-school:reusable', 'obalende-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('obalende-school:heat', 'obalende-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('obalende-school:wipes', 'obalende-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('obalende-school:washbag', 'obalende-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('obalende-school:guide', 'obalende-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('obalende-market:pads', 'obalende-market', 'pads', 'good', 'Quick to change between customers.'),
  ('obalende-market:liners', 'obalende-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('obalende-market:tampons', 'obalende-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('obalende-market:cup', 'obalende-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('obalende-market:pants', 'obalende-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('obalende-market:reusable', 'obalende-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('obalende-market:heat', 'obalende-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('obalende-market:wipes', 'obalende-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('obalende-market:washbag', 'obalende-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('obalende-market:guide', 'obalende-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('obalende-busstop:pads', 'obalende-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('obalende-busstop:liners', 'obalende-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('obalende-busstop:tampons', 'obalende-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('obalende-busstop:cup', 'obalende-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('obalende-busstop:pants', 'obalende-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('obalende-busstop:reusable', 'obalende-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('obalende-busstop:heat', 'obalende-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('obalende-busstop:wipes', 'obalende-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('obalende-busstop:washbag', 'obalende-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('obalende-busstop:guide', 'obalende-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('obalende-clinic:pads', 'obalende-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('obalende-clinic:liners', 'obalende-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('obalende-clinic:tampons', 'obalende-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('obalende-clinic:cup', 'obalende-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('obalende-clinic:pants', 'obalende-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('obalende-clinic:reusable', 'obalende-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('obalende-clinic:heat', 'obalende-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('obalende-clinic:wipes', 'obalende-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('obalende-clinic:washbag', 'obalende-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('obalende-clinic:guide', 'obalende-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('obalende-youth:pads', 'obalende-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('obalende-youth:liners', 'obalende-youth', 'liners', 'good', 'For light days at the club.'),
  ('obalende-youth:tampons', 'obalende-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('obalende-youth:cup', 'obalende-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('obalende-youth:pants', 'obalende-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('obalende-youth:reusable', 'obalende-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('obalende-youth:heat', 'obalende-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('obalende-youth:wipes', 'obalende-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('obalende-youth:washbag', 'obalende-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('obalende-youth:guide', 'obalende-youth', 'guide', 'good', 'Answers questions they have.'),
  ('obalende-hostel:pads', 'obalende-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('obalende-hostel:liners', 'obalende-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('obalende-hostel:tampons', 'obalende-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('obalende-hostel:cup', 'obalende-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('obalende-hostel:pants', 'obalende-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('obalende-hostel:reusable', 'obalende-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('obalende-hostel:heat', 'obalende-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('obalende-hostel:wipes', 'obalende-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('obalende-hostel:washbag', 'obalende-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('obalende-hostel:guide', 'obalende-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('obalende-home:pads', 'obalende-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('obalende-home:liners', 'obalende-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('obalende-home:tampons', 'obalende-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('obalende-home:cup', 'obalende-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('obalende-home:pants', 'obalende-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('obalende-home:reusable', 'obalende-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('obalende-home:heat', 'obalende-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('obalende-home:wipes', 'obalende-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('obalende-home:washbag', 'obalende-home', 'washbag', 'good', 'A private place for her things.'),
  ('obalende-home:guide', 'obalende-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('obalende-football:pads', 'obalende-football', 'pads', 'good', 'A simple backup for match day.'),
  ('obalende-football:liners', 'obalende-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('obalende-football:tampons', 'obalende-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('obalende-football:cup', 'obalende-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('obalende-football:pants', 'obalende-football', 'pants', 'good', 'Stay put while running.'),
  ('obalende-football:reusable', 'obalende-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('obalende-football:heat', 'obalende-football', 'heat', 'good', 'Helps cramps after training.'),
  ('obalende-football:wipes', 'obalende-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('obalende-football:washbag', 'obalende-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('obalende-football:guide', 'obalende-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('obalende-training:pads', 'obalende-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('obalende-training:liners', 'obalende-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('obalende-training:tampons', 'obalende-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('obalende-training:cup', 'obalende-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('obalende-training:pants', 'obalende-training', 'pants', 'good', 'Last through long sessions.'),
  ('obalende-training:reusable', 'obalende-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('obalende-training:heat', 'obalende-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('obalende-training:wipes', 'obalende-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('obalende-training:washbag', 'obalende-training', 'washbag', 'good', 'Private storage at work.'),
  ('obalende-training:guide', 'obalende-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('obalende-shelter:pads', 'obalende-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('obalende-shelter:liners', 'obalende-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('obalende-shelter:tampons', 'obalende-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('obalende-shelter:cup', 'obalende-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('obalende-shelter:pants', 'obalende-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('obalende-shelter:reusable', 'obalende-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('obalende-shelter:heat', 'obalende-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('obalende-shelter:wipes', 'obalende-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('obalende-shelter:washbag', 'obalende-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('obalende-shelter:guide', 'obalende-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikoyi-school:pads', 'ikoyi-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ikoyi-school:liners', 'ikoyi-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ikoyi-school:tampons', 'ikoyi-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikoyi-school:cup', 'ikoyi-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikoyi-school:pants', 'ikoyi-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikoyi-school:reusable', 'ikoyi-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikoyi-school:heat', 'ikoyi-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikoyi-school:wipes', 'ikoyi-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('ikoyi-school:washbag', 'ikoyi-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ikoyi-school:guide', 'ikoyi-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ikoyi-market:pads', 'ikoyi-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ikoyi-market:liners', 'ikoyi-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikoyi-market:tampons', 'ikoyi-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikoyi-market:cup', 'ikoyi-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikoyi-market:pants', 'ikoyi-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikoyi-market:reusable', 'ikoyi-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikoyi-market:heat', 'ikoyi-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ikoyi-market:wipes', 'ikoyi-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ikoyi-market:washbag', 'ikoyi-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ikoyi-market:guide', 'ikoyi-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikoyi-busstop:pads', 'ikoyi-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ikoyi-busstop:liners', 'ikoyi-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikoyi-busstop:tampons', 'ikoyi-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikoyi-busstop:cup', 'ikoyi-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikoyi-busstop:pants', 'ikoyi-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ikoyi-busstop:reusable', 'ikoyi-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikoyi-busstop:heat', 'ikoyi-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ikoyi-busstop:wipes', 'ikoyi-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ikoyi-busstop:washbag', 'ikoyi-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ikoyi-busstop:guide', 'ikoyi-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikoyi-clinic:pads', 'ikoyi-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ikoyi-clinic:liners', 'ikoyi-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikoyi-clinic:tampons', 'ikoyi-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikoyi-clinic:cup', 'ikoyi-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikoyi-clinic:pants', 'ikoyi-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ikoyi-clinic:reusable', 'ikoyi-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikoyi-clinic:heat', 'ikoyi-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ikoyi-clinic:wipes', 'ikoyi-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikoyi-clinic:washbag', 'ikoyi-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ikoyi-clinic:guide', 'ikoyi-clinic', 'guide', 'good', 'Explains when pain needs a doctor.');

INSERT INTO giveday_needs (id, place_id, product_id, rating, why) VALUES
  ('ikoyi-youth:pads', 'ikoyi-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ikoyi-youth:liners', 'ikoyi-youth', 'liners', 'good', 'For light days at the club.'),
  ('ikoyi-youth:tampons', 'ikoyi-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikoyi-youth:cup', 'ikoyi-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikoyi-youth:pants', 'ikoyi-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikoyi-youth:reusable', 'ikoyi-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikoyi-youth:heat', 'ikoyi-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikoyi-youth:wipes', 'ikoyi-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikoyi-youth:washbag', 'ikoyi-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ikoyi-youth:guide', 'ikoyi-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ikoyi-waterside:pads', 'ikoyi-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('ikoyi-waterside:liners', 'ikoyi-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikoyi-waterside:tampons', 'ikoyi-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('ikoyi-waterside:cup', 'ikoyi-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikoyi-waterside:pants', 'ikoyi-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('ikoyi-waterside:reusable', 'ikoyi-waterside', 'reusable', 'good', 'They last a year when the shop is far.'),
  ('ikoyi-waterside:heat', 'ikoyi-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikoyi-waterside:wipes', 'ikoyi-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikoyi-waterside:washbag', 'ikoyi-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('ikoyi-waterside:guide', 'ikoyi-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('ikoyi-hostel:pads', 'ikoyi-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('ikoyi-hostel:liners', 'ikoyi-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikoyi-hostel:tampons', 'ikoyi-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikoyi-hostel:cup', 'ikoyi-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikoyi-hostel:pants', 'ikoyi-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikoyi-hostel:reusable', 'ikoyi-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('ikoyi-hostel:heat', 'ikoyi-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikoyi-hostel:wipes', 'ikoyi-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikoyi-hostel:washbag', 'ikoyi-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('ikoyi-hostel:guide', 'ikoyi-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('ikoyi-football:pads', 'ikoyi-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ikoyi-football:liners', 'ikoyi-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikoyi-football:tampons', 'ikoyi-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikoyi-football:cup', 'ikoyi-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikoyi-football:pants', 'ikoyi-football', 'pants', 'good', 'Stay put while running.'),
  ('ikoyi-football:reusable', 'ikoyi-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikoyi-football:heat', 'ikoyi-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ikoyi-football:wipes', 'ikoyi-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikoyi-football:washbag', 'ikoyi-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ikoyi-football:guide', 'ikoyi-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikoyi-training:pads', 'ikoyi-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ikoyi-training:liners', 'ikoyi-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikoyi-training:tampons', 'ikoyi-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikoyi-training:cup', 'ikoyi-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikoyi-training:pants', 'ikoyi-training', 'pants', 'good', 'Last through long sessions.'),
  ('ikoyi-training:reusable', 'ikoyi-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikoyi-training:heat', 'ikoyi-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ikoyi-training:wipes', 'ikoyi-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikoyi-training:washbag', 'ikoyi-training', 'washbag', 'good', 'Private storage at work.'),
  ('ikoyi-training:guide', 'ikoyi-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikoyi-shelter:pads', 'ikoyi-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('ikoyi-shelter:liners', 'ikoyi-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikoyi-shelter:tampons', 'ikoyi-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikoyi-shelter:cup', 'ikoyi-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikoyi-shelter:pants', 'ikoyi-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikoyi-shelter:reusable', 'ikoyi-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikoyi-shelter:heat', 'ikoyi-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('ikoyi-shelter:wipes', 'ikoyi-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('ikoyi-shelter:washbag', 'ikoyi-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('ikoyi-shelter:guide', 'ikoyi-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('victoria-island-school:pads', 'victoria-island-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('victoria-island-school:liners', 'victoria-island-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('victoria-island-school:tampons', 'victoria-island-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('victoria-island-school:cup', 'victoria-island-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('victoria-island-school:pants', 'victoria-island-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('victoria-island-school:reusable', 'victoria-island-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('victoria-island-school:heat', 'victoria-island-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('victoria-island-school:wipes', 'victoria-island-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('victoria-island-school:washbag', 'victoria-island-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('victoria-island-school:guide', 'victoria-island-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('victoria-island-market:pads', 'victoria-island-market', 'pads', 'good', 'Quick to change between customers.'),
  ('victoria-island-market:liners', 'victoria-island-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('victoria-island-market:tampons', 'victoria-island-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('victoria-island-market:cup', 'victoria-island-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('victoria-island-market:pants', 'victoria-island-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('victoria-island-market:reusable', 'victoria-island-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('victoria-island-market:heat', 'victoria-island-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('victoria-island-market:wipes', 'victoria-island-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('victoria-island-market:washbag', 'victoria-island-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('victoria-island-market:guide', 'victoria-island-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('victoria-island-busstop:pads', 'victoria-island-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('victoria-island-busstop:liners', 'victoria-island-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('victoria-island-busstop:tampons', 'victoria-island-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('victoria-island-busstop:cup', 'victoria-island-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('victoria-island-busstop:pants', 'victoria-island-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('victoria-island-busstop:reusable', 'victoria-island-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('victoria-island-busstop:heat', 'victoria-island-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('victoria-island-busstop:wipes', 'victoria-island-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('victoria-island-busstop:washbag', 'victoria-island-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('victoria-island-busstop:guide', 'victoria-island-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('victoria-island-clinic:pads', 'victoria-island-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('victoria-island-clinic:liners', 'victoria-island-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('victoria-island-clinic:tampons', 'victoria-island-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('victoria-island-clinic:cup', 'victoria-island-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('victoria-island-clinic:pants', 'victoria-island-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('victoria-island-clinic:reusable', 'victoria-island-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('victoria-island-clinic:heat', 'victoria-island-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('victoria-island-clinic:wipes', 'victoria-island-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('victoria-island-clinic:washbag', 'victoria-island-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('victoria-island-clinic:guide', 'victoria-island-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('victoria-island-youth:pads', 'victoria-island-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('victoria-island-youth:liners', 'victoria-island-youth', 'liners', 'good', 'For light days at the club.'),
  ('victoria-island-youth:tampons', 'victoria-island-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('victoria-island-youth:cup', 'victoria-island-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('victoria-island-youth:pants', 'victoria-island-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('victoria-island-youth:reusable', 'victoria-island-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('victoria-island-youth:heat', 'victoria-island-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('victoria-island-youth:wipes', 'victoria-island-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('victoria-island-youth:washbag', 'victoria-island-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('victoria-island-youth:guide', 'victoria-island-youth', 'guide', 'good', 'Answers questions they have.'),
  ('victoria-island-waterside:pads', 'victoria-island-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('victoria-island-waterside:liners', 'victoria-island-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('victoria-island-waterside:tampons', 'victoria-island-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('victoria-island-waterside:cup', 'victoria-island-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('victoria-island-waterside:pants', 'victoria-island-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('victoria-island-waterside:reusable', 'victoria-island-waterside', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('victoria-island-waterside:heat', 'victoria-island-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('victoria-island-waterside:wipes', 'victoria-island-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('victoria-island-waterside:washbag', 'victoria-island-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('victoria-island-waterside:guide', 'victoria-island-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('victoria-island-community:pads', 'victoria-island-community', 'pads', 'good', 'Something for everyone today.'),
  ('victoria-island-community:liners', 'victoria-island-community', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('victoria-island-community:tampons', 'victoria-island-community', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('victoria-island-community:cup', 'victoria-island-community', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('victoria-island-community:pants', 'victoria-island-community', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('victoria-island-community:reusable', 'victoria-island-community', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('victoria-island-community:heat', 'victoria-island-community', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('victoria-island-community:wipes', 'victoria-island-community', 'wipes', 'good', 'There is no running water to wash.'),
  ('victoria-island-community:washbag', 'victoria-island-community', 'washbag', 'good', 'Keeps reusables clean.'),
  ('victoria-island-community:guide', 'victoria-island-community', 'guide', 'good', 'Helps mothers talk to daughters.'),
  ('victoria-island-football:pads', 'victoria-island-football', 'pads', 'good', 'A simple backup for match day.'),
  ('victoria-island-football:liners', 'victoria-island-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('victoria-island-football:tampons', 'victoria-island-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('victoria-island-football:cup', 'victoria-island-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('victoria-island-football:pants', 'victoria-island-football', 'pants', 'good', 'Stay put while running.'),
  ('victoria-island-football:reusable', 'victoria-island-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('victoria-island-football:heat', 'victoria-island-football', 'heat', 'good', 'Helps cramps after training.'),
  ('victoria-island-football:wipes', 'victoria-island-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('victoria-island-football:washbag', 'victoria-island-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('victoria-island-football:guide', 'victoria-island-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('victoria-island-shelter:pads', 'victoria-island-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('victoria-island-shelter:liners', 'victoria-island-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('victoria-island-shelter:tampons', 'victoria-island-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('victoria-island-shelter:cup', 'victoria-island-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('victoria-island-shelter:pants', 'victoria-island-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('victoria-island-shelter:reusable', 'victoria-island-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('victoria-island-shelter:heat', 'victoria-island-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('victoria-island-shelter:wipes', 'victoria-island-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('victoria-island-shelter:washbag', 'victoria-island-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('victoria-island-shelter:guide', 'victoria-island-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('oniru-school:pads', 'oniru-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('oniru-school:liners', 'oniru-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('oniru-school:tampons', 'oniru-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oniru-school:cup', 'oniru-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('oniru-school:pants', 'oniru-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('oniru-school:reusable', 'oniru-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('oniru-school:heat', 'oniru-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('oniru-school:wipes', 'oniru-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('oniru-school:washbag', 'oniru-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('oniru-school:guide', 'oniru-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('oniru-market:pads', 'oniru-market', 'pads', 'good', 'Quick to change between customers.'),
  ('oniru-market:liners', 'oniru-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oniru-market:tampons', 'oniru-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oniru-market:cup', 'oniru-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('oniru-market:pants', 'oniru-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('oniru-market:reusable', 'oniru-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('oniru-market:heat', 'oniru-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('oniru-market:wipes', 'oniru-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('oniru-market:washbag', 'oniru-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('oniru-market:guide', 'oniru-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('oniru-busstop:pads', 'oniru-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('oniru-busstop:liners', 'oniru-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oniru-busstop:tampons', 'oniru-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oniru-busstop:cup', 'oniru-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('oniru-busstop:pants', 'oniru-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('oniru-busstop:reusable', 'oniru-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('oniru-busstop:heat', 'oniru-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('oniru-busstop:wipes', 'oniru-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('oniru-busstop:washbag', 'oniru-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('oniru-busstop:guide', 'oniru-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('oniru-clinic:pads', 'oniru-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('oniru-clinic:liners', 'oniru-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oniru-clinic:tampons', 'oniru-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oniru-clinic:cup', 'oniru-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('oniru-clinic:pants', 'oniru-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('oniru-clinic:reusable', 'oniru-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('oniru-clinic:heat', 'oniru-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('oniru-clinic:wipes', 'oniru-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('oniru-clinic:washbag', 'oniru-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('oniru-clinic:guide', 'oniru-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('oniru-youth:pads', 'oniru-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('oniru-youth:liners', 'oniru-youth', 'liners', 'good', 'For light days at the club.'),
  ('oniru-youth:tampons', 'oniru-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oniru-youth:cup', 'oniru-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('oniru-youth:pants', 'oniru-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('oniru-youth:reusable', 'oniru-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('oniru-youth:heat', 'oniru-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('oniru-youth:wipes', 'oniru-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('oniru-youth:washbag', 'oniru-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('oniru-youth:guide', 'oniru-youth', 'guide', 'good', 'Answers questions they have.'),
  ('oniru-waterside:pads', 'oniru-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('oniru-waterside:liners', 'oniru-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oniru-waterside:tampons', 'oniru-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('oniru-waterside:cup', 'oniru-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('oniru-waterside:pants', 'oniru-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('oniru-waterside:reusable', 'oniru-waterside', 'reusable', 'good', 'They last a year when the shop is far.'),
  ('oniru-waterside:heat', 'oniru-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('oniru-waterside:wipes', 'oniru-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('oniru-waterside:washbag', 'oniru-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('oniru-waterside:guide', 'oniru-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('oniru-community:pads', 'oniru-community', 'pads', 'good', 'Something for everyone today.'),
  ('oniru-community:liners', 'oniru-community', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oniru-community:tampons', 'oniru-community', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oniru-community:cup', 'oniru-community', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('oniru-community:pants', 'oniru-community', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('oniru-community:reusable', 'oniru-community', 'reusable', 'good', 'With water, these save money for a year.'),
  ('oniru-community:heat', 'oniru-community', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('oniru-community:wipes', 'oniru-community', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('oniru-community:washbag', 'oniru-community', 'washbag', 'good', 'Keeps reusables clean.'),
  ('oniru-community:guide', 'oniru-community', 'guide', 'good', 'Helps mothers talk to daughters.'),
  ('oniru-football:pads', 'oniru-football', 'pads', 'good', 'A simple backup for match day.'),
  ('oniru-football:liners', 'oniru-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oniru-football:tampons', 'oniru-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oniru-football:cup', 'oniru-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('oniru-football:pants', 'oniru-football', 'pants', 'good', 'Stay put while running.'),
  ('oniru-football:reusable', 'oniru-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('oniru-football:heat', 'oniru-football', 'heat', 'good', 'Helps cramps after training.'),
  ('oniru-football:wipes', 'oniru-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('oniru-football:washbag', 'oniru-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('oniru-football:guide', 'oniru-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('oniru-shelter:pads', 'oniru-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('oniru-shelter:liners', 'oniru-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('oniru-shelter:tampons', 'oniru-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('oniru-shelter:cup', 'oniru-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('oniru-shelter:pants', 'oniru-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('oniru-shelter:reusable', 'oniru-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('oniru-shelter:heat', 'oniru-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('oniru-shelter:wipes', 'oniru-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('oniru-shelter:washbag', 'oniru-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('oniru-shelter:guide', 'oniru-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('lekki-phase-1-school:pads', 'lekki-phase-1-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('lekki-phase-1-school:liners', 'lekki-phase-1-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('lekki-phase-1-school:tampons', 'lekki-phase-1-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lekki-phase-1-school:cup', 'lekki-phase-1-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('lekki-phase-1-school:pants', 'lekki-phase-1-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('lekki-phase-1-school:reusable', 'lekki-phase-1-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('lekki-phase-1-school:heat', 'lekki-phase-1-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('lekki-phase-1-school:wipes', 'lekki-phase-1-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('lekki-phase-1-school:washbag', 'lekki-phase-1-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('lekki-phase-1-school:guide', 'lekki-phase-1-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('lekki-phase-1-market:pads', 'lekki-phase-1-market', 'pads', 'good', 'Quick to change between customers.'),
  ('lekki-phase-1-market:liners', 'lekki-phase-1-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lekki-phase-1-market:tampons', 'lekki-phase-1-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lekki-phase-1-market:cup', 'lekki-phase-1-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('lekki-phase-1-market:pants', 'lekki-phase-1-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('lekki-phase-1-market:reusable', 'lekki-phase-1-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('lekki-phase-1-market:heat', 'lekki-phase-1-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('lekki-phase-1-market:wipes', 'lekki-phase-1-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('lekki-phase-1-market:washbag', 'lekki-phase-1-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('lekki-phase-1-market:guide', 'lekki-phase-1-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('lekki-phase-1-busstop:pads', 'lekki-phase-1-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('lekki-phase-1-busstop:liners', 'lekki-phase-1-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lekki-phase-1-busstop:tampons', 'lekki-phase-1-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lekki-phase-1-busstop:cup', 'lekki-phase-1-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('lekki-phase-1-busstop:pants', 'lekki-phase-1-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('lekki-phase-1-busstop:reusable', 'lekki-phase-1-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('lekki-phase-1-busstop:heat', 'lekki-phase-1-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('lekki-phase-1-busstop:wipes', 'lekki-phase-1-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('lekki-phase-1-busstop:washbag', 'lekki-phase-1-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('lekki-phase-1-busstop:guide', 'lekki-phase-1-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('lekki-phase-1-clinic:pads', 'lekki-phase-1-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('lekki-phase-1-clinic:liners', 'lekki-phase-1-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lekki-phase-1-clinic:tampons', 'lekki-phase-1-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lekki-phase-1-clinic:cup', 'lekki-phase-1-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('lekki-phase-1-clinic:pants', 'lekki-phase-1-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('lekki-phase-1-clinic:reusable', 'lekki-phase-1-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('lekki-phase-1-clinic:heat', 'lekki-phase-1-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('lekki-phase-1-clinic:wipes', 'lekki-phase-1-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('lekki-phase-1-clinic:washbag', 'lekki-phase-1-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('lekki-phase-1-clinic:guide', 'lekki-phase-1-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('lekki-phase-1-youth:pads', 'lekki-phase-1-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('lekki-phase-1-youth:liners', 'lekki-phase-1-youth', 'liners', 'good', 'For light days at the club.'),
  ('lekki-phase-1-youth:tampons', 'lekki-phase-1-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lekki-phase-1-youth:cup', 'lekki-phase-1-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('lekki-phase-1-youth:pants', 'lekki-phase-1-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('lekki-phase-1-youth:reusable', 'lekki-phase-1-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('lekki-phase-1-youth:heat', 'lekki-phase-1-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('lekki-phase-1-youth:wipes', 'lekki-phase-1-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('lekki-phase-1-youth:washbag', 'lekki-phase-1-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('lekki-phase-1-youth:guide', 'lekki-phase-1-youth', 'guide', 'good', 'Answers questions they have.'),
  ('lekki-phase-1-hostel:pads', 'lekki-phase-1-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('lekki-phase-1-hostel:liners', 'lekki-phase-1-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lekki-phase-1-hostel:tampons', 'lekki-phase-1-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lekki-phase-1-hostel:cup', 'lekki-phase-1-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('lekki-phase-1-hostel:pants', 'lekki-phase-1-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('lekki-phase-1-hostel:reusable', 'lekki-phase-1-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('lekki-phase-1-hostel:heat', 'lekki-phase-1-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('lekki-phase-1-hostel:wipes', 'lekki-phase-1-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('lekki-phase-1-hostel:washbag', 'lekki-phase-1-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('lekki-phase-1-hostel:guide', 'lekki-phase-1-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('lekki-phase-1-home:pads', 'lekki-phase-1-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('lekki-phase-1-home:liners', 'lekki-phase-1-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lekki-phase-1-home:tampons', 'lekki-phase-1-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lekki-phase-1-home:cup', 'lekki-phase-1-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('lekki-phase-1-home:pants', 'lekki-phase-1-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('lekki-phase-1-home:reusable', 'lekki-phase-1-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('lekki-phase-1-home:heat', 'lekki-phase-1-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('lekki-phase-1-home:wipes', 'lekki-phase-1-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('lekki-phase-1-home:washbag', 'lekki-phase-1-home', 'washbag', 'good', 'A private place for her things.'),
  ('lekki-phase-1-home:guide', 'lekki-phase-1-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('lekki-phase-1-football:pads', 'lekki-phase-1-football', 'pads', 'good', 'A simple backup for match day.'),
  ('lekki-phase-1-football:liners', 'lekki-phase-1-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lekki-phase-1-football:tampons', 'lekki-phase-1-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lekki-phase-1-football:cup', 'lekki-phase-1-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('lekki-phase-1-football:pants', 'lekki-phase-1-football', 'pants', 'good', 'Stay put while running.'),
  ('lekki-phase-1-football:reusable', 'lekki-phase-1-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('lekki-phase-1-football:heat', 'lekki-phase-1-football', 'heat', 'good', 'Helps cramps after training.'),
  ('lekki-phase-1-football:wipes', 'lekki-phase-1-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('lekki-phase-1-football:washbag', 'lekki-phase-1-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('lekki-phase-1-football:guide', 'lekki-phase-1-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('lekki-phase-1-training:pads', 'lekki-phase-1-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('lekki-phase-1-training:liners', 'lekki-phase-1-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lekki-phase-1-training:tampons', 'lekki-phase-1-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lekki-phase-1-training:cup', 'lekki-phase-1-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('lekki-phase-1-training:pants', 'lekki-phase-1-training', 'pants', 'good', 'Last through long sessions.'),
  ('lekki-phase-1-training:reusable', 'lekki-phase-1-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('lekki-phase-1-training:heat', 'lekki-phase-1-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('lekki-phase-1-training:wipes', 'lekki-phase-1-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('lekki-phase-1-training:washbag', 'lekki-phase-1-training', 'washbag', 'good', 'Private storage at work.'),
  ('lekki-phase-1-training:guide', 'lekki-phase-1-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikate-school:pads', 'ikate-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ikate-school:liners', 'ikate-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ikate-school:tampons', 'ikate-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikate-school:cup', 'ikate-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikate-school:pants', 'ikate-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikate-school:reusable', 'ikate-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikate-school:heat', 'ikate-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikate-school:wipes', 'ikate-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('ikate-school:washbag', 'ikate-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ikate-school:guide', 'ikate-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ikate-market:pads', 'ikate-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ikate-market:liners', 'ikate-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikate-market:tampons', 'ikate-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikate-market:cup', 'ikate-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikate-market:pants', 'ikate-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikate-market:reusable', 'ikate-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikate-market:heat', 'ikate-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ikate-market:wipes', 'ikate-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ikate-market:washbag', 'ikate-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ikate-market:guide', 'ikate-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikate-busstop:pads', 'ikate-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ikate-busstop:liners', 'ikate-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikate-busstop:tampons', 'ikate-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikate-busstop:cup', 'ikate-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikate-busstop:pants', 'ikate-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ikate-busstop:reusable', 'ikate-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikate-busstop:heat', 'ikate-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ikate-busstop:wipes', 'ikate-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ikate-busstop:washbag', 'ikate-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ikate-busstop:guide', 'ikate-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikate-clinic:pads', 'ikate-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ikate-clinic:liners', 'ikate-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikate-clinic:tampons', 'ikate-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikate-clinic:cup', 'ikate-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikate-clinic:pants', 'ikate-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ikate-clinic:reusable', 'ikate-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikate-clinic:heat', 'ikate-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ikate-clinic:wipes', 'ikate-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikate-clinic:washbag', 'ikate-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ikate-clinic:guide', 'ikate-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ikate-youth:pads', 'ikate-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ikate-youth:liners', 'ikate-youth', 'liners', 'good', 'For light days at the club.'),
  ('ikate-youth:tampons', 'ikate-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikate-youth:cup', 'ikate-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikate-youth:pants', 'ikate-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikate-youth:reusable', 'ikate-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikate-youth:heat', 'ikate-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikate-youth:wipes', 'ikate-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikate-youth:washbag', 'ikate-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ikate-youth:guide', 'ikate-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ikate-waterside:pads', 'ikate-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('ikate-waterside:liners', 'ikate-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikate-waterside:tampons', 'ikate-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('ikate-waterside:cup', 'ikate-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikate-waterside:pants', 'ikate-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('ikate-waterside:reusable', 'ikate-waterside', 'reusable', 'good', 'They last a year when the shop is far.'),
  ('ikate-waterside:heat', 'ikate-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikate-waterside:wipes', 'ikate-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikate-waterside:washbag', 'ikate-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('ikate-waterside:guide', 'ikate-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('ikate-community:pads', 'ikate-community', 'pads', 'good', 'Something for everyone today.'),
  ('ikate-community:liners', 'ikate-community', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikate-community:tampons', 'ikate-community', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikate-community:cup', 'ikate-community', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikate-community:pants', 'ikate-community', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikate-community:reusable', 'ikate-community', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikate-community:heat', 'ikate-community', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ikate-community:wipes', 'ikate-community', 'wipes', 'good', 'There is no running water to wash.'),
  ('ikate-community:washbag', 'ikate-community', 'washbag', 'good', 'Keeps reusables clean.'),
  ('ikate-community:guide', 'ikate-community', 'guide', 'good', 'Helps mothers talk to daughters.'),
  ('ikate-football:pads', 'ikate-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ikate-football:liners', 'ikate-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikate-football:tampons', 'ikate-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikate-football:cup', 'ikate-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ikate-football:pants', 'ikate-football', 'pants', 'good', 'Stay put while running.'),
  ('ikate-football:reusable', 'ikate-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ikate-football:heat', 'ikate-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ikate-football:wipes', 'ikate-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ikate-football:washbag', 'ikate-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ikate-football:guide', 'ikate-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ikate-shelter:pads', 'ikate-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('ikate-shelter:liners', 'ikate-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ikate-shelter:tampons', 'ikate-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ikate-shelter:cup', 'ikate-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ikate-shelter:pants', 'ikate-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ikate-shelter:reusable', 'ikate-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ikate-shelter:heat', 'ikate-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('ikate-shelter:wipes', 'ikate-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('ikate-shelter:washbag', 'ikate-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('ikate-shelter:guide', 'ikate-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('chevron-school:pads', 'chevron-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('chevron-school:liners', 'chevron-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('chevron-school:tampons', 'chevron-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('chevron-school:cup', 'chevron-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('chevron-school:pants', 'chevron-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('chevron-school:reusable', 'chevron-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('chevron-school:heat', 'chevron-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('chevron-school:wipes', 'chevron-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('chevron-school:washbag', 'chevron-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('chevron-school:guide', 'chevron-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('chevron-market:pads', 'chevron-market', 'pads', 'good', 'Quick to change between customers.'),
  ('chevron-market:liners', 'chevron-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('chevron-market:tampons', 'chevron-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('chevron-market:cup', 'chevron-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('chevron-market:pants', 'chevron-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('chevron-market:reusable', 'chevron-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('chevron-market:heat', 'chevron-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('chevron-market:wipes', 'chevron-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('chevron-market:washbag', 'chevron-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('chevron-market:guide', 'chevron-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('chevron-busstop:pads', 'chevron-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('chevron-busstop:liners', 'chevron-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('chevron-busstop:tampons', 'chevron-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('chevron-busstop:cup', 'chevron-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('chevron-busstop:pants', 'chevron-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('chevron-busstop:reusable', 'chevron-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('chevron-busstop:heat', 'chevron-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('chevron-busstop:wipes', 'chevron-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('chevron-busstop:washbag', 'chevron-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('chevron-busstop:guide', 'chevron-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('chevron-clinic:pads', 'chevron-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('chevron-clinic:liners', 'chevron-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('chevron-clinic:tampons', 'chevron-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('chevron-clinic:cup', 'chevron-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('chevron-clinic:pants', 'chevron-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('chevron-clinic:reusable', 'chevron-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('chevron-clinic:heat', 'chevron-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('chevron-clinic:wipes', 'chevron-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('chevron-clinic:washbag', 'chevron-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('chevron-clinic:guide', 'chevron-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('chevron-youth:pads', 'chevron-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('chevron-youth:liners', 'chevron-youth', 'liners', 'good', 'For light days at the club.'),
  ('chevron-youth:tampons', 'chevron-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('chevron-youth:cup', 'chevron-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('chevron-youth:pants', 'chevron-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('chevron-youth:reusable', 'chevron-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('chevron-youth:heat', 'chevron-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('chevron-youth:wipes', 'chevron-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('chevron-youth:washbag', 'chevron-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('chevron-youth:guide', 'chevron-youth', 'guide', 'good', 'Answers questions they have.'),
  ('chevron-hostel:pads', 'chevron-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('chevron-hostel:liners', 'chevron-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('chevron-hostel:tampons', 'chevron-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('chevron-hostel:cup', 'chevron-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('chevron-hostel:pants', 'chevron-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('chevron-hostel:reusable', 'chevron-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('chevron-hostel:heat', 'chevron-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('chevron-hostel:wipes', 'chevron-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('chevron-hostel:washbag', 'chevron-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('chevron-hostel:guide', 'chevron-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('chevron-home:pads', 'chevron-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('chevron-home:liners', 'chevron-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('chevron-home:tampons', 'chevron-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('chevron-home:cup', 'chevron-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('chevron-home:pants', 'chevron-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('chevron-home:reusable', 'chevron-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('chevron-home:heat', 'chevron-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('chevron-home:wipes', 'chevron-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('chevron-home:washbag', 'chevron-home', 'washbag', 'good', 'A private place for her things.'),
  ('chevron-home:guide', 'chevron-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('chevron-football:pads', 'chevron-football', 'pads', 'good', 'A simple backup for match day.'),
  ('chevron-football:liners', 'chevron-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('chevron-football:tampons', 'chevron-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('chevron-football:cup', 'chevron-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('chevron-football:pants', 'chevron-football', 'pants', 'good', 'Stay put while running.'),
  ('chevron-football:reusable', 'chevron-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('chevron-football:heat', 'chevron-football', 'heat', 'good', 'Helps cramps after training.'),
  ('chevron-football:wipes', 'chevron-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('chevron-football:washbag', 'chevron-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('chevron-football:guide', 'chevron-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('chevron-training:pads', 'chevron-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('chevron-training:liners', 'chevron-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('chevron-training:tampons', 'chevron-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('chevron-training:cup', 'chevron-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('chevron-training:pants', 'chevron-training', 'pants', 'good', 'Last through long sessions.'),
  ('chevron-training:reusable', 'chevron-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('chevron-training:heat', 'chevron-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('chevron-training:wipes', 'chevron-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('chevron-training:washbag', 'chevron-training', 'washbag', 'good', 'Private storage at work.'),
  ('chevron-training:guide', 'chevron-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('chevron-shelter:pads', 'chevron-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('chevron-shelter:liners', 'chevron-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('chevron-shelter:tampons', 'chevron-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('chevron-shelter:cup', 'chevron-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('chevron-shelter:pants', 'chevron-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('chevron-shelter:reusable', 'chevron-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('chevron-shelter:heat', 'chevron-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('chevron-shelter:wipes', 'chevron-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('chevron-shelter:washbag', 'chevron-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('chevron-shelter:guide', 'chevron-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('osapa-school:pads', 'osapa-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('osapa-school:liners', 'osapa-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('osapa-school:tampons', 'osapa-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('osapa-school:cup', 'osapa-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('osapa-school:pants', 'osapa-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('osapa-school:reusable', 'osapa-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('osapa-school:heat', 'osapa-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('osapa-school:wipes', 'osapa-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('osapa-school:washbag', 'osapa-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('osapa-school:guide', 'osapa-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('osapa-market:pads', 'osapa-market', 'pads', 'good', 'Quick to change between customers.'),
  ('osapa-market:liners', 'osapa-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('osapa-market:tampons', 'osapa-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('osapa-market:cup', 'osapa-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('osapa-market:pants', 'osapa-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('osapa-market:reusable', 'osapa-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('osapa-market:heat', 'osapa-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('osapa-market:wipes', 'osapa-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('osapa-market:washbag', 'osapa-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('osapa-market:guide', 'osapa-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('osapa-busstop:pads', 'osapa-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('osapa-busstop:liners', 'osapa-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('osapa-busstop:tampons', 'osapa-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('osapa-busstop:cup', 'osapa-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('osapa-busstop:pants', 'osapa-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('osapa-busstop:reusable', 'osapa-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('osapa-busstop:heat', 'osapa-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('osapa-busstop:wipes', 'osapa-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('osapa-busstop:washbag', 'osapa-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('osapa-busstop:guide', 'osapa-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('osapa-clinic:pads', 'osapa-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('osapa-clinic:liners', 'osapa-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('osapa-clinic:tampons', 'osapa-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('osapa-clinic:cup', 'osapa-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('osapa-clinic:pants', 'osapa-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('osapa-clinic:reusable', 'osapa-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('osapa-clinic:heat', 'osapa-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('osapa-clinic:wipes', 'osapa-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('osapa-clinic:washbag', 'osapa-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('osapa-clinic:guide', 'osapa-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('osapa-youth:pads', 'osapa-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('osapa-youth:liners', 'osapa-youth', 'liners', 'good', 'For light days at the club.'),
  ('osapa-youth:tampons', 'osapa-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('osapa-youth:cup', 'osapa-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('osapa-youth:pants', 'osapa-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('osapa-youth:reusable', 'osapa-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('osapa-youth:heat', 'osapa-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('osapa-youth:wipes', 'osapa-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('osapa-youth:washbag', 'osapa-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('osapa-youth:guide', 'osapa-youth', 'guide', 'good', 'Answers questions they have.'),
  ('osapa-waterside:pads', 'osapa-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('osapa-waterside:liners', 'osapa-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('osapa-waterside:tampons', 'osapa-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('osapa-waterside:cup', 'osapa-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('osapa-waterside:pants', 'osapa-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('osapa-waterside:reusable', 'osapa-waterside', 'reusable', 'good', 'They last a year when the shop is far.'),
  ('osapa-waterside:heat', 'osapa-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('osapa-waterside:wipes', 'osapa-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('osapa-waterside:washbag', 'osapa-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('osapa-waterside:guide', 'osapa-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('osapa-hostel:pads', 'osapa-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('osapa-hostel:liners', 'osapa-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('osapa-hostel:tampons', 'osapa-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('osapa-hostel:cup', 'osapa-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('osapa-hostel:pants', 'osapa-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('osapa-hostel:reusable', 'osapa-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('osapa-hostel:heat', 'osapa-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('osapa-hostel:wipes', 'osapa-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('osapa-hostel:washbag', 'osapa-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('osapa-hostel:guide', 'osapa-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('osapa-football:pads', 'osapa-football', 'pads', 'good', 'A simple backup for match day.'),
  ('osapa-football:liners', 'osapa-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('osapa-football:tampons', 'osapa-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('osapa-football:cup', 'osapa-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('osapa-football:pants', 'osapa-football', 'pants', 'good', 'Stay put while running.'),
  ('osapa-football:reusable', 'osapa-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('osapa-football:heat', 'osapa-football', 'heat', 'good', 'Helps cramps after training.'),
  ('osapa-football:wipes', 'osapa-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('osapa-football:washbag', 'osapa-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('osapa-football:guide', 'osapa-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('osapa-training:pads', 'osapa-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('osapa-training:liners', 'osapa-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('osapa-training:tampons', 'osapa-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('osapa-training:cup', 'osapa-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('osapa-training:pants', 'osapa-training', 'pants', 'good', 'Last through long sessions.'),
  ('osapa-training:reusable', 'osapa-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('osapa-training:heat', 'osapa-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('osapa-training:wipes', 'osapa-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('osapa-training:washbag', 'osapa-training', 'washbag', 'good', 'Private storage at work.'),
  ('osapa-training:guide', 'osapa-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ajah-school:pads', 'ajah-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ajah-school:liners', 'ajah-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ajah-school:tampons', 'ajah-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajah-school:cup', 'ajah-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ajah-school:pants', 'ajah-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ajah-school:reusable', 'ajah-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ajah-school:heat', 'ajah-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ajah-school:wipes', 'ajah-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('ajah-school:washbag', 'ajah-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ajah-school:guide', 'ajah-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ajah-market:pads', 'ajah-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ajah-market:liners', 'ajah-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajah-market:tampons', 'ajah-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajah-market:cup', 'ajah-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ajah-market:pants', 'ajah-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ajah-market:reusable', 'ajah-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ajah-market:heat', 'ajah-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ajah-market:wipes', 'ajah-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ajah-market:washbag', 'ajah-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ajah-market:guide', 'ajah-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ajah-busstop:pads', 'ajah-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ajah-busstop:liners', 'ajah-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajah-busstop:tampons', 'ajah-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajah-busstop:cup', 'ajah-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ajah-busstop:pants', 'ajah-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ajah-busstop:reusable', 'ajah-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ajah-busstop:heat', 'ajah-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ajah-busstop:wipes', 'ajah-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ajah-busstop:washbag', 'ajah-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ajah-busstop:guide', 'ajah-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ajah-clinic:pads', 'ajah-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ajah-clinic:liners', 'ajah-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajah-clinic:tampons', 'ajah-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajah-clinic:cup', 'ajah-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ajah-clinic:pants', 'ajah-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ajah-clinic:reusable', 'ajah-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ajah-clinic:heat', 'ajah-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ajah-clinic:wipes', 'ajah-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ajah-clinic:washbag', 'ajah-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ajah-clinic:guide', 'ajah-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ajah-youth:pads', 'ajah-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ajah-youth:liners', 'ajah-youth', 'liners', 'good', 'For light days at the club.'),
  ('ajah-youth:tampons', 'ajah-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajah-youth:cup', 'ajah-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ajah-youth:pants', 'ajah-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ajah-youth:reusable', 'ajah-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ajah-youth:heat', 'ajah-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ajah-youth:wipes', 'ajah-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ajah-youth:washbag', 'ajah-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ajah-youth:guide', 'ajah-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ajah-waterside:pads', 'ajah-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('ajah-waterside:liners', 'ajah-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajah-waterside:tampons', 'ajah-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('ajah-waterside:cup', 'ajah-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ajah-waterside:pants', 'ajah-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('ajah-waterside:reusable', 'ajah-waterside', 'reusable', 'good', 'They last a year when the shop is far.'),
  ('ajah-waterside:heat', 'ajah-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ajah-waterside:wipes', 'ajah-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ajah-waterside:washbag', 'ajah-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('ajah-waterside:guide', 'ajah-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('ajah-hostel:pads', 'ajah-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('ajah-hostel:liners', 'ajah-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajah-hostel:tampons', 'ajah-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajah-hostel:cup', 'ajah-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ajah-hostel:pants', 'ajah-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ajah-hostel:reusable', 'ajah-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('ajah-hostel:heat', 'ajah-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ajah-hostel:wipes', 'ajah-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ajah-hostel:washbag', 'ajah-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('ajah-hostel:guide', 'ajah-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('ajah-football:pads', 'ajah-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ajah-football:liners', 'ajah-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajah-football:tampons', 'ajah-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajah-football:cup', 'ajah-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ajah-football:pants', 'ajah-football', 'pants', 'good', 'Stay put while running.'),
  ('ajah-football:reusable', 'ajah-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ajah-football:heat', 'ajah-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ajah-football:wipes', 'ajah-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ajah-football:washbag', 'ajah-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ajah-football:guide', 'ajah-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ajah-training:pads', 'ajah-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('ajah-training:liners', 'ajah-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajah-training:tampons', 'ajah-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajah-training:cup', 'ajah-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ajah-training:pants', 'ajah-training', 'pants', 'good', 'Last through long sessions.'),
  ('ajah-training:reusable', 'ajah-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ajah-training:heat', 'ajah-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('ajah-training:wipes', 'ajah-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ajah-training:washbag', 'ajah-training', 'washbag', 'good', 'Private storage at work.'),
  ('ajah-training:guide', 'ajah-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ajah-shelter:pads', 'ajah-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('ajah-shelter:liners', 'ajah-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ajah-shelter:tampons', 'ajah-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ajah-shelter:cup', 'ajah-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ajah-shelter:pants', 'ajah-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ajah-shelter:reusable', 'ajah-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ajah-shelter:heat', 'ajah-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('ajah-shelter:wipes', 'ajah-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('ajah-shelter:washbag', 'ajah-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('ajah-shelter:guide', 'ajah-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('sangotedo-school:pads', 'sangotedo-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('sangotedo-school:liners', 'sangotedo-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('sangotedo-school:tampons', 'sangotedo-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('sangotedo-school:cup', 'sangotedo-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('sangotedo-school:pants', 'sangotedo-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('sangotedo-school:reusable', 'sangotedo-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('sangotedo-school:heat', 'sangotedo-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('sangotedo-school:wipes', 'sangotedo-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('sangotedo-school:washbag', 'sangotedo-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('sangotedo-school:guide', 'sangotedo-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('sangotedo-market:pads', 'sangotedo-market', 'pads', 'good', 'Quick to change between customers.'),
  ('sangotedo-market:liners', 'sangotedo-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('sangotedo-market:tampons', 'sangotedo-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('sangotedo-market:cup', 'sangotedo-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('sangotedo-market:pants', 'sangotedo-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('sangotedo-market:reusable', 'sangotedo-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('sangotedo-market:heat', 'sangotedo-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('sangotedo-market:wipes', 'sangotedo-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('sangotedo-market:washbag', 'sangotedo-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('sangotedo-market:guide', 'sangotedo-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('sangotedo-busstop:pads', 'sangotedo-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('sangotedo-busstop:liners', 'sangotedo-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('sangotedo-busstop:tampons', 'sangotedo-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('sangotedo-busstop:cup', 'sangotedo-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('sangotedo-busstop:pants', 'sangotedo-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('sangotedo-busstop:reusable', 'sangotedo-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('sangotedo-busstop:heat', 'sangotedo-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('sangotedo-busstop:wipes', 'sangotedo-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('sangotedo-busstop:washbag', 'sangotedo-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('sangotedo-busstop:guide', 'sangotedo-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('sangotedo-clinic:pads', 'sangotedo-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('sangotedo-clinic:liners', 'sangotedo-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('sangotedo-clinic:tampons', 'sangotedo-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('sangotedo-clinic:cup', 'sangotedo-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('sangotedo-clinic:pants', 'sangotedo-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('sangotedo-clinic:reusable', 'sangotedo-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('sangotedo-clinic:heat', 'sangotedo-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('sangotedo-clinic:wipes', 'sangotedo-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('sangotedo-clinic:washbag', 'sangotedo-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('sangotedo-clinic:guide', 'sangotedo-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('sangotedo-youth:pads', 'sangotedo-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('sangotedo-youth:liners', 'sangotedo-youth', 'liners', 'good', 'For light days at the club.'),
  ('sangotedo-youth:tampons', 'sangotedo-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('sangotedo-youth:cup', 'sangotedo-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('sangotedo-youth:pants', 'sangotedo-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('sangotedo-youth:reusable', 'sangotedo-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('sangotedo-youth:heat', 'sangotedo-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('sangotedo-youth:wipes', 'sangotedo-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('sangotedo-youth:washbag', 'sangotedo-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('sangotedo-youth:guide', 'sangotedo-youth', 'guide', 'good', 'Answers questions they have.'),
  ('sangotedo-hostel:pads', 'sangotedo-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('sangotedo-hostel:liners', 'sangotedo-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('sangotedo-hostel:tampons', 'sangotedo-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('sangotedo-hostel:cup', 'sangotedo-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('sangotedo-hostel:pants', 'sangotedo-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('sangotedo-hostel:reusable', 'sangotedo-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('sangotedo-hostel:heat', 'sangotedo-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('sangotedo-hostel:wipes', 'sangotedo-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('sangotedo-hostel:washbag', 'sangotedo-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('sangotedo-hostel:guide', 'sangotedo-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('sangotedo-home:pads', 'sangotedo-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('sangotedo-home:liners', 'sangotedo-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('sangotedo-home:tampons', 'sangotedo-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('sangotedo-home:cup', 'sangotedo-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('sangotedo-home:pants', 'sangotedo-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('sangotedo-home:reusable', 'sangotedo-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('sangotedo-home:heat', 'sangotedo-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('sangotedo-home:wipes', 'sangotedo-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('sangotedo-home:washbag', 'sangotedo-home', 'washbag', 'good', 'A private place for her things.'),
  ('sangotedo-home:guide', 'sangotedo-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('sangotedo-football:pads', 'sangotedo-football', 'pads', 'good', 'A simple backup for match day.'),
  ('sangotedo-football:liners', 'sangotedo-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('sangotedo-football:tampons', 'sangotedo-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('sangotedo-football:cup', 'sangotedo-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('sangotedo-football:pants', 'sangotedo-football', 'pants', 'good', 'Stay put while running.'),
  ('sangotedo-football:reusable', 'sangotedo-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('sangotedo-football:heat', 'sangotedo-football', 'heat', 'good', 'Helps cramps after training.'),
  ('sangotedo-football:wipes', 'sangotedo-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('sangotedo-football:washbag', 'sangotedo-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('sangotedo-football:guide', 'sangotedo-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('sangotedo-training:pads', 'sangotedo-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('sangotedo-training:liners', 'sangotedo-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('sangotedo-training:tampons', 'sangotedo-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('sangotedo-training:cup', 'sangotedo-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('sangotedo-training:pants', 'sangotedo-training', 'pants', 'good', 'Last through long sessions.'),
  ('sangotedo-training:reusable', 'sangotedo-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('sangotedo-training:heat', 'sangotedo-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('sangotedo-training:wipes', 'sangotedo-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('sangotedo-training:washbag', 'sangotedo-training', 'washbag', 'good', 'Private storage at work.'),
  ('sangotedo-training:guide', 'sangotedo-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('abijo-school:pads', 'abijo-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('abijo-school:liners', 'abijo-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('abijo-school:tampons', 'abijo-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('abijo-school:cup', 'abijo-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('abijo-school:pants', 'abijo-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('abijo-school:reusable', 'abijo-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('abijo-school:heat', 'abijo-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('abijo-school:wipes', 'abijo-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('abijo-school:washbag', 'abijo-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('abijo-school:guide', 'abijo-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('abijo-market:pads', 'abijo-market', 'pads', 'good', 'Quick to change between customers.'),
  ('abijo-market:liners', 'abijo-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('abijo-market:tampons', 'abijo-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('abijo-market:cup', 'abijo-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('abijo-market:pants', 'abijo-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('abijo-market:reusable', 'abijo-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('abijo-market:heat', 'abijo-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('abijo-market:wipes', 'abijo-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('abijo-market:washbag', 'abijo-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('abijo-market:guide', 'abijo-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('abijo-busstop:pads', 'abijo-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('abijo-busstop:liners', 'abijo-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('abijo-busstop:tampons', 'abijo-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('abijo-busstop:cup', 'abijo-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('abijo-busstop:pants', 'abijo-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('abijo-busstop:reusable', 'abijo-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('abijo-busstop:heat', 'abijo-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('abijo-busstop:wipes', 'abijo-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('abijo-busstop:washbag', 'abijo-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('abijo-busstop:guide', 'abijo-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('abijo-clinic:pads', 'abijo-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('abijo-clinic:liners', 'abijo-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('abijo-clinic:tampons', 'abijo-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('abijo-clinic:cup', 'abijo-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('abijo-clinic:pants', 'abijo-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('abijo-clinic:reusable', 'abijo-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('abijo-clinic:heat', 'abijo-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('abijo-clinic:wipes', 'abijo-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('abijo-clinic:washbag', 'abijo-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('abijo-clinic:guide', 'abijo-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('abijo-youth:pads', 'abijo-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('abijo-youth:liners', 'abijo-youth', 'liners', 'good', 'For light days at the club.'),
  ('abijo-youth:tampons', 'abijo-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('abijo-youth:cup', 'abijo-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('abijo-youth:pants', 'abijo-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('abijo-youth:reusable', 'abijo-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('abijo-youth:heat', 'abijo-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('abijo-youth:wipes', 'abijo-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('abijo-youth:washbag', 'abijo-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('abijo-youth:guide', 'abijo-youth', 'guide', 'good', 'Answers questions they have.'),
  ('abijo-hostel:pads', 'abijo-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('abijo-hostel:liners', 'abijo-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('abijo-hostel:tampons', 'abijo-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('abijo-hostel:cup', 'abijo-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('abijo-hostel:pants', 'abijo-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('abijo-hostel:reusable', 'abijo-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('abijo-hostel:heat', 'abijo-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('abijo-hostel:wipes', 'abijo-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('abijo-hostel:washbag', 'abijo-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('abijo-hostel:guide', 'abijo-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('abijo-home:pads', 'abijo-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('abijo-home:liners', 'abijo-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('abijo-home:tampons', 'abijo-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('abijo-home:cup', 'abijo-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('abijo-home:pants', 'abijo-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('abijo-home:reusable', 'abijo-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('abijo-home:heat', 'abijo-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('abijo-home:wipes', 'abijo-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('abijo-home:washbag', 'abijo-home', 'washbag', 'good', 'A private place for her things.'),
  ('abijo-home:guide', 'abijo-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('abijo-football:pads', 'abijo-football', 'pads', 'good', 'A simple backup for match day.'),
  ('abijo-football:liners', 'abijo-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('abijo-football:tampons', 'abijo-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('abijo-football:cup', 'abijo-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('abijo-football:pants', 'abijo-football', 'pants', 'good', 'Stay put while running.'),
  ('abijo-football:reusable', 'abijo-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('abijo-football:heat', 'abijo-football', 'heat', 'good', 'Helps cramps after training.'),
  ('abijo-football:wipes', 'abijo-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('abijo-football:washbag', 'abijo-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('abijo-football:guide', 'abijo-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('abijo-training:pads', 'abijo-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('abijo-training:liners', 'abijo-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('abijo-training:tampons', 'abijo-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('abijo-training:cup', 'abijo-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('abijo-training:pants', 'abijo-training', 'pants', 'good', 'Last through long sessions.'),
  ('abijo-training:reusable', 'abijo-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('abijo-training:heat', 'abijo-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('abijo-training:wipes', 'abijo-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('abijo-training:washbag', 'abijo-training', 'washbag', 'good', 'Private storage at work.'),
  ('abijo-training:guide', 'abijo-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('abijo-shelter:pads', 'abijo-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('abijo-shelter:liners', 'abijo-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('abijo-shelter:tampons', 'abijo-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('abijo-shelter:cup', 'abijo-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('abijo-shelter:pants', 'abijo-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('abijo-shelter:reusable', 'abijo-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('abijo-shelter:heat', 'abijo-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('abijo-shelter:wipes', 'abijo-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('abijo-shelter:washbag', 'abijo-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('abijo-shelter:guide', 'abijo-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('lakowe-school:pads', 'lakowe-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('lakowe-school:liners', 'lakowe-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('lakowe-school:tampons', 'lakowe-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lakowe-school:cup', 'lakowe-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('lakowe-school:pants', 'lakowe-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('lakowe-school:reusable', 'lakowe-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('lakowe-school:heat', 'lakowe-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('lakowe-school:wipes', 'lakowe-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('lakowe-school:washbag', 'lakowe-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('lakowe-school:guide', 'lakowe-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('lakowe-market:pads', 'lakowe-market', 'pads', 'good', 'Quick to change between customers.'),
  ('lakowe-market:liners', 'lakowe-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lakowe-market:tampons', 'lakowe-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lakowe-market:cup', 'lakowe-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('lakowe-market:pants', 'lakowe-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('lakowe-market:reusable', 'lakowe-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('lakowe-market:heat', 'lakowe-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('lakowe-market:wipes', 'lakowe-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('lakowe-market:washbag', 'lakowe-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('lakowe-market:guide', 'lakowe-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('lakowe-busstop:pads', 'lakowe-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('lakowe-busstop:liners', 'lakowe-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lakowe-busstop:tampons', 'lakowe-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lakowe-busstop:cup', 'lakowe-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('lakowe-busstop:pants', 'lakowe-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('lakowe-busstop:reusable', 'lakowe-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('lakowe-busstop:heat', 'lakowe-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('lakowe-busstop:wipes', 'lakowe-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('lakowe-busstop:washbag', 'lakowe-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('lakowe-busstop:guide', 'lakowe-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('lakowe-clinic:pads', 'lakowe-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('lakowe-clinic:liners', 'lakowe-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lakowe-clinic:tampons', 'lakowe-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lakowe-clinic:cup', 'lakowe-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('lakowe-clinic:pants', 'lakowe-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('lakowe-clinic:reusable', 'lakowe-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('lakowe-clinic:heat', 'lakowe-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('lakowe-clinic:wipes', 'lakowe-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('lakowe-clinic:washbag', 'lakowe-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('lakowe-clinic:guide', 'lakowe-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('lakowe-youth:pads', 'lakowe-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('lakowe-youth:liners', 'lakowe-youth', 'liners', 'good', 'For light days at the club.'),
  ('lakowe-youth:tampons', 'lakowe-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lakowe-youth:cup', 'lakowe-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('lakowe-youth:pants', 'lakowe-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('lakowe-youth:reusable', 'lakowe-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('lakowe-youth:heat', 'lakowe-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('lakowe-youth:wipes', 'lakowe-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('lakowe-youth:washbag', 'lakowe-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('lakowe-youth:guide', 'lakowe-youth', 'guide', 'good', 'Answers questions they have.'),
  ('lakowe-hostel:pads', 'lakowe-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('lakowe-hostel:liners', 'lakowe-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lakowe-hostel:tampons', 'lakowe-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lakowe-hostel:cup', 'lakowe-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('lakowe-hostel:pants', 'lakowe-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('lakowe-hostel:reusable', 'lakowe-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('lakowe-hostel:heat', 'lakowe-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('lakowe-hostel:wipes', 'lakowe-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('lakowe-hostel:washbag', 'lakowe-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('lakowe-hostel:guide', 'lakowe-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('lakowe-home:pads', 'lakowe-home', 'pads', 'good', 'Easy for girls new to periods.'),
  ('lakowe-home:liners', 'lakowe-home', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lakowe-home:tampons', 'lakowe-home', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lakowe-home:cup', 'lakowe-home', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('lakowe-home:pants', 'lakowe-home', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('lakowe-home:reusable', 'lakowe-home', 'reusable', 'good', 'Clean water and a line mean these can last a year.'),
  ('lakowe-home:heat', 'lakowe-home', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('lakowe-home:wipes', 'lakowe-home', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('lakowe-home:washbag', 'lakowe-home', 'washbag', 'good', 'A private place for her things.'),
  ('lakowe-home:guide', 'lakowe-home', 'guide', 'good', 'Helps girls who have not started yet.'),
  ('lakowe-football:pads', 'lakowe-football', 'pads', 'good', 'A simple backup for match day.'),
  ('lakowe-football:liners', 'lakowe-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lakowe-football:tampons', 'lakowe-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lakowe-football:cup', 'lakowe-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('lakowe-football:pants', 'lakowe-football', 'pants', 'good', 'Stay put while running.'),
  ('lakowe-football:reusable', 'lakowe-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('lakowe-football:heat', 'lakowe-football', 'heat', 'good', 'Helps cramps after training.'),
  ('lakowe-football:wipes', 'lakowe-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('lakowe-football:washbag', 'lakowe-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('lakowe-football:guide', 'lakowe-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('lakowe-training:pads', 'lakowe-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('lakowe-training:liners', 'lakowe-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lakowe-training:tampons', 'lakowe-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lakowe-training:cup', 'lakowe-training', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('lakowe-training:pants', 'lakowe-training', 'pants', 'good', 'Last through long sessions.'),
  ('lakowe-training:reusable', 'lakowe-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('lakowe-training:heat', 'lakowe-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('lakowe-training:wipes', 'lakowe-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('lakowe-training:washbag', 'lakowe-training', 'washbag', 'good', 'Private storage at work.'),
  ('lakowe-training:guide', 'lakowe-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('lakowe-shelter:pads', 'lakowe-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('lakowe-shelter:liners', 'lakowe-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('lakowe-shelter:tampons', 'lakowe-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('lakowe-shelter:cup', 'lakowe-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('lakowe-shelter:pants', 'lakowe-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('lakowe-shelter:reusable', 'lakowe-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('lakowe-shelter:heat', 'lakowe-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('lakowe-shelter:wipes', 'lakowe-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('lakowe-shelter:washbag', 'lakowe-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('lakowe-shelter:guide', 'lakowe-shelter', 'guide', 'ok', 'Helpful, but not the main need here.');

INSERT INTO giveday_needs (id, place_id, product_id, rating, why) VALUES
  ('eleko-school:pads', 'eleko-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('eleko-school:liners', 'eleko-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('eleko-school:tampons', 'eleko-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('eleko-school:cup', 'eleko-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('eleko-school:pants', 'eleko-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('eleko-school:reusable', 'eleko-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('eleko-school:heat', 'eleko-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('eleko-school:wipes', 'eleko-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('eleko-school:washbag', 'eleko-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('eleko-school:guide', 'eleko-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('eleko-market:pads', 'eleko-market', 'pads', 'good', 'Quick to change between customers.'),
  ('eleko-market:liners', 'eleko-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('eleko-market:tampons', 'eleko-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('eleko-market:cup', 'eleko-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('eleko-market:pants', 'eleko-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('eleko-market:reusable', 'eleko-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('eleko-market:heat', 'eleko-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('eleko-market:wipes', 'eleko-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('eleko-market:washbag', 'eleko-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('eleko-market:guide', 'eleko-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('eleko-busstop:pads', 'eleko-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('eleko-busstop:liners', 'eleko-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('eleko-busstop:tampons', 'eleko-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('eleko-busstop:cup', 'eleko-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('eleko-busstop:pants', 'eleko-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('eleko-busstop:reusable', 'eleko-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('eleko-busstop:heat', 'eleko-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('eleko-busstop:wipes', 'eleko-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('eleko-busstop:washbag', 'eleko-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('eleko-busstop:guide', 'eleko-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('eleko-clinic:pads', 'eleko-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('eleko-clinic:liners', 'eleko-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('eleko-clinic:tampons', 'eleko-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('eleko-clinic:cup', 'eleko-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('eleko-clinic:pants', 'eleko-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('eleko-clinic:reusable', 'eleko-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('eleko-clinic:heat', 'eleko-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('eleko-clinic:wipes', 'eleko-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('eleko-clinic:washbag', 'eleko-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('eleko-clinic:guide', 'eleko-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('eleko-youth:pads', 'eleko-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('eleko-youth:liners', 'eleko-youth', 'liners', 'good', 'For light days at the club.'),
  ('eleko-youth:tampons', 'eleko-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('eleko-youth:cup', 'eleko-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('eleko-youth:pants', 'eleko-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('eleko-youth:reusable', 'eleko-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('eleko-youth:heat', 'eleko-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('eleko-youth:wipes', 'eleko-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('eleko-youth:washbag', 'eleko-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('eleko-youth:guide', 'eleko-youth', 'guide', 'good', 'Answers questions they have.'),
  ('eleko-waterside:pads', 'eleko-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('eleko-waterside:liners', 'eleko-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('eleko-waterside:tampons', 'eleko-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('eleko-waterside:cup', 'eleko-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('eleko-waterside:pants', 'eleko-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('eleko-waterside:reusable', 'eleko-waterside', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('eleko-waterside:heat', 'eleko-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('eleko-waterside:wipes', 'eleko-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('eleko-waterside:washbag', 'eleko-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('eleko-waterside:guide', 'eleko-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('eleko-community:pads', 'eleko-community', 'pads', 'good', 'Something for everyone today.'),
  ('eleko-community:liners', 'eleko-community', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('eleko-community:tampons', 'eleko-community', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('eleko-community:cup', 'eleko-community', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('eleko-community:pants', 'eleko-community', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('eleko-community:reusable', 'eleko-community', 'reusable', 'good', 'With water, these save money for a year.'),
  ('eleko-community:heat', 'eleko-community', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('eleko-community:wipes', 'eleko-community', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('eleko-community:washbag', 'eleko-community', 'washbag', 'good', 'Keeps reusables clean.'),
  ('eleko-community:guide', 'eleko-community', 'guide', 'good', 'Helps mothers talk to daughters.'),
  ('eleko-football:pads', 'eleko-football', 'pads', 'good', 'A simple backup for match day.'),
  ('eleko-football:liners', 'eleko-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('eleko-football:tampons', 'eleko-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('eleko-football:cup', 'eleko-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('eleko-football:pants', 'eleko-football', 'pants', 'good', 'Stay put while running.'),
  ('eleko-football:reusable', 'eleko-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('eleko-football:heat', 'eleko-football', 'heat', 'good', 'Helps cramps after training.'),
  ('eleko-football:wipes', 'eleko-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('eleko-football:washbag', 'eleko-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('eleko-football:guide', 'eleko-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('eleko-shelter:pads', 'eleko-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('eleko-shelter:liners', 'eleko-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('eleko-shelter:tampons', 'eleko-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('eleko-shelter:cup', 'eleko-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('eleko-shelter:pants', 'eleko-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('eleko-shelter:reusable', 'eleko-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('eleko-shelter:heat', 'eleko-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('eleko-shelter:wipes', 'eleko-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('eleko-shelter:washbag', 'eleko-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('eleko-shelter:guide', 'eleko-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ibeju-school:pads', 'ibeju-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('ibeju-school:liners', 'ibeju-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('ibeju-school:tampons', 'ibeju-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ibeju-school:cup', 'ibeju-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ibeju-school:pants', 'ibeju-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ibeju-school:reusable', 'ibeju-school', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ibeju-school:heat', 'ibeju-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ibeju-school:wipes', 'ibeju-school', 'wipes', 'good', 'Handy when the toilets have no water.'),
  ('ibeju-school:washbag', 'ibeju-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('ibeju-school:guide', 'ibeju-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('ibeju-market:pads', 'ibeju-market', 'pads', 'good', 'Quick to change between customers.'),
  ('ibeju-market:liners', 'ibeju-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ibeju-market:tampons', 'ibeju-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ibeju-market:cup', 'ibeju-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ibeju-market:pants', 'ibeju-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ibeju-market:reusable', 'ibeju-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ibeju-market:heat', 'ibeju-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('ibeju-market:wipes', 'ibeju-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('ibeju-market:washbag', 'ibeju-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('ibeju-market:guide', 'ibeju-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ibeju-busstop:pads', 'ibeju-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('ibeju-busstop:liners', 'ibeju-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ibeju-busstop:tampons', 'ibeju-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ibeju-busstop:cup', 'ibeju-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ibeju-busstop:pants', 'ibeju-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('ibeju-busstop:reusable', 'ibeju-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ibeju-busstop:heat', 'ibeju-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('ibeju-busstop:wipes', 'ibeju-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('ibeju-busstop:washbag', 'ibeju-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('ibeju-busstop:guide', 'ibeju-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ibeju-clinic:pads', 'ibeju-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('ibeju-clinic:liners', 'ibeju-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ibeju-clinic:tampons', 'ibeju-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ibeju-clinic:cup', 'ibeju-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ibeju-clinic:pants', 'ibeju-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('ibeju-clinic:reusable', 'ibeju-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ibeju-clinic:heat', 'ibeju-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('ibeju-clinic:wipes', 'ibeju-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ibeju-clinic:washbag', 'ibeju-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('ibeju-clinic:guide', 'ibeju-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('ibeju-youth:pads', 'ibeju-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('ibeju-youth:liners', 'ibeju-youth', 'liners', 'good', 'For light days at the club.'),
  ('ibeju-youth:tampons', 'ibeju-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ibeju-youth:cup', 'ibeju-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ibeju-youth:pants', 'ibeju-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ibeju-youth:reusable', 'ibeju-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ibeju-youth:heat', 'ibeju-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ibeju-youth:wipes', 'ibeju-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ibeju-youth:washbag', 'ibeju-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('ibeju-youth:guide', 'ibeju-youth', 'guide', 'good', 'Answers questions they have.'),
  ('ibeju-waterside:pads', 'ibeju-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('ibeju-waterside:liners', 'ibeju-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ibeju-waterside:tampons', 'ibeju-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('ibeju-waterside:cup', 'ibeju-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ibeju-waterside:pants', 'ibeju-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('ibeju-waterside:reusable', 'ibeju-waterside', 'reusable', 'good', 'They last a year when the shop is far.'),
  ('ibeju-waterside:heat', 'ibeju-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ibeju-waterside:wipes', 'ibeju-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ibeju-waterside:washbag', 'ibeju-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('ibeju-waterside:guide', 'ibeju-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('ibeju-community:pads', 'ibeju-community', 'pads', 'good', 'Something for everyone today.'),
  ('ibeju-community:liners', 'ibeju-community', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ibeju-community:tampons', 'ibeju-community', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ibeju-community:cup', 'ibeju-community', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ibeju-community:pants', 'ibeju-community', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ibeju-community:reusable', 'ibeju-community', 'reusable', 'good', 'With water, these save money for a year.'),
  ('ibeju-community:heat', 'ibeju-community', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ibeju-community:wipes', 'ibeju-community', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ibeju-community:washbag', 'ibeju-community', 'washbag', 'good', 'Keeps reusables clean.'),
  ('ibeju-community:guide', 'ibeju-community', 'guide', 'good', 'Helps mothers talk to daughters.'),
  ('ibeju-football:pads', 'ibeju-football', 'pads', 'good', 'A simple backup for match day.'),
  ('ibeju-football:liners', 'ibeju-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ibeju-football:tampons', 'ibeju-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ibeju-football:cup', 'ibeju-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('ibeju-football:pants', 'ibeju-football', 'pants', 'good', 'Stay put while running.'),
  ('ibeju-football:reusable', 'ibeju-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('ibeju-football:heat', 'ibeju-football', 'heat', 'good', 'Helps cramps after training.'),
  ('ibeju-football:wipes', 'ibeju-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('ibeju-football:washbag', 'ibeju-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('ibeju-football:guide', 'ibeju-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ibeju-shelter:pads', 'ibeju-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('ibeju-shelter:liners', 'ibeju-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ibeju-shelter:tampons', 'ibeju-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ibeju-shelter:cup', 'ibeju-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ibeju-shelter:pants', 'ibeju-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ibeju-shelter:reusable', 'ibeju-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ibeju-shelter:heat', 'ibeju-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('ibeju-shelter:wipes', 'ibeju-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('ibeju-shelter:washbag', 'ibeju-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('ibeju-shelter:guide', 'ibeju-shelter', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('ibeju-community-2:pads', 'ibeju-community-2', 'pads', 'good', 'Something for everyone today.'),
  ('ibeju-community-2:liners', 'ibeju-community-2', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('ibeju-community-2:tampons', 'ibeju-community-2', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('ibeju-community-2:cup', 'ibeju-community-2', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('ibeju-community-2:pants', 'ibeju-community-2', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('ibeju-community-2:reusable', 'ibeju-community-2', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('ibeju-community-2:heat', 'ibeju-community-2', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('ibeju-community-2:wipes', 'ibeju-community-2', 'wipes', 'good', 'There is no running water to wash.'),
  ('ibeju-community-2:washbag', 'ibeju-community-2', 'washbag', 'good', 'Keeps reusables clean.'),
  ('ibeju-community-2:guide', 'ibeju-community-2', 'guide', 'good', 'Helps mothers talk to daughters.'),
  ('epe-school:pads', 'epe-school', 'pads', 'good', 'Easy for girls who are new to periods.'),
  ('epe-school:liners', 'epe-school', 'liners', 'good', 'Help on light days at the start and end.'),
  ('epe-school:tampons', 'epe-school', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('epe-school:cup', 'epe-school', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('epe-school:pants', 'epe-school', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('epe-school:reusable', 'epe-school', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('epe-school:heat', 'epe-school', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('epe-school:wipes', 'epe-school', 'wipes', 'good', 'There is no running water to wash.'),
  ('epe-school:washbag', 'epe-school', 'washbag', 'good', 'Keeps things private in a busy school.'),
  ('epe-school:guide', 'epe-school', 'guide', 'good', 'Many girls are just starting: a guide answers their questions.'),
  ('epe-market:pads', 'epe-market', 'pads', 'good', 'Quick to change between customers.'),
  ('epe-market:liners', 'epe-market', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('epe-market:tampons', 'epe-market', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('epe-market:cup', 'epe-market', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('epe-market:pants', 'epe-market', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('epe-market:reusable', 'epe-market', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('epe-market:heat', 'epe-market', 'heat', 'good', 'Cramps are hard when you stand all day.'),
  ('epe-market:wipes', 'epe-market', 'wipes', 'good', 'Toilets are far and cost money.'),
  ('epe-market:washbag', 'epe-market', 'washbag', 'good', 'Keeps spares clean at the stall.'),
  ('epe-market:guide', 'epe-market', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('epe-busstop:pads', 'epe-busstop', 'pads', 'good', 'Simple to carry and change anywhere.'),
  ('epe-busstop:liners', 'epe-busstop', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('epe-busstop:tampons', 'epe-busstop', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('epe-busstop:cup', 'epe-busstop', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('epe-busstop:pants', 'epe-busstop', 'pants', 'poor', 'Pants need washing and drying, which is hard here.'),
  ('epe-busstop:reusable', 'epe-busstop', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('epe-busstop:heat', 'epe-busstop', 'heat', 'good', 'Cramps with nowhere to rest are hard.'),
  ('epe-busstop:wipes', 'epe-busstop', 'wipes', 'good', 'There is no steady water by the road.'),
  ('epe-busstop:washbag', 'epe-busstop', 'washbag', 'good', 'Keeps things clean and private on the move.'),
  ('epe-busstop:guide', 'epe-busstop', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('epe-clinic:pads', 'epe-clinic', 'pads', 'good', 'Heavy days need plenty of pads.'),
  ('epe-clinic:liners', 'epe-clinic', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('epe-clinic:tampons', 'epe-clinic', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('epe-clinic:cup', 'epe-clinic', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('epe-clinic:pants', 'epe-clinic', 'pants', 'good', 'Extra protection on heavy nights.'),
  ('epe-clinic:reusable', 'epe-clinic', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('epe-clinic:heat', 'epe-clinic', 'heat', 'good', 'Eases cramps while they wait.'),
  ('epe-clinic:wipes', 'epe-clinic', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('epe-clinic:washbag', 'epe-clinic', 'washbag', 'ok', 'Nice to have, but not the main need.'),
  ('epe-clinic:guide', 'epe-clinic', 'guide', 'good', 'Explains when pain needs a doctor.'),
  ('epe-youth:pads', 'epe-youth', 'pads', 'good', 'Ready for a surprise period.'),
  ('epe-youth:liners', 'epe-youth', 'liners', 'good', 'For light days at the club.'),
  ('epe-youth:tampons', 'epe-youth', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('epe-youth:cup', 'epe-youth', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('epe-youth:pants', 'epe-youth', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('epe-youth:reusable', 'epe-youth', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('epe-youth:heat', 'epe-youth', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('epe-youth:wipes', 'epe-youth', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('epe-youth:washbag', 'epe-youth', 'washbag', 'good', 'Keeps a spare in her school bag.'),
  ('epe-youth:guide', 'epe-youth', 'guide', 'good', 'Answers questions they have.'),
  ('epe-waterside:pads', 'epe-waterside', 'pads', 'ok', 'Useful, but not the most needed here.'),
  ('epe-waterside:liners', 'epe-waterside', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('epe-waterside:tampons', 'epe-waterside', 'tampons', 'poor', 'Hard to use without a private, clean place.'),
  ('epe-waterside:cup', 'epe-waterside', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('epe-waterside:pants', 'epe-waterside', 'pants', 'good', 'Long-lasting and washable.'),
  ('epe-waterside:reusable', 'epe-waterside', 'reusable', 'good', 'They last a year when the shop is far.'),
  ('epe-waterside:heat', 'epe-waterside', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('epe-waterside:wipes', 'epe-waterside', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('epe-waterside:washbag', 'epe-waterside', 'washbag', 'good', 'Keeps reusables clean.'),
  ('epe-waterside:guide', 'epe-waterside', 'guide', 'good', 'Shows how to wash and dry them safely.'),
  ('epe-hostel:pads', 'epe-hostel', 'pads', 'good', 'Simple for younger girls.'),
  ('epe-hostel:liners', 'epe-hostel', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('epe-hostel:tampons', 'epe-hostel', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('epe-hostel:cup', 'epe-hostel', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('epe-hostel:pants', 'epe-hostel', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('epe-hostel:reusable', 'epe-hostel', 'reusable', 'good', 'Water and a laundry line mean these last.'),
  ('epe-hostel:heat', 'epe-hostel', 'heat', 'ok', 'Nice for cramps, but not the main need.'),
  ('epe-hostel:wipes', 'epe-hostel', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('epe-hostel:washbag', 'epe-hostel', 'washbag', 'good', 'Privacy in shared rooms.'),
  ('epe-hostel:guide', 'epe-hostel', 'guide', 'good', 'Answers questions they are shy to ask.'),
  ('epe-football:pads', 'epe-football', 'pads', 'good', 'A simple backup for match day.'),
  ('epe-football:liners', 'epe-football', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('epe-football:tampons', 'epe-football', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('epe-football:cup', 'epe-football', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('epe-football:pants', 'epe-football', 'pants', 'good', 'Stay put while running.'),
  ('epe-football:reusable', 'epe-football', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('epe-football:heat', 'epe-football', 'heat', 'good', 'Helps cramps after training.'),
  ('epe-football:wipes', 'epe-football', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('epe-football:washbag', 'epe-football', 'washbag', 'good', 'Keeps kit separate in the changing room.'),
  ('epe-football:guide', 'epe-football', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('epe-training:pads', 'epe-training', 'pads', 'good', 'Easy to change on a short break.'),
  ('epe-training:liners', 'epe-training', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('epe-training:tampons', 'epe-training', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('epe-training:cup', 'epe-training', 'cup', 'ok', 'Lasts years, but needs boiling and privacy.'),
  ('epe-training:pants', 'epe-training', 'pants', 'good', 'Last through long sessions.'),
  ('epe-training:reusable', 'epe-training', 'reusable', 'ok', 'Lasts a year if there is water to wash them.'),
  ('epe-training:heat', 'epe-training', 'heat', 'good', 'Cramps make long hours harder.'),
  ('epe-training:wipes', 'epe-training', 'wipes', 'ok', 'Handy, but not the main need.'),
  ('epe-training:washbag', 'epe-training', 'washbag', 'good', 'Private storage at work.'),
  ('epe-training:guide', 'epe-training', 'guide', 'ok', 'Helpful, but not the main need here.'),
  ('epe-shelter:pads', 'epe-shelter', 'pads', 'good', 'Simple when water is shared.'),
  ('epe-shelter:liners', 'epe-shelter', 'liners', 'ok', 'Handy on light days, but not enough alone.'),
  ('epe-shelter:tampons', 'epe-shelter', 'tampons', 'ok', 'Fine for some older girls once someone shows them how.'),
  ('epe-shelter:cup', 'epe-shelter', 'cup', 'poor', 'A cup needs boiling and privacy, which is hard here.'),
  ('epe-shelter:pants', 'epe-shelter', 'pants', 'ok', 'Comfortable, but costly for a big group.'),
  ('epe-shelter:reusable', 'epe-shelter', 'reusable', 'poor', 'Without steady water, reusables are hard to keep clean.'),
  ('epe-shelter:heat', 'epe-shelter', 'heat', 'good', 'Comfort with nowhere to rest.'),
  ('epe-shelter:wipes', 'epe-shelter', 'wipes', 'good', 'Water points are crowded.'),
  ('epe-shelter:washbag', 'epe-shelter', 'washbag', 'good', 'Privacy in shared tents.'),
  ('epe-shelter:guide', 'epe-shelter', 'guide', 'ok', 'Helpful, but not the main need here.');

COMMIT;
