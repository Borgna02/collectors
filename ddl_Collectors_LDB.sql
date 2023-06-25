DROP DATABASE IF EXISTS collectors;

CREATE DATABASE collectors;
USE collectors;

DROP USER IF EXISTS 'collectorsUser'@'localhost';
CREATE USER 'collectorsUser'@'localhost' IDENTIFIED BY 'collectorsPwd';
GRANT select,insert,update,delete,execute ON collectors.* TO 'collectorsUser'@'localhost';

CREATE TABLE Collezionista (
  ID INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nickname VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE Collezione (
  ID INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(50) NOT NULL,
  isPubblica BOOLEAN NOT NULL,
  id_Collezionista INT UNSIGNED NOT NULL,
  CONSTRAINT collezione_distinta UNIQUE (nome, id_Collezionista),
  CONSTRAINT collezione_collezionista FOREIGN KEY (id_Collezionista) REFERENCES Collezionista(ID) ON DELETE CASCADE ON UPDATE CASCADE 
);

CREATE TABLE Etichetta (
  nome VARCHAR(50) PRIMARY KEY
);

CREATE TABLE Genere (
  nome VARCHAR(50) PRIMARY KEY
);

CREATE TABLE Formato (
  nome VARCHAR(50) PRIMARY KEY
);

CREATE TABLE Disco (
  ID INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  EAN CHAR(13) UNIQUE NOT NULL, #EAN-13 ha una lunghezza fissa di 13 caratteri
  titolo VARCHAR(50) NOT NULL,
  anno SMALLINT UNSIGNED NOT NULL,
  nome_Etichetta VARCHAR(50) NOT NULL,
  nome_Formato VARCHAR(50) NOT NULL,
  id_Autore INT UNSIGNED NOT NULL,
  CONSTRAINT disco_distinto UNIQUE (titolo, anno, nome_Etichetta, nome_Formato, id_Autore), #Non ha senso che esistano dischi uguali con EAN distinti
  CONSTRAINT disco_etichetta FOREIGN KEY (nome_Etichetta) REFERENCES Etichetta(nome) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT disco_formato FOREIGN KEY (nome_Formato) REFERENCES Formato(nome) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT controllo_anno CHECK (anno > 1930 AND anno < 2100) #Nel 1931, la casa discografica Rca-Victor distribuì il primo Lp della storia: la Quinta sinfonia di Beethoven.
);

CREATE TABLE classifica (
	id_Disco INT UNSIGNED,
  nome_Genere VARCHAR(50),
  PRIMARY KEY (id_Disco, nome_Genere),
  CONSTRAINT classifica_disco FOREIGN KEY (id_Disco) REFERENCES Disco(ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT classifica_genere FOREIGN KEY (nome_Genere) REFERENCES Genere(nome) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Stato (
  nome VARCHAR(50) PRIMARY KEY
);

CREATE TABLE Copia (
  ID INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  SN CHAR(12) UNIQUE,
  doppioni INT UNSIGNED NOT NULL DEFAULT 1, #Quando si inserisce una copia, il conteggio viene chiaramente inizializzato a 1 (ovvero la copia stessa)
  id_Disco INT UNSIGNED NOT NULL,
  nome_Stato VARCHAR(50) NOT NULL,
  id_Collezione INT UNSIGNED NOT NULL,
  CONSTRAINT copia_disco FOREIGN KEY (id_Disco) REFERENCES Disco(ID) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT copia_stato FOREIGN KEY (nome_Stato) REFERENCES Stato(nome) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT copia_collezione FOREIGN KEY (id_Collezione) REFERENCES Collezione(ID) ON DELETE CASCADE ON UPDATE CASCADE 
  #Facendo riferimento all'assunzione 1, essendo la copia strettamente collegata alla collezione, ritengo corretto eliminare
  #le copie di una collezione quando essa viene eliminata.
);

CREATE TABLE Autore (
  ID INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nomeDarte VARCHAR(100) UNIQUE NOT NULL,
  nome VARCHAR(100),
  cognome VARCHAR(100),
  isGruppo BOOLEAN NOT NULL
);

-- Controllo sul vincolo tra nome, cognome e isGruppo
DELIMITER $$
CREATE TRIGGER AggiuntaAutore BEFORE INSERT ON Autore FOR EACH ROW
BEGIN
	IF NEW.nome IS NOT NULL AND NEW.cognome IS NOT NULL AND NEW.isGruppo = TRUE THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Se nome e cognome non sono nulli, autore deve essere un singolo';
	END IF;
END$$
DELIMITER ;


CREATE TABLE Traccia (
  ID INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  titolo VARCHAR(100) NOT NULL,
  ISRC CHAR(12) UNIQUE NOT NULL, #codice ISRC composto da 12 cifre alfanumeriche
  Durata TIME NOT NULL
);

CREATE TABLE Posizione (
  nome VARCHAR(50) PRIMARY KEY
);

CREATE TABLE Immagine (
  ID INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  URL VARCHAR(2083),
  id_Disco INT UNSIGNED NOT NULL,
  nome_Posizione VARCHAR(50) NOT NULL,
  CONSTRAINT immagine_distinta UNIQUE (id_Disco, nome_Posizione),
  CONSTRAINT immagine_disco FOREIGN KEY (id_Disco) REFERENCES Disco(ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT immagine_posizione FOREIGN KEY (nome_Posizione) REFERENCES Posizione(nome) ON DELETE NO ACTION ON UPDATE CASCADE
);

CREATE TABLE Tipo (
  nome VARCHAR(50) PRIMARY KEY
);

CREATE TABLE condivide (
  id_Collezionista INT UNSIGNED,
  id_Collezione INT UNSIGNED,
  PRIMARY KEY (id_Collezionista, id_Collezione),
  CONSTRAINT condivide_collezionista FOREIGN KEY (id_Collezionista) REFERENCES Collezionista(ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT condivide_collezione FOREIGN KEY (id_Collezione) REFERENCES Collezione(ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE collabora (
  id_Autore INT UNSIGNED,
  id_Disco INT UNSIGNED,
  nome_Tipo VARCHAR(50) NOT NULL,
  PRIMARY KEY (id_Autore, id_Disco),
  CONSTRAINT collabora_autore FOREIGN KEY (id_Autore) REFERENCES Autore(ID) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT collabora_disco FOREIGN KEY (id_Disco) REFERENCES Disco(ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT collabora_tipo FOREIGN KEY (nome_Tipo) REFERENCES Tipo(nome) ON DELETE NO ACTION ON UPDATE CASCADE
);
 
CREATE TABLE contribuisce (
  id_Autore INT UNSIGNED,
  id_Traccia INT UNSIGNED,
  nome_Tipo VARCHAR(50) NOT NULL,
  PRIMARY KEY (id_Autore, id_Traccia),
  CONSTRAINT contribuisce_autore FOREIGN KEY (id_Autore) REFERENCES Autore(ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT contribuisce_traccia FOREIGN KEY (id_Traccia) REFERENCES Traccia(ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT contribuisce_tipo FOREIGN KEY (nome_Tipo) REFERENCES Tipo(nome) ON DELETE NO ACTION ON UPDATE CASCADE
);

CREATE TABLE contiene (
  id_Traccia INT UNSIGNED,
  id_Disco INT UNSIGNED,
  PRIMARY KEY (id_Traccia, id_Disco),
  CONSTRAINT contiene_traccia FOREIGN KEY (id_Traccia) REFERENCES Traccia(ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT contiene_disco FOREIGN KEY (id_Disco) REFERENCES Disco(ID) ON DELETE CASCADE ON UPDATE CASCADE
);


