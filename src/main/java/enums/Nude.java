package enums;

public enum Nude {

    MOAM("Moam#6449", "https://i.imgur.com/0AhFV4F.png"),
    EHLYANA("Ehlyana", "https://i.imgur.com/NklWpTc.jpeg");

    private String author;
    private String image;

    Nude(String author, String image){
        this.author = author;
        this.image = image;
    }

    public String getAuthor(){ return author; }

    public String getImage(){ return image; }
}
