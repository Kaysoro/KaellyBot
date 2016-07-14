package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 14/07/2016.
 */
public class ItemDofus {

    private static List<ItemDofus> items = null;

    public ItemDofus(){
        //TODO
    }

    public static List<ItemDofus> getItems(){
        if (items == null){
            System.out.println("Chargement des items de la base de données");
            items = new ArrayList<ItemDofus>();
            //TODO
            System.out.println("Chargement effectuée avec succès");
        }
        return items;
    }
}

