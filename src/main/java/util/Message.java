package util;

import data.Constants;
import enums.Language;
import exceptions.MissingPermissionDiscordException;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.*;

/**
 * Created by steve on 14/11/2016.
 */
public class Message {

    public static void sendText(IChannel channel, String content){
        Language lg = Translator.getLanguageFrom(channel);
        RequestBuffer.request(() -> {
            try {
                new MessageBuilder(ClientConfig.DISCORD())
                        .withChannel(channel)
                        .withContent(content)
                        .build();
            } catch(RateLimitException e){
                LoggerFactory.getLogger(Message.class).warn(e.getMessage(),e );
                throw e;
            } catch (DiscordException e){
                ClientConfig.setSentryContext(channel.isPrivate()? null : channel.getGuild(), null, channel, null);
                LoggerFactory.getLogger(Message.class).error(e.getMessage(), e);
            } catch(MissingPermissionsException e){
                LoggerFactory.getLogger(Message.class).warn(Constants.name
                        + " n'a pas les permissions pour appliquer cette requête.");
                new MissingPermissionDiscordException().throwException(channel, lg, e);
            } catch(Exception e){
                ClientConfig.setSentryContext(channel.isPrivate()? null : channel.getGuild(), null, channel, null);
                LoggerFactory.getLogger(Message.class).error(e.getMessage(),e );
            }
            return null;
        });
    }

    public static void sendEmbed(IChannel channel, EmbedObject content){
        Language lg = Translator.getLanguageFrom(channel);
        RequestBuffer.request(() -> {
            try {
                new MessageBuilder(ClientConfig.DISCORD())
                        .withChannel(channel)
                        .withEmbed(content)
                        .build();
            } catch(RateLimitException e){
                LoggerFactory.getLogger(Message.class).warn(e.getMessage(), e);
                throw e;
            } catch (DiscordException e){
                ClientConfig.setSentryContext(channel.isPrivate()? null : channel.getGuild(), null, channel, null);
                LoggerFactory.getLogger(Message.class).error(e.getMessage(), e);
            } catch(MissingPermissionsException e){
                LoggerFactory.getLogger(Message.class).warn(Constants.name
                        + " n'a pas les permissions pour appliquer cette requête.");
                new MissingPermissionDiscordException().throwException(channel, lg, e);
            } catch(Exception e){
                ClientConfig.setSentryContext(channel.isPrivate()? null : channel.getGuild(), null, channel, null);
                LoggerFactory.getLogger(Message.class).error(e.getMessage(), e);
            }
            return null;
        });
    }
}