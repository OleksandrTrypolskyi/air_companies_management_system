insert into air_company (company_type, founded_at, name) VALUES ('Big company', now(), 'British Air Company');
insert into air_company (company_type, founded_at, name) VALUES ('Small company', now(), 'France Air Company');

insert into airplane (created_at, factory_serial_number, flight_distance, fuel_capacity, name, number_of_flights,
                      type) VALUES (now(), '123', '100', '10', 'Boing 747', 5, 'Passenger');
insert into airplane (created_at, factory_serial_number, flight_distance, fuel_capacity, name, number_of_flights,
                      type) VALUES (now(), '456', '200', '20', 'Boing 1000', 10, 'Military');

insert into flight (created_at, departure_country, destination_country, distance, ended_at,
                    flight_status, estimated_flight_time, started_at)
                    VALUES (date('2022-1-1 10:0:0'), 'USA', 'UK', 1000.0, date('2022-3-2 10:0:0'),
                            'COMPLETED', 24, date('2022-3-1 10:0:0'));