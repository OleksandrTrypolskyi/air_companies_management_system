insert into air_company (company_type, founded_at, name)
VALUES ('Big company', now(), 'British Air Company');
insert into air_company (company_type, founded_at, name)
VALUES ('Small company', now(), 'France Air Company');
insert into air_company (company_type, founded_at, name)
VALUES ('Company from cold country', now(), 'Norway Air Company');



insert into airplane (created_at, factory_serial_number, flight_distance,
                      fuel_capacity, name, number_of_flights, type, air_company_id)
VALUES (now(), '123', '100', '10', 'Boing 747', 5, 'Passenger',
        (select id from air_company where name = 'British Air Company'));

insert into airplane (created_at, factory_serial_number, flight_distance,
                      fuel_capacity, name, number_of_flights, type, air_company_id)
VALUES (now(), '456', '200', '20', 'Boing 1000', 10, 'Military',
        (select id from air_company where name = 'France Air Company'));
insert into airplane (created_at, factory_serial_number, flight_distance,
                      fuel_capacity, name, number_of_flights, type, air_company_id)
VALUES (now(), '4789', '300', '30', 'Boing 900', 8, 'Transport',
        (select id from air_company where name = 'Norway Air Company'));



insert into flight (created_at, departure_country, destination_country, distance, ended_at,
                    flight_status, estimated_flight_time, started_at, air_company_id)
VALUES (time('2022-1-1 10:0:0'), 'USA', 'UK', 1000.0, time('2022-3-2 10:0:0'),
        'COMPLETED', (24 * (60 * 60000000000)), time('2022-3-1 10:0:0'),
        (select id from air_company where name = 'British Air Company'));

insert into flight (created_at, departure_country, destination_country, distance, ended_at,
                    flight_status, estimated_flight_time, started_at, air_company_id)
VALUES (time('2022-2-1 10:0:0'), 'Australia', 'Japan', 1500.0, time('2022-3-2 10:0:0'),
        'COMPLETED', (24 * (60 * 60000000000)), time('2022-3-1 10:0:0'),
        (select id from air_company where name = 'British Air Company'));

insert into flight (created_at, departure_country, destination_country, distance,
                    flight_status, estimated_flight_time, started_at, air_company_id)
VALUES (time('2022-2-1 10:0:0'), 'Germany', 'France', 700.0,
        'ACTIVE', (10 * (60 * 60000000000)), time('2022-3-21 10:0:0'),
        (select id from air_company where name = 'France Air Company'));

insert into flight (created_at, departure_country, destination_country, distance,
                    flight_status, estimated_flight_time, started_at, air_company_id)
VALUES (time('2022-2-1 10:0:0'), 'Spain', 'Italy', 300.0,
        'ACTIVE', (4 * (60 * 60000000000)), time('2022-3-21 12:0:0'),
        (select id from air_company where name = 'France Air Company'));

insert into flight (created_at, departure_country, destination_country, distance, ended_at,
                    flight_status, estimated_flight_time, started_at, air_company_id)
VALUES (time('2022-2-1 10:0:0'), 'UK', 'France', 800.0, time('2022-3-21 20:0:0'),
        'COMPLETED', (6 * (60 * 60000000000)), time('2022-3-21 10:0:0'),
        (select id from air_company where name = 'France Air Company'));

insert into flight (created_at, departure_country, destination_country, distance, ended_at,
                    flight_status, estimated_flight_time, started_at, air_company_id)
VALUES (time('2022-2-1 10:0:0'), 'Spain', 'Germany', 600.0, time('2022-3-21 18:0:0'),
        'COMPLETED', (4 * (60 * 60000000000)), time('2022-3-21 12:0:0'),
        (select id from air_company where name = 'France Air Company'));