INSERT INTO fuel (fuel, fuel_acronym)
VALUES ('Gasolina', 'G');

INSERT INTO preco_fipe.vehicle_data (code_fipe, fuel_id, brand, model, model_year, vehicle_type)
VALUES ('038003-2', (SELECT id FROM fuel WHERE fuel_acronym = 'G' LIMIT 1), 'Acura', 'Integra GS 1.8', 1992, 'Carro');

INSERT INTO preco_fipe.favorite (user_id, vehicle_data_id)
VALUES ((SELECT id FROM user WHERE email = 'admin@example.com' LIMIT 1),(SELECT id FROM vehicle_data WHERE code_fipe = '038003-2' AND model_year = 1992 LIMIT 1));