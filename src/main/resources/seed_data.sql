-- ============================================================
-- Purohit Darpan - Seed Data
-- Festivals 2025-2026, Sample Pujas, Mantras, Samagri
-- ============================================================

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- -------------------------------------------------------
-- ADMIN USER
-- password: Admin@123 (BCrypt hash)
-- -------------------------------------------------------
INSERT IGNORE INTO users (username, email, password_hash, full_name, role, is_active)
VALUES
('admin', 'admin@purohitdarpan.com',
 '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMlLLIezHSmVBYGVa5RkBHG0q2',
 'Platform Admin', 'ADMIN', TRUE),
('guru_sharma', 'guru@purohitdarpan.com',
 '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMlLLIezHSmVBYGVa5RkBHG0q2',
 'Pt. Rajesh Sharma', 'MENTOR', TRUE),
('arjun_student', 'arjun@example.com',
 '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMlLLIezHSmVBYGVa5RkBHG0q2',
 'Arjun Mishra', 'STUDENT', TRUE);

-- -------------------------------------------------------
-- SAMAGRI (ritual items)
-- -------------------------------------------------------
INSERT IGNORE INTO samagri (name, name_devanagari, description) VALUES
('Kalash', 'कलश', 'Sacred water pot, symbolizes deity residence'),
('Mango Leaves', 'आम के पत्ते', 'Fresh mango leaves placed on Kalash'),
('Coconut', 'नारियल', 'Placed on top of Kalash, symbolizes Lord Shiva'),
('Roli (Kumkum)', 'रोली / कुमकुम', 'Red vermillion powder for tilak'),
('Akshat (Rice)', 'अक्षत', 'Unbroken rice grains offered during puja'),
('Incense Sticks (Agarbatti)', 'अगरबत्ती', 'Used to purify the atmosphere'),
('Ghee Diya', 'घी दीया', 'Earthen lamp with pure ghee wick'),
('Flowers (Pushpa)', 'पुष्प', 'Fresh flowers for deity offering'),
('Dhoti (White Cloth)', 'धोती', 'White cotton cloth for pandit to wear'),
('Paan Patta', 'पान पत्ता', 'Betel leaf for offering'),
('Supari', 'सुपारी', 'Areca nut, auspicious offering'),
('Camphor (Kapoor)', 'कपूर', 'Used in aarti for illumination'),
('Gangajal', 'गंगाजल', 'Holy water from River Ganga'),
('Chandan (Sandalwood)', 'चंदन', 'Sandalwood paste for tilak and fragrance'),
('Turmeric (Haldi)', 'हल्दी', 'Auspicious yellow powder'),
('Panchamrit', 'पंचामृत', 'Mixture of milk, curd, honey, sugar, ghee'),
('Tulsi Leaves', 'तुलसी पत्ते', 'Sacred basil leaves, especially for Vishnu puja'),
('Lotus Flower', 'कमल', 'Sacred flower of Lakshmi and Vishnu'),
('Sindoor', 'सिंदूर', 'Vermillion for married women during puja'),
('Dhoop', 'धूप', 'Resin incense for purification');

-- -------------------------------------------------------
-- PUJAS
-- -------------------------------------------------------
INSERT IGNORE INTO pujas (name, name_devanagari, description, duration_minutes, difficulty, category, thumbnail_url, created_by) VALUES
('Ganesh Puja', 'गणेश पूजा',
 'The foundational puja performed at the beginning of every ritual. Lord Ganesha is the remover of obstacles and is invoked first to bless any endeavor.',
 60, 'BEGINNER', 'Vighnaharta', '/images/ganesh-puja.jpg', 1),

('Lakshmi Puja', 'लक्ष्मी पूजा',
 'Worship of Goddess Lakshmi, the deity of wealth and prosperity. Performed especially on Diwali and Fridays.',
 90, 'BEGINNER', 'Wealth', '/images/lakshmi-puja.jpg', 1),

('Satyanarayan Katha', 'सत्यनारायण कथा',
 'A complete Vishnu puja with the reading of Satyanarayan Vrat Katha. Performed on Purnima and for special occasions.',
 180, 'INTERMEDIATE', 'Vrat', '/images/satyanarayan.jpg', 1),

('Rudrabhishek', 'रुद्राभिषेक',
 'Sacred ritual bathing of Shiva Linga with Panchamrit and Gangajal while chanting the Sri Rudram.',
 120, 'ADVANCED', 'Shaivite', '/images/rudrabhishek.jpg', 1),

('Navratri Devi Puja', 'नवरात्रि देवी पूजा',
 'Nine-day worship of the nine forms of Goddess Durga during Navratri. Includes Durga Saptashati recitation.',
 150, 'INTERMEDIATE', 'Shakti', '/images/navratri.jpg', 1),

('Griha Pravesh', 'गृह प्रवेश',
 'Housewarming ceremony invoking Vastu Purusha and Panchdeva for blessing the new home.',
 240, 'ADVANCED', 'Samskara', '/images/griha-pravesh.jpg', 1);

-- -------------------------------------------------------
-- MANTRAS (Devanagari)
-- -------------------------------------------------------
INSERT IGNORE INTO mantras (shlok_text, transliteration, word_meanings, source_text) VALUES

-- Ganesh Vandana
('वक्रतुण्ड महाकाय सूर्यकोटि समप्रभ।\nनिर्विघ्नं कुरु मे देव सर्वकार्येषु सर्वदा॥',
 'Vakratunda Mahakaya Suryakoti Samaprabha\nNirvighnam Kuru Me Deva Sarvakaryeshu Sarvada',
 '[{"word":"वक्रतुण्ड","meaning":"One with curved trunk","role":"Nominative"},{"word":"महाकाय","meaning":"Mighty-bodied","role":"Nominative"},{"word":"सूर्यकोटि","meaning":"Crores of suns","role":"Nominative"},{"word":"समप्रभ","meaning":"Equal in radiance","role":"Nominative"},{"word":"निर्विघ्नम्","meaning":"Without obstacles","role":"Accusative"},{"word":"कुरु","meaning":"Make / grant","role":"Verb, Imperative"},{"word":"सर्वकार्येषु","meaning":"In all works/tasks","role":"Locative plural"},{"word":"सर्वदा","meaning":"Always","role":"Indeclinable"}]',
 'Traditional Ganesh Vandana'),

-- Lakshmi Mantra
('ॐ श्रीं ह्रीं श्रीं कमले कमलालये प्रसीद प्रसीद।\nश्रीं ह्रीं श्रीं ॐ महालक्ष्म्यै नमः॥',
 'Om Shrim Hrim Shrim Kamale Kamalaalaye Prasida Prasida\nShrim Hrim Shrim Om Mahalakshmyai Namah',
 '[{"word":"ॐ","meaning":"Primordial sacred syllable","role":"Invocation"},{"word":"श्रीं","meaning":"Bija mantra of Lakshmi","role":"Bija"},{"word":"ह्रीं","meaning":"Bija of Maya/Shakti","role":"Bija"},{"word":"कमले","meaning":"O Lotus-seated one","role":"Vocative"},{"word":"प्रसीद","meaning":"Be pleased, grace us","role":"Verb, Imperative"},{"word":"महालक्ष्म्यै","meaning":"To Maha Lakshmi","role":"Dative"},{"word":"नमः","meaning":"I bow / Salutation","role":"Indeclinable"}]',
 'Sri Sukta, Rigveda Khila'),

-- Satyanarayan Mantra
('ॐ नमो भगवते वासुदेवाय।',
 'Om Namo Bhagavate Vasudevaya',
 '[{"word":"ॐ","meaning":"Sacred syllable, the absolute","role":"Invocation"},{"word":"नमो","meaning":"Salutation","role":"Indeclinable"},{"word":"भगवते","meaning":"To the Divine Lord","role":"Dative"},{"word":"वासुदेवाय","meaning":"To Vasudeva (Krishna/Vishnu)","role":"Dative"}]',
 'Vishnu Purana, Dwadashakshari Mantra'),

-- Shiva Mantra (Panchakshara)
('ॐ नमः शिवाय।',
 'Om Namah Shivaya',
 '[{"word":"ॐ","meaning":"Primordial sound, the absolute truth","role":"Invocation"},{"word":"नमः","meaning":"Salutation, I bow","role":"Indeclinable"},{"word":"शिवाय","meaning":"To Shiva, the auspicious one","role":"Dative"}]',
 'Krishna Yajurveda, Taittiriya Samhita 4.5.1'),

-- Gayatri Mantra
('ॐ भूर्भुवः स्वः।\nतत्सवितुर्वरेण्यं भर्गो देवस्य धीमहि।\nधियो यो नः प्रचोदयात्॥',
 'Om Bhur Bhuvah Svah\nTat Savitur Varenyam Bhargo Devasya Dhimahi\nDhiyo Yo Nah Prachodayat',
 '[{"word":"ॐ","meaning":"Pranava, the primordial sound","role":"Invocation"},{"word":"भूः","meaning":"Earth","role":"Vyahrti"},{"word":"भुवः","meaning":"Atmosphere","role":"Vyahrti"},{"word":"स्वः","meaning":"Heaven / Upper realms","role":"Vyahrti"},{"word":"तत्","meaning":"That (transcendent)","role":"Pronoun"},{"word":"सवितुः","meaning":"Of the Sun / Divine illuminator","role":"Genitive"},{"word":"वरेण्यम्","meaning":"Most excellent, most worshipful","role":"Accusative"},{"word":"भर्गः","meaning":"Radiance, purifying light","role":"Accusative"},{"word":"देवस्य","meaning":"Of the deity","role":"Genitive"},{"word":"धीमहि","meaning":"Let us meditate","role":"Verb, Optative"},{"word":"धियः","meaning":"Intellects, faculties of understanding","role":"Accusative"},{"word":"यः","meaning":"Who","role":"Relative pronoun"},{"word":"नः","meaning":"Our","role":"Genitive"},{"word":"प्रचोदयात्","meaning":"May he inspire/guide","role":"Verb, Optative"}]',
 'Rigveda 3.62.10'),

-- Durga Mantra
('या देवी सर्वभूतेषु शक्तिरूपेण संस्थिता।\nनमस्तस्यै नमस्तस्यै नमस्तस्यै नमो नमः॥',
 'Ya Devi Sarvabhuteshu Shaktirúpena Samsthita\nNamastasyai Namastasyai Namastasyai Namo Namah',
 '[{"word":"या","meaning":"Who (she who)","role":"Relative pronoun, feminine"},{"word":"देवी","meaning":"The Goddess","role":"Nominative"},{"word":"सर्वभूतेषु","meaning":"In all beings","role":"Locative plural"},{"word":"शक्तिरूपेण","meaning":"In the form of Shakti/power","role":"Instrumental"},{"word":"संस्थिता","meaning":"Abiding, established","role":"Past participle"},{"word":"नमः","meaning":"Salutation","role":"Indeclinable"}]',
 'Durgasaptashati, Markandeya Purana'),

-- Kalash Sthapana Mantra
('कलशस्य मुखे विष्णुर्कण्ठे रुद्रः समाश्रितः।\nमूले तत्र स्थितो ब्रह्मा मध्ये मातृगणाः स्मृताः॥',
 'Kalashasya Mukhe Vishnur Kanthe Rudrah Samashritah\nMule Tatra Sthito Brahma Madhye Matriganah Smritah',
 '[{"word":"कलशस्य","meaning":"Of the Kalash","role":"Genitive"},{"word":"मुखे","meaning":"In the mouth/opening","role":"Locative"},{"word":"विष्णुः","meaning":"Lord Vishnu","role":"Nominative"},{"word":"कण्ठे","meaning":"In the neck","role":"Locative"},{"word":"रुद्रः","meaning":"Lord Rudra/Shiva","role":"Nominative"},{"word":"समाश्रितः","meaning":"Has taken residence","role":"Past participle"},{"word":"मूले","meaning":"At the base","role":"Locative"},{"word":"ब्रह्मा","meaning":"Lord Brahma","role":"Nominative"},{"word":"मध्ये","meaning":"In the middle","role":"Locative"},{"word":"मातृगणाः","meaning":"The Divine Mothers","role":"Nominative"}]',
 'Traditional Kalash Sthapana Mantra');

-- -------------------------------------------------------
-- PUJA STEPS - Ganesh Puja (puja_id = 1)
-- -------------------------------------------------------
INSERT IGNORE INTO puja_steps (puja_id, step_order, title, title_devanagari, description, video_url, video_transcript) VALUES

(1, 1, 'Achamana - Ritual Purification', 'आचमन',
 '<h3>आचमन - Ritual Sipping of Water</h3>
<p>Begin by purifying the body and mind. Sit facing East in Sukhasana (cross-legged position). Take a small copper vessel with water and a spoon.</p>
<ol>
  <li>With the right hand in Arghya mudra, sip water three times chanting each Vishnu name.</li>
  <li>Chant: <strong>ॐ केशवाय नमः</strong> (sip), <strong>ॐ नारायणाय नमः</strong> (sip), <strong>ॐ माधवाय नमः</strong> (sip)</li>
  <li>Touch both hands to chest, then wipe with cloth.</li>
</ol>
<p><em>Purpose:</em> Achamana removes subtle impurities and invokes Vishnu as the purifier.</p>',
 'https://www.youtube.com/watch?v=example_achamana',
 'In this step we begin the Achamana ritual. Achamana is the sacred act of sipping water three times to purify the body and mind. Hold your right hand in the Arghya Mudra - form a cup shape with your palm. Take a small copper spoon and pour water into it. As you sip, chant each divine name of Vishnu...'),

(1, 2, 'Sankalpa - Taking the Vow', 'संकल्प',
 '<h3>संकल्प - The Sacred Vow</h3>
<p>Sankalpa is the declaration of your intention before the gods. Hold flowers, akshat (rice), and water in your right palm.</p>
<ol>
  <li>State your name, gotra, and the purpose of the puja</li>
  <li>Chant the Sankalpa mantra mentioning the date, time, location in the traditional Vedic way</li>
  <li>Release the flowers and rice as an offering</li>
</ol>
<p>The Sankalpa connects your personal intention to the divine will and makes the puja an official ritual act.</p>',
 'https://www.youtube.com/watch?v=example_sankalpa',
 'Sankalpa is the resolute intention we set before any puja. Take flowers and unbroken rice grains (akshat) in your right palm with a small amount of water. The Sankalpa informs the divine realm of who you are, what you desire, and why you are performing this ritual...'),

(1, 3, 'Kalash Sthapana - Establishing the Sacred Pot', 'कलश स्थापना',
 '<h3>कलश स्थापना - Sacred Pot Installation</h3>
<p>The Kalash represents the universe and all deities. It is the central sacred element of the puja.</p>
<h4>Materials needed:</h4>
<ul>
  <li>Copper or brass Kalash (water pot)</li>
  <li>5 Mango leaves (placed inside, fanning out from mouth)</li>
  <li>1 Coconut (placed on top)</li>
  <li>Gangajal or pure water to fill the pot</li>
  <li>Roli, Chandan for marking</li>
</ul>
<p>Place Kalash on a mound of raw rice. Apply chandan and roli. Arrange 5 mango leaves. Place coconut wrapped in cloth on top. Chant the Kalash mantra as you fill it.</p>',
 'https://www.youtube.com/watch?v=example_kalash',
 'The Kalash Sthapana is the most important preparatory step. The Kalash or sacred pot represents the entire cosmos. Lord Vishnu resides in its mouth, Lord Shiva in its neck, Lord Brahma at its base, and the divine mothers in its middle...'),

(1, 4, 'Ganesh Avahana - Invocation of Ganesha', 'गणेश आवाहन',
 '<h3>गणेश आवाहन - Invoking Lord Ganesha</h3>
<p>Now we formally invite Lord Ganesha to be present in the idol or picture. This is the most important step as Ganesha must be invoked before all other deities.</p>
<p>Place the Ganesha murti on a clean cloth (asana). Offer flowers, akshat, and light the diya. Perform the following Shodashopachara (16 offerings) for Ganesha:</p>
<ol>
  <li><strong>Asana</strong> - offering a seat</li>
  <li><strong>Padya</strong> - water for feet</li>
  <li><strong>Arghya</strong> - water offering</li>
  <li><strong>Achamana</strong> - water for sipping</li>
  <li><strong>Snana</strong> - sacred bath with Panchamrit</li>
  <li><strong>Vastra</strong> - offering cloth</li>
  <li><strong>Yajnopaveeta</strong> - sacred thread</li>
  <li><strong>Gandha</strong> - sandalwood paste</li>
  <li><strong>Pushpa</strong> - flowers</li>
  <li><strong>Dhupa</strong> - incense</li>
  <li><strong>Dipa</strong> - lamp</li>
  <li><strong>Naivedya</strong> - food offering (modak)</li>
  <li><strong>Tambula</strong> - betel leaves</li>
  <li><strong>Phala</strong> - fruit offering</li>
  <li><strong>Daksina</strong> - offering of dakshina</li>
  <li><strong>Aarti</strong> - waving of lamp</li>
</ol>',
 'https://www.youtube.com/watch?v=example_avahana',
 'Ganesh Avahana means formally inviting Lord Ganesha into the ritual space. Without invoking Ganesha first, no puja is considered complete. Place the Ganesha idol on a clean red cloth. Take flowers in your hand and chant the Avahana mantra...'),

(1, 5, 'Puja Aarti - Concluding Worship', 'पूजा आरती',
 '<h3>पूजा आरती - The Concluding Aarti</h3>
<p>Aarti is the culmination of the puja. Wave a lit camphor/ghee diya in circular motions before the deity.</p>
<h4>Steps:</h4>
<ol>
  <li>Light a clean ghee diya or camphor</li>
  <li>Hold with both hands or right hand only</li>
  <li>Move in 3 clockwise circles at feet level</li>
  <li>2 circles at navel level</li>
  <li>7 circles at face level</li>
  <li>Ring the bell continuously during aarti</li>
  <li>Sing the Ganesh Aarti: Jai Ganesh Jai Ganesh...</li>
</ol>
<p>After aarti, pass the flame to all devotees to receive blessings. Do Pradakshina (circumambulation) 3 times. Receive prasad and distribute.</p>',
 'https://www.youtube.com/watch?v=example_aarti',
 'The aarti is the most joyful part of the puja. Light the camphor or ghee diya. Hold it with both hands. Wave it in clockwise circles starting at the feet of Ganesha moving upward. Ring the bell as you chant the Aarti....');

-- -------------------------------------------------------
-- STEP_MANTRAS - map mantras to steps
-- -------------------------------------------------------
INSERT IGNORE INTO step_mantras (step_id, mantra_id, sequence_order) VALUES
(1, 1, 1),
(3, 7, 1),
(4, 1, 1),
(5, 5, 1);

-- -------------------------------------------------------
-- STEP SAMAGRI REQUIREMENTS
-- -------------------------------------------------------
INSERT IGNORE INTO step_samagri (step_id, samagri_id, quantity, notes) VALUES
(1, 13, '3 sips', 'Use copper spoon for sipping'),
(3, 1, '1 piece', 'Copper or brass preferred'),
(3, 2, '5 leaves', 'Fresh, unbroken'),
(3, 3, '1 piece', 'Unbroken coconut only'),
(3, 13, 'Full pot', 'Fill completely'),
(3, 4, '1 pinch', 'Apply on Kalash'),
(3, 14, '1 teaspoon', 'Mix with water for paste'),
(4, 8, 'Handful', 'Red flowers preferred for Ganesha'),
(4, 5, '1 handful', 'Unbroken rice only'),
(4, 7, '1 diya', 'Pure ghee preferred'),
(4, 15, '1 pinch', 'Mixed with akshat'),
(5, 12, '2 pieces', 'Double camphor for bright flame'),
(5, 7, '1 diya', 'Ghee diya for main aarti');

-- -------------------------------------------------------
-- RESOURCES (PDF books)
-- -------------------------------------------------------
INSERT IGNORE INTO resources (puja_id, title, resource_type, file_url, page_count, is_downloadable) VALUES
(1, 'Ganesh Puja Paddhati', 'PDF', '/pdfs/ganesh-paddhati.pdf', 45, FALSE),
(2, 'Lakshmi Puja Manual', 'PDF', '/pdfs/lakshmi-paddhati.pdf', 60, FALSE),
(3, 'Satyanarayan Vrat Katha', 'PDF', '/pdfs/satyanarayan-katha.pdf', 80, TRUE),
(4, 'Sri Rudram with Translation', 'PDF', '/pdfs/sri-rudram.pdf', 120, FALSE),
(5, 'Durga Saptashati (Full)', 'PDF', '/pdfs/durga-saptashati.pdf', 200, TRUE);

-- -------------------------------------------------------
-- HINDU FESTIVALS 2025 - 2026
-- -------------------------------------------------------
INSERT IGNORE INTO hindu_festivals (name, name_devanagari, description, puja_id, event_date, days_duration, notification_days_before) VALUES

('Makar Sankranti', 'मकर संक्रांति',
 'The festival marking the transition of the sun into Capricorn. Celebrated with horse bathing at holy rivers and kite flying. Begins the auspicious Uttarayan period.',
 NULL, '2025-01-14', 1, 20),

('Maha Shivratri', 'महाशिवरात्रि',
 'The great night of Shiva. Devotees fast throughout the day and night, performing Rudrabhishek and chanting Om Namah Shivaya. Most sacred night for Shaivites.',
 4, '2025-02-26', 1, 20),

('Holi', 'होली',
 'Festival of colors celebrating the victory of Prahlada (devotion) over Hiranyakashipu (ego) and the burning of Holika. Rang Panchami follows five days later.',
 NULL, '2025-03-14', 2, 20),

('Ram Navami', 'राम नवमी',
 'Celebration of the birth of Lord Rama, the seventh avatar of Vishnu. Ramayana recitation and Puja of Rama Parivar are central activities.',
 NULL, '2025-04-06', 1, 20),

('Hanuman Jayanti', 'हनुमान जयंती',
 'Birthday of Lord Hanuman. Recitation of Hanuman Chalisa, Sundar Kanda, and distribution of prasad of sindoor and oil.',
 NULL, '2025-04-12', 1, 20),

('Akshaya Tritiya', 'अक्षय तृतीया',
 'One of the most auspicious days in the Hindu calendar. Any charitable act or purchase done on this day is believed to grow manifold.',
 1, '2025-04-30', 1, 20),

('Nirjala Ekadashi', 'निर्जला एकादशी',
 'The toughest of all Ekadashis - a waterless fast. Observed in the scorching summer heat. Dedicated to Lord Vishnu. Said to be equivalent to all 24 Ekadashi fasts.',
 NULL, '2025-06-07', 1, 20),

('Guru Purnima', 'गुरु पूर्णिमा',
 'Full moon day dedicated to the Guru-disciple relationship. Offerings made to one''s spiritual teacher and to Veda Vyasa, compiler of the Vedas.',
 NULL, '2025-07-10', 1, 20),

('Nag Panchami', 'नाग पंचमी',
 'Worship of the Naga (serpent) deities. Milk offered at snake shrines. Part of the Shravan month sacred to Lord Shiva.',
 NULL, '2025-07-29', 1, 20),

('Raksha Bandhan', 'रक्षाबंधन',
 'Sacred bond between siblings. Sisters tie Rakhi on brothers'' wrists, brothers vow their protection. Celebrated on Shravan Purnima.',
 NULL, '2025-08-09', 1, 20),

('Janmashtami', 'जन्माष्टमी',
 'Birth of Lord Krishna, the eighth avatar of Vishnu. Midnight celebrations, Dahi Handi, and Bhagavat recitation. One of the most joyful festivals.',
 NULL, '2025-08-16', 2, 20),

('Ganesh Chaturthi', 'गणेश चतुर्थी',
 'Ten-day festival celebrating the birth of Lord Ganesha. Elaborately decorated Ganesha idols installed in homes and public pandals, culminating in Visarjan.',
 1, '2025-08-27', 10, 20),

('Navratri (Sharad)', 'शारदीय नवरात्रि',
 'Nine nights dedicated to the nine forms of Goddess Durga. Garba and Dandiya dances in Gujarat, Durga Puja in Bengal, Golu display in South India.',
 5, '2025-10-02', 9, 20),

('Durga Puja', 'दुर्गा पूजा',
 'The grand celebration of Goddess Durga''s victory over the demon Mahishasura. Enormous pandals, cultural programs, and processions mark this 5-day festival in Bengal.',
 5, '2025-10-02', 5, 25),

('Dussehra (Vijayadashami)', 'दशहरा / विजयादशमी',
 'Victory of Lord Rama over Ravana, celebrated on the tenth day after Navratri. Ravana effigies burned. Also marks Goddess Durga''s departure.',
 NULL, '2025-10-02', 1, 20),

('Kojagari Purnima', 'कोजागरी पूर्णिमा',
 'Lakshmi Puja on the full moon of Ashwin. Goddess Lakshmi is said to descend and bless those who are awake at night.',
 2, '2025-10-07', 1, 20),

('Dhanteras', 'धनतेरस',
 'Two days before Diwali. Lord Dhanvantari, the physician of gods, is worshipped. Gold and silver purchases are considered auspicious.',
 NULL, '2025-10-20', 1, 20),

('Diwali (Deepavali)', 'दीपावली',
 'Festival of Lights - Goddess Lakshmi is worshipped with elaborately lit diyas. Lord Rama''s return to Ayodhya is celebrated. Five-day festival culminating in Bhai Dooj.',
 2, '2025-10-20', 5, 25),

('Govardhan Puja', 'गोवर्धन पूजा',
 'One day after Diwali. Celebrates Krishna''s lifting of Govardhan mountain to protect the villagers. Annakut (mountain of food) offering made to Lord.',
 NULL, '2025-10-22', 1, 20),

('Chhath Puja', 'छठ पूजा',
 'Sun worship festival observed in Bihar, Jharkhand, and UP. Devotees stand in water at sunrise and sunset offering arghya to Surya Dev and Chhathi Maiya.',
 NULL, '2025-10-28', 4, 20),

('Kartik Purnima', 'कार्तिक पूर्णिमा',
 'The most sacred full moon of the year. Holy dip at river ghats. Guru Nanak Jayanti is also celebrated on this day.',
 NULL, '2025-11-05', 1, 20),

('Vivah Panchami', 'विवाह पंचमी',
 'The divine marriage of Lord Rama and Sita Mata. Celebrated with Ram-Sita wedding enactments in temples especially in Nepal and Janakpur.',
 NULL, '2025-11-27', 1, 20),

('Makar Sankranti 2026', 'मकर संक्रांति २०२६',
 'The festival marking the transition of the sun into Capricorn. Celebrated with horse bathing at holy rivers and kite flying.',
 NULL, '2026-01-14', 1, 20),

('Basant Panchami', 'बसंत पंचमी',
 'Worship of Goddess Saraswati on the fifth day of spring. Students worship their books and musical instruments. Yellow clothing worn to honor the season.',
 NULL, '2026-01-26', 1, 20),

('Maha Shivratri 2026', 'महाशिवरात्रि २०२६',
 'The great night of Shiva. Devotees fast throughout the day and night, performing Rudrabhishek and chanting Om Namah Shivaya.',
 4, '2026-02-15', 1, 20),

('Holi 2026', 'होली २०२६',
 'Festival of colors celebrating the victory of devotion over ego. Holika Dahan the night before.',
 NULL, '2026-03-03', 2, 20),

('Navratri (Chaitra) 2026', 'चैत्र नवरात्रि २०२६',
 'Nine-night Navratri in the spring month of Chaitra. Ram Navami falls on the ninth day.',
 5, '2026-03-29', 9, 20),

('Ram Navami 2026', 'राम नवमी २०२६',
 'Celebration of the birth of Lord Rama.',
 NULL, '2026-04-06', 1, 20),

('Ganesh Chaturthi 2026', 'गणेश चतुर्थी २०२६',
 'Ten-day festival celebrating the birth of Lord Ganesha.',
 1, '2026-08-17', 10, 20),

('Navratri (Sharad) 2026', 'शारदीय नवरात्रि २०२६',
 'Nine nights dedicated to the nine forms of Goddess Durga.',
 5, '2026-09-22', 9, 20),

('Diwali 2026', 'दीपावली २०२६',
 'Festival of Lights - Goddess Lakshmi is worshipped with elaborately lit diyas.',
 2, '2026-10-09', 5, 25),

('Chhath Puja 2026', 'छठ पूजा २०२६',
 'Sun worship festival observed in Bihar, Jharkhand, and UP.',
 NULL, '2026-10-17', 4, 20);

-- -------------------------------------------------------
-- USER NOTIFICATION PREFERENCES
-- -------------------------------------------------------
INSERT IGNORE INTO user_notification_preferences
    (user_id, festival_reminders, panchang_alerts, learning_reminders, puja_practice, reminder_days_before)
VALUES
(1, TRUE, TRUE, TRUE, TRUE, 20),
(2, TRUE, TRUE, TRUE, TRUE, 20),
(3, TRUE, TRUE, TRUE, TRUE, 20);