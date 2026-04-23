CREATE TABLE membership_fee (
                                id VARCHAR(36) PRIMARY KEY,
                                collectivity_id VARCHAR(36) REFERENCES collectivity(id) ON DELETE CASCADE,
                                eligible_from DATE NOT NULL,
                                frequency VARCHAR(20) NOT NULL CHECK (frequency IN ('WEEKLY', 'MONTHLY', 'ANNUALLY', 'PUNCTUALLY')),
                                amount NUMERIC(12,2) NOT NULL CHECK (amount >= 0),
                                label VARCHAR(100),
                                status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE TABLE financial_account (
                                   id VARCHAR(36) PRIMARY KEY,
                                   collectivity_id VARCHAR(36) REFERENCES collectivity(id) ON DELETE CASCADE,
                                   account_type VARCHAR(20) NOT NULL CHECK (account_type IN ('CASH', 'MOBILE', 'BANK')),
                                   holder_name VARCHAR(100),
                                   mobile_service VARCHAR(20) CHECK (mobile_service IN ('AIRTEL_MONEY', 'MVOLA', 'ORANGE_MONEY')),
                                   mobile_number VARCHAR(20),
                                   bank_name VARCHAR(30) CHECK (bank_name IN ('BRED', 'MCB', 'BMOI', 'BOA', 'BGFI', 'AFG', 'ACCES_BAQUE', 'BAOBAB', 'SIPEM')),
                                   bank_code VARCHAR(5),
                                   bank_branch_code VARCHAR(5),
                                   bank_account_number VARCHAR(11),
                                   bank_account_key VARCHAR(2),
                                   balance NUMERIC(12,2) DEFAULT 0
);

CREATE TABLE transaction (
                             id VARCHAR(36) PRIMARY KEY,
                             collectivity_id VARCHAR(36) REFERENCES collectivity(id) ON DELETE CASCADE,
                             member_id VARCHAR(36) REFERENCES member(id),
                             amount NUMERIC(12,2) NOT NULL,
                             payment_mode VARCHAR(20) NOT NULL CHECK (payment_mode IN ('CASH', 'MOBILE_BANKING', 'BANK_TRANSFER')),
                             account_credited_id VARCHAR(36) REFERENCES financial_account(id),
                             creation_date DATE NOT NULL
);

CREATE TABLE member_payment (
                                id VARCHAR(36) PRIMARY KEY,
                                member_id VARCHAR(36) REFERENCES member(id),
                                membership_fee_id VARCHAR(36) REFERENCES membership_fee(id),
                                amount NUMERIC(12,2) NOT NULL,
                                payment_mode VARCHAR(20) NOT NULL,
                                account_credited_id VARCHAR(36) REFERENCES financial_account(id),
                                creation_date DATE NOT NULL
);


CREATE TYPE frequency AS ENUM ('WEEKLY', 'MONTHLY', 'ANNUALLY', 'PUNCTUALLY');
CREATE TYPE activity_status AS ENUM ('ACTIVE', 'INACTIVE');
CREATE TYPE payment_mode AS ENUM ('CASH', 'MOBILE_BANKING', 'BANK_TRANSFER');

CREATE TYPE mobile_banking_service AS ENUM ('AIRTEL_MONEY', 'MVOLA', 'ORANGE_MONEY');
CREATE TYPE bank_name AS ENUM ('BRED', 'MCB', 'BMOI', 'BOA', 'BGFI', 'AFG', 'ACCES_BAQUE', 'BAOBAB', 'SIPEM');