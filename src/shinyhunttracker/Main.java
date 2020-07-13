package shinyhunttracker;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static java.lang.Integer.parseInt;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //check save data file for previous saves
        //if anything is found, ask the user if they would like to start a new hunt or a previous one
        SaveData checkForData = new SaveData();
        if(checkForData.getLinefromFile(0, "PreviousHunts") != null) {
            selectionPageController selectionPageController = new selectionPageController();
            selectionPageController.newOrOldHunt();
        }else{
            //creates selection page window
            Stage huntSelectionWindow = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("selectionPage.fxml"));
            huntSelectionWindow.setTitle("Shiny Hunt Tracker");
            huntSelectionWindow.setResizable(false);
            huntSelectionWindow.setScene(new Scene(root, 750, 480));
            huntSelectionWindow.show();
        }
    }
}
