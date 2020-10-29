package shinyhunttracker;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Method {
    StringProperty name = new SimpleStringProperty();
    int base;
    int modifier;

    Method(){
        name.setValue("");
        base = 8192;
        modifier = 0;
    }

    Method(String name, int generation){
        //determines the initial base and modifier with the selected game and method
        if(generation >= 5)
            base = 4096;
        else
            base = 8192;
        switch(name){
            case "None":
            case "Radar Chaining":
            case "Chain Fishing":
            case "DexNav":
            case "SOS Chaining":
            case "Ultra Wormholes":
            case "Catch Combo":
            case "Total Encounters":
                this.name.setValue(name);
                modifier = 1;
                break;
            case "Breeding with Shiny":
                this.name.setValue(name);
                modifier += 128;
                break;
            case "Masuda":
                this.name.setValue(name);
                if(generation == 4)
                    modifier = 5;
                else
                    modifier = 6;
                break;
            case "Friend Safari":
                this.name.setValue(name);
                modifier = 6;
                break;
            case "Dynamax Adventure":
                this.name.setValue(name);
                base = 300;
                modifier = 1;
            default:
                break;
        }
    }

    //returns modifier to added to the base modifier by selected method with given encounters

    public int chainFishing(int encounters){
        if(encounters >= 20)
            encounters = 20;
        return encounters * 2;
    }

    public int dexNav(int encounters, int searchLevel){
        int searchPoints = searchLevel;
        double points = 0;
        if (searchLevel < 999) {
            searchLevel++;
            if (searchPoints >= 100) {
                points += 100 * 6.0;
                searchPoints -= 100;
            }
            else
                points += searchLevel * 6.0;

            if (searchPoints >= 100) {
                points += 100 * 2.0;
                searchPoints -= 100;
            }
            else
                points += searchLevel * 2.0;

            if (searchPoints > 0) {
                points += searchPoints;
            }
        }
        else
            points = 1599;
        points = points / 100;
        double shinyOdds = points / 10000;
        double normalOdds = 1 - shinyOdds;

        if(encounters == 50)
            return (int)(1/(1 - Math.pow(normalOdds, modifier + 5)));
        else if (encounters == 100)
            return (int)(1 /(1 - Math.pow(normalOdds, modifier + 10)));
        else if((int)(1 / (1 - Math.pow(normalOdds, modifier))) < base && searchLevel != 0)
            return (int)(1 /(1 - Math.pow(normalOdds, modifier)));
        else
            return 1;
    }

    public int sosChaining(int encounters){
        if(encounters >= 255 && (name.getValue().compareTo("Sun") == 0 || name.getValue().compareTo("Moon") == 0)) {
            while(encounters >= 255){
                encounters = encounters - 255;
            }
        }
        if (encounters < 10)
            return 0;
        else if (encounters < 20)
            return 4;
        else if (encounters < 30)
            return 8;
        else
            return 12;
    }

    public int catchCombo(int encounters){
        if(encounters >= 0 && encounters <= 10)
            return 0;
        else if (encounters > 10 && encounters <= 20)
            return 3;
        else if (encounters > 20 && encounters <= 30)
            return 7;
        else
            return 11;
    }

    public int totalEncounters(int previousEncounters){
        if(previousEncounters < 50)
            return 0;
        else if(previousEncounters < 100)
            return 1;
        else if (previousEncounters < 200)
            return 2;
        else if (previousEncounters < 300)
            return 3;
        else if (previousEncounters < 500)
            return 4;
        else
            return 5;
    }

    public StringProperty getNameProperty(){return name;}

    public String getName(){
        return name.getValue();
    }

    public int getModifier(){
        return modifier;
    }

    public int getBase(){
        return base;
    }

    public void setModifier(int modifier){
        this.modifier = modifier;
    }
}
