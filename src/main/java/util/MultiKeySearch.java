package util;

import com.google.common.collect.Multimap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Collection thread-safe used to do seach from tags or keys.
 * Example:
 * <A, B, C> → 1
 * <A, D, E> → 2
 * <F, B, C> → 3
 * if you give <A> you will get {1, 2}
 * if you give <B, C> you will get {1, 3}
 * if you give <A, B>, you will get {1}
 */
public class MultiKeySearch<V> implements Serializable {

    private Map<String, V> collection;
    private Multimap<Object, V>[] multimaps;
    private int keysNumber;

    public MultiKeySearch(int keysNumber){
        this.keysNumber = keysNumber;
        collection = new ConcurrentHashMap<>();
    }

    /**
     * @param args Une ou plusieurs clefs permettant de récupérer la ou les V
     * @return une collection de V correspondant à l'ensemble de clefs fournis
     */
    public List<V> get(Object... args){
        StringBuilder st = new StringBuilder();

        List<V> result = new ArrayList<>();
        //TODO
        return result;
    }

    /**
     * Ajoute une valeur et ses clefs au sein de la collection
     * @param value Valeur à stocker
     * @param args L'ensemble des clefs permettant de récupérer V
     */
    public void add(V value, Object... args){
        if(args.length != keysNumber)
            throw new IllegalArgumentException("keys size is not the ");
        //TODO
    }

    /**
     * Supprime V si les clefs fournis en argument sont présents
     * @param args L'ensemble des clefs permettant de récupérer V
     */
    public void remove(Object... args){
        if(args.length != keysNumber)
            throw new IllegalArgumentException("keys size is not the ");
        //TODO
    }

    /**
     * @param args L'ensemble des clefs permettant de récupérer V
     * @return True si déjà présent, false le cas échéant
     */
    public boolean containsKeys(Object... args){
        if(args.length != keysNumber)
            throw new IllegalArgumentException("keys size is not the ");
        //TODO
        return true;
    }

    /**
     * @return True si aucune clef(s)/valeur n'a été ajouté, false le cas échéant
     */
    public boolean isEmpty(){
        //TODO
        return true;
    }
}