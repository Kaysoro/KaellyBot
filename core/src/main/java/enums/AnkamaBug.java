package enums;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;
import util.Translator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public enum AnkamaBug {

    ALLY_NOT_FOUND("ankama.bug.ally_not_found", LocalDate.of(2018, 10, 6)),
    CHARACTER_NOT_FOUND("ankama.bug.character_not_found", LocalDate.of(2018, 10, 6)),
    GHOST_ALLY("ankama.bug.ghost_ally", LocalDate.of(2018, 10, 6)),
    GHOST_CHARACTER("ankama.bug.ghost_character", LocalDate.of(2018, 9, 25)),
    GHOST_GUILD("ankama.bug.ghost_guild", LocalDate.of(2018, 10, 6)),
    GUILD_NOT_FOUND("ankama.bug.guild_not_found", LocalDate.of(2018, 10, 6)),
    ITEM_NOT_FOUND_APOSTROPHE("ankama.bug.item_not_found_apostrophe", LocalDate.of(2018, 10, 6)),
    ITEM_PAGE_MULDO_VOLKORNE_NOT_FOUND("ankama.bug.item_page_muldo_volkorne_not_found", LocalDate.of(2018, 10, 6)),
    MONSTER_RESOURCES_NOT_FOUND("ankama.bug.monster_resources_not_found", LocalDate.of(2018, 10, 6));

    private String bug;
    private LocalDate date;

    AnkamaBug(String bug, LocalDate date){ this.bug = bug; this.date = date;}

    public long getDuration(){
        return ChronoUnit.DAYS.between(date, LocalDate.now());
    }

    public String getLabel(Language lg){
        return Translator.getLabel(lg, bug);
    }

    public EmbedObject getEmbed(String title, Language lg){
        String[] bible = Translator.getLabel(lg, "ankama.bug.bible").split(";");

        EmbedBuilder builder = new EmbedBuilder();
        builder.withTitle(title);
        builder.withDescription(Translator.getLabel(lg, "ankama.bug.description") + "```" + getLabel(lg)
                + "```\n" + Translator.getLabel(lg, "ankama.bug.duration")
                    .replace("{days}", String.valueOf(getDuration())));
        builder.withColor(16711680);
        builder.withImage("https://s.ankama.com/www/static.ankama.com/dofus/ng/modules/mmorpg/encyclopedia/no-result.png");
        builder.withFooterText(bible[new Random().nextInt(bible.length)]);
        return builder.build();
    }
}
