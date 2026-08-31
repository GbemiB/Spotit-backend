package com.spotit.api.content;

import com.spotit.api.content.entity.ContentItem;
import com.spotit.api.content.repository.ContentItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContentDataSeeder {

    private final ContentItemRepository contentItemRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void seed() {
        if (contentItemRepository.count() > 0) {
            boolean bodyMissing = contentItemRepository.findAll()
                    .stream().anyMatch(i -> i.getBody() == null || i.getBody().isBlank());
            if (!bodyMissing) return;
            contentItemRepository.deleteAll();
        }

        List<ContentItem> items = List.of(

                ContentItem.builder()
                        .tag("Health")
                        .title("Understanding Your Menstrual Cycle")
                        .body("""
                                Your menstrual cycle is far more than just your period. It is a monthly rhythm that touches every part of your physiology — your energy levels, skin, digestion, immune function, and even how you process emotions. Understanding it gives you a powerful lens for reading your body.
                                
                                **The Four Phases**
                                
                                The cycle begins on the first day of bleeding and is divided into four phases. During menstruation (days 1–5 on average), the uterine lining sheds because the previous cycle did not result in pregnancy. Oestrogen and progesterone are at their lowest, which is why many people feel tired, heavy, or low in mood during this time. This is your body's reset — rest and gentle nourishment are the most supportive things you can offer yourself.
                                
                                The follicular phase overlaps with menstruation and continues until ovulation (roughly days 1–13). The pituitary gland releases follicle-stimulating hormone (FSH), which prompts several follicles in the ovaries to grow. As these follicles develop, they produce rising levels of oestrogen. This hormone rebuilds the uterine lining and also sharpens mental clarity, boosts mood, and increases physical energy. Many people report feeling their most confident and social during this phase.
                                
                                Ovulation is a single event — typically around day 14 in a 28-day cycle, though it can range considerably. A surge of luteinising hormone (LH) triggers the dominant follicle to release a mature egg. This egg survives 12–24 hours. Oestrogen peaks just before ovulation and drops sharply immediately after. You may notice increased libido, glowing skin, and heightened verbal fluency around this time.
                                
                                The luteal phase (roughly days 15–28) follows ovulation. The ruptured follicle becomes the corpus luteum and secretes progesterone, which prepares the uterine lining for potential implantation. Progesterone has a calming, slightly sedating effect on the nervous system. As it rises, many people feel more inward-focused and in need of quiet. If the egg is not fertilised, the corpus luteum breaks down, progesterone and oestrogen drop sharply, and menstruation begins — starting the cycle again.
                                
                                **Why Cycle Length Varies**
                                
                                A "normal" cycle is anything between 21 and 35 days. What matters more than the absolute length is consistency. If your cycle has always been 32 days, that is your normal. What warrants attention is a sudden change of more than a week in either direction, or cycles that become irregular after being regular.
                                
                                Stress, illness, travel, significant weight change, intense exercise, and certain medications can all shift your cycle timing. The brain-ovary communication axis is sensitive to signals that the body interprets as unsafe for reproduction — your body is, above all, prioritising your survival.
                                
                                **Tracking as Self-Knowledge**
                                
                                The more you track — flow volume, energy, mood, sleep, skin, cravings, and pain — the clearer your personal pattern becomes. Over three to six months of data, you will likely be able to predict which days you will feel sharp and social, which days you will want solitude, and which days physical exertion will feel effortless versus draining. That is a significant life advantage.""")
                        .sponsored(false)
                        .sortOrder(1)
                        .build(),

                ContentItem.builder()
                        .tag("Nutrition")
                        .title("Foods That Ease Period Cramps")
                        .body("""
                                Dysmenorrhoea — the clinical term for painful periods — affects up to 80% of people who menstruate at some point in their lives. For many, it is the single most disruptive symptom each cycle. While over-the-counter pain relief helps in acute moments, what you eat in the days leading up to and during your period can meaningfully reduce the intensity of cramping.
                                
                                **The Prostaglandin Connection**
                                
                                Period pain is primarily driven by prostaglandins — hormone-like compounds produced in the uterine lining that trigger muscle contractions to help shed that lining. People who experience more severe cramps tend to produce higher levels of certain prostaglandins. Diet can influence prostaglandin production in two important ways: by reducing inflammation and by altering the ratio of omega-6 to omega-3 fatty acids in your cells.
                                
                                **Anti-Inflammatory Foods to Prioritise**
                                
                                Fatty fish — salmon, mackerel, sardines, and herring — are rich in EPA and DHA, the omega-3 fatty acids that dampen inflammatory prostaglandin production. Aim for two or more servings per week throughout your cycle, not just during your period. Walnuts, flaxseeds, and chia seeds offer plant-based omega-3s (ALA) that the body can partially convert.
                                
                                Ginger has well-documented anti-inflammatory and antispasmodic properties. Several clinical studies have found that ginger supplementation reduces period pain comparably to ibuprofen. Fresh ginger in hot water with lemon and honey, or grated into stir-fries and smoothies, is an easy daily habit.
                                
                                Turmeric contains curcumin, a powerful anti-inflammatory compound. It is best absorbed with black pepper (which contains piperine, dramatically increasing curcumin bioavailability). Add both to golden milk, curries, or scrambled eggs.
                                
                                Leafy greens — spinach, kale, Swiss chard, and broccoli — provide magnesium and calcium, both of which relax smooth muscle. Magnesium deficiency is associated with more severe cramping, and many people who menstruate are mildly deficient in it.
                                
                                **Magnesium: The Cramp-Calming Mineral**
                                
                                Magnesium is the most important nutrient for period pain. It relaxes the uterine muscle, inhibits prostaglandin production, and also eases the tension headaches and lower back pain that often accompany menstruation. Dark chocolate (70%+ cacao), pumpkin seeds, almonds, black beans, avocado, and banana are all excellent sources. A magnesium glycinate or citrate supplement (200–400 mg daily) is worth discussing with your doctor if dietary sources are insufficient.
                                
                                **Foods to Limit**
                                
                                Refined sugar and processed carbohydrates spike insulin and promote inflammation — the opposite of what you want premenstrually. Alcohol increases oestrogen levels and disrupts sleep, worsening PMS symptoms. Excess salt causes fluid retention and bloating. Caffeine constricts blood vessels and can intensify cramping; consider switching to herbal teas in the week before your period.
                                
                                **Hydration**
                                
                                Being well-hydrated reduces bloating, eases constipation (common during menstruation due to prostaglandins acting on the intestines), and helps transport nutrients to muscles. Aim for at least 2 litres of water daily. Warm or hot liquids are particularly soothing — they relax the abdomen and improve local circulation.""")
                        .sponsored(false)
                        .sortOrder(2)
                        .build(),

                ContentItem.builder()
                        .tag("Fitness")
                        .title("How to Exercise Through Your Entire Cycle")
                        .body("""
                                The idea that you should rest completely during your period, or push equally hard every day of the month, are both outdated. Your hormones create a changing internal environment that makes different types of movement feel better — and perform better — at different points in your cycle. Working with this rhythm rather than against it is one of the most practical applications of cycle awareness.
                                
                                **Menstruation (Days 1–5): Gentle and Restorative**
                                
                                During your period, oestrogen and progesterone are both at their lowest. Energy is naturally reduced and the body is doing significant work. This is not a phase to push through high-intensity training — doing so elevates cortisol, which can worsen cramps, disrupt sleep, and prolong recovery. Instead, this is ideal for:
                                
                                Yoga (particularly yin or restorative styles), slow walks in natural light, gentle swimming, and light stretching. Movement at this level still improves circulation to the pelvis, which actively reduces cramping and eases the sluggishness that comes with low hormone levels. Even 20 minutes of slow walking has measurable effects on mood during menstruation.
                                
                                **Follicular Phase (Days 6–13): Build and Explore**
                                
                                Rising oestrogen drives increasing energy, improved coordination, faster muscle repair, and a higher pain threshold. Your body is primed for effort. This is the phase to:
                                
                                Try new workouts, increase weights or distances, attend that harder class, or begin a new training programme. Strength gains are facilitated by oestrogen's anabolic effects. Cardio endurance also tends to feel more accessible. Recovery is faster, so you can train on consecutive days more comfortably.
                                
                                **Ovulation (Around Day 14): Peak Performance**
                                
                                Oestrogen peaks around ovulation, and a brief surge in testosterone adds to strength and motivation. Many athletes report their best performances mid-cycle. High-intensity interval training (HIIT), heavy lifting, competitive sport, or long runs all suit this window well.
                                
                                One important note: the ligament-relaxing effects of peak oestrogen mean your joints — particularly the knees and ankles — are slightly more mobile and therefore slightly more vulnerable to injury. Prioritise proper warm-up, controlled form, and adequate footwear.
                                
                                **Luteal Phase (Days 15–28): Steady and Sustained**
                                
                                Progesterone rises after ovulation and has a stabilising but somewhat fatiguing effect. Core temperature is slightly elevated, which increases perceived exertion during cardio. This is not the phase for personal records or pushing to new limits. It is, however, excellent for:
                                
                                Strength training at moderate intensity, Pilates, hiking, barre, cycling, and swimming at a comfortable pace. The body remains metabolically active (basal metabolic rate increases slightly in the luteal phase), so you may find cravings increase — this is physiological, not a failure of willpower.
                                
                                In the final days before your period, when PMS symptoms peak, ease back further. Gentle yoga, stretching, and walks maintain the benefits of movement without adding physical stress.
                                
                                **Tracking Your Fitness Alongside Your Cycle**
                                
                                Log how your workouts feel — energy before, effort during, recovery after — alongside your cycle data. Within two or three cycles, you will likely see clear patterns emerging. Use that data to plan your training calendar deliberately, scheduling your hardest sessions in the follicular and ovulatory phases and active recovery in the luteal phase and menstruation.""")
                        .sponsored(false)
                        .sortOrder(3)
                        .build(),

                ContentItem.builder()
                        .tag("Wellness")
                        .title("Managing Period Cramps Naturally")
                        .body("""
                                For many people, period cramps are the most physically challenging aspect of their cycle. The good news is that a range of well-evidenced, non-pharmaceutical approaches can significantly reduce their severity — and in some cases, work as effectively as over-the-counter medication.
                                
                                **Heat Therapy**
                                
                                Heat is consistently the most recommended and most effective immediate remedy for primary dysmenorrhoea. Applying a heat pad, warm wheat bag, or hot water bottle to the lower abdomen at 38–40°C relaxes the smooth muscle of the uterus, increases local blood flow, and interrupts pain signals through a mechanism similar to analgesic medication. Research published in Evidence-Based Nursing found continuous low-level topical heat as effective as ibuprofen for pain relief.
                                
                                Apply heat for 20–30 minutes at a time. A warm bath or shower works equally well and has the added benefit of easing lower back pain and promoting relaxation throughout the body.
                                
                                **Omega-3 Supplementation**
                                
                                Several randomised controlled trials have demonstrated that fish oil supplementation reduces prostaglandin-driven uterine contractions. Studies used doses ranging from 1,000 mg to 2,000 mg of EPA+DHA daily, started 1–2 weeks before menstruation. The effect builds over two to three cycles of consistent supplementation.
                                
                                **Magnesium**
                                
                                Magnesium glycinate or citrate at 300–400 mg per day has been shown to reduce the severity and duration of menstrual cramps. It works by relaxing smooth muscle, inhibiting prostaglandin synthesis, and reducing inflammation. Starting supplementation in the second half of your cycle (the luteal phase) is particularly effective.
                                
                                **TENS Therapy**
                                
                                Transcutaneous electrical nerve stimulation (TENS) devices, now widely available as small, wearable units, deliver low-level electrical pulses that interfere with pain signals travelling to the brain. High-frequency TENS applied to the lower abdomen has been shown in multiple studies to be significantly more effective than placebo for period pain. These devices are drug-free and reusable.
                                
                                **Herbal Teas**
                                
                                Raspberry leaf tea is a traditional remedy with some scientific backing — it contains fragarine, a compound thought to tone uterine muscle and reduce excessive contractions. Chamomile tea contains glycine, which relieves muscle spasms and acts as a mild nerve relaxant. Ginger tea has anti-prostaglandin and anti-nausea properties. Drink 2–3 cups daily starting 2 days before expected menstruation.
                                
                                **Movement and Acupressure**
                                
                                Gentle yoga postures — child's pose, reclined bound angle, supine twist — open the pelvis, release tension in the lower back and hips, and improve circulation to the uterus. Even 15 minutes of light movement when cramps are mild can interrupt the pain cycle meaningfully.
                                
                                Acupressure at the Spleen 6 point (located four finger-widths above the inner ankle bone) is used in traditional Chinese medicine for menstrual pain. Several small studies support its effectiveness for reducing cramping intensity when firm pressure is applied for 1–3 minutes.
                                
                                **When to See a Doctor**
                                
                                Natural remedies work well for primary dysmenorrhoea (cramps without underlying disease). If your pain is severe enough to prevent you from functioning, gets progressively worse over time, or does not respond to any of the above, it is important to discuss secondary dysmenorrhoea with a healthcare provider — conditions like endometriosis, fibroids, or adenomyosis may be contributing.""")
                        .sponsored(false)
                        .sortOrder(4)
                        .build(),

                ContentItem.builder()
                        .tag("Mindfulness")
                        .title("Your Mood and Your Menstrual Cycle")
                        .body("""
                                The connection between hormones and mental wellbeing is real, complex, and deeply individual. For many people who menstruate, mood does not stay constant across the month — it shifts in ways that, once recognised, can be anticipated, understood, and supported rather than experienced as mysterious or overwhelming.
                                
                                **The Hormonal Drivers**
                                
                                Oestrogen has a significant positive effect on mood. It increases serotonin (the "wellbeing" neurotransmitter), boosts dopamine sensitivity (reward and motivation), and reduces cortisol reactivity (the stress response). When oestrogen is rising — during the follicular phase — most people feel more positive, motivated, socially engaged, and emotionally resilient.
                                
                                At ovulation, a brief testosterone surge adds confidence, assertiveness, and drive. This mid-cycle window is often when people feel most outgoing and capable.
                                
                                Progesterone, which dominates the luteal phase, has a more complex effect. It activates GABA receptors in the brain, which is sedating and calming — but this same mechanism can produce feelings of flatness, fatigue, or low motivation in some people. Progesterone also reduces the brain's sensitivity to positive stimuli, which is why things that would normally feel rewarding (social interaction, food, entertainment) can feel muted in the late luteal phase.
                                
                                In the final days before menstruation, both oestrogen and progesterone fall sharply. This drop — particularly in oestrogen — is linked to the serotonin withdrawal that underpins PMS symptoms: irritability, sadness, anxiety, difficulty concentrating, and emotional sensitivity. For people with PMDD (premenstrual dysphoric disorder), this hormonal shift triggers clinically significant mood changes that deserve medical support.
                                
                                **The Inner Seasons Framework**
                                
                                Many practitioners use a seasonal metaphor to describe the emotional quality of each phase:
                                
                                - **Menstruation (Winter):** A time for reflection, rest, and turning inward. Intuition tends to be sharp; the desire for solitude is natural. This is not a flaw — it is a biological invitation to slow down.
                                - **Follicular Phase (Spring):** Energy returns, creativity opens, optimism rises. Excellent for starting projects, socialising, and setting intentions.
                                - **Ovulation (Summer):** Outward focus, communication, warmth. The most extroverted phase for many people.
                                - **Luteal Phase (Autumn):** Productivity narrows, detail-orientation sharpens, but tolerance for excess stimulation decreases. This is the time to complete rather than begin.
                                
                                **Practical Mood Support**
                                
                                Journaling is one of the most evidence-backed mood tools across all phases. Writing about what you feel — without judgement — helps regulate the emotional brain and creates distance from reactive thoughts.
                                
                                Reducing caffeine and alcohol in the late luteal phase prevents additional cortisol spikes and sleep disruption. Both amplify the mood changes already driven by hormonal decline.
                                
                                Regular aerobic exercise increases endorphins and serotonin, offering real protection against PMS-related mood changes. Even 30 minutes of walking five days a week has measurable antidepressant effects.
                                
                                Social connection during the follicular and ovulatory phases, and intentional solitude during the luteal phase, helps many people feel more in alignment with their natural needs rather than fighting against them.
                                
                                Finally, tracking your mood alongside your cycle for three to four months is transformative. When you can look back and see that your difficult days always land in days 26–28, they become predictable rather than mysterious — and predictable is manageable.""")
                        .sponsored(false)
                        .sortOrder(5)
                        .build(),

                ContentItem.builder()
                        .tag("Health")
                        .title("Signs of a Healthy Menstrual Cycle")
                        .body("""
                                There is a great deal of variation in what a normal cycle looks like — and a great deal of misinformation about what should be considered standard. Understanding what genuinely healthy menstruation looks like helps you recognise when something deserves attention and when it falls within the wide range of normal.
                                
                                **Cycle Length**
                                
                                A healthy cycle is typically 21 to 35 days long, measured from the first day of one period to the first day of the next. The commonly cited "28-day cycle" is an average, not a standard — many people naturally cycle on 26, 30, or 34-day patterns. What matters is consistency. A cycle that varies by more than 7–9 days from month to month is considered irregular and worth monitoring.
                                
                                **Period Duration**
                                
                                Bleeding lasting 2 to 7 days is within the normal range. Some people bleed for just 2 days and others for a full week — both can be healthy. What is worth noting is a significant change from your personal baseline: periods that were previously 4 days becoming 7, or vice versa.
                                
                                **Flow Volume**
                                
                                The average blood loss per period is 30–80 ml — roughly 2 to 6 tablespoons. In practice, this is difficult to measure, so the more useful markers are:
                                
                                - Needing to change a pad or tampon every hour or more frequently for several consecutive hours: this is considered heavy bleeding and deserves medical evaluation.
                                - Clots smaller than a 50p coin are common and normal. Larger, frequent clots may indicate a heavier flow that warrants discussion.
                                - Spotting between periods occasionally (especially at ovulation) is normal; consistent mid-cycle bleeding is worth investigating.
                                
                                **Pain**
                                
                                Mild to moderate cramping — typically felt in the lower abdomen and sometimes radiating to the lower back and thighs — is normal during menstruation. It is caused by prostaglandins contracting the uterus to shed its lining. The pain should be manageable with over-the-counter remedies and should not stop you from your daily activities.
                                
                                Pain that is severe enough to prevent you from working, socialising, or exercising; pain that worsens year on year; pain during sex; or significant pain outside of menstruation — these are not "just bad periods." They are reasons to seek a medical opinion. Endometriosis, for example, affects roughly 1 in 10 people who menstruate and is often dismissed for years.
                                
                                **Premenstrual Symptoms**
                                
                                Mild breast tenderness, slight bloating, increased food cravings, and minor mood shifts in the week before your period are common and normal. They result from the drop in oestrogen and rise in progesterone. When these symptoms are severe enough to impair daily function, they may indicate PMDD, which is a recognised medical condition with effective treatments.
                                
                                **Colour and Texture**
                                
                                Menstrual blood ranges from bright red to dark brown or almost black, and from thin and watery to slightly thick. Colour and texture change across the period — darker blood at the beginning or end simply reflects older blood that took longer to exit. Greyish tissue or an unusually foul odour should be evaluated by a doctor.
                                
                                **The Value of Tracking**
                                
                                You cannot know what your normal is without data. Tracking your cycle for at least three months builds your personal baseline — which is far more useful than any general average when it comes to identifying meaningful changes.""")
                        .sponsored(false)
                        .sortOrder(6)
                        .build(),

                ContentItem.builder()
                        .tag("Tips")
                        .title("Why Tracking Your Symptoms Changes Everything")
                        .body("""
                                Symptom tracking is one of the most underrated health practices available. When done consistently, it transforms vague feelings into clear patterns, converts frustrating surprises into predictable events, and gives both you and your healthcare providers data that would otherwise take years of guesswork to accumulate.
                                
                                **What You Can Track**
                                
                                The most informative data points are:
                                
                                - **Flow:** Volume (light, moderate, heavy), colour (bright red, dark, brown), consistency (thin, thick, clotted), and duration.
                                - **Pain:** Location (lower abdomen, back, hips, legs), severity (rate 1–10), and what helps.
                                - **Mood:** Emotional state throughout the day — not just the extremes. Irritability, sadness, anxiety, contentment, motivation, and flatness are all worth noting.
                                - **Energy:** Physical energy for exercise and daily tasks, and mental energy for focus and concentration.
                                - **Sleep:** How easily you fell asleep, whether you woke during the night, and how rested you felt.
                                - **Skin:** Breakouts, oiliness, dryness, and sensitivity. Skin often tracks hormonal shifts closely.
                                - **Digestion:** Bloating, constipation, diarrhoea, and nausea — all of which are influenced by prostaglandins and hormonal changes.
                                - **Libido:** Fluctuations in sexual desire across the cycle are normal and informative.
                                - **Headaches:** Migraine-prone people often find a clear hormonal trigger pattern within a few cycles of tracking.
                                - **Cervical mucus:** Changes in consistency and volume (dry, creamy, stretchy/egg-white) directly signal where you are in your cycle.
                                
                                **Why Three Cycles Is the Minimum**
                                
                                One cycle of data shows you a single data point. Two cycles let you begin to see a pattern. Three to four cycles reveal your genuine baseline — accounting for the natural variation that stress, travel, illness, and lifestyle changes introduce. Once you have three or more cycles tracked, patterns become unmistakable.
                                
                                **How It Helps Medical Appointments**
                                
                                When a doctor asks how long your periods have been heavy, how often you get headaches, or whether your pain has changed over time, the honest answer for most people is "I'm not sure." Tracked data changes that completely. You can show a doctor exactly when your symptoms occur, how severe they are, how they relate to your cycle, and how they have changed over time. This accelerates diagnosis dramatically.
                                
                                For conditions like endometriosis, PCOS, thyroid dysfunction, and fibroids, tracked symptom data is often the first concrete evidence that something beyond normal variation is happening. People with endometriosis wait an average of 7–10 years for diagnosis — consistent symptom tracking is one of the most powerful ways to shorten that gap.
                                
                                **Anticipation as Empowerment**
                                
                                Perhaps the most immediate benefit of tracking is the shift from being blindsided by your body to anticipating what is coming. Knowing that you tend to feel low on days 26–28, or that your energy peaks around days 10–14, allows you to plan around these patterns — scheduling important meetings, social events, or challenging tasks in your high-energy window, and protecting your rest days without guilt.
                                
                                This is not about controlling your body. It is about understanding it well enough to work with it.""")
                        .sponsored(false)
                        .sortOrder(7)
                        .build(),

                ContentItem.builder()
                        .tag("Wellness")
                        .title("Sleep and Your Menstrual Cycle")
                        .body("""
                                Sleep and the menstrual cycle are locked in a bidirectional relationship: hormonal changes across your cycle affect sleep quality, and poor sleep in turn disrupts hormonal balance, worsens PMS, and can even alter cycle length. Understanding this relationship helps you protect your sleep in the phases when it is most vulnerable.
                                
                                **How Each Phase Affects Sleep**
                                
                                During menstruation, the drop in oestrogen and progesterone can cause lighter, more fragmented sleep. Cramping, back pain, and the need to change protection during the night are obvious disruptors. Many people also experience night sweats in the first day or two of menstruation, driven by the sharp hormonal drop affecting the temperature regulation centre of the brain.
                                
                                The follicular phase typically brings improved sleep. Rising oestrogen promotes deeper, more restorative slow-wave sleep. Energy during the day is higher and falling asleep is easier. Many people report their best sleep during the days following menstruation and leading up to ovulation.
                                
                                After ovulation, progesterone rises. This hormone has a mild sedating effect via GABA receptors and can make you feel sleepier in the afternoon. However, it also raises core body temperature by around 0.3–0.5°C, which counterintuitively makes sleep lighter — the body needs to cool down to enter deep sleep, and elevated temperature works against this. You may find yourself waking more easily during the luteal phase.
                                
                                In the late luteal phase — the week before your period — progesterone drops, PMS symptoms peak, and sleep is often at its worst. Cortisol tends to be higher, making it harder to fall asleep and easier to wake during the night. Anxiety, racing thoughts, and physical discomfort all contribute.
                                
                                **Practical Sleep Support by Phase**
                                
                                During menstruation, a warm bath before bed reduces cramping and promotes the drop in core temperature needed for sleep onset. A low, dark room with breathable bedding helps with night sweats. A heat pad worn during sleep provides pain relief without medication.
                                
                                In the luteal phase, where elevated temperature is the enemy of deep sleep, sleeping in a cool room (16–19°C) is particularly important. Avoid alcohol — which increases body temperature and suppresses REM sleep — and reduce caffeine after noon. Chamomile or valerian tea in the evening can ease the nervous system.
                                
                                **Sleep and PMS**
                                
                                Poor sleep dramatically worsens PMS symptoms. Studies consistently show that people sleeping fewer than 7 hours experience significantly more severe irritability, anxiety, and physical symptoms premenstrually. This creates a painful cycle: PMS disrupts sleep, and poor sleep worsens PMS. Breaking this cycle requires protecting sleep hygiene specifically in the 7–10 days before your period.
                                
                                **Consistent Sleep Timing**
                                
                                The single most effective sleep intervention across all phases is keeping a consistent sleep and wake time — including on weekends. Your body's circadian rhythm operates like an anchor; the more consistent it is, the more effectively it coordinates with your hormonal rhythms. Even during the follicular phase when sleep feels effortless, maintaining your schedule protects you in the phases when it is harder.
                                
                                **Tracking Sleep With Your Cycle**
                                
                                When you track sleep quality alongside your cycle, the patterns become clear within two or three cycles. Most people are surprised by how predictable their difficult sleep nights are — and how much easier it is to manage them once they are expected rather than random.""")
                        .sponsored(false)
                        .sortOrder(8)
                        .build(),

                ContentItem.builder()
                        .tag("Health")
                        .title("What Is Ovulation and How to Recognise It")
                        .body("""
                                Ovulation is the central event of the menstrual cycle. Everything before it — the follicular phase — builds toward it, and everything after it — the luteal phase — is a response to it. Yet despite its importance, most people receive very little education about what ovulation actually is, when it happens, or how to recognise it. Understanding ovulation gives you detailed knowledge of your fertility, your hormones, and your health.
                                
                                **What Actually Happens**
                                
                                During the follicular phase, follicle-stimulating hormone (FSH) stimulates multiple follicles in the ovaries to begin developing. Each follicle contains an immature egg. As they grow, the follicles produce oestrogen. Eventually, one follicle becomes dominant — it grows largest and produces the most oestrogen.
                                
                                When oestrogen reaches a threshold level, it triggers a surge of luteinising hormone (LH) from the pituitary gland. This LH surge is the signal that causes the dominant follicle to rupture and release the mature egg — this is ovulation.
                                
                                The released egg travels down the fallopian tube toward the uterus. It is viable for fertilisation for only 12 to 24 hours. However, sperm can survive in the female reproductive tract for up to 5 days, which is why the fertile window extends for 5–6 days ending at ovulation.
                                
                                **When Does Ovulation Happen?**
                                
                                The common understanding is "day 14," based on an average 28-day cycle. In reality, ovulation timing varies significantly between people and even from cycle to cycle for the same person. What is consistent is that the luteal phase — the time from ovulation to the next period — is typically 12 to 16 days and is relatively stable. Variability in cycle length mostly comes from variability in the follicular phase, not the luteal phase.
                                
                                If your period is consistently irregular, your ovulation timing is likely variable too. Stress, illness, significant changes in exercise or diet, and hormonal conditions like PCOS can delay or suppress ovulation entirely — a condition called anovulation.
                                
                                **Physical Signs of Ovulation**
                                
                                Several observable signs indicate ovulation is approaching or has occurred:
                                
                                **Cervical mucus changes** are the most reliable physical sign. As oestrogen rises toward ovulation, cervical mucus changes from dry or absent (just after menstruation), to white and creamy, to clear, slippery, and stretchy — often described as raw egg-white consistency. This fertile-quality mucus helps sperm survive and travel. After ovulation, under progesterone's influence, mucus becomes thick, cloudy, and less abundant.
                                
                                **Basal body temperature (BBT)** rises by 0.2–0.5°C after ovulation and stays elevated until your next period. This rise is driven by progesterone from the corpus luteum. Tracking BBT with a sensitive thermometer each morning before getting out of bed reveals your ovulation pattern over time — though it confirms ovulation after the fact rather than predicting it.
                                
                                **LH surge on ovulation test strips** — available inexpensively at pharmacies — detect the LH surge in urine 24–36 hours before ovulation. This gives advance notice of your most fertile time.
                                
                                **Mittelschmerz** is a one-sided, mild ache in the lower abdomen that roughly half of ovulating people experience around ovulation. It is caused by the follicle swelling and rupturing, and sometimes by the small amount of fluid or blood that spills into the pelvis. It typically lasts a few minutes to a few hours.
                                
                                **Ovulation and Overall Health**
                                
                                Regular, consistent ovulation is one of the clearest signs that your endocrine system is functioning well. Cycles in which ovulation does not occur — anovulatory cycles — produce oestrogen but no progesterone, which over time has effects on bone density, cardiovascular health, and mood. This is why regular periods matter for more than just fertility: they signal a healthy hormonal environment.""")
                        .sponsored(false)
                        .sortOrder(9)
                        .build(),

                ContentItem.builder()
                        .tag("Nutrition")
                        .title("Iron-Rich Eating During and After Your Period")
                        .body("""
                                Iron deficiency is the most common nutritional deficiency worldwide, and people who menstruate are at significantly higher risk than the general population. Each period results in blood loss, and with that blood goes iron — a mineral essential for producing haemoglobin, the protein that carries oxygen in red blood cells. When iron stores are depleted, the result is fatigue, poor concentration, breathlessness, and a general feeling of depletion that can persist well beyond your period itself.
                                
                                **How Much Iron Do You Actually Lose?**
                                
                                The average menstrual blood loss of 30–80 ml contains roughly 12–35 mg of iron per period. Iron requirements for people who menstruate are accordingly higher: 18 mg per day (compared to 8 mg for adult men). During particularly heavy periods, iron losses can be double this — making it nearly impossible to replenish through diet alone without deliberate planning.
                                
                                Symptoms of iron deficiency that are often dismissed as simply "period fatigue" include: persistent tiredness in the week following your period, difficulty concentrating, cold hands and feet, paleness, shortness of breath during light activity, heart palpitations, brittle nails, hair thinning, and unusual cravings for ice or non-food substances (pica). If you experience several of these symptoms regularly, ask your doctor for a full blood count and ferritin test.
                                
                                **Haem vs Non-Haem Iron**
                                
                                There are two forms of dietary iron. Haem iron, found only in animal products, is highly bioavailable — roughly 15–35% of it is absorbed. Non-haem iron, found in plant foods, is less bioavailable (2–20% absorption) but is still a valuable source when consumed consistently and alongside absorption-enhancing factors.
                                
                                **Best Haem Iron Sources**
                                
                                Red meat — particularly beef and lamb — is the most concentrated source of haem iron. Liver is extraordinarily iron-rich (a 100g serving of beef liver provides approximately 6.5 mg of iron) but should be limited to once a week due to its high vitamin A content. Shellfish, especially oysters, clams, and mussels, are excellent. Sardines, tuna, and dark poultry meat provide moderate amounts.
                                
                                **Best Non-Haem Iron Sources**
                                
                                Cooked lentils and chickpeas provide 3–4 mg of iron per cup. Tofu and tempeh are good plant-based sources. Pumpkin seeds (4.2 mg per 30g), hemp seeds, and sesame seeds are highly concentrated. Spinach and other dark leafy greens contain iron but also contain oxalates that reduce absorption — cooking them reduces oxalate levels significantly. Fortified cereals and wholegrain bread contribute meaningfully to daily totals.
                                
                                **Maximising Absorption**
                                
                                Vitamin C dramatically increases non-haem iron absorption — by as much as three to six times. Add a squeeze of lemon to lentil soup, eat bell peppers alongside plant-based meals, or start your day with a glass of orange juice alongside your iron-rich breakfast.
                                
                                Conversely, certain compounds inhibit iron absorption and should not be consumed at the same time as iron-rich foods. Calcium (dairy, calcium-fortified plant milks) competes directly with iron for absorption. Tannins in tea and coffee bind iron and significantly reduce uptake — wait at least one hour after an iron-rich meal before drinking either. Phytates in wholegrains and legumes can be partially neutralised by soaking, sprouting, or fermenting these foods.
                                
                                **When Supplementation Makes Sense**
                                
                                If you have confirmed iron deficiency or deficiency anaemia, dietary changes alone are unlikely to restore your levels quickly enough. Iron supplements (ferrous sulphate or ferrous bisglycinate — the latter is gentler on the stomach) taken on alternate days have been shown in research to achieve similar or better absorption than daily dosing, with fewer side effects. Always take supplements with food and vitamin C, and away from calcium. Constipation is a common side effect; ferrous bisglycinate and liquid iron formulations tend to be better tolerated.""")
                        .sponsored(false)
                        .sortOrder(10)
                        .build()
        );

        contentItemRepository.saveAll(items);
        log.info("Content feed seeded with {} items", items.size());
    }
}
