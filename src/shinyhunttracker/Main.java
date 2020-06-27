package shinyhunttracker;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //SaveData checkForData = new SaveData();
        //if(checkForData.getHuntPokemon() != null){
        Platform.runLater(() -> {
            Stage loadStage = new Stage();
            VBox loadLayout = new VBox();
            Label prompt = new Label("Would you like to Continue a Previous hunt or \nstart a new one?");
            Button continuePrevious = new Button("Continue Previous Hunt");
            Button newHunt = new Button("Start New Hunt");
            loadLayout.getChildren().addAll(prompt, continuePrevious, newHunt);
            loadLayout.setSpacing(10);
            loadLayout.setAlignment(Pos.CENTER);

            Scene loadScene = new Scene(loadLayout, 275, 150);
            loadStage.setTitle("Load previous save");
            loadStage.setResizable(false);
            loadStage.setScene(loadScene);
            loadStage.show();

            newHunt.setOnAction(e -> {
                try {
                    Stage huntSelectionWindow = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("selectionPage.fxml"));
                    huntSelectionWindow.setTitle("Shiny Hunt Tracker");
                    huntSelectionWindow.setResizable(false);
                    huntSelectionWindow.setScene(new Scene(root, 750, 480));
                    huntSelectionWindow.show();
                    loadStage.close();
                }catch (IOException f){
                    System.out.println("Error");
                }
            });
        });
    }

}
