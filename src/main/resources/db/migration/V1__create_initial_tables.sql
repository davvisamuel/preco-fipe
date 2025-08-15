create table preco_fipe.user
(
    id       bigint auto_increment
        primary key,
    email    varchar(255) not null,
    password varchar(255) not null,
    roles    varchar(255) not null,
    constraint UKob8kqyqqgmefl0aco34akdtpe
        unique (email)
);

create table preco_fipe.vehicle_query
(
    id           bigint auto_increment
        primary key,
    brand_id     varchar(255) not null,
    model_id     varchar(255) not null,
    vehicle_type varchar(255) not null,
    year_id      varchar(255) not null
);

create table preco_fipe.favorite
(
    id               bigint auto_increment
        primary key,
    user_id          bigint not null,
    vehicle_query_id bigint not null,
    constraint UK4c76k40cepqlnx1e215crioyp
        unique (user_id, vehicle_query_id),
    constraint UKg31whdqshshp386hc475gdl4f
        unique (vehicle_query_id),
    constraint FKh3f2dg11ibnht4fvnmx60jcif
        foreign key (user_id) references preco_fipe.user (id),
    constraint FKopjyasbhrwn8n7yd4r30hwimv
        foreign key (vehicle_query_id) references preco_fipe.vehicle_query (id)
);

create table preco_fipe.history
(
    id               bigint auto_increment
        primary key,
    user_id          bigint not null,
    vehicle_query_id bigint not null,
    constraint UKbom9tpq8dlkicy963ka1drwbq
        unique (vehicle_query_id),
    constraint FKai5h1arefqiap6mi4s0jnd6l
        foreign key (vehicle_query_id) references preco_fipe.vehicle_query (id),
    constraint FKn4gjyu69m6xa5f3bot571imbe
        foreign key (user_id) references preco_fipe.user (id)
);

