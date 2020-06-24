package shinyhunttracker;

public class Method {
    String name;
    int base;
    int modifier;

    Method(){
        name = "";
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
            case "Breeding with a Shiny":
                this.name = name;
                modifier += 128;
            case "Masuda":
                this.name = name;
                if(generation == 4)
                    modifier = 5;
                else
                    modifier = 6;
            case "Rader Chaining":
                this.name = name;
            case "Chain Fishing":
                this.name = name;
            case "Friend Safari":
                this.name = name;
                modifier = 6;
            case "DexNav":
                this.name = name;
            case "SOS Chaining":
                this.name = name;
            case "Ultra Wormholes":
                this.name = name;
            case "Catch Combo":
                this.name = name;
            case "Total Encounters":
                this.name = name;
            default:
                break;
        }
    }


}
