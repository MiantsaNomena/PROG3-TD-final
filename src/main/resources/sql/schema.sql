CREATE TYPE gender_enum AS ENUM ('MALE', 'FEMALE');
CREATE TYPE member_occupation_enum AS ENUM ('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT');


CREATE TABLE member (
                        id VARCHAR(36) PRIMARY KEY,
                        first_name VARCHAR(50) NOT NULL,
                        last_name VARCHAR(50) NOT NULL,
                        birth_date DATE NOT NULL,
                        gender gender_enum NOT NULL,
                        address TEXT,
                        profession VARCHAR(100),
                        phone_number VARCHAR(20) NOT NULL,
                        email VARCHAR(100) UNIQUE NOT NULL,
                        occupation member_occupation_enum NOT NULL,
                        collectivity_id VARCHAR(36),
                        active BOOLEAN DEFAULT TRUE,
                        membership_date DATE NOT NULL
);

CREATE TABLE collectivity (
                              id VARCHAR(36) PRIMARY KEY,
                              location VARCHAR(100) NOT NULL,
                              creation_date DATE NOT NULL,
                              federation_approval BOOLEAN NOT NULL
);

CREATE TABLE collectivity_structure (
                                        collectivity_id VARCHAR(36) REFERENCES collectivity(id) ON DELETE CASCADE,
                                        role VARCHAR(20) CHECK (role IN ('PRESIDENT', 'VICE_PRESIDENT', 'TREASURER', 'SECRETARY')),
                                        member_id VARCHAR(36) REFERENCES member(id) ON DELETE CASCADE,
                                        PRIMARY KEY (collectivity_id, role)
);

ALTER TABLE member ADD CONSTRAINT fk_member_collectivity
    FOREIGN KEY (collectivity_id) REFERENCES collectivity(id) ON DELETE SET NULL;


CREATE INDEX idx_member_collectivity ON member(collectivity_id);
CREATE INDEX idx_member_email ON member(email);
CREATE INDEX idx_collectivity_location ON collectivity(location);
CREATE INDEX idx_collectivity_structure_member ON collectivity_structure(member_id);
