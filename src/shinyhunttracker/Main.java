package shinyhunttracker;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        huntController controller = new huntController();


        GameData gameData = new GameData();
        //gameData.translateData();
    }
}