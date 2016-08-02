package commands;

import sx.blah.discord.handle.obj.IMessage;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public abstract class AbstractCommand implements Command {

    protected Pattern name;
    protected Pattern pattern;
    protected Matcher m;

    public AbstractCommand(Pattern name, Pattern pattern){
        super();
        this.name = name;
        this.pattern = pattern;
    }

    @Override
    public boolean request(IMessage message) {
        m =  pattern.matcher(message.getContent());
        return (m.find());
    }

    @Override
    public Pattern getName() {
        return name;
    }
}
