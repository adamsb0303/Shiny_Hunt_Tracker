package shinyhunttracker;

import javafx.beans.property.IntegerProperty;

public class Hunt {
    Pokemon selectedPokemon;
    Game selectedGame;
    Method selectedMethod;
    IntegerProperty encounters, combo;
    int huntNumber, increment, methodBase;
    char keyBind;

    public Hunt(Pokemon selectedPokemon, Game selectedGame, Method selectedMethod, IntegerProperty encounters, IntegerProperty combo, int increment, int huntNum, char keybind) {
        this.selectedPokemon = selectedPokemon;
        this.selectedGame = selectedGame;
        this.selectedMethod = selectedMethod;
        this.encounters = encounters;
        this.combo = combo;
        this.huntNumber = huntNum;
        this.increment = increment;
        this.keyBind = keybind;
    }

    //writes objects to previous hunts file
    public void saveHunt(String filePath){
        SaveData data = new SaveData(selectedPokemon, selectedGame, selectedMethod, encounters.getValue(), combo.getValue(), increment, "");
        boolean tempSave = filePath.contains("previousSession");
        data.saveHunt(filePath, tempSave);
    }

    public void incrementEncounters(){
        encounters.setValue(encounters.getValue() + increment);
    }

    public Pokemon getSelectedPokemon() {return selectedPokemon;}

    public void setSelectedPokemon(Pokemon selectedPokemon) {this.selectedPokemon = selectedPokemon;}

    public Game getSelectedGame() {return selectedGame;}

    public void setSelectedGame(Game selectedGame) {this.selectedGame = selectedGame;}

    public Method getSelectedMethod() {return selectedMethod;}

    public void setSelectedMethod(Method selectedMethod) {this.selectedMethod = selectedMethod;}

    public int getEncounters() { return encounters.get(); }

    public IntegerProperty encountersProperty() {return encounters;}

    public void setEncounters(int encounters) {this.encounters.set(encounters);}

    public int getCombo() {return combo.get();}

    public IntegerProperty comboProperty() {return combo;}

    public void setCombo(int combo) {this.combo.set(combo);}

    public int getHuntNumber() {return huntNumber;}

    public void setHuntNumber(int huntNumber) {this.huntNumber = huntNumber;}

    public int getIncrement() {return increment;}

    public void setIncrement(int increment) {this.increment = increment;}

    public char getKeyBind() {return keyBind;}

    public void setKeyBind(char keyBind) {this.keyBind = keyBind;}
}
