create table if not exists person
(
    id int generated by default as identity primary key,
    full_name varchar(100) not null unique,
    email varchar(100) unique,
    role varchar(100),
    description varchar(300),
    year_of_birth date,
    password varchar(100),
    chief_id int
    );
create table if not exists task
(
    id int generated by default as identity primary key,
    task_name varchar(200) not null,
    priority int not null,
    status boolean not null,
    created_at timestamp,
    deadline timestamp,
    execution_at timestamp,
    person_id int references person(id) on delete set null,
    description varchar(300),
    report varchar(300)
    );
insert into person(full_name,role,email,chief_id,password) values ('admin', 'ROLE_ADMIN', 'admin@test.ru',1,'$2a$10$8VPdrBLeEq4cELlw5h76SeSoF4XPABfQ1gmg2Qo6KMUU8qHC3p22S');
