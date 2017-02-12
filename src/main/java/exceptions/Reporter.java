package exceptions;

import data.ClientConfig;
import data.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.RequestBuffer;

/**
 * Created by steve on 12/02/2017.
 */
public class Reporter {

    private final static Logger LOG = LoggerFactory.getLogger(Reporter.class);
    private static Reporter instance = null;
    private IChannel channel;

    private Reporter(){
        IUser user = ClientConfig.CLIENT().getUserByID(Constants.chanReportID);

        if (user != null)
            channel = user.getOrCreatePMChannel();
        else
            channel = ClientConfig.CLIENT().getChannelByID(Constants.chanReportID);

        if (channel == null)
            LOG.warn("Reporter ID is not defined.");
    }

    private static Reporter getInstance(){
        if (instance == null)
            instance = new Reporter();
        return instance;
    }

    public void send(java.lang.Exception e) {
        if (channel != null)
            try {
                RequestBuffer.request(() -> {
                    new MessageBuilder(ClientConfig.CLIENT())
                            .withChannel(channel)
                            .withContent(e.toString())
                            .build();
                    return null;
                });
            } catch (java.lang.Exception e1) {
                LoggerFactory.getLogger(Reporter.class).error(e1.getMessage());
            }
    }

    public static void report(java.lang.Exception e){
        Reporter.getInstance().send(e);
    }
}
