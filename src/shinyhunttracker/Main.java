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
        Stage huntSelectionWindow = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("selectionPage.fxml"));
        huntSelectionWindow.setTitle("Shiny Hunt Tracker");
        huntSelectionWindow.setResizable(false);
        huntSelectionWindow.setScene(new Scene(root, 750, 480));

        SaveData checkForData = new SaveData();
        if(checkForData.getLinefromFile(0) != null) {
            Platform.runLater(() -> {
                Stage loadStage = new Stage();

                Label prompt1 = new Label("Would you like to continue a previous hunt or");
                Label prompt2 = new Label("start a new one?");
                Button continuePrevious = new Button("Continue Previous Hunt");
                Button newHunt = new Button("Start New Hunt");

                VBox loadLayout = new VBox();
                loadLayout.getChildren().addAll(prompt1, prompt2, continuePrevious, newHunt);
                loadLayout.setSpacing(10);
                loadLayout.setAlignment(Pos.CENTER);

                Scene loadScene = new Scene(loadLayout, 275, 150);
                loadStage.setTitle("Load previous save");
                loadStage.setResizable(false);
                loadStage.setScene(loadScene);
                loadStage.show();

                newHunt.setOnAction(e -> {
                    huntSelectionWindow.show();
                    loadStage.close();
                });

                continuePrevious.setOnAction(e -> {
                    Stage previousHuntsStage = new Stage();

                    previousHuntsStage.setTitle("Select a previous hunt");
                    TreeView<String> previousHuntsView = new TreeView<>();
                    TreeItem<String> previousHuntsRoot = new TreeItem<>();

                    SaveData previousHuntData = new SaveData();

                    for(int i = 0; i < previousHuntData.getfileLength(); i++){
                        makeBranch(previousHuntData.getLinefromFile(i), previousHuntsRoot);
                    }

                    previousHuntsView.setRoot(previousHuntsRoot);
                    previousHuntsView.setShowRoot(false);

                    VBox previousHuntsLayout = new VBox();
                    previousHuntsLayout.getChildren().addAll(previousHuntsView);

                    Scene previousHuntsScene = new Scene(previousHuntsLayout, 300, 400);
                    previousHuntsStage.setScene(previousHuntsScene);
                    previousHuntsStage.show();
                    loadStage.close();

                    previousHuntsView.getSelectionModel().selectedItemProperty()
                            .addListener((v, oldValue, newValue) -> {
                                String line = newValue.toString().substring(18);
                                previousHuntData.loadHunt(parseInt(line.substring(0, line.indexOf(')'))) - 1);
                                previousHuntsStage.close();
                            });
                });
            });
        }else
            huntSelectionWindow.show();
    }

    public void makeBranch(String title, TreeItem<String> parent){
        TreeItem<String> item = new TreeItem<>(title);
        parent.getChildren().add(item);
    }
}
