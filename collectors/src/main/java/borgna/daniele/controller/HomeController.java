package borgna.daniele.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    }

    @FXML
    private void operazione2d(ActionEvent event) {
       
    }

    @FXML
    private void operazione2e(ActionEvent event) {
        // Logica per l'operazione 2e
    }

    @FXML
    private void operazione2f(ActionEvent event) {
        // Logica per l'operazione 2f
    }

    @FXML
    private void operazione2g(ActionEvent event) {
        // Logica per l'operazione 2g
    }

    @FXML
    private void operazione3a(ActionEvent event) {
        // Logica per l'operazione 3a
    }

    @FXML
    private void operazione3b(ActionEvent event) {
        // Logica per l'operazione 3b
    }

    @FXML
    private void operazione4(ActionEvent event) {
        // Logica per l'operazione 4
    }

    @FXML
    private void operazione5(ActionEvent event) {
        // Logica per l'operazione 5
    }

    @FXML
    private void operazione6(ActionEvent event) {
        // Logica per l'operazione 6
    }

    @FXML
    private void operazione7(ActionEvent event) {
        // Logica per l'operazione 7
    }

    @FXML
    private void operazione8(ActionEvent event) {
        // Logica per l'operazione 8
    }

    @FXML
    private void operazione9(ActionEvent event) {
        // Logica per l'operazione 9
    }

    @FXML
    private void operazione10(ActionEvent event) {
        // Logica per l'operazione 10
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
                    primaTextArea.setText("RIEMPIRE TUTTI I CAMPI");
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
                                "SELECT C.*, CO.nickname FROM Collezione C, Collezionista CO WHERE C.id_collezionista = CO.id ORDER BY C.id");

                        String resultString = "ID, NOME, ISPUBBLICA, OWNER \\n";
                        while (rslt.next()) {
                            resultString += rslt.getString(1) + " " + rslt.getString(2) + " " + rslt.getString(3) + " "
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
                            // inserisco nel box "dopo" la tabella dopo della query
                            con = DriverManager.getConnection(connectionString, username, password);
                            stmt = con.createStatement();
                            rslt = stmt.executeQuery(
                                    "SELECT C.*, CO.nickname FROM Collezione C, Collezionista CO WHERE C.id_collezionista = CO.id ORDER BY C.id");

                            String resultString = "ID, NOME, ISPUBBLICA, OWNER \n";
                            while (rslt.next()) {
                                resultString += rslt.getString(1) + " " + rslt.getString(2) + " " + rslt.getString(3)
                                        + " "
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
                    primaTextArea.setText("RIEMPIRE TUTTI I CAMPI");
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
                                "CALL DischiAutori()");

                        String resultString = "DISCO, EAN, FORMATO, AUTORE PRINCIPALE \n\n";
                        while (rslt.next()) {
                            resultString += rslt.getString(1) + " " + rslt.getString(2) + " " + rslt.getString(3) + " "
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
                            // inserisco nel box "dopo" la tabella dopo della query
                            con = DriverManager.getConnection(connectionString, username, password);
                            stmt = con.createStatement();
                            rslt = stmt.executeQuery(
                                    "CALL DischiAutori()");

                            String resultString = "DISCO, EAN, FORMATO, AUTORE PRINCIPALE \n\n";
                            while (rslt.next()) {
                                resultString += rslt.getString(1) + " " + rslt.getString(2) + " " + rslt.getString(3)
                                        + " "
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
                // Body of the operation 2d function
                primaTextArea.setText("Risultato operazione 2d");
                dopoTextArea.clear();
                break;

            case "operazione2e":
                // Body of the operation 2e function
                primaTextArea.setText("Risultato operazione 2e");
                dopoTextArea.clear();
                break;

            case "operazione2f":
                // Body of the operation 2f function
                primaTextArea.setText("Risultato operazione 2f");
                dopoTextArea.clear();
                break;

            case "operazione2g":
                // Body of the operation 2g function
                primaTextArea.setText("Risultato operazione 2g");
                dopoTextArea.clear();
                break;

            case "operazione3a":
                // Body of the operation 3a function
                primaTextArea.setText("Risultato operazione 3a");
                dopoTextArea.clear();
                break;

            case "operazione3b":
                // Body of the operation 3b function
                primaTextArea.setText("Risultato operazione 3b");
                dopoTextArea.clear();
                break;

            case "operazione4":
                // Body of the operation 4 function
                primaTextArea.setText("Risultato operazione 4");
                dopoTextArea.clear();
                break;

            case "operazione5":
                // Body of the operation 5 function
                primaTextArea.setText("Risultato operazione 5");
                dopoTextArea.clear();
                break;

            case "operazione6":
                // Body of the operation 6 function
                primaTextArea.setText("Risultato operazione 6");
                dopoTextArea.clear();
                break;

            case "operazione7":
                // Body of the operation 7 function
                primaTextArea.setText("Risultato operazione 7");
                dopoTextArea.clear();
                break;

            case "operazione8":
                // Body of the operation 8 function
                primaTextArea.setText("Risultato operazione 8");
                dopoTextArea.clear();
                break;

            case "operazione9":
                // Body of the operation 9 function
                primaTextArea.setText("Risultato operazione 9");
                dopoTextArea.clear();
                break;

            case "operazione10":
                // Body of the operation 10 function
                primaTextArea.setText("Risultato operazione 10");
                dopoTextArea.clear();
                break;

            case "operazione11":
                // Body of the operation 11 function
                primaTextArea.setText("Risultato operazione 11");
                dopoTextArea.clear();
                break;

            case "operazione12a":
                // Body of the operation 12a function
                primaTextArea.setText("Risultato operazione 12a");
                dopoTextArea.clear();
                break;

            case "operazione12b":
                // Body of the operation 12b function
                primaTextArea.setText("Risultato operazione 12b");
                dopoTextArea.clear();
                break;

            case "operazione13":
                // Body of the operation 13 function
                primaTextArea.setText("Risultato operazione 13");
                dopoTextArea.clear();
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
}