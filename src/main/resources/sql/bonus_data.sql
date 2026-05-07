
DELETE
FROM attendance
WHERE activity_id IN (SELECT id FROM activity WHERE collectivity_id IN ('col-1', 'col-2', 'col-3'));
DELETE
FROM activity
WHERE collectivity_id IN ('col-1', 'col-2', 'col-3');

-- ACTIVITÉS POUR COLLECTIVITÉ 1
INSERT INTO activity (id, collectivity_id, label, activity_type, executive_date, created_at)
VALUES ('act-1-2026-03', 'col-1', 'AG1 (mars 2026)', 'MEETING', '2026-03-07', NOW()),
       ('act-1-2026-04', 'col-1', 'AG1 (avril 2026)', 'MEETING', '2026-04-04', NOW()),
       ('act-2-2026-03', 'col-1', 'Formation de base (mars 2026)', 'TRAINING', '2026-03-08', NOW()),
       ('act-2-2026-04', 'col-1', 'Formation de base (avril 2026)', 'TRAINING', '2026-04-12', NOW());

-- ACTIVITÉS POUR COLLECTIVITÉ 2
INSERT INTO activity (id, collectivity_id, label, activity_type, executive_date, created_at)
VALUES ('act-3-2026-03', 'col-2', 'AG2 (mars 2026)', 'MEETING', '2026-03-08', NOW()),
       ('act-3-2026-04', 'col-2', 'AG2 (avril 2026)', 'MEETING', '2026-04-05', NOW()),
       ('act-4-2026-03', 'col-2', 'Formation de base (mars 2026)', 'TRAINING', '2026-03-15', NOW()),
       ('act-4-2026-04', 'col-2', 'Formation de base (avril 2026)', 'TRAINING', '2026-04-19', NOW()),
       ('act-5', 'col-2', 'Perfectionnement', 'OTHER', '2026-04-30', NOW());

-- ACTIVITÉS POUR COLLECTIVITÉ 3
INSERT INTO activity (id, collectivity_id, label, activity_type, executive_date, created_at)
VALUES ('act-6-2026-03', 'col-3', 'AG3 (mars 2026)', 'MEETING', '2026-03-06', NOW()),
       ('act-6-2026-04', 'col-3', 'AG3 (avril 2026)', 'MEETING', '2026-04-03', NOW()),
       ('act-7-2026-03', 'col-3', 'Formation de base (mars 2026)', 'TRAINING', '2026-03-25', NOW()),
       ('act-7-2026-04', 'col-3', 'Formation de base (avril 2026)', 'TRAINING', '2026-04-22', NOW());

-- PRÉSENCES POUR COLLECTIVITÉ 1
INSERT INTO attendance (id, activity_id, member_id, status, updated_at)
VALUES
-- AG1 mars 2026
(gen_random_uuid()::text, 'act-1-2026-03', 'C1-M1', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-1-2026-03', 'C1-M2', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-1-2026-03', 'C1-M3', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-1-2026-03', 'C1-M4', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-1-2026-03', 'C1-M5', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-1-2026-03', 'C1-M6', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-1-2026-03', 'C1-M7', 'MISSING', NOW()),
(gen_random_uuid()::text, 'act-1-2026-03', 'C1-M8', 'MISSING', NOW()),
-- AG1 avril 2026
(gen_random_uuid()::text, 'act-1-2026-04', 'C1-M1', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-1-2026-04', 'C1-M2', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-1-2026-04', 'C1-M3', 'MISSING', NOW()),
(gen_random_uuid()::text, 'act-1-2026-04', 'C1-M4', 'MISSING', NOW()),
(gen_random_uuid()::text, 'act-1-2026-04', 'C1-M5', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-1-2026-04', 'C1-M6', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-1-2026-04', 'C1-M7', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-1-2026-04', 'C1-M8', 'ATTENDED', NOW());

-- PRÉSENCES POUR COLLECTIVITÉ 2
INSERT INTO attendance (id, activity_id, member_id, status, updated_at)
VALUES
-- AG2 mars 2026
(gen_random_uuid()::text, 'act-3-2026-03', 'C2-M1', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-3-2026-03', 'C2-M2', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-3-2026-03', 'C2-M3', 'MISSING', NOW()),
(gen_random_uuid()::text, 'act-3-2026-03', 'C2-M4', 'MISSING', NOW()),
(gen_random_uuid()::text, 'act-3-2026-03', 'C2-M5', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-3-2026-03', 'C2-M6', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-3-2026-03', 'C2-M7', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-3-2026-03', 'C2-M8', 'ATTENDED', NOW()),
-- AG2 avril 2026
(gen_random_uuid()::text, 'act-3-2026-04', 'C2-M1', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-3-2026-04', 'C2-M2', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-3-2026-04', 'C2-M3', 'MISSING', NOW()),
(gen_random_uuid()::text, 'act-3-2026-04', 'C2-M4', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-3-2026-04', 'C2-M5', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-3-2026-04', 'C2-M6', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-3-2026-04', 'C2-M7', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-3-2026-04', 'C2-M8', 'MISSING', NOW()),
-- Perfectionnement (act-5)
(gen_random_uuid()::text, 'act-5', 'C2-M1', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-5', 'C2-M2', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-5', 'C2-M3', 'ATTENDED', NOW()),
(gen_random_uuid()::text, 'act-5', 'C2-M4', 'MISSING', NOW()),
(gen_random_uuid()::text, 'act-5', 'C2-M5', 'UNDEFINED', NOW()),
(gen_random_uuid()::text, 'act-5', 'C2-M6', 'UNDEFINED', NOW()),
(gen_random_uuid()::text, 'act-5', 'C2-M7', 'UNDEFINED', NOW()),
(gen_random_uuid()::text, 'act-5', 'C2-M8', 'UNDEFINED', NOW());

