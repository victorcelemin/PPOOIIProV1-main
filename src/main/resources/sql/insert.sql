-- Insertar un Conductor (tipo_persona = 'C')
INSERT INTO persona (identificacion, tipo_identificacion, nombres, apellidos, correo_electronico, tipo_persona)
VALUES ('1102', 'CC', 'Victor M', 'Celemin', 'vmcelemin@ut.edu.co', 'A');
INSERT INTO persona (identificacion, tipo_identificacion, nombres, apellidos, correo_electronico, tipo_persona)
VALUES ('1103', 'CC', 'Jose A', 'Melo R', 'jamelor@ut.edu.co', 'C');
INSERT INTO persona (identificacion, tipo_identificacion, nombres, apellidos, correo_electronico, tipo_persona)
VALUES ('1104', 'CC', 'Juan E', 'Mejia V', 'jemejiav@ut.edu.co', 'A');
INSERT INTO persona (identificacion, tipo_identificacion, nombres, apellidos, correo_electronico, tipo_persona)
VALUES ('1105', 'CC', 'Breiner E', 'Trujillo', 'betrujillo@ut.edu.co', 'C');


INSERT INTO usuario (login, idpersona, password, apikey)
VALUES ('vmc', 1, '12345678', '123456789');
INSERT INTO usuario (login, idpersona, password, apikey)
VALUES ('jam', 2, '12345678', '123456789');
INSERT INTO usuario (login, idpersona, password, apikey)
VALUES ('jem', 3, '12345678', '123456789');
INSERT INTO usuario (login, idpersona, password, apikey)
VALUES ('bet', 4, '12345678', '123456789');

