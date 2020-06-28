package shinyhunttracker;

import javafx.fxml.FXMLLoader;
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
            String saveData = selectedPokemon.getName() + "," + selectedGame.getName() + "," + selectedGame.getGeneration() + "," + selectedMethod.getName() + "," + selectedMethod.getModifier() + "," + encounters + ",";
            File file = new File("Save Data/PreviousHunts.txt");
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            int sameDataLine = checkForPreviousData(saveData);
            if(sameDataLine == -1)
                bufferedWriter.write(saveData);
            else
                replaceLine(sameDataLine, saveData);

            bufferedWriter.close();
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

    public int checkForPreviousData(String saveData) throws IOException{
        BufferedReader fileReader = new BufferedReader(new FileReader("Save Data/PreviousHunts.txt"));
        int lineNumber = -1;

        String line;
        while((line = fileReader.readLine()) != null){
            lineNumber++;
            for(int i = 0; i < 6; i++){
                if(i == 5)
                    return lineNumber;
                if(spiltString(saveData,i).compareTo(spiltString(line,i)) != 0) {
                    break;
                }
            }
        }
        return lineNumber;
    }

    public void replaceLine(int lineNumber, String saveData) throws IOException{
        BufferedReader fileReader = new BufferedReader(new FileReader("Save Data/PreviousHunts.txt"));
        StringBuilder inputBuffer = new StringBuilder();

        for(int i = 0; i < getfileLength(); i++){
            String line = fileReader.readLine();
            if(i == lineNumber){
                line = saveData;
            }
            inputBuffer.append(line);
            inputBuffer.append('\n');
        }

        FileOutputStream fileOut = new FileOutputStream("Save Data/PreviousHunts.txt");
        fileOut.write(inputBuffer.toString().getBytes());
        fileOut.close();
    }
}
