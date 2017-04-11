package discord;

import data.ClientConfig;
import data.Constants;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

/**
 * Created by steve on 14/11/2016.
 */
public class Message {

    public static void send(IChannel channel, String content){
        RequestBuffer.request(() -> {
            try {
                new MessageBuilder(ClientConfig.DISCORD())
                        .withChannel(channel)
                        .withContent(content)
                        .build();
            } catch (DiscordException e){
                LoggerFactory.getLogger(Message.class).error(e.getErrorMessage());
            } catch(MissingPermissionsException e){
                LoggerFactory.getLogger(Message.class).warn(Constants.name + " n'a pas les permissions pour appliquer cette requête.");
            } catch(Exception e){
                LoggerFactory.getLogger(Message.class).error(e.getMessage());
            }
            return null;
        });
    }
}
