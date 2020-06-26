package shinyhunttracker;

import com.sun.javafx.iio.ios.IosDescriptor;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class huntControlsController implements Initializable {
    public Button encountersButton, pokemonCaughtButton, phaseButton, resetEncountersButton;
    public hunterController controller;
    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    public void incrementEncounters(){
        controller.incrementEncounters();
    }

    public void resetEncounters(){
        controller.resetEncounters();
    }

    public void createLink(hunterController controller){
        this.controller = controller;
    }
}
