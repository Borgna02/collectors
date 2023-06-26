USE collectors;
DELIMITER $$
# 0 Procedure ausiliarie

-- Stampa ogni coppia disco-traccia con i relativi EAN e ISRC
DROP PROCEDURE IF EXISTS DischiTracce$$

CREATE PROCEDURE DischiTracce() 
BEGIN
	SELECT D.titolo as "Titolo Disco", D.EAN, T.titolo as "Titolo Traccia", T.ISRC, T.durata, A.nomeDarte as "Autore principale" FROM Disco D, Traccia T, Contiene C, Autore A WHERE D.id = C.id_disco AND T.id = C.id_traccia AND A.id = D.id_autore ORDER BY D.titolo;
END$$

-- Stampa ogni coppia collezione-collezionista delle condivisioni
DROP PROCEDURE IF EXISTS Condivisioni$$

CREATE PROCEDURE Condivisioni()
BEGIN
	SELECT C.nickname as "Owner", CL.nome as "Collezione", C2.nickname as "Guest" FROM Collezionista C, Collezione CL, Collezionista C2, Condivide CO WHERE CL.isPubblica = false AND CL.id_collezionista = C.id AND CL.id = CO.id_collezione AND C2.id = CO.id_collezionista;
END$$

-- Stampa ogni coppia collezione-copia
DROP PROCEDURE IF EXISTS CollezioniCopie$$

CREATE PROCEDURE CollezioniCopie()
BEGIN
	SELECT CL.nickname, C.nome, CO.SN, CO.nome_Stato AS "Stato", CO.doppioni, C.isPubblica AS Pubblica, D.titolo, D.EAN, A.nomeDarte AS "Autore principale" FROM Autore A, Collezionista CL, Collezione C, Copia CO, Disco D WHERE C.id = CO.id_collezione AND CO.id_disco = D.id AND C.id_collezionista = CL.id AND A.id = D.id_autore ORDER BY C.nome;
END$$

-- Stampa ogni coppia disco-copia
DROP PROCEDURE IF EXISTS DischiCopie$$

CREATE PROCEDURE DischiCopie()
BEGIN
	SELECT D.titolo, D.EAN, C.SN, C.nome_Stato, A.nomeDarte AS "Autore principale" FROM Disco D, Copia C, Autore A WHERE C.id_disco = D.id AND A.id = D.id_autore;
END$$

-- Stampa ogni coppia disco-autore secondario
DROP PROCEDURE IF EXISTS DischiAutoriSec$$

CREATE PROCEDURE DischiAutoriSec()
BEGIN
	SELECT D.titolo, D.EAN, D.nome_formato, A.nomeDarte AS "Autore Secondario", C.nome_tipo AS "Tipo" FROM Disco D, Autore A, Collabora C WHERE D.id = C.id_disco AND C.id_autore = A.id;
END$$

-- Stampa ogni coppia disco-autore
DROP PROCEDURE IF EXISTS DischiAutori$$

CREATE PROCEDURE DischiAutori()
BEGIN
	SELECT D.titolo, D.EAN, D.nome_formato, A.nomeDarte FROM Disco D, Autore A WHERE D.id_autore = A.id; 
END$$

-- Stampa ogni coppia traccia-autore secondario
DROP PROCEDURE IF EXISTS TracceAutoriSec$$

CREATE PROCEDURE TracceAutoriSec()
BEGIN
	SELECT T.titolo, t.ISRC, A.nomeDarte AS "Autore Secondario", C.nome_tipo AS "Tipo" FROM Traccia T, Autore A, Contribuisce C WHERE T.id = C.id_traccia AND C.id_autore = A.id;
END$$

-- Dato nome della collezione e nickname del collezionista, restituisce l'id della collezione
DROP FUNCTION IF EXISTS GetCollezione$$

CREATE FUNCTION GetCollezione (
	p_nome_collezione VARCHAR(50),
    p_nickname_collezionista VARCHAR(50)
) RETURNS INT UNSIGNED DETERMINISTIC
BEGIN
	RETURN (SELECT id FROM Collezione WHERE nome = p_nome_collezione AND id_collezionista = (SELECT id FROM Collezionista WHERE nickname = p_nickname_collezionista));
END$$

# 1 Inserimento di una nuova collezione

DROP PROCEDURE IF EXISTS InserisciCollezione$$

CREATE PROCEDURE InserisciCollezione(
    IN p_nome VARCHAR(50),
    IN p_isPubblica BOOLEAN,
    IN p_nickname_collezionista VARCHAR(50)
)
BEGIN
	DECLARE p_id_collezionista INT UNSIGNED;
    SET p_id_collezionista = (SELECT id FROM Collezionista WHERE nickname = p_nickname_collezionista);
    
    IF p_id_collezionista IS NULL THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Non esiste un collezionista con questo nickname';
	ELSE
    -- Inserimento della nuova riga nella tabella Collezione
		INSERT INTO Collezione (nome, isPubblica, id_Collezionista)
		VALUES (p_nome, p_isPubblica, p_id_Collezionista);
	END IF;
END$$


# 2a Aggiunta di Dischi (Copie) a una collezione

-- Inserimento di un disco non presente nel sistema
DROP PROCEDURE IF EXISTS InserisciDisco$$

CREATE PROCEDURE InserisciDisco(
	IN p_EAN CHAR(13),
    IN p_titolo VARCHAR(50),
    IN p_anno SMALLINT,
    IN p_nome_Etichetta VARCHAR(50),
    IN p_nome_Formato VARCHAR(50),
    IN p_nomeDarte_autore_principale VARCHAR(50),
    IN p_nome_genere_principale VARCHAR(50)
)
BEGIN
	DECLARE p_id_autore INT UNSIGNED;
    DECLARE p_id_nuovo_disco INT UNSIGNED;
    SET p_id_autore = (SELECT id FROM Autore WHERE nomeDarte = p_nomeDarte_autore_principale);
    
    IF p_id_autore IS NULL THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Non esiste un autore con questo nome d\'arte';
	ELSEIF (SELECT * FROM Genere WHERE nome = p_nome_genere_principale) IS NULL THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Il genere indicato non esiste';
	ELSEIF (SELECT * FROM Formato WHERE nome = p_nome_Formato) IS NULL THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Il formato indicato non esiste';
	ELSE
		IF (SELECT * FROM Etichetta WHERE nome = p_nome_Etichetta) IS NULL THEN
			INSERT INTO Etichetta VALUE (p_nome_Etichetta);
		END IF;
		INSERT INTO Disco(EAN,titolo,anno,nome_Etichetta,nome_Formato,id_Autore) VALUE (p_EAN,p_titolo,p_anno,p_nome_Etichetta,p_nome_Formato,p_id_autore);
        SET p_id_nuovo_disco = last_insert_id();
		INSERT INTO Classifica VALUE (p_id_nuovo_disco, p_nome_genere_principale);
	END IF;
END$$

-- Aggiunta genere secondario al disco
DROP PROCEDURE IF EXISTS AggiungiGenereAlDisco$$

CREATE PROCEDURE AggiungiGenereAlDisco(
	IN p_nome_genere VARCHAR(50),
    IN p_EAN_disco CHAR(13)
)
BEGIN
	IF (SELECT * FROM Genere WHERE nome = p_nome_genere) IS NULL THEN	
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Non esiste un genere con questo nome';
	ELSEIF (SELECT id FROM Disco WHERE EAN = p_EAN_disco) IS NULL THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Non esiste un disco con questo EAN';
	ELSEIF EXISTS (SELECT * FROM Classifica WHERE id_disco = (SELECT id FROM Disco WHERE EAN = p_EAN_disco) AND nome_genere = p_nome_genere) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Questo genere è già associato al disco';
	ELSE
		INSERT INTO Classifica VALUE ((SELECT id FROM Disco WHERE EAN = p_EAN_disco), p_nome_genere);
	END IF;
END$$

-- Aggiunta autore secondario al disco
DROP PROCEDURE IF EXISTS AggiungiAutoreAlDisco$$

CREATE PROCEDURE AggiungiAutoreAlDisco(
	IN p_nomeDarte_autore VARCHAR(50),
    IN p_EAN_disco CHAR(13),
    IN p_nome_tipo VARCHAR(50)
)
BEGIN
	DECLARE p_id_autore INT UNSIGNED;
    DECLARE p_id_disco INT UNSIGNED;
    SET p_id_autore = (SELECT id FROM Autore WHERE nomeDarte = p_nomeDarte_autore);
    SET p_id_disco = (SELECT id FROM Disco WHERE EAN = p_EAN_disco);
	IF p_id_autore IS NULL THEN	
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L\'autore indicato non esiste';
	ELSEIF p_id_disco IS NULL THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Non esiste un disco con questo EAN';
	ELSEIF (SELECT * FROM Tipo WHERE nome = p_nome_tipo) IS NULL THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Non esiste un tipo con questo nome';
	ELSEIF EXISTS ((SELECT * FROM collabora WHERE id_disco = p_id_disco AND id_autore = p_id_autore AND nome_Tipo = p_nome_tipo)) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Questo autore è già associato al disco in quel ruolo';
	ELSEIF EXISTS (SELECT id FROM Disco WHERE id_autore = p_id_autore and id = p_id_disco) THEN 
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Questo autore è già associato al disco in quel ruolo';
    ELSE
		INSERT INTO collabora VALUE (p_id_autore, p_id_disco, p_nome_tipo);
	END IF;
END$$

-- Procedura per aggiungere una nuova copia ed assegnarla ad una collezione
DROP PROCEDURE IF EXISTS InserisciCopia$$

CREATE PROCEDURE InserisciCopia(
    IN p_SN CHAR(12),
    IN p_EAN_Disco CHAR(13),
    IN p_nome_Stato VARCHAR(50),
    IN p_nome_Collezione VARCHAR(50),
    IN p_nickname_Collezionista VARCHAR(50)
)
BEGIN
    DECLARE num_doppioni INT;
    DECLARE p_id_Disco INT UNSIGNED;
    DECLARE p_id_collezione INT UNSIGNED;
    SET p_id_Disco = (SELECT D.id FROM Disco D WHERE D.EAN = p_EAN_Disco);
    SET p_id_collezione = GetCollezione(p_nome_collezione, p_nickname_Collezionista);
    IF EXISTS (SELECT id FROM Copia WHERE SN = p_SN) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Esiste già una copia con questo SN';
    ELSEIF p_id_Disco IS NULL THEN 
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Non esiste un disco con questo EAN';
	ELSEIF p_id_collezione IS NULL THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Non esiste una collezione con questo nome associata a questo nickname';
	ELSE    
		-- Inserimento della nuova riga nella tabella Copia
		INSERT INTO Copia (SN, id_Disco, nome_Stato, id_Collezione) 
		VALUES (p_SN, p_id_Disco, p_nome_Stato, p_id_Collezione);
    
		-- Calcolo del numero di doppioni per l'id_disco appena inserito
		SET num_doppioni = (SELECT COUNT(*) FROM Copia WHERE id_Disco = p_id_Disco);
		
		-- Aggiornamento della colonna doppioni per tutte le copie con lo stesso id_disco
		UPDATE Copia C
		SET C.doppioni = num_doppioni
		WHERE C.id_Disco = p_id_Disco;
	END IF;
END$$

-- Procedura per spostare una copia già esistente da una collezione ad un'altra (dello stesso utente)
DROP PROCEDURE IF EXISTS CopiaInCollezione$$

CREATE PROCEDURE CopiaInCollezione(
    IN p_nome_collezione VARCHAR(50),
    IN p_SN_copia CHAR(12)
)
BEGIN
    DECLARE p_id_collezionista INT UNSIGNED;
    SET p_id_collezionista = (SELECT CZ.id_Collezionista FROM Copia C, Collezione CZ WHERE C.SN = p_SN_copia AND CZ.id = C.id_Collezione);
    
    -- Un collezionista non può spostare una sua copia nella collezione di un altro utente
    IF EXISTS(SELECT CZ.nome FROM Copia C, Collezione CZ WHERE C.SN = p_SN_copia AND C.id_collezione = CZ.id AND CZ.nome = p_nome_collezione AND CZ.id_collezionista = p_id_collezionista) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La copia è già nella collezione';
    ELSEIF EXISTS(SELECT CZ.nome FROM Collezione CZ WHERE CZ.nome = p_nome_collezione AND CZ.id_collezionista = p_id_collezionista) THEN
        UPDATE Copia C
        SET C.id_Collezione = (SELECT C.id FROM Collezione C, Collezionista CL WHERE C.nome = p_nome_collezione AND CL.id = p_id_collezionista)
        WHERE C.SN = p_SN_copia;
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La collezione indicata non esiste'; #n.b. non esiste tra quelle dell'utente
    END IF;
END$$

# 2b Aggiunta di tracce a un disco

-- Procedura per inserire una nuova traccia ed assegnarla ad un disco esistente
DROP PROCEDURE IF EXISTS NuovaTracciaInDisco$$

CREATE PROCEDURE NuovaTracciaInDisco(
	p_titolo VARCHAR(100),
    p_ISRC CHAR(12),
    p_durata TIME,
    p_EAN CHAR(13)
)
BEGIN
	DECLARE p_id_disco INT UNSIGNED;
    DECLARE p_id_nuova_traccia INT UNSIGNED;
    SET p_id_disco = (SELECT D.id FROM Disco D WHERE D.EAN = p_EAN);
    
    IF p_id_disco IS NULL THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Il disco indicato non esiste'; 
	ELSE
		INSERT INTO Traccia(titolo, ISRC, durata) VALUES (p_titolo, p_ISRC, p_durata);
        SET p_id_nuova_traccia = last_insert_id();
        INSERT INTO Contiene VALUES (p_id_nuova_traccia,p_id_disco);
	END IF;
END$$

-- Procedura per spostre una traccia esistente in un disco
DROP PROCEDURE IF EXISTS TracciaInDisco$$

CREATE PROCEDURE TracciaInDisco(
    IN p_ISRC_traccia CHAR(12),
    IN p_EAN_disco CHAR(13)
)
BEGIN
    DECLARE p_id_traccia INT UNSIGNED;
    DECLARE p_id_disco INT UNSIGNED;
    SET p_id_traccia = (SELECT T.id FROM Traccia T WHERE T.ISRC = p_ISRC_traccia);
    SET p_id_disco = (SELECT D.id FROM Disco D WHERE D.EAN = p_EAN_disco);
    
    IF p_id_disco IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Il disco indicato non esiste';
    ELSEIF p_id_traccia IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La traccia indicata non esiste';
    ELSEIF EXISTS (SELECT C.* FROM Contiene C WHERE C.id_traccia = p_id_traccia AND C.id_disco = p_id_disco) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La traccia è già nel disco';
    ELSE
        INSERT INTO Contiene (id_traccia, id_disco) VALUES (p_id_traccia, p_id_disco);
    END IF;
END$$

# 3a Modifica dello stato di condivisione
DROP PROCEDURE IF EXISTS switchCondivisione$$

CREATE PROCEDURE switchCondivisione(
    IN p_nome_Collezione VARCHAR(50),
    IN p_nickname_Collezionista VARCHAR(50)
)
BEGIN
    DECLARE p_stato_attuale BOOLEAN;
    DECLARE p_id_collezione INT UNSIGNED;
    
    SET p_id_collezione = GetCollezione(p_nome_Collezione, p_nickname_Collezionista);
    SET p_stato_attuale = (SELECT isPubblica FROM Collezione WHERE id = p_id_collezione);
    
    UPDATE Collezione SET isPubblica = NOT p_stato_attuale WHERE id = p_id_collezione;
    
    IF p_id_collezione IS NULL THEN 
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La collezione non esiste';
    ELSEIF (p_stato_attuale = TRUE) THEN
        DELETE FROM condivide WHERE id_collezione = p_id_collezione;
        # Se sto switchando da Privata a Pubblica, rimuovo i collezionisti con cui era condivisa
    END IF;
END$$

# 3b Aggiunta di una nuova condivisione (se privata)

DROP PROCEDURE IF EXISTS AggiungiCondivisione$$

CREATE PROCEDURE AggiungiCondivisione(
    IN p_nickname_owner VARCHAR(50),
    IN p_nickname_guest VARCHAR(50),
    IN p_nome_collezione VARCHAR(50)
)
BEGIN
    DECLARE p_id_collezione INT UNSIGNED;
    DECLARE p_id_owner INT UNSIGNED;
    DECLARE p_id_guest INT UNSIGNED;
    
    SET p_id_owner = (SELECT id FROM Collezionista WHERE nickname = p_nickname_owner);
    
    IF p_id_owner IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Non esiste un utente con il nickname indicato per l\'owner';
    ELSE
        SET p_id_collezione = (SELECT id FROM Collezione WHERE nome = p_nome_collezione AND id_collezionista = p_id_owner);
        
        IF p_id_collezione IS NULL THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La collezione non esiste';
        ELSE
            SET p_id_guest = (SELECT id FROM Collezionista WHERE nickname = p_nickname_guest);
            
            IF p_id_guest IS NULL THEN
                SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Non esiste un utente con il nickname indicato per il guest';
            ELSE
                IF (SELECT isPubblica FROM Collezione WHERE id = p_id_collezione) = TRUE THEN
                    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La collezione è pubblica, renderla privata e riprovare';
                ELSE
                    IF EXISTS (SELECT * FROM Condivide WHERE id_collezionista = p_id_guest AND id_collezione = p_id_collezione) THEN
                        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La collezione è già condivisa con questo utente';
                    ELSE
                        INSERT INTO Condivide VALUES (p_id_guest, p_id_collezione);
                    END IF;
                END IF;
            END IF;
        END IF;
    END IF;
END$$

# 4 Rimozione di un disco da una collezione
DROP PROCEDURE IF EXISTS RimuoviCopiaDaCollezione$$

-- Come detto nell'assunzione 1, ritengo poco utile mantenere nel sistema copie senza una collezione. 
-- Di conseguenza, rimuovere una copia da una collezione significa rimuoverla dal sistema.
CREATE PROCEDURE RimuoviCopiaDaCollezione(
	p_SN_copia CHAR(12),
    p_nome_collezione VARCHAR(50)
)
BEGIN
	DECLARE p_id_copia INT UNSIGNED;
    DECLARE p_id_collezione INT UNSIGNED;
    SET p_id_copia = (SELECT id FROM Copia WHERE SN = p_SN_copia); 
	IF p_id_copia IS NULL THEN 
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Non esiste una copia con questo SN';
	ELSE 
		SET p_id_collezione = (SELECT C.id FROM Collezione C, Copia CP WHERE CP.id = p_id_copia AND C.id = CP.id_collezione AND C.nome = p_nome_collezione);
        IF p_id_collezione IS NULL THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La copia non fa parte della collezione indicata';
		ELSE 
			DELETE FROM Copia WHERE id = p_id_copia;
		END IF;
	END IF;
END$$

# 5 Rimozione di una collezione

DROP PROCEDURE IF EXISTS RimuoviCollezione$$

CREATE PROCEDURE RimuoviCollezione(
	p_nickname_collezionista VARCHAR(50),
    p_nome_collezione VARCHAR(50)
)
BEGIN
	DECLARE p_id_collezione INT UNSIGNED;
    SET p_id_collezione = GetCollezione(p_nome_collezione, p_nickname_collezionista);
    
    IF p_id_collezione IS NULL THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La collezione indicata non esiste';
    ELSE
		DELETE FROM Collezione WHERE id = p_id_collezione;
	END IF;
END$$

# 6 Lista di tutti i dischi in una collezione
DROP PROCEDURE IF EXISTS DischiInCollezione$$

CREATE PROCEDURE DischiInCollezione(
	p_nickname_collezionista VARCHAR(50),
    p_nome_collezione VARCHAR(50)
)
BEGIN
	DECLARE p_id_collezione INT UNSIGNED;
    SET p_id_collezione = GetCollezione(p_nome_collezione, p_nickname_collezionista);
    
    IF p_id_collezione IS NULL THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La collezione indicata non esiste';
    ELSE
		SELECT DISTINCT D.* FROM Disco D, Copia C WHERE D.id = C.id_disco AND C.id_collezione = p_id_collezione;
	END IF;
END$$

# 7 Track list di un disco
DROP PROCEDURE IF EXISTS TrackList$$

CREATE PROCEDURE TrackList(
	p_EAN CHAR(13)
)
BEGIN
	DECLARE p_id_disco INT UNSIGNED;
    SET p_id_disco = (SELECT id FROM Disco WHERE EAN = p_EAN);
    
    IF p_id_disco IS NULL THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Il disco indicato non esiste';
	ELSE
		SELECT DISTINCT T.* FROM Traccia T, Contiene C WHERE C.id_traccia = T.id AND C.id_disco = p_id_disco;
	END IF;
END$$

# 8 Ricerca dischi in base a diversi criteri
DROP PROCEDURE IF EXISTS RicercaDischi$$

# Per non includere l'autore nella ricerca, inserisco null come argomento dell'autore. Stesso discorso per il titolo.
CREATE PROCEDURE RicercaDischi(
    IN p_nome_autore VARCHAR(50),
    IN p_titolo VARCHAR(50),
    IN p_nome_collezionista VARCHAR(50),
    IN p_includi_private BOOLEAN,
    IN p_includi_condivise BOOLEAN,
    IN p_includi_pubbliche BOOLEAN
)
BEGIN
    DECLARE p_id_collezionista INT UNSIGNED;
    DECLARE p_id_autore INT UNSIGNED;
    SET p_id_collezionista = (SELECT id FROM Collezionista WHERE nickname = p_nome_collezionista);

	SET p_id_autore = (SELECT id FROM Autore WHERE nomeDarte = p_nome_autore);
	IF p_id_autore IS NULL AND p_nome_autore IS NOT NULL THEN 
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L\'autore indicato non esiste';
	ELSE
		# Ricerca in base ai criteri specificati 
		SELECT DISTINCT D.* FROM Disco D, collabora C WHERE 
        ((p_titolo IS NOT NULL AND p_id_autore IS NULL AND D.titolo LIKE CONCAT('%', p_titolo, '%')) 
		OR 
        (p_id_autore IS NOT NULL AND p_titolo IS NULL AND ((C.id_autore = p_id_autore AND D.id = C.id_disco) OR (D.id_autore = p_id_autore))) 
        OR 
        (p_titolo IS NOT NULL AND p_id_autore IS NOT NULL AND D.titolo LIKE CONCAT('%', p_titolo, '%') AND ((C.id_autore = p_id_autore AND D.id = C.id_disco) OR (D.id_autore = p_id_autore))))
		AND 
        ((p_includi_private = 1 AND EXISTS (SELECT * FROM Copia C WHERE C.id_disco = D.id AND C.id_collezione IN (SELECT id FROM Collezione WHERE id_collezionista = p_id_collezionista)))
		OR 
        (p_includi_condivise = 1 AND EXISTS (SELECT * FROM Copia C, Condivide CC WHERE C.id_disco = D.id AND C.id_collezione = CC.id_collezione AND CC.id_collezionista = p_id_collezionista))
		OR 
        (p_includi_pubbliche = 1 AND EXISTS (SELECT * FROM Copia C, Collezione CZ WHERE C.id_disco = D.id AND C.id_collezione = CZ.id AND CZ.isPubblica = 1 AND CZ.id_collezionista != p_id_collezionista))); # escludo le collezioni pubbliche che appartengono al collezionista che fa la query
	END IF;
END$$

# 9 Verifica visibilità
DROP PROCEDURE IF EXISTS CheckVisibilita$$

CREATE PROCEDURE CheckVisibilita(
	IN p_nome_collezione VARCHAR(50),
    IN p_nickname_owner VARCHAR(50),
    IN p_nickname_guest VARCHAR(50)
)
BEGIN
	DECLARE p_id_collezione INT UNSIGNED;
    SET p_id_collezione = GetCollezione(p_nome_collezione, p_nickname_owner);
    
	IF (SELECT id FROM Collezionista WHERE nickname = p_nickname_guest) IS NULL THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Il collezionista indicato non esiste';
	ELSEIF p_id_collezione IS NULL THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La collezione indicata non esiste';
	ELSE
		IF (SELECT ((p_nickname_owner = p_nickname_guest) OR (SELECT isPubblica FROM Collezione WHERE id = p_id_collezione) = TRUE OR EXISTS (SELECT * FROM Condivide WHERE id_collezione = p_id_collezione AND id_collezionista = (SELECT id FROM Collezionista WHERE nickname = p_nickname_guest)))) 
			= true THEN
				SELECT "Visibile" AS Risultato;
			ELSE
				SELECT "Non visibile" AS Risultato;
			END IF;    
    END IF;
END$$

# 10 Numero di brani di un autore nelle collezioni pubbliche
DROP PROCEDURE IF EXISTS ContaBraniByAutore$$

CREATE PROCEDURE ContaBraniByAutore(
	IN p_nomeDarte VARCHAR(50)
)
BEGIN
	DECLARE p_id_autore INT UNSIGNED;
    SET p_id_autore = (SELECT id FROM Autore WHERE nomeDarte = p_nomeDarte);
    
    IF p_id_autore IS NULL THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L\'autore indicato non esiste';
	ELSE
		SELECT count(DISTINCT T.id) AS "Numero tracce" FROM Traccia T, Disco D, Contiene C, Collezione CZ, Copia CO, Contribuisce CB, Collabora CL WHERE 
			((T.id = CB.id_traccia AND CB.id_autore = p_id_autore) OR (D.id = CL.id_disco AND CL.id_autore = p_id_autore AND T.id = C.id_traccia AND C.id_disco = D.id) OR (D.id_autore = p_id_autore AND T.id = C.id_traccia AND C.id_disco = D.id)) AND 
            T.id = C.id_traccia AND C.id_disco = D.id AND CO.id_disco = D.id AND CO.id_collezione = CZ.id AND CZ.isPubblica = true;
	END IF;
END$$

# 11 Tempo di musica di un autore nelle collezioni pubbliche
DROP PROCEDURE IF EXISTS TempoBraniByAutore$$

CREATE PROCEDURE TempoBraniByAutore(
	IN p_nomeDarte VARCHAR(50)
)
BEGIN
	DECLARE p_id_autore INT UNSIGNED;
    SET p_id_autore = (SELECT id FROM Autore WHERE nomeDarte = p_nomeDarte);
    
    IF p_id_autore IS NULL THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L\'autore indicato non esiste';
	ELSE
		SELECT SEC_TO_TIME(SUM(TIME_TO_SEC(durata))) AS "Tempo di musica"
			FROM (SELECT DISTINCT T.id, T.durata AS durata FROM Traccia T, Disco D, Contiene C, Collezione CZ, Copia CO, Contribuisce CB, Collabora CL WHERE 
			((T.id = CB.id_traccia AND CB.id_autore = p_id_autore) OR (D.id = CL.id_disco AND CL.id_autore = p_id_autore AND T.id = C.id_traccia AND C.id_disco = D.id) OR (D.id_autore = p_id_autore AND T.id = C.id_traccia AND C.id_disco = D.id)) AND 
            T.id = C.id_traccia AND C.id_disco = D.id AND CO.id_disco = D.id AND CO.id_collezione = CZ.id AND CZ.isPubblica = true) as SubQuery;
	END IF;
END$$

# 12a Numero di collezioni di ciascun collezionista
DROP PROCEDURE IF EXISTS ContaCollezioni$$ 

CREATE PROCEDURE ContaCollezioni()
BEGIN
	SELECT C.nickname, count(DISTINCT CO.id) as "Numero" FROM Collezionista C, Collezione CO WHERE CO.id_collezionista = C.id GROUP BY C.id;
END$$

# 12b Numero di dischi per genere
DROP PROCEDURE IF EXISTS ContaDischiPerGenere$$

CREATE PROCEDURE ContaDischiPerGenere()
BEGIN
	SELECT G.nome, count(DISTINCT D.id) as "Numero" FROM Genere G, Classifica C, Disco D WHERE C.id_disco = D.id AND C.nome_genere = G.nome GROUP BY G.nome;
END$$
 
 
# 13 Ricerca coerente
DROP PROCEDURE IF EXISTS RicercaCoerente$$

CREATE PROCEDURE RicercaCoerente(
	IN p_EAN CHAR(13),
    IN p_titolo VARCHAR(50),
    IN p_nomeDarte VARCHAR(50)
)
-- Utilizzo due volte soundex per rendere il range di compatibilità più ampio
BEGIN
	IF p_titolo = "" THEN 
		SET p_titolo = null; # altrimenti cercando con il titolo vuoto mi verrebbero restituiti tutti i dischi del sistema
    END IF;
    
    IF p_nomeDarte = "" THEN
		SET p_nomeDarte = null; # altrimenti cercando con il nome d'arte vuoto mi verrebbero restituiti tutti i dischi del sistema
	END IF;
    
	-- MATCH con i dischi con lo stesso EAN
	(SELECT D.*, A.nomeDarte AS "Autore principale" FROM Disco D, Autore A WHERE D.EAN = p_EAN AND A.id = D.id_autore ) 
	UNION
    -- MATCH con i dischi che contengono la parola cercata come titolo e come autore principale
    (SELECT D.*, A.nomeDarte AS "Autore principale" FROM Disco D, Autore A WHERE D.titolo LIKE CONCAT("%",p_titolo,"%") 
    AND D.id_autore = A.id AND A.nomeDarte LIKE CONCAT("%",p_nomeDarte,"%"))
    UNION
    -- MATCH con i dischi che contengono la parola cercata come titolo e si avvicinano foneticamente alla parola cercata come autore nell'autore principale
    (SELECT D.*, A.nomeDarte AS "Autore principale" FROM Disco D, Autore A WHERE D.titolo LIKE CONCAT("%",p_titolo,"%") AND A.id = D.id_autore AND soundex(soundex(A.nomeDarte)) = soundex(soundex(p_nomeDarte)))
    UNION
	-- MATCH con i dischi che contengono la parola cercata come autore nell'autore principale e si avvicinano foneticamente alla parola cercata come titolo nel titolo
	(SELECT D.*, A.nomeDarte AS "Autore principale" FROM Disco D, Autore A WHERE soundex(soundex(D.titolo)) = soundex(soundex(p_titolo)) AND D.id_autore = A.id AND A.nomeDarte LIKE CONCAT("%",p_nomeDarte,"%"))
    UNION
	-- MATCH con i dischi che si avvicinano foneticamente alla parola cercata sia come titolo nel titolo che come autore nell'autore principale
	(SELECT D.*, A.nomeDarte AS "Autore principale" FROM Disco D, Autore A WHERE soundex(soundex(D.titolo)) = soundex(soundex(p_titolo)) AND D.id_autore = A.id AND soundex(soundex(A.nomeDarte)) = soundex(soundex(p_nomeDarte)))
    UNION
	-- MATCH con i dischi che contengono la parola cercata come titolo e come autore secondario
    (SELECT D.*, AP.nomeDarte AS "Autore principale" FROM Disco D, Collabora C, Autore A, Autore AP WHERE D.titolo LIKE CONCAT("%",p_titolo,"%") 
    AND D.id = C.id_disco AND C.id_autore = A.id AND A.nomeDarte LIKE CONCAT("%",p_nomeDarte,"%") AND AP.id = D.id_autore)
    UNION 
    -- MATCH con i dischi che contengono la parola cercata come autore nell'autore secondario e si avvicinano foneticamente alla parola cercata come titolo nel titolo
    (SELECT D.*,  AP.nomeDarte AS "Autore principale" FROM Disco D, Collabora C, Autore A, Autore AP WHERE soundex(soundex(D.titolo)) = soundex(soundex(p_titolo)) 
    AND D.id = C.id_disco AND C.id_autore = A.id AND A.nomeDarte LIKE CONCAT("%",p_nomeDarte,"%") AND AP.id = D.id_autore) 
	UNION
    -- MATCH con i dischi che contengono la parola cercata come titolo nel titolo e si avvicinano foneticamente alla parola cercata come autore nell'autore secondario
    (SELECT D.*,  AP.nomeDarte AS "Autore principale" FROM Disco D, Collabora C, Autore A, Autore AP WHERE D.titolo LIKE CONCAT("%",p_titolo,"%") 
    AND D.id = C.id_disco AND C.id_autore = A.id AND soundex(soundex(A.nomeDarte)) = soundex(soundex(p_nomeDarte)) AND AP.id = D.id_autore )
	UNION
	-- MATCH con i dischi che si avvicinano foneticamente alla parola cercata sia come titolo nel titolo che come autore nell'autore secondario
    (SELECT D.*,  AP.nomeDarte AS "Autore principale" FROM Disco D, Collabora C, Autore A, Autore AP WHERE soundex(soundex(D.titolo)) = soundex(soundex(p_titolo)) 
    AND D.id = C.id_disco AND C.id_autore = A.id AND soundex(soundex(A.nomeDarte)) = soundex(soundex(p_nomeDarte)) AND AP.id = D.id_autore)
    UNION
    -- MATCH con i dischi che si avvicinano foneticamente alla parola cercata come autore nell'autore principale e con qualsiasi titolo
    (SELECT D.*, A.nomeDarte AS "Autore principale" FROM Disco D, Autore A WHERE D.id_autore = A.id AND soundex(soundex(A.nomeDarte)) = soundex(soundex(p_nomeDarte)))
    UNION
    -- MATCH con i dischi che si avvicinano foneticamente alla parola cercata come autore negli autori secondari e con qualsiasi titolo
	(SELECT D.*,  AP.nomeDarte AS "Autore principale" FROM Disco D, Collabora C, Autore A, Autore AP WHERE D.id = C.id_disco 
    AND C.id_autore = A.id AND soundex(soundex(A.nomeDarte)) = soundex(soundex(p_nomeDarte)) AND AP.id = D.id_autore) 
	UNION
    -- MATCH con i dischi che si avvicinano foneticamente alla parola cercata come titolo nel titolo e con qualsiasi autore
    (SELECT D.*, A.nomeDarte AS "Autore principale" FROM Disco D, Autore A WHERE (soundex(soundex(D.titolo)) = soundex(soundex(p_titolo)) 
    OR D.titolo LIKE CONCAT("%",p_titolo,"%")) AND A.id = D.id_autore)
    ;
END$$

DELIMITER ;


