package shinyhunttracker;

import java.io.*;

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
            FileOutputStream f = new FileOutputStream(new File("Save Data/PreviousHunts.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(selectedPokemon);
            o.writeObject(selectedGame);
            o.writeObject(selectedMethod);
            o.writeObject(encounters);

            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e){
            System.out.println("Error initializing stream");
        }
        System.out.println("Hunt saved successfully");
    }

    public void loadHunt(){
        try {
            FileInputStream fi = new FileInputStream(new File("Save Data/PreviousHunts.txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);

            this.selectedPokemon = (Pokemon) oi.readObject();
            this.selectedGame = (Game) oi.readObject();
            this.selectedMethod = (Method) oi.readObject();
            encounters = (int) oi.readObject();

            oi.close();
            fi.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e){
            System.out.println("Error initializing stream");
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
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
