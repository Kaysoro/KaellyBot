package enums;

import data.Constants;

public enum Game {

    DOFUS("Dofus", "https://discordapp.com/oauth2/authorize?&client_id=202916641414184960&scope=bot"),
    DOFUS_TOUCH("Dofus Touch", Constants.invite);

    private String name;
    private String botInvite;

    Game(String name, String botInvite){
        this.name = name;
        this.botInvite = botInvite;
    }

    public String getName() {
        return name;
    }

    public String getBotInvite(){
        return botInvite;
    }
}