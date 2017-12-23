package enums;

import util.Translator;

public enum SuperTypeResource implements SuperType{

    CONSUMABLE("consumable.url"),
    RESOURCE("resource.url");

    private String url;

    SuperTypeResource(String url){this.url = url;}

    public String getUrl(Language lg){
        return Translator.getLabel(lg, url);
    }
}
