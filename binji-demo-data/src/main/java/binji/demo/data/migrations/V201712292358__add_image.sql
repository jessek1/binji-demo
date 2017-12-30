create sequence ovd_image_seq start 1 increment 50;

create table ovd_image (
        id int8 not null,
        created timestamptz not null,
        updated timestamptz not null,
        version int8 not null,
        bg_banner varchar(255),
        detail varchar(255),
        thumb varchar(255),
        ovd_content_id int8 not null,
        primary key (id)
    );
    
alter table ovd_image 
        add constraint UK_k6aw9x5r1t6bhiuhbhc8w9ffv unique (ovd_content_id);

alter table ovd_image 
add constraint FKijbsv9undm07s5xbttb4jd4m4 
foreign key (ovd_content_id) 
references ovd_content;