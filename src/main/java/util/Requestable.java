package util;

public class Requestable {

    private String name;
    private String url;

    public Requestable(String name, String url){
        this.name = name;
        this.url = url;
    }

    @Override
    public String toString(){
        return name;
    }

    public String getName(){
        return name;
    }

    public String getUrl(){
        return url;
    }
}
