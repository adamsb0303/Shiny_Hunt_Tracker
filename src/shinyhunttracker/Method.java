package shinyhunttracker;

public class Method {
    String name;
    int base;
    int modifier;

    Method(){
        base = 8192;
        modifier = 0;
    }

    Method(String name, int generation){
        if(generation >= 5)
            base = 4096;
        switch(name){
            case "None":
            case "Radar Chaining":
            case "Chain Fishing":
            case "DexNav":
            case "SOS Chaining":
            case "Ultra Wormholes":
            case "Catch Combo":
            case "Total Encounters":
                this.name = name;
                modifier = 1;
                break;
            case "Breeding with a Shiny":
                this.name = name;
                modifier += 128;
                break;
            case "Masuda":
                this.name = name;
                if(generation == 4)
                    modifier = 5;
                else
                    modifier = 6;
                break;
            case "Friend Safari":
                this.name = name;
                modifier = 6;
                break;
            default:
                break;
        }
    }

    public void radarChaining(int encounters){

    }

    public int chainFishing(int encounters){
        if(encounters >= 20)
            encounters = 20;
        return encounters * 2;
    }

    public void dexNav(int encounters){

    }

    public int sosChaining(int encounters){
        if(encounters >= 255 && (name.compareTo("Sun") == 0 || name.compareTo("Moon") == 0)) {
            while(encounters >= 255){
                encounters = encounters - 255;
            }
        }
        if (encounters >= 0 && encounters < 10)
            return 0;
        else if (encounters >= 10 && encounters < 20)
            return 4;
        else if (encounters >= 20 && encounters < 30)
            return 8;
        else
            return 12;
    }

    public void catchCombo(int encounters){

    }

    public void totalEncounters(int encounters){

    }

    public String getName(){
        return name;
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
