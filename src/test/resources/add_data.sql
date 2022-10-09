delete
from auth_info;

insert into auth_info(user_id, access_token, refresh_token, remote_addr)
values (1, 'some_access_token', 'some_refresh_token', '192.42.44.11');

insert into auth_info(user_id, access_token, refresh_token, remote_addr)
values (2, 'some_access_token2', 'some_refresh_token2', '245.11.654.432');