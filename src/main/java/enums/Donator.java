package enums;

public enum Donator {

    IHART69("Hart69#0001", 10),
    ELDER_MASTER("Elder-Master#7684", 20);

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
