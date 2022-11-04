package shinyhunttracker;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.util.Vector;

import static java.lang.Double.parseDouble;

public class ElementSettings {
    static Vector<String> fonts = generateFonts();

    /**
     * Creates ImageView settings TitledPane
     * @param windowLayout Layout to add boundary guide to
     * @param image Image the settings are for
     * @param pokemon Pokemon the image is of
     * @param selectedGame Game the image is from
     * @return TitledPane with the Image settings
     */
    public static TitledPane createImageSettings(AnchorPane windowLayout, ImageView image, Pokemon pokemon, Game selectedGame){
        //Check to make image visible
        CheckBox visibleCheck = new CheckBox("Visible");
        visibleCheck.setSelected(image.isVisible());

        //Creates a list of available forms for pokemon
        HBox form = new HBox();
        form.setSpacing(5);
        form.setAlignment(Pos.CENTER);
        Label formLabel = new Label("Form");
        ComboBox<String> formCombo = new ComboBox<>();
        if(pokemon.getForms().size() != 0) {
            for (String i : pokemon.getForms())
                formCombo.getItems().add(i);
            formCombo.getSelectionModel().select(pokemon.getForm());
        }else
            formCombo.getSelectionModel().select(0);
        form.getChildren().addAll(formLabel, formCombo);

        //Changes X location of Image
        HBox changeX = new HBox();
        changeX.setSpacing(5);
        changeX.setAlignment(Pos.CENTER_LEFT);
        Label XLabel = new Label("X");
        TextField XField = new TextField();
        XField.setMaxWidth(100);
        XField.setText(String.valueOf(image.getLayoutX()));
        changeX.getChildren().addAll(XLabel, XField);
        image.layoutXProperty().addListener((o, oldVal, newVal) -> {
            if(Double.parseDouble(XField.getText()) != newVal.doubleValue())
                XField.setText(newVal.toString());
        });

        //Changes Y location of image
        HBox changeY = new HBox();
        changeY.setSpacing(5);
        changeY.setAlignment(Pos.CENTER_LEFT);
        Label YLabel = new Label("Y");
        TextField YField = new TextField();
        YField.setMaxWidth(100);
        YField.setText(String.valueOf(image.getLayoutY()));
        YField.promptTextProperty().bind(image.layoutYProperty().asString());
        changeY.getChildren().addAll(YLabel, YField);
        image.layoutYProperty().addListener((o, oldVal, newVal) -> {
            if(Double.parseDouble(YField.getText()) != newVal.doubleValue())
                YField.setText(newVal.toString());
        });

        //Group for X and Y fields
        HBox changeLocation = new HBox();
        changeLocation.setSpacing(10);
        changeLocation.setAlignment(Pos.CENTER);
        changeLocation.getChildren().addAll(changeX, changeY);

        //Changes scale of Image
        HBox changeSize = new HBox();
        changeSize.setSpacing(5);
        Label sizeLabel = new Label("Scale");
        TextField sizeField = new TextField();
        sizeField.setMaxWidth(100);
        sizeField.setText(String.valueOf(image.getScaleX()));
        changeSize.setAlignment(Pos.CENTER);
        changeSize.getChildren().addAll(sizeLabel, sizeField);
        image.scaleXProperty().addListener((o, oldVal, newVal) -> {
            if(Double.parseDouble(sizeField.getText()) != newVal.doubleValue())
                sizeField.setText(newVal.toString());
        });

        //Adjust fit of image
        VBox imageFit = new VBox();
        imageFit.setSpacing(5);
        imageFit.setAlignment(Pos.CENTER);
        Button imageFitButton = new Button("Adjust Image Layout Bounds");
        imageFit.getChildren().add(imageFitButton);

        VBox imageVBox = new VBox();
        imageVBox.setSpacing(10);
        imageVBox.setAlignment(Pos.CENTER);
        imageVBox.setPadding(new Insets(10, 10, 10, 10));

        //adds all elements to master layout
        imageVBox.getChildren().add(visibleCheck);
        if(formCombo.getItems().size() != 0)
            imageVBox.getChildren().add(form);
        imageVBox.getChildren().addAll(changeLocation, changeSize, imageFit);

        TitledPane imageTitledPane = new TitledPane(pokemon.getName() + " Sprite", imageVBox);

        formCombo.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if(newValue != null){
                pokemon.setForm(formCombo.getSelectionModel().getSelectedIndex());
                image.setImage(FetchImage.getImage(new ProgressIndicator(), image, pokemon, selectedGame));
            }
        });

        XField.setOnKeyTyped(e ->{
            if(XField.getText().length() == 0)
                XField.setText("0");
            try{
                image.setLayoutX(parseDouble(XField.getText()));
            }catch(NumberFormatException ignored){
                XField.setText(String.valueOf(image.getLayoutX()));
            }
        });

        YField.setOnKeyTyped(e ->{
            if(YField.getText().length() == 0)
                YField.setText("0");
            try{
                image.setLayoutY(parseDouble(YField.getText()));
            }catch(NumberFormatException ignored){
                YField.setText(String.valueOf(image.getLayoutY()));
            }
        });

        sizeField.setOnKeyTyped(e -> {
            if(sizeField.getText().length() == 0)
                sizeField.setText("0");
            try{
                double scale = parseDouble(sizeField.getText());
                image.setScaleX(scale);
                image.setScaleY(scale);
                image.setTranslateX(-image.getImage().getWidth() / 2);
                image.setTranslateY(-((image.getImage().getHeight() / 2) + (image.getImage().getHeight() * image.getScaleX()) / 2));
            }catch(NumberFormatException ignored){
                sizeField.setText(String.valueOf(image.getScaleX()));
            }
        });

        visibleCheck.setOnAction(e -> image.setVisible(visibleCheck.isSelected()));

        imageFitButton.setOnAction(e -> {
            //doesn't draw another boundary if one is already open
            if(windowLayout.getChildren().get(windowLayout.getChildren().size() - 1) instanceof Line)
                return;

            //sets fit width and height if it wasn't already (when user changes scale with textfield)
            if(image.getImage().getWidth() * image.getScaleX() > -image.getFitWidth())
                image.setFitWidth(-image.getImage().getWidth() * image.getScaleX());
            if(image.getImage().getHeight() * image.getScaleX() > -image.getFitHeight())
                image.setFitHeight(-image.getImage().getHeight() * image.getScaleX());
            sizeField.setDisable(true);

            Rectangle square = new Rectangle();
            drawBoundaryGuide(windowLayout, image, square);
            imageFit.getChildren().remove(0);

            Button saveImageFit = new Button("Save Bounds");
            Button resetImageFit = new Button("Fit to Image");
            HBox saveResetHBox = new HBox();
            saveResetHBox.setAlignment(Pos.CENTER);
            saveResetHBox.setSpacing(5);
            saveResetHBox.getChildren().addAll(saveImageFit, resetImageFit);

            Button cancelImageFit = new Button("Cancel");
            HBox cancelHBox = new HBox();
            cancelHBox.setAlignment(Pos.CENTER);
            cancelHBox.getChildren().add(cancelImageFit);

            imageFit.getChildren().addAll(saveResetHBox, cancelHBox);

            //Sets fit height and width to the height and width of the square
            saveImageFit.setOnAction(f -> {
                image.setFitHeight(-square.getHeight() * square.getScaleY());
                image.setFitWidth(-square.getWidth() * square.getScaleX());
                windowLayout.getChildren().remove(windowLayout.getChildren().size() - 13, windowLayout.getChildren().size());
                imageFit.getChildren().remove(0,2);
                imageFit.getChildren().add(imageFitButton);
                sizeField.setDisable(false);
            });

            resetImageFit.setOnAction(f-> {
                double oldHeight = square.getHeight() * square.getScaleY();
                //adjusts square to be same dimensions as image
                square.setScaleX(image.getImage().getWidth() * image.getScaleX() / square.getWidth());
                square.setScaleY(image.getImage().getHeight() * image.getScaleY() / square.getHeight());

                //Translates square down by the half of the change in height
                square.setTranslateY(square.getTranslateY() + (oldHeight - square.getHeight() * square.getScaleY()) / 2);
            });

            //Removes square and resets image scale
            double originalScale = image.getScaleX();
            cancelImageFit.setOnAction(f -> {
                image.setScaleX(originalScale);
                image.setScaleY(originalScale);
                image.setTranslateX(-image.getImage().getWidth() / 2);
                image.setTranslateY(-((image.getImage().getHeight() / 2) + (image.getImage().getHeight() * image.getScaleX()) / 2));
                windowLayout.getChildren().remove(windowLayout.getChildren().size() - 13, windowLayout.getChildren().size());
                imageFit.getChildren().remove(0,2);
                imageFit.getChildren().add(imageFitButton);
                sizeField.setDisable(false);
            });
        });

        return imageTitledPane;
    }

    /**
     * Creates Label settings TitledPane
     * @param label Label the settings are for
     * @param labelName Name of the label for the pane title
     * @return TitledPane with the Label settings
     */
    public static TitledPane createLabelSettings(Text label, String labelName){
        //Check to make image visible
        CheckBox visibleCheck = new CheckBox("Visible");
        visibleCheck.setSelected(label.isVisible());

        //Change X location of Label
        HBox changeX = new HBox();
        changeX.setSpacing(5);
        changeX.setAlignment(Pos.CENTER_LEFT);
        Label XLabel = new Label("X");
        TextField XField = new TextField();
        XField.setMaxWidth(100);
        XField.setText(String.valueOf(label.getLayoutX()));
        changeX.getChildren().addAll(XLabel, XField);
        label.layoutXProperty().addListener((o, oldVal, newVal) -> {
            if(Double.parseDouble(XField.getText()) != newVal.doubleValue())
                XField.setText(newVal.toString());
        });

        //Change Y location of Label
        HBox changeY = new HBox();
        changeY.setSpacing(5);
        changeY.setAlignment(Pos.CENTER_LEFT);
        Label YLabel = new Label("Y");
        TextField YField = new TextField();
        YField.setMaxWidth(100);
        YField.setText(String.valueOf(label.getLayoutY()));
        changeY.getChildren().addAll(YLabel, YField);
        label.layoutYProperty().addListener((o, oldVal, newVal) -> {
            if(Double.parseDouble(YField.getText()) != newVal.doubleValue())
                YField.setText(newVal.toString());
        });

        //Group for X and Y
        HBox changeLocation = new HBox();
        changeLocation.setSpacing(10);
        changeLocation.setAlignment(Pos.CENTER);
        changeLocation.getChildren().addAll(changeX, changeY);

        //Change scale of Label
        HBox changeSize = new HBox();
        changeSize.setSpacing(5);
        Label sizeLabel = new Label("Scale");
        TextField sizeField = new TextField();
        sizeField.setMaxWidth(100);
        sizeField.setText(String.valueOf(label.getScaleX()));
        changeSize.setAlignment(Pos.CENTER);
        changeSize.getChildren().addAll(sizeLabel, sizeField);
        label.scaleXProperty().addListener((o, oldVal, newVal) -> {
            if(Double.parseDouble(sizeField.getText()) != newVal.doubleValue())
                sizeField.setText(newVal.toString());
        });

        //Change color of label
        HBox color = new HBox();
        color.setSpacing(5);
        Label colorLabel = new Label("Color");
        ColorPicker colorField = new ColorPicker();
        colorField.setMinHeight(25);
        colorField.setValue((Color) label.getFill());
        color.setAlignment(Pos.CENTER);
        color.getChildren().addAll(colorLabel, colorField);

        //Change font of label
        HBox font = new HBox();
        font.setSpacing(5);
        Label fontLabel = new Label("Font");
        ComboBox<String> fontNameBox = new ComboBox<>();
        fontNameBox.setEditable(false);
        fontNameBox.getItems().addAll(fonts);
        font.setAlignment(Pos.CENTER);
        fontNameBox.getSelectionModel().select(label.getFont().getName());
        font.getChildren().addAll(fontLabel, fontNameBox);

        //Italics, Bold, and Underline Checkboxes
        HBox textProperties = new HBox();
        textProperties.setSpacing(25);
        CheckBox italicsCheck = new CheckBox("Italics");
        italicsCheck.setSelected(label.getFont().getName().contains("Italic"));
        CheckBox boldCheck = new CheckBox("Bold");
        boldCheck.setSelected(label.getFont().getName().contains("Bold"));
        CheckBox underlinedCheck = new CheckBox("Underlined");
        underlinedCheck.setSelected(label.isUnderline());
        textProperties.setAlignment(Pos.CENTER);
        textProperties.getChildren().addAll(italicsCheck, boldCheck, underlinedCheck);

        //Turns on outline for label
        CheckBox strokeCheckbox = new CheckBox("Outline");
        label.setStrokeWidth(0);

        //Width of outline
        HBox strokeWidth = new HBox();
        strokeWidth.setSpacing(5);
        Label strokeWidthLabel = new Label("Width");
        TextField strokeWidthField = new TextField();
        strokeWidthField.setMaxWidth(100);
        strokeWidthField.setText(String.valueOf(label.getStrokeWidth()));
        strokeWidthField.promptTextProperty().bind(label.strokeWidthProperty().asString());
        strokeWidth.disableProperty().bind(strokeCheckbox.selectedProperty().not());
        strokeWidthField.disableProperty().bind(strokeCheckbox.selectedProperty().not());
        strokeWidth.setAlignment(Pos.CENTER_LEFT);
        strokeWidth.getChildren().addAll(strokeWidthLabel, strokeWidthField);

        //Color of outline
        HBox strokeColor = new HBox();
        strokeColor.setSpacing(5);
        Label strokeColorLabel = new Label("Color");
        ColorPicker strokeColorPicker = new ColorPicker();
        strokeColorPicker.setMinHeight(25);
        strokeColorPicker.setMaxWidth(100);
        strokeColorPicker.setValue((Color) label.getStroke());
        strokeColorLabel.disableProperty().bind(strokeCheckbox.selectedProperty().not());
        strokeColorPicker.disableProperty().bind(strokeCheckbox.selectedProperty().not());
        strokeColor.setAlignment(Pos.CENTER_LEFT);
        strokeColor.getChildren().addAll(strokeColorLabel, strokeColorPicker);

        //Group for outline settings
        HBox strokeSettings = new HBox();
        strokeSettings.setSpacing(10);
        strokeSettings.setAlignment(Pos.CENTER);
        strokeSettings.getChildren().addAll(strokeWidth, strokeColor);

        //Adds all elements to layout
        VBox labelVBox = new VBox();
        labelVBox.setSpacing(10);
        labelVBox.setAlignment(Pos.CENTER);
        labelVBox.setPadding(new Insets(10, 10, 10, 10));
        labelVBox.getChildren().addAll(visibleCheck, changeLocation, changeSize, color, font, textProperties, strokeCheckbox, strokeSettings);

        TitledPane labelTitledPane = new TitledPane(labelName + " Text", labelVBox);

        XField.setOnKeyTyped(e ->{
            if(XField.getText().length() == 0)
                XField.setText("0");
            try{
                label.setLayoutX(parseDouble(XField.getText()));
            }catch(NumberFormatException ignored){
                XField.setText(String.valueOf(label.getLayoutX()));
            }
        });

        YField.setOnKeyTyped(e ->{
            if(YField.getText().length() == 0)
                YField.setText("0");
            try{
                label.setLayoutY(parseDouble(YField.getText()));
            }catch(NumberFormatException ignored){
                YField.setText(String.valueOf(label.getLayoutY()));
            }
        });

        sizeField.setOnKeyTyped(e -> {
            if(sizeField.getText().length() == 0)
                sizeField.setText("0");
            try{
                label.setScaleX(parseDouble(sizeField.getText()));
                label.setScaleY(parseDouble(sizeField.getText()));
            }catch(NumberFormatException ignored){
                sizeField.setText(String.valueOf(label.getScaleX()));
            }
        });

        fontNameBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if(newValue == null)
                return;

            //Disables Bold if font can't be bolded
            if(!canBold(newValue)) {
                boldCheck.setSelected(false);
                boldCheck.setDisable(true);
            }else
                boldCheck.setDisable(false);

            //Disables Italics if font can't be italicized
            if(!canItalic(newValue)){
                italicsCheck.setSelected(false);
                italicsCheck.setDisable(true);
            }else
                italicsCheck.setDisable(false);

            //Apply bold or italics to label
            if(boldCheck.isSelected() && italicsCheck.isSelected())
                label.setFont(Font.font(newValue, FontWeight.BOLD, FontPosture.ITALIC, 12));
            else if(boldCheck.isSelected())
                label.setFont(Font.font(newValue, FontWeight.BOLD, 12));
            else if(italicsCheck.isSelected())
                label.setFont(Font.font(newValue, FontPosture.ITALIC, 12));
            else
                label.setFont(new Font(newValue, 12));
        });

        colorField.setOnAction(e -> label.setFill(colorField.getValue()));

        strokeCheckbox.setOnAction(e -> {
            if(strokeCheckbox.isSelected())
                label.setStrokeWidth(parseDouble(strokeWidthField.getText()));
            else
                label.setStrokeWidth(0);
        });

        strokeWidthField.setOnKeyTyped(e -> {
            try{
                label.setStrokeWidth(parseDouble(strokeWidthField.getText()));
            }catch(NumberFormatException ignored){
                strokeWidthField.setText(String.valueOf(label.getStrokeWidth()));
            }
        });

        strokeColorPicker.setOnAction(e -> label.setStroke(strokeColorPicker.getValue()));

        italicsCheck.setOnAction(e -> {
            //makes label italicized
            String fontName = sanitizeFontName(String.valueOf(label.getFont().getName()));
            if(boldCheck.isSelected() && italicsCheck.isSelected())
                label.setFont(Font.font(fontName, FontWeight.BOLD, FontPosture.ITALIC, 12));
            else if(boldCheck.isSelected())
                label.setFont(Font.font(fontName, FontWeight.BOLD, 12));
            else if(italicsCheck.isSelected())
                label.setFont(Font.font(fontName, FontPosture.ITALIC, 12));
            else
                label.setFont(new Font(fontName, 12));
        });

        boldCheck.setOnAction(e -> {
            //makes label bolded
            String fontName = sanitizeFontName(String.valueOf(label.getFont().getName()));
            if(boldCheck.isSelected() && italicsCheck.isSelected())
                label.setFont(Font.font(fontName, FontWeight.BOLD, FontPosture.ITALIC, 12));
            else if(boldCheck.isSelected())
                label.setFont(Font.font(fontName, FontWeight.BOLD, 12));
            else if(italicsCheck.isSelected())
                label.setFont(Font.font(fontName, FontPosture.ITALIC, 12));
            else
                label.setFont(new Font(fontName, 12));
        });

        underlinedCheck.setOnAction(e -> label.setUnderline(underlinedCheck.isSelected()));

        visibleCheck.setOnAction(e -> label.setVisible(visibleCheck.isSelected()));

        return labelTitledPane;
    }

    /**
     * Creates Background settings TitledPane
     * @param windowStage Stage to make transparent
     * @param windowLayout Layout to change background of
     * @return TitledPane with background settings
     */
    public static TitledPane createBackgroundSettings(Stage windowStage, AnchorPane windowLayout){
        //Color Picker for background color
        HBox backgroundColorSettings = new HBox();
        backgroundColorSettings.setAlignment(Pos.CENTER);
        backgroundColorSettings.setSpacing(5);
        Label backgroundColorLabel = new Label("Color");
        ColorPicker backgroundColorPicker = new ColorPicker();
        backgroundColorPicker.setMinHeight(25);
        backgroundColorPicker.disableProperty().bind(windowStage.showingProperty().not());
        backgroundColorSettings.getChildren().addAll(backgroundColorLabel, backgroundColorPicker);

        //Checkbox to make background transparent
        CheckBox transparent = new CheckBox("Transparent");
        VBox backgroundVBox = new VBox();
        backgroundVBox.setAlignment(Pos.CENTER);
        backgroundVBox.setPadding(new Insets(10, 10, 10, 10));
        backgroundVBox.setSpacing(10);
        backgroundVBox.getChildren().addAll(backgroundColorSettings, transparent);

        Stage transparentStage = new Stage();
        transparentStage.initStyle(StageStyle.TRANSPARENT);
        transparent.setOnAction(e -> {
            if(transparent.isSelected()) {
                transparentStage.setX(windowStage.getX());
                transparentStage.setY(windowStage.getY());

                transparentStage.setHeight(windowStage.getHeight());
                transparentStage.setWidth(windowStage.getWidth());

                transparentStage.setScene(windowStage.getScene());
                transparentStage.getScene().getRoot().setStyle("-fx-background-color:transparent;");
                transparentStage.getScene().setFill(Color.TRANSPARENT);

                windowStage.hide();
                transparentStage.show();
            }else {
                windowStage.setX(transparentStage.getX());
                windowStage.setY(transparentStage.getY());

                windowStage.setScene(transparentStage.getScene());
                windowStage.getScene().getRoot().setStyle("-fx-background-color:#" + backgroundColorPicker.getValue().toString().substring(2));

                windowStage.show();
                transparentStage.hide();
            }
        });

        if (windowLayout.getBackground() != null)
            backgroundColorPicker.setValue((Color) windowLayout.getBackground().getFills().get(0).getFill());

        backgroundColorPicker.setOnAction(e -> windowLayout.setBackground(new Background(new BackgroundFill(backgroundColorPicker.getValue(), CornerRadii.EMPTY, Insets.EMPTY))));

        return new TitledPane("Background", backgroundVBox);
    }

    /**
     * Draws square that shows where the fit width and length are
     * @param windowLayout Layout to add boundary to
     * @param image Image the boundary is for
     * @param square Square that covers the fit height and width
     */
    public static void drawBoundaryGuide(AnchorPane windowLayout, ImageView image, Rectangle square){
        //Sets square to the fit width of image
        square.setHeight(-image.getFitHeight());
        square.setWidth(-image.getFitWidth());
        square.setTranslateX(-square.getWidth() / 2);
        square.setTranslateY(-square.getHeight());
        square.setLayoutX(image.getLayoutX());
        square.setLayoutY(image.getLayoutY());
        square.setOpacity(0.8);
        windowLayout.getChildren().add(square);

        //Cornered piece in the top left
        Line verticalTopLeft = new Line();
        verticalTopLeft.setStroke(Color.WHITE);
        verticalTopLeft.startXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        verticalTopLeft.endXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        verticalTopLeft.startYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).subtract(7 / 2)));
        verticalTopLeft.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).multiply(7).divide(8).subtract(7 / 2)));
        verticalTopLeft.setStrokeWidth(7);
        windowLayout.getChildren().add(verticalTopLeft);

        verticalTopLeft.setOnMousePressed(e -> {
            double oldHeight = square.getHeight();
            double oldWidth = square.getWidth();
            verticalTopLeft.setOnMouseDragged(f -> {
                adjustY(square, oldHeight, f.getSceneY(), image);
                adjustX(square, oldWidth, f.getSceneX(), image);
            });
        });

        Line horizontalTopLeft = new Line();
        horizontalTopLeft.setStroke(Color.WHITE);
        horizontalTopLeft.startXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        horizontalTopLeft.endXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).multiply(3).divide(8).subtract(7 / 2)));
        horizontalTopLeft.startYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).subtract(7 / 2)));
        horizontalTopLeft.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).subtract(7 / 2)));
        horizontalTopLeft.setStrokeWidth(7);
        windowLayout.getChildren().add(horizontalTopLeft);

        horizontalTopLeft.setOnMousePressed(e -> {
            double oldHeight = square.getHeight();
            double oldWidth = square.getWidth();
            horizontalTopLeft.setOnMouseDragged(f -> {
                adjustY(square, oldHeight, f.getSceneY(), image);
                adjustX(square, oldWidth, f.getSceneX(), image);
            });
        });

        //Top center line
        Line topCenter = new Line();
        topCenter.setStroke(Color.WHITE);
        topCenter.startXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(8)));
        topCenter.endXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(8)));
        topCenter.startYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).subtract(7 / 2)));
        topCenter.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).subtract(7 / 2)));
        topCenter.setStrokeWidth(7);
        windowLayout.getChildren().add(topCenter);

        topCenter.setOnMousePressed(e -> {
            double oldHeight = square.getHeight();
            topCenter.setOnMouseDragged(f -> adjustY(square, oldHeight, f.getSceneY(), image));
        });

        //Cornered piece in the top right
        Line verticalTopRight = new Line();
        verticalTopRight.setStroke(Color.WHITE);
        verticalTopRight.startXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        verticalTopRight.endXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        verticalTopRight.startYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).subtract(7 / 2)));
        verticalTopRight.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).multiply(7).divide(8).subtract(7 / 2)));
        verticalTopRight.setStrokeWidth(7);
        windowLayout.getChildren().add(verticalTopRight);

        verticalTopRight.setOnMousePressed(e -> {
            double oldHeight = square.getHeight();
            double oldWidth = square.getWidth();
            verticalTopRight.setOnMouseDragged(f -> {
                adjustY(square, oldHeight, f.getSceneY(), image);
                adjustX(square, oldWidth, square.getLayoutX() - (f.getSceneX() - square.getLayoutX()), image);
            });
        });

        Line horizontalTopRight = new Line();
        horizontalTopRight.setStroke(Color.WHITE);
        horizontalTopRight.startXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        horizontalTopRight.endXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).multiply(3).divide(8).subtract(7 / 2)));
        horizontalTopRight.startYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).subtract(7 / 2)));
        horizontalTopRight.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).subtract(7 / 2)));
        horizontalTopRight.setStrokeWidth(7);
        windowLayout.getChildren().add(horizontalTopRight);

        horizontalTopRight.setOnMousePressed(e -> {
            double oldHeight = square.getHeight();
            double oldWidth = square.getWidth();
            horizontalTopRight.setOnMouseDragged(f -> {
                adjustY(square, oldHeight, f.getSceneY(), image);
                adjustX(square, oldWidth, square.getLayoutX() - (f.getSceneX() - square.getLayoutX()), image);
            });
        });

        //Center right line
        Line rightCenter = new Line();
        rightCenter.setStroke(Color.WHITE);
        rightCenter.startXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        rightCenter.endXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        rightCenter.startYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).multiply(5).divide(8)));
        rightCenter.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).multiply(3).divide(8)));
        rightCenter.setStrokeWidth(7);
        windowLayout.getChildren().add(rightCenter);

        rightCenter.setOnMousePressed(e -> {
            double oldWidth = square.getWidth();
            rightCenter.setOnMouseDragged(f -> adjustX(square, oldWidth, square.getLayoutX() - (f.getSceneX() - square.getLayoutX()), image));
        });

        //Cornered piece in the bottom right
        Line verticalBottomRight = new Line();
        verticalBottomRight.setStroke(Color.WHITE);
        verticalBottomRight.startXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        verticalBottomRight.endXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        verticalBottomRight.startYProperty().bind(square.layoutYProperty().subtract(7/2));
        verticalBottomRight.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).divide(8).subtract(7/2)));
        verticalBottomRight.setStrokeWidth(7);
        windowLayout.getChildren().add(verticalBottomRight);

        verticalBottomRight.setOnMousePressed(e -> {
            double oldWidth = square.getWidth();
            verticalBottomRight.setOnMouseDragged(f -> adjustX(square, oldWidth, square.getLayoutX() - (f.getSceneX() - square.getLayoutX()), image));
        });

        Line horizontalBottomRight = new Line();
        horizontalBottomRight.setStroke(Color.WHITE);
        horizontalBottomRight.startXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        horizontalBottomRight.endXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).multiply(3).divide(8).subtract(7 / 2)));
        horizontalBottomRight.startYProperty().bind(square.layoutYProperty().subtract(7 / 2));
        horizontalBottomRight.endYProperty().bind(square.layoutYProperty().subtract(7 / 2));
        horizontalBottomRight.setStrokeWidth(7);
        windowLayout.getChildren().add(horizontalBottomRight);

        //Bottom center line
        Line bottomCenter = new Line();
        bottomCenter.setStroke(Color.WHITE);
        bottomCenter.startXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(8)));
        bottomCenter.endXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(8)));
        bottomCenter.startYProperty().bind(square.layoutYProperty().subtract(7/2));
        bottomCenter.endYProperty().bind(square.layoutYProperty().subtract(7/2));
        bottomCenter.setStrokeWidth(7);
        windowLayout.getChildren().add(bottomCenter);

        //Bottom left corner piece
        Line verticalBottomLeft = new Line();
        verticalBottomLeft.setStroke(Color.WHITE);
        verticalBottomLeft.startXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7/2)));
        verticalBottomLeft.endXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7/2)));
        verticalBottomLeft.startYProperty().bind(square.layoutYProperty().subtract(7/2));
        verticalBottomLeft.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).divide(8).subtract(7/2)));
        verticalBottomLeft.setStrokeWidth(7);
        windowLayout.getChildren().add(verticalBottomLeft);

        verticalBottomLeft.setOnMousePressed(e -> {
            double oldWidth = square.getWidth();
            verticalBottomLeft.setOnMouseDragged(f -> adjustX(square, oldWidth, square.getLayoutX() - (f.getSceneX() - square.getLayoutX()), image));
        });

        Line horizontalBottomLeft = new Line();
        horizontalBottomLeft.setStroke(Color.WHITE);
        horizontalBottomLeft.startXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7/2)));
        horizontalBottomLeft.endXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).multiply(3).divide(8).subtract(7/2)));
        horizontalBottomLeft.startYProperty().bind(square.layoutYProperty().subtract(7/2));
        horizontalBottomLeft.endYProperty().bind(square.layoutYProperty().subtract(7/2));
        horizontalBottomLeft.setStrokeWidth(7);
        windowLayout.getChildren().add(horizontalBottomLeft);

        //Left center line
        Line leftCenter = new Line();
        leftCenter.setStroke(Color.WHITE);
        leftCenter.startXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7/2)));
        leftCenter.endXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7/2)));
        leftCenter.startYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).multiply(5).divide(8)));
        leftCenter.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).multiply(3).divide(8)));
        leftCenter.setStrokeWidth(7);
        windowLayout.getChildren().add(leftCenter);

        leftCenter.setOnMousePressed(e -> {
            double oldWidth = square.getWidth();
            leftCenter.setOnMouseDragged(f -> adjustX(square, oldWidth, f.getSceneX(), image));
        });

        image.layoutXProperty().bindBidirectional(square.layoutXProperty());
        image.layoutYProperty().bindBidirectional(square.layoutYProperty());

        square.setOnMousePressed(e -> {
            double diffX = square.getLayoutX() - e.getSceneX();
            double diffY = square.getLayoutY() - e.getSceneY();
            square.setOnMouseDragged(f -> {
                square.setLayoutX(f.getSceneX() + diffX);
                square.setLayoutY(f.getSceneY() + diffY);
            });
        });
    }

    /**
     * Adjust boundary Y of guide
     * @param square Guide
     * @param oldHeight Current Image height
     * @param mouseLocation Where the mouse is
     * @param image Image to change
     */
    public static void adjustY(Rectangle square, double oldHeight, double mouseLocation, ImageView image){
        //Makes sure to not adjust width past 20 px tall
        if(square.getHeight() * (square.getLayoutY() - mouseLocation) / square.getHeight() < 20)
            return;

        //Adjusts scale of guide
        square.setScaleY((square.getLayoutY() - mouseLocation) / square.getHeight());
        square.setTranslateY(-(square.getHeight() * square.getScaleY() / 2) - oldHeight / 2);
        double newImageHeight = square.getHeight() * square.getScaleY() ;

        //Adjusts scale of image
        double imageScale = newImageHeight / image.getImage().getHeight();
        if(image.getImage().getWidth() * imageScale <= square.getWidth() * square.getScaleX()) {
            image.setScaleY(imageScale);
            image.setScaleX(imageScale);
            image.setTranslateX(-image.getImage().getWidth() / 2);
            image.setTranslateY(-((image.getImage().getHeight() / 2) + (image.getImage().getHeight() * image.getScaleX()) / 2));
        }
    }

    /**
     * Adjust boundary X of guide
     * @param square Guide
     * @param oldWidth Current Image width
     * @param mouseLocation Where the mouse is
     * @param image Image to change
     */
    public static void adjustX(Rectangle square, double oldWidth, double mouseLocation, ImageView image){
        if(square.getWidth() * (square.getLayoutX() - mouseLocation) / square.getWidth() * 2 < 20)
            return;

        //Adjusts scale of guide
        square.setScaleX((square.getLayoutX() - mouseLocation) / square.getWidth() * 2);
        square.setTranslateX(-oldWidth / 2);
        double newImageWidth = square.getWidth() * square.getScaleX();

        //Adjusts scale of image
        double imageScale = newImageWidth / image.getImage().getWidth();
        if(image.getImage().getHeight() * imageScale <= square.getHeight() * square.getScaleY()) {
            image.setScaleY(imageScale);
            image.setScaleX(imageScale);
            image.setTranslateX(-image.getImage().getWidth() / 2);
            image.setTranslateY(-((image.getImage().getHeight() / 2) + (image.getImage().getHeight() * image.getScaleX()) / 2));
        }
    }

    /**
     * Change location of Text when dragged
     * Change scale of Text when scrolled
     * @param element Label to apply to
     */
    public static void quickEdit(Text element){
        //Increases scale when scrolled on
        element.setOnScroll(e -> {
            element.setScaleX(element.getScaleX() + (e.getDeltaY() / 1000));
            element.setScaleY(element.getScaleY() + (e.getDeltaY() / 1000));
        });
        //Saves mouse click, and follows until mouse is released
        element.setOnMousePressed(e -> {
            double diffX = element.getLayoutX() - e.getSceneX();
            double diffY = element.getLayoutY() - e.getSceneY();
            element.setOnMouseDragged(f -> {
                element.setLayoutX(f.getSceneX() + diffX);
                element.setLayoutY(f.getSceneY() + diffY);
            });
        });
    }

    /**
     * Change location of ImageView when dragged
     * Change scale of ImageView when scrolled
     * @param element ImageView to apply to
     */
    public static void quickEdit(ImageView element){
        //Increases scale when scrolled on
        element.setOnScroll(e -> {
            double scale = element.getScaleX() + (e.getDeltaY() / 1000);
            element.setScaleX(scale);
            element.setScaleY(scale);
            if(element.getImage().getWidth() * scale > -element.getFitWidth())
                element.setFitWidth(-element.getImage().getWidth() * scale);
            if(element.getImage().getHeight() * scale > -element.getFitHeight())
                element.setFitHeight(-element.getImage().getHeight() * scale);
            element.setScaleX(scale);
            element.setScaleY(scale);
            element.setTranslateX(-element.getImage().getWidth() / 2);
            element.setTranslateY(-((element.getImage().getHeight() / 2) + (element.getImage().getHeight() * element.getScaleX()) / 2));
        });
        //Saves mouse click, and follows until mouse is released
        element.setOnMousePressed(e -> {
            double diffX = element.getLayoutX() - e.getSceneX();
            double diffY = element.getLayoutY() - e.getSceneY();
            element.setOnMouseDragged(f -> {
                element.setLayoutX(f.getSceneX() + diffX);
                element.setLayoutY(f.getSceneY() + diffY);
            });
        });
    }

    /**
     * Creates string array of available fonts
     * @return Font array
     */
    private static Vector<String> generateFonts(){
        String[] allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Vector<String> availableFonts = new Vector<>();
        for (String availableFont : allFonts)
            if (sanitizeAvailableFontStrings(availableFont) != null)
                availableFonts.add(availableFont);

        return availableFonts;
    }

    /**
     * Removes "Bold", "Italics", and "Regular" from given string
     * @param name Font name
     * @return Proper font name
     */
    public static String sanitizeFontName(String name){
        int index = 0;
        if(name.contains("Bold"))
            index += 5;
        if(name.contains("Italic"))
            index += 7;
        if(name.contains("Regular"))
            index += 8;
        return name.substring(0, name.length() - index);
    }

    /**
     * Checks to see if using a given font returns the system default font
     * @param name Font name
     * @return Font, if it can be used
     */
    public static String sanitizeAvailableFontStrings(String name){
        Font test = new Font(name, 12);
        if(test.getName().compareTo("System Regular") == 0){
            return null;
        }
        return name;
    }

    /**
     * Checks to see if bolding and un-bolding a font returns system default
     * @param name Font name
     * @return If font can be bolded properly
     */
    public static boolean canBold(String name){
        Font test;
        test = Font.font(name,  FontWeight.BOLD, 12);
        if(test.getName().compareTo(name) == 0)
            return false;
        test = new Font(sanitizeFontName(test.getName()), 12);

        return test.getName().compareTo("System") != 0;
    }

    /**
     * Checks to see if italicizing and un-italicizing a font returns system default
     * @param name Font name
     * @return If font can be italicized properly
     */
    public static boolean canItalic(String name){
        Font test;
        test = Font.font(name,  FontPosture.ITALIC, 12);
        if(test.getName().compareTo(name) == 0)
            return false;
        test = new Font(sanitizeFontName(test.getName()), 12);

        return test.getName().compareTo("System") != 0;
    }
}