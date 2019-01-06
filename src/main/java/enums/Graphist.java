package enums;

public enum Graphist {

    ELYCANN("Elycann", "https://www.facebook.com/Elysdrawings/");

    private String name;
    private String link;

    Graphist(String name, String link){
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String toMarkdown(){
        return "[" + getName() + "](" + getLink() + ")";
    }
}