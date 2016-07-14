package controler;

import data.ClientConfig;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

/**
 * Created by steve on 14/07/2016.
 */
public class ReadyListener {
    @EventSubscriber
    public void onReady(ReadyEvent event) {
        System.out.println(ClientConfig.getIDiscordClient().getChannels(true).toString());
        RequestBuffer.request(() -> {
            try {
                new MessageBuilder(ClientConfig.getIDiscordClient())
                        //.withChannel()
                        .build();

            } catch (DiscordException | MissingPermissionsException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
