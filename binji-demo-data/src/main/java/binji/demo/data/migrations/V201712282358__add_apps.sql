create sequence apps_seq start 1 increment 50;

create table apps (
        id int8 not null,
        created timestamptz not null,
        updated timestamptz not null,
        version int8 not null,
        description varchar(2083),
        genres TEXT,
        icon_img varchar(2083),
        sortOrder int4,
        title varchar(255),
        url varchar(2083),
        primary key (id)
    );