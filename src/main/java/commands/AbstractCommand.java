package commands;

import data.Constants;
import exceptions.BadUseCommandDiscordException;
import exceptions.NotUsableInMPDiscordException;
import sx.blah.discord.handle.obj.IMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public abstract class AbstractCommand implements Command {

    protected Pattern name;
    protected Pattern pattern;
    protected Matcher m;
    protected boolean isPublic;
    protected boolean isUsableInMP;
    protected boolean isAdmin;

    protected AbstractCommand(Pattern name, Pattern pattern){
        super();
        this.name = name;
        this.pattern = pattern;
        this.isPublic = true;
        this.isUsableInMP = true;
        this.isAdmin = false;
    }

    @Override
    public boolean request(IMessage message) {
        m =  pattern.matcher(message.getContent());
        boolean isFound = m.find();
        if (isPublic() && ! isAdmin()) {
            if (isFound && message.getChannel().isPrivate() && !isUsableInMP())
                new NotUsableInMPDiscordException().throwException(message, this);
            else if (!isFound && message.getContent().startsWith(Constants.prefixCommand + name.pattern()))
                new BadUseCommandDiscordException().throwException(message, this);
        }
        else if ((! isPublic() || isAdmin()) && ! message.getAuthor().getStringID().equals(Constants.author))
            return false;
        return isFound && (isUsableInMP() || ! message.getChannel().isPrivate());
    }

    @Override
    public Pattern getName() {
        return name;
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public void setPublic(boolean isPublic){
        this.isPublic = isPublic;
    }

    @Override
    public boolean isPublic(){ return isPublic; }

    @Override
    public boolean isUsableInMP(){ return isUsableInMP; }

    @Override
    public void setUsableInMP(boolean isUsableInMP) {
        this.isUsableInMP = isUsableInMP;
    }

    @Override
    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
