use collectors;

CALL CopiaInCollezione("Vinili pop","000000000002");

CALL TracciaInDisco("GBUM71604197","0834738293948");

CALL DischiTracce();

CALL Condivisioni();



SELECT * FROM collezione;


CALL AggiungiCondivisione("pippo123","pluto456","Rock");

CALL RimuoviCopiaDaCollezione("000000000001","La mia preferita");



CALL RimuoviCollezione("mickey789","La Mia Preferita");
CALL RimuoviCollezione("pluto456","Rock");

CALL DischiCopie();

CALL DischiInCollezione("mickey789","La Mia Preferita");

CALL RicercaDischi("Vasco Rossi", "va", "Mickey789", true, true, false);
CALL RicercaDischi("Annalisa", null, "Mickey789", true, true, true);

CALL SwitchCondivisione("La Mia Preferita","Mickey789");
CALL SwitchCondivisione("Rock","Pippo123");
CALL SwitchCondivisione("Rock","pluto456");

CALL CheckVisibilita("La Mia Preferita","mickey789","pippo123");

CALL TrackList("0834738293948");

CALL CollezioniCopie;
CALL ContaBraniByAutore("Vasco Rossi");
CALL TempoBraniByAutore("Zef");

CALL ContaCollezioni();

CALL ContaDischiPerGenere();

CALL DischiAutoriSec();
CALL TracceAutoriSec();

CALL RicercaCoerente("","","");
