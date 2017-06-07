package data;

/**
 * Created by steve on 07/06/2017.
 */
public enum Language {

    FR("French"), EN("English"), SP("Spanish");

    private String name;

    Language(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}
