package enums;

public enum Donator {

    HART69("Hart69"),
    ELDER_MASTER("Eaglow"),
    DARKRAI("Darkrai25"),
    DREAMSVOID("DreamsVoid"),
    SIID("! ༺ -Siid- ༻ !#0001"),
    TYNAGMO("Tynagmo"),
    NOCKS("Nocks");

    private String name;

    Donator(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
