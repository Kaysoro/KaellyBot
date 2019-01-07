package enums;

public enum Nude {

    MOAM("Moam#6449", "http://image.noelshack.com/fichiers/2019/02/1/1546846452-finale.png");

    private String author;
    private String image;

    Nude(String author, String image){
        this.author = author;
        this.image = image;
    }

    public String getAuthor(){ return author; }

    public String getImage(){ return image; }
}
