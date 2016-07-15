package commands;

import sx.blah.discord.handle.obj.IMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public abstract class AbstractCommand implements Command {

    protected Pattern pattern;
    protected String content;

    public AbstractCommand(){
        super();
        pattern = null;
    }

    @Override
    public boolean request(IMessage message) {
        Matcher m =  pattern.matcher(message.getContent());
        if (m.matches()) {
            content = m.group(2);
            return true;
        }
        return false;
    }
}
