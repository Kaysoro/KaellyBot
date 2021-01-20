package enums;

public enum Nude {

    MOAM("Moam#6449", "https://i.imgur.com/BPqxFKm.png", "https://www.twitch.tv/moamstripes"),
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
