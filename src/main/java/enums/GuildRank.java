package enums;

import util.Translator;

public enum GuildRank {

    LEADER("guild.rank.leader"), SECOND("guild.rank.second");

    private String name;

    GuildRank(String name){ this.name = name;}

    public String getName(){
        return name;
    }

    public String getLabel(Language lg){
        return Translator.getLabel(lg, getName());
    }
}