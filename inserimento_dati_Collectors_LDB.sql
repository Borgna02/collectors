USE collectors;

DELETE FROM Copia;
DELETE FROM collabora;
DELETE FROM Autore;
DELETE FROM Collezione;
DELETE FROM Collezionista;
DELETE FROM Condivide;
DELETE FROM Contiene;
DELETE FROM Contribuisce;
DELETE FROM Disco;
DELETE FROM Etichetta;
DELETE FROM Formato;
DELETE FROM Genere;
DELETE FROM Immagine;
DELETE FROM Posizione;
DELETE FROM Stato;
DELETE FROM Tipo;
DELETE FROM Traccia;

INSERT INTO Autore(nomeDarte,nome,cognome,isGruppo) VALUE ("The Rolling Stones",null,null,true);
INSERT INTO Autore(nomeDarte,nome,cognome,isGruppo) VALUE ("Vasco Rossi","Vasco","Rossi",false);
INSERT INTO Autore(nomeDarte,nome,cognome,isGruppo) VALUE ("Annalisa","Annalisa","Scarrone",false);
INSERT INTO Autore(nomeDarte,nome,cognome,isGruppo) VALUE ("Zef","Stefano","Tognini",false);
       
INSERT INTO Collezionista(nickname,email) VALUES 
	("pippo123","pippo123@ex.it"),
	("pluto456","pluto456@ex.it"),
    ("mickey789","mickey789@ex.it");

CALL InserisciCollezione("Rock",false,"pippo123");
CALL InserisciCollezione("Rock",true,"pluto456");
CALL InserisciCollezione("Vinili Pop",false,"pluto456");
CALL InserisciCollezione("La mia preferita",true,"mickey789");
    
INSERT INTO Condivide VALUES 
	((SELECT C.ID FROM Collezionista C Where C.nickname = "pluto456"),(SELECT C.ID FROM Collezione C Where C.nome = "Rock" AND C.id_collezionista = ((SELECT C.ID FROM Collezionista C Where C.nickname = "pippo123")))),
	((SELECT C.ID FROM Collezionista C Where C.nickname = "mickey789"),(SELECT C.ID FROM Collezione C Where C.nome = "Rock" AND C.id_collezionista = ((SELECT C.ID FROM Collezionista C Where C.nickname = "pippo123")))),
	((SELECT C.ID FROM Collezionista C Where C.nickname = "pippo123"),(SELECT C.ID FROM Collezione C Where C.nome = "Vinili Pop" AND C.id_collezionista = ((SELECT C.ID FROM Collezionista C Where C.nickname = "pluto456"))));

INSERT INTO Etichetta(nome) VALUES 
	("Polydor Records"),
    ("Carosello"),
    ("Warner Music Italy");

INSERT INTO Formato(nome) VALUES 
	("CD"),
    ("Vinile"),
    ("Digitale"),
    ("Cassetta");

INSERT INTO Genere(nome) VALUES 
	("Rock"),
    ("Pop"),
    ("Blues");

INSERT INTO Traccia(titolo,ISRC,Durata) VALUES   #ISRC trovati su isrcsearch.ifpi.org
	("Just Your Fool","GBUM71604197",000216),
    ("Commit A Crime","GBUM71604638",000338),
    ("I Gotta Go","GBUM71604634",000326),
    ("Vita Spericolata", "ITB269999067",000442),
    ("Bollicine","ITB260000024",000536),
    ("Vado Al Massimo","ITB268299062",000340),
    ("Mon Amour","ITQ002300239",000323);

INSERT INTO Tipo VALUES
	("Cantante"),
    ("Produttore"),
    ("Testo");
    
#Alcuni EAN non sono reali in quanto non reperibili sul web
CALL InserisciDisco("0602557149463","Blue&Lonesome Deluxe Edition",2016, "Polydor Records", "CD", "The Rolling Stones", "Rock");
CALL InserisciDisco("0834738293948","Mon Amour",2023, "Warner Music Italy", "Digitale","Annalisa","Pop"); 
CALL InserisciDisco("8032529710025","Vado al massimo", 1982, "Carosello","CD","Vasco Rossi","Pop");
CALL InserisciDisco("8034125847372","Bollicine", 1983, "Carosello", "Vinile", "Vasco Rossi","Pop");
CALL AggiungiGenereAlDisco("Blues","0602557149463");

CALL AggiungiAutoreAlDisco("Vasco Rossi","0834738293948","Testo");

  
CALL TracciaInDisco("ITQ002300239","0834738293948"); # Mon Amour  in  Mon Amour
CALL TracciaInDisco("GBUM71604197","0602557149463"); # Just Your Fool in Blue&Lonesome Deluxe Edition
CALL TracciaInDisco("GBUM71604638","0602557149463"); # Commit a Crime in Blue&Lonesome Deluxe Edition
CALL TracciaInDisco("GBUM71604634","0602557149463"); # I Gotta Go in Blue&Lonesome Deluxe Edition
CALL TracciaInDisco("ITB269999067","8034125847372"); # Vita spericolata in Bollicine
CALL TracciaInDisco("ITB268299062","8032529710025"); # Vado al massimo in Vado al massimo
CALL TracciaInDisco("ITB260000024","8034125847372"); # Bollicine in Bollicine
        
CALL NuovaTracciaInDisco("Bellissima","ITQ002200517",000321,"0834738293948"); # Bellissima in Mon Amour
        
INSERT INTO Contribuisce VALUES
	((SELECT A.id FROM Autore A WHERE A.nomeDarte="Zef"),(SELECT T.id FROM Traccia T WHERE T.ISRC="ITQ002300239"),"Testo");
    
INSERT INTO Stato VALUES
	("Nuovo"),
    ("Ottimo"),
    ("Buono"),
    ("Scarso"),
    ("Pessimo");
    
CALL InserisciCopia("000000000001", "0834738293948", "Ottimo", "La Mia Preferita","Mickey789"); # Mon Amour 
CALL InserisciCopia("000000000002", "0602557149463", "Ottimo", "Rock","Pluto456"); # Blue&Lonesome Deluxe Edition
CALL InserisciCopia("000000000003", "0834738293948", "Buono", "La Mia Preferita","Mickey789"); # Mon Amour
CALL InserisciCopia("000000000004", "0602557149463", "Nuovo", "Rock" , "Pippo123"); # Blue&Lonesome Deluxe Edition
CALL InserisciCopia("000000000005", "0602557149463", "Pessimo", "Rock", "Pippo123"); # Blue&Lonesome Deluxe Edition
CALL InserisciCopia("000000000006", "8034125847372", "Scarso","Rock","Pippo123"); # Bollicine
CALL InserisciCopia("000000000008", "8034125847372", "Scarso","Rock","Pluto456"); # Bollicine
CALL InserisciCopia("000000000007", "8032529710025", "Ottimo","La Mia Preferita","Mickey789"); # Vado al massimo

INSERT INTO Posizione VALUES
	("Fronte"),
    ("Retro"),
    ("Libretto"),
    ("Digitale"),
    ("Fronte disco");

INSERT INTO Immagine(URL,id_Disco,nome_Posizione) VALUES
	("https://www.google.com/url?sa=i&url=https%3A%2F%2Fmusic.amazon.it%2Falbums%2FB0BZ6LJRPP&psig=AOvVaw0-b4Fo8NERBp_dPyt-TX-P&ust=1687701656779000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCOCnnd-I3P8CFQAAAAAdAAAAABAE",(SELECT D.id FROM Disco D WHERE D.EAN = "0834738293948"),"Digitale");