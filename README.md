# corso-ktor-user-service

## Database Schema

```sql
create schema if not exists app;

create type app.account_type as enum ('ADMIN', 'USER', 'SUPPORT');

create table if not exists app.app_user(
  id uuid primary key,
  email varchar not null unique,
  type app.account_type not null default 'USER',
  password varchar not null,
  is_active boolean not null default false,
  first_name varchar not null,
  last_name varchar not null
);

create table if not exists app.confirmation_code(
  email varchar not null,
  code integer not null,
  primary key (email, code)
);
```
