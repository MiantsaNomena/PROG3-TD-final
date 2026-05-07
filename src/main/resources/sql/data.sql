-- ============================================================
-- Nettoyage complet avant réinsertion des données de test
-- ============================================================
SET session_replication_role = replica;
TRUNCATE TABLE collectivity_structure CASCADE;
TRUNCATE TABLE member_payment CASCADE;
TRUNCATE TABLE transaction CASCADE;
TRUNCATE TABLE membership_fee CASCADE;
TRUNCATE TABLE financial_account CASCADE;
TRUNCATE TABLE attendance CASCADE;
TRUNCATE TABLE activity CASCADE;
TRUNCATE TABLE member CASCADE;
TRUNCATE TABLE collectivity CASCADE;
SET session_replication_role = DEFAULT;

-- ============================================================
-- 1. Collectivités (Tableau 1)
-- ============================================================
INSERT INTO collectivity (id, location, creation_date, federation_approval, unique_number, unique_name)
VALUES ('col-1', 'Ambatondrazaka', '2026-05-07', true, '1', 'Mpanorina'),
       ('col-2', 'Ambatondrazaka', '2026-05-07', true, '2', 'Dobo voalahany'),
       ('col-3', 'Brickaville', '2026-05-07', true, '3', 'Tantely mamy');

-- ============================================================
-- 2. Membres (Tableaux 2, 3, 4)
-- ============================================================
-- Pour tous les membres, membership_date = '2026-01-01' sauf nouveaux membres (ajoutés plus tard)
-- Occupation: PRÉSIDENT, VICE_PRESIDENT, SECRETARY, TREASURER, SENIOR (Confirmé) – utiliser les enums exacts
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation,
                    collectivity_id, active, membership_date)
VALUES
-- Collectivité 1
('C1-M1', 'Nom membre 1', 'Prénom membre 1', '1980-02-01', 'MALE', 'Lot II V M Ambat.', 'Riziculteur', '0341234567',
 'member.1@fed-agri.mg', 'PRESIDENT', 'col-1', true, '2026-01-01'),
('C1-M2', 'Nom membre 2', 'Prénom membre 2', '1982-03-05', 'MALE', 'Lot II F Ambat.', 'Agriculteur', '0321234567',
 'member.2@fed-agri.mg', 'VICE_PRESIDENT', 'col-1', true, '2026-01-01'),
('C1-M3', 'Nom membre 3', 'Prénom membre 3', '1992-03-10', 'MALE', 'Lot II J Ambat.', 'Collecteur', '0331234567',
 'member.3@fed-agri.mg', 'SECRETARY', 'col-1', true, '2026-01-01'),
('C1-M4', 'Nom membre 4', 'Prénom membre 4', '1988-05-22', 'FEMALE', 'Lot A K 50 Ambat.', 'Distributeur', '0381234567',
 'member.4@fed-agri.mg', 'TREASURER', 'col-1', true, '2026-01-01'),
('C1-M5', 'Nom membre 5', 'Prénom membre 5', '1999-08-21', 'MALE', 'Lot UV 80 Ambat.', 'Riziculteur', '037034567',
 'member.5@fed-agri.mg', 'SENIOR', 'col-1', true, '2026-01-01'),
('C1-M6', 'Nom membre 6', 'Prénom membre 6', '1998-08-22', 'FEMALE', 'Lot UV 6 Ambat.', 'Riziculteur', '0372234567',
 'member.6@fed-agri.mg', 'SENIOR', 'col-1', true, '2026-01-01'),
('C1-M7', 'Nom membre 7', 'Prénom membre 7', '1998-01-31', 'MALE', 'Lot UV 7 Ambat.', 'Riziculteur', '0374234567',
 'member.7@fed-agri.mg', 'SENIOR', 'col-1', true, '2026-01-01'),
('C1-M8', 'Nom membre 8', 'Prénom membre 6', '1975-08-20', 'MALE', 'Lot UV 8 Ambat.', 'Riziculteur', '0370234567',
 'member.8@fed-agri.mg', 'SENIOR', 'col-1', true, '2026-01-01'),
-- Collectivité 2 (les mêmes personnes physiques, emails distincts)
('C2-M1', 'Nom membre 1', 'Prénom membre 1', '1980-02-01', 'MALE', 'Lot II V M Ambat.', 'Riziculteur', '0341234567',
 'member.1.col2@fed-agri.mg', 'SENIOR', 'col-2', true, '2026-01-01'),
('C2-M2', 'Nom membre 2', 'Prénom membre 2', '1982-03-05', 'MALE', 'Lot II F Ambat.', 'Agriculteur', '0321234567',
 'member.2.col2@fed-agri.mg', 'SENIOR', 'col-2', true, '2026-01-01'),
('C2-M3', 'Nom membre 3', 'Prénom membre 3', '1992-03-10', 'MALE', 'Lot II J Ambat.', 'Collecteur', '0331234567',
 'member.3.col2@fed-agri.mg', 'SENIOR', 'col-2', true, '2026-01-01'),
('C2-M4', 'Nom membre 4', 'Prénom membre 4', '1988-05-22', 'FEMALE', 'Lot A K 50 Ambat.', 'Distributeur', '0381234567',
 'member.4.col2@fed-agri.mg', 'SENIOR', 'col-2', true, '2026-01-01'),
('C2-M5', 'Nom membre 5', 'Prénom membre 5', '1999-08-21', 'MALE', 'Lot UV 80 Ambat.', 'Riziculteur', '037034567',
 'member.5.col2@fed-agri.mg', 'PRESIDENT', 'col-2', true, '2026-01-01'),
('C2-M6', 'Nom membre 6', 'Prénom membre 6', '1998-08-22', 'FEMALE', 'Lot UV 6 Ambat.', 'Riziculteur', '0372234567',
 'member.6.col2@fed-agri.mg', 'VICE_PRESIDENT', 'col-2', true, '2026-01-01'),
('C2-M7', 'Nom membre 7', 'Prénom membre 7', '1998-01-31', 'MALE', 'Lot UV 7 Ambat.', 'Riziculteur', '0374234567',
 'member.7.col2@fed-agri.mg', 'SECRETARY', 'col-2', true, '2026-01-01'),
('C2-M8', 'Nom membre 8', 'Prénom membre 6', '1975-08-20', 'MALE', 'Lot UV 8 Ambat.', 'Riziculteur', '0370234567',
 'member.8.col2@fed-agri.mg', 'TREASURER', 'col-2', true, '2026-01-01'),
-- Collectivité 3
('C3-M1', 'Nom membre 9', 'Prénom membre 9', '1988-01-02', 'MALE', 'Lot 33 J Antsirabe', 'Apiculteur', '034034567',
 'member.9@fed-agri.mg', 'PRESIDENT', 'col-3', true, '2026-01-01'),
('C3-M2', 'Nom membre 10', 'Prénom membre 10', '1982-03-05', 'MALE', 'Lot 2 J Antsirabe', 'Agriculteur', '0338634567',
 'member.10@fed-agri.mg', 'VICE_PRESIDENT', 'col-3', true, '2026-01-01'),
('C3-M3', 'Nom membre 11', 'Prénom membre 11', '1992-03-12', 'MALE', 'Lot 8 KM Antsirabe', 'Collecteur', '0338234567',
 'member.11@fed-agri.mg', 'SECRETARY', 'col-3', true, '2026-01-01'),
('C3-M4', 'Nom membre 12', 'Prénom membre 12', '1988-05-10', 'FEMALE', 'Lot A K 50 Antsirabe', 'Distributeur',
 '0382334567', 'member.12@fed-agri.mg', 'TREASURER', 'col-3', true, '2026-01-01'),
('C3-M5', 'Nom membre 13', 'Prénom membre 13', '1999-08-11', 'MALE', 'Lot UV 80 Antsirabe', 'Apiculteur', '0373365567',
 'member.13@fed-agri.mg', 'SENIOR', 'col-3', true, '2026-01-01'),
('C3-M6', 'Nom membre 14', 'Prénom membre 14', '1998-08-09', 'FEMALE', 'Lot UV 6 Antsirabe', 'Apiculteur', '0378234567',
 'member.14@fed-agri.mg', 'SENIOR', 'col-3', true, '2026-01-01'),
('C3-M7', 'Nom membre 15', 'Prénom membre 15', '1998-01-13', 'MALE', 'Lot UV 7 Antsirabe', 'Apiculteur', '0374914567',
 'member.15@fed-agri.mg', 'SENIOR', 'col-3', true, '2026-01-01'),
('C3-M8', 'Nom membre 16', 'Prénom membre 16', '1975-08-02', 'MALE', 'Lot UV 8 Antsirabe', 'Apiculteur', '0370634567',
 'member.16@fed-agri.mg', 'SENIOR', 'col-3', true, '2026-01-01');

-- ============================================================
-- 3. Structures (postes) des collectivités
-- ============================================================
INSERT INTO collectivity_structure (collectivity_id, role, member_id)
VALUES ('col-1', 'PRESIDENT', 'C1-M1'),
       ('col-1', 'VICE_PRESIDENT', 'C1-M2'),
       ('col-1', 'TREASURER', 'C1-M4'),
       ('col-1', 'SECRETARY', 'C1-M3'),
       ('col-2', 'PRESIDENT', 'C2-M5'),
       ('col-2', 'VICE_PRESIDENT', 'C2-M6'),
       ('col-2', 'TREASURER', 'C2-M8'),
       ('col-2', 'SECRETARY', 'C2-M7'),
       ('col-3', 'PRESIDENT', 'C3-M1'),
       ('col-3', 'VICE_PRESIDENT', 'C3-M2'),
       ('col-3', 'TREASURER', 'C3-M4'),
       ('col-3', 'SECRETARY', 'C3-M3');

-- ============================================================
-- 4. Comptes financiers (anciens + nouveaux)
-- ============================================================
-- Collectivité 1
INSERT INTO financial_account (id, collectivity_id, account_type, holder_name, mobile_service, mobile_number, balance)
VALUES ('C1-A-CASH', 'col-1', 'CASH', NULL, NULL, NULL, 0),
       ('C1-A-MOBILE-1', 'col-1', 'MOBILE', 'Mpanorina', 'ORANGE_MONEY', '0370489612', 0);
-- Collectivité 2
INSERT INTO financial_account (id, collectivity_id, account_type, holder_name, mobile_service, mobile_number, balance)
VALUES ('C2-A-CASH', 'col-2', 'CASH', NULL, NULL, NULL, 0),
       ('C2-A-MOBILE-1', 'col-2', 'MOBILE', 'Dobo voalahany', 'ORANGE_MONEY', '0320489612', 0);

INSERT INTO financial_account (id, collectivity_id, account_type, holder_name, mobile_service, mobile_number, bank_name,
                               bank_code, bank_branch_code, bank_account_number, bank_account_key, balance)
VALUES ('C3-A-CASH', 'col-3', 'CASH', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0),
       ('C3-A-BANK-1', 'col-3', 'BANK', 'Koto', NULL, NULL, 'BMOI', '00004', '00001', '1234567890', '12', 0),
       ('C3-A-BANK-2', 'col-3', 'BANK', 'Naivo', NULL, NULL, 'BRED', '00008', '00003', '4567890123', '58', 0),
       ('C3-A-MOBILE-1', 'col-3', 'MOBILE', 'Kolo', 'MVOLA', '0341889612', NULL, NULL, NULL, NULL, NULL, 0);

-- ============================================================
-- 5. Cotisations (membership_fee)
-- ============================================================
INSERT INTO membership_fee (id, collectivity_id, eligible_from, frequency, amount, label, status)
VALUES ('cot-1', 'col-1', '2026-01-01', 'ANNUALLY', 200000, 'Cotisation annuelle', 'ACTIVE'),
       ('cot-2', 'col-1', '2026-04-30', 'PUNCTUALLY', 20000, 'Famangiana', 'ACTIVE'),
       ('cot-3', 'col-2', '2026-01-01', 'ANNUALLY', 200000, 'Cotisation annuelle', 'ACTIVE'),
       ('cot-4', 'col-2', '2025-01-01', 'ANNUALLY', 100000, 'Cotisation 2025', 'INACTIVE'),
       ('cot-5', 'col-3', '2026-04-01', 'MONTHLY', 25000, 'Cotisation mensuelle', 'ACTIVE');

-- ============================================================
-- 6. Paiements et transactions pour collectivité 1
-- ============================================================
-- Tableau 8 (Col-1) : montants et comptes
INSERT INTO member_payment (id, member_id, membership_fee_id, amount, payment_mode, account_credited_id, creation_date)
VALUES ('pay-C1M1-1', 'C1-M1', 'cot-1', 200000, 'CASH', 'C1-A-CASH', '2026-01-01'),
       ('pay-C1M2-1', 'C1-M2', 'cot-1', 200000, 'CASH', 'C1-A-CASH', '2026-01-01'),
       ('pay-C1M3-1', 'C1-M3', 'cot-1', 200000, 'MOBILE_BANKING', 'C1-A-MOBILE-1', '2026-01-01'),
       ('pay-C1M4-1', 'C1-M4', 'cot-1', 200000, 'MOBILE_BANKING', 'C1-A-MOBILE-1', '2026-01-01'),
       ('pay-C1M5-1', 'C1-M5', 'cot-1', 150000, 'MOBILE_BANKING', 'C1-A-MOBILE-1', '2026-01-01'),
       ('pay-C1M6-1', 'C1-M6', 'cot-1', 100000, 'CASH', 'C1-A-CASH', '2026-05-01'),
       ('pay-C1M7-1', 'C1-M7', 'cot-1', 60000, 'CASH', 'C1-A-CASH', '2026-05-01'),
       ('pay-C1M8-1', 'C1-M8', 'cot-1', 90000, 'CASH', 'C1-A-CASH', '2026-05-01');

-- Transactions correspondantes (mêmes montants, mêmes dates)
INSERT INTO transaction (id, collectivity_id, member_id, amount, payment_mode, account_credited_id, creation_date)
VALUES ('tx-C1M1-1', 'col-1', 'C1-M1', 200000, 'CASH', 'C1-A-CASH', '2026-01-01'),
       ('tx-C1M2-1', 'col-1', 'C1-M2', 200000, 'CASH', 'C1-A-CASH', '2026-01-01'),
       ('tx-C1M3-1', 'col-1', 'C1-M3', 200000, 'MOBILE_BANKING', 'C1-A-MOBILE-1', '2026-01-01'),
       ('tx-C1M4-1', 'col-1', 'C1-M4', 200000, 'MOBILE_BANKING', 'C1-A-MOBILE-1', '2026-01-01'),
       ('tx-C1M5-1', 'col-1', 'C1-M5', 150000, 'MOBILE_BANKING', 'C1-A-MOBILE-1', '2026-01-01'),
       ('tx-C1M6-1', 'col-1', 'C1-M6', 100000, 'CASH', 'C1-A-CASH', '2026-05-01'),
       ('tx-C1M7-1', 'col-1', 'C1-M7', 60000, 'CASH', 'C1-A-CASH', '2026-05-01'),
       ('tx-C1M8-1', 'col-1', 'C1-M8', 90000, 'CASH', 'C1-A-CASH', '2026-05-01');

-- Mise à jour des soldes après paiements (col-1)
UPDATE financial_account
SET balance = balance + 200000
WHERE id = 'C1-A-CASH';
UPDATE financial_account
SET balance = balance + 200000
WHERE id = 'C1-A-CASH';
UPDATE financial_account
SET balance = balance + 200000
WHERE id = 'C1-A-MOBILE-1';
UPDATE financial_account
SET balance = balance + 200000
WHERE id = 'C1-A-MOBILE-1';
UPDATE financial_account
SET balance = balance + 150000
WHERE id = 'C1-A-MOBILE-1';
UPDATE financial_account
SET balance = balance + 100000
WHERE id = 'C1-A-CASH';
UPDATE financial_account
SET balance = balance + 60000
WHERE id = 'C1-A-CASH';
UPDATE financial_account
SET balance = balance + 90000
WHERE id = 'C1-A-CASH';
-- Résultat: C1-A-CASH = 200k+200k+100k+60k+90k = 650k; C1-A-MOBILE-1 = 200k+200k+150k = 550k

-- ============================================================
-- 7. Paiements et transactions pour collectivité 2
-- ============================================================
INSERT INTO member_payment (id, member_id, membership_fee_id, amount, payment_mode, account_credited_id, creation_date)
VALUES ('pay-C2M1-1', 'C2-M1', 'cot-3', 120000, 'CASH', 'C2-A-CASH', '2026-01-01'),
       ('pay-C2M2-1', 'C2-M2', 'cot-3', 180000, 'CASH', 'C2-A-CASH', '2026-01-01'),
       ('pay-C2M3-1', 'C2-M3', 'cot-3', 200000, 'CASH', 'C2-A-CASH', '2026-01-01'),
       ('pay-C2M4-1', 'C2-M4', 'cot-3', 200000, 'CASH', 'C2-A-CASH', '2026-01-01'),
       ('pay-C2M5-1', 'C2-M5', 'cot-3', 200000, 'CASH', 'C2-A-CASH', '2026-01-01'),
       ('pay-C2M6-1', 'C2-M6', 'cot-3', 200000, 'CASH', 'C2-A-CASH', '2026-01-01'),
       ('pay-C2M7-1', 'C2-M7', 'cot-3', 80000, 'MOBILE_BANKING', 'C2-A-MOBILE-1', '2026-01-01'),
       ('pay-C2M8-1', 'C2-M8', 'cot-3', 120000, 'MOBILE_BANKING', 'C2-A-MOBILE-1', '2026-01-01');

INSERT INTO transaction (id, collectivity_id, member_id, amount, payment_mode, account_credited_id, creation_date)
VALUES ('tx-C2M1-1', 'col-2', 'C2-M1', 120000, 'CASH', 'C2-A-CASH', '2026-01-01'),
       ('tx-C2M2-1', 'col-2', 'C2-M2', 180000, 'CASH', 'C2-A-CASH', '2026-01-01'),
       ('tx-C2M3-1', 'col-2', 'C2-M3', 200000, 'CASH', 'C2-A-CASH', '2026-01-01'),
       ('tx-C2M4-1', 'col-2', 'C2-M4', 200000, 'CASH', 'C2-A-CASH', '2026-01-01'),
       ('tx-C2M5-1', 'col-2', 'C2-M5', 200000, 'CASH', 'C2-A-CASH', '2026-01-01'),
       ('tx-C2M6-1', 'col-2', 'C2-M6', 200000, 'CASH', 'C2-A-CASH', '2026-01-01'),
       ('tx-C2M7-1', 'col-2', 'C2-M7', 80000, 'MOBILE_BANKING', 'C2-A-MOBILE-1', '2026-01-01'),
       ('tx-C2M8-1', 'col-2', 'C2-M8', 120000, 'MOBILE_BANKING', 'C2-A-MOBILE-1', '2026-01-01');

UPDATE financial_account
SET balance = balance + 120000
WHERE id = 'C2-A-CASH';
UPDATE financial_account
SET balance = balance + 180000
WHERE id = 'C2-A-CASH';
UPDATE financial_account
SET balance = balance + 200000
WHERE id = 'C2-A-CASH';
UPDATE financial_account
SET balance = balance + 200000
WHERE id = 'C2-A-CASH';
UPDATE financial_account
SET balance = balance + 200000
WHERE id = 'C2-A-CASH';
UPDATE financial_account
SET balance = balance + 200000
WHERE id = 'C2-A-CASH';
UPDATE financial_account
SET balance = balance + 80000
WHERE id = 'C2-A-MOBILE-1';
UPDATE financial_account
SET balance = balance + 120000
WHERE id = 'C2-A-MOBILE-1';
-- Résultat: C2-A-CASH = 1 100 000; C2-A-MOBILE-1 = 200 000

-- ============================================================
-- 8. Paiements pour collectivité 3 (cotisation mensuelle)
-- ============================================================
-- Paiements du 01/04/2026 (chaque membre 25 000, sauf C3-M1 et C3-M2 déjà indiqués)
INSERT INTO member_payment (id, member_id, membership_fee_id, amount, payment_mode, account_credited_id, creation_date)
VALUES ('pay-C3M1-1', 'C3-M1', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-04-01'),
       ('pay-C3M2-1', 'C3-M2', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-04-01'),
       ('pay-C3M3-1', 'C3-M3', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-04-01'),
       ('pay-C3M4-1', 'C3-M4', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-04-01'),
       ('pay-C3M5-1', 'C3-M5', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-04-01'),
       ('pay-C3M6-1', 'C3-M6', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-04-01'),
       ('pay-C3M7-1', 'C3-M7', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-04-01'),
       ('pay-C3M8-1', 'C3-M8', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-04-01');

-- Transactions correspondantes
INSERT INTO transaction (id, collectivity_id, member_id, amount, payment_mode, account_credited_id, creation_date)
VALUES ('tx-C3M1-1', 'col-3', 'C3-M1', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-04-01'),
       ('tx-C3M2-1', 'col-3', 'C3-M2', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-04-01'),
       ('tx-C3M3-1', 'col-3', 'C3-M3', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-04-01'),
       ('tx-C3M4-1', 'col-3', 'C3-M4', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-04-01'),
       ('tx-C3M5-1', 'col-3', 'C3-M5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-04-01'),
       ('tx-C3M6-1', 'col-3', 'C3-M6', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-04-01'),
       ('tx-C3M7-1', 'col-3', 'C3-M7', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-04-01'),
       ('tx-C3M8-1', 'col-3', 'C3-M8', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-04-01');

UPDATE financial_account
SET balance = balance + 25000 * 8
WHERE id = 'C3-A-BANK-1';

-- Paiements du 01/05/2026 (certains montants différents – on suppose tous 25000 pour simplifier, sauf indication contraire dans l’annexe, mais l’annexe est vague. On mettra tous 25000 également pour mai)
INSERT INTO member_payment (id, member_id, membership_fee_id, amount, payment_mode, account_credited_id, creation_date)
VALUES ('pay-C3M1-2', 'C3-M1', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-05-01'),
       ('pay-C3M2-2', 'C3-M2', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-05-01'),
       ('pay-C3M3-2', 'C3-M3', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-05-01'),
       ('pay-C3M4-2', 'C3-M4', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-05-01'),
       ('pay-C3M5-2', 'C3-M5', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-05-01'),
       ('pay-C3M6-2', 'C3-M6', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-05-01'),
       ('pay-C3M7-2', 'C3-M7', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-05-01'),
       ('pay-C3M8-2', 'C3-M8', 'cot-5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-05-01');

INSERT INTO transaction (id, collectivity_id, member_id, amount, payment_mode, account_credited_id, creation_date)
VALUES ('tx-C3M1-2', 'col-3', 'C3-M1', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-05-01'),
       ('tx-C3M2-2', 'col-3', 'C3-M2', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-05-01'),
       ('tx-C3M3-2', 'col-3', 'C3-M3', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-05-01'),
       ('tx-C3M4-2', 'col-3', 'C3-M4', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-05-01'),
       ('tx-C3M5-2', 'col-3', 'C3-M5', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-05-01'),
       ('tx-C3M6-2', 'col-3', 'C3-M6', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-05-01'),
       ('tx-C3M7-2', 'col-3', 'C3-M7', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-05-01'),
       ('tx-C3M8-2', 'col-3', 'C3-M8', 25000, 'BANK_TRANSFER', 'C3-A-BANK-1', '2026-05-01');

UPDATE financial_account
SET balance = balance + 25000 * 8
WHERE id = 'C3-A-BANK-1';
-- Solde final de C3-A-BANK-1 = 25000*16 = 400 000

-- ============================================================
-- 9. Nouveaux membres (JUNIOR) pour chaque collectivité
-- ============================================================
-- Collectivité 1 : 4 nouveaux adhérents (dates: 01/04, 01/04, 01/05, 01/06)
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation,
                    collectivity_id, active, membership_date)
VALUES ('N1-C1-1', 'Nouveau1', 'Junior1', '2000-01-01', 'MALE', 'Ambatondrazaka', 'Étudiant', '034000001',
        'new1.col1@fed-agri.mg', 'JUNIOR', 'col-1', true, '2026-04-01'),
       ('N1-C1-2', 'Nouveau2', 'Junior2', '2000-02-02', 'FEMALE', 'Ambatondrazaka', 'Étudiant', '034000002',
        'new2.col1@fed-agri.mg', 'JUNIOR', 'col-1', true, '2026-04-01'),
       ('N1-C1-3', 'Nouveau3', 'Junior3', '2000-03-03', 'MALE', 'Ambatondrazaka', 'Étudiant', '034000003',
        'new3.col1@fed-agri.mg', 'JUNIOR', 'col-1', true, '2026-05-01'),
       ('N1-C1-4', 'Nouveau4', 'Junior4', '2000-04-04', 'FEMALE', 'Ambatondrazaka', 'Étudiant', '034000004',
        'new4.col1@fed-agri.mg', 'JUNIOR', 'col-1', true, '2026-06-01');

-- Collectivité 2 : 3 nouveaux adhérents (dates en mars 2026)
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation,
                    collectivity_id, active, membership_date)
VALUES ('N1-C2-1', 'Nouveau1', 'JuniorC2', '2001-01-01', 'MALE', 'Ambatondrazaka', 'Étudiant', '034100001',
        'new1.col2@fed-agri.mg', 'JUNIOR', 'col-2', true, '2026-03-01'),
       ('N1-C2-2', 'Nouveau2', 'JuniorC2', '2001-02-02', 'FEMALE', 'Ambatondrazaka', 'Étudiant', '034100002',
        'new2.col2@fed-agri.mg', 'JUNIOR', 'col-2', true, '2026-03-15'),
       ('N1-C2-3', 'Nouveau3', 'JuniorC2', '2001-03-03', 'MALE', 'Ambatondrazaka', 'Étudiant', '034100003',
        'new3.col2@fed-agri.mg', 'JUNIOR', 'col-2', true, '2026-03-30');

-- Collectivité 3 : 6 nouveaux adhérents (dates janvier à mars 2026)
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation,
                    collectivity_id, active, membership_date)
VALUES ('N1-C3-1', 'Nouveau1', 'JuniorC3', '2002-01-01', 'MALE', 'Brickaville', 'Étudiant', '034200001',
        'new1.col3@fed-agri.mg', 'JUNIOR', 'col-3', true, '2026-01-10'),
       ('N1-C3-2', 'Nouveau2', 'JuniorC3', '2002-02-02', 'FEMALE', 'Brickaville', 'Étudiant', '034200002',
        'new2.col3@fed-agri.mg', 'JUNIOR', 'col-3', true, '2026-01-20'),
       ('N1-C3-3', 'Nouveau3', 'JuniorC3', '2002-03-03', 'MALE', 'Brickaville', 'Étudiant', '034200003',
        'new3.col3@fed-agri.mg', 'JUNIOR', 'col-3', true, '2026-02-05'),
       ('N1-C3-4', 'Nouveau4', 'JuniorC3', '2002-04-04', 'FEMALE', 'Brickaville', 'Étudiant', '034200004',
        'new4.col3@fed-agri.mg', 'JUNIOR', 'col-3', true, '2026-02-18'),
       ('N1-C3-5', 'Nouveau5', 'JuniorC3', '2002-05-05', 'MALE', 'Brickaville', 'Étudiant', '034200005',
        'new5.col3@fed-agri.mg', 'JUNIOR', 'col-3', true, '2026-03-02'),
       ('N1-C3-6', 'Nouveau6', 'JuniorC3', '2002-06-06', 'FEMALE', 'Brickaville', 'Étudiant', '034200006',
        'new6.col3@fed-agri.mg', 'JUNIOR', 'col-3', true, '2026-03-25');

-- ============================================================
-- 10. Mise à jour des compteurs de séquences (si utilisées)
-- ============================================================
-- Les IDs sont manuels, pas de séquence à mettre à jour.
-- ============================================================
-- Fin du script