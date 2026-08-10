-- name: GetSessionByID :one
select
    s.id,
    s.user_id,
    s.access_token,
    s.refresh_token,
    s.device_id,
    s.type,
    s.status,
    s.ip_address,
    s.user_agent,
    s.access_expires_at,
    s.refresh_expires_at,
    s.created_at,
    s.updated_at,
    s.deleted_at
from diva_session s
where s.id = ?
;

-- name: ListSessions :many
select
    s.id,
    s.user_id,
    s.access_token,
    s.refresh_token,
    s.device_id,
    s.type,
    s.status,
    s.ip_address,
    s.user_agent,
    s.access_expires_at,
    s.refresh_expires_at,
    s.created_at,
    s.updated_at,
    s.deleted_at
from diva_session s
order by created_at desc
;

-- name: ListSessionsByUser :many
select
    s.id,
    s.user_id,
    s.access_token,
    s.refresh_token,
    s.device_id,
    s.type,
    s.status,
    s.ip_address,
    s.user_agent,
    s.access_expires_at,
    s.refresh_expires_at,
    s.created_at,
    s.updated_at,
    s.deleted_at
from diva_session s
where s.user_id = ? and deleted_at is null
order by created_at desc
;

-- name: CreateSession :exec
insert into diva_session (
    id,
    user_id,
    access_token,
    refresh_token,
    device_id,
    status,
    type,
    ip_address,
    user_agent,
    access_expires_at,
    refresh_expires_at
) values (
    ?,
    ?,
    ?,
    ?,
    ?,
    ?,
    ?,
    ?,
    ?,
    ?,
    ?
);

-- name: UpdateSession :exec
update diva_session set
    access_token = ?,
    refresh_token = ?,
    ip_address = ?,
    access_expires_at = ?,
    refresh_expires_at = ?,
    updated_at = CURRENT_TIMESTAMP
where id = ?;

-- name: UpdateSessionStatus :exec
update diva_session set
    status = ?,
    updated_at = CURRENT_TIMESTAMP
where id = ?;

-- name: CloseExpiredSessions :exec
update diva_session set
    status = 'CLOSED',
    updated_at = CURRENT_TIMESTAMP
where refresh_expires_at < CURRENT_TIMESTAMP and status != 'CLOSED'
;

-- name: CloseAllByUser :exec
update diva_session set
    status = 'CLOSED',
    updated_at = CURRENT_TIMESTAMP
where user_id = ? and status = 'ACTIVE'
;

-- name: SoftDeleteSession :exec
update diva_session set
    deleted_at = CURRENT_TIMESTAMP
where id = ?
;

-- name: DeleteSessionsForever :exec
delete from diva_session
where deleted_at is not null
;
