CREATE TABLE activity (
                          id VARCHAR(36) PRIMARY KEY,
                          collectivity_id VARCHAR(36) REFERENCES collectivity(id) ON DELETE CASCADE,
                          label VARCHAR(255) NOT NULL,
                          activity_type VARCHAR(20) NOT NULL CHECK (activity_type IN ('MEETING', 'TRAINING', 'OTHER')),
                          executive_date DATE,
                          recurrence_rule JSONB,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE attendance (
                            id VARCHAR(36) PRIMARY KEY,
                            activity_id VARCHAR(36) REFERENCES activity(id) ON DELETE CASCADE,
                            member_id VARCHAR(36) REFERENCES member(id) ON DELETE CASCADE,
                            status VARCHAR(20) NOT NULL CHECK (status IN ('UNDEFINED', 'ATTENDED', 'MISSING')),
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            CONSTRAINT unique_activity_member UNIQUE (activity_id, member_id)
);