package shinyhunttracker;

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

import java.awt.*;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class ElementSettings {
    static String[] fonts = generateFonts();

    //creates ImageView settings VBox
    public static VBox createImageSettings(AnchorPane windowLayout, ImageView image, Pokemon pokemon, Game selectedGame){
        HBox groupLabel = new HBox();
        Label Group = new Label(pokemon.getName() + " Sprite:");
        Group.setUnderline(true);
        groupLabel.getChildren().add(Group);

        HBox changeSize = new HBox();
        changeSize.setSpacing(5);
        Label sizeLabel = new Label("Scale:");
        TextField sizeField = new TextField();
        sizeField.promptTextProperty().bind(image.scaleXProperty().asString());
        changeSize.getChildren().addAll(sizeLabel, sizeField);

        HBox changeX = new HBox();
        changeX.setSpacing(5);
        Label XLabel = new Label("X Location:");
        TextField XField = new TextField();
        XField.promptTextProperty().bind(image.layoutXProperty().asString());
        changeX.getChildren().addAll(XLabel, XField);

        HBox changeY = new HBox();
        changeY.setSpacing(5);
        Label YLabel = new Label("Y Location:");
        TextField YField = new TextField();
        YField.promptTextProperty().bind(image.layoutYProperty().asString());
        changeY.getChildren().addAll(YLabel, YField);

        HBox visablility = new HBox();
        visablility.setSpacing(5);
        Label visableLabel = new Label("Visable:");
        CheckBox visableCheck = new CheckBox();
        visableCheck.setSelected(image.isVisible());
        visablility.getChildren().addAll(visableLabel, visableCheck);

        HBox imageFit = new HBox();
        imageFit.setSpacing(5);
        Button imageFitButton = new Button("Adjust Image Layout Bounds");
        imageFit.getChildren().add(imageFitButton);

        HBox form = new HBox();
        form.setSpacing(5);
        Label formLabel = new Label("Form");
        ComboBox<String> formCombo = new ComboBox<>();
        if(pokemon.getForms().size() != 0) {
            for (String i : pokemon.getForms())
                formCombo.getItems().add(i);
            formCombo.getSelectionModel().select(pokemon.getForm());
        }else
            formCombo.getSelectionModel().select(0);
        form.getChildren().addAll(formLabel, formCombo);

        VBox imageVBox = new VBox();
        imageVBox.setSpacing(10);
        if(formCombo.getItems().size() != 0)
            imageVBox.getChildren().add(form);
        imageVBox.getChildren().addAll(groupLabel, changeSize, changeX, changeY, visablility, imageFit);

        Accordion accordion = new Accordion();
        TitledPane imageTitledPane = new TitledPane(pokemon.getName() + " Sprite", imageVBox);
        accordion.getPanes().add(imageTitledPane);

        VBox imageSettings = new VBox();
        imageSettings.getChildren().add(accordion);

        formCombo.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if(newValue != null){
                pokemon.setForm(formCombo.getSelectionModel().getSelectedIndex());
                FetchImage.setImage(image, pokemon, selectedGame);
            }
        });

        sizeField.setOnAction(e -> {
            double scale = 0;
            try{
                scale = parseDouble(sizeField.getText());
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            image.setScaleX(scale);
            image.setScaleY(scale);
            image.setTranslateX(-image.getImage().getWidth() / 2);
            image.setTranslateY(-((image.getImage().getHeight() / 2) + (image.getImage().getHeight() * image.getScaleX()) / 2));
            sizeField.setText("");
        });

        XField.setOnAction(e ->{
            try{
                image.setLayoutX(parseDouble(XField.getText()));
                XField.setText("");
            }catch(NumberFormatException f){
                XField.setText("");
            }
        });

        YField.setOnAction(e ->{
            try{
                image.setLayoutY(parseDouble(YField.getText()));
                YField.setText("");
            }catch(NumberFormatException f){
                YField.setText("");
            }
        });

        visableCheck.setOnAction(e -> image.setVisible(visableCheck.isSelected()));

        imageFitButton.setOnAction(e -> {
            double originalScale = image.getScaleX();

            Rectangle square = new Rectangle();
            drawBoundaryGuide(windowLayout, image, square);
            imageFit.getChildren().remove(0);

            Button saveImageFit = new Button("Save Boundaries");
            Button cancelImageFit = new Button("Cancel");

            imageFit.getChildren().addAll(saveImageFit, cancelImageFit);

            saveImageFit.setOnAction(f -> {
                image.setFitHeight(-square.getHeight() * square.getScaleY());
                image.setFitWidth(-square.getWidth() * square.getScaleX());
                windowLayout.getChildren().remove(windowLayout.getChildren().size() - 13, windowLayout.getChildren().size());
                imageFit.getChildren().remove(0,2);
                imageFit.getChildren().add(imageFitButton);
            });

            cancelImageFit.setOnAction(f -> {
                image.setScaleX(originalScale);
                image.setScaleY(originalScale);
                image.setTranslateX(-image.getImage().getWidth() / 2);
                image.setTranslateY(-((image.getImage().getHeight() / 2) + (image.getImage().getHeight() * image.getScaleX()) / 2));
                windowLayout.getChildren().remove(windowLayout.getChildren().size() - 13, windowLayout.getChildren().size());
                imageFit.getChildren().remove(0,2);
                imageFit.getChildren().add(imageFitButton);
            });
        });

        return imageSettings;
    }

    //draws square that shows where the fit width and length are
    public static void drawBoundaryGuide(AnchorPane windowLayout, ImageView image, Rectangle square){
        square.setHeight(-image.getFitHeight());
        square.setWidth(-image.getFitWidth());
        square.setTranslateX(-square.getWidth() / 2);
        square.setTranslateY(-square.getHeight());
        square.setLayoutX(image.getLayoutX());
        square.setLayoutY(image.getLayoutY());
        square.setOpacity(0.8);
        windowLayout.getChildren().add(square);

        //cornered piece in the top left
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

        //top center line
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

        //cornered piece in the top right
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

        //center right line
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

        //cornered piece in the bottom right
        Line verticalBottomRight = new Line();
        verticalBottomRight.setStroke(Color.WHITE);
        verticalBottomRight.startXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        verticalBottomRight.endXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        verticalBottomRight.startYProperty().bind(square.layoutYProperty().subtract(7/2));
        verticalBottomRight.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).divide(8).subtract(7/2)));
        verticalBottomRight.setStrokeWidth(7);
        windowLayout.getChildren().add(verticalBottomRight);

        Line horizontalBottomRight = new Line();
        horizontalBottomRight.setStroke(Color.WHITE);
        horizontalBottomRight.startXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        horizontalBottomRight.endXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).multiply(3).divide(8).subtract(7 / 2)));
        horizontalBottomRight.startYProperty().bind(square.layoutYProperty().subtract(7 / 2));
        horizontalBottomRight.endYProperty().bind(square.layoutYProperty().subtract(7 / 2));
        horizontalBottomRight.setStrokeWidth(7);
        windowLayout.getChildren().add(horizontalBottomRight);

        //bottom center line
        Line bottomCenter = new Line();
        bottomCenter.setStroke(Color.WHITE);
        bottomCenter.startXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(8)));
        bottomCenter.endXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(8)));
        bottomCenter.startYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().divide(64)));
        bottomCenter.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().divide(64)));
        bottomCenter.setStrokeWidth(7);
        windowLayout.getChildren().add(bottomCenter);

        //bottom left corner piece
        Line verticalBottomLeft = new Line();
        verticalBottomLeft.setStroke(Color.WHITE);
        verticalBottomLeft.startXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7/2)));
        verticalBottomLeft.endXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7/2)));
        verticalBottomLeft.startYProperty().bind(square.layoutYProperty().subtract(7/2));
        verticalBottomLeft.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).divide(8).subtract(7/2)));
        verticalBottomLeft.setStrokeWidth(7);
        windowLayout.getChildren().add(verticalBottomLeft);

        Line horizontalBottomLeft = new Line();
        horizontalBottomLeft.setStroke(Color.WHITE);
        horizontalBottomLeft.startXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7/2)));
        horizontalBottomLeft.endXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).multiply(3).divide(8).subtract(7/2)));
        horizontalBottomLeft.startYProperty().bind(square.layoutYProperty().subtract(7/2));
        horizontalBottomLeft.endYProperty().bind(square.layoutYProperty().subtract(7/2));
        horizontalBottomLeft.setStrokeWidth(7);
        windowLayout.getChildren().add(horizontalBottomLeft);

        //left center line
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

    //adjusts boundary Y
    public static void adjustY(Rectangle square, double oldHeight, double mouseLocation, ImageView image){
        if(square.getHeight() * (square.getLayoutY() - mouseLocation) / square.getHeight() >= 20) {
            square.setScaleY((square.getLayoutY() - mouseLocation) / square.getHeight());
            square.setTranslateY(-(square.getHeight() * square.getScaleY() / 2) - oldHeight / 2);
            double newImageHeight = square.getHeight() * square.getScaleY() ;

            double imageScale = newImageHeight / image.getImage().getHeight();
            if(image.getImage().getWidth() * imageScale <= square.getWidth() * square.getScaleX()) {
                image.setScaleY(imageScale);
                image.setScaleX(imageScale);
                image.setTranslateX(-image.getImage().getWidth() / 2);
                image.setTranslateY(-((image.getImage().getHeight() / 2) + (image.getImage().getHeight() * image.getScaleX()) / 2));
            }
        }
    }

    //adjusts boundary X
    public static void adjustX(Rectangle square, double oldWidth, double mouseLocation, ImageView image){
        if(square.getWidth() * (square.getLayoutX() - mouseLocation) / square.getWidth() * 2 >= 20) {
            square.setScaleX((square.getLayoutX() - mouseLocation) / square.getWidth() * 2);
            square.setTranslateX(-oldWidth / 2);

            double newImageWidth = square.getWidth() * square.getScaleX();

            double imageScale = newImageWidth / image.getImage().getWidth();
            if(image.getImage().getHeight() * imageScale <= square.getHeight() * square.getScaleY()) {
                image.setScaleY(imageScale);
                image.setScaleX(imageScale);
                image.setTranslateX(-image.getImage().getWidth() / 2);
                image.setTranslateY(-((image.getImage().getHeight() / 2) + (image.getImage().getHeight() * image.getScaleX()) / 2));
            }
        }
    }

    //creates Label settings VBox
    public static VBox createLabelSettings(Text label, String labelName){
        HBox groupLabel = new HBox();
        Label Group = new Label(labelName + " Label");
        Group.setUnderline(true);
        groupLabel.getChildren().add(Group);

        HBox changeSize = new HBox();
        changeSize.setSpacing(5);
        Label sizeLabel = new Label("Scale: ");
        TextField sizeField = new TextField();
        sizeField.promptTextProperty().bind(label.scaleXProperty().asString());
        changeSize.getChildren().addAll(sizeLabel, sizeField);

        HBox changeX = new HBox();
        changeX.setSpacing(5);
        Label XLabel = new Label("X Location:");
        TextField XField = new TextField();
        XField.promptTextProperty().bind(label.layoutXProperty().asString());
        changeX.getChildren().addAll(XLabel, XField);

        HBox changeY = new HBox();
        changeY.setSpacing(5);
        Label YLabel = new Label("Y Location:");
        TextField YField = new TextField();
        YField.promptTextProperty().bind(label.layoutYProperty().asString());
        changeY.getChildren().addAll(YLabel, YField);

        HBox font = new HBox();
        font.setSpacing(5);
        Label fontLabel = new Label("Font:");
        ComboBox<String> fontNameBox = new ComboBox<>();
        fontNameBox.setEditable(false);
        fontNameBox.getItems().addAll(fonts);
        font.getChildren().addAll(fontLabel, fontNameBox);

        HBox color = new HBox();
        color.setSpacing(5);
        Label colorLabel = new Label("Color:");
        ColorPicker colorField = new ColorPicker();
        colorField.setValue((Color) label.getFill());
        color.getChildren().addAll(colorLabel, colorField);

        HBox stroke = new HBox();
        stroke.setSpacing(5);
        Label strokeLabel = new Label("Stroke:");
        CheckBox strokeCheckbox = new CheckBox();
        stroke.getChildren().addAll(strokeLabel, strokeCheckbox);

        HBox strokeWidth = new HBox();
        strokeWidth.setSpacing(5);
        Label strokeWidthLabel = new Label("Stroke Width:");
        TextField strokeWidthField = new TextField();
        strokeWidthField.promptTextProperty().bind(label.strokeWidthProperty().asString());
        strokeWidthField.disableProperty().bind(strokeCheckbox.selectedProperty());
        strokeWidth.getChildren().addAll(strokeWidthLabel, strokeWidthField);

        HBox strokeColor = new HBox();
        strokeColor.setSpacing(5);
        Label strokeColorLabel = new Label("Stroke Color:");
        ColorPicker strokeColorPicker = new ColorPicker();
        strokeColorPicker.setValue((Color) label.getStroke());
        strokeColorLabel.setDisable(true);
        strokeColorPicker.setDisable(true);
        strokeColor.getChildren().addAll(strokeColorLabel, strokeColorPicker);

        HBox textProperties = new HBox();
        textProperties.setSpacing(5);
        Label italics = new Label("Italics:");
        CheckBox italicsCheck = new CheckBox();
        italicsCheck.setSelected(label.getFont().getName().contains("Italic"));
        Label bold = new Label("Bold:");
        CheckBox boldCheck = new CheckBox();
        boldCheck.setSelected(label.getFont().getName().contains("Bold"));
        Label underlined = new Label("Underlined:");
        CheckBox underlinedCheck = new CheckBox();
        underlinedCheck.setSelected(label.isUnderline());
        textProperties.getChildren().addAll(italics, italicsCheck, bold, boldCheck, underlined, underlinedCheck);

        HBox visablility = new HBox();
        visablility.setSpacing(5);
        Label visableLabel = new Label("Visable:");
        CheckBox visableCheck = new CheckBox();
        visableCheck.setSelected(label.isVisible());
        visablility.getChildren().addAll(visableLabel, visableCheck);

        VBox labelVBox = new VBox();
        labelVBox.setSpacing(10);
        labelVBox.getChildren().addAll(groupLabel, changeSize, changeX, changeY, /*font,*/ color, stroke, /*strokeWidth, strokeColor,*/ textProperties, visablility);

        Accordion accordion = new Accordion();
        TitledPane labelTitledPane = new TitledPane(labelName, labelVBox);
        accordion.getPanes().add(labelTitledPane);

        VBox labelSettings = new VBox();
        labelSettings.getChildren().add(accordion);

        sizeField.setOnAction(e -> {
            double scale = 0;
            try{
                scale = parseDouble(sizeField.getText());
                sizeField.setText("");
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            label.setScaleX(scale);
            label.setScaleY(scale);
        });

        XField.setOnAction(e ->{
            double X = 0;
            try{
                X = parseDouble(XField.getText());
                XField.setText("");
            }catch(NumberFormatException f){
                XField.setText("");
            }
            label.setLayoutX(X);
        });

        YField.setOnAction(e ->{
            double Y = 0;
            try{
                Y = parseDouble(YField.getText());
                YField.setText("");
            }catch(NumberFormatException f){
                YField.setText("");
            }
            label.setLayoutY(Y);
        });

        fontNameBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if(newValue != null){
                if(!canBold(newValue)) {
                    boldCheck.setSelected(false);
                    boldCheck.setDisable(true);
                }else
                    boldCheck.setDisable(false);

                if(!canItalic(newValue)){
                    italicsCheck.setSelected(false);
                    italicsCheck.setDisable(true);
                }else
                    italicsCheck.setDisable(false);

                if(boldCheck.isSelected() && italicsCheck.isSelected())
                    label.setFont(Font.font(newValue, FontWeight.BOLD, FontPosture.ITALIC, 12));
                else if(boldCheck.isSelected())
                    label.setFont(Font.font(newValue, FontWeight.BOLD, 12));
                else if(italicsCheck.isSelected())
                    label.setFont(Font.font(newValue, FontPosture.ITALIC, 12));
                else
                    label.setFont(new Font(newValue, 12));
            }
        });

        colorField.setOnAction(e -> label.setFill(colorField.getValue()));

        strokeCheckbox.setOnAction(e -> {
            boolean selected = strokeCheckbox.isSelected();
            if(selected) {
                strokeWidthLabel.setDisable(false);
                strokeWidthField.setDisable(false);
                strokeColorLabel.setDisable(false);
                strokeColorPicker.setDisable(false);
                label.setStrokeWidth(parseDouble(strokeWidthField.getPromptText()));
            }else {
                label.setStrokeWidth(0);
            }
        });

        strokeWidthField.setOnAction(e -> {
            try{
                double width = parseDouble(strokeWidthField.getText());
                label.setStrokeWidth(width);
                strokeWidthField.setPromptText(String.valueOf(label.getStrokeWidth()));
                strokeWidthField.setText("");
            }catch(NumberFormatException f){
                strokeWidthField.setText("");
            }
        });

        strokeColorPicker.setOnAction(e -> label.setStroke(strokeColorPicker.getValue()));

        italicsCheck.setOnAction(e -> {
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

        visableCheck.setOnAction(e -> label.setVisible(visableCheck.isSelected()));

        return labelSettings;
    }

    //change location of Text when dragged
    //change scale of Text when scrolled
    public static void quickEdit(Text element){
        element.setOnScroll(e -> {
            element.setScaleX(element.getScaleX() + (e.getDeltaY() / 1000));
            element.setScaleY(element.getScaleY() + (e.getDeltaY() / 1000));
        });
        element.setOnMousePressed(e -> {
            double diffX = element.getLayoutX() - e.getSceneX();
            double diffY = element.getLayoutY() - e.getSceneY();
            element.setOnMouseDragged(f -> {
                element.setLayoutX(f.getSceneX() + diffX);
                element.setLayoutY(f.getSceneY() + diffY);
            });
        });
    }

    //change location of ImageView when dragged
    //change scale of ImageView when scrolled
    public static void quickEdit(ImageView element){
        element.setOnScroll(e -> {
            double scale = element.getScaleX() + (e.getDeltaY() / 1000);
            if(element.getImage().getWidth() * scale > -element.getFitWidth())
                scale = -element.getFitWidth() / element.getImage().getWidth();
            if(element.getImage().getHeight() * scale > -element.getFitHeight())
                scale = -element.getFitHeight() / element.getImage().getHeight();
            element.setScaleX(scale);
            element.setScaleY(scale);
            element.setTranslateX(-element.getImage().getWidth() / 2);
            element.setTranslateY(-((element.getImage().getHeight() / 2) + (element.getImage().getHeight() * element.getScaleX()) / 2));
        });
        element.setOnMousePressed(e -> {
            double diffX = element.getLayoutX() - e.getSceneX();
            double diffY = element.getLayoutY() - e.getSceneY();
            element.setOnMouseDragged(f -> {
                element.setLayoutX(f.getSceneX() + diffX);
                element.setLayoutY(f.getSceneY() + diffY);
            });
        });
    }

    //creates string array of available fonts
    private static String[] generateFonts(){
        String[] avaliableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        int fontArraySize = 0;
        for (String avaliableFont : avaliableFonts) {
            if (sanitizeAvailableFontStrings(avaliableFont) != null) {
                fontArraySize++;
            }
        }

        String[] temp = new String[fontArraySize];
        int index = 0;
        for (String avaliableFont : avaliableFonts) {
            if (sanitizeAvailableFontStrings(avaliableFont) != null) {
                temp[index] = avaliableFont;
                index++;
            }
        }

        return temp;
    }

    //removes "Bold", "Italics", and "Regular" from given string
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

    //checks to see if using a given font returns the system default font
    public static String sanitizeAvailableFontStrings(String name){
        Font test = new Font(name, 12);
        if(test.getName().compareTo("System Regular") == 0){
            return null;
        }
        return name;
    }

    //checks to see if bolding and unbolding a font returns system default
    public static boolean canBold(String name){
        Font test;
        test = Font.font(name,  FontWeight.BOLD, 12);
        if(test.getName().compareTo(name) == 0)
            return false;
        test = new Font(sanitizeFontName(test.getName()), 12);

        return test.getName().compareTo("System") != 0;
    }

    //checks to see if italicizing and unitalicizing a font returns system default
    public static boolean canItalic(String name){
        Font test;
        test = Font.font(name,  FontPosture.ITALIC, 12);
        if(test.getName().compareTo(name) == 0)
            return false;
        test = new Font(sanitizeFontName(test.getName()), 12);

        return test.getName().compareTo("System") != 0;
    }

    //method to create Tree Item branches
    public void makeBranch(String title, TreeItem<String> parent){
        TreeItem<String> item = new TreeItem<>(title);
        parent.getChildren().add(item);
    }
}