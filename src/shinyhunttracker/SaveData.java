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
    public void saveHunt(){
        try {
            String saveData = selectedPokemon.getName() + "," + selectedGame.getName() + "," + selectedGame.getGeneration() + "," + selectedMethod.getName() + "," + selectedMethod.getModifier() + "," + encounters + "," + combo + "," + increment + "," + layout + ",";
            File file = new File("Save Data/PreviousHunts.txt");
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            int sameDataLine = checkForPreviousData(saveData);
            if(sameDataLine == -1) {
                bufferedWriter.write(saveData);
                bufferedWriter.write("\n");
            }
            else
                replaceLine(sameDataLine, saveData, "PreviousHunts");
            bufferedWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //pulls information from previous hunts file
    public void loadHunt(int lineNumber, huntController controller){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("Save Data/PreviousHunts.txt"));

            for(int i = 0; i < getfileLength("PreviousHunts"); i++) {
                String line = fileReader.readLine();
                if(i == lineNumber) {
                    int generation = parseInt(splitString(line, 2));
                    selectedPokemon = new Pokemon(splitString(line, 0), generation);
                    selectedGame = new Game(splitString(line, 1), generation);
                    selectedMethod = new Method(splitString(line, 3), generation);
                    selectedMethod.setModifier(parseInt(splitString(line, 4)));
                    encounters = parseInt(splitString(line, 5));
                    combo = parseInt(splitString(line, 6));
                    increment = parseInt(splitString(line, 7));
                    layout = splitString(line, 8);
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
            String saveData = selectedPokemon.getName() + "," + selectedGame.getName() + "," + selectedGame.getGeneration() + "," + selectedMethod.getName() + "," + selectedMethod.getModifier() + "," + encounters + ",";
            File file = new File("Save Data/CaughtPokemon.txt");
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

        controller.addHuntWindow(selectedPokemon, selectedGame, selectedMethod, family.getStage0().getName(), family.getStage1().getName(), layout, 0, 0, 1);
    }

    //saves layout
    public void saveLayout(String layoutName, AnchorPane huntLayout, boolean newSave){
        if(newSave) {
            try {
                File file;
                if(layoutName.substring(0, layoutName.indexOf('/')).compareTo("Hunts") == 0)
                    file = new File("Save Data/Layouts/Hunts/~Layouts.txt");
                else
                    file = new File("Save Data/Layouts/Previously-Caught/~Layouts.txt");
                FileWriter fileWriter = new FileWriter(file, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                bufferedWriter.write(layoutName.substring(layoutName.indexOf('/') + 1) + '\n');
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try{
            File file = new File("Save Data/Layouts/" + layoutName + ".txt");
            FileWriter fileWriter = new FileWriter(file, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for(Node i : huntLayout.getChildren()) {
                if(i instanceof ImageView){
                    ImageView image = (ImageView) i;
                    bufferedWriter.write(image.getLayoutX() + "," + image.getLayoutY() + "," + image.getScaleX() + "," + image.isVisible() + ",\n");
                }else {
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
            BufferedReader fileReader = new BufferedReader(new FileReader("Save Data/Layouts/" + layoutName + ".txt"));

            if(layoutName.substring(0, layoutName.indexOf('/')).compareTo("Hunts") == 0) {
                for (int i = 0; i < getfileLength("Layouts/" + layoutName) - 1; i++) {
                    String line = fileReader.readLine();
                    if (i < 3) {
                        try {
                            ImageView image = (ImageView) huntLayout.getChildren().get(i);
                            image.setLayoutX(parseDouble(splitString(line, 0)));
                            image.setLayoutY(parseDouble(splitString(line, 1)));
                            double scale = parseDouble(splitString(line, 2));
                            image.setScaleX(scale);
                            image.setScaleY(scale);
                            image.setTranslateX(-(image.getImage().getWidth() * ((1 - scale) / 2)));
                            image.setTranslateY(-(image.getImage().getHeight() * ((1 - scale) / 2)));
                            image.setVisible(splitString(line, 3).compareTo("true") == 0);
                        } catch (NullPointerException ignored) {

                        }
                    } else if (i < 10) {
                        Text text = (Text) huntLayout.getChildren().get(i);
                        text.setLayoutX(parseDouble(splitString(line, 0)));
                        text.setLayoutY(parseDouble(splitString(line, 1)));
                        text.setScaleX(parseDouble(splitString(line, 2)));
                        text.setScaleY(parseDouble(splitString(line, 2)));
                        text.setFont(new Font(splitString(line, 3), 12));
                        text.setFill(Color.web(splitString(line, 4)));
                        text.setStrokeWidth(parseDouble(splitString(line, 5)));
                        text.setStroke(Color.web(splitString(line, 6)));
                        text.setUnderline(splitString(line, 7).compareTo("true") == 0);
                        text.setVisible(splitString(line, 8).compareTo("true") == 0);
                    }
                }
                huntLayout.setBackground(new Background(new BackgroundFill(Color.web(getLinefromFile(getfileLength("Layouts/" + layoutName) - 1, ("Layouts/" + layoutName))), CornerRadii.EMPTY, Insets.EMPTY)));
            }else{
                for(int i = 0; i < getfileLength("Layouts/" + layoutName) - 2; i++){
                    String line = fileReader.readLine();
                    if(i % 4 == 0){
                        ImageView image = (ImageView) huntLayout.getChildren().get(i);
                        image.setLayoutX(parseDouble(splitString(line, 0)));
                        image.setLayoutY(parseDouble(splitString(line, 1)));
                        double scale = parseDouble(splitString(line, 2));
                        image.setScaleX(scale);
                        image.setScaleY(scale);
                        image.setTranslateX(-(image.getImage().getWidth() * ((1 - scale) / 2)));
                        image.setTranslateY(-(image.getImage().getHeight() * ((1 - scale) / 2)));
                        image.setVisible(splitString(line, 3).compareTo("true") == 0);
                    }else{
                        Text text = (Text) huntLayout.getChildren().get(i);
                        text.setLayoutX(parseDouble(splitString(line, 0)));
                        text.setLayoutY(parseDouble(splitString(line, 1)));
                        text.setScaleX(parseDouble(splitString(line, 2)));
                        text.setScaleY(parseDouble(splitString(line, 2)));
                        text.setFont(new Font(splitString(line, 3), 12));
                        text.setFill(Color.web(splitString(line, 4)));
                        text.setStrokeWidth(parseDouble(splitString(line, 5)));
                        text.setStroke(Color.web(splitString(line, 6)));
                        text.setUnderline(splitString(line, 7).compareTo("true") == 0);
                        text.setVisible(splitString(line, 8).compareTo("true") == 0);
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
            BufferedReader fileReader = new BufferedReader(new FileReader("Save Data/" + file + ".txt"));

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

    //splits the given string by the , and returns the number word that is given
    public String splitString(String line, int word){
        int index;
        for(int i = 0; i < word; i++){
            index = line.indexOf(',');
            line = line.substring(index + 1);
        }
        return line.substring(0,line.indexOf(','));
    }

    //returns how many lines are in the previous hunts file
    public int getfileLength(String file){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Save Data/" + file + ".txt"));
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
    public int checkForPreviousData(String saveData) throws IOException{
        BufferedReader fileReader = new BufferedReader(new FileReader("Save Data/PreviousHunts.txt"));

        int lineNumber = 0;
        String line;
        while((line = fileReader.readLine()) != null){
            for(int i = 0; i < 6; i++){
                if(i == 5)
                    return lineNumber;
                if(splitString(saveData,i).compareTo(splitString(line,i)) != 0) {
                    break;
                }
            }
            lineNumber++;
        }
        return -1;
    }

    //replaces the given line with the given string
    public void replaceLine(int lineNumber, String saveData, String file) throws IOException{
        BufferedReader fileReader = new BufferedReader(new FileReader("Save Data/" + file + ".txt"));
        StringBuilder inputBuffer = new StringBuilder();

        for(int i = 0; i < getfileLength(file); i++){
            String line = fileReader.readLine();
            if(i == lineNumber){
                line = saveData;
            }
            inputBuffer.append(line);
            inputBuffer.append('\n');
        }

        FileOutputStream fileOut = new FileOutputStream("Save Data/" + file + ".txt");
        fileOut.write(inputBuffer.toString().getBytes());
        fileOut.close();
    }

    //deletes the given line
    public void deleteLine(int lineNumber, String file) throws IOException{
        BufferedReader fileReader = new BufferedReader(new FileReader("Save Data/" + file + ".txt"));
        StringBuilder inputBuffer = new StringBuilder();

        for(int i = 0; i < getfileLength(file); i++){
            String line = fileReader.readLine();
            if(i == lineNumber){
                continue;
            }
            inputBuffer.append(line);
            inputBuffer.append('\n');
        }

        FileOutputStream fileOut = new FileOutputStream("Save Data/" + file + ".txt");
        fileOut.write(inputBuffer.toString().getBytes());
        fileOut.close();
    }
}
