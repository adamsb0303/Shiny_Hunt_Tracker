package shinyhunttracker;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.json.simple.JSONObject;

import java.io.*;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class SaveData {
    Pokemon selectedPokemon;
    Game selectedGame;
    Method selectedMethod;
    String layout;
    int encounters, combo, increment;

    SaveData(){

    }

    SaveData(Pokemon selectedPokemon, Game selectedGame, Method selectedMethod, int encounters, int combo, int increment, String layout){
        this.selectedPokemon = selectedPokemon;
        this.selectedGame = selectedGame;
        this.selectedMethod = selectedMethod;
        this.encounters = encounters;
        this.combo = combo;
        this.increment = increment;
        this.layout = layout;
    }

    //writes information to previous hunts file
    //tempSave is for saving the hunts that are open when the program closes
    public void saveHunt(String filePath, boolean tempSave){
        try {
            String saveData = selectedPokemon.getName() + "," + selectedPokemon.getForm() + "," + selectedGame.getName() + "," + selectedGame.getGeneration() + "," + selectedMethod.getName() + "," + selectedMethod.getModifier() + "," + encounters + "," + combo + "," + increment + "," + layout + ",";
            File file = new File(filePath);
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            int sameDataLine = checkForPreviousData(saveData);
            if(tempSave)
                sameDataLine = -1;
            if (sameDataLine == -1) {
                bufferedWriter.write(saveData);
                bufferedWriter.write("\n");
            } else
                replaceLine(sameDataLine, saveData, "PreviousHunts");
            bufferedWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //pulls information from previous hunts file
    public void loadHunt(int lineNumber, huntController controller, String filePath){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(filePath));

            for(int i = 0; i < getfileLength(filePath.substring(filePath.indexOf('/'), filePath.length() - 4)); i++) {
                String line = fileReader.readLine();
                String[] data = line.split(",");
                if(i == lineNumber) {
                    int generation = parseInt(data[3]);
                    //selectedPokemon = new Pokemon(data[0]);
                    selectedPokemon.setForm(data[1]);
                    selectedGame = new Game(data[2], generation);
                    selectedMethod = new Method(data[4], generation);
                    selectedMethod.setModifier(parseInt(data[5]));
                    encounters = parseInt(data[6]);
                    combo = parseInt(data[7]);
                    increment = parseInt(data[8]);
                    layout = data[9];
                    if(layout.compareTo("") == 0)
                        layout = null;
                    break;
                }
            }

            beginHunt(controller);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //writes information to caught pokemon file
    public void pokemonCaught(){
        try{
            String saveData = selectedPokemon.getName() + "," + selectedPokemon.getForm() + "," + selectedGame.getName() + "," + selectedGame.getGeneration() + "," + selectedMethod.getName() + "," + selectedMethod.getModifier() + "," + encounters + ",";
            File file = new File("SaveData/CaughtPokemon.txt");
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            int sameDataLine = checkForPreviousData(saveData);
            if(sameDataLine != -1)
                deleteLine(sameDataLine, "PreviousHunts");
            bufferedWriter.write(saveData);
            bufferedWriter.write("\n");
            bufferedWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //opens huntControls window
    public void beginHunt(huntController controller){
        selectionPageController family = new selectionPageController();
        family.createFamily(selectedPokemon.getName());

        controller.addHuntWindow(selectedPokemon, selectedGame, selectedMethod, family.getStage0().getName(), family.getStage1().getName(), layout, encounters, combo, 1);
    }

    //saves layout
    public void saveLayout(String layoutName, AnchorPane huntLayout, boolean newSave){
        if(newSave) {
            try {
                File file;
                if(layoutName.substring(0, layoutName.indexOf('/')).compareTo("Hunts") == 0)
                    file = new File("SaveData/Layouts/Hunts/~Layouts.txt");
                else
                    file = new File("SaveData/Layouts/Previously-Caught/~Layouts.txt");
                FileWriter fileWriter = new FileWriter(file, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                bufferedWriter.write(layoutName.substring(layoutName.indexOf('/') + 1) + '\n');
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try{
            File file = new File("SaveData/Layouts/" + layoutName + ".txt");
            FileWriter fileWriter = new FileWriter(file, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for(Node i : huntLayout.getChildren()) {
                if(i instanceof ImageView){
                    ImageView image = (ImageView) i;
                    bufferedWriter.write(image.getLayoutX() + "," + image.getLayoutY() + "," + image.getFitWidth() + "," + image.getFitHeight() + "," + image.isVisible() + ",\n");
                }else if(i instanceof Text){
                    Text text = (Text) i;
                    bufferedWriter.write(text.getLayoutX() + "," + text.getLayoutY() + "," + text.getScaleX() + "," + text.getFont().getName() + "," + text.getFill() + "," + text.getStrokeWidth() + "," + text.getStroke() + "," + text.isUnderline() + "," + text.isVisible() + ",\n");
                }
            }
            bufferedWriter.write(String.valueOf(huntLayout.getBackground().getFills().get(0).getFill()) + '\n');
            if(layoutName.substring(0, layoutName.indexOf('/')).compareTo("Previously-Caught") == 0)
                bufferedWriter.write(String.valueOf(huntLayout.getChildren().size() / 4) + '\n');
            bufferedWriter.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //load saved layout
    public void loadLayout(String layoutName, AnchorPane huntLayout){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("SaveData/Layouts/" + layoutName + ".txt"));

            if(layoutName.substring(0, layoutName.indexOf('/')).compareTo("Hunts") == 0) {
                for (int i = 0; i < getfileLength("Layouts/" + layoutName) - 1; i++) {
                    String line = fileReader.readLine();
                    String[] data = line.split(",");
                    if (i < 3) {
                        ImageView image = (ImageView) huntLayout.getChildren().get(i);
                        image.setLayoutX(parseDouble(data[0]));
                        image.setLayoutY(parseDouble(data[1]));
                        double imageWidth = -parseDouble(data[2]);
                        double imageHeight = -parseDouble(data[3]);
                        if(-image.getFitWidth() != imageWidth && -image.getFitHeight() != imageHeight){
                            double width = -image.getFitWidth();
                            double height = -image.getFitHeight();
                            double scale;
                            if(height > width)
                                scale = imageHeight / height;
                            else
                                scale = imageWidth / width;
                            image.setScaleX(image.getScaleX() * scale);
                            image.setScaleY(image.getScaleY() * scale);
                            image.setTranslateX(-image.getImage().getWidth() / 2);
                            image.setTranslateY(-((image.getImage().getHeight() / 2) + (image.getImage().getHeight() * image.getScaleX()) / 2));
                        }
                        image.setVisible(data[4].compareTo("true") == 0);
                    } else if (i < 10) {
                        Text text = (Text) huntLayout.getChildren().get(i);
                        text.setLayoutX(parseDouble(data[0]));
                        text.setLayoutY(parseDouble(data[1]));
                        text.setScaleX(parseDouble(data[2]));
                        text.setScaleY(parseDouble(data[2]));
                        text.setFont(new Font(data[3], 12));
                        text.setFill(Color.web(data[4]));
                        text.setStrokeWidth(parseDouble(data[5]));
                        text.setStroke(Color.web(data[6]));
                        text.setUnderline(data[7].compareTo("true") == 0);
                        text.setVisible(data[8].compareTo("true") == 0);
                    }
                }
                huntLayout.setBackground(new Background(new BackgroundFill(Color.web(getLinefromFile(getfileLength("Layouts/" + layoutName) - 1, ("Layouts/" + layoutName))), CornerRadii.EMPTY, Insets.EMPTY)));
            }else{
                for(int i = 0; i < getfileLength("Layouts/" + layoutName) - 2; i++){
                    String line = fileReader.readLine();
                    String[] data = line.split(",");
                    if(i % 4 == 0){
                        ImageView image = (ImageView) huntLayout.getChildren().get(i);
                        image.setLayoutX(parseDouble(data[0]));
                        image.setLayoutY(parseDouble(data[1]));
                        double imageWidth = -parseDouble(data[2]);
                        double imageHeight = -parseDouble(data[3]);
                        if(-image.getFitWidth() != imageWidth && -image.getFitHeight() != imageHeight){
                            double width = -image.getFitWidth();
                            double height = -image.getFitHeight();
                            double scale;
                            if(height > width)
                                scale = imageHeight / height;
                            else
                                scale = imageWidth / width;
                            image.setScaleX(image.getScaleX() * scale);
                            image.setScaleY(image.getScaleY() * scale);
                            image.setTranslateX(-image.getImage().getWidth() / 2);
                            image.setTranslateY(-((image.getImage().getHeight() / 2) + (image.getImage().getHeight() * image.getScaleX()) / 2));
                        }
                        image.setVisible(data[4].compareTo("true") == 0);
                    }else{
                        Text text = (Text) huntLayout.getChildren().get(i);
                        text.setLayoutX(parseDouble(data[0]));
                        text.setLayoutY(parseDouble(data[1]));
                        text.setScaleX(parseDouble(data[2]));
                        text.setScaleY(parseDouble(data[2]));
                        text.setFont(new Font(data[3], 12));
                        text.setFill(Color.web(data[4]));
                        text.setStrokeWidth(parseDouble(data[5]));
                        text.setStroke(Color.web(data[6]));
                        text.setUnderline(data[7].compareTo("true") == 0);
                        text.setVisible(data[8].compareTo("true") == 0);
                    }
                }
                huntLayout.setBackground(new Background(new BackgroundFill(Color.web(getLinefromFile(getfileLength("Layouts/" + layoutName) - 2, ("Layouts/" + layoutName))), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //returns the given line from the previous hunts file
    public String getLinefromFile(int lineNumber, String file){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("SaveData/" + file + ".txt"));

            for(int i = 0; i < getfileLength(file); i++) {
                String line = fileReader.readLine();
                if(i == lineNumber)
                    return line;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    //returns how many lines are in the previous hunts file
    public int getfileLength(String file){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("SaveData/" + file + ".txt"));
            int lines = 0;
            while(reader.readLine() != null)
                lines++;
            return lines;
        }catch (IOException e){
            e.printStackTrace();
        }
        return 0;
    }

    //checks to see if the given string is already in the preivous hunts file
    public int checkForPreviousData(String saveData){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("SaveData/PreviousHunts.txt"));

            int lineNumber = 0;
            String line;

            String[] saveDataArray = saveData.split(",");
            while ((line = fileReader.readLine()) != null) {
                String[] lineDataArray = line.split(",");
                for (int i = 0; i < 6; i++) {
                    if (i == 5)
                        return lineNumber;
                    if (saveDataArray[i].compareTo(lineDataArray[i]) != 0) {
                        break;
                    }
                }
                lineNumber++;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return -1;
    }

    //replaces the given line with the given string
    public void replaceLine(int lineNumber, String saveData, String file){
        try{
            BufferedReader fileReader = new BufferedReader(new FileReader("SaveData/" + file + ".txt"));
            StringBuilder inputBuffer = new StringBuilder();

            for (int i = 0; i < getfileLength(file); i++) {
                String line = fileReader.readLine();
                if (i == lineNumber) {
                    line = saveData;
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }

            FileOutputStream fileOut = new FileOutputStream("SaveData/" + file + ".txt");
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //deletes the given line
    public void deleteLine(int lineNumber, String file){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("SaveData/" + file + ".txt"));
            StringBuilder inputBuffer = new StringBuilder();

            for (int i = 0; i < getfileLength(file); i++) {
                String line = fileReader.readLine();
                if (i == lineNumber) {
                    continue;
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }

            FileOutputStream fileOut = new FileOutputStream("SaveData/" + file + ".txt");
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
