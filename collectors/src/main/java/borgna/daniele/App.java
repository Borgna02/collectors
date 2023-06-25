package borgna.daniele;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.sql.CallableStatement;

import borgna.daniele.controller.HomeController;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home.fxml"));
        HomeController controller = loader.getController();
        Parent parent = loader.load();
        loader.setController(controller);

        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
 /*
        String connectionString = "jdbc:mysql://localhost:3306/collectors";
        String username = "collectorsUser";
        String password = "collectorsPwd";

        try {

            // Crea una connessione al database
            Connection con = DriverManager.getConnection(connectionString, username, password);

            // Crea uno statement per eseguire le query
            Statement stmt = con.createStatement();

            String query2 = "{ ? = CALL GetCollezione(?, ?) }";
            CallableStatement stmt2 = con.prepareCall(query2);

            stmt2.registerOutParameter(1, Types.INTEGER);
            stmt2.setString(2, "La Mia Preferita");
            stmt2.setString(3, "Mickey789");

            stmt2.execute();

            int result = stmt2.getInt(1);
            System.out.println(result);
            // // Esegui una query di selezione
            // String query = "SELECT * FROM Collezionista;";
            // ResultSet resultSet = stmt.executeQuery(query);

            // Elabora i risultati della query
            // while (resultSet.next()) {
            //     System.out.println(resultSet.toString());
            // }

            // Chiudi le risorse
            // resultSet.close();
            stmt2.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }
}
