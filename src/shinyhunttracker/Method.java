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
            case "Rader Chaining":
                this.name = name;
                break;
            case "Chain Fishing":
                this.name = name;
                break;
            case "Friend Safari":
                this.name = name;
                modifier = 6;
                break;
            case "DexNav":
                this.name = name;
                break;
            case "SOS Chaining":
                this.name = name;
                break;
            case "Ultra Wormholes":
                this.name = name;
                break;
            case "Catch Combo":
                this.name = name;
                break;
            case "Total Encounters":
                this.name = name;
                break;
            default:
                break;
        }
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
