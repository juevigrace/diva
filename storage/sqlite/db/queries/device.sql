-- name: GetDeviceByName :one
select
    d.id,
    d.name,
    d.created_at,
    d.updated_at
from diva_devices d
where d.name = ?
;

-- name: GetDeviceByID :one
select
    d.id,
    d.name,
    d.created_at,
    d.updated_at
from diva_devices d
where d.id = ?
;

-- name: CreateDevice :exec
insert into diva_devices (
    id,
    name
) values (
    ?,
    ?
);
