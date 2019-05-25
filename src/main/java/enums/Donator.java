package enums;

public enum Donator {

    HART69("Hart69#0001"),
    ELDER_MASTER("Elder-Master#7684"),
    DARKRAI("Darkrai#8780"),
    DREAMSVOID("DreamsVoid#8802"),
    SIID("! ༺ -Siid- ༻ !#0001");

    private String name;

    Donator(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
