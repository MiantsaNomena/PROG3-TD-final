create database fca;

create user fca_user with password '123456';

grant connect on database fca to fca_user;

\c fca

grant create on schema public to fca_user;
grant  usage , create on schema public to fca_user;
alter default privileges in schema public
    grant select, insert, update, delete on tables to fca_user;

alter default privileges in schema public
    grant usage, select, update on sequences to fca_user;