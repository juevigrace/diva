-- name: CreateUserDevice :exec
insert into diva_user_device (
    user_id,
    device_id
) values (
    $1,
    $2
);

-- name: ListUserDevices :many
select
    ud.user_id,
    ud.device_id,
    ud.created_at,
    ud.updated_at
from diva_user_device ud
where ud.user_id = $1
order by ud.updated_at desc;

-- name: DeleteUserDevice :exec
delete from diva_user_device
where user_id = $1 and device_id = $2;

-- name: GetUserDevice :one
select
    ud.user_id,
    ud.device_id,
    ud.created_at,
    ud.updated_at
from diva_user_device ud
where ud.user_id = $1 and ud.device_id = $2;