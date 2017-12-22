package enums;

import util.Translator;

public enum SuperTypeResource implements SuperType{

    //HAVEN_BAG("havenbag.url"),
    HARNESS("harness.url"),
    IDOL("idol.url"),
    CONSUMABLE("consumable.url"),
    RESOURCE("resource.url");

    private String url;

    SuperTypeResource(String url){this.url = url;}

    public String getUrl(Language lg){
        return Translator.getLabel(lg, url);
    }
}
