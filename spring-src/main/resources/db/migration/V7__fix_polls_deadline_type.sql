-- polls.deadline was created as TEXT but the JPA entity maps it as Instant (TIMESTAMPTZ).
-- Cast existing values (ISO-8601 strings or NULL) and change the column type.
ALTER TABLE polls
    ALTER COLUMN deadline TYPE TIMESTAMPTZ
    USING CASE WHEN deadline IS NULL THEN NULL ELSE deadline::TIMESTAMPTZ END;
