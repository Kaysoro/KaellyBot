package enums;

public enum Donator {

    HART69("Hart69"),
    ELDER_MASTER("Eaglow"),
    DARKRAI("Darkrai25"),
    DREAMSVOID("DreamsVoid"),
    SIID("Siid"),
    TYNAGMO("Tynagmo"),
    NOCKS("Nocks"),
    SACREE_SACRI("Sacree-Sacri");

    private String name;

    Donator(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
