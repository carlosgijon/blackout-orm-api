CREATE TABLE tasks (
    id VARCHAR(255) PRIMARY KEY,
    band_id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'TODO',
    priority VARCHAR(50) NOT NULL DEFAULT 'MEDIUM',
    assignee_id VARCHAR(255),
    created_by VARCHAR(255) NOT NULL,
    due_date TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_tasks_band_id ON tasks(band_id);
CREATE INDEX idx_tasks_status ON tasks(status);
