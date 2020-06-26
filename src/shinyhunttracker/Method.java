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

    public void chainFishing(int encounters){

    }

    public void dexNav(int encounters){

    }

    public void sosChaining(int encounters){

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
