package shinyhunttracker;

import java.io.*;
import java.nio.Buffer;

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

    public void loadHunt(){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("Save Data/PreviousHunts.txt"));

            String line = fileReader.readLine();
            if(line != null) {
                int generation = parseInt(spiltString(line, 2));
                selectedPokemon = new Pokemon(spiltString(line, 0), generation);
                selectedGame = new Game(spiltString(line, 1), generation);
                selectedMethod = new Method(spiltString(line, 3), generation);
                selectedMethod.setModifier(parseInt(spiltString(line, 4)));
                encounters = parseInt(spiltString(line, 5));
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private String spiltString(String line, int word){
        int index;
        for(int i = 0; i < word; i++){
            index = line.indexOf(',');
            line = line.substring(index + 1);
        }
        return line.substring(0,line.indexOf(','));
    }

    public Pokemon getHuntPokemon(){
        return selectedPokemon;
    }

    public Game getHuntGame(){
        return selectedGame;
    }

    public Method getHuntMethod(){
        return selectedMethod;
    }

    public int getHuntEncounters(){
        return encounters;
    }
}
