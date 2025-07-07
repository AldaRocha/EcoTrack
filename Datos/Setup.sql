	USE master;
	GO

	IF EXISTS (SELECT * FROM sys.databases WHERE name = 'EcoTrack')
	BEGIN
		DROP DATABASE EcoTrack;
	END
	GO

	CREATE DATABASE EcoTrack;
	GO

	USE EcoTrack;
	GO

	CREATE TABLE [EcoTrack].[dbo].[pais](
		 PaisId				INT NOT NULL PRIMARY KEY IDENTITY(1, 1)
		,Nombre				VARCHAR(50) NOT NULL
		,Activo				TINYINT NOT NULL
	);
	GO

	INSERT INTO [EcoTrack].[dbo].[pais](
		 Nombre
		,Activo) VALUES('México', 1);

	CREATE TABLE [EcoTrack].[dbo].[persona](
		 PersonaId			INT NOT NULL PRIMARY KEY IDENTITY(1, 1)
		,Nombre				VARCHAR(45) NOT NULL
		,ApellidoPaterno	VARCHAR(45) NOT NULL
		,ApellidoMaterno	VARCHAR(45) NOT NULL
		,Celular			VARCHAR(45) NOT NULL
		,FechaNacimiento	DATETIME NOT NULL
		,Email				VARCHAR(100) NOT NULL
		,FechaRegistro		DATETIME NOT NULL
		,PaisId					INT NOT NULL

		,FOREIGN KEY (PaisId)				REFERENCES [EcoTrack].[dbo].[pais](PaisId)
	);
	GO

	INSERT INTO [EcoTrack].[dbo].[persona](
		 Nombre
		,ApellidoPaterno
		,ApellidoMaterno
		,Celular
		,FechaNacimiento
		,Email
		,FechaRegistro
		,PaisId) VALUES('Francisco Javier', 'Rocha', 'Aldana', '4778594709', '2000-10-02T13:32:54', 'rochaaldanafcojavier@gmail.com', '2025-07-04T18:58:14', (SELECT MAX(PaisId) FROM [EcoTrack].[dbo].[pais]));
	GO

	CREATE TABLE [EcoTrack].[dbo].[tipousuario](
		 TipoUsuarioId			INT NOT NULL PRIMARY KEY IDENTITY(1, 1)
		,Nombre					VARCHAR(45) NOT NULL
		,Expresion				VARCHAR(150) NOT NULL
		,ComentarioExpresion	VARCHAR(200) NOT NULL
		,Activo					TINYINT NOT NULL
	);
	GO

	INSERT INTO [EcoTrack].[dbo].[tipousuario](
		 Nombre
		,Expresion
		,ComentarioExpresion
		,Activo) VALUES('Super Administrador', '((?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,30})', 'La contraseña debe tener mínimo 6 caracteres y máximo 30. Debe contener al menos un letra mayúscula, una minúscula y un número.', 1);
	GO

	CREATE TABLE [EcoTrack].[dbo].[usuario](
		 UsuarioId				INT NOT NULL PRIMARY KEY IDENTITY(1, 1)
		,Cuenta					VARCHAR(45) NOT NULL
		,Contrasena				VARCHAR(45) NOT NULL
		,ReiniciarContrasena	TINYINT NOT NULL
		,Terminos				TINYINT NOT NULL
		,Activo					TINYINT NOT NULL
		,PersonaId				INT NOT NULL
		,TipoUsuarioId			INT NOT NULL

		,FOREIGN KEY (PersonaId)			REFERENCES [EcoTrack].[dbo].[persona](PersonaId)
		,FOREIGN KEY (TipoUsuarioId)		REFERENCES [EcoTrack].[dbo].[tipousuario](TipoUsuarioId)
	);
	GO

	INSERT INTO [EcoTrack].[dbo].[usuario](
		 Cuenta
		,Contrasena
		,ReiniciarContrasena
		,Terminos
		,Activo
		,PersonaId
		,TipoUsuarioId) VALUES('Alda.ts', '0DE5DC91DE1E8FC92B13748651099D32', 1, 1, 1, (SELECT MAX(PersonaId) FROM [EcoTrack].[dbo].[persona]), (SELECT MIN(TipoUsuarioId) FROM [EcoTrack].[dbo].[tipousuario]));
	GO

	CREATE TABLE [EcoTrack].[dbo].[sesion](
		 SesionId		INT NOT NULL PRIMARY KEY IDENTITY(1, 1)
		,Token			TEXT
		,Inicia			DATETIME NOT NULL
		,Finaliza		DATETIME
		,UsuarioId		INT NOT NULL

		,FOREIGN KEY (UsuarioId)	REFERENCES [EcoTrack].[dbo].[usuario](UsuarioId)
	);
	GO

	CREATE TABLE [EcoTrack].[dbo].[perfil](
		 PerfilId				INT NOT NULL PRIMARY KEY IDENTITY(1, 1)
		,Nombre					VARCHAR(45) NOT NULL
		,TiempoSesion			INT NOT NULL
		,TiempoInactividad		INT NOT NULL
		,SesionesSimultaneas	TINYINT NOT NULL
		,Activo					TINYINT NOT NULL
	);
	GO

	INSERT INTO [EcoTrack].[dbo].[perfil](
		 Nombre
		,TiempoSesion
		,TiempoInactividad
		,SesionesSimultaneas
		,Activo) VALUES('Super Administrador', 420, 15, 1, 1);
	GO

	CREATE TABLE [EcoTrack].[dbo].[usuarioporperfil](
		 UsuarioporperfilId		INT NOT NULL PRIMARY KEY IDENTITY(1, 1)
		,UsuarioId				INT NOT NULL
		,PerfilId				INT NOT NULL

		,FOREIGN KEY (UsuarioId)	REFERENCES [EcoTrack].[dbo].[usuario](UsuarioId)
		,FOREIGN KEY (PerfilId)		REFERENCES [EcoTrack].[dbo].[perfil](PerfilId)
	);
	GO

	INSERT INTO [EcoTrack].[dbo].[usuarioporperfil](
		 UsuarioId
		,PerfilId) VALUES((SELECT MAX(UsuarioId) FROM [EcoTrack].[dbo].[usuario]), (SELECT MAX(PerfilId) FROM [EcoTrack].[dbo].[perfil]));
	GO