-- name: GetDeviceByName :one
select
    d.id,
    d.name,
    d.created_at,
    d.updated_at
from diva_devices d
where d.name = $1
;

-- name: GetDeviceByID :one
select
    d.id,
    d.name,
    d.created_at,
    d.updated_at
from diva_devices d
where d.id = $1
;

-- name: ListAllDevices :many
select
    d.id,
    d.name,
    d.created_at,
    d.updated_at
from diva_devices d
order by d.name asc;

-- name: CreateDevice :exec
insert into diva_devices (
    id,
    name
) values (
    $1,
    $2
);
