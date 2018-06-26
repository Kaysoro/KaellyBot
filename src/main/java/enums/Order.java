package enums;

import util.Translator;

import java.util.HashMap;
import java.util.Map;

public enum Order {

    COEUR("order.heart"), ESPRIT("order.spirit"), OEIL("order.eye");

    private static Map<String, Order> orders;
    private String name;

    Order(String name){ this.name = name;}

    public String getName(){
        return name;
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
