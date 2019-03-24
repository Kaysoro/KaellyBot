package enums;

public enum Donator {

    IHART69("Hart69#0001"),
    ELDER_MASTER("Elder-Master#7684"),
    DARKRAI("Darkrai#6493"),
    DREAMSVOID("DreamsVoid#8802");

    private String name;

    Donator(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
