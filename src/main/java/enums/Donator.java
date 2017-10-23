package enums;

public enum Donator {

    IHART69("iHart69#4211", 10);

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
