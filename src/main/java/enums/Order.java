package enums;

import util.Translator;

import java.util.HashMap;
import java.util.Map;

public enum Order {

    COEUR("order.heart", ":heart:"), ESPRIT("order.spirit", ":ghost:"), OEIL("order.eye", ":eye:");

    private static Map<String, Order> orders;
    private String name;
    private String logo;

    Order(String name, String logo){ this.name = name; this.logo = logo;}

    public String getName(){
        return name;
    }

    public String getLogo(){
        return logo;
    }

    public String getLabel(Language lg){
        return Translator.getLabel(lg, getName());
    }

    public static Order getOrder(String name){
        if (orders == null){
            orders = new HashMap<>();
            for(Order order : Order.values())
                orders.put(order.getName(), order);
        }

        if (orders.containsKey(name))
            return orders.get(name);
        throw new IllegalArgumentException("Aucun ordre trouvé pour \"" + name + "\".");
    }
}
