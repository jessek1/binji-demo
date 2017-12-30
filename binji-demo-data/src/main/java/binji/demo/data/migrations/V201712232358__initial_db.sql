create sequence ovd_content_seq start 1 increment 50;
create sequence ovd_fullvideos_seq start 1 increment 50;
create sequence ovd_providers_seq start 1 increment 50;
create sequence ovd_seasons_seq start 1 increment 50;
create sequence ovd_urls_seq start 1 increment 50;
create sequence ovd_viewing_options_seq start 1 increment 50;

    create table ovd_content (
        id int8 not null,
        created timestamptz not null,
        updated timestamptz not null,
        version int8 not null,
        airDate timestamptz,
        colorCode varchar(255),
        contentType varchar(255),
        description TEXT,
        episodeNumber int8,
        feed_timestamp timestamptz,
        genre TEXT,
        originalLanguage varchar(255),
        programType varchar(255),
        rating TEXT,
        root_id int8,
        runTime int8,
        seasonNumber int8,
        seriesId varchar(255),
        title TEXT,
        tms_id varchar(255),
        year int4,
        parent_id int8,
        primary key (id)
    );

    create table ovd_data (
        ovd_data_id int8 not null,
        ovd_data_value varchar(255),
        ovd_data_key varchar(255) not null,
        primary key (ovd_data_id, ovd_data_key)
    );

    create table ovd_fullvideos (
        id int8 not null,
        created timestamptz not null,
        updated timestamptz not null,
        version int8 not null,
        availableFromDateTime timestamptz,
        duration int4,
        expiresAtDateTime timestamptz,
        studio_name TEXT,
        video_id varchar(255),
        content_id int8 not null,
        ovd_provider_id int8,
        primary key (id)
    );

    create table ovd_providers (
        id int8 not null,
        created timestamptz not null,
        updated timestamptz not null,
        version int8 not null,
        alpha_dark_image varchar(255),
        alpha_light_image varchar(255),
        name TEXT,
        provider_id int4,
        solid_dark_image varchar(255),
        solid_light_image varchar(255),
        primary key (id)
    );

    create table ovd_seasons (
        id int8 not null,
        created timestamptz not null,
        updated timestamptz not null,
        version int8 not null,
        seasonId int8,
        seasonNumber int8,
        content_id int8 not null,
        primary key (id)
    );

    create table ovd_urls (
        id int8 not null,
        created timestamptz not null,
        updated timestamptz not null,
        version int8 not null,
        type varchar(255),
        value TEXT,
        ovd_fullvideos_id int8 not null,
        primary key (id)
    );

    create table ovd_viewing_options (
        id int8 not null,
        created timestamptz not null,
        updated timestamptz not null,
        version int8 not null,
        currency varchar(255),
        license varchar(255),
        price numeric(19, 2),
        quality varchar(255),
        ovd_fullvideos_id int8 not null,
        primary key (id)
    );

    alter table ovd_content 
        add constraint FK26fip8vl81lnped687vcsgf03 
        foreign key (parent_id) 
        references ovd_content;

    alter table ovd_data 
        add constraint FKgkel65wlgkqyo7g9mw2n1qs75 
        foreign key (ovd_data_id) 
        references ovd_fullvideos;

    alter table ovd_fullvideos 
        add constraint FKsldlj4wkwjun82uubbejlfl6b 
        foreign key (content_id) 
        references ovd_content;

    alter table ovd_fullvideos 
        add constraint FK8jb4vlwoict4j4i0f2tt8qhau 
        foreign key (ovd_provider_id) 
        references ovd_providers;

    alter table ovd_seasons 
        add constraint FKaak7lynog21cq2i3smifmdcm5 
        foreign key (content_id) 
        references ovd_content;

    alter table ovd_urls 
        add constraint FK9e239n5qh2534j48rt9liaq5c 
        foreign key (ovd_fullvideos_id) 
        references ovd_fullvideos;

    alter table ovd_viewing_options 
        add constraint FKrkiddajltvtmdtth5tns6c0ty 
        foreign key (ovd_fullvideos_id) 
        references ovd_fullvideos;
