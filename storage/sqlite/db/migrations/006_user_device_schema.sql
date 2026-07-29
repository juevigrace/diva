-- +goose Up
-- +goose StatementBegin
CREATE TABLE IF NOT EXISTS diva_user_device (
    user_id TEXT NOT NULL REFERENCES diva_user(id) ON DELETE CASCADE,
    device_id TEXT NOT NULL REFERENCES diva_devices(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, device_id)
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE IF EXISTS diva_user_device;
-- +goose StatementEnd