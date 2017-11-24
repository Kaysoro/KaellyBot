package enums;

public enum Donator {

    IHART69("iHart69#4211", 10),
    ELDER_MASTER("Elder-Master#4090", 10);

    private String name;
    private int amount;

    Donator(String name, int amount){
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
