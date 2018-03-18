package util;

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
        IUser user = ClientConfig.DISCORD().getUserByID(Constants.chanReportID);

        if (user != null)
            channel = user.getOrCreatePMChannel();
        else
            channel = ClientConfig.DISCORD().getChannelByID(Constants.chanReportID);

        if (channel == null)
            LOG.warn("Reporter ID is not defined.");
    }

    private static Reporter getInstance(){
        if (instance == null)
            instance = new Reporter();
        return instance;
    }

    public void send(Exception e, Object... others) {
        if (channel != null) {
            StringBuilder st = new StringBuilder(e.toString());

            for(int i = 0; i < e.getStackTrace().length; i++)
                st.append("\n").append(e.getStackTrace()[i]);

            for(Object obj : others)
                st.append("[").append(obj.toString()).append("]");

            try {
                RequestBuffer.request(() -> {
                    new MessageBuilder(ClientConfig.DISCORD())
                            .withChannel(channel)
                            .withContent(st.toString())
                            .build();
                    return null;
                });
            } catch (Exception e1) {
                LoggerFactory.getLogger(Reporter.class).error(e1.getMessage());
            }
        }
    }

    public static void report(Exception e, Object... others){
        Reporter.getInstance().send(e, others);
    }
}