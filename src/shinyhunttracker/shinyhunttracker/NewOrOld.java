package shinyhunttracker;

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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

import static java.lang.Integer.parseInt;

class NewOrOld extends Window {
    HuntController controller;

    public void newOrOldHunt(){
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

        //show the previously created Selection Page Window
        newHunt.setOnAction(e -> {
            //creates selection page window
            try {
                //creates selection page window
                FXMLLoader selectionPageLoader = new FXMLLoader();
                selectionPageLoader.setLocation(getClass().getResource("selectionPage.fxml"));
                Parent root = selectionPageLoader.load();

                windowStage.setTitle("Shiny Hunt Tracker");
                windowStage.setResizable(false);
                windowStage.setScene(new Scene(root, 750, 480));

                SelectionPageController selectionPageController = selectionPageLoader.getController();
                selectionPageController.setController(controller);
                selectionPageController.setCurrentLayout(currentLayout);
                windowStage.show();
            }catch(IOException f){
                f.printStackTrace();
            }
            loadStage.close();
        });

        //if they would like to continue, show a list of the previous hunts found on the file
        continuePrevious.setOnAction(e -> {
            windowStage.setTitle("Select a previous hunt");
            TreeView<String> previousHuntsView = new TreeView<>();
            TreeItem<String> previousHuntsRoot = new TreeItem<>();

            JSONParser jsonParser = new JSONParser();

            try (FileReader reader = new FileReader("SaveData/previousHunts.json")){
                //Read JSON file
                Object obj = jsonParser.parse(reader);
                JSONArray huntList = (JSONArray) obj;

                for(int i = 0; i < huntList.size(); i++){
                    JSONObject huntData = (JSONObject) huntList.get(i);
                    String name = Pokemon.findName(Integer.parseInt(huntData.get("pokemon_id").toString()));
                    String game = huntData.get("game").toString();
                    String method = huntData.get("method").toString();
                    String encounters = huntData.get("encounters").toString();

                    makeBranch((i+1) + ") " + name + " | " + game + " | " + method + " | " + encounters + " encounters", previousHuntsRoot);
                }
            }catch (IOException | ParseException f) {
                f.printStackTrace();
            }

            previousHuntsView.setRoot(previousHuntsRoot);
            previousHuntsView.setShowRoot(false);

            VBox previousHuntsLayout = new VBox();
            previousHuntsLayout.getChildren().addAll(previousHuntsView);

            Scene previousHuntsScene = new Scene(previousHuntsLayout, 300, 400);
            windowStage.setScene(previousHuntsScene);
            windowStage.show();
            loadStage.close();

            //skip selection window and go straight to hunt page
            previousHuntsView.getSelectionModel().selectedItemProperty()
                    .addListener((v, oldValue, newValue) -> {
                        SaveData previousHuntData = new SaveData();

                        String line = newValue.toString().substring(18);
                        previousHuntData.loadHunt(parseInt(line.substring(0, line.indexOf(')'))) - 1, controller);
                        windowStage.close();
                    });
        });
    }

    public void setController(HuntController controller){
        this.controller = controller;
    }
}