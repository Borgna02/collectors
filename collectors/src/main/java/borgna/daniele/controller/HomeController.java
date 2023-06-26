package borgna.daniele.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class HomeController implements Initializable {

    @FXML
    private TextField parametro1;
    @FXML
    private TextField parametro2;
    @FXML
    private TextField parametro3;
    @FXML
    private TextField parametro4;
    @FXML
    private TextField parametro5;
    @FXML
    private TextField parametro6;
    @FXML
    private TextField parametro7;

    @FXML
    private Text labelP1;
    @FXML
    private Text labelP2;
    @FXML
    private Text labelP3;
    @FXML
    private Text labelP4;
    @FXML
    private Text labelP5;
    @FXML
    private Text labelP6;
    @FXML
    private Text labelP7;
    @FXML
    private CheckBox booleano1;
    @FXML
    private CheckBox booleano2;
    @FXML
    private CheckBox booleano3;

    @FXML
    private TextArea primaTextArea;
    @FXML
    private TextArea dopoTextArea;

    private String query;

    private String operazioneCorrente;

    private String connectionString;

    private String username;

    private String password;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectionString = "jdbc:mysql://localhost:3306/collectors";
        username = "collectorsUser";
        password = "collectorsPwd";
        query = "";
        operazioneCorrente = "";
    }

    @FXML
    private void operazione1(ActionEvent event) {
        reset();

        parametro3.setDisable(true);
        parametro4.setDisable(true);
        parametro5.setDisable(true);
        parametro6.setDisable(true);
        parametro7.setDisable(true);
        booleano2.setDisable(true);
        booleano3.setDisable(true);

        labelP1.setText("Nome collezione");
        labelP2.setText("Nome collezionista");
        booleano1.setText("Pubblica");

        operazioneCorrente = "operazione1";

        query = "{CALL InserisciCollezione(?, ?, ?) }";

        Connection con = null;
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            // inserisco nel box "prima" la tabella prima della query
            con = DriverManager.getConnection(connectionString, username, password);
            stmt = con.createStatement();
            rslt = stmt.executeQuery(
                    "SELECT C.*, CO.nickname FROM Collezione C, Collezionista CO WHERE C.id_collezionista = CO.id ORDER BY C.id");

            String resultString = "ID, NOME, ISPUBBLICA, OWNER \n\n";
            while (rslt.next()) {
                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                        + "; "
                        + rslt.getString(5) + "\n";
            }

            primaTextArea.setText(resultString);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rslt != null) {
                try {
                    rslt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @FXML
    private void operazione2a(ActionEvent event) {
        reset();
        booleano1.setDisable(true);
        booleano2.setDisable(true);
        booleano3.setDisable(true);

        labelP1.setText("EAN");
        labelP2.setText("Titolo");
        labelP3.setText("Anno");
        labelP4.setText("Etichetta");
        labelP5.setText("Formato");
        labelP6.setText("Autore principale");
        labelP7.setText("Genere principale");

        operazioneCorrente = "operazione2a";

        query = "{CALL InserisciDisco(?, ?, ?, ?, ?, ?, ?) }";

        Connection con = null;
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            // inserisco nel box "prima" la tabella prima della query
            con = DriverManager.getConnection(connectionString, username, password);
            stmt = con.createStatement();
            rslt = stmt.executeQuery(
                    "CALL DischiAutori()");

            String resultString = "DISCO, EAN, FORMATO, AUTORE PRINCIPALE \n\n";
            while (rslt.next()) {
                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                        + "; "
                        + rslt.getString(4) + "\n";
            }

            primaTextArea.setText(resultString);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rslt != null) {
                try {
                    rslt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @FXML
    private void operazione2d(ActionEvent event) {
        reset();
        labelP1.setText("SN");
        labelP2.setText("EAN");
        labelP3.setText("Stato");
        labelP4.setText("Nome Collezione");
        labelP5.setText("Nome Collezionista");
        parametro6.setDisable(true);
        parametro7.setDisable(true);
        booleano1.setDisable(true);
        booleano2.setDisable(true);
        booleano3.setDisable(true);

        operazioneCorrente = "operazione2d";

        query = "{CALL InserisciCopia(?, ?, ?, ?, ?)}";

        Connection con = null;
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            // inserisco nel box "prima" la tabella prima della query
            con = DriverManager.getConnection(connectionString, username, password);
            stmt = con.createStatement();
            rslt = stmt.executeQuery(
                    "CALL CollezioniCopie()");

            String resultString = "COLLEZIONISTA, COLLEZIONE, SN, STATO, DOPPIONI, ISPUBBLICA, DISCO, EAN, AUTORE \n\n";
            while (rslt.next()) {
                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                        + "; "
                        + rslt.getString(4) + "; " + rslt.getString(5) + "; " + rslt.getString(6) + "; "
                        + rslt.getString(7) + "; " + rslt.getString(8) + "; " + rslt.getString(9) + "\n";
            }

            primaTextArea.setText(resultString);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rslt != null) {
                try {
                    rslt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @FXML
    private void operazione2e(ActionEvent event) {
        reset();
        labelP1.setText("Nome collezione");
        labelP2.setText("SN");
        parametro3.setDisable(true);
        parametro4.setDisable(true);
        parametro5.setDisable(true);
        parametro6.setDisable(true);
        parametro7.setDisable(true);
        booleano1.setDisable(true);
        booleano2.setDisable(true);
        booleano3.setDisable(true);

        operazioneCorrente = "operazione2e";

        query = "{CALL CopiaInCollezione(?, ?)}";

        Connection con = null;
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            // inserisco nel box "prima" la tabella prima della query
            con = DriverManager.getConnection(connectionString, username, password);
            stmt = con.createStatement();
            rslt = stmt.executeQuery(
                    "CALL CollezioniCopie()");

            String resultString = "COLLEZIONISTA, COLLEZIONE, SN, STATO, DOPPIONI, ISPUBBLICA, DISCO, EAN, AUTORE \n\n";
            while (rslt.next()) {
                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                        + "; "
                        + rslt.getString(4) + "; " + rslt.getString(5) + "; " + rslt.getString(6) + "; "
                        + rslt.getString(7) + "; " + rslt.getString(8) + "; " + rslt.getString(9) + "\n";
            }

            primaTextArea.setText(resultString);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rslt != null) {
                try {
                    rslt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @FXML
    private void operazione2f(ActionEvent event) {
        reset();
        labelP1.setText("Titolo traccia");
        labelP2.setText("ISRC");
        labelP3.setText("Durata");
        labelP4.setText("EAN");
        parametro5.setDisable(true);
        parametro6.setDisable(true);
        parametro7.setDisable(true);
        booleano1.setDisable(true);
        booleano2.setDisable(true);
        booleano3.setDisable(true);

        operazioneCorrente = "operazione2f";

        query = "{CALL NuovaTracciaInDisco(?, ?, ?, ?)}";

        Connection con = null;
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            // inserisco nel box "prima" la tabella prima della query
            con = DriverManager.getConnection(connectionString, username, password);
            stmt = con.createStatement();
            rslt = stmt.executeQuery(
                    "CALL DischiTracce()");

            String resultString = "DISCO, EAN, TRACCIA, ISRC, DURATA, AUTORE PRINCIPALE \n\n";
            while (rslt.next()) {
                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                        + "; "
                        + rslt.getString(4) + "; " + rslt.getString(5) + "; " + rslt.getString(6) + "\n";
            }

            primaTextArea.setText(resultString);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rslt != null) {
                try {
                    rslt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @FXML
    private void operazione2g(ActionEvent event) {
        reset();
        labelP1.setText("ISRC Traccia");
        labelP2.setText("EAN Disco");
        parametro3.setDisable(true);
        parametro4.setDisable(true);
        parametro5.setDisable(true);
        parametro6.setDisable(true);
        parametro7.setDisable(true);
        booleano1.setDisable(true);
        booleano2.setDisable(true);
        booleano3.setDisable(true);

        operazioneCorrente = "operazione2g";

        query = "{CALL TracciaInDisco(?, ?)}";

        Connection con = null;
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            // inserisco nel box "prima" la tabella prima della query
            con = DriverManager.getConnection(connectionString, username, password);
            stmt = con.createStatement();
            rslt = stmt.executeQuery(
                    "CALL DischiTracce()");

            String resultString = "DISCO, EAN, TRACCIA, ISRC, DURATA, AUTORE \n\n";
            while (rslt.next()) {
                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                        + "; "
                        + rslt.getString(4) + "; "
                        + rslt.getString(5) + "; "
                        + rslt.getString(6) + "\n";
            }

            primaTextArea.setText(resultString);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rslt != null) {
                try {
                    rslt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @FXML
    private void operazione3a(ActionEvent event) {
        reset();
        labelP1.setText("Nome collezione");
        labelP2.setText("Nickname Owner");
        parametro3.setDisable(true);
        parametro4.setDisable(true);
        parametro5.setDisable(true);
        parametro6.setDisable(true);
        parametro7.setDisable(true);
        booleano1.setDisable(true);
        booleano2.setDisable(true);
        booleano3.setDisable(true);

        operazioneCorrente = "operazione3a";

        query = "{CALL switchCondivisione(?, ?)}";

        Connection con = null;
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            // inserisco nel box "prima" la tabella prima della query
            con = DriverManager.getConnection(connectionString, username, password);
            stmt = con.createStatement();
            rslt = stmt.executeQuery(
                    "SELECT C.*, CO.nickname FROM Collezione C, Collezionista CO WHERE C.id_collezionista = CO.id ORDER BY C.id");

            String resultString = "ID, NOME, ISPUBBLICA, OWNER \\n";
            while (rslt.next()) {
                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                        + "; "
                        + rslt.getString(5) + "\n";
            }

            primaTextArea.setText(resultString);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rslt != null) {
                try {
                    rslt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @FXML
    private void operazione3b(ActionEvent event) {
        reset();
        labelP1.setText("Nickname Owner");
        labelP2.setText("Nickname Guest");
        labelP3.setText("Nome collezione");
        parametro4.setDisable(true);
        parametro5.setDisable(true);
        parametro6.setDisable(true);
        parametro7.setDisable(true);
        booleano1.setDisable(true);
        booleano2.setDisable(true);
        booleano3.setDisable(true);

        operazioneCorrente = "operazione3b";

        query = "{CALL AggiungiCondivisione(?, ?, ?)}";

        Connection con = null;
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            // inserisco nel box "prima" la tabella prima della query
            con = DriverManager.getConnection(connectionString, username, password);
            stmt = con.createStatement();
            rslt = stmt.executeQuery(
                    "CALL Condivisioni()");

            String resultString = "OWNER, COLLEZIONE, GUEST \n\n";
            while (rslt.next()) {
                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                        + "\n";
            }

            primaTextArea.setText(resultString);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rslt != null) {
                try {
                    rslt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @FXML
    private void operazione4(ActionEvent event) {
        reset();
        labelP1.setText("SN copia");
        labelP2.setText("Nome collezione");
        parametro3.setDisable(true);
        parametro4.setDisable(true);
        parametro5.setDisable(true);
        parametro6.setDisable(true);
        parametro7.setDisable(true);
        booleano1.setDisable(true);
        booleano2.setDisable(true);
        booleano3.setDisable(true);
        operazioneCorrente = "operazione4";

        query = "{CALL RimuoviCopiaDaCollezione(?, ?)}";

        Connection con = null;
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            // inserisco nel box "prima" la tabella prima della query
            con = DriverManager.getConnection(connectionString, username, password);
            stmt = con.createStatement();
            rslt = stmt.executeQuery(
                    "CALL CollezioniCopie()");

            String resultString = "COLLEZIONISTA, COLLEZIONE, SN, STATO, DOPPIONI, ISPUBBLICA, DISCO, EAN, AUTORE \n\n";
            while (rslt.next()) {
                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                        + "; "
                        + rslt.getString(4) + "; " + rslt.getString(5) + "; " + rslt.getString(6) + "; "
                        + rslt.getString(7) + "; " + rslt.getString(8) + "; " + rslt.getString(9) + "\n";
            }

            primaTextArea.setText(resultString);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rslt != null) {
                try {
                    rslt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @FXML
    private void operazione5(ActionEvent event) {
        reset();
        labelP1.setText("Nickname Owner");
        labelP2.setText("Nome collezione");
        parametro3.setDisable(true);
        parametro4.setDisable(true);
        parametro5.setDisable(true);
        parametro6.setDisable(true);
        parametro7.setDisable(true);
        booleano1.setDisable(true);
        booleano2.setDisable(true);
        booleano3.setDisable(true);

        operazioneCorrente = "operazione5";

        query = "{CALL RimuoviCollezione(?, ?)}";

        Connection con = null;
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            // inserisco nel box "prima" la tabella prima della query
            con = DriverManager.getConnection(connectionString, username, password);
            stmt = con.createStatement();
            rslt = stmt.executeQuery(
                    "SELECT C.*, CO.nickname FROM Collezione C, Collezionista CO WHERE C.id_collezionista = CO.id ORDER BY C.id");

            String resultString = "ID, NOME, ISPUBBLICA, OWNER \n\n";
            while (rslt.next()) {
                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                        + "; "
                        + rslt.getString(5) + "\n";
            }

            primaTextArea.setText(resultString);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rslt != null) {
                try {
                    rslt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @FXML
    private void operazione6(ActionEvent event) {
        reset();
        labelP1.setText("Nickname Owner");
        labelP2.setText("Nome collezione");
        parametro3.setDisable(true);
        parametro4.setDisable(true);
        parametro5.setDisable(true);
        parametro6.setDisable(true);
        parametro7.setDisable(true);
        booleano1.setDisable(true);
        booleano2.setDisable(true);
        booleano3.setDisable(true);

        operazioneCorrente = "operazione6";

        query = "{CALL DischiInCollezione(?, ?)}";

        Connection con = null;
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            // inserisco nel box "prima" la tabella prima della query
            con = DriverManager.getConnection(connectionString, username, password);
            stmt = con.createStatement();
            rslt = stmt.executeQuery(
                    "SELECT C.*, CO.nickname FROM Collezione C, Collezionista CO WHERE C.id_collezionista = CO.id ORDER BY C.id");

            String resultString = "ID, NOME, ISPUBBLICA, OWNER \n\n";
            while (rslt.next()) {
                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                        + "; "
                        + rslt.getString(5) + "\n";
            }

            primaTextArea.setText(resultString);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rslt != null) {
                try {
                    rslt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @FXML
    private void operazione7(ActionEvent event) {
        reset();
        labelP1.setText("EAN disco");
        parametro2.setDisable(true);
        parametro3.setDisable(true);
        parametro4.setDisable(true);
        parametro5.setDisable(true);
        parametro6.setDisable(true);
        parametro7.setDisable(true);
        booleano1.setDisable(true);
        booleano2.setDisable(true);
        booleano3.setDisable(true);

        operazioneCorrente = "operazione7";

        query = "{CALL TrackList(?)}";

        Connection con = null;
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            // inserisco nel box "prima" la tabella prima della query
            con = DriverManager.getConnection(connectionString, username, password);
            stmt = con.createStatement();
            rslt = stmt.executeQuery(
                    "CALL DischiAutori()");

            String resultString = "DISCO, EAN, FORMATO, AUTORE PRINCIPALE \n\n";
            while (rslt.next()) {
                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                        + "; "
                        + rslt.getString(4) + "\n";
            }

            primaTextArea.setText(resultString);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rslt != null) {
                try {
                    rslt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @FXML
    private void operazione8(ActionEvent event) {
        reset();
        labelP1.setText("Nome autore");
        parametro1.setText("null");
        labelP2.setText("Titolo disco");
        parametro2.setText("null");
        labelP3.setText("Nome collezionista");
        parametro4.setDisable(true);
        parametro5.setDisable(true);
        parametro6.setDisable(true);
        parametro7.setDisable(true);

        booleano1.setText("Private");
        booleano2.setText("Condivise");
        booleano3.setText("Pubbliche");

        operazioneCorrente = "operazione8";

        query = "{CALL RicercaDischi(?,?,?,?,?,?)}";

        Connection con = null;
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            // inserisco nel box "prima" la tabella prima della query
            con = DriverManager.getConnection(connectionString, username, password);
            stmt = con.createStatement();
            rslt = stmt.executeQuery(
                    "CALL CollezioniCopie()");

            String resultString = "COLLEZIONISTA, COLLEZIONE, SN, STATO, DOPPIONI, ISPUBBLICA, DISCO, EAN, AUTORE \n\n";
            while (rslt.next()) {
                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                        + "; "
                        + rslt.getString(4) + "; " + rslt.getString(5) + "; " + rslt.getString(6) + "; "
                        + rslt.getString(7) + "; " + rslt.getString(8) + "; " + rslt.getString(9)
                        + "\n";
            }

            primaTextArea.setText(resultString);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rslt != null) {
                try {
                    rslt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @FXML
    private void operazione9(ActionEvent event) {
        reset();
        labelP1.setText("Nome collezione");
        labelP2.setText("Nickname owner");
        labelP3.setText("Nickname guest");
        parametro4.setDisable(true);
        parametro5.setDisable(true);
        parametro6.setDisable(true);
        parametro7.setDisable(true);

        booleano1.setDisable(true);
        booleano2.setDisable(true);
        booleano3.setDisable(true);

        operazioneCorrente = "operazione9";

        query = "{CALL CheckVisibilita(?,?,?)}";

        Connection con = null;
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            // inserisco nel box "prima" la tabella prima della query
            con = DriverManager.getConnection(connectionString, username, password);
            stmt = con.createStatement();
            rslt = stmt.executeQuery(
                    "CALL Condivisioni()");

            String resultString = "OWNER, COLLEZIONE, GUEST \n\n";
            while (rslt.next()) {
                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                        + "\n";
            }

            primaTextArea.setText(resultString);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rslt != null) {
                try {
                    rslt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @FXML
    private void operazione10(ActionEvent event) {
        reset();
        labelP1.setText("Autore");
        parametro2.setDisable(true);
        parametro3.setDisable(true);
        parametro4.setDisable(true);
        parametro5.setDisable(true);
        parametro6.setDisable(true);
        parametro7.setDisable(true);

        booleano1.setDisable(true);
        booleano2.setDisable(true);
        booleano3.setDisable(true);

        operazioneCorrente = "operazione10";

        query = "{CALL ContaBraniByAutore(?)}";

        Connection con = null;
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            // inserisco nel box "prima" la tabella prima della query
            con = DriverManager.getConnection(connectionString, username, password);
            stmt = con.createStatement();
            rslt = stmt.executeQuery(
                    "CALL CollezioniCopie()");

            String resultString = "COLLEZIONISTA, COLLEZIONE, SN, STATO, DOPPIONI, ISPUBBLICA, DISCO, EAN, AUTORE \n\n";
            while (rslt.next()) {
                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                        + "; "
                        + rslt.getString(4) + "; " + rslt.getString(5) + "; " + rslt.getString(6) + "; "
                        + rslt.getString(7) + "; " + rslt.getString(8) + "; " + rslt.getString(9) + "\n";
            }

            primaTextArea.setText(resultString);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rslt != null) {
                try {
                    rslt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @FXML
    private void operazione11(ActionEvent event) {
        // Logica per l'operazione 11
    }

    @FXML
    private void operazione12a(ActionEvent event) {
        // Logica per l'operazione 12a
    }

    @FXML
    private void operazione12b(ActionEvent event) {
        // Logica per l'operazione 12b
    }

    @FXML
    private void operazione13(ActionEvent event) {
        // Logica per l'operazione 13
    }

    @FXML
    public void esegui(ActionEvent event) {

        switch (operazioneCorrente) {
            case "operazione1":
                if (parametro1.getText().isEmpty() || parametro2.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else {
                    Boolean error = false;
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;

                    try {
                        con = DriverManager.getConnection(connectionString, username, password);
                        cllstmt = con.prepareCall(query);

                        cllstmt.setString(1, parametro1.getText());
                        cllstmt.setString(3, parametro2.getText());
                        cllstmt.setBoolean(2, booleano1.selectedProperty().getValue());

                        cllstmt.execute();

                    } catch (SQLException e) {
                        primaTextArea.setText(e.getMessage());
                        error = true;
                    } finally {
                        if (rslt != null) {
                            try {
                                rslt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (cllstmt != null) {
                            try {
                                cllstmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    if (!error) {
                        try {
                            // inserisco nel box "dopo" la tabella dopo la query
                            con = DriverManager.getConnection(connectionString, username, password);
                            stmt = con.createStatement();
                            rslt = stmt.executeQuery(
                                    "SELECT C.*, CO.nickname FROM Collezione C, Collezionista CO WHERE C.id_collezionista = CO.id ORDER BY C.id");

                            String resultString = "ID, NOME, ISPUBBLICA, OWNER \n";
                            while (rslt.next()) {
                                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                                        + "; "
                                        + rslt.getString(5) + "\n";
                            }

                            dopoTextArea.setText(resultString);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (rslt != null) {
                                try {
                                    rslt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (stmt != null) {
                                try {
                                    stmt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (con != null) {
                                try {
                                    con.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
                break;

            case "operazione2a":

                if (parametro1.getText().isEmpty() || parametro2.getText().isEmpty() || parametro3.getText().isEmpty()
                        || parametro4.getText().isEmpty() || parametro5.getText().isEmpty()
                        || parametro6.getText().isEmpty() || parametro7.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else {
                    Boolean error = false;
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;

                    try {
                        con = DriverManager.getConnection(connectionString, username, password);
                        cllstmt = con.prepareCall(query);

                        cllstmt.setString(1, parametro1.getText());
                        cllstmt.setString(2, parametro2.getText());
                        cllstmt.setInt(3, Integer.parseInt(parametro3.getText()));
                        cllstmt.setString(4, parametro4.getText());
                        cllstmt.setString(5, parametro5.getText());
                        cllstmt.setString(6, parametro6.getText());
                        cllstmt.setString(7, parametro7.getText());

                        cllstmt.execute();
                    } catch (SQLException e) {
                        primaTextArea.setText(e.getMessage());
                        error = true;
                    } finally {
                        if (cllstmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    if (!error) {
                        try {
                            // inserisco nel box "dopo" la tabella dopo la query
                            con = DriverManager.getConnection(connectionString, username, password);
                            stmt = con.createStatement();
                            rslt = stmt.executeQuery(
                                    "CALL DischiAutori()");

                            String resultString = "DISCO, EAN, FORMATO, AUTORE PRINCIPALE \n\n";
                            while (rslt.next()) {
                                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                                        + "; "
                                        + rslt.getString(4) + "\n";
                            }

                            dopoTextArea.setText(resultString);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (rslt != null) {
                                try {
                                    rslt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (stmt != null) {
                                try {
                                    stmt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (con != null) {
                                try {
                                    con.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
                break;

            case "operazione2d":
                if (parametro1.getText().isEmpty() || parametro2.getText().isEmpty() || parametro3.getText().isEmpty()
                        || parametro4.getText().isEmpty() || parametro5.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else {
                    Boolean error = false;
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;

                    try {
                        con = DriverManager.getConnection(connectionString, username, password);
                        cllstmt = con.prepareCall(query);

                        cllstmt.setString(1, parametro1.getText());
                        cllstmt.setString(2, parametro2.getText());
                        cllstmt.setString(3, parametro3.getText());
                        cllstmt.setString(4, parametro4.getText());
                        cllstmt.setString(5, parametro5.getText());

                        cllstmt.execute();
                    } catch (SQLException e) {
                        primaTextArea.setText(e.getMessage());
                        error = true;
                    } finally {
                        if (cllstmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    if (!error) {
                        try {
                            // inserisco nel box "dopo" la tabella dopo la query
                            con = DriverManager.getConnection(connectionString, username, password);
                            stmt = con.createStatement();

                            rslt = stmt.executeQuery(
                                    "CALL CollezioniCopie()");

                            String resultString = "COLLEZIONISTA, COLLEZIONE, SN, STATO, DOPPIONI, ISPUBBLICA, DISCO, EAN, AUTORE \n\n";
                            while (rslt.next()) {
                                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                                        + "; "
                                        + rslt.getString(4) + "; " + rslt.getString(5) + "; " + rslt.getString(6) + "; "
                                        + rslt.getString(7) + "; " + rslt.getString(8) + "; " + rslt.getString(9)
                                        + "\n";
                            }

                            dopoTextArea.setText(resultString);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (rslt != null) {
                                try {
                                    rslt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (stmt != null) {
                                try {
                                    stmt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (con != null) {
                                try {
                                    con.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
                break;

            case "operazione2e":
                if (parametro1.getText().isEmpty() || parametro2.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else {
                    Boolean error = false;
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;

                    try {
                        con = DriverManager.getConnection(connectionString, username, password);
                        cllstmt = con.prepareCall(query);

                        cllstmt.setString(1, parametro1.getText());
                        cllstmt.setString(2, parametro2.getText());

                        cllstmt.execute();
                    } catch (SQLException e) {
                        primaTextArea.setText(e.getMessage());
                        error = true;
                    } finally {
                        if (cllstmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    if (!error) {
                        try {
                            // inserisco nel box "dopo" la tabella dopo la query
                            con = DriverManager.getConnection(connectionString, username, password);
                            stmt = con.createStatement();

                            rslt = stmt.executeQuery(
                                    "CALL CollezioniCopie()");

                            String resultString = "COLLEZIONISTA, COLLEZIONE, SN, STATO, DOPPIONI, ISPUBBLICA, DISCO, EAN, AUTORE \n\n";
                            while (rslt.next()) {
                                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                                        + "; "
                                        + rslt.getString(4) + "; " + rslt.getString(5) + "; " + rslt.getString(6) + "; "
                                        + rslt.getString(7) + "; " + rslt.getString(8) + "; " + rslt.getString(9)
                                        + "\n";
                            }

                            dopoTextArea.setText(resultString);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (rslt != null) {
                                try {
                                    rslt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (stmt != null) {
                                try {
                                    stmt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (con != null) {
                                try {
                                    con.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
                break;

            case "operazione2f":
                if (parametro1.getText().isEmpty() || parametro2.getText().isEmpty() || parametro3.getText().isEmpty()
                        || parametro4.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else if (parametro3.getText().length() > 6 || !isInteger(parametro3.getText())) {
                    dopoTextArea.setText("SCIVERE LA DURATA NEL FORMATO HHMMSS");

                } else {
                    Boolean error = false;
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;

                    try {
                        con = DriverManager.getConnection(connectionString, username, password);
                        cllstmt = con.prepareCall(query);

                        cllstmt.setString(1, parametro1.getText());
                        cllstmt.setString(2, parametro2.getText());
                        cllstmt.setInt(3, Integer.parseInt(parametro3.getText()));
                        cllstmt.setString(4, parametro4.getText());

                        cllstmt.execute();
                    } catch (SQLException e) {
                        primaTextArea.setText(e.getMessage());
                        error = true;
                    } finally {
                        if (cllstmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    if (!error) {
                        try {
                            // inserisco nel box "dopo" la tabella dopo la query
                            con = DriverManager.getConnection(connectionString, username, password);
                            stmt = con.createStatement();
                            rslt = stmt.executeQuery(
                                    "CALL DischiTracce()");

                            String resultString = "DISCO, EAN, TRACCIA, ISRC, DURATA, AUTORE PRINCIPALE \n\n";
                            while (rslt.next()) {
                                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                                        + "; "
                                        + rslt.getString(4) + "; " + rslt.getString(5) + "; " + rslt.getString(6)
                                        + "\n";
                            }

                            dopoTextArea.setText(resultString);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (rslt != null) {
                                try {
                                    rslt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (stmt != null) {
                                try {
                                    stmt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (con != null) {
                                try {
                                    con.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
                break;

            case "operazione2g":
                if (parametro1.getText().isEmpty() || parametro2.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else {
                    Boolean error = false;
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;

                    try {
                        con = DriverManager.getConnection(connectionString, username, password);
                        cllstmt = con.prepareCall(query);

                        cllstmt.setString(1, parametro1.getText());
                        cllstmt.setString(2, parametro2.getText());

                        cllstmt.execute();
                    } catch (SQLException e) {
                        primaTextArea.setText(e.getMessage());
                        error = true;
                    } finally {
                        if (cllstmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    if (!error) {
                        try {
                            // inserisco nel box "dopo" la tabella dopo la query
                            con = DriverManager.getConnection(connectionString, username, password);
                            stmt = con.createStatement();
                            rslt = stmt.executeQuery(
                                    "CALL DischiTracce()");

                            String resultString = "DISCO, EAN, TRACCIA, ISRC, DURATA, AUTORE \n\n";
                            while (rslt.next()) {
                                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                                        + "; "
                                        + rslt.getString(4) + "; "
                                        + rslt.getString(5) + "; "
                                        + rslt.getString(6) + "\n";
                            }

                            dopoTextArea.setText(resultString);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (rslt != null) {
                                try {
                                    rslt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (stmt != null) {
                                try {
                                    stmt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (con != null) {
                                try {
                                    con.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
                break;

            case "operazione3a":
                if (parametro1.getText().isEmpty() || parametro2.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else {
                    Boolean error = false;
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;

                    try {
                        con = DriverManager.getConnection(connectionString, username, password);
                        cllstmt = con.prepareCall(query);

                        cllstmt.setString(1, parametro1.getText());
                        cllstmt.setString(2, parametro2.getText());

                        cllstmt.execute();
                    } catch (SQLException e) {
                        primaTextArea.setText(e.getMessage());
                        error = true;
                    } finally {
                        if (cllstmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    if (!error) {
                        try {
                            // inserisco nel box "dopo" la tabella dopo la query
                            con = DriverManager.getConnection(connectionString, username, password);
                            stmt = con.createStatement();

                            rslt = stmt.executeQuery(
                                    "SELECT C.*, CO.nickname FROM Collezione C, Collezionista CO WHERE C.id_collezionista = CO.id ORDER BY C.id");

                            String resultString = "ID, NOME, ISPUBBLICA, OWNER \\n";
                            while (rslt.next()) {
                                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                                        + "; "
                                        + rslt.getString(5) + "\n";
                            }

                            dopoTextArea.setText(resultString);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (rslt != null) {
                                try {
                                    rslt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (stmt != null) {
                                try {
                                    stmt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (con != null) {
                                try {
                                    con.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
                break;

            case "operazione3b":
                if (parametro1.getText().isEmpty() || parametro2.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else {
                    Boolean error = false;
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;

                    try {
                        con = DriverManager.getConnection(connectionString, username, password);
                        cllstmt = con.prepareCall(query);

                        cllstmt.setString(1, parametro1.getText());
                        cllstmt.setString(2, parametro2.getText());
                        cllstmt.setString(3, parametro3.getText());

                        cllstmt.execute();
                    } catch (SQLException e) {
                        primaTextArea.setText(e.getMessage());
                        error = true;
                    } finally {
                        if (cllstmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    if (!error) {
                        try {
                            // inserisco nel box "dopo" la tabella dopo la query
                            con = DriverManager.getConnection(connectionString, username, password);
                            stmt = con.createStatement();

                            rslt = stmt.executeQuery(
                                    "CALL Condivisioni()");

                            String resultString = "OWNER, COLLEZIONE, GUEST \n\n";
                            while (rslt.next()) {
                                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                                        + "\n";
                            }

                            dopoTextArea.setText(resultString);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (rslt != null) {
                                try {
                                    rslt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (stmt != null) {
                                try {
                                    stmt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (con != null) {
                                try {
                                    con.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
                break;

            case "operazione4":
                if (parametro1.getText().isEmpty() || parametro2.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else {
                    Boolean error = false;
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;

                    try {
                        con = DriverManager.getConnection(connectionString, username, password);
                        cllstmt = con.prepareCall(query);

                        cllstmt.setString(1, parametro1.getText());
                        cllstmt.setString(2, parametro2.getText());

                        cllstmt.execute();
                    } catch (SQLException e) {
                        primaTextArea.setText(e.getMessage());
                        error = true;
                    } finally {
                        if (cllstmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    if (!error) {
                        try {
                            // inserisco nel box "dopo" la tabella dopo la query
                            con = DriverManager.getConnection(connectionString, username, password);
                            stmt = con.createStatement();

                            rslt = stmt.executeQuery(
                                    "CALL CollezioniCopie()");

                            String resultString = "COLLEZIONISTA, COLLEZIONE, SN, STATO, DOPPIONI, ISPUBBLICA, DISCO, EAN, AUTORE \n\n";
                            while (rslt.next()) {
                                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                                        + "; "
                                        + rslt.getString(4) + "; " + rslt.getString(5) + "; " + rslt.getString(6) + "; "
                                        + rslt.getString(7) + "; " + rslt.getString(8) + "; " + rslt.getString(9)
                                        + "\n";
                            }

                            dopoTextArea.setText(resultString);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (rslt != null) {
                                try {
                                    rslt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (stmt != null) {
                                try {
                                    stmt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (con != null) {
                                try {
                                    con.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
                break;

            case "operazione5":
                if (parametro1.getText().isEmpty() || parametro2.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else {
                    Boolean error = false;
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;

                    try {
                        con = DriverManager.getConnection(connectionString, username, password);
                        cllstmt = con.prepareCall(query);

                        cllstmt.setString(1, parametro1.getText());
                        cllstmt.setString(2, parametro2.getText());

                        cllstmt.execute();
                    } catch (SQLException e) {
                        primaTextArea.setText(e.getMessage());
                        error = true;
                    } finally {
                        if (cllstmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    if (!error) {
                        try {
                            // inserisco nel box "dopo" la tabella dopo la query
                            con = DriverManager.getConnection(connectionString, username, password);
                            stmt = con.createStatement();

                            rslt = stmt.executeQuery(
                                    "SELECT C.*, CO.nickname FROM Collezione C, Collezionista CO WHERE C.id_collezionista = CO.id ORDER BY C.id");

                            String resultString = "ID, NOME, ISPUBBLICA, OWNER \\n";
                            while (rslt.next()) {
                                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                                        + "; "
                                        + rslt.getString(5) + "\n";
                            }

                            dopoTextArea.setText(resultString);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (rslt != null) {
                                try {
                                    rslt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (stmt != null) {
                                try {
                                    stmt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (con != null) {
                                try {
                                    con.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
                break;

            case "operazione6":
                if (parametro1.getText().isEmpty() || parametro2.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else {
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;

                    try {
                        // inserisco nel box "dopo" la tabella dopo la query
                        con = DriverManager.getConnection(connectionString, username, password);

                        cllstmt = con.prepareCall(query);

                        cllstmt.setString(1, parametro1.getText());
                        cllstmt.setString(2, parametro2.getText());

                        rslt = cllstmt.executeQuery();

                        String resultString = "DISCO, EAN \n\n";
                        while (rslt.next()) {
                            resultString += rslt.getString(3) + "; " + rslt.getString(2) + "\n";
                        }

                        dopoTextArea.setText(resultString);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (rslt != null) {
                            try {
                                rslt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (stmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
                break;

            case "operazione7":
                if (parametro1.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else {
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;

                    try {
                        // inserisco nel box "dopo" la tabella dopo la query
                        con = DriverManager.getConnection(connectionString, username, password);

                        cllstmt = con.prepareCall(query);

                        cllstmt.setString(1, parametro1.getText());

                        rslt = cllstmt.executeQuery();

                        String resultString = "TRACCIA, ISRC, DURATA \n\n";
                        while (rslt.next()) {
                            resultString += rslt.getString(2) + "; " + rslt.getString(3) + "; " + rslt.getString(4)
                                    + "\n";
                        }

                        dopoTextArea.setText(resultString);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (rslt != null) {
                            try {
                                rslt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (stmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
                break;

            case "operazione8":
                if (parametro1.getText().isEmpty() || parametro2.getText().isEmpty()
                        || parametro3.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else {
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;

                    try {
                        // inserisco nel box "dopo" la tabella dopo la query
                        con = DriverManager.getConnection(connectionString, username, password);

                        cllstmt = con.prepareCall(query);

                        if (parametro1.getText().equals("null"))
                            cllstmt.setString(1, null);
                        else
                            cllstmt.setString(1, parametro1.getText());
                        if (parametro2.getText().equals("null"))
                            cllstmt.setString(2, null);
                        else
                            cllstmt.setString(2, parametro2.getText());

                        cllstmt.setString(3, parametro3.getText());
                        cllstmt.setBoolean(4, booleano1.selectedProperty().getValue());
                        cllstmt.setBoolean(5, booleano2.selectedProperty().getValue());
                        cllstmt.setBoolean(6, booleano3.selectedProperty().getValue());

                        rslt = cllstmt.executeQuery();

                        String resultString = "DISCO, EAN \n\n";
                        while (rslt.next()) {
                            resultString += rslt.getString(3) + "; " + rslt.getString(2) + "\n";
                        }

                        dopoTextArea.setText(resultString);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (rslt != null) {
                            try {
                                rslt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (stmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
                break;

            case "operazione9":
                if (parametro1.getText().isEmpty() || parametro2.getText().isEmpty()
                        || parametro3.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else {
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;

                    try {
                        // inserisco nel box "dopo" la tabella dopo la query
                        con = DriverManager.getConnection(connectionString, username, password);

                        cllstmt = con.prepareCall(query);

                        cllstmt.setString(1, parametro1.getText());
                        cllstmt.setString(2, parametro2.getText());

                        cllstmt.setString(3, parametro3.getText());

                        rslt = cllstmt.executeQuery();

                        String resultString = "";
                        while (rslt.next()) {
                            resultString += rslt.getString(1) + "\n";
                        }

                        dopoTextArea.setText(resultString);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (rslt != null) {
                            try {
                                rslt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (stmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
                break;

            case "operazione10":
                if (parametro1.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else {
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;

                    try {
                        // inserisco nel box "dopo" la tabella dopo la query
                        con = DriverManager.getConnection(connectionString, username, password);

                        cllstmt = con.prepareCall(query);

                        cllstmt.setString(1, parametro1.getText());
                        
                        rslt = cllstmt.executeQuery();

                        String resultString = "";
                        while (rslt.next()) {
                            resultString += rslt.getString(1) + "\n";
                        }

                        dopoTextArea.setText(resultString);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (rslt != null) {
                            try {
                                rslt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (stmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
                break;

            case "operazione11":
                if (parametro1.getText().isEmpty() || parametro2.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else {
                    Boolean error = false;
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;
                    try {
                        // inserisco nel box "prima" la tabella prima della query
                        con = DriverManager.getConnection(connectionString, username, password);
                        stmt = con.createStatement();
                        rslt = stmt.executeQuery(
                                "CALL CollezioniCopie()");

                        String resultString = "COLLEZIONISTA, COLLEZIONE, SN, STATO, DOPPIONI, ISPUBBLICA, DISCO, EAN, AUTORE \n\n";
                        while (rslt.next()) {
                            resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                                    + "; "
                                    + rslt.getString(4) + "; " + rslt.getString(5) + "; " + rslt.getString(6) + "; "
                                    + rslt.getString(7) + "; " + rslt.getString(8) + "; " + rslt.getString(9) + "\n";
                        }

                        primaTextArea.setText(resultString);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (rslt != null) {
                            try {
                                rslt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (stmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    try {
                        con = DriverManager.getConnection(connectionString, username, password);
                        cllstmt = con.prepareCall(query);

                        cllstmt.setString(1, parametro1.getText());
                        cllstmt.setString(2, parametro2.getText());

                        cllstmt.execute();
                    } catch (SQLException e) {
                        primaTextArea.setText(e.getMessage());
                        error = true;
                    } finally {
                        if (cllstmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    if (!error) {
                        try {
                            // inserisco nel box "dopo" la tabella dopo la query
                            con = DriverManager.getConnection(connectionString, username, password);
                            stmt = con.createStatement();

                            rslt = stmt.executeQuery(
                                    "CALL CollezioniCopie()");

                            String resultString = "COLLEZIONISTA, COLLEZIONE, SN, STATO, DOPPIONI, ISPUBBLICA, DISCO, EAN, AUTORE \n\n";
                            while (rslt.next()) {
                                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                                        + "; "
                                        + rslt.getString(4) + "; " + rslt.getString(5) + "; " + rslt.getString(6) + "; "
                                        + rslt.getString(7) + "; " + rslt.getString(8) + "; " + rslt.getString(9)
                                        + "\n";
                            }

                            dopoTextArea.setText(resultString);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (rslt != null) {
                                try {
                                    rslt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (stmt != null) {
                                try {
                                    stmt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (con != null) {
                                try {
                                    con.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
                break;

            case "operazione12a":
                if (parametro1.getText().isEmpty() || parametro2.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else {
                    Boolean error = false;
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;
                    try {
                        // inserisco nel box "prima" la tabella prima della query
                        con = DriverManager.getConnection(connectionString, username, password);
                        stmt = con.createStatement();
                        rslt = stmt.executeQuery(
                                "CALL CollezioniCopie()");

                        String resultString = "COLLEZIONISTA, COLLEZIONE, SN, STATO, DOPPIONI, ISPUBBLICA, DISCO, EAN, AUTORE \n\n";
                        while (rslt.next()) {
                            resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                                    + "; "
                                    + rslt.getString(4) + "; " + rslt.getString(5) + "; " + rslt.getString(6) + "; "
                                    + rslt.getString(7) + "; " + rslt.getString(8) + "; " + rslt.getString(9) + "\n";
                        }

                        primaTextArea.setText(resultString);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (rslt != null) {
                            try {
                                rslt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (stmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    try {
                        con = DriverManager.getConnection(connectionString, username, password);
                        cllstmt = con.prepareCall(query);

                        cllstmt.setString(1, parametro1.getText());
                        cllstmt.setString(2, parametro2.getText());

                        cllstmt.execute();
                    } catch (SQLException e) {
                        primaTextArea.setText(e.getMessage());
                        error = true;
                    } finally {
                        if (cllstmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    if (!error) {
                        try {
                            // inserisco nel box "dopo" la tabella dopo la query
                            con = DriverManager.getConnection(connectionString, username, password);
                            stmt = con.createStatement();

                            rslt = stmt.executeQuery(
                                    "CALL CollezioniCopie()");

                            String resultString = "COLLEZIONISTA, COLLEZIONE, SN, STATO, DOPPIONI, ISPUBBLICA, DISCO, EAN, AUTORE \n\n";
                            while (rslt.next()) {
                                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                                        + "; "
                                        + rslt.getString(4) + "; " + rslt.getString(5) + "; " + rslt.getString(6) + "; "
                                        + rslt.getString(7) + "; " + rslt.getString(8) + "; " + rslt.getString(9)
                                        + "\n";
                            }

                            dopoTextArea.setText(resultString);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (rslt != null) {
                                try {
                                    rslt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (stmt != null) {
                                try {
                                    stmt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (con != null) {
                                try {
                                    con.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
                break;

            case "operazione12b":
                if (parametro1.getText().isEmpty() || parametro2.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else {
                    Boolean error = false;
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;
                    try {
                        // inserisco nel box "prima" la tabella prima della query
                        con = DriverManager.getConnection(connectionString, username, password);
                        stmt = con.createStatement();
                        rslt = stmt.executeQuery(
                                "CALL CollezioniCopie()");

                        String resultString = "COLLEZIONISTA, COLLEZIONE, SN, STATO, DOPPIONI, ISPUBBLICA, DISCO, EAN, AUTORE \n\n";
                        while (rslt.next()) {
                            resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                                    + "; "
                                    + rslt.getString(4) + "; " + rslt.getString(5) + "; " + rslt.getString(6) + "; "
                                    + rslt.getString(7) + "; " + rslt.getString(8) + "; " + rslt.getString(9) + "\n";
                        }

                        primaTextArea.setText(resultString);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (rslt != null) {
                            try {
                                rslt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (stmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    try {
                        con = DriverManager.getConnection(connectionString, username, password);
                        cllstmt = con.prepareCall(query);

                        cllstmt.setString(1, parametro1.getText());
                        cllstmt.setString(2, parametro2.getText());

                        cllstmt.execute();
                    } catch (SQLException e) {
                        primaTextArea.setText(e.getMessage());
                        error = true;
                    } finally {
                        if (cllstmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    if (!error) {
                        try {
                            // inserisco nel box "dopo" la tabella dopo la query
                            con = DriverManager.getConnection(connectionString, username, password);
                            stmt = con.createStatement();

                            rslt = stmt.executeQuery(
                                    "CALL CollezioniCopie()");

                            String resultString = "COLLEZIONISTA, COLLEZIONE, SN, STATO, DOPPIONI, ISPUBBLICA, DISCO, EAN, AUTORE \n\n";
                            while (rslt.next()) {
                                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                                        + "; "
                                        + rslt.getString(4) + "; " + rslt.getString(5) + "; " + rslt.getString(6) + "; "
                                        + rslt.getString(7) + "; " + rslt.getString(8) + "; " + rslt.getString(9)
                                        + "\n";
                            }

                            dopoTextArea.setText(resultString);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (rslt != null) {
                                try {
                                    rslt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (stmt != null) {
                                try {
                                    stmt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (con != null) {
                                try {
                                    con.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
                break;

            case "operazione13":
                if (parametro1.getText().isEmpty() || parametro2.getText().isEmpty()) {
                    dopoTextArea.setText("RIEMPIRE TUTTI I CAMPI");
                } else {
                    Boolean error = false;
                    Connection con = null;
                    Statement stmt = null;
                    CallableStatement cllstmt = null;
                    ResultSet rslt = null;
                    try {
                        // inserisco nel box "prima" la tabella prima della query
                        con = DriverManager.getConnection(connectionString, username, password);
                        stmt = con.createStatement();
                        rslt = stmt.executeQuery(
                                "CALL CollezioniCopie()");

                        String resultString = "COLLEZIONISTA, COLLEZIONE, SN, STATO, DOPPIONI, ISPUBBLICA, DISCO, EAN, AUTORE \n\n";
                        while (rslt.next()) {
                            resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                                    + "; "
                                    + rslt.getString(4) + "; " + rslt.getString(5) + "; " + rslt.getString(6) + "; "
                                    + rslt.getString(7) + "; " + rslt.getString(8) + "; " + rslt.getString(9) + "\n";
                        }

                        primaTextArea.setText(resultString);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (rslt != null) {
                            try {
                                rslt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (stmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    try {
                        con = DriverManager.getConnection(connectionString, username, password);
                        cllstmt = con.prepareCall(query);

                        cllstmt.setString(1, parametro1.getText());
                        cllstmt.setString(2, parametro2.getText());

                        cllstmt.execute();
                    } catch (SQLException e) {
                        primaTextArea.setText(e.getMessage());
                        error = true;
                    } finally {
                        if (cllstmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    if (!error) {
                        try {
                            // inserisco nel box "dopo" la tabella dopo la query
                            con = DriverManager.getConnection(connectionString, username, password);
                            stmt = con.createStatement();

                            rslt = stmt.executeQuery(
                                    "CALL CollezioniCopie()");

                            String resultString = "COLLEZIONISTA, COLLEZIONE, SN, STATO, DOPPIONI, ISPUBBLICA, DISCO, EAN, AUTORE \n\n";
                            while (rslt.next()) {
                                resultString += rslt.getString(1) + "; " + rslt.getString(2) + "; " + rslt.getString(3)
                                        + "; "
                                        + rslt.getString(4) + "; " + rslt.getString(5) + "; " + rslt.getString(6) + "; "
                                        + rslt.getString(7) + "; " + rslt.getString(8) + "; " + rslt.getString(9)
                                        + "\n";
                            }

                            dopoTextArea.setText(resultString);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (rslt != null) {
                                try {
                                    rslt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (stmt != null) {
                                try {
                                    stmt.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (con != null) {
                                try {
                                    con.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
                break;

            default:
                break;
        }

    }

    private void reset() {
        parametro1.setDisable(false);
        parametro2.setDisable(false);
        parametro3.setDisable(false);
        parametro4.setDisable(false);
        parametro5.setDisable(false);
        parametro6.setDisable(false);
        parametro7.setDisable(false);
        booleano1.setDisable(false);
        booleano2.setDisable(false);
        booleano3.setDisable(false);

        labelP1.setText("Parametro 1");
        labelP2.setText("Parametro 2");
        labelP3.setText("Parametro 3");
        labelP4.setText("Parametro 4");
        labelP5.setText("Parametro 5");
        labelP6.setText("Parametro 6");
        labelP7.setText("Parametro 7");
        booleano1.setText("Booleano 1");
        booleano2.setText("Booleano 2");
        booleano3.setText("Booleano 3");

        parametro1.clear();
        parametro2.clear();
        parametro3.clear();
        parametro4.clear();
        parametro5.clear();
        parametro6.clear();
        parametro7.clear();
        booleano1.setSelected(false);
        booleano2.setSelected(false);
        booleano3.setSelected(false);

        primaTextArea.clear();
        dopoTextArea.clear();
    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true; // La stringa è un intero valido
        } catch (NumberFormatException e) {
            return false; // La stringa non è un intero
        }
    }
}