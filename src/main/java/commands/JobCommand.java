package commands;

import data.Constants;
import exceptions.InDeveloppmentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class JobCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(JobCommand.class);

    public JobCommand(){
        super(Pattern.compile("job"),
        Pattern.compile("^(" + Constants.prefixCommand + "job)\\s+(\\w+)(\\s+\\d{1,3})?$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)){

            //TODO

            new InDeveloppmentException().throwException(message, this);
            return true;
        }

        return false;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "job** renvoit l'annuaire des artisans pour ce métier.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "job` : renvoit l'annuaire des artisans pour ce métier."
                + "\n`" + Constants.prefixCommand + "job `*`niveau`* : vous ajoute à l'annuaire du métier correspondant\n";
    }
}
