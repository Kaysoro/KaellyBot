package enums;

public enum Nude {

    MOAM("Moam#6449 :transgender_flag:", "https://i.imgur.com/0AhFV4F.png", "https://wikitrans.co/intro"),
    EHLYANA("Ehlyana", "https://i.imgur.com/NklWpTc.jpeg", "http://www.ehlyana.com/");

    private final String author;
    private final String image;
    private final String url;

    Nude(String author, String image, String url){
        this.author = author;
        this.image = image;
        this.url = url;
    }

    public String getAuthor(){ return author; }

    public String getImage(){ return image; }

    public String getUrl(){ return url; }
}
