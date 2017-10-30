package enums;

import data.Constants;

public enum SuperTypeResource {

    HAVEN_BAG(Constants.havenBagPageURL),
    HARNESS(Constants.harnessPageURL),
    IDOL(Constants.idolPageURL),
    CONSUMABLE(Constants.consumablePageURL),
    RESOURCE(Constants.resourcePageURL);

    private String url;

    SuperTypeResource(String url){this.url = url;}

    public String getUrl(){
        return url;
    }
}
