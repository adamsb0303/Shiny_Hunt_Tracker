package shinyhunttracker;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

import static java.lang.Integer.parseInt;

public class SaveData {
    Pokemon selectedPokemon;
    Game selectedGame;
    Method selectedMethod;
    int encounters;

    SaveData(){

    }

    SaveData(Pokemon selectedPokemon, Game selectedGame, Method selectedMethod, int encounters){
        this.selectedPokemon = selectedPokemon;
        this.selectedGame = selectedGame;
        this.selectedMethod = selectedMethod;
        this.encounters = encounters;
    }

    public void saveHunt(){
        try {
            File file = new File("Save Data/PreviousHunts.txt");
            FileWriter test = new FileWriter(file, true);
            BufferedWriter fileWriter = new BufferedWriter(test);
            fileWriter.write(selectedPokemon.getName() + "," + selectedGame.getName() + "," + selectedGame.getGeneration() + "," + selectedMethod.getName() + "," + selectedMethod.getModifier() + "," + encounters + ",\n");
            fileWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadHunt(int lineNumber){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("Save Data/PreviousHunts.txt"));

            for(int i = 0; i < getfileLength(); i++) {
                String line = fileReader.readLine();
                if(i == lineNumber) {
                    int generation = parseInt(spiltString(line, 2));
                    selectedPokemon = new Pokemon(spiltString(line, 0), generation);
                    selectedGame = new Game(spiltString(line, 1), generation);
                    selectedMethod = new Method(spiltString(line, 3), generation);
                    selectedMethod.setModifier(parseInt(spiltString(line, 4)));
                    encounters = parseInt(spiltString(line, 5));
                    break;
                }
            }

            beginHunt();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void beginHunt() throws IOException{
        FXMLLoader huntControlsLoader = new FXMLLoader();
        huntControlsLoader.setLocation(getClass().getResource("huntControls.fxml"));
        Parent hunterControlsParent = huntControlsLoader.load();

        huntControlsController huntControlsController = huntControlsLoader.getController();
        huntControlsController.createHuntWindow(selectedPokemon, selectedGame, selectedMethod);

        Stage huntControls = new Stage();
        huntControls.setTitle("Hunt Controls");
        huntControls.setResizable(false);
        huntControls.setScene(new Scene(hunterControlsParent, 600, 150));
        huntControlsController.importStage(huntControls);
        huntControls.show();

        if(selectedMethod.getName().compareTo("DexNav") == 0 || selectedMethod.getName().compareTo("Total Encounters") == 0) {
            huntControlsController.promptPreviousEncounters();
        }
    }

    public String getLinefromFile(int lineNumber){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("Save Data/PreviousHunts.txt"));

            for(int i = 0; i < getfileLength(); i++) {
                String line = fileReader.readLine();
                if(i == lineNumber)
                    return (i+1) + ") " + spiltString(line, 0) + " | " + spiltString(line, 1) + " | " + spiltString(line, 3) + " | " + spiltString(line, 5) + " encounters";
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private String spiltString(String line, int word){
        int index;
        for(int i = 0; i < word; i++){
            index = line.indexOf(',');
            line = line.substring(index + 1);
        }
        return line.substring(0,line.indexOf(','));
    }

    public int getfileLength(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Save Data/PreviousHunts.txt"));
            int lines = 0;
            while(reader.readLine() != null)
                lines++;
            return lines;
        }catch (IOException e){
            e.printStackTrace();
        }
        return 0;
    }
}
