package commands;

import data.ChannelLanguage;
import data.Guild;
import data.User;
import enums.Language;
import exceptions.NotEnoughRightsDiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class LanguageCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(LanguageCommand.class);

    public LanguageCommand(){
        super("lang", "(\\s+-channel)?(\\s+[A-Za-z]+)?");
        setUsableInMP(false);
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            User author = User.getUser(message.getGuild(), message.getAuthor());
            Matcher m = getMatcher(message);
            m.find();

            if (m.group(2) != null) { // Ajouts
                if (author.getRights() >= User.RIGHT_MODERATOR) {
                    List<Language> langs = new ArrayList<>();
                    for(Language lang : Language.values())
                        if (m.group(2).trim().toUpperCase().equals(lang.getAbrev()))
                            langs.add(lang);

                    if (langs.size() == 1) {
                        if (m.group(1) == null) {
                            Guild.getGuild(message.getGuild()).setLanguage(langs.get(0));
                            Message.sendText(message.getChannel(), message.getGuild().getName()
                                    + " est maintenant en " + langs.get(0));
                        } else {
                            ChannelLanguage chan = ChannelLanguage.getChannelLanguages().get(message.getChannel().getLongID());
                            if (chan != null){
                                if (chan.getLang().equals(langs.get(0))){
                                    chan.removeToDatabase();
                                    Message.sendText(message.getChannel(), message.getChannel().getName()
                                            + " se réfère maintenant à la langue du serveur : "
                                            + Guild.getGuild(message.getGuild()).getLanguage());
                                }
                                else {
                                    chan.setLanguage(langs.get(0));
                                    Message.sendText(message.getChannel(), message.getChannel().getName()
                                            + " est désormais en " + chan.getLang());
                                }
                            }
                            else {
                                chan = new ChannelLanguage(langs.get(0), message.getChannel().getLongID());
                                chan.addToDatabase();
                                Message.sendText(message.getChannel(), message.getChannel().getName()
                                        + " est désormais en " + chan.getLang());
                            }
                        }
                    }
                    else if (langs.isEmpty())
                        Message.sendText(message.getChannel(), "Aucune langue correspondante trouvée.");
                    else
                        Message.sendText(message.getChannel(), "Plusieurs langues trouvées. Recommencez en étant plus précis !");

                } else {
                    new NotEnoughRightsDiscordException().throwException(message, this);
                    return false;
                }
            }
            else { // Consultation
                String text = "**" + message.getGuild().getName() + "** est en " + Guild.getGuild(message.getGuild()).getLanguage() + ".";

                ChannelLanguage chanLang = ChannelLanguage.getChannelLanguages().get(message.getChannel().getLongID());
                if (chanLang != null)
                    text += "\nLe salon *" + message.getChannel().getName() + "* est en " + chanLang.getLang() + ".";
                Message.sendText(message.getChannel(), text);
            }
        }
        return false;
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** change la langue utilisée (FR, EN, ES).";
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`"  + name + " `*`language`* : change la langue du serveur"
                + "\n" + prefixe + "`"  + name + " -channel `*`language`* : change la langue du salon spécifié uniquement"
                    + " (prioritaire sur la langue du serveur)\n";
    }
}
