-- name: CreateUserDevice :exec
insert into diva_user_device (
    user_id,
    device_id
) values (
    ?,
    ?
);

-- name: ListUserDevices :many
select
    ud.user_id,
    ud.device_id,
    ud.created_at,
    ud.updated_at
from diva_user_device ud
where ud.user_id = ?
order by ud.updated_at desc;

-- name: DeleteUserDevice :exec
delete from diva_user_device
where user_id = ? and device_id = ?;

-- name: GetUserDevice :one
select
    ud.user_id,
    ud.device_id,
    ud.created_at,
    ud.updated_at
from diva_user_device ud
where ud.user_id = ? and ud.device_id = ?;