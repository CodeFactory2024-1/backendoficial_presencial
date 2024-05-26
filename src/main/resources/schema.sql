CREATE TABLE flight (
                        id SERIAL PRIMARY KEY,
                        airline VARCHAR(255),
                        origin VARCHAR(255),
                        destination VARCHAR(255),
                        departure_date TIMESTAMP,
                        arrival_date TIMESTAMP,
                        price_seat_cost INTEGER,
                        price_flight_cost INTEGER,
                        price_baggage_cost INTEGER,
                        price_other_costs INTEGER,
                        price_total_cost INTEGER
);
