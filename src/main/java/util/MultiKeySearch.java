package util;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Collection thread-safe used to do search from tags or keys.
 * Example:
 * <A, B, C> → 1
 * <A, D, E> → 2
 * <F, B, C> → 3
 * if you give <A> you will get {1, 2}
 * if you give <B, C> you will get {1, 3}
 * if you give <A, B>, you will get {1}
 */
public class MultiKeySearch<V> implements Serializable {

    private Map<List<Object>, V>  register;
    private int keysNumber;

    public MultiKeySearch(int keysNumber){
        this.keysNumber = keysNumber;
        register = new ConcurrentHashMap<>();
    }

    /**
     * @param args L'ensemble des clefs partiel ou complet permettant de récupérer la ou les V. L'ensemble des clefs doivent être précisés : si certaines  ne sont pas souhaitées, indiquer la valeur <i>null</i>.
     * @return une collection de V correspondant à l'ensemble de clefs fournis
     * @throws IllegalArgumentException lorsque le nombre de clef passé en paramètre n'équivaut pas au nombre initial
     */
    public List<V> get(Object... args) throws IllegalArgumentException {
        if(args.length != keysNumber)
            throw new IllegalArgumentException("Le nombre de clefs n'est pas identique à celui déclaré : " + keysNumber);

        // On check si l'ensemble de clefs est null
        boolean isNull = true;
        boolean isNotNull = true;
        for(Object arg : args) {
            isNull = isNull && arg == null;
            isNotNull = isNotNull && arg != null;
        }
        if (isNull) return new ArrayList<>(register.values());

        // L'ensemble de clef est soit partielle, soit complet.
        List<V> result = new ArrayList<>();

        // On traite le cas où c'est complet (accès direct)
        if (isNotNull) {
            V value = register.get(Arrays.asList(args));
            if (value != null)
            result.add(value);
            return result;
        }

        // Il reste l'ensemble partiel : pas le choix, il faut parcourir l'ensemble des clefs
        for(List<Object> objs : register.keySet()){
            boolean correspond = true;
            int i = 0;
            for(Object obj : objs){
                if (args[i] != null && ! obj.equals(args[i])){
                    correspond = false;
                    break;
                }
                i++;
            }

            if (correspond)
                result.add(register.get(objs));
        }

        return result;
    }

    /**
     * Ajoute une valeur et ses clefs au sein de la collection
     * @param value Valeur à stocker
     * @param args L'ensemble des clefs permettant de récupérer V. <code>null</code> non permis.
     * @throws IllegalArgumentException lorsque le nombre de clef passé en paramètre n'équivaut pas au nombre initial ou lorsqu'une clef est à null.
     */
    public void add(V value, Object... args) throws IllegalArgumentException {
        if(args.length != keysNumber)
            throw new IllegalArgumentException("Le nombre de clefs n'est pas identique à celui déclaré : " + keysNumber);
        for(Object arg : args)
            if (arg == null)
                throw new IllegalArgumentException("Clef à null interdit.");
        register.put(Arrays.asList(args), value);
    }

    /**
     * Supprime V si les clefs fournis en argument sont présents
     * @param args L'ensemble des clefs permettant de récupérer V
     * @throws IllegalArgumentException lorsque le nombre de clef passé en paramètre n'équivaut pas au nombre initial
     */
    public void remove(Object... args) throws IllegalArgumentException {
        if(args.length != keysNumber)
            throw new IllegalArgumentException("Le nombre de clefs n'est pas identique à celui déclaré : " + keysNumber);
        register.remove(Arrays.asList(args));
    }

    /**
     * @param args L'ensemble des clefs permettant de récupérer V
     * @return True si déjà présent, false le cas échéant
     * @throws IllegalArgumentException lorsque le nombre de clef passé en paramètre n'équivaut pas au nombre initial
     */
    public boolean containsKeys(Object... args) throws IllegalArgumentException {
        if(args.length != keysNumber)
            throw new IllegalArgumentException("Le nombre de clefs n'est pas identique à celui déclaré : " + keysNumber);
        return register.containsKey(Arrays.asList(args));
    }

    /**
     * @return True si aucune clef(s)/valeur n'a été ajouté, false le cas échéant
     */
    public boolean isEmpty(){
        return register.isEmpty();
    }

    /**
     * @return Nombre d'élément dans la collection
     */
    public int size(){
        return register.size();
    }
}