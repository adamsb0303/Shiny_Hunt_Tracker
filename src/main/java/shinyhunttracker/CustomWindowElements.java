package shinyhunttracker;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.awt.*;

public class CustomWindowElements {
    static double xOffset, yOffset;

    /**
     * Returns a new title bar with exit and minimize buttons
     * @param stage window to close or minimize
     * @return HBox with new title bar
     */
    public static HBox titleBar(Stage stage){
        if(!stage.isShowing())
            spawnOnMouse(stage);

        stage.getIcons().add(new Image("file:Images/icon.png"));

        //Exits given stage
        Button exit = new Button();
        exit.setFocusTraversable(false);
        ImageView exitIcon = new ImageView(new Image("file:Images/x.png"));
        exit.setPadding(new Insets(0, 0, 0, 0));
        exit.setGraphic(exitIcon);
        exit.setMinSize(25, 25);
        exit.setOnAction(e -> stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST)));

        //Minimizes given stage
        Button minimize = new Button();
        minimize.setFocusTraversable(false);
        ImageView minimizeIcon = new ImageView(new Image("file:Images/underscore.png"));
        minimize.setPadding(new Insets(0, 0, 0, 0));
        minimize.setGraphic(minimizeIcon);
        minimize.setMinSize(25, 25);
        minimize.setOnAction(e -> stage.setIconified(true));

        //Places the buttons next to each other in the top right
        HBox windowControls = new HBox();
        windowControls.getChildren().addAll(minimize, exit);
        windowControls.setAlignment(Pos.CENTER_RIGHT);
        windowControls.setSpacing(5);
        windowControls.setPadding(new Insets(5));

        return windowControls;
    }

    /**
     * Creates the window on the screen that the mouse is on
     * @param stage custom stage
     */
    public static void spawnOnMouse(Stage stage){
        Point mouse = MouseInfo.getPointerInfo().getLocation();

        Screen.getScreens().forEach(e -> {
            //checks for screen on x and y axises
            if(e.getBounds().getMinX() <= mouse.getX() && e.getBounds().getMaxX() > mouse.getX())
                if(e.getBounds().getMinY() <= mouse.getY() && e.getBounds().getMaxY() > mouse.getY()) {
                    double centerX = (e.getBounds().getMinX() + e.getBounds().getMaxX()) / 2;
                    double centerY = (e.getBounds().getMinY() + e.getBounds().getMaxY()) / 2;
                    Platform.runLater(() -> {
                        stage.setX(centerX - stage.getScene().getWidth() / 2);
                        stage.setY(centerY - stage.getScene().getHeight() / 2);
                    });
                }
        });
    }

    /**
     * Allows given Scene to detect mouse movement and drag stage with mouse
     * @param scene Scene that is going to be made draggable
     */
    public static void makeDraggable(Scene scene){
        Window stage = scene.getWindow();
        scene.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        scene.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
    }
}
