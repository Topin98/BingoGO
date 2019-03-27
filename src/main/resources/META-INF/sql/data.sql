insert into roles (nombre) values ("ROLE_USER");
insert into roles (nombre) values ("ROLE_ADMIN");
insert into usuarios (nombre, correo, password, puntuacionTotal, fichas, fechaRegistro, enabled, imagenPerfil) values ("Dani", "correo@gmail.com", "$2a$10$qwe.rdhx61TNOIEEIbPUoOBSWbwunggHgjHV4xBVcKEWXpA1yddP6", 50, 20, current_date(), true, null);
insert into usuarios (nombre, correo, password, puntuacionTotal, fichas, fechaRegistro, enabled, imagenPerfil) values ("Alex", "correo2@gmail.com", "$2a$10$j1QpeH0Qr8DXLPGsW9CD.ep4dW.gYiWVDTpbqf2qOJo37X4u2bYke", 10, 60, current_date(), true, null);
insert into linUsuRol (rol_idRol, usuario_idUsuario) values (1,1);
insert into linUsuRol (rol_idRol, usuario_idUsuario) values (2,1);
insert into linUsuRol (rol_idRol, usuario_idUsuario) values (1,2)
insert into linUsuUsu (usuario1_idUsuario, usuario2_idUsuario, activo) values (1,2,true);
insert into salas (nombre, idPropietario, activa, publica) values ("Sala1", 1, true, true);
insert into salas (nombre, idPropietario, activa, publica) values ("Sala2", 1, false, true);
insert into salas (nombre, idPropietario, activa, publica, password) values ("Sala3", 1, true, false, "$2a$10$qwe.rdhx61TNOIEEIbPUoOBSWbwunggHgjHV4xBVcKEWXpA1yddP6");
insert into premios (nombre, descripcion, precio, imagen) values ("Camiseta", "Hecha de tela", 5, null);
insert into linUsuPre (idUsuario, idPremio, fecha) values (1, 1, current_date());
insert into partidas (fecha) values (current_date());
insert into linUsuPar (usuario_idUsuario, partida_idPartida, posicionLinea, posicionBingo) values (1, 1, 1, 1);
insert into linUsuPar (usuario_idUsuario, partida_idPartida, posicionLinea, posicionBingo) values (2, 1, 2, 2);