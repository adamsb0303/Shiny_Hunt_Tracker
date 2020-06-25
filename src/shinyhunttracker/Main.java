package shinyhunttracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("selectionPage.fxml"));
        primaryStage.setTitle("Shiny Hunt Tracker");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 750, 480));
        primaryStage.show();
    }

}
