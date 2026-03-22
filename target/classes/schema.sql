-- ============================================================
-- Purohit Darpan — Complete Database Schema
-- MySQL 8 | ENGINE=InnoDB | utf8mb4_unicode_ci
-- ============================================================

-- Drop all tables in reverse dependency order (for clean restarts in dev)
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS user_notification_preferences;
DROP TABLE IF EXISTS notification_logs;
DROP TABLE IF EXISTS user_saved_events;
DROP TABLE IF EXISTS panchang_cache;
DROP TABLE IF EXISTS hindu_festivals;
DROP TABLE IF EXISTS pdf_annotations;
DROP TABLE IF EXISTS video_bookmarks;
DROP TABLE IF EXISTS user_highlights;
DROP TABLE IF EXISTS user_step_notes;
DROP TABLE IF EXISTS user_puja_progress;
DROP TABLE IF EXISTS ai_feedback;
DROP TABLE IF EXISTS ai_query_logs;
DROP TABLE IF EXISTS step_samagri;
DROP TABLE IF EXISTS step_mantras;
DROP TABLE IF EXISTS resources;
DROP TABLE IF EXISTS puja_steps;
DROP TABLE IF EXISTS samagri;
DROP TABLE IF EXISTS mantras;
DROP TABLE IF EXISTS pujas;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = 'utf8mb4_unicode_ci';

-- -------------------------------------------------------
-- 1. USERS
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    username        VARCHAR(100)    NOT NULL UNIQUE,
    email           VARCHAR(255)    NOT NULL UNIQUE,
    password_hash   VARCHAR(255)    NOT NULL,
    full_name       VARCHAR(200)    NOT NULL,
    role            ENUM('STUDENT','MENTOR','ADMIN') NOT NULL DEFAULT 'STUDENT',
    profile_picture VARCHAR(500),
    preferred_format ENUM('DOC','VIDEO','PDF') DEFAULT 'DOC',
    fcm_token       VARCHAR(500),
    notifications_enabled BOOLEAN   DEFAULT TRUE,
    is_active       BOOLEAN         DEFAULT TRUE,
    last_login      TIMESTAMP,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- 2. PUJAS
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS pujas (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    name            VARCHAR(300)    NOT NULL,
    name_devanagari VARCHAR(300),
    description     TEXT,
    duration_minutes INT,
    difficulty      ENUM('BEGINNER','INTERMEDIATE','ADVANCED') DEFAULT 'BEGINNER',
    category        VARCHAR(200),
    thumbnail_url   VARCHAR(500),
    is_active       BOOLEAN         DEFAULT TRUE,
    created_by      BIGINT,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_pujas_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- 3. PUJA STEPS
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS puja_steps (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    puja_id         BIGINT          NOT NULL,
    step_order      INT             NOT NULL,
    title           VARCHAR(300)    NOT NULL,
    title_devanagari VARCHAR(300),
    description     MEDIUMTEXT,
    video_url       VARCHAR(500),
    video_transcript LONGTEXT,
    pdf_resource_id BIGINT,
    duration_seconds INT,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_steps_puja FOREIGN KEY (puja_id) REFERENCES pujas(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- 4. MANTRAS
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS mantras (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    shlok_text      TEXT            NOT NULL COMMENT 'Full shlok in Devanagari',
    transliteration TEXT,
    word_meanings   JSON            COMMENT 'JSON: [{word, meaning, grammatical_role}]',
    audio_url       VARCHAR(500),
    source_text     VARCHAR(300)    COMMENT 'e.g. Rigveda 1.1.1',
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- 5. STEP_MANTRAS (join)
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS step_mantras (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    step_id         BIGINT          NOT NULL,
    mantra_id       BIGINT          NOT NULL,
    sequence_order  INT             NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    CONSTRAINT fk_sm_step   FOREIGN KEY (step_id)   REFERENCES puja_steps(id) ON DELETE CASCADE,
    CONSTRAINT fk_sm_mantra FOREIGN KEY (mantra_id) REFERENCES mantras(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- 6. SAMAGRI (ritual items)
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS samagri (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    name            VARCHAR(300)    NOT NULL,
    name_devanagari VARCHAR(300),
    description     TEXT,
    image_url       VARCHAR(500),
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- 7. STEP_SAMAGRI (join)
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS step_samagri (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    step_id         BIGINT          NOT NULL,
    samagri_id      BIGINT          NOT NULL,
    quantity        VARCHAR(100),
    notes           VARCHAR(500),
    PRIMARY KEY (id),
    CONSTRAINT fk_ss_step    FOREIGN KEY (step_id)    REFERENCES puja_steps(id) ON DELETE CASCADE,
    CONSTRAINT fk_ss_samagri FOREIGN KEY (samagri_id) REFERENCES samagri(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- 8. RESOURCES (PDF books, assets)
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS resources (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    puja_id         BIGINT,
    title           VARCHAR(300)    NOT NULL,
    resource_type   ENUM('PDF','AUDIO','IMAGE','VIDEO') NOT NULL DEFAULT 'PDF',
    file_url        VARCHAR(500)    NOT NULL,
    file_size_kb    INT,
    page_count      INT,
    table_of_contents JSON          COMMENT 'JSON: [{page, title}]',
    is_downloadable BOOLEAN         DEFAULT FALSE,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_res_puja FOREIGN KEY (puja_id) REFERENCES pujas(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add FK from puja_steps to resources (after resources is created)
ALTER TABLE puja_steps
    ADD CONSTRAINT fk_steps_pdf_resource
    FOREIGN KEY (pdf_resource_id) REFERENCES resources(id) ON DELETE SET NULL;

-- -------------------------------------------------------
-- 9. AI QUERY LOGS
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS ai_query_logs (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    query_type      ENUM('WORD_QUERY','SHLOK_QUERY','GENERAL_QUESTION') NOT NULL,
    query_text      TEXT            NOT NULL,
    context_puja_id BIGINT,
    context_step_id BIGINT,
    context_shlok   TEXT,
    response_text   LONGTEXT,
    tokens_used     INT,
    response_time_ms INT,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_aql_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- 10. AI FEEDBACK
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS ai_feedback (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    query_log_id    BIGINT          NOT NULL,
    user_id         BIGINT          NOT NULL,
    rating          TINYINT         NOT NULL COMMENT '1=thumbs up, -1=thumbs down',
    comment         TEXT,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_af_log  FOREIGN KEY (query_log_id) REFERENCES ai_query_logs(id) ON DELETE CASCADE,
    CONSTRAINT fk_af_user FOREIGN KEY (user_id)      REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- 11. USER PUJA PROGRESS
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS user_puja_progress (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    puja_id         BIGINT          NOT NULL,
    step_id         BIGINT          NOT NULL,
    format          ENUM('DOC','VIDEO','PDF') NOT NULL DEFAULT 'DOC',
    completed       BOOLEAN         DEFAULT FALSE,
    last_accessed   TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_progress (user_id, puja_id, step_id, format),
    CONSTRAINT fk_upp_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_upp_puja FOREIGN KEY (puja_id) REFERENCES pujas(id) ON DELETE CASCADE,
    CONSTRAINT fk_upp_step FOREIGN KEY (step_id) REFERENCES puja_steps(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- 12. USER STEP NOTES
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS user_step_notes (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    step_id         BIGINT          NOT NULL,
    note_text       TEXT            NOT NULL,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_usn_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_usn_step FOREIGN KEY (step_id) REFERENCES puja_steps(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- 13. USER HIGHLIGHTS
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS user_highlights (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    step_id         BIGINT          NOT NULL,
    highlighted_text TEXT           NOT NULL,
    start_offset    INT             NOT NULL,
    end_offset      INT             NOT NULL,
    color           VARCHAR(20)     DEFAULT '#FFD700',
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_uh_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_uh_step FOREIGN KEY (step_id) REFERENCES puja_steps(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- 14. VIDEO BOOKMARKS
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS video_bookmarks (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    step_id         BIGINT          NOT NULL,
    video_url       VARCHAR(500)    NOT NULL,
    timestamp_seconds INT           NOT NULL,
    label           VARCHAR(200),
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_vb_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_vb_step FOREIGN KEY (step_id) REFERENCES puja_steps(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- 15. PDF ANNOTATIONS
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS pdf_annotations (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    resource_id     BIGINT          NOT NULL,
    page_number     INT             NOT NULL,
    annotation_text TEXT,
    position_x      FLOAT           NOT NULL DEFAULT 0,
    position_y      FLOAT           NOT NULL DEFAULT 0,
    color           VARCHAR(20)     DEFAULT '#FFD700',
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_pa_user    FOREIGN KEY (user_id)    REFERENCES users(id)     ON DELETE CASCADE,
    CONSTRAINT fk_pa_resource FOREIGN KEY (resource_id) REFERENCES resources(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- 16. HINDU FESTIVALS
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS hindu_festivals (
    id                          BIGINT          NOT NULL AUTO_INCREMENT,
    name                        VARCHAR(300)    NOT NULL,
    name_devanagari             VARCHAR(300),
    description                 TEXT,
    puja_id                     BIGINT,
    event_date                  DATE            NOT NULL,
    days_duration               INT             NOT NULL DEFAULT 1,
    notification_days_before    INT             NOT NULL DEFAULT 20,
    is_active                   BOOLEAN         DEFAULT TRUE,
    created_at                  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_hf_puja FOREIGN KEY (puja_id) REFERENCES pujas(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- 17. PANCHANG CACHE
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS panchang_cache (
    id                      BIGINT          NOT NULL AUTO_INCREMENT,
    date                    DATE            NOT NULL UNIQUE,
    tithi                   VARCHAR(100),
    nakshatra               VARCHAR(100),
    yoga                    VARCHAR(100),
    karana                  VARCHAR(100),
    vara                    VARCHAR(100)    COMMENT 'Day of week in Sanskrit',
    rahu_start              TIME,
    rahu_end                TIME,
    gulika_start            TIME,
    gulika_end              TIME,
    yamghant_start          TIME,
    yamghant_end            TIME,
    abhijit_start           TIME,
    abhijit_end             TIME,
    brahma_muhurta_start    TIME,
    brahma_muhurta_end      TIME,
    sunrise                 TIME,
    sunset                  TIME,
    is_purnima              BOOLEAN         DEFAULT FALSE,
    is_amavasya             BOOLEAN         DEFAULT FALSE,
    is_ekadashi             BOOLEAN         DEFAULT FALSE,
    created_at              TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- 18. USER SAVED EVENTS
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS user_saved_events (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    festival_id     BIGINT          NOT NULL,
    reminder_set    BOOLEAN         DEFAULT TRUE,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_user_festival (user_id, festival_id),
    CONSTRAINT fk_use_user     FOREIGN KEY (user_id)    REFERENCES users(id)           ON DELETE CASCADE,
    CONSTRAINT fk_use_festival FOREIGN KEY (festival_id) REFERENCES hindu_festivals(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- 19. NOTIFICATION LOGS
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS notification_logs (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    user_id             BIGINT          NOT NULL,
    festival_id         BIGINT,
    notification_type   ENUM('FESTIVAL_REMINDER','PUJA_PRACTICE','PANCHANG_ALERT','LEARNING_REMINDER') NOT NULL,
    title               VARCHAR(200)    NOT NULL,
    body                TEXT            NOT NULL,
    action_url          VARCHAR(500),
    sent_at             TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    delivery_status     ENUM('SENT','FAILED','DELIVERED') NOT NULL DEFAULT 'SENT',
    fcm_message_id      VARCHAR(500),
    PRIMARY KEY (id),
    CONSTRAINT fk_nl_user     FOREIGN KEY (user_id)    REFERENCES users(id)           ON DELETE CASCADE,
    CONSTRAINT fk_nl_festival FOREIGN KEY (festival_id) REFERENCES hindu_festivals(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- 20. USER NOTIFICATION PREFERENCES
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS user_notification_preferences (
    user_id                 BIGINT          NOT NULL,
    festival_reminders      BOOLEAN         DEFAULT TRUE,
    panchang_alerts         BOOLEAN         DEFAULT TRUE,
    learning_reminders      BOOLEAN         DEFAULT TRUE,
    puja_practice           BOOLEAN         DEFAULT TRUE,
    reminder_days_before    INT             DEFAULT 20,
    quiet_hours_start       TIME            DEFAULT '22:00:00',
    quiet_hours_end         TIME            DEFAULT '07:00:00',
    updated_at              TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id),
    CONSTRAINT fk_unp_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- INDEXES for performance
-- -------------------------------------------------------
-- -------------------------------------------------------
-- INDEXES for performance
-- -------------------------------------------------------
CREATE INDEX idx_puja_steps_puja_id     ON puja_steps(puja_id);
CREATE INDEX idx_step_mantras_step_id   ON step_mantras(step_id);
CREATE INDEX idx_step_samagri_step_id   ON step_samagri(step_id);
CREATE INDEX idx_ai_logs_user_id        ON ai_query_logs(user_id);
CREATE INDEX idx_ai_logs_type           ON ai_query_logs(query_type);
CREATE INDEX idx_progress_user_puja     ON user_puja_progress(user_id, puja_id);
CREATE INDEX idx_notes_user_step        ON user_step_notes(user_id, step_id);
CREATE INDEX idx_highlights_user_step   ON user_highlights(user_id, step_id);
CREATE INDEX idx_video_bm_user_step     ON video_bookmarks(user_id, step_id);
CREATE INDEX idx_pdf_ann_user_resource  ON pdf_annotations(user_id, resource_id);
CREATE INDEX idx_festivals_date         ON hindu_festivals(event_date);
CREATE INDEX idx_panchang_date          ON panchang_cache(date);
CREATE INDEX idx_notif_logs_user        ON notification_logs(user_id);
CREATE INDEX idx_notif_logs_type        ON notification_logs(notification_type);