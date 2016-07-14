package controler;

import data.ClientConfig;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

/**
 * Created by steve on 14/07/2016.
 */
public class MessageListener {
        @EventSubscriber
        public void onReady(MessageReceivedEvent event) {
                if (event.getMessage().getContent().equals("!test")) {
                    MessageBuilder builder = new MessageBuilder(ClientConfig
                            .getIDiscordClient())
                            .withChannel(event.getMessage().getChannel());
                        RequestBuffer.request(() -> {
                            try {
                                IMessage msg = builder.withContent(event.getMessage().getAuthor() + " tu m'as parlé ?").build();

                            } catch (DiscordException | MissingPermissionsException e) {
                                e.printStackTrace();
                            }
                            return null;
                        });
                }
        }
}
