INSERT INTO estudiante (rut, apellidos, nombres, nacimiento, tipocolegio, nombrecolegio, añoegresocolegio, tipodepago) VALUES
('20,984,912-7', 'Perez Soto', 'Juan', '1995-06-12', 'Municipal', 'Liceo A-1', '2019', 'Cuotas'),
('20,775,416-5', 'Martinez Rojas', 'Maria', '1996-08-22', 'Particular', 'Colegio Santa Clara', '2020', 'Cuotas'),
('19,234,887-1', 'Orellana Diaz', 'Carlos', '1994-11-10', 'Subvencionado', 'Instituto Nacional', '2022', 'Contado'),
('18,456,321-9', 'Lopez Ramirez', 'Paula', '1995-02-05', 'Municipal', 'Liceo B-12', '2021', 'Cuotas'),
('17,654,789-0', 'Gonzalez Blanco', 'Pedro', '1996-03-15', 'Particular', 'Colegio San Ignacio', '2020', 'Cuotas'),
('21,234,567-8', 'Figueroa Castro', 'Isabel', '1994-10-07', 'Subvencionado', 'Liceo D-4', '2019', 'Cuotas'),
('20,333,444-2', 'Valdes Sandoval', 'Rodrigo', '1995-05-28', 'Municipal', 'Liceo C-2', '2018', 'Cuotas'),
('19,888,777-6', 'Guerrero Pinto', 'Cecilia', '1996-09-19', 'Particular', 'Colegio Los Alpes', '2022', 'Cuotas'),
('18,999,111-4', 'Solar Espinoza', 'Manuel', '1994-12-31', 'Subvencionado', 'Instituto OHiggins', '2021', 'Cuotas'),
('21,111,222-3', 'Villanueva Parra', 'Daniela', '1995-04-04', 'Municipal', 'Liceo E-5', '2020', 'Contado');

INSERT INTO cuota_pago (id_estudiante, monto, monto_base, estado, tipo, vencimiento) 
VALUES 
((SELECT id FROM estudiante WHERE rut = '20,984,912-7'), 70000.00, 70000.00, false, 'Matricula', NULL),
((SELECT id FROM estudiante WHERE rut = '20,775,416-5'), 70000.00, 70000.00, false, 'Matricula', NULL),
((SELECT id FROM estudiante WHERE rut = '19,234,887-1'), 70000.00, 70000.00, false, 'Matricula', NULL),
((SELECT id FROM estudiante WHERE rut = '18,456,321-9'), 70000.00, 70000.00, false, 'Matricula', NULL),
((SELECT id FROM estudiante WHERE rut = '17,654,789-0'), 70000.00, 70000.00, false, 'Matricula', NULL),
((SELECT id FROM estudiante WHERE rut = '21,234,567-8'), 70000.00, 70000.00, false, 'Matricula', NULL),
((SELECT id FROM estudiante WHERE rut = '20,333,444-2'), 70000.00, 70000.00, false, 'Matricula', NULL),
((SELECT id FROM estudiante WHERE rut = '19,888,777-6'), 70000.00, 70000.00, false, 'Matricula', NULL),
((SELECT id FROM estudiante WHERE rut = '18,999,111-4'), 70000.00, 70000.00, false, 'Matricula', NULL),
((SELECT id FROM estudiante WHERE rut = '21,111,222-3'), 70000.00, 70000.00, false, 'Matricula', NULL);

