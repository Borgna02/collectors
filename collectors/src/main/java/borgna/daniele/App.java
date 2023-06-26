package borgna.daniele;

import java.io.IOException;

import borgna.daniele.controller.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
    }
}
